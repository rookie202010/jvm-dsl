package com.dongjiaqiang.jvm.dsl.api.expression.binary

import com.dongjiaqiang.jvm.dsl.api.`type`.DslType
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression

/**
 * <pre><code>
 * program{
 *    def method()=Unit{
 *      bool condition = 1>2 && 2>1; // 1>2 && 2>1 => And
 *    }
 * }
 * <pre><code>
 */
case class And(left: ValueExpression, right: ValueExpression) extends BinaryExpression {
  override def toString: String = s"$left &&  $right"

  override def equals(obj: Any): Boolean = obj match {
    case and: And ⇒
      and.left == left && and.right == right
    case _ ⇒ false
  }

  override def checkValueType(leftType: DslType, rightType: DslType): Unit = checkRelationExpressionValueType( leftType, rightType )
}
