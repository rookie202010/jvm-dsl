package com.dongjiaqiang.jvm.dsl.core.expression.generator

import com.dongjiaqiang.jvm.dsl.api.expression.{Expression, Lambda}
import com.dongjiaqiang.jvm.dsl.core.JvmDslParserParser._
import com.dongjiaqiang.jvm.dsl.core.parser.ExprContext

import scala.collection.convert.ImplicitConversionsToScala._

object LambdaGenerator extends IExpressionGenerator[LambdaExpressionContext, Expression] {

  override def generate(exprContext: ExprContext,
                        ruleContext: LambdaExpressionContext): Expression = {
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
