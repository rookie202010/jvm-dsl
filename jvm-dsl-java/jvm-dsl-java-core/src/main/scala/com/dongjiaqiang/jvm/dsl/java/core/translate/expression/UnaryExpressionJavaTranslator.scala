package com.dongjiaqiang.jvm.dsl.java.core.translate.expression

import com.dongjiaqiang.jvm.dsl.api.`type`.BasicDslType
import com.dongjiaqiang.jvm.dsl.api.expression.unary._
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.ExpressionVisitor
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.unary.expression.UnaryExpressionVisitor
import com.dongjiaqiang.jvm.dsl.java.api
import com.dongjiaqiang.jvm.dsl.java.api.expression.JavaTranslatorContext

trait UnaryExpressionJavaTranslator extends UnaryExpressionVisitor[String] {

  val javaTranslatorContext: JavaTranslatorContext


  override def visit(cast: Cast, visitor: ExpressionVisitor[String]): String = {
    if(cast.flag==1) {
      s"((${api.toJavaType( cast.castType, javaTranslatorContext )})(${visitor.visit( cast.child )}))"
    }else{
      s"((${api.toBasicType( cast.castType, javaTranslatorContext )})(${visitor.visit( cast.child )}))"
    }
  }

  override def visit(negate: Negate, visitor: ExpressionVisitor[String]): String = {
    s"!${visitor.visit( negate.child )}"
  }

  override def visit(opposite: Opposite, visitor: ExpressionVisitor[String]): String = {
    s"(-${visitor.visit( opposite.child )})"
  }

  override def visit(instanceof: Instanceof, visitor: ExpressionVisitor[String]): String = {
    s"${visitor.visit( instanceof.child )} instanceof ${api.toJavaType( instanceof.judgeType,javaTranslatorContext )}"
  }

  override def visit(paren: Paren, visitor: ExpressionVisitor[String]): String = {
    s"(${visitor.visit( paren.child )})"
  }

  override def visit(signedNumber: SignedNumber, visitor: ExpressionVisitor[String]): String = {
    s"${signedNumber.signed}${visitor.visit( signedNumber.child )}"
  }
}
