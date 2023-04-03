package com.dongjiaqiang.jvm.dsl.api.`type`.visitor
import com.dongjiaqiang.jvm.dsl.api.`type`.{DslType, UnitType}
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.`type`.visitor.MethodVisitor._

trait ConvertTypeMethodVisitor[T] extends MethodVisitor[T] {

  def toInt(calleeDslType: DslType,callee: ValueExpression): T

  def toLong(calleeDslType: DslType,callee: ValueExpression): T

  def toFloat(calleeDslType: DslType,callee: ValueExpression): T

  def toDouble(calleeDslType: DslType,callee: ValueExpression): T

  def toChar(calleeDslType: DslType,callee: ValueExpression): T

  def toByte(calleeDslType: DslType,callee: ValueExpression): T

  override def visit(calleeDslType: DslType, callee: ValueExpression, name: String, params: Array[ValueExpression]): Option[T] = {
    (name match {
      case TO_CHAR⇒
        generate(params,()⇒toChar(calleeDslType,callee))
      case TO_BYTE⇒
        generate(params,()⇒toByte(calleeDslType,callee))
      case TO_DOUBLE⇒
        generate(params,()⇒ toDouble(calleeDslType: DslType,callee))
      case TO_INT ⇒
        generate( params, () ⇒ toInt( calleeDslType: DslType,callee ) )
      case TO_LONG ⇒
        generate( params, () ⇒ toLong(calleeDslType: DslType, callee ) )
      case TO_FLOAT ⇒
        generate( params, () ⇒ toFloat(calleeDslType: DslType, callee ) )
      case _ ⇒
        None
    }).orElse(super.visit(calleeDslType,callee, name, params))
  }
}


