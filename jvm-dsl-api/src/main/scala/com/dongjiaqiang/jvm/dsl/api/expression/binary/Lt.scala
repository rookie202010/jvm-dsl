package com.dongjiaqiang.jvm.dsl.api.expression.binary

import com.dongjiaqiang.jvm.dsl.api.`type`.{BoolType, DslType}
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope

/**
 * <pre><code>
 * program{
 *      def method()=Unit{
 *          Bool j = 1&lt;2; // 1&lt;2 => Lt
 *          Bool k = 1>2; // 1>2 => Gt
 *          Bool i = 1&lt;=2; // 1&lt;=2 => Le
 *          Bool z = 1>=2; // 1>=2 => Ge
 *      }
 * }
 * <pre><code>
 */

case class Lt(left: ValueExpression, right: ValueExpression) extends BinaryExpression {
  override def toString: String = s"$left<$right"

  override def equals(obj: Any): Boolean = obj match {
    case lt: Lt ⇒
      lt.left == left && lt.right == right
    case _ ⇒ false
  }


  override def getValueType(programScope: ProgramScope): DslType = BoolType

}
