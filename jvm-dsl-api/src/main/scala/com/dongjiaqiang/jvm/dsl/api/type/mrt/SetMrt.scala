package com.dongjiaqiang.jvm.dsl.api.`type`.mrt

import com.dongjiaqiang.jvm.dsl.api.`type`.visitor.SetMethodVisitor
import com.dongjiaqiang.jvm.dsl.api.`type`.{DslType, SetType, UnitType}
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope

class SetMrt(override val programScope: ProgramScope) extends MonadMrt with SetMethodVisitor[DslType] {
  override def add(calleeDslType: SetType,
                   callee: ValueExpression,
                   element: ValueExpression): DslType = UnitType

  override def addAll(calleeDslType: SetType,
                      callee: ValueExpression,
                      element: ValueExpression): DslType = UnitType

  override def withSort(calleeDslType: SetType,
                        callee: ValueExpression): DslType = calleeDslType

  override def withSeq(calleeDslType: SetType,
                       callee: ValueExpression): DslType = calleeDslType
}
