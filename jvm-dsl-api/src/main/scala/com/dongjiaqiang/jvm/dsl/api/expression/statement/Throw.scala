package com.dongjiaqiang.jvm.dsl.api.expression.statement

import com.dongjiaqiang.jvm.dsl.api.expression.Expression

/**
 * <pre><code>
 * program{
 * def method(Int i)=Unit{
 * throw new Exception(); // throw ... => Throw
 * }
 * }
 * <pre><code>
 */

case class Throw(expression: Expression) extends Expression {
  override def toString: String = s"throw $expression"

  override def equals(obj: Any): Boolean = obj match {
    case `throw`: Throw ⇒
      `throw`.expression == expression
    case _ ⇒ false
  }
}
