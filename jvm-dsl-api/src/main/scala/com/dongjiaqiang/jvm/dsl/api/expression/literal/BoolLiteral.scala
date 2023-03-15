package com.dongjiaqiang.jvm.dsl.api.expression.literal

import com.dongjiaqiang.jvm.dsl.api.`type`.{BoolType, DslType}
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope

class BoolLiteral(literal: Boolean) extends Literal[Boolean, BoolType.type]( literal ) with BaseLiteral {
  override val dslType: BoolType.type = BoolType

  override def toString: String = literal.toString

  override def equals(obj: Any): Boolean = {
    obj match {
      case boolLiteral: BoolLiteral ⇒ boolLiteral.literal == literal
      case _ ⇒ false
    }
  }

  override def getValueType(programScope: ProgramScope): DslType = BoolType
}
