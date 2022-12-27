package com.dongjiaqiang.jvm.dsl.core.expression.visitor.literal

import com.dongjiaqiang.jvm.dsl.core.`type`.{DslType, ListType}
import com.dongjiaqiang.jvm.dsl.core.expression.visitor.{ExpressionReviser, ExpressionVisitor}
import com.dongjiaqiang.jvm.dsl.core.expression.{BoolLiteral, CharLiteral, ClazzLiteral, DoubleLiteral, Expression, FloatLiteral, IntLiteral, ListLiteral, LongLiteral, MapLiteral, OptionLiteral, SetLiteral, StringLiteral, TupleLiteral}

/**
 * @author: rookie
 * @mail: dongjiaqiang@qiniu.com
 * @date: 2022/12/26 
 * */
trait LiteralExpressionReviser extends LiteralExpressionVisitor[Expression]{


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

  def revise(literal: Expression {val literal:Array[Expression]},
             visitor: ExpressionVisitor[Expression], reviser:Array[Expression]⇒Expression): Expression = {
    val expressions = ExpressionReviser.revise[Expression, Expression](literal.literal, visitor)
    if (expressions.isDefined) {
      reviser.apply(expressions.get)
    } else {
      literal
    }
  }

  override def visit(literal: ListLiteral,
                     visitor: ExpressionVisitor[Expression]): Expression = {
    revise(literal,visitor, expressions⇒new ListLiteral(expressions,literal.dslType))
  }

  override def visit(literal: SetLiteral,
                     visitor: ExpressionVisitor[Expression]): Expression = {
    revise(literal,visitor, expressions⇒new SetLiteral(expressions,literal.dslType))
  }

  override def visit(literal: TupleLiteral,
                     visitor: ExpressionVisitor[Expression]): Expression = {
    revise(literal,visitor, expressions⇒new TupleLiteral(expressions,literal.dslType))
  }

  override def visit(literal: ClazzLiteral,
                     visitor: ExpressionVisitor[Expression]): Expression = {
    revise(literal,visitor, expressions⇒new ClazzLiteral(expressions,literal.dslType))
  }


  override def visit(literal: MapLiteral,
                     visitor: ExpressionVisitor[Expression]): Expression = {
    val expressions = literal.literal
    if(expressions.map{
      case (k,v)⇒(visitor.visit(k),visitor.visit(v))
    }.zip(literal.literal).exists{
      case (e1,e2) ⇒ e1._1!=e2._1 || e2._2!=e2._2
    }){
      new MapLiteral(expressions,literal.dslType)
    }else{
      literal
    }
  }


  override def visit(literal: OptionLiteral,
                     visitor: ExpressionVisitor[Expression]): Expression = {
    val expression = visitor.visit(literal.literal)
    if(expression!=literal.literal){
      new OptionLiteral(expression,literal.dslType)
    }else{
      literal
    }
  }
}
