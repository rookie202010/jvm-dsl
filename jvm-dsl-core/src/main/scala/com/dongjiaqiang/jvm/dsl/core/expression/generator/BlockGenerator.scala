package com.dongjiaqiang.jvm.dsl.core.expression.generator

import com.dongjiaqiang.jvm.dsl.core.JvmDslParserParser.BlockContext
import com.dongjiaqiang.jvm.dsl.core.expression.Block

object BlockGenerator extends IExpressionGenerator[BlockContext, Block] {
  override def generate(expressionContext: ExpressionContext, ruleContext: BlockContext): Block = {
    val block = new Block( )
    expressionContext.lambdaBlockStack.push(block)
    block
  }
}


