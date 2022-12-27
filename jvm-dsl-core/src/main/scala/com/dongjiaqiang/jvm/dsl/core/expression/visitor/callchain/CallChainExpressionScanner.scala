package com.dongjiaqiang.jvm.dsl.core.expression.visitor.callchain
import com.dongjiaqiang.jvm.dsl.core.expression.{FuncCallChain, IntLiteralCallChain, LongLiteralCallChain}


trait CallChainExpressionScanner extends CallChainExpressionVisitor[Unit]{
  override def visitFuncCallChain(funcCallChain: FuncCallChain): Unit = {}

  override def visitIntLiteralCallChain(literalCallChain: IntLiteralCallChain): Unit = {}

  override def visitLongLiteralCallChain(literalCallChain: LongLiteralCallChain): Unit = {}
}
