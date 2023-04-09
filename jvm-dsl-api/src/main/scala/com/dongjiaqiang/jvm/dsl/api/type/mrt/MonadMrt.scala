package com.dongjiaqiang.jvm.dsl.api.`type`.mrt

import com.dongjiaqiang.jvm.dsl.api.`type`._
import com.dongjiaqiang.jvm.dsl.api.`type`.visitor.MonadMethodVisitor
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.expression.block.Lambda
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope

trait MonadMrt extends MonadMethodVisitor[DslType] {
  override def map(calleeType:MonadDslType,
                   callee: ValueExpression,
                   param: ValueExpression): DslType = {
    val lambdaType = param.getValueType( programScope ).asInstanceOf[LambdaType]
    calleeType.transform( lambdaType.outputType )
  }

  override def mapValue(calleeType: MonadDslType, callee: ValueExpression, param: ValueExpression): DslType = {
      throw new UnsupportedOperationException("unsupported")
  }


  override def flatMap(calleeType:MonadDslType,
                       callee: ValueExpression,
                       param: ValueExpression): DslType = {
    val lambdaType = param.getValueType( programScope ).asInstanceOf[LambdaType]
    calleeType.transform( lambdaType.outputType.asInstanceOf[MonadDslType].carryDslType )
  }
  override def flatten(calleeType:MonadDslType,
                       callee: ValueExpression): DslType = {
    calleeType.transform(calleeType.carryDslType.asInstanceOf[MonadDslType].carryDslType)
  }
  override def filter(calleeType:MonadDslType,
                      callee: ValueExpression,
                      param: ValueExpression): DslType = {
    calleeType
  }
  override def filterNot(calleeType:MonadDslType,
                         callee: ValueExpression,
                         param: ValueExpression): DslType = {
    calleeType
  }
  override def foreach(calleeType:MonadDslType,
                       callee: ValueExpression,
                       param: ValueExpression): DslType = UnitType
  override def exist(calleeType:MonadDslType,
                     callee: ValueExpression,
                     param: ValueExpression): DslType = BoolType
  override def find(calleeType:MonadDslType,
                    callee: ValueExpression,
                    param: ValueExpression): DslType = new OptionType( callee.getValueType( programScope ) )
  override def toList(calleeType:MonadDslType,
                      callee: ValueExpression): DslType = {
    ListType( calleeType.carryDslType )
  }
  override def toSet(calleeType:MonadDslType,
                     callee: ValueExpression): DslType = SetType( calleeType.carryDslType )
  override def toMap(calleeType:MonadDslType,
                     callee: ValueExpression): DslType = {
    val kv = calleeType.carryDslType.asInstanceOf[TupleType].parameterTypes
    MapType( kv.head, kv.last )
  }
  override def contains(calleeType:MonadDslType,
                        callee: ValueExpression,
                        param: ValueExpression): DslType = BoolType
  override def sort(calleeType:MonadDslType,
                    callee: ValueExpression,
                    param: ValueExpression): DslType =  calleeType
  override def zipWithIndex(calleeType:MonadDslType,
                            callee: ValueExpression): DslType = {
    ListType( TupleType( Array( calleeType.carryDslType, IntType ) ) )
  }
  override def isEmpty(calleeType:MonadDslType,
                       callee: ValueExpression): DslType = BoolType
  override def nonEmpty(calleeType:MonadDslType,
                        callee: ValueExpression): DslType = BoolType
  override def length(calleeType:MonadDslType,
                      callee: ValueExpression): DslType = IntType
  override def reduce(calleeType:MonadDslType,
                      callee: ValueExpression,
                      param: ValueExpression): DslType = calleeType.carryDslType

  override def reduceOption(calleeType:MonadDslType,
                            callee: ValueExpression,
                            param: ValueExpression): DslType = new OptionType(calleeType.carryDslType)
  override def toSeqSet(calleeType: MonadDslType,
                        callee: ValueExpression): DslType = SetType( calleeType.carryDslType,seq = true )
  override def toSortedSet(calleeType: MonadDslType,
                           callee: ValueExpression): DslType = SetType(calleeType.carryDslType,sorted = true)
  override def toSeqMap(calleeType: MonadDslType,
                        callee: ValueExpression): DslType = {
    val kv = calleeType.carryDslType.asInstanceOf[TupleType].parameterTypes
    MapType( kv.head, kv.last,seq = true )
  }
  override def toSortedMap(calleeType: MonadDslType,
                           callee: ValueExpression): DslType = {
    val kv = calleeType.carryDslType.asInstanceOf[TupleType].parameterTypes
    MapType( kv.head, kv.last,sorted = true )
  }

  override def toSortedSet(calleeType: MonadDslType,
                           callee: ValueExpression,
                           param: ValueExpression): DslType = SetType(calleeType.carryDslType,sorted = true,sorter = Some(param.asInstanceOf[Lambda]))

  override def toSortedMap(calleeType: MonadDslType,
                           callee: ValueExpression,
                           param: ValueExpression): DslType ={
    val kv = calleeType.carryDslType.asInstanceOf[TupleType].parameterTypes
    MapType( kv.head, kv.last, sorted = true )
  }

  override def reverse(calleeType: MonadDslType, callee: ValueExpression): DslType = calleeType

  override def toArray(calleeType: MonadDslType, callee: ValueExpression): DslType = ArrayType(calleeType.carryDslType)

  override def reduce(calleeType: MonadDslType, callee: ValueExpression, init: ValueExpression, param: ValueExpression): DslType = calleeType.carryDslType

  override def mkString(calleeType: MonadDslType, callee: ValueExpression, sep: ValueExpression): DslType = StringType

  override def mkString(calleeType: MonadDslType, callee: ValueExpression, start: ValueExpression, sep: ValueExpression, end: ValueExpression): DslType = StringType

  override def head(calleeType: MonadDslType, callee: ValueExpression): DslType = calleeType.carryDslType

  override def last(calleeType: MonadDslType, callee: ValueExpression): DslType = calleeType.carryDslType

  override def headOption(calleeType: MonadDslType, callee: ValueExpression): DslType = OptionType(calleeType.carryDslType)

  override def lastOption(calleeType: MonadDslType, callee: ValueExpression): DslType = OptionType(calleeType.carryDslType)
}
