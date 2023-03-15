package com.dongjiaqiang.jvm.dsl.api.expression.binary

import com.dongjiaqiang.jvm.dsl.api.`type`.DslType
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression

/**
 * <pre><code>
 * program{
 * def method()=Unit{
 * Int value = 1 &#94 2; // 1 &#94 2 => BitOr
 * }
 * }
 * <pre><code>
 */
case class Caret(left: ValueExpression, right: ValueExpression) extends BinaryExpression {
  override def toString: String = s"$left ^  $right"

  override def equals(obj: Any): Boolean = obj match {
    case caret: Caret ⇒
      caret.left == left && caret.right == right
    case _ ⇒ false
  }

  override def checkValueType(leftType: DslType, rightType: DslType): Unit = {
    checkBitExpressionValueTyp( leftType )
    checkBitExpressionValueTyp( rightType )
  }

}
