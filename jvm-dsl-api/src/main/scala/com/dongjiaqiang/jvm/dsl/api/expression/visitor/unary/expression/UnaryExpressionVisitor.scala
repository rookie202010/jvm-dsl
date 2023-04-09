package com.dongjiaqiang.jvm.dsl.api.expression.visitor.unary.expression

import com.dongjiaqiang.jvm.dsl.api.expression.unary._
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.ExpressionVisitor

trait UnaryExpressionVisitor[T] {

  def visit(negate: Negate, visitor: ExpressionVisitor[T]): T = {
    throw new UnsupportedOperationException( "not supported" )
  }

  def visit(opposite: Opposite, visitor: ExpressionVisitor[T]): T = {
    throw new UnsupportedOperationException( "not supported" )
  }

  def visit(cast: Cast, visitor: ExpressionVisitor[T]): T = {
    throw new UnsupportedOperationException( "not supported" )
  }

  def visit(instanceof: Instanceof, visitor: ExpressionVisitor[T]): T = {
    throw new UnsupportedOperationException( "not supported" )
  }

  def visit(paren: Paren, visitor: ExpressionVisitor[T]): T = {
    throw new UnsupportedOperationException( "not supported" )
  }

  def visit(signedNumber: SignedNumber,visitor: ExpressionVisitor[T]):T={
    throw new UnsupportedOperationException( "not supported" )
  }
}
