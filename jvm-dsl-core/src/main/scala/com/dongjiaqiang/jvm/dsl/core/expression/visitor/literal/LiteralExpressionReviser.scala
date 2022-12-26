package com.dongjiaqiang.jvm.dsl.core.expression.visitor.literal

import com.dongjiaqiang.jvm.dsl.core.expression.{BoolLiteral, CharLiteral, ClazzLiteral, DoubleLiteral, Expression, FloatLiteral, IntLiteral, ListLiteral, LongLiteral, MapLiteral, OptionLiteral, SetLiteral, StringLiteral, TupleLiteral}

/**
 * @author: rookie
 * @mail: dongjiaqiang@qiniu.com
 * @date: 2022/12/26 
 * */
trait LiteralExpressionReviser extends LiteralExpressionVisitor[Expression]{
  override def visitInt(literal: IntLiteral): Expression = literal

  override def visitLong(literal: LongLiteral): Expression = literal

  override def visitDouble(literal: DoubleLiteral): Expression = literal

  override def visitFloat(literal: FloatLiteral): Expression = literal

  override def visitBool(literal: BoolLiteral): Expression = literal

  override def visitString(literal: StringLiteral): Expression = literal

  override def visitChar(literal: CharLiteral): Expression = literal

  override def visitList(literal: ListLiteral): Expression = literal

  override def visitMap(literal: MapLiteral): Expression = literal

  override def visitSet(literal: SetLiteral): Expression = literal

  override def visitTuple(literal: TupleLiteral): Expression = literal

  override def visitClazz(literal: ClazzLiteral): Expression = literal

  override def visitOption(literal: OptionLiteral): Expression = literal
}
