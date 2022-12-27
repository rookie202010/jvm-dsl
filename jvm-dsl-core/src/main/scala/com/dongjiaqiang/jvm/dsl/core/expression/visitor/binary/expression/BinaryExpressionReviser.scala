package com.dongjiaqiang.jvm.dsl.core.expression.visitor.binary.expression

import com.dongjiaqiang.jvm.dsl.core.expression.visitor.ExpressionVisitor
import com.dongjiaqiang.jvm.dsl.core.expression._

trait BinaryExpressionReviser extends BinaryExpressionVisitor[Expression] {

  private def revise(binaryExpression: BinaryExpression,
                     visitor: ExpressionVisitor[Expression],
                     reviser: (Expression, Expression) ⇒ BinaryExpression): BinaryExpression = {
    val left = visitor.visit( binaryExpression.left )
    val right = visitor.visit( binaryExpression.right )
    if (binaryExpression.left == left && binaryExpression.right == right) {
      binaryExpression
    } else {
      reviser.apply( left, right )
    }
  }

  override def visit(div: Div, visitor: ExpressionVisitor[Expression]): Expression = {
    revise( div, visitor, (l, r) ⇒ new Div( l, r ) )
  }

  override def visit(mod: Mod, visitor: ExpressionVisitor[Expression]): Expression = {
    revise( mod, visitor, (l, r) ⇒ new Mod( l, r ) )
  }

  override def visit(mul: Mul, visitor: ExpressionVisitor[Expression]): Expression = {
    revise( mul, visitor, (l, r) ⇒ new Mul( l, r ) )
  }

  override def visit(add: Add, visitor: ExpressionVisitor[Expression]): Expression = {
    revise( add, visitor, (l, r) ⇒ new Add( l, r ) )
  }

  override def visit(sub: Sub, visitor: ExpressionVisitor[Expression]): Expression = {
    revise( sub, visitor, (l, r) ⇒ new Sub( l, r ) )
  }

  override def visit(leftShift: LeftShift, visitor: ExpressionVisitor[Expression]): Expression = {
    revise( leftShift, visitor, (l, r) ⇒ new LeftShift( l, r ) )
  }

  override def visit(rightShift: RightShift, visitor: ExpressionVisitor[Expression]): Expression = {
    revise( rightShift, visitor, (l, r) ⇒ new RightShift( l, r ) )
  }

  override def visit(unsignedRightShift: UnsignedRightShift, visitor: ExpressionVisitor[Expression]): Expression = {
    revise( unsignedRightShift, visitor, (l, r) ⇒ new UnsignedRightShift( l, r ) )
  }

  override def visit(lt: Lt, visitor: ExpressionVisitor[Expression]): Expression = {
    revise( lt, visitor, (l, r) ⇒ new Lt( l, r ) )
  }

  override def visit(gt: Gt, visitor: ExpressionVisitor[Expression]): Expression = {
    revise( gt, visitor, (l, r) ⇒ new Gt( l, r ) )
  }

  override def visit(le: Le, visitor: ExpressionVisitor[Expression]): Expression = {
    new Le( visitor.visit( le.left ), visitor.visit( le.right ) )
  }

  override def visit(ge: Ge, visitor: ExpressionVisitor[Expression]): Expression = {
    revise( ge, visitor, (l, r) ⇒ new Ge( l, r ) )
  }

  override def visit(eq: Eq, visitor: ExpressionVisitor[Expression]): Expression = {
    revise( eq, visitor, (l, r) ⇒ new Eq( l, r ) )
  }

  override def visit(ne: NotEq, visitor: ExpressionVisitor[Expression]): Expression = {
    revise( ne, visitor, (l, r) ⇒ new NotEq( l, r ) )
  }

  override def visit(bitAnd: BitAnd, visitor: ExpressionVisitor[Expression]): Expression = {
    revise( bitAnd, visitor, (l, r) ⇒ new BitAnd( l, r ) )
  }

  override def visit(caret: Caret, visitor: ExpressionVisitor[Expression]): Expression = {
    revise( caret, visitor, (l, r) ⇒ new Caret( l, r ) )
  }

  override def visit(bitOr: BitOr, visitor: ExpressionVisitor[Expression]): Expression = {
    revise( bitOr, visitor, (l, r) ⇒ new BitOr( l, r ) )
  }

  override def visit(and: And, visitor: ExpressionVisitor[Expression]): Expression = {
    revise( and, visitor, (l, r) ⇒ new And( l, r ) )
  }

  override def visit(or: Or, visitor: ExpressionVisitor[Expression]): Expression = {
    revise( or, visitor, (l, r) ⇒ new Or( l, r ) )
  }
}
