package com.dongjiaqiang.jvm.dsl.core.expression.visitor.callchain

import com.dongjiaqiang.jvm.dsl.core.expression.{Expression, FuncCallChain, IntLiteralCallChain, LongLiteralCallChain}

/**
 * @author: rookie
 * @mail: dongjiaqiang@qiniu.com
 * @date: 2022/12/26 
 * */
trait CallChainExpressionReviser extends CallChainExpressionVisitor[Expression]{
  override def visitFuncCallChain(funcCallChain: FuncCallChain): Expression = funcCallChain

  override def visitIntLiteralCallChain(literalCallChain: IntLiteralCallChain): Expression = literalCallChain

  override def visitLongLiteralCallChain(literalCallChain: LongLiteralCallChain): Expression = literalCallChain
}
