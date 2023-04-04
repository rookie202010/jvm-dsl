package com.dongjiaqiang.jvm.dsl.api.`type`.mrt

import com.dongjiaqiang.jvm.dsl.api.`type`._
import com.dongjiaqiang.jvm.dsl.api.`type`.visitor.MapMethodVisitor
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope

class MapMrt(override val programScope: ProgramScope) extends MonadMrt with MapMethodVisitor[DslType] {


  override def map(calleeType: MonadDslType, callee: ValueExpression, param: ValueExpression): DslType = {
    val lambdaType = param.getValueType( programScope ).asInstanceOf[LambdaType]
    ListType(lambdaType.outputType)
  }


  override def mapValue(calleeType: MonadDslType, callee: ValueExpression, param: ValueExpression): DslType = {
    val mapType = calleeType.asInstanceOf[MapType]
    val lambdaType = param.getValueType( programScope ).asInstanceOf[LambdaType]
    MapType(mapType.keyParameterType,lambdaType.outputType,mapType.seq,mapType.sorted,mapType.sorter)
  }

  override def keys(calleeType: MapType,
                    callee: ValueExpression): DslType = {
    val setType = SetType( calleeType.keyParameterType )
    if(calleeType.seq){
      setType.asSeq()
    }else if(calleeType.sorted){
      calleeType.sorter match {
        case None⇒ setType.asSorted()
        case Some(lambda)⇒setType.asSorted(lambda)
      }
    }else{
      setType
    }
  }

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
