package com.dongjiaqiang.jvm.dsl.core.expression.generator

import com.dongjiaqiang.jvm.dsl.core.JvmDslParserParser.MatchCaseExpressionContext
import com.dongjiaqiang.jvm.dsl.core.`type`.DslType
import com.dongjiaqiang.jvm.dsl.core.expression._
import com.dongjiaqiang.jvm.dsl.core.parser.ExprContext

import scala.collection.convert.ImplicitConversionsToScala._

object MatchCaseExpressionGenerator extends IExpressionGenerator[MatchCaseExpressionContext, Expression] {

  override def generate(exprContext: ExprContext, ruleContext: MatchCaseExpressionContext): Expression = {
    val fieldScope = exprContext.getContextScope.resolveVarRefs( exprContext.getCurrentExpressionIndex,
      List( ruleContext.localVariable( ).IDENTIFIER( ).getText ) )

    //todo
    val matched = VarRef( List( ruleContext.localVariable( ).IDENTIFIER( ).getText ), fieldScope.get )

    val cases = ruleContext.caseExpression( ).map( ctx ⇒ {
      if (ctx.typeMatchExpression( ) != null) {
        val matchVar = MatchType( ctx.typeMatchExpression( ).localVariable( ).IDENTIFIER( ).getText,
          DslType.unapply( ctx.typeMatchExpression( ).`type`( ) ) )

        val block = new Block( )
        exprContext.pushBlock( block )

        (matchVar, block)
      } else {

        val block = new Block( )
        exprContext.pushBlock( block )

        (UnapplyExpressionGenerator.generate( exprContext, ctx.unapplyExpression( ) ), block)
      }
    } )

    val default = if (ruleContext.DEFAULT( ) != null) {
      val block = new Block( )
      exprContext.pushBlock( block )
      Some( block )
    } else {
      None
    }
    MatchCase( matched, cases.toArray, default )
  }
}


