package com.dongjiaqiang.jvm.dsl.core.expression.visitor.unary.expression
import com.dongjiaqiang.jvm.dsl.core.expression.visitor.ExpressionVisitor
import com.dongjiaqiang.jvm.dsl.core.expression._

trait UnaryExpressionScanner extends UnaryExpressionVisitor[Unit]{
  override def visit(negate: Negate, visitor: ExpressionVisitor[Unit]): Unit = {

  }

  override def visit(opposite: Opposite, visitor: ExpressionVisitor[Unit]): Unit = {

  }

  override def visit(cast: Cast, visitor: ExpressionVisitor[Unit]): Unit = {

  }

  override def visit(instanceof: Instanceof, visitor: ExpressionVisitor[Unit]): Unit = {

  }

  override def visit(paren: Paren, visitor: ExpressionVisitor[Unit]): Unit = {

  }
}
