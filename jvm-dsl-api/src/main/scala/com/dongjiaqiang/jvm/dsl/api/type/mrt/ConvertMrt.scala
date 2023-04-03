package com.dongjiaqiang.jvm.dsl.api.`type`.mrt

import com.dongjiaqiang.jvm.dsl.api.`type`._
import com.dongjiaqiang.jvm.dsl.api.`type`.visitor.ConvertTypeMethodVisitor
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression

trait ConvertMrt extends ConvertTypeMethodVisitor[DslType] {
  override def toInt(calleeType:DslType,callee: ValueExpression): DslType = IntType

  override def toLong(calleeType:DslType,callee: ValueExpression): DslType = LongType

  override def toFloat(calleeType:DslType,callee: ValueExpression): DslType = FloatType

  override def toDouble(calleeType:DslType,callee: ValueExpression): DslType = DoubleType

  override def toChar(calleeType:DslType,callee: ValueExpression): DslType = CharType

  override def toByte(calleeType:DslType,callee: ValueExpression): DslType = ByteType

}
