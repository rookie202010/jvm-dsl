package com.dongjiaqiang.jvm.dsl.java.core.translate

import com.dongjiaqiang.jvm.dsl.api.expression.visitor.ExpressionVisitor
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.unary.expression.UnaryExpressionVisitor
import com.dongjiaqiang.jvm.dsl.api.expression._
import com.dongjiaqiang.jvm.dsl.java.api.expression.JavaTranslatorContext
import com.dongjiaqiang.jvm.dsl.java.core

trait UnaryExpressionJavaTranslator extends UnaryExpressionVisitor[String] {

  val javaTranslatorContext: JavaTranslatorContext


  override def visit(cast: Cast, visitor: ExpressionVisitor[String]): String = {
    s"((${core.toJavaType( cast.castType )})(${visitor.visit( cast.child )}))"
  }

  override def visit(negate: Negate, visitor: ExpressionVisitor[String]): String = {
    s"!${visitor.visit( negate )}"
  }

  override def visit(opposite: Opposite, visitor: ExpressionVisitor[String]): String = {
    s"(-${visitor.visit( opposite.child )})"
  }

  override def visit(instanceof: Instanceof, visitor: ExpressionVisitor[String]): String = {
    s"${visitor.visit( instanceof.child )} instanceof ${core.toJavaType( instanceof.judgeType )}"
  }

  override def visit(paren: Paren, visitor: ExpressionVisitor[String]): String = {
    s"(${visitor.visit( paren.child )})"
  }

}
