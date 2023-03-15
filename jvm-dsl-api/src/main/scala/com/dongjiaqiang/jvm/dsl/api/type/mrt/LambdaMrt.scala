package com.dongjiaqiang.jvm.dsl.api.`type`.mrt

import com.dongjiaqiang.jvm.dsl.api.`type`.visitor.LambdaMethodVisitor
import com.dongjiaqiang.jvm.dsl.api.`type`.{DslType, LambdaType}
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope

class LambdaMrt(override val programScope: ProgramScope) extends LambdaMethodVisitor[DslType] {
  override def get(calleeType: LambdaType,
                   callee: ValueExpression): DslType = calleeType.outputType

  override def apply(calleeType: LambdaType,
                     callee: ValueExpression,
                     params: Array[ValueExpression]): DslType = calleeType.outputType

  override def apply(calleeType: LambdaType,
                     callee: ValueExpression,
                     param: ValueExpression): DslType = calleeType.outputType

  override def supply(calleeType: LambdaType,
                      callee: ValueExpression,
                      params: Array[ValueExpression]): DslType = calleeType.outputType

  override def supply(calleeType: LambdaType,
                      callee: ValueExpression,
                      param: ValueExpression): DslType = calleeType.outputType
}
