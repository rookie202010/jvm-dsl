package com.dongjiaqiang.jvm.dsl.api.expression.visitor.block

import com.dongjiaqiang.jvm.dsl.api.expression._
import com.dongjiaqiang.jvm.dsl.api.expression.block._
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.ExpressionVisitor

trait BlockExpressionVisitor[T] {

  def visit(block: Block, visitor: ExpressionVisitor[T]): T = {
    throw new UnsupportedOperationException( "not supported" )
  }

  def visit(forExpr: For, visitor: ExpressionVisitor[T]): T = {
    throw new UnsupportedOperationException( "not supported" )
  }

  def visit(forCollection: ForCollection, visitor: ExpressionVisitor[T]): T = {
    throw new UnsupportedOperationException( "not supported" )
  }

  def visit(forMap: ForMap, visitor: ExpressionVisitor[T]): T = {
    throw new UnsupportedOperationException( "not supported" )
  }

  def visit(whileExpr: While, visitor: ExpressionVisitor[T]): T = {
    throw new UnsupportedOperationException( "not supported" )
  }

  def visit(doWhile: DoWhile, visitor: ExpressionVisitor[T]): T = {
    throw new UnsupportedOperationException( "not supported" )
  }

  def visit(sync: Sync, visitor: ExpressionVisitor[T]): T = {
    throw new UnsupportedOperationException( "not supported" )
  }

  def visit(ifExpr: If, visitor: ExpressionVisitor[T]): T = {
    throw new UnsupportedOperationException( "not supported" )
  }

  def visit(tryCatch: TryCatch, visitor: ExpressionVisitor[T]): T = {
    throw new UnsupportedOperationException( "not supported" )
  }

  def visit(asyncBlock: Async, visitor: ExpressionVisitor[T]): T = {
    throw new UnsupportedOperationException( "not supported" )
  }

  def visit(tryBlock: Try, visitor: ExpressionVisitor[T]): T = {
    throw new UnsupportedOperationException( "not supported" )
  }

  def visit(customBlockExpression: CustomBlockExpression, visitor: ExpressionVisitor[T]): T = {
    throw new UnsupportedOperationException( "not supported" )
  }

  def visit(matchCase: MatchCase, visitor: ExpressionVisitor[T]): T = {
    throw new UnsupportedOperationException( "not supported" )
  }

  def visit(matchType: MatchType, visitor: ExpressionVisitor[T]): T = {
    throw new UnsupportedOperationException( "not supported" )
  }

  def visit(matchHead: MatchHead, visitor: ExpressionVisitor[T]): T = {
    throw new UnsupportedOperationException( "not supported" )
  }

  def visit(matchList: MatchList, visitor: ExpressionVisitor[T]): T = {
    throw new UnsupportedOperationException( "not supported" )
  }

  def visit(matchTuple: MatchTuple, visitor: ExpressionVisitor[T]): T = {
    throw new UnsupportedOperationException( "not supported" )
  }

  def visit(matchClass: MatchClass, visitor: ExpressionVisitor[T]): T = {
    throw new UnsupportedOperationException( "not supported" )
  }


}
