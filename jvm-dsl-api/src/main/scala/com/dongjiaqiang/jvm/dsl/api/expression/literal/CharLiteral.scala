package com.dongjiaqiang.jvm.dsl.api.expression.literal

import com.dongjiaqiang.jvm.dsl.api.`type`.{CharType, DslType}
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope

class CharLiteral(literal: Char) extends Literal[Char, CharType.type]( literal ) with BaseLiteral {
  override val dslType: CharType.type = CharType

  override def toString: String = s"\'$literal\'"

  override def equals(obj: Any): Boolean = {
    obj match {
      case charLiteral: CharLiteral ⇒ charLiteral.literal == literal
      case _ ⇒ false
    }
  }

  override def getValueType(programScope: ProgramScope): DslType = CharType
}
