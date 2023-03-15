package com.dongjiaqiang.jvm.dsl.core.expression.generator

import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.core.JvmDslParserParser.ExpressionContext
import com.dongjiaqiang.jvm.dsl.core.parser.ExprContext

object ExpressionGenerator extends IExpressionGenerator[ExpressionContext, ValueExpression,GeneratorContext] {
  override def generate(exprContext: ExprContext,
                        ruleContext: ExpressionContext,
                        generatorContext: GeneratorContext = NoneGeneratorContext): ValueExpression = {
    if (ruleContext.lambdaExpression( ) != null) {
      LambdaGenerator.generate( exprContext, ruleContext.lambdaExpression( ) )
    } else if (ruleContext.blockExpression( ) != null) {
      BlockExpressionGenerator.generate( exprContext, ruleContext.blockExpression( ) )
    } else if (ruleContext.conditionalOrExpression( ) != null) {
      OrGenerator.generate( exprContext, ruleContext.conditionalOrExpression( ) )
    } else {
      MatchCaseExpressionGenerator.generate( exprContext, ruleContext.matchCaseExpression( ) )
    }
  }
}
