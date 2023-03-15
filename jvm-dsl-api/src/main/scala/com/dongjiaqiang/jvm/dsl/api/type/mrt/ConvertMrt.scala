package com.dongjiaqiang.jvm.dsl.api.`type`.mrt

import com.dongjiaqiang.jvm.dsl.api.`type`._
import com.dongjiaqiang.jvm.dsl.api.`type`.visitor.ConvertTypeMethodVisitor
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression

trait ConvertMrt extends ConvertTypeMethodVisitor[DslType] {
  override def toInt(callee: ValueExpression): DslType = IntType

  override def toLong(callee: ValueExpression): DslType = LongType

  override def toFloat(callee: ValueExpression): DslType = FloatType

  override def toDouble(callee: ValueExpression): DslType = DoubleType

  override def toChar(callee: ValueExpression): DslType = CharType

  override def toByte(callee: ValueExpression): DslType = ByteType

}
