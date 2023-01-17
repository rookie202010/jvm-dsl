package com.dongjiaqiang.jvm.dsl.java.core.translate

import com.dongjiaqiang.jvm.dsl.core.`type`.{DoubleType, DslType, IntType, LongType}
import com.dongjiaqiang.jvm.dsl.core.expression._
import com.dongjiaqiang.jvm.dsl.core.expression.visitor.ExpressionVisitor
import com.dongjiaqiang.jvm.dsl.core.scope.ProgramScope
import com.dongjiaqiang.jvm.dsl.java.core
import com.dongjiaqiang.jvm.dsl.java.core.lambda.supplier.{_1_Supplier, _DoubleSupplier, _IntSupplier, _LongSupplier}
import com.typesafe.config.Config

import java.util.concurrent.atomic.AtomicInteger
import scala.collection.mutable.{ArrayBuffer, Map ⇒ MMap}

case class JavaVarRef(name: List[String]) extends Expression

case class JavaVarCall(varRef: List[String], name: String, params: Array[Expression]) extends Expression

case class JavaLocalVarDef(name: String, dslType: DslType, assigned: Option[Expression]) extends Expression

case class JavaMatchCase(matchedType: DslType, code: String) extends Expression

/**
 * clazz methods generate by java translator
 *
 * clazzName -> methodName -> methodCode
 *
 * program methods generate by java translator
 *
 * methodName -> methodCode
 */
case class JavaTranslatorContext(packageName: String,
                                 javaTranslateConfig: Config,
                                 systemGenerateMethods: MMap[String, ArrayBuffer[String]] = MMap( ),
                                 systemGenerateProgramMethods: ArrayBuffer[String] = ArrayBuffer( ),
                                 systemVarIndex: AtomicInteger = new AtomicInteger( 0 ))

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
      case JavaLocalVarDef( name, dslType, assigned, ) ⇒
        assigned match {
          case None ⇒ s"${core.toJavaType( dslType )} ${name}"
          case Some( expression ) ⇒ s"${core.toJavaType( dslType )} ${name} = ${visitor.visit( expression )}"
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
