package com.dongjiaqiang.jvm.dsl.core.expression.generator

import com.dongjiaqiang.jvm.dsl.core.JvmDslParserParser._
import com.dongjiaqiang.jvm.dsl.core.`type`.ClazzType
import com.dongjiaqiang.jvm.dsl.core.exception.ExpressionParserException
import com.dongjiaqiang.jvm.dsl.core.expression._
import com.dongjiaqiang.jvm.dsl.core.parser.ExprContext

import scala.collection.convert.ImplicitConversionsToScala._

object UnapplyExpressionGenerator extends IExpressionGenerator[UnapplyExpressionContext, Expression] {

  def generateExpr(exprContext: ExprContext, ruleContext: UnapplyExpressionContext): Either[Expression, String] = {
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
        Left( MatchClass( new ClazzType( c.clazzType( ).IDENTIFIER( ).getText, Array( ) ),
          c.unapplyExpression( ).map( c ⇒ generateExpr( exprContext, c ) ).toArray ) )
      case c: UnapplyHeadExprContext ⇒
        val cs = c.unapplyExpression( )
        Left( MatchHead( cs.slice( 0, cs.length - 1 ).map( c ⇒ generateExpr( exprContext, c ) ).toArray,
          generateExpr( exprContext, cs.last ) ) )
    }
  }

  override def generate(exprContext: ExprContext, ruleContext: UnapplyExpressionContext): Expression = {
    generateExpr( exprContext, ruleContext ) match {
      case Left( expression ) ⇒ expression
      //todo
      case Right( _ ) ⇒ throw ExpressionParserException( "" )
    }
  }
}
