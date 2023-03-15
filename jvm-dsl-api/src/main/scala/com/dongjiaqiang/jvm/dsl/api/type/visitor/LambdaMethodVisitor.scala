package com.dongjiaqiang.jvm.dsl.api.`type`.visitor

import com.dongjiaqiang.jvm.dsl.api.`type`.{DslType, LambdaType, TupleType, UnitType}
import com.dongjiaqiang.jvm.dsl.api.exception.ExpressionParseException
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression

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
                    multiGenerator:(LambdaType,ValueExpression,Array[ValueExpression])⇒T,msg:()⇒String):Option[T]={
   lambdaType.mayInputType.get match {
      case TupleType( parameterTypes ) ⇒ {
        if (params.length == 1) {
          val paramType = actualDslTypes.head
          paramType match {
            case TupleType( types ) ⇒
              require( parameterTypes.length == types.length, msg)
              Some( generator.apply(lambdaType,callee,params.head) )
            case _ ⇒
              throw ExpressionParseException( msg.apply() )
          }
        } else {
          require( parameterTypes.length == params.length && parameterTypes.zip( actualDslTypes ).forall( {
            case (t, p) ⇒ t.isSuperDslType( programScope.importManager, p )
          } ), msg.apply() )
          Some( multiGenerator.apply(lambdaType,callee,params))
        }
      }
      case inputType ⇒
        require( params.length == 1 && inputType.isSuperDslType( programScope.importManager, actualDslTypes.head ), msg )
        Some( generator.apply(lambdaType,callee,params.head) )
    }
  }

  override def visit(calleeDslType: DslType, callee: ValueExpression, name: String, params: Array[ValueExpression]): Option[T] = {
    val lambdaType = calleeDslType.asInstanceOf[LambdaType]
      name match {
        case "get"⇒
          val (_,msg) = actualTypes(programScope,params,name,lambdaType, Array())
          require(lambdaType.mayInputType.isEmpty &&
            lambdaType.outputType!=UnitType &&
            params.isEmpty,msg)
          Some(get(lambdaType,callee))
        case "supply"⇒
          val (actualDslTypes,msg) = actualTypes(programScope,params,name,lambdaType, lambdaType.mayInputType.toArray.map(_.toString))
          require(lambdaType.outputType==UnitType &&
                  lambdaType.mayInputType.isDefined,msg)
          visit(lambdaType,callee,actualDslTypes,params,supply,supply,msg)
        case "apply"⇒
          val (actualDslTypes,msg) = actualTypes(programScope,params,name,lambdaType, lambdaType.mayInputType.toArray.map(_.toString))
          require(lambdaType.mayInputType.isDefined &&
            lambdaType.outputType!=UnitType,msg)
          visit(lambdaType,callee,actualDslTypes,params,apply,apply,msg)
      }
  }
}
