package com.dongjiaqiang.jvm.dsl.api.expression.literal

import com.dongjiaqiang.jvm.dsl.api.`type`.DslType
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression

abstract class Literal[T, D <: DslType](val literal: T) extends ValueExpression {
  val dslType: D
}

trait BaseLiteral extends ValueExpression

object Literal {
  def literal(literal: Int): IntLiteral = new IntLiteral( literal )

  def literal(literal: Long): LongLiteral = new LongLiteral( literal )

  def literal(literal: Float): FloatLiteral = new FloatLiteral( literal )

  def literal(literal: Double): DoubleLiteral = new DoubleLiteral( literal )

  def literal(literal: Boolean): BoolLiteral = new BoolLiteral( literal )

  def literal(literal: String): StringLiteral = new StringLiteral( literal )

  def literal(literal: Char): CharLiteral = new CharLiteral( literal )
}