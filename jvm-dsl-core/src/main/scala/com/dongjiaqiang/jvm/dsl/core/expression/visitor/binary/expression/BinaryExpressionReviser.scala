package com.dongjiaqiang.jvm.dsl.core.expression.visitor.binary.expression

import com.dongjiaqiang.jvm.dsl.core.expression.{Add, And, BitAnd, BitOr, Caret, Div, Eq, Expression, Ge, Gt, Le, LeftShift, Lt, Mod, Mul, NotEq, Or, RightShift, Sub, UnsignedRightShift}

/**
 * @author: rookie
 * @mail: dongjiaqiang@qiniu.com
 * @date: 2022/12/26 
 * */
trait BinaryExpressionReviser extends BinaryExpressionVisitor[Expression]{
  override def visitDiv(div: Div): Expression = div

  override def visitMod(mod: Mod): Expression = mod

  override def visitMul(mul: Mul): Expression = mul

  override def visitAdd(add: Add): Expression = add

  override def visitSub(sub: Sub): Expression = sub

  override def visitLeftShift(leftShift: LeftShift): Expression = leftShift

  override def visitRightShift(rightShift: RightShift): Expression = rightShift

  override def visitUnsignedRightShift(unsignedRightShift: UnsignedRightShift): Expression = unsignedRightShift

  override def visitLt(lt: Lt): Expression = lt

  override def visitGt(gt: Gt): Expression = gt

  override def visitLe(le: Le): Expression = le

  override def visitGe(ge: Ge): Expression = ge

  override def visitEq(eq: Eq): Expression = eq

  override def visitNotEq(ne: NotEq): Expression = ne

  override def visitBitAnd(bitAnd: BitAnd): Expression = bitAnd

  override def visitCaret(caret: Caret): Expression = caret

  override def visitBitOr(bitOr: BitOr): Expression = bitOr

  override def visitAnd(and: And): Expression = and

  override def visitOr(or: Or): Expression = or
}
