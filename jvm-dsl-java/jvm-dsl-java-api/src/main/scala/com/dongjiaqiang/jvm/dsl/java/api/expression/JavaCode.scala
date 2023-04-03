package com.dongjiaqiang.jvm.dsl.java.api.expression

import com.dongjiaqiang.jvm.dsl.api.`type`.DslType
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope

/**
 *  expression represent a piece of java code
 */
case class JavaCode(code:String,returnType:DslType) extends ValueExpression{
  override def getValueType(programScope: ProgramScope): DslType = returnType
}


