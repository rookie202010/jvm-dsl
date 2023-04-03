package com.dongjiaqiang.jvm.dsl.java.api.expression

import com.dongjiaqiang.jvm.dsl.api.`type`.{DslType, FutureType}
import com.dongjiaqiang.jvm.dsl.api.expression.{Expression, ValueExpression}
import com.dongjiaqiang.jvm.dsl.api.expression.block.Block
import com.dongjiaqiang.jvm.dsl.api.scope.{FieldScope, ProgramScope}

case class JavaAsync(body:Block, executor: Option[FieldScope], returnType: FutureType) extends ValueExpression{
  override def getValueType(programScope: ProgramScope): FutureType = returnType
}
