package com.dongjiaqiang.jvm.dsl.core.expression.visitor.block

import com.dongjiaqiang.jvm.dsl.core.expression._
import com.dongjiaqiang.jvm.dsl.core.expression.visitor.ExpressionVisitor

trait BlockExpressionVisitor[T] {

  def visit(block: Block, visitor: ExpressionVisitor[T]): T={
    throw new UnsupportedOperationException("not supported")
  }

  def visit(forExpr: For, visitor: ExpressionVisitor[T]): T={
    throw new UnsupportedOperationException("not supported")
  }

  def visit(forCollection: ForCollection, visitor: ExpressionVisitor[T]): T={
    throw new UnsupportedOperationException("not supported")
  }

  def visit(forMap: ForMap, visitor: ExpressionVisitor[T]): T={
    throw new UnsupportedOperationException("not supported")
  }

  def visit(whileExpr: While, visitor: ExpressionVisitor[T]): T={
    throw new UnsupportedOperationException("not supported")
  }

  def visit(doWhile: DoWhile, visitor: ExpressionVisitor[T]): T={
    throw new UnsupportedOperationException("not supported")
  }

  def visit(sync: Sync, visitor: ExpressionVisitor[T]): T={
    throw new UnsupportedOperationException("not supported")
  }

  def visit(ifExpr: If, visitor: ExpressionVisitor[T]): T={
    throw new UnsupportedOperationException("not supported")
  }

  def visit(tryCatch: TryCatch, visitor: ExpressionVisitor[T]): T={
    throw new UnsupportedOperationException("not supported")
  }

  def visit(asyncBlock: Async, visitor: ExpressionVisitor[T]): T={
    throw new UnsupportedOperationException("not supported")
  }

  def visit(tryBlock: Try, visitor: ExpressionVisitor[T]): T={
    throw new UnsupportedOperationException("not supported")
  }

  def visit(matchCase: MatchCase, visitor: ExpressionVisitor[T]): T={
    throw new UnsupportedOperationException("not supported")
  }

}
