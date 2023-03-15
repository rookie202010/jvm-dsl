package com.dongjiaqiang.jvm.dsl.core.expression.visitor.literal

import com.dongjiaqiang.jvm.dsl.api.expression._
import com.dongjiaqiang.jvm.dsl.api.expression.`var`.Null
import com.dongjiaqiang.jvm.dsl.api.expression.literal._
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.ExpressionVisitor
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.literal.LiteralExpressionVisitor
import com.dongjiaqiang.jvm.dsl.core.expression.visitor.ExpressionReviser


trait LiteralExpressionReviser extends LiteralExpressionVisitor[Expression] {


  override def visit(literal: IntLiteral,
                     visitor: ExpressionVisitor[Expression]): Expression = {
    literal
  }

  override def visit(literal: LongLiteral,
                     visitor: ExpressionVisitor[Expression]): Expression = {
    literal
  }

  override def visit(literal: DoubleLiteral,
                     visitor: ExpressionVisitor[Expression]): Expression = {
    literal
  }

  override def visit(literal: FloatLiteral,
                     visitor: ExpressionVisitor[Expression]): Expression = {
    literal
  }

  override def visit(literal: BoolLiteral,
                     visitor: ExpressionVisitor[Expression]): Expression = {
    literal
  }

  override def visit(literal: StringLiteral,
                     visitor: ExpressionVisitor[Expression]): Expression = {
    literal
  }

  override def visit(literal: CharLiteral,
                     visitor: ExpressionVisitor[Expression]): Expression = {
    literal
  }

  private def revise(literal: ValueExpression {val literal: Array[ValueExpression]},
                     visitor: ExpressionVisitor[Expression], reviser: Array[ValueExpression] ⇒ ValueExpression): ValueExpression = {
    val expressions = ExpressionReviser.revise[ValueExpression, ValueExpression]( literal.literal, visitor )
    if (expressions.isDefined) {
      reviser.apply( expressions.get )
    } else {
      literal
    }
  }

  override def visit(literal: ListLiteral,
                     visitor: ExpressionVisitor[Expression]): Expression = {
    revise( literal, visitor, expressions ⇒ new ListLiteral( expressions, literal.dslType ) )
  }


  override def visit(literal: ArrayLiteral, visitor: ExpressionVisitor[Expression]): Expression = {
    revise( literal, visitor, expressions ⇒ new ArrayLiteral( expressions, literal.dslType ) )
  }

  override def visit(literal: EitherLiteral, visitor: ExpressionVisitor[Expression]): Expression = {
    val either = literal.literal
    either match {
      case Left( left ) ⇒ new EitherLiteral( Left( visitor.visit( left ).asInstanceOf[ValueExpression] ), literal.dslType )
      case Right( right ) ⇒ new EitherLiteral( Right( visitor.visit( right ).asInstanceOf[ValueExpression] ), literal.dslType )
    }
  }

  override def visit(literal: SetLiteral,
                     visitor: ExpressionVisitor[Expression]): Expression = {
    revise( literal, visitor, expressions ⇒ new SetLiteral( expressions, literal.dslType ) )
  }

  override def visit(literal: TupleLiteral,
                     visitor: ExpressionVisitor[Expression]): Expression = {
    revise( literal, visitor, expressions ⇒ new TupleLiteral( expressions, literal.dslType ) )
  }

  override def visit(literal: ClazzLiteral,
                     visitor: ExpressionVisitor[Expression]): Expression = {
    revise(literal,visitor, expressions⇒new ClazzLiteral(expressions,literal.dslType))
  }

  override def visit(literal: MapLiteral,
                     visitor: ExpressionVisitor[Expression]): Expression = {

    val expressions = ExpressionReviser.revise[ValueExpression,ValueExpression,ValueExpression,ValueExpression](literal.literal,visitor,visitor)
    if(expressions.isDefined){
      new MapLiteral(expressions.get,literal.dslType)
    }else{
      literal
    }

  }

  override def visit(literal: OptionLiteral,
                     visitor: ExpressionVisitor[Expression]): Expression = {
    val expression = visitor.visit( literal.literal )
    if (expression != literal.literal) {
      new OptionLiteral( expression.asInstanceOf[ValueExpression], literal.dslType )
    } else {
      literal
    }
  }

  override def visit(literal: Null.type, visitor: ExpressionVisitor[Expression]): Expression = {
    literal
  }
}
