package com.dongjiaqiang.jvm.dsl.api.expression.binary

import com.dongjiaqiang.jvm.dsl.api.`type`.DslType
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression

/**
 * <pre><code>
 * program{
 * def method()=Unit{
 * Bool j = 1==2; // 1==2 => Eq
 * Bool k = 1!=2; // 1!=2 => NotEq
 * }
 * }
 * <pre><code>
 */

case class Eq(left: ValueExpression, right: ValueExpression) extends BinaryExpression {
  override def toString: String = s"$left ==  $right"

  override def equals(obj: Any): Boolean = obj match {
    case eq: Eq ⇒
      eq.left == left && eq.right == right
    case _ ⇒ false
  }

  override def checkValueType(leftType: DslType, rightType: DslType): Unit = checkRelationExpressionValueType( leftType, rightType )
}
