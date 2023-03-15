package com.dongjiaqiang.jvm.dsl.api.`type`.mrt

import com.dongjiaqiang.jvm.dsl.api.`type`.visitor.SeqMethodVisitor
import com.dongjiaqiang.jvm.dsl.api.`type`._
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression

trait SeqMrt extends SeqMethodVisitor[DslType]{
  override def indexOf(calleeDslType: MonadDslType, callee: ValueExpression, param: ValueExpression): DslType = IntType

  override def get(calleeDslType: MonadDslType, callee: ValueExpression, index: ValueExpression): DslType = calleeDslType.carryDslType

  override def splitAt(calleeDslType: MonadDslType, callee: ValueExpression, index: ValueExpression): DslType = TupleType(Array(calleeDslType,calleeDslType))

  override def add(calleeDslType: MonadDslType, callee: ValueExpression, index: ValueExpression, element: ValueExpression): DslType = UnitType

  override def add(calleeDslType: MonadDslType, callee: ValueExpression, element: ValueExpression): DslType = UnitType

  override def addAll(calleeDslType: MonadDslType, callee: ValueExpression, index: ValueExpression, element: ValueExpression): DslType = UnitType

  override def addAll(calleeDslType: MonadDslType, callee: ValueExpression, element: ValueExpression): DslType = UnitType

  override def remove(calleeDslType: MonadDslType, callee: ValueExpression, index: ValueExpression): DslType = calleeDslType.carryDslType

  override def update(calleeDslType: MonadDslType, callee: ValueExpression, index: ValueExpression, element: ValueExpression): DslType = calleeDslType.carryDslType
}
