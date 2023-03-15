package com.dongjiaqiang.jvm.dsl.api.expression.binary

import com.dongjiaqiang.jvm.dsl.api.`type`.DslType
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression

case class Mul(left: ValueExpression, right: ValueExpression) extends BinaryExpression {
  override def toString: String = s"$left*$right"

  override def equals(obj: Any): Boolean = obj match {
    case mul: Mul ⇒
      mul.left == left && mul.right == right
    case _ ⇒ false
  }

  override def checkValueType(leftType: DslType, rightType: DslType): Unit = {
    checkArithmeticExpressionValueType( leftType )
    checkArithmeticExpressionValueType( rightType )
  }
}
