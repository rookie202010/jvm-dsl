package com.dongjiaqiang.jvm.dsl.core.expression.visitor.literal
import com.dongjiaqiang.jvm.dsl.core.expression._

trait LiteralExpressionScanner extends LiteralExpressionVisitor[Unit] {
  override def visitInt(literal: IntLiteral): Unit = {}

  override def visitLong(literal: LongLiteral): Unit = {}

  override def visitDouble(literal: DoubleLiteral): Unit = {}

  override def visitFloat(literal: FloatLiteral): Unit = {}

  override def visitBool(literal: BoolLiteral): Unit = {}

  override def visitString(literal: StringLiteral): Unit = {}

  override def visitChar(literal: CharLiteral): Unit = {}

  override def visitList(literal: ListLiteral): Unit = {}

  override def visitMap(literal: MapLiteral): Unit = {}

  override def visitSet(literal: SetLiteral): Unit = {}

  override def visitTuple(literal: TupleLiteral): Unit = {}

  override def visitClazz(literal: ClazzLiteral): Unit = {}

  override def visitOption(literal: OptionLiteral): Unit = {}
}
