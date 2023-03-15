package com.dongjiaqiang.jvm.dsl.api.`type`.visitor

import com.dongjiaqiang.jvm.dsl.api.`type`.OptionType
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression

trait OptionMethodVisitor[T] extends MonadMethodVisitor[T] with MethodVisitor[T] {

  def get(calleeType: OptionType, callee: ValueExpression): T

  def getOrElse(calleeType: OptionType, callee: ValueExpression, default: ValueExpression): T

  def isDefine(calleeType: OptionType, callee: ValueExpression): T

  def isEmpty(calleeType: OptionType, callee: ValueExpression): T
}
