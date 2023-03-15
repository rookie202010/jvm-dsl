package com.dongjiaqiang.jvm.dsl.api.expression.unary

import com.dongjiaqiang.jvm.dsl.api.`type`.DslType
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope

/**
 * <p>unary expression include negate, opposite, cast,instanceof and paren operator</p>
 */
trait UnaryExpression extends ValueExpression {
  val child: ValueExpression

  override def getValueType(programScope: ProgramScope): DslType = child.getValueType( programScope )

}
