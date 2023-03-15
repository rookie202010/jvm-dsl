package com.dongjiaqiang.jvm.dsl.api.`type`.mrt

import com.dongjiaqiang.jvm.dsl.api.`type`._
import com.dongjiaqiang.jvm.dsl.api.`type`.visitor.MonadMethodVisitor
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression

trait MonadMrt extends MonadMethodVisitor[DslType] {
  override def map(callee: ValueExpression, param: ValueExpression): DslType = {
    val monadDslType = callee.getValueType( programScope ).asInstanceOf[MonadDslType]
    val lambdaType = param.getValueType( programScope ).asInstanceOf[LambdaType]
    monadDslType.transform( lambdaType.outputType )
  }

  override def flatMap(callee: ValueExpression, param: ValueExpression): DslType = {
    val monadDslType = callee.getValueType( programScope ).asInstanceOf[MonadDslType]
    val lambdaType = param.getValueType( programScope ).asInstanceOf[LambdaType]
    monadDslType.transform( lambdaType.outputType.asInstanceOf[MonadDslType].carryDslType )
  }

  override def flatten(callee: ValueExpression): DslType = {
    callee.getValueType( programScope ).asInstanceOf[MonadDslType]
      .carryDslType.asInstanceOf[MonadDslType].carryDslType
  }

  override def filter(callee: ValueExpression, param: ValueExpression): DslType = {
    callee.getValueType( programScope )
  }

  override def filterNot(callee: ValueExpression, param: ValueExpression): DslType = {
    callee.getValueType( programScope )
  }

  override def foreach(callee: ValueExpression, param: ValueExpression): DslType = UnitType

  override def exist(callee: ValueExpression, param: ValueExpression): DslType = BoolType

  override def find(callee: ValueExpression, param: ValueExpression): DslType = OptionType( callee.getValueType( programScope ) )

  override def toList(callee: ValueExpression): DslType = ListType( callee.getValueType( programScope ).asInstanceOf[MonadDslType].carryDslType )

  override def toSet(callee: ValueExpression): DslType = SetType( callee.getValueType( programScope ).asInstanceOf[MonadDslType].carryDslType )

  override def toMap(callee: ValueExpression): DslType = {
    val kv = callee.getValueType( programScope ).asInstanceOf[MonadDslType].carryDslType.asInstanceOf[TupleType].parameterTypes
    MapType( kv.head, kv.last )
  }

  override def contains(callee: ValueExpression, param: ValueExpression): DslType = BoolType


  override def sort(callee: ValueExpression, param: ValueExpression): DslType =  callee.getValueType( programScope )

  override def zipWithIndex(callee: ValueExpression): DslType = {
    val carryDslType = callee.getValueType( programScope ).asInstanceOf[MonadDslType].carryDslType
    ListType( TupleType( Array( carryDslType, IntType ) ) )
  }


  override def isEmpty(callee: ValueExpression): DslType = BoolType

  override def nonEmpty(callee: ValueExpression): DslType = BoolType

  override def length(callee: ValueExpression): DslType = IntType

  override def reduce(callee: ValueExpression, param: ValueExpression): DslType = callee.getValueType(programScope)

  override def reduceOption(callee: ValueExpression, param: ValueExpression): DslType = OptionType(callee.getValueType(programScope))
}
