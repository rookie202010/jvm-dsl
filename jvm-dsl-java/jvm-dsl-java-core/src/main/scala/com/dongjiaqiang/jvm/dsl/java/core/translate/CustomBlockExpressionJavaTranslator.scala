package com.dongjiaqiang.jvm.dsl.java.core.translate

import com.dongjiaqiang.jvm.dsl.core.expression.CustomBlockExpression
import com.dongjiaqiang.jvm.dsl.core.expression.visitor.ExpressionVisitor
import com.dongjiaqiang.jvm.dsl.core.expression.visitor.block.BlockExpressionVisitor

trait CustomBlockExpressionJavaTranslator extends BlockExpressionVisitor[String] {

  val javaTranslatorContext: JavaTranslatorContext

  override def visit(customBlockExpression: CustomBlockExpression, visitor: ExpressionVisitor[String]): String = ???
}
