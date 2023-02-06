package com.dongjiaqiang.jvm.dsl.core.expression.generator

import com.dongjiaqiang.jvm.dsl.api.`type`.{FutureType, TryType, UnResolvedType}
import com.dongjiaqiang.jvm.dsl.api.exception.ExpressionParserException
import com.dongjiaqiang.jvm.dsl.api.expression._
import com.dongjiaqiang.jvm.dsl.core.JvmDslParserParser.BlockExpressionContext
import com.dongjiaqiang.jvm.dsl.core.parser.ExprContext

import scala.collection.convert.ImplicitConversionsToScala._

object BlockExpressionGenerator extends IExpressionGenerator[BlockExpressionContext, Expression] {

  override def generate(exprContext: ExprContext, ruleContext: BlockExpressionContext): Expression = {
    if (ruleContext.IDENTIFIER( ) == null) {
      return UnitLiteral
    }
    val blockType = ruleContext.IDENTIFIER( ).getText
    val fieldScope = Option.apply( ruleContext.variable( ) ).map( v ⇒ v.IDENTIFIER( ).map( _.getText ).toList )
      .flatMap( v ⇒ exprContext.getContextScope.resolveVarRefs( exprContext.getCurrentExpressionIndex, v ) )
    val block = LambdaBlockGenerator.generate( exprContext, ruleContext.lambdaBlock( ) )
    blockType match {
      case "Try" ⇒
        if (ruleContext.variable( ) != null) {
          throw ExpressionParserException( "Try block must not define variable" )
        } else {
          Try( block,TryType( UnResolvedType ) )
        }
      case "Async" ⇒
        Async( block, fieldScope, FutureType( UnResolvedType ) )
      case _ ⇒
        block.ignoreVarRefResolved = true
        CustomBlockExpression( blockType, block, fieldScope )
    }
  }
}
