package com.dongjiaqiang.jvm.dsl.api.expression.unary

import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression

/**
 * <pre><code>
 * program{
 *    def method()=Unit{
 *        Long a=1;
 *        Long b=1;
 *        Long c=2;
 *        Long i = (a+b)*c; // (a+b) => Paren
 *    }
 * }
 * <pre><code>
 */
case class Paren(child: ValueExpression) extends UnaryExpression {
  override def toString: String = s"($child)"

  override def equals(obj: Any): Boolean = obj match {
    case paren: Paren ⇒ paren.child == child
    case _ ⇒ false
  }
}
