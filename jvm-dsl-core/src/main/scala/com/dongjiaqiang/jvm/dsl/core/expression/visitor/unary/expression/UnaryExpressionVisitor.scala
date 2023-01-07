package com.dongjiaqiang.jvm.dsl.core.expression.visitor.unary.expression

import com.dongjiaqiang.jvm.dsl.core.expression._
import com.dongjiaqiang.jvm.dsl.core.expression.visitor.ExpressionVisitor

/**
 * @author: rookie
 * @mail: dongjiaqiang@qiniu.com
 * @date: 2022/12/26 
 * */
trait UnaryExpressionVisitor[T] {

  def visit(negate: Negate, visitor: ExpressionVisitor[T]): T={
    throw new UnsupportedOperationException("not supported")
  }

  def visit(opposite: Opposite, visitor: ExpressionVisitor[T]): T={
    throw new UnsupportedOperationException("not supported")
  }

  def visit(cast: Cast, visitor: ExpressionVisitor[T]): T={
    throw new UnsupportedOperationException("not supported")
  }

  def visit(instanceof: Instanceof, visitor: ExpressionVisitor[T]): T={
    throw new UnsupportedOperationException("not supported")
  }

  def visit(paren: Paren, visitor: ExpressionVisitor[T]): T={
    throw new UnsupportedOperationException("not supported")
  }
}
