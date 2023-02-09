package com.dongjiaqiang.jvm.dsl.core.expression.generator

import com.dongjiaqiang.jvm.dsl.api.`type`.ClazzType
import com.dongjiaqiang.jvm.dsl.api.expression.{BoolLiteral, CharLiteral, ClazzLiteral, DoubleLiteral, Expression, FloatLiteral, FuncCall, FuncCallChain, IntLiteral, ListLiteral, LiteralCall, LiteralCallChain, LongLiteral, MapLiteral, MethodCall, OptionLiteral, Part, SetLiteral, StaticCall, StringLiteral, TupleLiteral, VarCall, VarName, VarRef}
import com.dongjiaqiang.jvm.dsl.core.JvmDslParserParser._
import com.dongjiaqiang.jvm.dsl.core.parser.ExprContext
import com.dongjiaqiang.jvm.dsl.core.scope.toDslType

import scala.collection.convert.ImplicitConversionsToScala._

object CallChainGenerator extends IExpressionGenerator[LiteralAndCallChainContext, Expression,GeneratorContext] {

  def partExpression(partContext: PartContext,
                     expressionContext: ExprContext,
                     generatorContext: GeneratorContext = NoneGeneratorContext): Part = {
    if (partContext.variable( ) != null) {
      VarName( partContext.variable( ).IDENTIFIER( ).map( _.getText ).mkString( "." ) )
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

  def generateMethodCall(exprContext: ExprContext,funcName:String,expressions: List[ExpressionContext]):MethodCall={
    new MethodCall( exprContext.resolveMethod(funcName),
      funcName,
      expressions.map( e ⇒ ExpressionGenerator.generate( exprContext, e ) ).toArray )
  }

  def generator(expressionContext: ExprContext,
                funcName: String, variable: VariableContext, expressions: List[ExpressionContext]): FuncCall = {
    Option.apply(variable) match {
      case Some(v) ⇒
        if (v.IDENTIFIER().length == 1 &&
          expressionContext.getProgramScope.isImportClazz(v.IDENTIFIER().head.getText)) {
          StaticCall(ClazzType(v.IDENTIFIER().head.getText, Array()),
            funcName,
            expressions.map(e ⇒ ExpressionGenerator.generate(expressionContext, e)).toArray)
        } else {
          VarCall(VarGenerator.generate(expressionContext, v), funcName,
            expressions.map(e ⇒ ExpressionGenerator.generate(expressionContext, e)).toArray)
        }
      case None ⇒
        generateMethodCall(expressionContext, funcName, expressions)
    }
  }

  def generator(expressionContext: ExprContext,
                funcName: String, typeContext: TypeContext,
                expressions: List[ExpressionContext]): FuncCall = {
    Option.apply( typeContext )
      .map( t ⇒ toDslType( t ) )
    match {
      case Some( t ) ⇒
        StaticCall( t, funcName, expressions.map( e ⇒ ExpressionGenerator.generate( expressionContext, e ) ).toArray )
      case _ ⇒
        generateMethodCall(expressionContext,funcName,expressions)
    }
  }

  def generator(expressionContext: ExprContext,
                funcName: String, literalContext: LiteralContext,
                expressions: List[ExpressionContext]): FuncCall = {
    Option.apply( literalContext ).map( l ⇒ LiteralGenerator.generate( expressionContext, l ) )
    match {
      case Some( l ) ⇒
        new LiteralCall( l, funcName, expressions.map( e ⇒ ExpressionGenerator.generate( expressionContext, e ) ).toArray )
      case _ ⇒
        generateMethodCall(expressionContext,funcName,expressions)
    }
  }


  override def generate(exprContext: ExprContext,
                        ruleContext: LiteralAndCallChainContext,
                        generatorContext: GeneratorContext = NoneGeneratorContext): Expression = {
    ruleContext match {
      case c: LiteralExprContext ⇒
        LiteralGenerator.generate( exprContext, c.literal( ) )
      case c: LiteralCallChainExprContext ⇒

       val expression = LiteralGenerator.generate(exprContext,c.literalCallChain().literal())
       val parts = c.literalCallChain( ).part( ).map( p ⇒ {
          partExpression( p, exprContext )
        } ).toList

        expression match {
          case intLiteral:IntLiteral⇒new LiteralCallChain(intLiteral,parts)
          case longLiteral:LongLiteral⇒new LiteralCallChain(longLiteral,parts)
          case floatLiteral:FloatLiteral⇒new LiteralCallChain(floatLiteral,parts)
          case doubleLiteral:DoubleLiteral⇒new LiteralCallChain(doubleLiteral,parts)
          case stringLiteral:StringLiteral⇒new LiteralCallChain(stringLiteral,parts)
          case charLiteral:CharLiteral⇒new LiteralCallChain(charLiteral,parts)
          case boolLiteral:BoolLiteral⇒new LiteralCallChain(boolLiteral,parts)

          case clazzLiteral: ClazzLiteral⇒ new LiteralCallChain(clazzLiteral,parts)

          case optional:OptionLiteral⇒new LiteralCallChain(optional,parts)
          case setLiteral:SetLiteral⇒new LiteralCallChain(setLiteral,parts)
          case tupleLiteral:TupleLiteral⇒new LiteralCallChain(tupleLiteral,parts)
          case listLiteral:ListLiteral⇒new LiteralCallChain(listLiteral,parts)
          case mapLiteral:MapLiteral⇒new LiteralCallChain(mapLiteral,parts)

          case varRef:VarRef⇒
            val index = parts.indexWhere(_.isInstanceOf[MethodCall])
            if(index == -1){
              VarRef(varRef.name++parts.map(_.asInstanceOf[VarName]).map(_.name),varRef.fieldScope)
            }else{
              val methodCall = parts(index).asInstanceOf[MethodCall]
              val leftParts = parts.slice(index+1,parts.length)

              val varCall = VarCall(VarRef(varRef.name++parts.slice(0,index).map(_.asInstanceOf[VarName]).map(_.name),varRef.fieldScope)
                ,methodCall.name,methodCall.params)
              if(leftParts.isEmpty){
                  varCall
              }else{
                  FuncCallChain(varCall,leftParts)
              }
            }
        }
      case c: FuncCallChainExprContext ⇒
        val funcCall = FuncCall.generate( exprContext, c.funcCallChain( ).funcCall( ) )
        val parts = c.funcCallChain( ).part( ).map( p ⇒ {
          partExpression( p, exprContext )
        } )
        if(parts.isEmpty){
          funcCall
        }else {
          FuncCallChain( funcCall, parts.toList )
        }
    }
  }
}


object FuncCall extends IExpressionGenerator[FuncCallContext,FuncCall,GeneratorContext]{
  override def generate(exprContext: ExprContext,
                        ruleContext: FuncCallContext,
                        generatorContext: GeneratorContext = NoneGeneratorContext): FuncCall = {
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

object VarCallNoArgs extends IExpressionGenerator[VarCallNoArgsContext,FuncCall,GeneratorContext]{
  override def generate(exprContext: ExprContext,
                        ruleContext: VarCallNoArgsContext,
                        generatorContext: GeneratorContext = NoneGeneratorContext): FuncCall = {
    CallChainGenerator
      .generator( exprContext,
        ruleContext.funcName( ).IDENTIFIER( ).getText, ruleContext.variable( ), List( ) )
  }
}

object VarCallArgs extends IExpressionGenerator[VarCallArgsContext,FuncCall,GeneratorContext]{
  override def generate(exprContext: ExprContext,
                        ruleContext: VarCallArgsContext,
                        generatorContext: GeneratorContext = NoneGeneratorContext): FuncCall = {
    CallChainGenerator
      .generator( exprContext,
        ruleContext.funcName( ).IDENTIFIER( ).getText, ruleContext.variable( ),
        ruleContext.expression( ).toList )
  }
}

object TypeCallNoArgs extends IExpressionGenerator[TypeCallNoArgsContext,FuncCall,GeneratorContext]{
  override def generate(exprContext: ExprContext,
                        ruleContext: TypeCallNoArgsContext,
                        generatorContext: GeneratorContext = NoneGeneratorContext): FuncCall = {
    CallChainGenerator.generator( exprContext, ruleContext.funcName( ).IDENTIFIER( ).getText,
      ruleContext.`type`( ), List( ) )
  }
}

object TypeCallArgs extends IExpressionGenerator[TypeCallArgsContext,FuncCall,GeneratorContext] {
  override def generate(exprContext: ExprContext,
                        ruleContext: TypeCallArgsContext,
                        generatorContext: GeneratorContext = NoneGeneratorContext): FuncCall = {
    CallChainGenerator.generator( exprContext, ruleContext.funcName( ).IDENTIFIER( ).getText,
      ruleContext.`type`( ), ruleContext.expression( ).toList )
  }
}

object LiteralCallNoArgs extends IExpressionGenerator[LiteralCallNoArgsContext,FuncCall,GeneratorContext]{
  override def generate(exprContext: ExprContext,
                        ruleContext: LiteralCallNoArgsContext,
                        generatorContext: GeneratorContext = NoneGeneratorContext): FuncCall = {
    CallChainGenerator.generator( exprContext, ruleContext.funcName( ).IDENTIFIER( ).getText,
      ruleContext.literal( ), List( ) )
  }
}

object LiteralCallArgs extends IExpressionGenerator[LiteralCallArgsContext,FuncCall,GeneratorContext]{
  override def generate(exprContext: ExprContext,
                        ruleContext: LiteralCallArgsContext,
                        generatorContext: GeneratorContext = NoneGeneratorContext): FuncCall = {
    CallChainGenerator.generator( exprContext, ruleContext.funcName( ).IDENTIFIER( ).getText,
      ruleContext.literal( ), ruleContext.expression( ).toList )
  }
}



