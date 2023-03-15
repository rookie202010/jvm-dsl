package com.dongjiaqiang.jvm.dsl.api.`type`.visitor
import com.dongjiaqiang.jvm.dsl.api.`type`.DslType
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression


/**
 * AnyType method visitor
 */
trait AnyMethodVisitor[T] extends MethodVisitor[T] {

  def hashCode(callee: ValueExpression):T

  def equals(callee: ValueExpression,param:ValueExpression):T

  def toString(callee: ValueExpression):T
  override def visit(calleeDslType:DslType,callee: ValueExpression, name: String, params: Array[ValueExpression]): Option[T] = {
    name match {
      case "hashCode" ⇒ Some(hashCode(callee))
      case "equals"⇒ Some(equals(callee,params.head))
      case "toString" ⇒ Some(toString(callee))
      case _⇒super.visit(calleeDslType,callee, name, params)
    }
  }
}



