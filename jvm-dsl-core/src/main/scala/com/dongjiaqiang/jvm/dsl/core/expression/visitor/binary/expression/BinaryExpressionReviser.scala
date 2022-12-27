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
    revise( div, visitor, (l, r) ⇒ Div( l, r ) )
  }

  override def visit(mod: Mod, visitor: ExpressionVisitor[Expression]): Expression = {
    revise( mod, visitor, (l, r) ⇒ Mod( l, r ) )
  }

  override def visit(mul: Mul, visitor: ExpressionVisitor[Expression]): Expression = {
    revise( mul, visitor, (l, r) ⇒ Mul( l, r ) )
  }

  override def visit(add: Add, visitor: ExpressionVisitor[Expression]): Expression = {
    revise( add, visitor, (l, r) ⇒ Add( l, r ) )
  }

  override def visit(sub: Sub, visitor: ExpressionVisitor[Expression]): Expression = {
    revise( sub, visitor, (l, r) ⇒ Sub( l, r ) )
  }

  override def visit(leftShift: LeftShift, visitor: ExpressionVisitor[Expression]): Expression = {
    revise( leftShift, visitor, (l, r) ⇒ LeftShift( l, r ) )
  }

  override def visit(rightShift: RightShift, visitor: ExpressionVisitor[Expression]): Expression = {
    revise( rightShift, visitor, (l, r) ⇒ RightShift( l, r ) )
  }

  override def visit(unsignedRightShift: UnsignedRightShift, visitor: ExpressionVisitor[Expression]): Expression = {
    revise( unsignedRightShift, visitor, (l, r) ⇒ UnsignedRightShift( l, r ) )
  }

  override def visit(lt: Lt, visitor: ExpressionVisitor[Expression]): Expression = {
    revise( lt, visitor, (l, r) ⇒ Lt( l, r ) )
  }

  override def visit(gt: Gt, visitor: ExpressionVisitor[Expression]): Expression = {
    revise( gt, visitor, (l, r) ⇒ Gt( l, r ) )
  }

  override def visit(le: Le, visitor: ExpressionVisitor[Expression]): Expression = {
    revise( le, visitor, (l, r) ⇒ Le( l, r ) )
  }

  override def visit(ge: Ge, visitor: ExpressionVisitor[Expression]): Expression = {
    revise( ge, visitor, (l, r) ⇒ Ge( l, r ) )
  }

  override def visit(eq: Eq, visitor: ExpressionVisitor[Expression]): Expression = {
    revise( eq, visitor, (l, r) ⇒ Eq( l, r ) )
  }

  override def visit(ne: NotEq, visitor: ExpressionVisitor[Expression]): Expression = {
    revise( ne, visitor, (l, r) ⇒ NotEq( l, r ) )
  }

  override def visit(bitAnd: BitAnd, visitor: ExpressionVisitor[Expression]): Expression = {
    revise( bitAnd, visitor, (l, r) ⇒ BitAnd( l, r ) )
  }

  override def visit(caret: Caret, visitor: ExpressionVisitor[Expression]): Expression = {
    revise( caret, visitor, (l, r) ⇒ Caret( l, r ) )
  }

  override def visit(bitOr: BitOr, visitor: ExpressionVisitor[Expression]): Expression = {
    revise( bitOr, visitor, (l, r) ⇒ BitOr( l, r ) )
  }

  override def visit(and: And, visitor: ExpressionVisitor[Expression]): Expression = {
    revise( and, visitor, (l, r) ⇒ And( l, r ) )
  }

  override def visit(or: Or, visitor: ExpressionVisitor[Expression]): Expression = {
    revise( or, visitor, (l, r) ⇒ Or( l, r ) )
  }
}
