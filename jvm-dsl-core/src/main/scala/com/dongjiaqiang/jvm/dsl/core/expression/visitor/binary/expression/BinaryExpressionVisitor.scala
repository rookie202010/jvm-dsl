package com.dongjiaqiang.jvm.dsl.core.expression.visitor.binary.expression

import com.dongjiaqiang.jvm.dsl.core.expression._

/**
 * @author: rookie
 * @mail: dongjiaqiang@qiniu.com
 * @date: 2022/12/26 
 * */
trait BinaryExpressionVisitor[T] {

  def visitDiv(div: Div): T

  def visitMod(mod: Mod): T

  def visitMul(mul: Mul): T

  def visitAdd(add: Add): T

  def visitSub(sub: Sub): T

  def visitLeftShift(leftShift: LeftShift): T

  def visitRightShift(rightShift: RightShift): T

  def visitUnsignedRightShift(unsignedRightShift: UnsignedRightShift): T

  def visitLt(lt: Lt): T

  def visitGt(gt: Gt): T

  def visitLe(le: Le): T

  def visitGe(ge: Ge): T

  def visitEq(eq: Eq): T

  def visitNotEq(ne: NotEq): T

  def visitBitAnd(bitAnd: BitAnd): T

  def visitCaret(caret: Caret): T

  def visitBitOr(bitOr: BitOr): T

  def visitAnd(and: And): T

  def visitOr(or: Or): T
}
