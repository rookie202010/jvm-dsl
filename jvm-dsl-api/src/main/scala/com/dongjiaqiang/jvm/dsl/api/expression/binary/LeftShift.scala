package com.dongjiaqiang.jvm.dsl.api.expression.binary

import com.dongjiaqiang.jvm.dsl.api.`type`.DslType
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope

/**
 * <pre><code>
 * program{
 * def method()=Unit{
 * Int j = 1&lt;&lt;2; // 1&lt;&lt;2 => LeftShit
 * Int k = 1>>2; // 1>>2 => RightShift
 * Int i = 1>>>2; // 1>>>2 => UnsignedRightShift
 * }
 * }
 * <pre><code>
 */
case class LeftShift(left: ValueExpression, right: ValueExpression) extends BinaryExpression {
  override def toString: String = s"$left<<$right"

  override def equals(obj: Any): Boolean = obj match {
    case leftShift: LeftShift ⇒
      leftShift.left == left && leftShift.right == right
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
