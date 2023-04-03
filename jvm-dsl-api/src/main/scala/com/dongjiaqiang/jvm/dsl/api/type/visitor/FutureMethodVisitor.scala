package com.dongjiaqiang.jvm.dsl.api.`type`.visitor

import com.dongjiaqiang.jvm.dsl.api.`type`.{DslType, FutureType, LongType, StringType}
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.`type`.visitor.MethodVisitor._
trait FutureMethodVisitor[T] extends MonadMethodVisitor[T]{
  def isComplete(calleeType:FutureType,callee:ValueExpression):T
  def isCancelled(calleeType:FutureType,callee:ValueExpression):T
  def isCompleteExceptionally(calleeType:FutureType,callee:ValueExpression):T
  def get(calleeType:FutureType,callee:ValueExpression):T
  def getNow(calleeType:FutureType,callee:ValueExpression,valueIfAbsent:ValueExpression):T
  def get(calleeType:FutureType,callee:ValueExpression,timeout:ValueExpression,timeUnit:ValueExpression):T
  def complete(calleeType:FutureType,callee:ValueExpression,value:ValueExpression):T
  def completeExceptionally(calleeType:FutureType,callee:ValueExpression,value:ValueExpression):T

  override def visit(calleeType: DslType, callee: ValueExpression, name: String, params: Array[ValueExpression]): Option[T] = {
    val calleeDslType = calleeType.asInstanceOf[FutureType]
    (name match {
      case IS_COMPLETE⇒
        generate(params,()⇒ isComplete(calleeDslType,callee))
      case IS_CANCELLED⇒
        generate(params,()⇒ isCancelled(calleeDslType,callee))
      case IS_COMPLETE_EXCEPTIONALLY⇒
        generate(params,()⇒ isCompleteExceptionally(calleeDslType,callee))
      case GET⇒
        generateOverload(Array(Array(),Array(LongType,StringType)),params,()⇒{
          if (params.isEmpty) {
            get( calleeDslType, callee )
          } else {
            get( calleeDslType, callee, params.head, params.last )
          }
        })
      case GET_NOW⇒
        generate(Array(calleeDslType.carryDslType),params,()⇒getNow(calleeDslType,callee,params.head))
      case COMPLETE⇒
        generate(Array(calleeDslType.carryDslType),params,()⇒complete(calleeDslType,callee,params.head))
      case COMPLETE_EXCEPTIONALLY⇒
        Some(completeExceptionally(calleeDslType,callee,params.head))
      case _⇒ None
    }).orElse(super.visit(calleeType,callee,name,params))
  }
}
