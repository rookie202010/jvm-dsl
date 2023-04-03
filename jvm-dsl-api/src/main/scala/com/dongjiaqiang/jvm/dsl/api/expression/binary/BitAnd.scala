package com.dongjiaqiang.jvm.dsl.api.expression.binary

import com.dongjiaqiang.jvm.dsl.api.`type`.DslType
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression

/**
 * <pre><code>
 * program{
 *    def method()=Unit{
 *        Int value = 1 & 2; // 1 & 2 => BitAnd
 *    }
 * }
 * <pre><code>
 */

case class BitAnd(left: ValueExpression, right: ValueExpression) extends BinaryExpression {
  override def toString: String = s"$left &  $right"

  override def equals(obj: Any): Boolean = obj match {
    case bitAnd: BitAnd ⇒
      bitAnd.left == left && bitAnd.right == right
    case _ ⇒ false
  }

  override def checkValueType(leftType: DslType, rightType: DslType): Unit = {
    checkBitExpressionValueTyp( leftType )
    checkBitExpressionValueTyp( rightType )
  }


}
