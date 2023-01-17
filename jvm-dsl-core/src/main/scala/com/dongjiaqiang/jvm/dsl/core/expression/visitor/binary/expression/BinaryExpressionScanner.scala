package com.dongjiaqiang.jvm.dsl.core.expression.visitor.binary.expression

import com.dongjiaqiang.jvm.dsl.api.expression.visitor.ExpressionVisitor
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.binary.expression.BinaryExpressionVisitor
import com.dongjiaqiang.jvm.dsl.api.expression._

trait BinaryExpressionScanner extends BinaryExpressionVisitor[Unit] {

  def visitBinaryExpression(binaryExpression: BinaryExpression, visitor: ExpressionVisitor[Unit]): Unit = {
    visitor.visit( binaryExpression.left )
    visitor.visit( binaryExpression.right )
  }

  override def visit(div: Div, visitor: ExpressionVisitor[Unit]): Unit = {
    visitBinaryExpression( div, visitor )
  }

  override def visit(mod: Mod, visitor: ExpressionVisitor[Unit]): Unit = {
    visitBinaryExpression( mod, visitor )
  }

  override def visit(mul: Mul, visitor: ExpressionVisitor[Unit]): Unit = {
    visitBinaryExpression( mul, visitor )
  }

  override def visit(add: Add, visitor: ExpressionVisitor[Unit]): Unit = {
    visitBinaryExpression( add, visitor )
  }

  override def visit(sub: Sub, visitor: ExpressionVisitor[Unit]): Unit = {
    visitBinaryExpression( sub, visitor )
  }

  override def visit(leftShift: LeftShift, visitor: ExpressionVisitor[Unit]): Unit = {
    visitBinaryExpression( leftShift, visitor )
  }

  override def visit(rightShift: RightShift, visitor: ExpressionVisitor[Unit]): Unit = {
    visitBinaryExpression( rightShift, visitor )
  }

  override def visit(unsignedRightShift: UnsignedRightShift, visitor: ExpressionVisitor[Unit]): Unit = {
    visitBinaryExpression( unsignedRightShift, visitor )
  }

  override def visit(lt: Lt, visitor: ExpressionVisitor[Unit]): Unit = {
    visitBinaryExpression( lt, visitor )
  }

  override def visit(gt: Gt, visitor: ExpressionVisitor[Unit]): Unit = {
    visitBinaryExpression( gt, visitor )
  }

  override def visit(le: Le, visitor: ExpressionVisitor[Unit]): Unit = {
    visitBinaryExpression( le, visitor )
  }

  override def visit(ge: Ge, visitor: ExpressionVisitor[Unit]): Unit = {
    visitBinaryExpression( ge, visitor )
  }

  override def visit(eq: Eq, visitor: ExpressionVisitor[Unit]): Unit = {
    visitBinaryExpression( eq, visitor )
  }

  override def visit(ne: NotEq, visitor: ExpressionVisitor[Unit]): Unit = {
    visitBinaryExpression( ne, visitor )
  }

  override def visit(bitAnd: BitAnd, visitor: ExpressionVisitor[Unit]): Unit = {
    visitBinaryExpression( bitAnd, visitor )
  }

  override def visit(caret: Caret, visitor: ExpressionVisitor[Unit]): Unit = {
    visitBinaryExpression( caret, visitor )
  }

  override def visit(bitOr: BitOr, visitor: ExpressionVisitor[Unit]): Unit = {
    visitBinaryExpression( bitOr, visitor )
  }

  override def visit(and: And, visitor: ExpressionVisitor[Unit]): Unit = {
    visitBinaryExpression( and, visitor )
  }

  override def visit(or: Or, visitor: ExpressionVisitor[Unit]): Unit = {
    visitBinaryExpression( or, visitor )
  }
}
