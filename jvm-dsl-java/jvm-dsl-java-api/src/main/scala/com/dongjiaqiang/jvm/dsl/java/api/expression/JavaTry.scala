package com.dongjiaqiang.jvm.dsl.java.api.expression

import com.dongjiaqiang.jvm.dsl.api.`type`.{DslType, TryType}
import com.dongjiaqiang.jvm.dsl.api.expression.{Expression, ValueExpression}
import com.dongjiaqiang.jvm.dsl.api.expression.block.Block
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope

case class JavaTry(body:Block, returnType: TryType) extends ValueExpression{
  override def getValueType(programScope: ProgramScope): TryType = returnType
}
