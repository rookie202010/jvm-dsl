package com.dongjiaqiang.jvm.dsl.api.`type`.visitor

import com.dongjiaqiang.jvm.dsl.api.`type`.{DslType, EitherType, UnitType}
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.expression.block.Lambda
import com.dongjiaqiang.jvm.dsl.api.`type`.visitor.MethodVisitor._

trait EitherMethodVisitor[T]  extends MonadMethodVisitor[T]{
  def left(calleeType:EitherType, callee:ValueExpression):T
  def right(calleeType: EitherType,callee:ValueExpression):T
  def isLeft(calleeType: EitherType,callee:ValueExpression):T
  def isRight(calleeType: EitherType,callee:ValueExpression):T
  def toLeft(calleeType: EitherType,callee:ValueExpression):T
  def toRight(calleeType: EitherType,callee:ValueExpression):T
  def mapLeft(calleeType:EitherType,callee:ValueExpression,function:ValueExpression):T
  def fold(calleeType:EitherType,callee:ValueExpression,functionL:ValueExpression,functionR:ValueExpression):T
  def orElse(calleeType:EitherType,callee:ValueExpression,param:ValueExpression):T
  def swap(calleeType:EitherType,callee:ValueExpression):T
  def toTry(calleeType:EitherType,callee:ValueExpression):T

  override def visit(calleeType: DslType, callee: ValueExpression, name: String, params: Array[ValueExpression]): Option[T] = {
    val calleeDslType = calleeType.asInstanceOf[EitherType]
    (name match {
      case LEFT⇒
        generate(params ,()⇒left(calleeDslType,callee))
      case RIGHT⇒
        generate(params, ()⇒right(calleeDslType,callee))
      case IS_LEFT⇒
        generate(params,()⇒isLeft(calleeDslType,callee))
      case IS_RIGHT⇒
        generate(params,()⇒ isRight(calleeDslType,callee))
      case TO_LEFT⇒
        generate( params,()⇒ toLeft(calleeDslType,callee))
      case TO_RIGHT⇒
        generate(params, ()⇒toRight(calleeDslType,callee))
      case FOLD⇒
        generateOption(Array(calleeDslType.leftParameterType,calleeDslType.rightParameterType),params,()⇒{
            params.head match {
              case p1: Lambda if params.last.isInstanceOf[Lambda] ⇒
                val p2 = params.last.asInstanceOf[Lambda]
                if (p1.inputs.length == 1 && p2.inputs.length == 1) {
                  Some( fold( calleeDslType, callee, p1, p2 ) )
                } else {
                  None
                }
              case _ ⇒
                None
            }
        })
      case MAP_LEFT⇒
        generateOption(Array(calleeDslType.leftParameterType),params,()⇒{
          params.head match {
            case lambda: Lambda if lambda.inputs.length == 1 ⇒
              Some( mapLeft( calleeDslType, callee, params.head ) )
            case _ ⇒
              None
          }
        })
      case OR_ELSE⇒
        generate(Array(calleeDslType.rightParameterType),params,()⇒orElse(calleeDslType,callee,params.head))
      case SWAP⇒
        generate(params,()⇒swap(calleeDslType,callee))
      case TO_TRY⇒
        generate(params,()⇒ toTry(calleeDslType,callee))
      case _⇒
        None
    }).orElse( super.visit(calleeType, callee, name, params))
  }
}
