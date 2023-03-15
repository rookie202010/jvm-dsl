package com.dongjiaqiang.jvm.dsl.api.`type`.mrt

import com.dongjiaqiang.jvm.dsl.api.`type`.visitor.RangeMethodVisitor
import com.dongjiaqiang.jvm.dsl.api.`type`.{DslType, ListType}
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression

trait RangeMrt extends RangeMethodVisitor[DslType] {
  override def to(callee: ValueExpression,end:ValueExpression): DslType = ListType( callee.getValueType( programScope ) )

  override def to(callee: ValueExpression, end:ValueExpression,step: ValueExpression): DslType = ListType( callee.getValueType( programScope ) )

  override def until(callee: ValueExpression,end:ValueExpression): DslType = ListType( callee.getValueType( programScope ) )

  override def until(callee: ValueExpression,end:ValueExpression, step: ValueExpression): DslType = ListType( callee.getValueType( programScope ) )
}
