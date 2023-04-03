package com.dongjiaqiang.jvm.dsl.api.`type`.mrt

import com.dongjiaqiang.jvm.dsl.api.`type`.visitor.TryMethodVisitor
import com.dongjiaqiang.jvm.dsl.api.`type`.{ArrayType, BoolType, ClazzType, DslType, LambdaType, MonadDslType, ThrowableType, TryType}
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope

class TryMrt(override val programScope: ProgramScope) extends MonadMrt with TryMethodVisitor[DslType] {
  override def getException(calleeType: TryType, callee: ValueExpression): DslType = {
    ThrowableType
  }


  override def fold(calleeType: TryType,
                    callee: ValueExpression,
                    function1: ValueExpression,
                    function2: ValueExpression): DslType = {
    val lambdaType = function1.getValueType( programScope ).asInstanceOf[LambdaType]
    calleeType.transform( lambdaType.outputType )
  }

  override def get(calleeType: TryType, callee: ValueExpression): DslType = calleeType.carryDslType

  override def isSuccess(calleeType: TryType, callee: ValueExpression): DslType = BoolType

  override def isFailure(calleeType: TryType, callee: ValueExpression): DslType = BoolType

  override def orElse(calleeType: TryType, callee: ValueExpression, param: ValueExpression): DslType = calleeType.carryDslType

  override def getOrElse(calleeType: TryType, callee: ValueExpression, param: ValueExpression): DslType = calleeType.carryDslType
}
