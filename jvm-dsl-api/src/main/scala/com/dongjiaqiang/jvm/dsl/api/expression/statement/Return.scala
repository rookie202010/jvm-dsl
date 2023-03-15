package com.dongjiaqiang.jvm.dsl.api.expression.statement

import com.dongjiaqiang.jvm.dsl.api.expression.{Expression, ValueExpression}

/**
 * <pre><code>
 * program{
 * def method(Int i)=Unit{
 * return i;   // return ... => Return
 * }
 * }
 * <pre><code>
 */

case class Return(expression: Option[ValueExpression]) extends Expression {
  override def toString: String = s"return $expression"

  override def equals(obj: Any): Boolean = obj match {
    case `return`: Return ⇒
      `return`.expression == expression
    case _ ⇒ false
  }
}
