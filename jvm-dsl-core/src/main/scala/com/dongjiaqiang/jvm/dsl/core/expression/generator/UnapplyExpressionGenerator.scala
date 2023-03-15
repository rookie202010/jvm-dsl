package com.dongjiaqiang.jvm.dsl.core.expression.generator

import com.dongjiaqiang.jvm.dsl.api.`type`._
import com.dongjiaqiang.jvm.dsl.api.exception.ExpressionParseException
import com.dongjiaqiang.jvm.dsl.api.expression._
import com.dongjiaqiang.jvm.dsl.api.expression.block.{MatchClass, MatchHead, MatchList, MatchTuple}
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

        val expressions = c.unapplyExpression( ).map( c ⇒ generateExpr( exprContext, c ) ).toArray
        val clazzName = c.clazzType().getText
        exprContext.getProgramScope.classes.get(clazzName) match {
          case Some(clazzScope)⇒
            Left( MatchClass(  DefinitionClazzType(clazzName,clazzScope),
              expressions ) )
          case None⇒
            clazzName match {
              case "Left" ⇒ Left( MatchClass( LeftType( UnResolvedType ), expressions ) )
              case "Right"⇒ Left(MatchClass(RightType(UnResolvedType),expressions))
              case "Some"⇒ Left(MatchClass(SomeType(UnResolvedType),expressions))
              case "None"⇒ Left(MatchClass(NoneType,Array()))
              case "Success"⇒ Left(MatchClass(SuccessType(UnResolvedType),expressions))
              case "Failure" ⇒ Left(MatchClass(FailureType,expressions))
              case _ ⇒ throw ExpressionParseException(s"class in match class expression must be definite in program scope ${clazzName}")
            }
        }
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
      case Right( clazzName ) ⇒
        clazzName match {
          case "None"⇒ MatchClass(NoneType,Array[Either[Expression,String]]())
          case _⇒ throw ExpressionParseException(s"$clazzName must have fields")
        }

    }
  }
}
