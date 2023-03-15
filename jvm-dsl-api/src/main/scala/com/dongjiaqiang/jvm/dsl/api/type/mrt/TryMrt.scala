package com.dongjiaqiang.jvm.dsl.api.`type`.mrt

import com.dongjiaqiang.jvm.dsl.api.`type`.visitor.TryMethodVisitor
import com.dongjiaqiang.jvm.dsl.api.`type`.{BoolType, ClazzType, DslType, TryType}
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope

class TryMrt(override val programScope: ProgramScope) extends MonadMrt with TryMethodVisitor[DslType] {
  override def getException(calleeType: TryType, callee: ValueExpression): DslType = ClazzType( "Throwable", Array( ) )

  override def get(calleeType: TryType, callee: ValueExpression): DslType = calleeType.carryDslType

  override def isSuccess(calleeType: TryType): DslType = BoolType

  override def isFailure(calleeType: TryType): DslType = BoolType
}
