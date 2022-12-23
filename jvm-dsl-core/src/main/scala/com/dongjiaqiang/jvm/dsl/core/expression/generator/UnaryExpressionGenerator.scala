package com.dongjiaqiang.jvm.dsl.core.expression.generator

import com.dongjiaqiang.jvm.dsl.core.JvmDslParserParser._
import com.dongjiaqiang.jvm.dsl.core.`type`.DslType
import com.dongjiaqiang.jvm.dsl.core.expression._
import com.dongjiaqiang.jvm.dsl.core.parser.ExprContext

object UnaryExpressionGenerator extends IExpressionGenerator[UnaryExpressionContext,Expression]{

  override def generate(exprContext: ExprContext,
                        ruleContext: UnaryExpressionContext): Expression = {
    ruleContext match {
      case c: CastExprContext ⇒
        val dslType = DslType.unapply( c.`type`( ) )
        new Cast( generate( exprContext, c.unaryExpression( ) ), dslType )
      case c: NegateExprContext ⇒
        new Negate( generate( exprContext, c.unaryExpression( ) ) )
      case c: OppositeExprContext ⇒
        new Opposite( generate( exprContext, c.unaryExpression( ) ) )
      case c: ParenExprContext ⇒
        new Paren( generate( exprContext, c.unaryExpression( ) ) )
      case c: InstanceofExprContext ⇒
        val dslType = DslType.unapply( c.`type`( ) )
        val expression = if (c.literalAndCallChain( ).callChain( ) != null) {
          CallChainGenerator.generate( exprContext, c.literalAndCallChain( ).callChain( ) )
        } else {
          LiteralGenerator.generate( exprContext, c.literalAndCallChain( ).literal( ) )
        }
        new Instanceof( expression, dslType )
      case c: LiteralAndFuncCallExprContext ⇒
        if (c.literalAndCallChain( ).literal( ) != null) {
          LiteralGenerator.generate( exprContext, c.literalAndCallChain( ).literal( ) )
        } else {
          CallChainGenerator.generate( exprContext, c.literalAndCallChain( ).callChain( ) )
        }
    }
  }
}
