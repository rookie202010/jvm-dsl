package com.dongjiaqiang.jvm.dsl.core.expression.visitor.binary.expression
import com.dongjiaqiang.jvm.dsl.core.expression.{Add, And, BitAnd, BitOr, Caret, Div, Eq, Ge, Gt, Le, LeftShift, Lt, Mod, Mul, NotEq, Or, RightShift, Sub, UnsignedRightShift}

/**
 * @author: rookie
 * @mail: dongjiaqiang@qiniu.com
 * @date: 2022/12/26 
 * */
trait BinaryExpressionScanner extends BinaryExpressionVisitor[Unit]{
  override def visitDiv(div: Div): Unit = {}

  override def visitMod(mod: Mod): Unit = {}

  override def visitMul(mul: Mul): Unit = {}

  override def visitAdd(add: Add): Unit = {}

  override def visitSub(sub: Sub): Unit = {}

  override def visitLeftShift(leftShift: LeftShift): Unit = {}

  override def visitRightShift(rightShift: RightShift): Unit = {}

  override def visitUnsignedRightShift(unsignedRightShift: UnsignedRightShift): Unit = {}

  override def visitLt(lt: Lt): Unit = {}

  override def visitGt(gt: Gt): Unit = {}

  override def visitLe(le: Le): Unit = {}

  override def visitGe(ge: Ge): Unit = {}

  override def visitEq(eq: Eq): Unit = {}

  override def visitNotEq(ne: NotEq): Unit = {}

  override def visitBitAnd(bitAnd: BitAnd): Unit = {}

  override def visitCaret(caret: Caret): Unit = {}

  override def visitBitOr(bitOr: BitOr): Unit = {}

  override def visitAnd(and: And): Unit = {}

  override def visitOr(or: Or): Unit = {}
}
