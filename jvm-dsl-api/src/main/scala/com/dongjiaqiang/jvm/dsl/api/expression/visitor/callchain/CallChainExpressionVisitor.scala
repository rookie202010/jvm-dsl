package com.dongjiaqiang.jvm.dsl.api.expression.visitor.callchain

import com.dongjiaqiang.jvm.dsl.api.expression._
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.ExpressionVisitor

trait CallChainExpressionVisitor[T] {

  def visit(funcCallChain: FuncCallChain, visitor: ExpressionVisitor[T]): T = {
    throw new UnsupportedOperationException( "not supported" )
  }

  def visit(literalCallChain: IntLiteralCallChain, visitor: ExpressionVisitor[T]): T = {
    throw new UnsupportedOperationException( "not supported" )
  }

  def visit(literalCallChain: LongLiteralCallChain, visitor: ExpressionVisitor[T]): T = {
    throw new UnsupportedOperationException( "not supported" )
  }

  def visit(literalCallChain: DoubleLiteralCallChain, visitor: ExpressionVisitor[T]): T = {
    throw new UnsupportedOperationException( "not supported" )
  }

  def visit(literalCallChain: FloatLiteralCallChain, visitor: ExpressionVisitor[T]): T = {
    throw new UnsupportedOperationException( "not supported" )
  }

  def visit(literalCallChain: BoolLiteralCallChain, visitor: ExpressionVisitor[T]): T = {
    throw new UnsupportedOperationException( "not supported" )
  }

  def visit(literalCallChain: StringLiteralCallChain, visitor: ExpressionVisitor[T]): T = {
    throw new UnsupportedOperationException( "not supported" )
  }

  def visit(literalCallChain: CharLiteralCallChain, visitor: ExpressionVisitor[T]): T = {
    throw new UnsupportedOperationException( "not supported" )
  }

  def visit(literalCallChain: ListLiteralCallChain, visitor: ExpressionVisitor[T]): T = {
    throw new UnsupportedOperationException( "not supported" )
  }

  def visit(literalCallChain: OptionLiteralCallChain, visitor: ExpressionVisitor[T]): T = {
    throw new UnsupportedOperationException( "not supported" )
  }

  def visit(literalCallChain: TupleLiteralCallChain, visitor: ExpressionVisitor[T]): T = {
    throw new UnsupportedOperationException( "not supported" )
  }

  def visit(literalCallChain: MapLiteralCallChain, visitor: ExpressionVisitor[T]): T = {
    throw new UnsupportedOperationException( "not supported" )
  }

  def visit(literalCallChain: SetLiteralCallChain, visitor: ExpressionVisitor[T]): T = {
    throw new UnsupportedOperationException( "not supported" )
  }

  def visit(literalCallChain: ClazzLiteralCallChain, visitor: ExpressionVisitor[T]): T = {
    throw new UnsupportedOperationException( "not supported" )
  }

}
