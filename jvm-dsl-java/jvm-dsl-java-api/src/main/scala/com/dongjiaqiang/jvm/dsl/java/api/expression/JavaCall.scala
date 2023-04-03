package com.dongjiaqiang.jvm.dsl.java.api.expression

import com.dongjiaqiang.jvm.dsl.api.`type`.DslType
import com.dongjiaqiang.jvm.dsl.api.expression.{Expression, ValueExpression}
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope

/**
 * expression repression java code call
 */
case class JavaCall(javaCode: JavaCode, name: String, params: Array[Expression], dslType: DslType) extends ValueExpression{
  override def getValueType(programScope: ProgramScope): DslType = dslType
}
