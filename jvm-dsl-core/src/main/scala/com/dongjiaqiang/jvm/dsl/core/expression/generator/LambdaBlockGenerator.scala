package com.dongjiaqiang.jvm.dsl.core.expression.generator

import com.dongjiaqiang.jvm.dsl.api.expression.Block
import com.dongjiaqiang.jvm.dsl.core.JvmDslParserParser.LambdaBlockContext
import com.dongjiaqiang.jvm.dsl.core.parser.ExprContext

object LambdaBlockGenerator extends IExpressionGenerator[LambdaBlockContext, Block,GeneratorContext] {
  override def generate(exprContext: ExprContext,
                        ruleContext: LambdaBlockContext,
                        generatorContext: GeneratorContext = NoneGeneratorContext): Block = {
    val block = Block( )
    exprContext.pushBlock( block )
    block
  }
}


