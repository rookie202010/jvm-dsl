package com.dongjiaqiang.jvm.dsl.core.expression.generator

import com.dongjiaqiang.jvm.dsl.core.JvmDslParserParser.BlockExpressionContext
import com.dongjiaqiang.jvm.dsl.core.`type`.{FutureType, TryType, UnResolvedType}
import com.dongjiaqiang.jvm.dsl.core.exception.ExpressionParserException
import com.dongjiaqiang.jvm.dsl.core.expression.{Async, Expression, Try}
import com.dongjiaqiang.jvm.dsl.core.parser.ExprContext

import scala.collection.convert.ImplicitConversionsToScala._

object BlockExpressionGenerator extends IExpressionGenerator[BlockExpressionContext, Expression] {

  override def generate(exprContext: ExprContext, ruleContext: BlockExpressionContext): Expression = {
    val blockType = ruleContext.IDENTIFIER( ).getText
    val fieldScope = Option.apply( ruleContext.variable( ).IDENTIFIER( ).map( _.getText ).toList )
      .flatMap( v ⇒ exprContext.getContextScope.resolveVarRefs( exprContext.getCurrentExpressionIndex, v ) )
    val block = LambdaBlockGenerator.generate( exprContext, ruleContext.lambdaBlock( ) )

    blockType match {
      case "Try" ⇒
        if (ruleContext.variable( ).IDENTIFIER( ) != null) {
          throw ExpressionParserException( "Try block must not define variable" )
        } else {
          new Try( block, new TryType( UnResolvedType ) )
        }
      case "Async" ⇒
        if (fieldScope.isEmpty) {
          throw ExpressionParserException( "Async block must be define variable" )
        } else {
          new Async( block, fieldScope.get, new FutureType( UnResolvedType ) )
        }
      case _ ⇒
        throw ExpressionParserException( "xxx" )
    }
  }
}
