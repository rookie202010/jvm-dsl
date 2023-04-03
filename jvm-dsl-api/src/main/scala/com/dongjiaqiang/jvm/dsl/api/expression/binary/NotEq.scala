package com.dongjiaqiang.jvm.dsl.api.expression.binary

import com.dongjiaqiang.jvm.dsl.api.`type`.{BoolType, DslType}
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope

case class NotEq(left: ValueExpression, right: ValueExpression) extends BinaryExpression {
  override def toString: String = s"$left !=  $right"

  override def equals(obj: Any): Boolean = obj match {
    case notEq: NotEq ⇒
      notEq.left == left && notEq.right == right
    case _ ⇒ false
  }

  override def getValueType(programScope: ProgramScope): DslType = BoolType
}
