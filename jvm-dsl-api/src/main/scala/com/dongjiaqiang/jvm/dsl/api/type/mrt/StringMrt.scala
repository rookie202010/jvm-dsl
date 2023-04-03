package com.dongjiaqiang.jvm.dsl.api.`type`.mrt

import com.dongjiaqiang.jvm.dsl.api.`type`.visitor.StringMethodVisitor
import com.dongjiaqiang.jvm.dsl.api.`type`._
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope

class StringMrt(override val programScope: ProgramScope) extends StringMethodVisitor[DslType] with MonadMrt with ConvertMrt {
  override def charAt(callee: ValueExpression,
                      param: ValueExpression): DslType = CharType

  override def split(callee: ValueExpression,
                     param: ValueExpression): DslType = ArrayType(StringType)

  override def format(callee: ValueExpression,
                      param: Array[ValueExpression]): DslType = StringType

  override def trim(callee: ValueExpression): DslType = StringType

  override def toUpperCase(callee: ValueExpression): DslType = StringType

  override def toLowerCase(callee: ValueExpression): DslType = StringType

  override def getBytes(callee: ValueExpression): DslType = ArrayType(ByteType)

  override def getBytes(callee: ValueExpression,
                        param: ValueExpression): DslType = ArrayType(ByteType)

  override def subString(callee: ValueExpression,
                         startIndex: ValueExpression,
                         endIndex: ValueExpression): DslType = StringType

  override def split(callee: ValueExpression, param: ValueExpression, limit: ValueExpression): DslType =  ArrayType(StringType)

  override def isNumeric(callee: ValueExpression): DslType = BoolType

  override def head(callee: ValueExpression): DslType = CharType

  override def headOption(callee: ValueExpression): DslType = OptionType(CharType)

  override def tail(callee: ValueExpression): DslType = CharType

  override def tailOption(callee: ValueExpression): DslType = OptionType(CharType)
}
