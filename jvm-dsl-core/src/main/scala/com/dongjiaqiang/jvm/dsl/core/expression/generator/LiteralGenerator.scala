package com.dongjiaqiang.jvm.dsl.core.expression.generator

import com.dongjiaqiang.jvm.dsl.core.JvmDslParserParser._
import com.dongjiaqiang.jvm.dsl.core.`type`._
import com.dongjiaqiang.jvm.dsl.core.expression._
import com.dongjiaqiang.jvm.dsl.core.parser.ExprContext

import scala.collection.convert.ImplicitConversionsToScala._


object LiteralGenerator extends IExpressionGenerator[LiteralContext,Expression] {

  def expression(exprContext: ExprContext,
                 r: LiteralAndCallChainContext): Expression = {
    r match {
      case c: LiteralExprContext ⇒
        LiteralGenerator.generate( exprContext, c.literal( ) )
      case _ ⇒
        CallChainGenerator.generate( exprContext, r )
    }
  }

  override def generate(exprContext: ExprContext, ruleContext: LiteralContext): Expression = {
    // val varRef = VariableGenerator.generator(currentExpressionIndex,currentScope,ruleContext.variable())
    if (ruleContext.baseLiteral( ) != null) {
      BaseLiteralGenerator.generate( exprContext, ruleContext.baseLiteral( ) )
    } else if (ruleContext.classLiteral( ) != null) {
      ClassLiteralGenerator.generate( exprContext, ruleContext.classLiteral( ) )
    } else if (ruleContext.variable( ) != null) {
      VarGenerator.generate( exprContext, ruleContext.variable( ) )
    } else if (ruleContext.arrayVariable( ) != null) {

      val indexExpr = OrGenerator.generate( exprContext, ruleContext.arrayVariable( ).conditionalOrExpression( ) )
      val variable = VarGenerator.generate( exprContext, ruleContext.arrayVariable( ).variable( ) )
      new ArrayVarRef( indexExpr, variable.name, variable.fieldScope.dslType, variable.fieldScope )

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
    } else {
      null
    }
  }
}

object ClassLiteralGenerator extends IExpressionGenerator[ClassLiteralContext,ClazzLiteral]{
  override def generate(exprContext: ExprContext,
                        ruleContext: ClassLiteralContext): ClazzLiteral = {
    val clazzName = ruleContext.clazzType( ).getText
    val valueTypes = ruleContext.`type`( ).map( DslType.unapply ).toArray
    val clazzType = new ClazzType( clazzName, valueTypes )
    val expressions = ruleContext.literalAndCallChain( ).map( r ⇒ {
      LiteralGenerator.expression( exprContext, r )
    } ).toArray
    new ClazzLiteral( expressions, clazzType )
  }
}

object OptionLiteralGenerator extends IExpressionGenerator[OptionalLiteralContext,OptionLiteral] {
  override def generate(exprContext: ExprContext, ruleContext: OptionalLiteralContext): OptionLiteral = {
    val expression = LiteralGenerator.expression( exprContext,
      ruleContext.literalAndCallChain( ) )
    new OptionLiteral( expression, new OptionType( UnResolvedType ) )
  }
}

object ListLiteralGenerator extends IExpressionGenerator[ListLiteralContext,ListLiteral]{
  override def generate(exprContext: ExprContext,
                        ruleContext: ListLiteralContext): ListLiteral = {
    val expressions = ruleContext.literalAndCallChain( ).map( r ⇒ {
      LiteralGenerator.expression( exprContext, r )
    } ).toArray
    new ListLiteral( expressions, new ListType( UnResolvedType ) )
  }
}

object SetLiteralGenerator extends IExpressionGenerator[SetLiteralContext,SetLiteral]{
  override def generate(exprContext: ExprContext, ruleContext: SetLiteralContext): SetLiteral = {
    val expressions = ruleContext.literalAndCallChain( ).map( r ⇒ {
      LiteralGenerator.expression( exprContext, r )
    } ).toArray
    new SetLiteral( expressions, new SetType( UnResolvedType ) )
  }
}

object TupleLiteralGenerator extends IExpressionGenerator[TupleLiteralContext,TupleLiteral]{
  override def generate(exprContext: ExprContext, ruleContext: TupleLiteralContext): TupleLiteral = {
    val expressions = ruleContext.literalAndCallChain( ).map( r ⇒ {
      LiteralGenerator.expression( exprContext, r )
    } ).toArray
    new TupleLiteral( expressions, new TupleType( expressions.indices.map( _ ⇒ UnResolvedType ).toArray ) )
  }
}

object MapLiteralGenerator extends IExpressionGenerator[MapLiteralContext,MapLiteral]{
  override def generate(exprContext: ExprContext, ruleContext: MapLiteralContext): MapLiteral = {
    val expressions = ruleContext.literalAndCallChain( ).grouped( 2 ).map( kv ⇒ {
      (LiteralGenerator.expression( exprContext, kv.head ),
        LiteralGenerator.expression( exprContext, kv.last ))
    } ).toArray
    new MapLiteral( expressions, new MapType( UnResolvedType, UnResolvedType ) )
  }
}

object BaseLiteralGenerator extends IExpressionGenerator[BaseLiteralContext,Expression] {
  override def generate(exprContext: ExprContext, ruleContext: BaseLiteralContext): Expression = {
    if (ruleContext.numberLiteral( ) != null) {
      NumberLiteralGenerator.generate( exprContext, ruleContext.numberLiteral( ) )
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
  override def generate(exprContext: ExprContext, ruleContext: NumberLiteralContext): Expression = {
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
