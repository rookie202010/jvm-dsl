package com.dongjiaqiang.jvm.dsl.api.`type`.visitor

import com.dongjiaqiang.jvm.dsl.api.`type`.{DslType, MonadDslType, SetType}
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression

/**
 * SetType method visitor
 */
trait SetMethodVisitor[T] extends MonadMethodVisitor[T] with MethodVisitor[T] {

  def add(calleeDslType: SetType, callee: ValueExpression, element: ValueExpression): T

  def addAll(calleeDslType: SetType, callee: ValueExpression, element: ValueExpression): T

  def withSort(calleeDslType: SetType, callee: ValueExpression): T

  def withSeq(calleeDslType: SetType, callee: ValueExpression): T

  override def visit(calleeType: DslType, callee: ValueExpression, name: String, params: Array[ValueExpression]): Option[T] = {
    val setType = calleeType.asInstanceOf[SetType]
    name match {
      case "add" ⇒
        val (actualDslTypes, msg) = actualTypes( programScope, params, name, calleeType, Array( setType.carryDslType.toString ) )
        require( params.length == 1 && setType.carryDslType.isSuperDslType( programScope.importManager, actualDslTypes.head ), msg )
        Some( add( setType, callee, params.head ) )
      case "addAll" ⇒
        val (actualDslTypes, msg) = actualTypes( programScope, params, name, calleeType, Array( s"MonadType[${setType.carryDslType.toString}]" ) )
        require( params.length == 1 && actualDslTypes.head.isInstanceOf[MonadDslType] &&
          setType.carryDslType.isSuperDslType( programScope.importManager, actualDslTypes.head.asInstanceOf[MonadDslType].carryDslType ), msg )
        Some( addAll( setType, callee, params.head ) )
      case "asSort" ⇒
        val (_, msg) = actualTypes( programScope, params, name, calleeType, Array( ) )
        require( params.isEmpty, msg )
        Some( withSort( setType, callee ) )
      case "asSeq" ⇒
        val (_, msg) = actualTypes( programScope, params, name, calleeType, Array( ) )
        require( params.isEmpty, msg )
        Some( withSeq( setType, callee ) )
      case _ ⇒ super.visit( calleeType, callee, name, params )
    }

  }
}
