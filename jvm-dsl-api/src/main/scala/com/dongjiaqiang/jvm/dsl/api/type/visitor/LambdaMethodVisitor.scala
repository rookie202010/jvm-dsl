package com.dongjiaqiang.jvm.dsl.api.`type`.visitor

import com.dongjiaqiang.jvm.dsl.api.`type`.{DslType, LambdaType, TupleType, UnitType}
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.`type`.visitor.MethodVisitor._
/**
 * LambdaType method visitor
 */
trait LambdaMethodVisitor[T] extends MethodVisitor[T]{

  def get(calleeType:LambdaType,callee:ValueExpression):T
  def apply(calleeType:LambdaType,callee:ValueExpression,params:Array[ValueExpression]):T
  def apply(calleeType:LambdaType,callee:ValueExpression,param:ValueExpression):T
  def supply(calleeType:LambdaType,callee:ValueExpression,params:Array[ValueExpression]):T
  def supply(calleeType:LambdaType,callee:ValueExpression,param:ValueExpression):T

  private def visit(lambdaType: LambdaType,
                    callee:ValueExpression,
                    actualDslTypes:Array[DslType],
                    params:Array[ValueExpression],
                    generator:(LambdaType,ValueExpression,ValueExpression)⇒T,
                    multiGenerator:(LambdaType,ValueExpression,Array[ValueExpression])⇒T):Option[T]={
   lambdaType.mayInputType.get match {
      case TupleType( parameterTypes ) ⇒ {
        if (params.length == 1) {
          val paramType = actualDslTypes.head
          paramType match {
            case TupleType( types ) ⇒
              if( parameterTypes.length == types.length) {
                Some( generator.apply( lambdaType, callee, params.head ) )
              }else{
                None
              }
            case _ ⇒
              None
          }
        } else {
          if( parameterTypes.length == params.length && parameterTypes.zip( actualDslTypes ).forall( {
            case (t, p) ⇒ t.isSuperDslType( programScope.importManager, p )
          } ) ) {
            Some( multiGenerator.apply( lambdaType, callee, params ) )
          }else{
            None
          }
        }
      }
      case inputType ⇒
        if( params.length == 1 && inputType.isSuperDslType( programScope.importManager, actualDslTypes.head ) ) {
          Some( generator.apply( lambdaType, callee, params.head ) )
        }else{
          None
        }
    }
  }

  override def visit(calleeDslType: DslType, callee: ValueExpression, name: String, params: Array[ValueExpression]): Option[T] = {
    val lambdaType = calleeDslType.asInstanceOf[LambdaType]
    (name match {
        case GET⇒
          generateOption(params, ()⇒{
            if(lambdaType.mayInputType.isEmpty &&
              lambdaType.outputType != UnitType){
              Some(get(lambdaType,callee))
            }else{
              None
            }
          })
        case SUPPLY⇒
          val actualTypes = params.map( _.getValueType( programScope ) )
          if(lambdaType.outputType==UnitType &&
                  lambdaType.mayInputType.isDefined) {
            visit( lambdaType, callee, actualTypes, params, supply, supply)
          }else{
            None
          }
        case APPLY⇒
          val actualTypes = params.map( _.getValueType( programScope ) )
          if(lambdaType.mayInputType.isDefined &&
            lambdaType.outputType!=UnitType) {
            visit( lambdaType, callee, actualTypes, params, apply, apply )
          }else{
            None
          }
      }).orElse(super.visit(calleeDslType, callee, name, params))
  }
}
