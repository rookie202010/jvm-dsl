package com.dongjiaqiang.jvm.dsl.api.expression.literal

import com.dongjiaqiang.jvm.dsl.api.`type`.{DslType, StringType}
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope

class StringLiteral(literal: String) extends Literal[String, StringType.type]( literal ) with BaseLiteral {
  override val dslType: StringType.type = StringType

  override def toString: String =
    s"""
       |"$literal"
       |""".stripMargin

  override def equals(obj: Any): Boolean = {
    obj match {
      case stringLiteral: StringLiteral ⇒ stringLiteral.literal == literal
      case _ ⇒ false
    }
  }

  override def getValueType(programScope: ProgramScope): DslType = StringType
}
