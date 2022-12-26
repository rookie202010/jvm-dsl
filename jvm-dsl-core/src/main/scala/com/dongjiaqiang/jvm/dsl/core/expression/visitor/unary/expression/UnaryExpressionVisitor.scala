package com.dongjiaqiang.jvm.dsl.core.expression.visitor.unary.expression

import com.dongjiaqiang.jvm.dsl.core.expression._

/**
 * @author: rookie
 * @mail: dongjiaqiang@qiniu.com
 * @date: 2022/12/26 
 * */
trait UnaryExpressionVisitor[T] {

  def visitNegate(negate: Negate): T

  def visitOpposite(opposite: Opposite): T

  def visitCast(cast: Cast): T

  def visitInstanceof(instanceof: Instanceof): T

  def visitParen(paren: Paren): T
}
