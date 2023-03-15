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
    val fieldScope = Option.apply( ruleContext.variable( ) ).map( v ⇒ {

      val arrayVarRefs = scala.collection.mutable.Set[Int]( )
      val varRefs = v.localVarOrArrayVar( ).zipWithIndex.map {
        case (context, index) ⇒
          if (context.localVariable( ) != null) {
            context.localVariable( ).IDENTIFIER( ).getText
          } else {
            arrayVarRefs.add( index )
            context.localArrayVariable( ).localVariable( ).getText
          }
      }.toList

      (varRefs, arrayVarRefs)
    } )
      .flatMap( v ⇒ exprContext.getContextScope.resolveVarRefs( exprContext.getCurrentExpressionIndex, v._1, v._2.toSet ) )
    val block = LambdaBlockGenerator.generate( exprContext, ruleContext.lambdaBlock( ) )
    blockType match {
      case "Try" ⇒
        if (ruleContext.variable( ) != null) {
          throw ExpressionParseException( "Try block must not define variable" )
        } else {
          Try( block, TryType( UnResolvedType ) )
        }
      case "Async" ⇒
        Async( block, fieldScope, FutureType( UnResolvedType ) )
      case _ ⇒
        block.ignoreVarRefResolved = true
        CustomBlockExpression( blockType, block, fieldScope )
    }
  }
}
