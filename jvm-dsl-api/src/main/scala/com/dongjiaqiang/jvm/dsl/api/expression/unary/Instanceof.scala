package com.dongjiaqiang.jvm.dsl.api.expression.unary

import com.dongjiaqiang.jvm.dsl.api.`type`.{BoolType, DslType}
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope

/**
 * <pre><code>
 * program{
 *      def method(Any j)=Unit{
 *          bool i = j instanceof Foo; // j instanceof Foo => Instanceof
 *      }
 * }
 * <pre><code>
 */
case class Instanceof(child: ValueExpression, judgeType: DslType) extends UnaryExpression {
  override def toString: String = s"$child instanceof $judgeType"

  override def equals(obj: Any): Boolean = obj match {
    case instanceof: Instanceof ⇒ instanceof.child == child && instanceof.judgeType == judgeType
    case _ ⇒ false
  }

  override def getValueType(programScope: ProgramScope): DslType = BoolType
}
