package com.dongjiaqiang.jvm.dsl.core.expression.generator

import com.dongjiaqiang.jvm.dsl.core.JvmDslParserParser._
import com.dongjiaqiang.jvm.dsl.core.`type`.DslType
import com.dongjiaqiang.jvm.dsl.core.expression._

object UnaryIExpressionGenerator extends IExpressionGenerator[UnaryExpressionContext,Expression]{

  override def generate(expressionContext:ExpressionContext,
                        ruleContext: UnaryExpressionContext): Expression = {
    ruleContext match {
      case c: CastExprContext ⇒
        val dslType = DslType.unapply( c.`type`( ) )
        new Cast( generate( expressionContext, c.unaryExpression( ) ), dslType )
      case c: NegateExprContext ⇒
        new Negate( generate( expressionContext, c.unaryExpression( ) ) )
      case c: OppositeExprContext ⇒
        new Opposite( generate( expressionContext, c.unaryExpression( ) ) )
      case c: ParenExprContext ⇒
        new Paren( generate( expressionContext, c.unaryExpression( ) ) )
      case c: InstanceofExprContext ⇒
        val dslType = DslType.unapply( c.`type`( ) )
        val expression = if (c.literalAndCallChain( ).callChain( ) != null) {
          CallChain.generate( expressionContext, c.literalAndCallChain( ).callChain( ) )
        } else {
          LiteralGenerator.generate( expressionContext, c.literalAndCallChain( ).literal( ) )
        }
        new Instanceof( expression, dslType )
      case c: LiteralAndFuncCallExprContext ⇒
        if (c.literalAndCallChain( ).literal( ) != null) {
          LiteralGenerator.generate( expressionContext, c.literalAndCallChain( ).literal( ) )
        } else {
          CallChain.generate( expressionContext, c.literalAndCallChain( ).callChain( ) )
        }
    }
  }
}
