package com.dongjiaqiang.jvm.dsl.api.expression.statement

import com.dongjiaqiang.jvm.dsl.api.expression.{Expression, ValueExpression}

/**
 * <pre><code>
 * program{
 *      def method(Int i)=Unit{
 *          assert i==1; // assert ... => Assert
 *      }
 * }
 * <pre><code>
 */
case class Assert(expression: ValueExpression) extends Expression {
  override def toString: String = s"assert $expression"

  override def equals(obj: Any): Boolean = obj match {
    case `assert`: Assert ⇒
      `assert`.expression == expression
    case _ ⇒ false
  }

}
