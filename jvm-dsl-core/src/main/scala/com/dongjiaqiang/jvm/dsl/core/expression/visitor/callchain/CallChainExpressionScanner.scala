package com.dongjiaqiang.jvm.dsl.core.expression.visitor.callchain
import com.dongjiaqiang.jvm.dsl.core.expression.{FuncCallChain, IntLiteralCallChain, LongLiteralCallChain}

/**
 * @author: rookie
 * @mail: dongjiaqiang@qiniu.com
 * @date: 2022/12/26 
 * */
trait CallChainExpressionScanner extends CallChainExpressionVisitor[Unit]{
  override def visitFuncCallChain(funcCallChain: FuncCallChain): Unit = {}

  override def visitIntLiteralCallChain(literalCallChain: IntLiteralCallChain): Unit = {}

  override def visitLongLiteralCallChain(literalCallChain: LongLiteralCallChain): Unit = {}
}
