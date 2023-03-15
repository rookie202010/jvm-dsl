package com.dongjiaqiang.jvm.dsl.core.expression.generator

import com.dongjiaqiang.jvm.dsl.api.`type`._
import com.dongjiaqiang.jvm.dsl.api.exception.ExpressionParseException
import com.dongjiaqiang.jvm.dsl.api.expression._
import com.dongjiaqiang.jvm.dsl.api.expression.`var`.Null
import com.dongjiaqiang.jvm.dsl.api.expression.literal._
import com.dongjiaqiang.jvm.dsl.core.JvmDslParserParser._
import com.dongjiaqiang.jvm.dsl.core.parser.ExprContext
import com.dongjiaqiang.jvm.dsl.core.scope.toDslType

import scala.collection.convert.ImplicitConversionsToScala._


object LiteralGenerator extends IExpressionGenerator[LiteralContext, ValueExpression,GeneratorContext] {

  private def expressionLiteralAndCallChain(exprContext: ExprContext,
                                            r: LiteralAndCallChainContext): ValueExpression = {
    r match {
      case c: LiteralExprContext ⇒
        LiteralGenerator.generate( exprContext, c.literal( ) )
      case _ ⇒
        CallChainGenerator.generate( exprContext, r )
    }
  }

  def expression(exprContext: ExprContext,
                 r: LiteralAndCallChainAndExpressionContext): ValueExpression = {
    r match {
      case c: LiteralAndCallChainExprContext ⇒
        expressionLiteralAndCallChain( exprContext, c.literalAndCallChain( ) )
      case e: ExpressionExprContext ⇒
        ExpressionGenerator.generate( exprContext, e.expression( ) )
    }
  }


  override def generate(exprContext: ExprContext,
                        ruleContext: LiteralContext,
                        generatorContext: GeneratorContext = NoneGeneratorContext): ValueExpression = {
    if (ruleContext.baseLiteral( ) != null) {
      BaseLiteralGenerator.generate( exprContext, ruleContext.baseLiteral( ) )
    } else if (ruleContext.classLiteral( ) != null) {
      ClassLiteralGenerator.generate( exprContext, ruleContext.classLiteral( ) )
    } else if (ruleContext.variable( ) != null) {
      VarRefGenerator.generate( exprContext, ruleContext.variable( ) )
    } else if (ruleContext.optionalLiteral( ) != null) {
      OptionLiteralGenerator.generate( exprContext, ruleContext.optionalLiteral( ) )
    } else if (ruleContext.listLiteral( ) != null) {
      ListLiteralGenerator.generate( exprContext, ruleContext.listLiteral( ) )
    } else if (ruleContext.setLiteral( ) != null) {
      SetLiteralGenerator.generate( exprContext, ruleContext.setLiteral( ) )
    } else if (ruleContext.mapLiteral( ) != null) {
      MapLiteralGenerator.generate( exprContext, ruleContext.mapLiteral( ) )
    } else if (ruleContext.tupleLiteral( ) != null) {
      TupleLiteralGenerator.generate( exprContext, ruleContext.tupleLiteral( ) )
    } else if (ruleContext.nulLiteral( ) != null) {
      Null
    } else {
      throw ExpressionParseException("never happen here!")
    }
  }
}

object ClassLiteralGenerator extends IExpressionGenerator[ClassLiteralContext,ValueExpression,GeneratorContext]{
  override def generate(exprContext: ExprContext,
                        ruleContext: ClassLiteralContext,
                        generatorContext: GeneratorContext = NoneGeneratorContext): ValueExpression = {
    val clazzName = ruleContext.clazzType( ).getText
    val valueTypes = ruleContext.`type`( ).map( t⇒toDslType(t,exprContext.getProgramScope) ).toArray
    val expressions = ruleContext.literalAndCallChainAndExpression( ).map( r ⇒ {
      LiteralGenerator.expression( exprContext, r )
    } ).toArray

    exprContext.getProgramScope.classes.get(clazzName) match {
      case Some(clazzScope)⇒
        val clazzType = DefinitionClazzType(clazzName,clazzScope)
        new ClazzLiteral(expressions,clazzType)
      case None⇒
        val clazzType = ClazzType( clazzName, valueTypes )
        new ClazzLiteral( expressions, clazzType )
    }

  }
}

object OptionLiteralGenerator extends IExpressionGenerator[OptionalLiteralContext,OptionLiteral,GeneratorContext] {
  override def generate(exprContext: ExprContext,
                        ruleContext: OptionalLiteralContext,
                        generatorContext: GeneratorContext = NoneGeneratorContext): OptionLiteral = {
    val expression = LiteralGenerator.expression( exprContext,
      ruleContext.literalAndCallChainAndExpression())
    new OptionLiteral( expression, OptionType( UnResolvedType ) )
  }
}

object ListLiteralGenerator extends IExpressionGenerator[ListLiteralContext,ListLiteral,GeneratorContext]{
  override def generate(exprContext: ExprContext,
                        ruleContext: ListLiteralContext,
                        generatorContext: GeneratorContext = NoneGeneratorContext): ListLiteral = {
    val expressions = ruleContext.literalAndCallChainAndExpression().map( r ⇒ {
      LiteralGenerator.expression( exprContext, r )
    } ).toArray
    new ListLiteral( expressions, ListType( UnResolvedType ) )
  }
}

object SetLiteralGenerator extends IExpressionGenerator[SetLiteralContext,SetLiteral,GeneratorContext]{
  override def generate(exprContext: ExprContext,
                        ruleContext: SetLiteralContext,
                        generatorContext: GeneratorContext = NoneGeneratorContext): SetLiteral = {
    val expressions = ruleContext.literalAndCallChainAndExpression().map( r ⇒ {
      LiteralGenerator.expression( exprContext, r )
    } ).toArray
    new SetLiteral( expressions, SetType( UnResolvedType ) )
  }
}

object TupleLiteralGenerator extends IExpressionGenerator[TupleLiteralContext,TupleLiteral,GeneratorContext]{
  override def generate(exprContext: ExprContext,
                        ruleContext: TupleLiteralContext,
                        generatorContext: GeneratorContext = NoneGeneratorContext): TupleLiteral = {
    val expressions = ruleContext.literalAndCallChainAndExpression().map( r ⇒ {
      LiteralGenerator.expression( exprContext, r )
    } ).toArray
    new TupleLiteral( expressions, TupleType( expressions.indices.map( _ ⇒ UnResolvedType ).toArray ) )
  }
}

object MapLiteralGenerator extends IExpressionGenerator[MapLiteralContext,MapLiteral,GeneratorContext]{
  override def generate(exprContext: ExprContext,
                        ruleContext: MapLiteralContext,
                        generatorContext: GeneratorContext = NoneGeneratorContext): MapLiteral = {
    val expressions = ruleContext.literalAndCallChainAndExpression().grouped( 2 ).map( kv ⇒ {
      (LiteralGenerator.expression( exprContext, kv.head ),
        LiteralGenerator.expression( exprContext, kv.last ))
    } ).toArray
    new MapLiteral( expressions, MapType( UnResolvedType, UnResolvedType ) )
  }
}

object BaseLiteralGenerator extends IExpressionGenerator[BaseLiteralContext,ValueExpression,GeneratorContext] {
  override def generate(exprContext: ExprContext,
                        ruleContext: BaseLiteralContext,
                        generatorContext: GeneratorContext = NoneGeneratorContext): ValueExpression = {
    if (ruleContext.numberLiteral( ) != null) {
      NumberLiteralGenerator.generate( exprContext, ruleContext.numberLiteral( ) )
    } else if (ruleContext.BOOL_LITERAL( ) != null) {
      Literal.literal( ruleContext.BOOL_LITERAL( ).getText.toBoolean )
    } else if (ruleContext.CHAR_LITERAL( ) != null) {
      Literal.literal( {
        val charLiteral = ruleContext.CHAR_LITERAL( ).getText.drop(1).dropRight(1)
        if(charLiteral.length==1){
          charLiteral.head
        }else{
          charLiteral.last
        }
      })
    } else {
      Literal.literal( ruleContext.STRING_LITERAL( ).getText )
    }
  }
}

object NumberLiteralGenerator extends IExpressionGenerator[NumberLiteralContext,ValueExpression,GeneratorContext]{
  override def generate(exprContext: ExprContext,
                        ruleContext: NumberLiteralContext,
                        generatorContext: GeneratorContext = NoneGeneratorContext): ValueExpression = {
    if (ruleContext.INT_LITERAL( ) != null) {
      Literal.literal( ruleContext.INT_LITERAL( ).getText.toInt )
    } else if (ruleContext.LONG_LITERAL( ) != null) {
      Literal.literal( ruleContext.LONG_LITERAL( ).getText.dropRight(1).toLong )
    } else if (ruleContext.FLOAT_LITERAL( ) != null) {
      Literal.literal( ruleContext.FLOAT_LITERAL( ).getText.dropRight(1).toFloat )
    } else {
      Literal.literal( ruleContext.DOUBLE_LITERAL( ).getText.dropRight(1).toDouble )
    }
  }

}
