package com.dongjiaqiang.jvm.dsl.api.`type`.mrt

import com.dongjiaqiang.jvm.dsl.api.`type`.visitor.AnyMethodVisitor
import com.dongjiaqiang.jvm.dsl.api.`type`.{BoolType, DslType, IntType, StringType}
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope

class AnyMrt(override val programScope: ProgramScope) extends AnyMethodVisitor[DslType] {

  override def hashCode(callee: ValueExpression): DslType = IntType

  override def equals(callee: ValueExpression, param: ValueExpression): DslType = BoolType

  override def toString(callee: ValueExpression): DslType = StringType

}
