package com.dongjiaqiang.jvm.dsl.api.`type`.visitor

import com.dongjiaqiang.jvm.dsl.api.`type`.{DslType, MapType}
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression

/**
 * MapType method visitor
 */
trait MapMethodVisitor[T] extends MonadMethodVisitor[T] with MethodVisitor[T] {

  def keys(calleeType: MapType, callee: ValueExpression): T

  def values(calleeType: MapType, callee: ValueExpression): T

  def containKey(calleeType: MapType, callee: ValueExpression, key: ValueExpression): T

  def get(calleeType: MapType, callee: ValueExpression, key: ValueExpression): T

  def put(calleeType: MapType, callee: ValueExpression, key: ValueExpression, value: ValueExpression): T

  override def visit(calleeType: DslType, callee: ValueExpression, name: String, params: Array[ValueExpression]): Option[T] = {
    val mapType = calleeType.asInstanceOf[MapType]
    name match {
      case "keys" ⇒
        val (_, msg) = actualTypes( programScope, params, name, calleeType, Array( ) )
        require( params.isEmpty, msg )
        Some( keys( mapType, callee ) )
      case "values" ⇒
        val (_, msg) = actualTypes( programScope, params, name, calleeType, Array( ) )
        require( params.isEmpty, msg )
        Some( values( mapType, callee ) )
      case "containKey" | "get" ⇒
        val (actualDslTypes, msg) = actualTypes( programScope, params, name, calleeType, Array( mapType.keyParameterType.toString ) )
        require( params.length == 1 && mapType.keyParameterType.isSuperDslType( programScope.importManager, actualDslTypes.head ), msg )
        if (name == "containKey") {
          Some( containKey( mapType, callee, params.head ) )
        } else {
          Some( get( mapType, callee, params.head ) )
        }
      case "put" ⇒
        val (actualDslTypes, msg) = actualTypes( programScope, params, name, calleeType, Array( mapType.keyParameterType.toString, mapType.valueParameterType.toString ) )
        require( params.length == 2 &&
          mapType.keyParameterType.isSuperDslType( programScope.importManager, actualDslTypes.head ) &&
          mapType.valueParameterType.isSuperDslType( programScope.importManager, actualDslTypes.last ), msg )
        Some( put( mapType, callee, params.head, params.last ) )
      case _ ⇒
        super.visit( calleeType, callee, name, params )
    }
  }
}
