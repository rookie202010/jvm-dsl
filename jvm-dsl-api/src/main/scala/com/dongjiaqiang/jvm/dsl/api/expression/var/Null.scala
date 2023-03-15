package com.dongjiaqiang.jvm.dsl.api.expression.`var`

import com.dongjiaqiang.jvm.dsl.api.`type`.{AnyType, DslType}
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope

/**
 *
 * <pre><code>
 * program{
 *    def method()=Unit{
 *        A a = null; // null is Null
 *     }
 * }
 * <pre><code>
 */
object Null extends ValueExpression {
  override def toString: String = "null"

  override def getValueType(programScope: ProgramScope): DslType = AnyType
}
