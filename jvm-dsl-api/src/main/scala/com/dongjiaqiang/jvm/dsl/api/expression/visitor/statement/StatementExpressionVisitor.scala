package com.dongjiaqiang.jvm.dsl.api.expression.visitor.statement

import com.dongjiaqiang.jvm.dsl.api.expression.`var`.Assign
import com.dongjiaqiang.jvm.dsl.api.expression.statement._
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.ExpressionVisitor

trait StatementExpressionVisitor[T] {

  def visit(assign: Assign, visitor: ExpressionVisitor[T]): T = {
    throw new UnsupportedOperationException( "not supported" )
  }

  def visit(break: Break.type, visitor: ExpressionVisitor[T]): T = {
    throw new UnsupportedOperationException( "not supported" )
  }

  def visit(continue: Continue.type, visitor: ExpressionVisitor[T]): T = {
    throw new UnsupportedOperationException( "not supported" )
  }

  def visit(throwExpr: Throw, visitor: ExpressionVisitor[T]): T = {
    throw new UnsupportedOperationException( "not supported" )
  }

  def visit(returnExpr: Return, visitor: ExpressionVisitor[T]): T = {
    throw new UnsupportedOperationException( "not supported" )
  }

  def visit(assert: Assert, visitor: ExpressionVisitor[T]): T = {
    throw new UnsupportedOperationException( "not supported" )
  }

}
