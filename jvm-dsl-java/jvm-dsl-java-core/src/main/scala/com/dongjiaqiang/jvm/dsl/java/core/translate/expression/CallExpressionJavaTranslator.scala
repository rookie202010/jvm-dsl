package com.dongjiaqiang.jvm.dsl.java.core.translate.expression

import com.dongjiaqiang.jvm.dsl.api.expression.call.{LiteralCall, MethodCall, StaticCall, VarCall}
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.ExpressionVisitor
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.call.CallExpressionVisitor


trait CallExpressionJavaTranslator extends CallExpressionVisitor[String]{

  override def visit(staticCall: StaticCall, visitor: ExpressionVisitor[String]): String = {
      s"""
      |${staticCall.`type`.toString}.${staticCall.name}(${staticCall.params.map(p⇒visitor.visit(p)).mkString(",")})
      |""".stripMargin
  }

  override def visit(varCall: VarCall, visitor: ExpressionVisitor[String]): String = {
      s"""
      |${visitor.visit(varCall.varRef)}.${varCall.name}(${varCall.params.map(p⇒visitor.visit(p)).mkString(",")})
      |""".stripMargin
  }

  override def visit(methodCall: MethodCall, visitor: ExpressionVisitor[String]): String = {
      s"""
      |${methodCall.name}(${methodCall.params.map(p⇒visitor.visit(p)).mkString(",")})
      |""".stripMargin
  }

  override def visit(literalCall: LiteralCall, visitor: ExpressionVisitor[String]): String = super.visit( literalCall, visitor )
}
