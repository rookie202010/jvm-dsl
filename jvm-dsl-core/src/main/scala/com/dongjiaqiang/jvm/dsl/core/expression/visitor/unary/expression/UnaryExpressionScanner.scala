package com.dongjiaqiang.jvm.dsl.core.expression.visitor.unary.expression
import com.dongjiaqiang.jvm.dsl.core.expression.{Cast, Instanceof, Negate, Opposite, Paren}

/**
 * @author: rookie
 * @mail: dongjiaqiang@qiniu.com
 * @date: 2022/12/26 
 * */
trait UnaryExpressionScanner extends UnaryExpressionVisitor[Unit]{
  override def visitNegate(negate: Negate): Unit = {}

  override def visitOpposite(opposite: Opposite): Unit = {}

  override def visitCast(cast: Cast): Unit = {}

  override def visitInstanceof(instanceof: Instanceof): Unit = {}

  override def visitParen(paren: Paren): Unit = {}
}
