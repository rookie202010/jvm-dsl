package com.dongjiaqiang.jvm.dsl.java.api.expression

import com.dongjiaqiang.jvm.dsl.api.`type`.DslType
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope

case class JavaVarRef(name: List[String],returnType:DslType) extends ValueExpression{
  override def getValueType(programScope: ProgramScope): DslType = returnType
}
