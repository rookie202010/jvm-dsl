package com.dongjiaqiang.jvm.dsl.java.api.expression

import com.dongjiaqiang.jvm.dsl.api.expression.visitor.ExpressionVisitor

abstract class CustomBlockExpressionTranslator(val javaTranslatorContext: JavaTranslatorContext){
  def translate(javaCustomBlockExpression: JavaCustomBlockExpression,
                visitor: ExpressionVisitor[String]): String

  def name:String
}
