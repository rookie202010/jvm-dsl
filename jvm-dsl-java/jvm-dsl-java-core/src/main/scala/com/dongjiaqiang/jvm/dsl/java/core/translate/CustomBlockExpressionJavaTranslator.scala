package com.dongjiaqiang.jvm.dsl.java.core.translate

import com.dongjiaqiang.jvm.dsl.api.expression.CustomBlockExpression
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.ExpressionVisitor
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.block.BlockExpressionVisitor
import com.dongjiaqiang.jvm.dsl.java.api.expression.JavaTranslatorContext

trait CustomBlockExpressionJavaTranslator extends BlockExpressionVisitor[String] {

  val javaTranslatorContext: JavaTranslatorContext

  override def visit(customBlockExpression: CustomBlockExpression, visitor: ExpressionVisitor[String]): String = ???
}
