package com.dongjiaqiang.jvm.dsl.api.`type`.visitor

import com.dongjiaqiang.jvm.dsl.api.`type`.DslType
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression

trait RangeMethodVisitor[T] extends MethodVisitor[T]{

  def to(callee: ValueExpression,end:ValueExpression): T

  def to(callee: ValueExpression, end:ValueExpression,step: ValueExpression): T

  def until(callee: ValueExpression,end:ValueExpression): T

  def until(callee: ValueExpression, end:ValueExpression,step: ValueExpression): T

  override def visit(calleeType:DslType,callee: ValueExpression, name: String, params: Array[ValueExpression]): Option[T] = {
      if(params.length==1){
          val (actualDslTypes,msg)  = actualTypes(programScope,params,name,calleeType,Array(calleeType.toString))
          require(actualDslTypes.head == calleeType,msg )
          name match {
            case "to"⇒
              Some(to(callee,params.head))
            case "until"⇒
              Some(until(callee,params.head))
          }
      }else if(params.length==2){
          val (actualDslTypes,msg)  = actualTypes(programScope,params,name,calleeType,Array(calleeType.toString,calleeType.toString))
          require(actualDslTypes.head == calleeType && actualDslTypes.last == calleeType,msg )
          name match {
            case "to"⇒
              Some(to(callee,params.head,params.last))
            case "until"⇒
              Some( until( callee, params.head,params.last) )
          }
      }else{
        super.visit(calleeType,callee, name, params)
      }
  }
}


