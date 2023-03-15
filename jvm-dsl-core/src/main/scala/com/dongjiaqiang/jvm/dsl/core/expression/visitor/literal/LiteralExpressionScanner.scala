package com.dongjiaqiang.jvm.dsl.core.expression.visitor.literal

import com.dongjiaqiang.jvm.dsl.api.expression.`var`.Null
import com.dongjiaqiang.jvm.dsl.api.expression.literal._
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.ExpressionVisitor
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.literal.LiteralExpressionVisitor

trait LiteralExpressionScanner extends LiteralExpressionVisitor[Unit] {
  override def visit(literal: IntLiteral, visitor: ExpressionVisitor[Unit]): Unit = {

  }

  override def visit(literal: LongLiteral, visitor: ExpressionVisitor[Unit]): Unit = {

  }

  override def visit(literal: DoubleLiteral, visitor: ExpressionVisitor[Unit]): Unit = {

  }

  override def visit(literal: FloatLiteral, visitor: ExpressionVisitor[Unit]): Unit = {

  }

  override def visit(literal: BoolLiteral, visitor: ExpressionVisitor[Unit]): Unit = {

  }

  override def visit(literal: StringLiteral, visitor: ExpressionVisitor[Unit]): Unit = {

  }

  override def visit(literal: CharLiteral, visitor: ExpressionVisitor[Unit]): Unit = {

  }

  override def visit(literal: ListLiteral, visitor: ExpressionVisitor[Unit]): Unit = {
    val expressions = literal.literal
    expressions.foreach( visitor.visit )
  }


  override def visit(literal: ArrayLiteral, visitor: ExpressionVisitor[Unit]): Unit = {
    val expressions = literal.literal
    expressions.foreach( visitor.visit )
  }

  override def visit(literal: EitherLiteral, visitor: ExpressionVisitor[Unit]): Unit = {
    literal.literal match {
      case Left( left ) ⇒ visitor.visit( left )
      case Right( right ) ⇒ visitor.visit( right )
    }
  }

  override def visit(literal: MapLiteral, visitor: ExpressionVisitor[Unit]): Unit = {
    val expressions = literal.literal
    expressions.foreach {
      case (k, v) ⇒
        visitor.visit( k )
        visitor.visit( v )
    }
  }

  override def visit(literal: SetLiteral, visitor: ExpressionVisitor[Unit]): Unit = {
    val expressions = literal.literal
    expressions.foreach( visitor.visit )
  }

  override def visit(literal: TupleLiteral, visitor: ExpressionVisitor[Unit]): Unit = {
    val expressions = literal.literal
    expressions.foreach( visitor.visit )
  }

  override def visit(literal: ClazzLiteral, visitor: ExpressionVisitor[Unit]): Unit = {
    val expressions = literal.literal
    expressions.foreach( visitor.visit )
  }

  override def visit(literal: OptionLiteral, visitor: ExpressionVisitor[Unit]): Unit = {
    val expression = literal.literal
    visitor.visit( expression )
  }

  override def visit(literal: Null.type, visitor: ExpressionVisitor[Unit]): Unit = {

  }
}
