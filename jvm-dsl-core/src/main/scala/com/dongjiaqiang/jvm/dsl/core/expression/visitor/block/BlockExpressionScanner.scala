package com.dongjiaqiang.jvm.dsl.core.expression.visitor.block

import com.dongjiaqiang.jvm.dsl.core.expression.visitor.ExpressionVisitor
import com.dongjiaqiang.jvm.dsl.core.expression._


trait BlockExpressionScanner extends BlockExpressionVisitor[Unit] {

  override def visit(block: Block, visitor: ExpressionVisitor[Unit]): Unit = {
    block
  }

  override def visit(forExpr: For, visitor: ExpressionVisitor[Unit]): Unit = {

  }

  override def visit(forCollection: ForCollection, visitor: ExpressionVisitor[Unit]): Unit = {

  }

  override def visit(forMap: ForMap, visitor: ExpressionVisitor[Unit]): Unit = {

  }

  override def visit(whileExpr: While, visitor: ExpressionVisitor[Unit]): Unit = {

  }

  override def visit(doWhile: DoWhile, visitor: ExpressionVisitor[Unit]): Unit = {

  }

  override def visit(sync: Sync, visitor: ExpressionVisitor[Unit]): Unit = {

  }

  override def visit(ifExpr: If, visitor: ExpressionVisitor[Unit]): Unit = {

  }

  override def visit(tryCatch: TryCatch, visitor: ExpressionVisitor[Unit]): Unit = {

  }

  override def visit(asyncBlock: Async, visitor: ExpressionVisitor[Unit]): Unit = {

  }

  override def visit(tryBlock: Try, visitor: ExpressionVisitor[Unit]): Unit = {

  }

  override def visit(matchCase: MatchCase, visitor: ExpressionVisitor[Unit]): Unit = {

  }
}
