package com.dongjiaqiang.jvm.dsl.api.expression.binary

import com.dongjiaqiang.jvm.dsl.api.`type`.DslType
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope

case class RightShift(left: ValueExpression, right: ValueExpression) extends BinaryExpression {
  override def toString: String = s"$left>>$right"

  override def equals(obj: Any): Boolean = obj match {
    case rightShift: RightShift ⇒
      rightShift.left == left && rightShift.right == right
    case _ ⇒ false
  }

  override def checkValueType(leftType: DslType, rightType: DslType): Unit = {
    checkBitExpressionValueTyp( leftType )
    checkBitExpressionValueTyp( rightType )
  }

  override def getValueType(programScope: ProgramScope): DslType = {
    left.getValueType( programScope )
    //resolveBitExpressionValueType(leftValueType,rightValueType,onlyAcceptLeftValueType = true)
  }

}
