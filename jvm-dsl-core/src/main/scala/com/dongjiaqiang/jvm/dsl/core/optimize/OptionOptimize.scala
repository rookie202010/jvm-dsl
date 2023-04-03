package com.dongjiaqiang.jvm.dsl.core.optimize

import com.dongjiaqiang.jvm.dsl.api.`type`.OptionType
import com.dongjiaqiang.jvm.dsl.api.`type`.visitor.OptionMethodVisitor
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope

class OptionOptimize(override val programScope: ProgramScope,
                     override val optimizeDslType: OptimizeDslType)
  extends MonadOptimize(programScope,optimizeDslType) with OptionMethodVisitor[Array[ValueExpression]]{
  override def get(calleeType: OptionType, callee: ValueExpression): Array[ValueExpression] = Array()

  override def getOrElse(calleeType: OptionType, callee: ValueExpression, default: ValueExpression): Array[ValueExpression] = {
    Array(transform(calleeType,default))
  }

  override def orElse(calleeType: OptionType, callee: ValueExpression, default: ValueExpression): Array[ValueExpression] = {
    Array(transform(calleeType,default))
  }

  override def isDefine(calleeType: OptionType, callee: ValueExpression): Array[ValueExpression] =  Array()
}
