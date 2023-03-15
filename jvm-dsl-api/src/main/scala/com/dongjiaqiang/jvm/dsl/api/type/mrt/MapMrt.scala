package com.dongjiaqiang.jvm.dsl.api.`type`.mrt

import com.dongjiaqiang.jvm.dsl.api.`type`._
import com.dongjiaqiang.jvm.dsl.api.`type`.visitor.MapMethodVisitor
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope

class MapMrt(override val programScope: ProgramScope) extends MonadMrt with MapMethodVisitor[DslType] {
  override def keys(calleeType: MapType,
                    callee: ValueExpression): DslType = SetType( calleeType.keyParameterType )

  override def values(calleeType: MapType,
                      callee: ValueExpression): DslType = ListType( calleeType.valueParameterType )

  override def containKey(calleeType: MapType,
                          callee: ValueExpression,
                          key: ValueExpression): DslType = BoolType

  override def get(calleeType: MapType,
                   callee: ValueExpression,
                   key: ValueExpression): DslType = calleeType.valueParameterType

  override def put(calleeType: MapType,
                   callee: ValueExpression,
                   key: ValueExpression,
                   value: ValueExpression): DslType = UnitType
}
