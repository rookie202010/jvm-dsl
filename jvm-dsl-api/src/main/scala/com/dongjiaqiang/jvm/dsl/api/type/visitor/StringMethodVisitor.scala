package com.dongjiaqiang.jvm.dsl.api.`type`.visitor

import com.dongjiaqiang.jvm.dsl.api.`type`.{DslType, IntType, StringType}
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression


trait StringMethodVisitor[T] extends MonadMethodVisitor[T] with ConvertTypeMethodVisitor[T] with MethodVisitor[T] {

  def charAt(callee: ValueExpression, param: ValueExpression): T
  def split(callee: ValueExpression, param: ValueExpression): T
  def format(callee:ValueExpression,param:Array[ValueExpression]):T
  def trim(callee:ValueExpression):T
  def toUpperCase(callee:ValueExpression):T
  def toLowerCase(callee:ValueExpression):T
  def getBytes(callee:ValueExpression):T
  def getBytes(callee:ValueExpression,param:ValueExpression):T
  def subString(callee:ValueExpression,startIndex:ValueExpression,endIndex:ValueExpression):T

  override def visit(calleeType:DslType ,callee: ValueExpression, name: String, params: Array[ValueExpression]): Option[T] = {
        val stringType = calleeType.asInstanceOf[StringType.type]
        val (actualDslTypes,msg)  = actualTypes(programScope,params,name,calleeType)
        name match {
          case "charAt" | "split"⇒
              require(params.length==1 && params.head.getValueType(programScope)==IntType,
                msg.apply(Array(calleeType.toString,stringType.toString)))
              name match {
                case "split"⇒ Some(split(callee,params.head))
                case "charAt"⇒  Some(charAt(callee,params.head))
              }


        }
  }
}
