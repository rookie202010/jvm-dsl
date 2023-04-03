package com.dongjiaqiang.jvm.dsl.api.expression.binary

import com.dongjiaqiang.jvm.dsl.api.`type`.DslType
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression

/**
 * <pre><code>
 * program{
 *        def method()=Unit{
 *          bool condition = 1>2 || 2>1; // 1>2 || 2>1 => Or
 *        }
 * }
 * <pre><code>
 */
case class Or(left: ValueExpression, right: ValueExpression) extends BinaryExpression {
  override def toString: String = s"$left ||  $right"

  override def equals(obj: Any): Boolean = obj match {
    case or: Or ⇒
      or.left == left && or.right == right
    case _ ⇒ false
  }

  override def checkValueType(leftType: DslType, rightType: DslType): Unit = checkRelationExpressionValueType( leftType, rightType )
}
