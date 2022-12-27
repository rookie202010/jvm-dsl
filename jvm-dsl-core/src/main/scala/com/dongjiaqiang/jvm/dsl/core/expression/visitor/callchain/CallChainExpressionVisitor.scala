package com.dongjiaqiang.jvm.dsl.core.expression.visitor.callchain

import com.dongjiaqiang.jvm.dsl.core.expression.visitor.ExpressionVisitor
import com.dongjiaqiang.jvm.dsl.core.expression.{BoolLiteralCallChain, CharLiteralCallChain, ClazzLiteralCallChain, DoubleLiteralCallChain, FloatLiteralCallChain, FuncCallChain, IntLiteralCallChain, ListLiteralCallChain, LongLiteralCallChain, MapLiteralCallChain, OptionLiteralCallChain, SetLiteralCallChain, StringLiteralCallChain, TupleLiteralCallChain}

/**
 * @author: rookie
 * @mail: dongjiaqiang@qiniu.com
 * @date: 2022/12/26 
 * */
trait CallChainExpressionVisitor[T] {

  def visit(funcCallChain: FuncCallChain, visitor: ExpressionVisitor[T]): T

  def visit(literalCallChain: IntLiteralCallChain, visitor: ExpressionVisitor[T]): T

  def visit(literalCallChain: LongLiteralCallChain, visitor: ExpressionVisitor[T]): T

  def visit(literalCallChain: DoubleLiteralCallChain, visitor: ExpressionVisitor[T]): T

  def visit(literalCallChain: FloatLiteralCallChain, visitor: ExpressionVisitor[T]): T

  def visit(literalCallChain: BoolLiteralCallChain, visitor: ExpressionVisitor[T]): T

  def visit(literalCallChain: StringLiteralCallChain, visitor: ExpressionVisitor[T]): T

  def visit(literalCallChain: CharLiteralCallChain, visitor: ExpressionVisitor[T]): T

  def visit(literalCallChain: ListLiteralCallChain, visitor: ExpressionVisitor[T]): T

  def visit(literalCallChain: OptionLiteralCallChain, visitor: ExpressionVisitor[T]): T

  def visit(literalCallChain: TupleLiteralCallChain, visitor: ExpressionVisitor[T]): T

  def visit(literalCallChain: MapLiteralCallChain, visitor: ExpressionVisitor[T]): T

  def visit(literalCallChain: SetLiteralCallChain, visitor: ExpressionVisitor[T]): T

  def visit(literalCallChain: ClazzLiteralCallChain, visitor: ExpressionVisitor[T]): T

}
