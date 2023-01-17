package com.dongjiaqiang.jvm.dsl.core.expression.visitor.statement

import com.dongjiaqiang.jvm.dsl.api.expression.visitor.ExpressionVisitor
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.statement.StatementExpressionVisitor
import com.dongjiaqiang.jvm.dsl.api.expression._

trait StatementExpressionScanner extends StatementExpressionVisitor[Unit] {
  override def visit(assign: Assign, visitor: ExpressionVisitor[Unit]): Unit = {

  }

  override def visit(break: Break.type, visitor: ExpressionVisitor[Unit]): Unit = {

  }

  override def visit(continue: Continue.type, visitor: ExpressionVisitor[Unit]): Unit = {

  }

  override def visit(throwExpr: Throw, visitor: ExpressionVisitor[Unit]): Unit = {

  }

  override def visit(returnExpr: Return, visitor: ExpressionVisitor[Unit]): Unit = {

  }

  override def visit(assert: Assert, visitor: ExpressionVisitor[Unit]): Unit = {

  }
}
