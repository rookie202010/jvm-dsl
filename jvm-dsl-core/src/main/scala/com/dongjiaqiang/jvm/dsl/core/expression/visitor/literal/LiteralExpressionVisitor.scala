package com.dongjiaqiang.jvm.dsl.core.expression.visitor.literal

import com.dongjiaqiang.jvm.dsl.core.expression._
import com.dongjiaqiang.jvm.dsl.core.expression.visitor.ExpressionVisitor

trait LiteralExpressionVisitor[T] {

  def visit(literal: IntLiteral, visitor: ExpressionVisitor[T]): T={
    throw new UnsupportedOperationException("not supported")
  }

  def visit(literal: LongLiteral, visitor: ExpressionVisitor[T]): T={
    throw new UnsupportedOperationException("not supported")
  }

  def visit(literal: DoubleLiteral, visitor: ExpressionVisitor[T]): T={
    throw new UnsupportedOperationException("not supported")
  }

  def visit(literal: FloatLiteral, visitor: ExpressionVisitor[T]): T={
    throw new UnsupportedOperationException("not supported")
  }

  def visit(literal: BoolLiteral, visitor: ExpressionVisitor[T]): T={
    throw new UnsupportedOperationException("not supported")
  }

  def visit(literal: StringLiteral, visitor: ExpressionVisitor[T]): T={
    throw new UnsupportedOperationException("not supported")
  }

  def visit(literal: CharLiteral, visitor: ExpressionVisitor[T]): T={
    throw new UnsupportedOperationException("not supported")
  }

  def visit(literal: ListLiteral, visitor: ExpressionVisitor[T]): T={
    throw new UnsupportedOperationException("not supported")
  }

  def visit(literal: MapLiteral, visitor: ExpressionVisitor[T]): T={
    throw new UnsupportedOperationException("not supported")
  }

  def visit(literal: SetLiteral, visitor: ExpressionVisitor[T]): T={
    throw new UnsupportedOperationException("not supported")
  }

  def visit(literal: TupleLiteral, visitor: ExpressionVisitor[T]): T={
    throw new UnsupportedOperationException("not supported")
  }

  def visit(literal: ClazzLiteral, visitor: ExpressionVisitor[T]): T={
    throw new UnsupportedOperationException("not supported")
  }

  def visit(literal: OptionLiteral, visitor: ExpressionVisitor[T]): T={
    throw new UnsupportedOperationException("not supported")
  }
}
