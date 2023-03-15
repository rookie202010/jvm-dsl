package com.dongjiaqiang.jvm.dsl.api.`type`.mrt

import com.dongjiaqiang.jvm.dsl.api.`type`.visitor.OptionMethodVisitor
import com.dongjiaqiang.jvm.dsl.api.`type`.{BoolType, DslType, OptionType}
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope

class OptionMrt(override val programScope: ProgramScope) extends MonadMrt with OptionMethodVisitor[DslType]{
  override def get(calleeType: OptionType,
                   callee: ValueExpression): DslType = calleeType.carryDslType

  override def getOrElse(calleeType: OptionType,
                         callee: ValueExpression,
                         default: ValueExpression): DslType = calleeType.carryDslType

  override def isDefine(calleeType: OptionType,
                        callee: ValueExpression): DslType = BoolType

  override def isEmpty(calleeType: OptionType,
                       callee: ValueExpression): DslType = BoolType
}
