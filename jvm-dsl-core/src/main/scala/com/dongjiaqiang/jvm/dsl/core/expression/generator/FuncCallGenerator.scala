package com.dongjiaqiang.jvm.dsl.core.expression.generator

import com.dongjiaqiang.jvm.dsl.core.JvmDslParserParser._
import com.dongjiaqiang.jvm.dsl.core.`type`.DslType
import com.dongjiaqiang.jvm.dsl.core.expression.{Expression, FuncCall, FuncCallChain, LiteralCall, LiteralCallChain, MethodCall, Part, StaticCall, VarCall, VarName}
import com.dongjiaqiang.jvm.dsl.core.parser.ExprContext

import scala.collection.convert.ImplicitConversionsToScala._

object CallChainGenerator extends IExpressionGenerator[CallChainContext, Expression] {

  def partExpression(partContext: PartContext, expressionContext: ExprContext): Part = {
    if (partContext.variable( ) != null) {
      new VarName( partContext.variable( ).IDENTIFIER( ).map( _.getText ).mkString( "." ) )
    } else {
      partContext.funcCall( ) match {
        case c: VarCallArgsContext ⇒
          VarCallArgs.generate( expressionContext, c )
            .asInstanceOf[MethodCall]
        case c: VarCallNoArgsContext ⇒
          VarCallNoArgs.generate( expressionContext, c )
            .asInstanceOf[MethodCall]
      }
    }
  }

  def generator(expressionContext: ExprContext,
                funcName: String, variable: VariableContext, expressions: List[ExpressionContext]): FuncCall = {
    Option.apply( variable )
      .map( v ⇒ VarGenerator.generate( expressionContext, v ) )
    match {
      case Some( varRef ) ⇒
        new VarCall( varRef, funcName,
          expressions.map( e ⇒ ExpressionGenerator.generate( expressionContext, e ) ) )
      case _ ⇒
        new MethodCall( funcName,
          expressions.map( e ⇒ ExpressionGenerator.generate( expressionContext, e ) ) )
    }
  }

  def generator(expressionContext: ExprContext,
                funcName: String, typeContext: TypeContext,
                expressions: List[ExpressionContext]): FuncCall = {
    Option.apply( typeContext )
      .map( t ⇒ DslType.unapply( t ) )
    match {
      case Some( t ) ⇒
        new StaticCall( t, funcName, expressions.map( e ⇒ ExpressionGenerator.generate( expressionContext, e ) ) )
      case _ ⇒
        new MethodCall( funcName, expressions.map( e ⇒ ExpressionGenerator.generate( expressionContext, e ) ) )
    }
  }

  def generator(expressionContext: ExprContext,
                funcName: String, literalContext: LiteralContext,
                expressions: List[ExpressionContext]): FuncCall = {
    Option.apply( literalContext ).map( l ⇒ LiteralGenerator.generate( expressionContext, l ) )
    match {
      case Some( l ) ⇒
        new LiteralCall( l, funcName, expressions.map( e ⇒ ExpressionGenerator.generate( expressionContext, e ) ) )
      case _ ⇒
        new MethodCall( funcName, expressions.map( e ⇒ ExpressionGenerator.generate( expressionContext, e ) ) )
    }
  }


  override def generate(exprContext: ExprContext,
                        ruleContext: CallChainContext): Expression = {
    if (ruleContext.literal( ) != null) {
      val clazzLiteral = ClassLiteralGenerator.generate( exprContext, ruleContext.literal( ).classLiteral( ) )
      val parts = ruleContext.part( ).map( p ⇒ {
        partExpression( p, exprContext )
      } )
      new LiteralCallChain( clazzLiteral, parts.toList )
    } else {
      val funcCall = FuncCall.generate( exprContext, ruleContext.funcCall( ) )
      val parts = ruleContext.part( ).map( p ⇒ {
        partExpression( p, exprContext )
      } )
      new FuncCallChain( funcCall, parts.toList )
    }
  }
}


object FuncCall extends IExpressionGenerator[FuncCallContext,FuncCall]{
  override def generate(exprContext: ExprContext,
                        ruleContext: FuncCallContext): FuncCall = {
    ruleContext match {
      case c: VarCallNoArgsContext ⇒
        VarCallNoArgs.generate( exprContext, c )
      case c: VarCallArgsContext ⇒
        VarCallArgs.generate( exprContext, c )
      case c: TypeCallNoArgsContext ⇒
        TypeCallNoArgs.generate( exprContext, c )
      case c: TypeCallArgsContext ⇒
        TypeCallArgs.generate( exprContext, c )
        case c:LiteralCallNoArgsContext⇒
          LiteralCallNoArgs.generate( exprContext, c )
        case c:LiteralCallArgsContext⇒
          LiteralCallArgs.generate( exprContext, c )
      }
  }
}

object VarCallNoArgs extends IExpressionGenerator[VarCallNoArgsContext,FuncCall]{
  override def generate(exprContext: ExprContext,
                        ruleContext: VarCallNoArgsContext): FuncCall = {
    CallChainGenerator
      .generator( exprContext,
        ruleContext.funcName( ).IDENTIFIER( ).getText, ruleContext.variable( ), List( ) )
  }
}

object VarCallArgs extends IExpressionGenerator[VarCallArgsContext,FuncCall]{
  override def generate(exprContext: ExprContext,
                        ruleContext: VarCallArgsContext): FuncCall = {
    CallChainGenerator
      .generator( exprContext,
        ruleContext.funcName( ).IDENTIFIER( ).getText, ruleContext.variable( ),
        ruleContext.expression( ).toList )
  }
}

object TypeCallNoArgs extends IExpressionGenerator[TypeCallNoArgsContext,FuncCall]{
  override def generate(exprContext: ExprContext,
                        ruleContext: TypeCallNoArgsContext): FuncCall = {
    CallChainGenerator.generator( exprContext, ruleContext.funcName( ).IDENTIFIER( ).getText,
      ruleContext.`type`( ), List( ) )
  }
}

object TypeCallArgs extends IExpressionGenerator[TypeCallArgsContext,FuncCall] {
  override def generate(exprContext: ExprContext,
                        ruleContext: TypeCallArgsContext): FuncCall = {
    CallChainGenerator.generator( exprContext, ruleContext.funcName( ).IDENTIFIER( ).getText,
      ruleContext.`type`( ), ruleContext.expression( ).toList )
  }
}

object LiteralCallNoArgs extends IExpressionGenerator[LiteralCallNoArgsContext,FuncCall]{
  override def generate(exprContext: ExprContext,
                        ruleContext: LiteralCallNoArgsContext): FuncCall = {
    CallChainGenerator.generator( exprContext, ruleContext.funcName( ).IDENTIFIER( ).getText,
      ruleContext.literal( ), List( ) )
  }
}

object LiteralCallArgs extends IExpressionGenerator[LiteralCallArgsContext,FuncCall]{
  override def generate(exprContext: ExprContext,
                        ruleContext: LiteralCallArgsContext): FuncCall = {
    CallChainGenerator.generator( exprContext, ruleContext.funcName( ).IDENTIFIER( ).getText,
      ruleContext.literal( ), ruleContext.expression( ).toList )
  }
}



