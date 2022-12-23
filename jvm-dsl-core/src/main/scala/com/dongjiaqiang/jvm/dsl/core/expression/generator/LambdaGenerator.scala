package com.dongjiaqiang.jvm.dsl.core.expression.generator

import com.dongjiaqiang.jvm.dsl.core.JvmDslParserParser._
import com.dongjiaqiang.jvm.dsl.core.`type`.{ClazzType, UnResolvedType}
import com.dongjiaqiang.jvm.dsl.core.expression.{Expression, Lambda, MatchCase, UnapplyClazzLiteral}
import com.dongjiaqiang.jvm.dsl.core.parser.ExprContext

import scala.collection.convert.ImplicitConversionsToScala._

object LambdaGenerator extends IExpressionGenerator[LambdaExpressionContext, Expression] {

  def generator(exprContext: ExprContext,
                c: CaseExpressionContext): Expression = {
    if (c.baseLiteral( ) != null) {
      BaseLiteralGenerator.generate( exprContext, c.baseLiteral( ) )
    } else if (c.localVariable( ) != null) {
      c.localVariable( ).IDENTIFIER( ).getText
    } else {
      val clazzType = new ClazzType( c.unapplyExpression( ).clazzType( ).IDENTIFIER( ).getText,
        (0 until c.unapplyExpression( ).literal( ).size( )).map( _ ⇒ UnResolvedType ).toArray )
      val literals = c.unapplyExpression( ).literal( )
        .map( l ⇒ LiteralGenerator.generate( expressionContext, l ) ).toArray
      UnapplyClazzLiteral( clazzType, literals )
    }
  }

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
        case c:MatchCaseExprContext⇒
          val matched = c.localVariable( ).IDENTIFIER( ).getText

          val (caseBlocks, defaultBlock) = if (c.DEFAULT( ) != null) {
            (c.lambdaBlock( ).subList( 0, c.lambdaBlock( ).size( ) - 1 ), Some( c.lambdaBlock( ).last ))
          } else {
            (c.lambdaBlock( ), None)
          }
          val caseLiterals = c.caseExpression( ).map( ce ⇒ {
            generator( exprContext, ce )
          } ).toList
          val cases = caseLiterals.zip( caseBlocks.map( b ⇒ LambdaBlockGenerator.generate( exprContext, b ) ) )
          val default = defaultBlock.map( d ⇒ LambdaBlockGenerator.generate( exprContext, d ) )
          MatchCase( matched, cases, default )
      }
  }
}
