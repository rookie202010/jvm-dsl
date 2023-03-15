package com.dongjiaqiang.jvm.dsl.api.expression.`var`

import com.dongjiaqiang.jvm.dsl.api.expression.{Expression, ValueExpression}

/**
 * <pre><code>
 *  program{
 *      def method()=Unit{
 *        i = foo()*10; // i = foo()*10; => Assign
 *      }
 *  }
 * <pre><code>
 */
case class Assign(varRef: VarRef, assigned: ValueExpression) extends Expression {
  override def toString: String = s"$varRef = $assigned"

  override def equals(obj: Any): Boolean = obj match {
    case assign: Assign ⇒
      assign.varRef == varRef && assign.assigned == assigned
    case _ => false
  }
}
