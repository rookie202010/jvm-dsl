package com.dongjiaqiang.jvm.dsl.java.core.translate

import com.dongjiaqiang.jvm.dsl.api.`type`._
import com.dongjiaqiang.jvm.dsl.api.expression.Expression
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.ExpressionVisitor
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope
import com.dongjiaqiang.jvm.dsl.java.api.expression._
import com.dongjiaqiang.jvm.dsl.java.api.lambda.supplier._
import com.dongjiaqiang.jvm.dsl.java.core

class JavaTranslator(val javaTranslatorContext: JavaTranslatorContext,
                     override val programScope: ProgramScope) extends ExpressionVisitor[String]
  with UnaryExpressionJavaTranslator
  with BinaryExpressionJavaTranslator
  with VarExpressionJavaTranslator
  with LiteralExpressionJavaTranslator
  with StatementExpressionJavaTranslator
  with MatchCaseExpressionJavaTranslator
  with BlockExpressionJavaTranslator
  with CustomBlockExpressionJavaTranslator {

  override def defaultVisit(expression: Expression, visitor: ExpressionVisitor[String]): String = {
    expression match {
      case JavaVarCall( varRef, name, params ) ⇒
        s"${varRef.mkString( "." )}.${name}(${params.map( visitor.visit ).mkString( "," )})"
      case JavaVarRef( name ) ⇒ name.mkString( "." )
      case JavaLocalVarDef( name, dslType, assigned ) ⇒
        assigned match {
          case None ⇒ s"${core.toJavaType( dslType )} ${name}"
          case Some( expression ) ⇒ s"${core.toJavaType( dslType )} ${name} = ${visitor.visit( expression )}"
        }
      case JavaLambda( lambdaType: LambdaType, lambda ) ⇒

        if (javaTranslatorContext.javaTranslateConfig.getBoolean( "java.translator.lambda.grammar.enable" )) {
          s"(${lambda.inputs.mkString( "," )})->${visitor.visit( lambda )}"
        } else {
          LambdaToAnonymousClassTranslator.translate( lambdaType, lambda, visitor )
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
               |new ${classOf[_1_Supplier[_]].getCanonicalName}<${core.toJavaType( matchedType )}>(){
               |            @Override
               |            public ${core.toJavaType( matchedType )} get() throws Exception {
               |               $code
               |            }
               |}.get()
               |""".stripMargin
        }
    }
  }
}
