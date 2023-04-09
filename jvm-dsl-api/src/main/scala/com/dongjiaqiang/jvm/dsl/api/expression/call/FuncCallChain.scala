package com.dongjiaqiang.jvm.dsl.api.expression.call

import com.dongjiaqiang.jvm.dsl.api.`type`.DslType
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.expression.`var`.VarRef
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope

import scala.annotation.tailrec
import scala.collection.mutable.ArrayBuffer

case class WrapValueTypeExpression(valueType:DslType) extends ValueExpression{
  override def getValueType(programScope: ProgramScope): DslType = valueType
}

trait Part

object FuncCallChain {

  @tailrec
  def getValueType(programScope: ProgramScope, calleeType: DslType, tails: List[Part]): DslType = {
    tails match {
      case Nil⇒
          calleeType
      case head :: tail ⇒
        head match {
          case varRef: VarRef ⇒
            getValueType( programScope, VarRef.getValueType( varRef.refs.zipWithIndex, varRef.arrayRefIndexExpressions, calleeType, programScope,varRef ), tail )
          case methodCall: MethodCall ⇒
            val dslType = programScope.multiMrt.visit( calleeType, WrapValueTypeExpression( calleeType ), methodCall.name, methodCall.params )
            getValueType( programScope, dslType.get, tail )
        }
      case head :: Nil ⇒
        head match {
          case varRef: VarRef ⇒ VarRef.getValueType( varRef.refs.zipWithIndex, varRef.arrayRefIndexExpressions, calleeType, programScope,varRef )
          case methodCall: MethodCall ⇒
            programScope.multiMrt.visit( calleeType, WrapValueTypeExpression( calleeType ), methodCall.name, methodCall.params ).get
        }
    }

  }
}
/**
 * <pre><code>
 * program{
 *    def method()=Unit{
 *      foo().a.bar(); // foo().a.bar() => MethodCall chain
 *      a.foo().b.a.bar(); // a.foo().b.a.bar() => VarCall chain
 *      A.foo().b.bar().a; // A.foo().b.bar().a => StaticCall chain
 *      "xxx".size(); // "xxx".size() => LiteralCall chain
 *    }
 * }
 * <pre><code>
 */
case class FuncCallChain(head: FuncCall, tails: List[Part],tailDslTypes:List[DslType] = List()) extends ValueExpression {

  override def toString: String = s"$head.${tails.mkString( "." )}"

  override def equals(obj: Any): Boolean = obj match {
    case funcCallChain: FuncCallChain ⇒
      funcCallChain.head == head && funcCallChain.tails == tails && funcCallChain.tailDslTypes == tailDslTypes
    case _ ⇒ false
  }

  override def getValueType(programScope: ProgramScope): DslType = {
    val calleeType = head.getValueType( programScope )
    getValueType( programScope, calleeType, tails )
  }

  def getValueType(programScope: ProgramScope, calleeType: DslType, tails: List[Part]): DslType = {
      FuncCallChain.getValueType(programScope, calleeType, tails)
  }
}
