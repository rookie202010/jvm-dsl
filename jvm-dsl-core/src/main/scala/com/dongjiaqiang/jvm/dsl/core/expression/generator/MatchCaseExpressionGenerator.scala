package com.dongjiaqiang.jvm.dsl.core.expression.generator

import com.dongjiaqiang.jvm.dsl.api.expression._
import com.dongjiaqiang.jvm.dsl.api.expression.`var`.VarRef
import com.dongjiaqiang.jvm.dsl.api.expression.block.{Block, MatchCase, MatchType}
import com.dongjiaqiang.jvm.dsl.core.JvmDslParserParser.MatchCaseExpressionContext
import com.dongjiaqiang.jvm.dsl.core.parser.ExprContext
import com.dongjiaqiang.jvm.dsl.core.scope.toDslType

import scala.collection.convert.ImplicitConversionsToScala._
import scala.collection.mutable.{LinkedHashMap ⇒ MutableMap}

object MatchCaseExpressionGenerator extends IExpressionGenerator[MatchCaseExpressionContext, ValueExpression,GeneratorContext] {

  override def generate(exprContext: ExprContext,
                        ruleContext: MatchCaseExpressionContext,
                        generatorContext: GeneratorContext = NoneGeneratorContext): ValueExpression = {
    val fieldScope = exprContext.getContextScope.resolveVarRefs( exprContext.getCurrentExpressionIndex,
      List( ruleContext.localVariable( ).IDENTIFIER( ).getText ),Set() )

    //todo
    val matched = VarRef( List( ruleContext.localVariable( ).IDENTIFIER( ).getText ),MutableMap(),fieldScope)

    val cases = ruleContext.caseExpression( ).map( ctx ⇒ {
      if (ctx.typeMatchExpression( ) != null) {
        val matchVar = MatchType( ctx.typeMatchExpression( ).localVariable( ).IDENTIFIER( ).getText,
          toDslType( ctx.typeMatchExpression( ).`type`( ),exprContext.getProgramScope ) )

        val block = Block( )
        exprContext.pushBlock( block )

        (matchVar, block)
      } else {

        val block = Block( )
        exprContext.pushBlock( block )

        (UnapplyExpressionGenerator.generate( exprContext, ctx.unapplyExpression( ) ), block)
      }
    } )

    val default = if (ruleContext.DEFAULT( ) != null) {
      val block = Block( )
      exprContext.pushBlock( block )
      Some( block )
    } else {
      None
    }
    MatchCase( matched, cases.toArray, default )
  }
}


