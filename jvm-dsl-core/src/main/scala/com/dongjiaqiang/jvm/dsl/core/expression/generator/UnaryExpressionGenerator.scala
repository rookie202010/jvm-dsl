package com.dongjiaqiang.jvm.dsl.core.expression.generator

import com.dongjiaqiang.jvm.dsl.api.expression._
import com.dongjiaqiang.jvm.dsl.core.JvmDslParserParser._
import com.dongjiaqiang.jvm.dsl.core.parser.ExprContext
import com.dongjiaqiang.jvm.dsl.core.scope.toDslType

object UnaryExpressionGenerator extends IExpressionGenerator[UnaryExpressionContext,Expression]{

  override def generate(exprContext: ExprContext,
                        ruleContext: UnaryExpressionContext): Expression = {
    ruleContext match {
      case c: CastExprContext ⇒
        val dslType = toDslType( c.`type`( ) )
        Cast( generate( exprContext, c.unaryExpression( ) ), dslType )
      case c: NegateExprContext ⇒
        Negate( generate( exprContext, c.unaryExpression( ) ) )
      case c: OppositeExprContext ⇒
        Opposite( generate( exprContext, c.unaryExpression( ) ) )
      case c: ParenExprContext ⇒
        Paren( generate( exprContext, c.unaryExpression( ) ) )
      case c: InstanceofExprContext ⇒
        val dslType = toDslType( c.`type`( ) )
        Instanceof( CallChainGenerator.generate( exprContext, c.literalAndCallChain( ) ),
          dslType )
      case c: LiteralAndFuncCallExprContext ⇒
        CallChainGenerator.generate( exprContext, c.literalAndCallChain( ) )
    }
  }
}
