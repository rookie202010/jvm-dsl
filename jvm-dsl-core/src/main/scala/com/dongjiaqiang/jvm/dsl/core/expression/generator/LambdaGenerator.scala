package com.dongjiaqiang.jvm.dsl.core.expression.generator

import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.expression.block.Lambda
import com.dongjiaqiang.jvm.dsl.core.JvmDslParserParser._
import com.dongjiaqiang.jvm.dsl.core.parser.ExprContext

import scala.collection.convert.ImplicitConversionsToScala._

object LambdaGenerator extends IExpressionGenerator[LambdaExpressionContext, ValueExpression,GeneratorContext] {

  override def generate(exprContext: ExprContext,
                        ruleContext: LambdaExpressionContext,
                        generatorContext: GeneratorContext = NoneGeneratorContext): ValueExpression = {
    ruleContext match {
      case c: NoParamLambdaExprContext ⇒
        Lambda( Array( ), LambdaBlockGenerator.generate( exprContext, c.lambdaBlock( ) ) )
      case c: OneParamLambdaExprContext ⇒
        Lambda( Array( c.localVariable( ).IDENTIFIER( ).getText ),
          LambdaBlockGenerator.generate( exprContext, c.lambdaBlock( ) ) )
      case c: ParamsLambdaExprContext ⇒
        Lambda( c.localVariable( ).map( _.IDENTIFIER( ).getText ).toArray,
          LambdaBlockGenerator.generate( exprContext, c.lambdaBlock( ) ) )
      }
  }
}
