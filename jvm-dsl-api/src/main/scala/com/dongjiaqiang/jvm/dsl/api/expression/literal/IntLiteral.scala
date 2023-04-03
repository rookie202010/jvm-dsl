package com.dongjiaqiang.jvm.dsl.api.expression.literal

import com.dongjiaqiang.jvm.dsl.api.`type`.{DslType, IntType}
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope

class IntLiteral(literal: Int) extends Literal[Int, IntType.type]( literal ) with BaseLiteral {
  override val dslType: IntType.type = IntType

  override def toString: String = literal.toString

  override def equals(obj: Any): Boolean = {
    obj match {
      case intLiteral: IntLiteral ⇒ intLiteral.literal == literal
      case _ ⇒ false
    }
  }

  override def getValueType(programScope: ProgramScope): IntType.type = IntType
}
