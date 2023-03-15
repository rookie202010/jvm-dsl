package com.dongjiaqiang.jvm.dsl.java.core.translate.expression

import com.dongjiaqiang.jvm.dsl.api.expression.binary._
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.ExpressionVisitor
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.binary.expression.BinaryExpressionVisitor
import com.dongjiaqiang.jvm.dsl.java.api.expression.JavaTranslatorContext

trait BinaryExpressionJavaTranslator extends BinaryExpressionVisitor[String] {

  val javaTranslatorContext: JavaTranslatorContext

  override def visit(div: Div, visitor: ExpressionVisitor[String]): String = {
    s"${visitor.visit( div.left )} / ${visitor.visit( div.right )}"
  }

  override def visit(mod: Mod, visitor: ExpressionVisitor[String]): String = {
    s"${visitor.visit( mod.left )} % ${visitor.visit( mod.right )}"
  }

  override def visit(mul: Mul, visitor: ExpressionVisitor[String]): String = {
    s"${visitor.visit( mul.left )} * ${visitor.visit( mul.right )}"
  }

  override def visit(add: Add, visitor: ExpressionVisitor[String]): String = {
    s"${visitor.visit( add.left )} + ${visitor.visit( add.right )}"
  }

  override def visit(sub: Sub, visitor: ExpressionVisitor[String]): String = {
    s"${visitor.visit( sub.left )} - ${visitor.visit( sub.right )}"
  }

  override def visit(leftShift: LeftShift, visitor: ExpressionVisitor[String]): String = {
    s"${visitor.visit( leftShift.left )} << ${visitor.visit( leftShift.right )}"
  }

  override def visit(rightShift: RightShift, visitor: ExpressionVisitor[String]): String = {
    s"${visitor.visit( rightShift.left )} >> ${visitor.visit( rightShift.right )}"
  }

  override def visit(unsignedRightShift: UnsignedRightShift, visitor: ExpressionVisitor[String]): String = {
    s"${visitor.visit( unsignedRightShift.left )} >>> ${visitor.visit( unsignedRightShift.right )}"
  }

  override def visit(lt: Lt, visitor: ExpressionVisitor[String]): String = {
    s"${visitor.visit( lt.left )} < ${visitor.visit( lt.right )}"
  }

  override def visit(gt: Gt, visitor: ExpressionVisitor[String]): String = {
    s"${visitor.visit( gt.left )} > ${visitor.visit( gt.right )}"
  }

  override def visit(le: Le, visitor: ExpressionVisitor[String]): String = {
    s"${visitor.visit( le.left )} <= ${visitor.visit( le.right )}"
  }

  override def visit(ge: Ge, visitor: ExpressionVisitor[String]): String = {
    s"${visitor.visit( ge.left )} >= ${visitor.visit( ge.right )}"
  }

  override def visit(eq: Eq, visitor: ExpressionVisitor[String]): String = {
    s"${visitor.visit( eq.left )} == ${visitor.visit( eq.right )}"
  }

  override def visit(ne: NotEq, visitor: ExpressionVisitor[String]): String = {
    s"${visitor.visit( ne.left )} != ${visitor.visit( ne.right )}"
  }

  override def visit(bitAnd: BitAnd, visitor: ExpressionVisitor[String]): String = {
    s"${visitor.visit( bitAnd.left )} & ${visitor.visit( bitAnd.right )}"
  }

  override def visit(caret: Caret, visitor: ExpressionVisitor[String]): String = {
    s"${visitor.visit( caret.left )} ^ ${visitor.visit( caret.right )}"
  }

  override def visit(bitOr: BitOr, visitor: ExpressionVisitor[String]): String = {
    s"${visitor.visit( bitOr.left )} | ${visitor.visit( bitOr.right )}"
  }

  override def visit(and: And, visitor: ExpressionVisitor[String]): String = {
    s"${visitor.visit( and.left )} && ${visitor.visit( and.right )}"
  }

  override def visit(or: Or, visitor: ExpressionVisitor[String]): String = {
    s"${visitor.visit( or.left )} || ${visitor.visit( or.right )}"
  }
}

