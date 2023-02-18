package com.dongjiaqiang.jvm.dsl.core.expression.generator

import com.dongjiaqiang.jvm.dsl.api.`type`.ClazzType
import com.dongjiaqiang.jvm.dsl.api.expression._
import com.dongjiaqiang.jvm.dsl.core.JvmDslParserParser._
import com.dongjiaqiang.jvm.dsl.core.parser.ExprContext

import scala.collection.convert.ImplicitConversionsToScala._

object UnapplyExpressionGenerator extends IExpressionGenerator[UnapplyExpressionContext, Expression,GeneratorContext] {


  private def generateHeadExpr(exprContext: ExprContext, ruleContext:UnapplyHeadExprContext):Array[Either[Expression, String]]={
    val headExprContext = ruleContext.unapplyExpression( ).head
    val lastExprContext = ruleContext.unapplyExpression( ).last
    val headExpr = generateExpr(exprContext,headExprContext)
    lastExprContext match {
      case unapplyHeadExprContext: UnapplyHeadExprContext⇒
        Array(headExpr) ++ generateHeadExpr(exprContext,unapplyHeadExprContext)
      case _⇒ Array(headExpr,  generateExpr(exprContext,lastExprContext) )
    }
  }
  private def generateExpr(exprContext: ExprContext,
                           ruleContext: UnapplyExpressionContext): Either[Expression, String] = {
    ruleContext match {
      case c: UnapplyLiteralExprContext ⇒
        Left( BaseLiteralGenerator.generate( exprContext, c.baseLiteral( ) ) )
      case c: UnapplyListExprContext ⇒
        Left( MatchList( c.unapplyExpression( ).map( c ⇒ generateExpr( exprContext, c ) ).toArray ) )
      case c: UnapplyTupleExprContext ⇒
        Left( MatchTuple( c.unapplyExpression( ).map( c ⇒ generateExpr( exprContext, c ) ).toArray ) )
      case c: UnapplyVarExprContext ⇒
        Right( c.localVariable( ).IDENTIFIER( ).getText )
      case c: UnapplyClazzExprContext ⇒
        Left( MatchClass( ClazzType( c.clazzType( ).getText, Array( ) ),
          c.unapplyExpression( ).map( c ⇒ generateExpr( exprContext, c ) ).toArray ) )
      case c: UnapplyHeadExprContext ⇒
        val cs = generateHeadExpr(exprContext,c)
        Left( MatchHead( cs.slice( 0, cs.length - 1 ),
         cs.last ) )
    }
  }

  override def generate(exprContext: ExprContext,
                        ruleContext: UnapplyExpressionContext,
                        generatorContext: GeneratorContext = NoneGeneratorContext): Expression = {
    generateExpr( exprContext, ruleContext ) match {
      case Left( expression ) ⇒ expression
      case Right( clazzName ) ⇒ new ObjectLiteral(ClazzType(clazzName))
    }
  }
}
