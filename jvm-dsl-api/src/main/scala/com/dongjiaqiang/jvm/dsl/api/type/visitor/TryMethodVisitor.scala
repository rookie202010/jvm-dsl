package com.dongjiaqiang.jvm.dsl.api.`type`.visitor

import com.dongjiaqiang.jvm.dsl.api.`type`.{DslType, TryType}
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression

/**
 * TryType method visitor
 */
trait TryMethodVisitor[T] extends MonadMethodVisitor[T] with MethodVisitor[T] {

  def getException(calleeType: TryType, callee: ValueExpression): T

  def get(calleeType: TryType, callee: ValueExpression): T

  def isSuccess(calleeType: TryType): T

  def isFailure(calleeType: TryType): T

  override def visit(calleeType: DslType,
                     callee: ValueExpression,
                     name: String, params:
                     Array[ValueExpression]): Option[T] = ???
}
