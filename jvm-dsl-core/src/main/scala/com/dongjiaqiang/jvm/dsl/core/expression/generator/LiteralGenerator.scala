package com.dongjiaqiang.jvm.dsl.core.expression.generator

import com.dongjiaqiang.jvm.dsl.core.JvmDslParserParser._
import com.dongjiaqiang.jvm.dsl.core.`type`._
import com.dongjiaqiang.jvm.dsl.core.expression._
import com.dongjiaqiang.jvm.dsl.core.scope.Scope

import scala.collection.convert.ImplicitConversionsToScala._


object LiteralGenerator extends IExpressionGenerator[LiteralContext,Expression] {

  def expression(expressionContext:ExpressionContext,
                  r: LiteralAndCallChainContext):Expression= {
    if (r.literal( ) != null) {
      LiteralGenerator.generate( expressionContext, r.literal( ) )
    } else {
      CallChainGenerator.generate( expressionContext, r.callChain( ) )
    }
  }

  override def generate(expressionContext:ExpressionContext, ruleContext: LiteralContext): Expression = {
    // val varRef = VariableGenerator.generator(currentExpressionIndex,currentScope,ruleContext.variable())
    if(ruleContext.baseLiteral()!=null){
        BaseLiteralGenerator.generate(expressionContext,ruleContext.baseLiteral())
    }else if(ruleContext.classLiteral()!=null){
        ClassLiteralGenerator.generate(expressionContext,ruleContext.classLiteral())
    }else if(ruleContext.variable()!=null){
        VariableGenerator.generate(expressionContext,ruleContext.variable())
    }else if(ruleContext.optionalLiteral()!=null){
        OptionLiteralGenerator.generate(expressionContext,ruleContext.optionalLiteral())
    }else if(ruleContext.listLiteral()!=null){
        ListLiteralGenerator.generate(expressionContext,ruleContext.listLiteral())
    }else if(ruleContext.setLiteral()!=null){
        SetLiteralGenerator.generate(expressionContext,ruleContext.setLiteral())
    }else if(ruleContext.mapLiteral()!=null){
        MapLiteralGenerator.generate(expressionContext,ruleContext.mapLiteral())
    }else if(ruleContext.tupleLiteral()!=null){
        TupleLiteralGenerator.generate(expressionContext,ruleContext.tupleLiteral())
    }else{
        AsyncLiteralGenerator.generate(expressionContext,ruleContext.asyncLiteral())
    }
  }
}

object ClassLiteralGenerator extends IExpressionGenerator[ClassLiteralContext,ClazzLiteral]{
  override def generate(expressionContext:ExpressionContext,
                        ruleContext: ClassLiteralContext): ClazzLiteral = {
    val clazzName = ruleContext.clazzType( ).getText
    val valueTypes = ruleContext.`type`( ).map( DslType.unapply ).toArray
    val clazzType = new ClazzType( clazzName, valueTypes )
    val expressions = ruleContext.literalAndCallChain().map(r⇒{
        LiteralGenerator.expression(expressionContext, r)
    }).toArray
    new ClazzLiteral( expressions, clazzType )
  }
}

object OptionLiteralGenerator extends IExpressionGenerator[OptionalLiteralContext,OptionLiteral] {
  override def generate(expressionContext:ExpressionContext, ruleContext: OptionalLiteralContext): OptionLiteral = {
    val expression = LiteralGenerator.expression( expressionContext,
      ruleContext.literalAndCallChain( ) )
    new OptionLiteral( expression, new OptionType( UnResolvedType ) )
  }
}

object ListLiteralGenerator extends IExpressionGenerator[ListLiteralContext,ListLiteral]{
  override def generate(expressionContext:ExpressionContext,
                        ruleContext: ListLiteralContext): ListLiteral = {
    val expressions = ruleContext.literalAndCallChain( ).map( r ⇒ {
      LiteralGenerator.expression( expressionContext, r )
    } ).toArray
    new ListLiteral(expressions,new ListType(UnResolvedType))
  }
}

object SetLiteralGenerator extends IExpressionGenerator[SetLiteralContext,SetLiteral]{
  override def generate(expressionContext:ExpressionContext, ruleContext: SetLiteralContext): SetLiteral = {
    val expressions = ruleContext.literalAndCallChain( ).map( r ⇒ {
      LiteralGenerator.expression( expressionContext,r )
    } ).toArray
    new SetLiteral( expressions, new SetType( UnResolvedType ) )
  }
}

object TupleLiteralGenerator extends IExpressionGenerator[TupleLiteralContext,TupleLiteral]{
  override def generate(expressionContext:ExpressionContext, ruleContext: TupleLiteralContext): TupleLiteral = {
      val expressions = ruleContext.literalAndCallChain().map(r⇒{
        LiteralGenerator.expression( expressionContext, r )
      }).toArray
    new TupleLiteral(expressions,new TupleType(expressions.indices.map( _⇒UnResolvedType).toArray))
  }
}

object MapLiteralGenerator extends IExpressionGenerator[MapLiteralContext,MapLiteral]{
  override def generate(expressionContext:ExpressionContext, ruleContext: MapLiteralContext): MapLiteral = {
      val expressions = ruleContext.literalAndCallChain().grouped(2).map(kv⇒{
        (LiteralGenerator.expression( expressionContext, kv.head),
          LiteralGenerator.expression( expressionContext, kv.last))
      }).toArray
    new MapLiteral(expressions,new MapType(UnResolvedType,UnResolvedType))
  }
}

object AsyncLiteralGenerator extends IExpressionGenerator[AsyncLiteralContext,AsyncLiteral]{
  override def generate(expressionContext:ExpressionContext, ruleContext: AsyncLiteralContext): AsyncLiteral = {
      val executor = VariableGenerator.generate(expressionContext,ruleContext.variable())
      val block = BlockGenerator.generate(expressionContext,ruleContext.block())
      new AsyncLiteral(block,executor,new FutureType(UnResolvedType))
  }
}

object BaseLiteralGenerator extends IExpressionGenerator[BaseLiteralContext,Expression] {
  override def generate(expressionContext:ExpressionContext, ruleContext: BaseLiteralContext): Expression = {
    if (ruleContext.numberLiteral( ) != null) {
      NumberLiteralGenerator.generate( expressionContext, ruleContext.numberLiteral( ) )
    } else if (ruleContext.BOOL_LITERAL( ) != null) {
      Literal.literal( ruleContext.BOOL_LITERAL( ).getText.toBoolean )
    } else if (ruleContext.CHAR_LITERAL( ) != null) {
      Literal.literal( ruleContext.CHAR_LITERAL( ).getText.head )
    } else {
      Literal.literal( ruleContext.STRING_LITERAL( ).getText )
    }
  }
}

object NumberLiteralGenerator extends IExpressionGenerator[NumberLiteralContext,Expression]{
  override def generate(expressionContext:ExpressionContext, ruleContext: NumberLiteralContext): Expression = {
    if (ruleContext.INT_LITERAL( ) != null) {
      Literal.literal( ruleContext.INT_LITERAL( ).getText.toInt )
    } else if (ruleContext.LONG_LITERAL( ) != null) {
      Literal.literal( ruleContext.LONG_LITERAL( ).getText.toLong )
    } else if (ruleContext.FLOAT_LITERAL( ) != null) {
      Literal.literal( ruleContext.FLOAT_LITERAL( ).getText.toFloat )
    } else {
      Literal.literal( ruleContext.DOUBLE_LITERAL( ).getText.toDouble )
    }
  }

}
