package com.dongjiaqiang.jvm.dsl.api.expression.binary

import com.dongjiaqiang.jvm.dsl.api.`type`.DslType
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression

/**
 * <pre><code>
 * program{
 *    def method()=Unit{
 *        Long j = 1-2; // 1-2 => Sub
 *        Long i = (a+b)*c; // a+b => Add
 *    }
 * }
 * <pre><code>
 */
case class Add(left: ValueExpression, right: ValueExpression) extends BinaryExpression {
  override def toString: String = s"$left+$right"

  override def equals(obj: Any): Boolean = obj match {
    case add: Add ⇒
      add.left == left && add.right == right
    case _ ⇒ false
  }

  override def checkValueType(leftType: DslType, rightType: DslType): Unit = {
    checkArithmeticExpressionValueType( leftType )
    checkArithmeticExpressionValueType( rightType )
  }
}
