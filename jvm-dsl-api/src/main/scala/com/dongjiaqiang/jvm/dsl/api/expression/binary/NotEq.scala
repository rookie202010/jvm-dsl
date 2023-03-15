package com.dongjiaqiang.jvm.dsl.api.expression.binary

import com.dongjiaqiang.jvm.dsl.api.`type`.DslType
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression

case class NotEq(left: ValueExpression, right: ValueExpression) extends BinaryExpression {
  override def toString: String = s"$left !=  $right"

  override def equals(obj: Any): Boolean = obj match {
    case notEq: NotEq ⇒
      notEq.left == left && notEq.right == right
    case _ ⇒ false
  }

  override def checkValueType(leftType: DslType, rightType: DslType): Unit = checkRelationExpressionValueType( leftType, rightType )
}
