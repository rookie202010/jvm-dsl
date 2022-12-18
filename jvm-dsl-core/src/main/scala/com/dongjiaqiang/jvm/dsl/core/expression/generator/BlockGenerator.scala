package com.dongjiaqiang.jvm.dsl.core.expression.generator

import com.dongjiaqiang.jvm.dsl.core.JvmDslParserParser.{BlockContext, CallChainContext}
import com.dongjiaqiang.jvm.dsl.core.expression.{Block, FuncCallChain}
import com.dongjiaqiang.jvm.dsl.core.scope.Scope

object BlockGenerator extends IExpressionGenerator [BlockContext,Block]{
  override def generate(expressionContext:ExpressionContext, ruleContext: BlockContext): Block = ???
}


