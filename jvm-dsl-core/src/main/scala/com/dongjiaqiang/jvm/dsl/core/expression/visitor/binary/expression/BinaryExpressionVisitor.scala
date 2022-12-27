package com.dongjiaqiang.jvm.dsl.core.expression.visitor.binary.expression

import com.dongjiaqiang.jvm.dsl.core.expression._
import com.dongjiaqiang.jvm.dsl.core.expression.visitor.ExpressionVisitor

trait BinaryExpressionVisitor[T] {

  def visit(div: Div, visitor: ExpressionVisitor[T]): T

  def visit(mod: Mod, visitor: ExpressionVisitor[T]): T

  def visit(mul: Mul, visitor: ExpressionVisitor[T]): T

  def visit(add: Add, visitor: ExpressionVisitor[T]): T

  def visit(sub: Sub, visitor: ExpressionVisitor[T]): T

  def visit(leftShift: LeftShift, visitor: ExpressionVisitor[T]): T

  def visit(rightShift: RightShift, visitor: ExpressionVisitor[T]): T

  def visit(unsignedRightShift: UnsignedRightShift, visitor: ExpressionVisitor[T]): T

  def visit(lt: Lt, visitor: ExpressionVisitor[T]): T

  def visit(gt: Gt, visitor: ExpressionVisitor[T]): T

  def visit(le: Le, visitor: ExpressionVisitor[T]): T

  def visit(ge: Ge, visitor: ExpressionVisitor[T]): T

  def visit(eq: Eq, visitor: ExpressionVisitor[T]): T

  def visit(ne: NotEq, visitor: ExpressionVisitor[T]): T

  def visit(bitAnd: BitAnd, visitor: ExpressionVisitor[T]): T

  def visit(caret: Caret, visitor: ExpressionVisitor[T]): T

  def visit(bitOr: BitOr, visitor: ExpressionVisitor[T]): T

  def visit(and: And, visitor: ExpressionVisitor[T]): T

  def visit(or: Or, visitor: ExpressionVisitor[T]): T
}