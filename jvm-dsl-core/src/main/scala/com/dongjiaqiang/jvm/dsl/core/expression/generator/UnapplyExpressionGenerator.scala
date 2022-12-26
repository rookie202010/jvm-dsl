package com.dongjiaqiang.jvm.dsl.core.expression.generator

import com.dongjiaqiang.jvm.dsl.core.JvmDslParserParser.UnapplyExpressionContext
import com.dongjiaqiang.jvm.dsl.core.expression._
import com.dongjiaqiang.jvm.dsl.core.parser.ExprContext

import scala.collection.convert.ImplicitConversionsToScala._

object UnapplyExpressionGenerator extends IExpressionGenerator[UnapplyExpressionContext, Expression] {

  override def generate(exprContext: ExprContext, ruleContext: UnapplyExpressionContext): Expression = {
    if (ruleContext.baseLiteral( ) != null) {
      BaseLiteralGenerator.generate( exprContext, ruleContext.baseLiteral( ) )
    } else if (ruleContext.unapplyHeadExpression( ) != null) {
      val varList = ruleContext.unapplyHeadExpression( ).matchVariable( ).map( _.IDENTIFIER( ).getText )
      MatchHead( varList.slice( 0, varList.length - 1 ).toArray, varList.last )
    } else if (ruleContext.matchVariable( ) != null) {
      MatchIdentify( ruleContext.matchVariable( ).IDENTIFIER( ).getText )
    } else if (ruleContext.unapplyListExpression( ) != null) {
      MatchList( ruleContext.unapplyListExpression( ).unapplyExpression( ).map( c ⇒ generate( exprContext, c ) ).toArray )
    } else if (ruleContext.unapplyTupleExpression( ) != null) {
      MatchTuple( ruleContext.unapplyTupleExpression( ).unapplyExpression( ).map( c ⇒ generate( exprContext, c ) ).toArray )
    } else {
      MatchClass( ruleContext.unapplyClazzExpression( ).unapplyExpression( ).map( c ⇒ generate( exprContext, c ) ).toArray )
    }
  }
}
