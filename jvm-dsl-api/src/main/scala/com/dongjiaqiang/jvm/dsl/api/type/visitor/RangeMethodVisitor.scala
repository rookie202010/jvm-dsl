package com.dongjiaqiang.jvm.dsl.api.`type`.visitor

import com.dongjiaqiang.jvm.dsl.api.`type`.DslType
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression

trait RangeMethodVisitor[T] extends MethodVisitor[T] {

  def to(callee: ValueExpression, end: ValueExpression): T

  def to(callee: ValueExpression, end: ValueExpression, step: ValueExpression): T

  def until(callee: ValueExpression, end: ValueExpression): T

  def until(callee: ValueExpression, end: ValueExpression, step: ValueExpression): T

  override def visit(calleeType: DslType, callee: ValueExpression, name: String, params: Array[ValueExpression]): Option[T] = {
    (name match {
      case MethodVisitor.TO⇒
        generateOverload(Array( Array( calleeType ), Array( calleeType, calleeType ) ), params,()⇒{
          if(params.length==1){
              to(callee,params.head)
          }else{
              to(callee,params.head,params.last)
          }
        })
      case MethodVisitor.UNTIL⇒
        generateOverload( Array( Array( calleeType ), Array( calleeType, calleeType ) ), params, () ⇒ {
          if (params.length == 1) {
            until( callee, params.head )
          } else {
            until( callee, params.head, params.last )
          }
        } )
      case _⇒
          None
    }).orElse(super.visit(calleeType,callee, name, params))

  }
}


