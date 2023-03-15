package com.dongjiaqiang.jvm.dsl.api.expression.literal

import com.dongjiaqiang.jvm.dsl.api.`type`.{DoubleType, DslType}
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope

class DoubleLiteral(literal: Double) extends Literal[Double, DoubleType.type]( literal ) with BaseLiteral {
  override val dslType: DoubleType.type = DoubleType

  override def toString: String = s"${literal.toString}d"

  override def equals(obj: Any): Boolean = {
    obj match {
      case doubleLiteral: DoubleLiteral ⇒ doubleLiteral.literal == literal
      case _ ⇒ false
    }
  }

  override def getValueType(programScope: ProgramScope): DslType = DoubleType
}
