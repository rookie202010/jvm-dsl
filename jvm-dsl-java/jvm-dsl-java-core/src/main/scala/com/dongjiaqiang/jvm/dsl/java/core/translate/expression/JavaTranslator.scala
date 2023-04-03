package com.dongjiaqiang.jvm.dsl.java.core.translate.expression

import com.dongjiaqiang.jvm.dsl.api.`type`._
import com.dongjiaqiang.jvm.dsl.api.expression.Expression
import com.dongjiaqiang.jvm.dsl.api.expression.block.Block
import com.dongjiaqiang.jvm.dsl.api.expression.unary.Cast
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.ExpressionVisitor
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope
import com.dongjiaqiang.jvm.dsl.java.api
import com.dongjiaqiang.jvm.dsl.java.api.exception.JavaTranslatorException
import com.dongjiaqiang.jvm.dsl.java.api.expression._
import com.dongjiaqiang.jvm.dsl.java.api.lambda.supplier._
import com.dongjiaqiang.jvm.dsl.java.core.translate.method.MultiMethodJavaTranslator

class JavaTranslator(val javaTranslatorContext: JavaTranslatorContext,
                     override val programScope: ProgramScope) extends ExpressionVisitor[String]
  with UnaryExpressionJavaTranslator
  with BinaryExpressionJavaTranslator
  with VarExpressionJavaTranslator
  with LiteralExpressionJavaTranslator
  with StatementExpressionJavaTranslator
  with MatchCaseExpressionJavaTranslator
  with CallExpressionJavaTranslator
  with CallChainExpressionJavaTranslator
  with BlockExpressionJavaTranslator {

  override val multiTranslator: MultiMethodJavaTranslator = new MultiMethodJavaTranslator(programScope,this)

  def specifyDslType(code:String,dslType: DslType):String={
    if (javaTranslatorContext.genericErasure( )) {
      dslType match {
        case IntType⇒
          s"((${api.toJavaType( dslType, javaTranslatorContext )})($code)).intValue()"
        case LongType⇒
          s"((${api.toJavaType( dslType, javaTranslatorContext )})($code)).longValue()"
        case FloatType⇒
          s"((${api.toJavaType( dslType, javaTranslatorContext )})($code)).floatValue()"
        case DoubleType⇒
          s"((${api.toJavaType( dslType, javaTranslatorContext )})($code)).doubleValue()"
        case ByteType⇒
          s"((${api.toJavaType( dslType, javaTranslatorContext )})($code)).byteValue()"
        case _⇒
          s"((${api.toJavaType( dslType, javaTranslatorContext )})($code))"
      }
    } else {
      code
    }
  }

  override def defaultVisit(expression: Expression, visitor: ExpressionVisitor[String]): String = {
    expression match {
      case JavaCode(code,_)⇒
          code
      case JavaCall( JavaCode(code,_), name, params,_ ) ⇒
        s"$code.$name(${params.map( visitor.visit ).mkString( "," )})"
      case JavaLocalVarDef( name, dslType, assigned ) ⇒
        assigned match {
          case None ⇒ s"${api.toJavaType( dslType,javaTranslatorContext )} ${name}"
          case Some( expression ) ⇒ s"${api.toJavaType( dslType,javaTranslatorContext )} ${name} = ${visitor.visit( expression )}"
        }
      case javaCustomBlockExpression: JavaCustomBlockExpression⇒
          javaTranslatorContext.customBlockExpressionJavaTranslators
            .get(javaCustomBlockExpression.customBlockExpression.name)
        match{
            case Some(translator)⇒translator.translate(javaCustomBlockExpression, visitor)
            case None⇒
              throw JavaTranslatorException(s"custom block expression ${javaCustomBlockExpression.customBlockExpression.name} not find translator")
          }
      case JavaLambda( lambdaType: LambdaType, lambda ) ⇒

        if (javaTranslatorContext.javaTranslateConfig.getBoolean( "java.translator.lambda.grammar.enable" )) {
          s"(${lambda.inputs.mkString( "," )})->${visitor.visit( lambda )}"
        } else {
          LambdaToAnonymousClassTranslator.translate( lambdaType, lambda, javaTranslatorContext,visitor,true,false )
        }
      case JavaTry(body:Block,tryType)⇒
        if (javaTranslatorContext.javaTranslateConfig.getBoolean( "java.translator.lambda.grammar.enable" )) {
          s"com.dongjiaqiang.jvm.dsl.java.core.extend.Try.apply(()->${visitor.visit( body )})"
        }else {
          val code =
            s"""
              |new _1_Supplier<${api.toJavaType(tryType.parameterType,javaTranslatorContext)}>(){
              |            @Override
              |            public ${api.toJavaType(tryType.parameterType,javaTranslatorContext)} get() throws Exception
              |                 ${visitor.visit(body)}
              |        }
              |""".stripMargin
          s"com.dongjiaqiang.jvm.dsl.java.core.extend.Try.apply($code)"
        }

      case JavaAsync(body,executor,futureType)⇒

        if (javaTranslatorContext.javaTranslateConfig.getBoolean( "java.translator.lambda.grammar.enable" )) {
          if (futureType.parameterType.isInstanceOf[UnitType.type ]) {
            executor match {
              case Some(executor) ⇒
                s"com.dongjiaqiang.jvm.dsl.java.core.runAsync(()->${visitor.visit(body)},${executor.symbolName})"
              case None ⇒
                s"com.dongjiaqiang.jvm.dsl.java.core.runAsync(()->${visitor.visit(body)})"
            }
          } else {
            executor match {
              case Some(executor) ⇒
                s"com.dongjiaqiang.jvm.dsl.java.core.supplyAsync(()->${visitor.visit(body)},${executor.symbolName})"
              case None ⇒
                s"com.dongjiaqiang.jvm.dsl.java.core.supplyAsync(()->${visitor.visit(body)})"
            }
          }
        }else{
          if(futureType.parameterType.isInstanceOf[UnitType.type ]){
            val code =
              s"""
                |new _Runnable() {
                |     @Override
                |     public void run() throws Exception
                |         ${visitor.visit(body)}
                | }
                |""".stripMargin
            executor match {
              case Some(executor) ⇒
                s"com.dongjiaqiang.jvm.dsl.java.core.runAsync($code,${executor.symbolName})"
              case None ⇒
                s"com.dongjiaqiang.jvm.dsl.java.core.runAsync($code)"
            }
          }else{
            val code =
              s"""
                 |new _1_Supplier<${api.toJavaType(futureType.parameterType,javaTranslatorContext)}>(){
                 |     @Override
                 |     public ${api.toJavaType(futureType.parameterType,javaTranslatorContext)} get() throws Exception
                 |        ${visitor.visit(body)}
                 |}
                 |""".stripMargin
            executor match {
              case Some(executor) ⇒
                s"com.dongjiaqiang.jvm.dsl.java.core.supplyAsync($code,${executor.symbolName})"
              case None ⇒
                s"com.dongjiaqiang.jvm.dsl.java.core.supplyAsync($code)"
            }
          }
        }

      case JavaMatchCase( matchedType, code ) ⇒
        matchedType match {
          case IntType ⇒
            s"""
               |new ${classOf[_IntSupplier].getCanonicalName}(){
               |            @Override
               |            public int getAsInt() throws Exception {
               |               $code
               |            }
               |}.getAsInt()
               |""".stripMargin
          case LongType ⇒
            s"""
               |new ${classOf[_LongSupplier].getCanonicalName}(){
               |            @Override
               |            public int getAsLong() throws Exception {
               |               $code
               |            }
               |}.getAsLong()
               |""".stripMargin
          case DoubleType ⇒
            s"""
               |new ${classOf[_DoubleSupplier].getCanonicalName}(){
               |            @Override
               |            public double getAsDouble() throws Exception {
               |               $code
               |            }
               |}.getAsDouble()
               |""".stripMargin
          case BoolType ⇒
            s"""
               |new ${classOf[_BooleanSupplier].getCanonicalName}(){
               |            @Override
               |            public boolean getAsBoolean() throws Exception {
               |               $code
               |            }
               |}.getAsBoolean
               |""".stripMargin
          case _ ⇒
            s"""
               |new ${classOf[_1_Supplier[_]].getCanonicalName}<${api.toJavaType( matchedType,javaTranslatorContext )}>(){
               |            @Override
               |            public ${api.toJavaType( matchedType,javaTranslatorContext )} get() throws Exception {
               |               $code
               |            }
               |}.get()
               |""".stripMargin
        }
    }
  }
}
