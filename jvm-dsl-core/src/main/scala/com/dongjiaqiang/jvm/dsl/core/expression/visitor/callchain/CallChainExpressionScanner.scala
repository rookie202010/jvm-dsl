package com.dongjiaqiang.jvm.dsl.core.expression.visitor.callchain
import com.dongjiaqiang.jvm.dsl.core.expression.visitor.ExpressionVisitor
import com.dongjiaqiang.jvm.dsl.core.expression.{FuncCallChain, IntLiteralCallChain, LongLiteralCallChain}


trait CallChainExpressionScanner extends CallChainExpressionVisitor[Unit]{
  override def visit(funcCallChain: FuncCallChain, visitor: ExpressionVisitor[Unit]): Unit = {

  }

  override def visit(literalCallChain: IntLiteralCallChain, visitor: ExpressionVisitor[Unit]): Unit = {

  }

  override def visit(literalCallChain: LongLiteralCallChain, visitor: ExpressionVisitor[Unit]): Unit = {

  }
}
