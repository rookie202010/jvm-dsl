package com.dongjiaqiang.jvm.dsl.core.expression.visitor.literal

import com.dongjiaqiang.jvm.dsl.core.expression._

/**
 * @author: rookie
 * @mail: dongjiaqiang@qiniu.com
 * @date: 2022/12/23
 * */
trait LiteralExpressionVisitor[T] {

  def visitInt(literal: IntLiteral): T

  def visitLong(literal: LongLiteral): T

  def visitDouble(literal: DoubleLiteral): T

  def visitFloat(literal: FloatLiteral): T

  def visitBool(literal: BoolLiteral): T

  def visitString(literal: StringLiteral): T

  def visitChar(literal: CharLiteral): T

  def visitList(literal: ListLiteral): T

  def visitMap(literal: MapLiteral): T

  def visitSet(literal: SetLiteral): T

  def visitTuple(literal: TupleLiteral): T

  def visitClazz(literal: ClazzLiteral): T

  def visitOption(literal: OptionLiteral): T
}
