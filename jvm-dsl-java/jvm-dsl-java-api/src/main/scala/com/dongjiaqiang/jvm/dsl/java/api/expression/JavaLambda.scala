package com.dongjiaqiang.jvm.dsl.java.api.expression

import com.dongjiaqiang.jvm.dsl.api.`type`.{DslType, LambdaType}
import com.dongjiaqiang.jvm.dsl.api.expression.{Expression, ValueExpression}
import com.dongjiaqiang.jvm.dsl.api.expression.block.Lambda
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope

case class JavaLambda(dslType: LambdaType, lambda: Lambda) extends ValueExpression{
  override def getValueType(programScope: ProgramScope): LambdaType = dslType
}
