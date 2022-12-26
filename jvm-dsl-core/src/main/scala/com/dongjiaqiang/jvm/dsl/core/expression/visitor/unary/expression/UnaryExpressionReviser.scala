package com.dongjiaqiang.jvm.dsl.core.expression.visitor.unary.expression

import com.dongjiaqiang.jvm.dsl.core.expression.{Cast, Expression, Instanceof, Negate, Opposite, Paren}

/**
 * @author: rookie
 * @mail: dongjiaqiang@qiniu.com
 * @date: 2022/12/26 
 * */
trait UnaryExpressionReviser extends UnaryExpressionVisitor[Expression]{
  override def visitNegate(negate: Negate): Expression = negate

  override def visitOpposite(opposite: Opposite): Expression = opposite

  override def visitCast(cast: Cast): Expression = cast

  override def visitInstanceof(instanceof: Instanceof): Expression = instanceof

  override def visitParen(paren: Paren): Expression = paren
}
