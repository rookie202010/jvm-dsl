package com.dongjiaqiang.jvm.dsl.api.expression.literal

import com.dongjiaqiang.jvm.dsl.api.`type`.{DslType, FloatType}
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope

class FloatLiteral(literal: Float) extends Literal[Float, FloatType.type]( literal ) with BaseLiteral {
  override val dslType: FloatType.type = FloatType

  override def toString: String = s"${literal.toString}f"

  override def equals(obj: Any): Boolean = {
    obj match {
      case floatLiteral: FloatLiteral ⇒ floatLiteral.literal == literal
      case _ ⇒ false
    }
  }

  override def getValueType(programScope: ProgramScope): FloatType.type = FloatType
}
