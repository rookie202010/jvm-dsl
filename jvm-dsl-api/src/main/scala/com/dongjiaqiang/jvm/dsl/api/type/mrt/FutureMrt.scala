package com.dongjiaqiang.jvm.dsl.api.`type`.mrt

import com.dongjiaqiang.jvm.dsl.api.`type`.{BoolType, DslType, FutureType, UnitType}
import com.dongjiaqiang.jvm.dsl.api.`type`.visitor.FutureMethodVisitor
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope

class FutureMrt(override val programScope: ProgramScope) extends MonadMrt with FutureMethodVisitor[DslType]{
  override def isComplete(calleeType: FutureType, callee: ValueExpression): DslType = BoolType

  override def isCancelled(calleeType: FutureType, callee: ValueExpression): DslType = BoolType

  override def isCompleteExceptionally(calleeType: FutureType, callee: ValueExpression): DslType = BoolType

  override def get(calleeType: FutureType, callee: ValueExpression): DslType = calleeType.carryDslType

  override def getNow(calleeType: FutureType, callee: ValueExpression, valueIfAbsent: ValueExpression): DslType = calleeType.carryDslType

  override def get(calleeType: FutureType, callee: ValueExpression, timeout: ValueExpression, timeUnit: ValueExpression): DslType = calleeType.carryDslType

  override def complete(calleeType: FutureType, callee: ValueExpression, value: ValueExpression): DslType = UnitType

  override def completeExceptionally(calleeType: FutureType, callee: ValueExpression, value: ValueExpression): DslType = UnitType
}
