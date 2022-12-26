package com.dongjiaqiang.jvm.dsl.core.expression.visitor.callchain

import com.dongjiaqiang.jvm.dsl.core.expression.{FuncCallChain, IntLiteralCallChain, LongLiteralCallChain}

/**
 * @author: rookie
 * @mail: dongjiaqiang@qiniu.com
 * @date: 2022/12/26 
 * */
trait CallChainExpressionVisitor[T] {

  def visitFuncCallChain(funcCallChain: FuncCallChain): T

  def visitIntLiteralCallChain(literalCallChain: IntLiteralCallChain): T

  def visitLongLiteralCallChain(literalCallChain: LongLiteralCallChain): T

}
