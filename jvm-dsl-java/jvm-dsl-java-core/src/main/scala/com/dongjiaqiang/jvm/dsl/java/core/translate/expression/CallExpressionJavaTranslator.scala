package com.dongjiaqiang.jvm.dsl.java.core.translate.expression

import com.dongjiaqiang.jvm.dsl.api.expression.call.{LiteralCall, MethodCall, StaticCall, VarCall}
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.ExpressionVisitor
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.call.CallExpressionVisitor
import com.dongjiaqiang.jvm.dsl.java.api.exception.JavaTranslatorException
import com.dongjiaqiang.jvm.dsl.java.core.translate.method.MultiMethodJavaTranslator


trait CallExpressionJavaTranslator extends CallExpressionVisitor[String]{

  val multiTranslator:MultiMethodJavaTranslator

  override def visit(staticCall: StaticCall, visitor: ExpressionVisitor[String]): String = {
      s"""
      |${staticCall.`type`.toString}.${staticCall.name}(${staticCall.params.map(p⇒visitor.visit(p)).mkString(",")})
      |""".stripMargin
  }

  override def visit(varCall: VarCall, visitor: ExpressionVisitor[String]): String = {
    multiTranslator.visit(varCall.varRef.getValueType(visitor.programScope),varCall.varRef,varCall.name,varCall.params) match {
      case Some(code)⇒code
      case None⇒
          throw JavaTranslatorException(varCall.toString)
    }
  }

  override def visit(methodCall: MethodCall, visitor: ExpressionVisitor[String]): String = {
      s"""
      |${methodCall.name}(${methodCall.params.map(p⇒visitor.visit(p)).mkString(",")})
      |""".stripMargin
  }

  override def visit(literalCall: LiteralCall, visitor: ExpressionVisitor[String]): String = {
    multiTranslator.visit(literalCall.literal.getValueType(visitor.programScope),literalCall.literal,literalCall.name,literalCall.params).get
  }
}
