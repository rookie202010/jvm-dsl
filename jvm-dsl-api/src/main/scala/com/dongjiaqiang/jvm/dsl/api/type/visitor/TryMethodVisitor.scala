package com.dongjiaqiang.jvm.dsl.api.`type`.visitor

import com.dongjiaqiang.jvm.dsl.api.`type`.{DslType, TryType}
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.`type`.visitor.MethodVisitor._
/**
 * TryType method visitor
 */
trait TryMethodVisitor[T] extends MonadMethodVisitor[T]{

  def getException(calleeType: TryType, callee: ValueExpression): T

  def get(calleeType: TryType, callee: ValueExpression): T

  def isSuccess(calleeType: TryType,callee: ValueExpression): T

  def isFailure(calleeType: TryType,callee: ValueExpression): T

  def fold(calleeType:TryType,callee:ValueExpression,function1:ValueExpression,function2:ValueExpression):T

  def orElse(calleeType:TryType,callee:ValueExpression,param:ValueExpression):T

  def getOrElse(calleeType:TryType,callee:ValueExpression,param:ValueExpression):T

  override def visit(calleeType: DslType,
                     callee: ValueExpression,
                     name: String, params:
                     Array[ValueExpression]): Option[T] = {
    val tryType = calleeType.asInstanceOf[TryType]
    (name match {
      case GET ⇒
        generate(params,()⇒get(tryType,callee))
      case GET_EXCEPTION ⇒
        generate(params,()⇒getException(tryType,callee))
      case IS_SUCCESS ⇒
        generate(params,()⇒isSuccess(tryType,callee))
      case IS_FAILURE ⇒
        generate(params,()⇒isFailure(tryType,callee))
      case FOLD⇒
        if(paramsChecker){
          //todo
          Some(fold(tryType,callee,params.head,params.last))
        }else{
          Some(fold(tryType,callee,params.head,params.last))
        }
      case OR_ELSE⇒
        Some(orElse(tryType,callee,params.head))
      case GET_OR_ELSE⇒
        if (paramsChecker) {
          //todo
          Some( getOrElse( tryType, callee, params.head ) )
        } else {
          Some( getOrElse( tryType, callee, params.head ) )
        }
      case _ ⇒
        None
    }).orElse( super.visit( calleeType, callee, name, params ) )
  }
}
