package com.dongjiaqiang.jvm.dsl.core.expression.generator

import com.dongjiaqiang.jvm.dsl.core.JvmDslParserParser.LambdaBlockContext
import com.dongjiaqiang.jvm.dsl.core.expression.Block
import com.dongjiaqiang.jvm.dsl.core.parser.ExprContext

object LambdaBlockGenerator extends IExpressionGenerator[LambdaBlockContext, Block] {
  override def generate(exprContext: ExprContext, ruleContext: LambdaBlockContext): Block = {
    val block = new Block( )
    exprContext.pushBlock( block )
    block
  }
}


