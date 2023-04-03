package com.dongjiaqiang.jvm.dsl.api.expression.literal

import com.dongjiaqiang.jvm.dsl.api.`type`.{DslType, LongType}
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope

class LongLiteral(literal: Long) extends Literal[Long, LongType.type]( literal ) with BaseLiteral {
  override val dslType: LongType.type = LongType

  override def toString: String = s"${literal.toString}l"

  override def equals(obj: Any): Boolean = {
    obj match {
      case longLiteral: LongLiteral ⇒ longLiteral.literal == literal
      case _ ⇒ false
    }
  }

  override def getValueType(programScope: ProgramScope): LongType.type = LongType
}
