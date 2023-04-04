package com.dongjiaqiang.jvm.dsl.api.`type`.visitor

import com.dongjiaqiang.jvm.dsl.api.`type`.{DslType, MapType}
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.`type`.visitor.MethodVisitor._
/**
 * MapType method visitor
 */
trait MapMethodVisitor[T] extends MonadMethodVisitor[T] {

  def keys(calleeType: MapType, callee: ValueExpression): T

  def values(calleeType: MapType, callee: ValueExpression): T

  def containKey(calleeType: MapType, callee: ValueExpression, key: ValueExpression): T

  def get(calleeType: MapType, callee: ValueExpression, key: ValueExpression): T

  def put(calleeType: MapType, callee: ValueExpression, key: ValueExpression, value: ValueExpression): T

  override def visit(calleeType: DslType, callee: ValueExpression, name: String, params: Array[ValueExpression]): Option[T] = {
    val mapType = calleeType.asInstanceOf[MapType]
    (name match {
      case KEYS ⇒
        generate(params,()⇒keys( mapType, callee ) )
      case VALUES ⇒
        generate(params,()⇒ values( mapType, callee ) )
      case CONTAIN_KEY | GET ⇒
        generate(Array(mapType.keyParameterType),params,()⇒{
          if (name == CONTAIN_KEY) {
            containKey( mapType, callee, params.head )
          } else {
           get( mapType, callee, params.head )
          }
        })
      case MAP_VALUE⇒
        Some(mapValue(mapType,callee,params.head))
      case PUT ⇒
        generate(Array(mapType.keyParameterType,mapType.valueParameterType),params,()⇒ put( mapType, callee, params.head, params.last ) )
      case _ ⇒
        None
    }).orElse(super.visit( calleeType, callee, name, params ))
  }
}
