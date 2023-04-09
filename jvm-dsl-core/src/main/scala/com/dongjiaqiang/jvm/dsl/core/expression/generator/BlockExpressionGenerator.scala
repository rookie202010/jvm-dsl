package com.dongjiaqiang.jvm.dsl.core.expression.generator

import com.dongjiaqiang.jvm.dsl.api.`type`.{FutureType, TryType, UnResolvedType}
import com.dongjiaqiang.jvm.dsl.api.exception.ExpressionParseException
import com.dongjiaqiang.jvm.dsl.api.expression._
import com.dongjiaqiang.jvm.dsl.api.expression.block.{Async, CustomBlockExpression, Try}
import com.dongjiaqiang.jvm.dsl.core.JvmDslParserParser.BlockExpressionContext
import com.dongjiaqiang.jvm.dsl.core.parser.ExprContext

import scala.collection.convert.ImplicitConversionsToScala._

object BlockExpressionGenerator extends IExpressionGenerator[BlockExpressionContext, ValueExpression,GeneratorContext] {

  override def generate(exprContext: ExprContext,
                        ruleContext: BlockExpressionContext,
                        generatorContext: GeneratorContext = NoneGeneratorContext): ValueExpression = {
    val blockType = ruleContext.IDENTIFIER( ).getText

    val varRef = Option.apply(ruleContext.variable()).map(v⇒ {
      VarRefGenerator.generate( exprContext, v, VarGeneratorContext( false ) )
    }).flatMap(_.fieldScope)

    val block = LambdaBlockGenerator.generate( exprContext, ruleContext.lambdaBlock( ) )
    blockType match {
      case "Try" ⇒
        if (ruleContext.variable( ) != null) {
          throw ExpressionParseException( "Try block must not define variable" )
        } else {
          Try( block, new TryType( UnResolvedType ) )
        }
      case "Async" ⇒
        Async( block, varRef, FutureType( UnResolvedType ) )
      case _ ⇒
        block.ignoreVarRefResolved = true
        CustomBlockExpression( blockType, block, varRef )
    }
  }
}
