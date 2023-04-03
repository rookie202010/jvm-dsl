package com.dongjiaqiang.jvm.dsl.core.optimize

import com.dongjiaqiang.jvm.dsl.api.`type`.TryType
import com.dongjiaqiang.jvm.dsl.api.`type`.visitor.TryMethodVisitor
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope

class TryOptimize(override val programScope: ProgramScope,
                   override val optimizeDslType: OptimizeDslType)
  extends MonadOptimize(programScope,optimizeDslType) with TryMethodVisitor[Array[ValueExpression]]{
  override def getException(calleeType: TryType, callee: ValueExpression): Array[ValueExpression] = Array()

  override def get(calleeType: TryType, callee: ValueExpression): Array[ValueExpression] = Array()

  override def isSuccess(calleeType: TryType, callee: ValueExpression): Array[ValueExpression] = Array()

  override def isFailure(calleeType: TryType, callee: ValueExpression): Array[ValueExpression] = Array()

  override def fold(calleeType: TryType, callee: ValueExpression, function1: ValueExpression,
                    function2: ValueExpression): Array[ValueExpression] = {
    Array(transform(calleeType,function1),transform(calleeType,function2))
  }

  override def orElse(calleeType: TryType, callee: ValueExpression, param: ValueExpression): Array[ValueExpression] =  Array(transform(calleeType,param))

  override def getOrElse(calleeType: TryType, callee: ValueExpression, param: ValueExpression): Array[ValueExpression] =  Array(transform(calleeType,param))
}
