package com.dongjiaqiang.jvm.dsl.api.`type`.mrt

import com.dongjiaqiang.jvm.dsl.api.`type`.{BoolType, DslType, EitherType, LambdaType, LeftType, RightType, TryType}
import com.dongjiaqiang.jvm.dsl.api.`type`.visitor.EitherMethodVisitor
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope

class EitherMrt(override val programScope: ProgramScope) extends MonadMrt with EitherMethodVisitor[DslType] {
  override def left(calleeType: EitherType, callee: ValueExpression): DslType = calleeType.leftParameterType

  override def right(calleeType: EitherType, callee: ValueExpression): DslType = calleeType.rightParameterType

  override def isLeft(calleeType: EitherType, callee: ValueExpression): DslType = BoolType

  override def isRight(calleeType: EitherType, callee: ValueExpression): DslType = BoolType

  override def toLeft(calleeType: EitherType, callee: ValueExpression): DslType = LeftType(calleeType.leftParameterType,calleeType.rightParameterType)

  override def toRight(calleeType: EitherType, callee: ValueExpression): DslType = RightType(calleeType.leftParameterType,calleeType.rightParameterType)

  override def mapLeft(calleeType: EitherType, callee: ValueExpression, function: ValueExpression): DslType = {
    val lambdaType = function.getValueType( programScope ).asInstanceOf[LambdaType]
    new EitherType(lambdaType.outputType,calleeType.rightParameterType)
  }

  override def fold(calleeType: EitherType, callee: ValueExpression, functionL: ValueExpression, functionR: ValueExpression): DslType = {
      functionL.getValueType(programScope)
  }

  override def orElse(calleeType: EitherType, callee: ValueExpression, param: ValueExpression): DslType = {
    calleeType.rightParameterType
  }

  override def swap(calleeType: EitherType, callee: ValueExpression): DslType = new EitherType(calleeType.rightParameterType,calleeType.leftParameterType)

  override def toTry(calleeType: EitherType, callee: ValueExpression): DslType = new TryType(calleeType.rightParameterType)
}
