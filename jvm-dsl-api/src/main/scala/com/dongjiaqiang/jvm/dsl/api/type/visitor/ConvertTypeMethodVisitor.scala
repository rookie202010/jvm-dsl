package com.dongjiaqiang.jvm.dsl.api.`type`.visitor
import com.dongjiaqiang.jvm.dsl.api.`type`.DslType
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression

trait ConvertTypeMethodVisitor[T] extends MethodVisitor[T]{

  def toInt(callee:ValueExpression):T

  def toLong(callee:ValueExpression):T

  def toFloat(callee:ValueExpression):T

  def toDouble(callee:ValueExpression):T

  def toChar(callee: ValueExpression):T

  def toByte(callee:ValueExpression):T

  override def visit(calleeDslType:DslType,callee: ValueExpression, name: String, params: Array[ValueExpression]): Option[T] = name match {
    case "toInt" ⇒
     requireNoParams(name,params)
      Some(toInt(callee))
    case "toLong" ⇒
      requireNoParams(name,params)
      Some(toLong(callee))
    case "toFloat" ⇒
      requireNoParams(name,params)
      Some(toFloat(callee))
    case _⇒ super.visit(calleeDslType,callee, name, params)
  }
}


