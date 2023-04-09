package com.dongjiaqiang.jvm.dsl.api.`type`.visitor
import com.dongjiaqiang.jvm.dsl.api.`type`.{AnyType, DslType, UnitType}
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression

import scala.util.Success


/**
 * AnyType method visitor
 */
trait AnyMethodVisitor[T] extends MethodVisitor[T] {

  def hashCode(callee: ValueExpression):T

  def equals(callee: ValueExpression,param:ValueExpression):T

  def toString(callee: ValueExpression):T
  override def visit(calleeDslType:DslType,callee: ValueExpression, name: String, params: Array[ValueExpression]): Option[T] = {
    (name match {
      case MethodVisitor.HASH_CODE ⇒
        generate(params,()⇒ hashCode(callee))
      case MethodVisitor.EQUALS⇒
        generate(Array(AnyType),params,()⇒equals(callee,params.head))
      case MethodVisitor.TO_STRING ⇒
        generate(params,()⇒toString(callee))
        Some(toString(callee))
      case _⇒
        None
    }).orElse(super.visit(calleeDslType,callee, name, params))
  }
}



