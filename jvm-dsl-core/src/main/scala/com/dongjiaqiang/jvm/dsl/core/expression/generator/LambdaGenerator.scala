package com.dongjiaqiang.jvm.dsl.core.expression.generator

import com.dongjiaqiang.jvm.dsl.core.JvmDslParserParser.{CaseExpressionContext, LambdaExpressionContext, MatchCaseExprContext, NoParamLambdaExprContext, OneParamLambdaExprContext, ParamsLambdaExprContext}
import com.dongjiaqiang.jvm.dsl.core.`type`.{ClazzType, DslType, UnResolvedType}
import com.dongjiaqiang.jvm.dsl.core.expression.{Block, Expression, Lambda, MatchCase, UnapplyClazzLiteral}

import scala.collection.convert.ImplicitConversionsToScala._

object LambdaGenerator extends IExpressionGenerator[LambdaExpressionContext,Expression]{

  def generator(expressionContext: ExpressionContext,
                c:CaseExpressionContext): Expression ={
      if(c.literal()!=null){
          LiteralGenerator.generate(expressionContext,c.literal())
      }else{
          val clazzType = new ClazzType(c.unapplyExpression().clazzType().IDENTIFIER().getText,
            (0 until c.unapplyExpression().literal().size()).map(_⇒UnResolvedType).toArray)
          val literals = c.unapplyExpression().literal()
            .map(l⇒LiteralGenerator.generate(expressionContext,l)).toArray
          UnapplyClazzLiteral(clazzType, literals)
      }
    }

  override def generate(expressionContext: ExpressionContext,
                        ruleContext: LambdaExpressionContext): Expression = {
      ruleContext match {
        case c:NoParamLambdaExprContext⇒
            Lambda(Array(),BlockGenerator.generate(expressionContext,c.block()))
        case c:OneParamLambdaExprContext⇒
            Lambda(Array(c.localVariable().IDENTIFIER().getText),
              BlockGenerator.generate(expressionContext,c.block()))
        case c:ParamsLambdaExprContext⇒
            Lambda(c.localVariable().map(_.IDENTIFIER().getText).toArray,
              BlockGenerator.generate(expressionContext,c.block()))
        case c:MatchCaseExprContext⇒
            val matched = c.localVariable().IDENTIFIER().getText

            val (caseBlocks,defaultBlock) = if(c.DEFAULT()!=null){
              (c.block().subList(0,c.block().size()-1),Some(c.block().last))
            }else{
              (c.block(),None)
            }
            val caseLiterals = c.caseExpression().map(ce⇒{
                generator(expressionContext,ce)
            }).toList
            val cases = caseLiterals.zip(caseBlocks.map(b⇒BlockGenerator.generate(expressionContext,b)))
            val default = defaultBlock.map(d⇒BlockGenerator.generate(expressionContext,d))
            MatchCase(matched,cases,default)
      }
  }
}
