package com.dongjiaqiang.jvm.dsl.core.expression.visitor.statement

import com.dongjiaqiang.jvm.dsl.core.expression._
import com.dongjiaqiang.jvm.dsl.core.expression.visitor.ExpressionVisitor

/**
 * @author: rookie
 * @mail: dongjiaqiang@qiniu.com
 * @date: 2022/12/26 
 * */
trait StatementExpressionVisitor[T] {

  def visit(assign: Assign, visitor: ExpressionVisitor[T]): T

  def visit(break: Break.type, visitor: ExpressionVisitor[T]): T

  def visit(continue: Continue.type, visitor: ExpressionVisitor[T]): T

  def visit(throwExpr: Throw, visitor: ExpressionVisitor[T]): T

  def visit(returnExpr: Return, visitor: ExpressionVisitor[T]): T

  def visit(assert: Assert, visitor: ExpressionVisitor[T]): T

}
