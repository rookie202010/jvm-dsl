package com.dongjiaqiang.jvm.dsl.java.api.expression

import com.dongjiaqiang.jvm.dsl.api.`type`.DslType
import com.dongjiaqiang.jvm.dsl.api.expression.{Expression, ValueExpression}
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope

case class JavaMatchCase(matchedType: DslType, code: String) extends ValueExpression{
  override def getValueType(programScope: ProgramScope): DslType = matchedType
}
