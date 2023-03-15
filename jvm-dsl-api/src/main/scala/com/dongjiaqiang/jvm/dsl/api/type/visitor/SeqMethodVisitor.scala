package com.dongjiaqiang.jvm.dsl.api.`type`.visitor

import com.dongjiaqiang.jvm.dsl.api.`type`.{DslType, IntType, MonadDslType}
import com.dongjiaqiang.jvm.dsl.api.exception.ExpressionParseException
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression

trait SeqMethodVisitor[T] extends MethodVisitor[T]{

  def indexOf(calleeDslType:MonadDslType,callee:ValueExpression,param:ValueExpression):T
  def get(calleeDslType:MonadDslType,callee:ValueExpression,index:ValueExpression):T
  def splitAt(calleeDslType:MonadDslType,callee:ValueExpression,index:ValueExpression):T
  def add(calleeDslType:MonadDslType,callee:ValueExpression,index:ValueExpression,element:ValueExpression):T
  def add(calleeDslType:MonadDslType,callee:ValueExpression,element:ValueExpression):T
  def addAll(calleeDslType:MonadDslType,callee:ValueExpression,index:ValueExpression,element:ValueExpression):T
  def addAll(calleeDslType:MonadDslType,callee:ValueExpression,element:ValueExpression):T
  def remove(calleeDslType:MonadDslType,callee:ValueExpression,index:ValueExpression):T
  def update(calleeDslType:MonadDslType,callee:ValueExpression,index:ValueExpression,element:ValueExpression):T

  override def visit(calleeType:DslType, callee: ValueExpression, name: String, params: Array[ValueExpression]): Option[T] = {
    val calleeDslType = calleeType.asInstanceOf[MonadDslType]
    val (actualDslTypes,msg)  = actualTypes(programScope,params,name,calleeDslType)
    name match {
      case "indexOf"⇒

        val (actualDslTypes,msg)  = actualTypes(programScope,params,name,calleeDslType)
        require(params.length==1 && calleeDslType.carryDslType.isSuperDslType(programScope.importManager,actualDslTypes.head),
          ()⇒msg.apply(Array(calleeDslType.carryDslType.toString)))

        Some(indexOf(calleeDslType,callee,params.head))
      case  "get" | "splitAt"⇒

        val (actualDslTypes,msg)  = actualTypes(programScope,params,name,calleeDslType)
        require(params.length==1 && actualDslTypes.head == IntType,()⇒msg.apply(Array(IntType.toString)))

        if(name=="get") {
          Some( get( calleeDslType, callee, params.head ) )
        }else{
          Some(splitAt(calleeDslType,callee,params.head))
        }

      case "add"⇒

        val (actualDslTypes,msg)  = actualTypes(programScope,params,name,calleeDslType)

        if(params.length==2) {

          require( actualDslTypes.head == IntType &&
            calleeDslType.carryDslType.isSuperDslType( programScope.importManager,actualDslTypes.last )
            , ()⇒msg.apply(Array(IntType.toString,calleeDslType.carryDslType.toString)) )
          Some( add( calleeDslType, callee, params.head, params.last ) )

        }else if(params.length==1){

          require( calleeDslType.carryDslType.isSuperDslType(programScope.importManager,actualDslTypes.head),()⇒msg.apply(Array(calleeDslType.carryDslType.toString)))
          Some(add(calleeDslType,callee,params.head))

        }else{
          throw ExpressionParseException( s"${msg.apply(Array(IntType.toString))}\nor\n${msg.apply(Array(IntType.toString,calleeDslType.carryDslType.toString))}")
        }

      case "addAll"⇒

        val (actualDslTypes,msg)  = actualTypes(programScope,params,name,calleeDslType)

        if(params.length==2){
          require(actualDslTypes.head==IntType && actualDslTypes.last.isInstanceOf[MonadDslType] &&
            calleeDslType.carryDslType.isSuperDslType(programScope.importManager,actualDslTypes.last.asInstanceOf[MonadDslType].carryDslType),msg)
          Some(addAll(calleeDslType,callee,params.head,params.last))
        }else if(params.length==1){
          require( actualDslTypes.head.isInstanceOf[MonadDslType] &&
            calleeDslType.carryDslType.isSuperDslType( programScope.importManager, actualDslTypes.head.asInstanceOf[MonadDslType].carryDslType ), msg )
          Some(addAll(calleeDslType,callee,params.head))
        }else{
          val msg1 = msg.apply(Array(IntType.toString,s"Monad[${calleeDslType.carryDslType.toString}]"))
          val msg2 = msg.apply(Array(s"Monad[${calleeDslType.carryDslType}]"))
          throw ExpressionParseException(s"$msg1\nor\n$msg2")
        }

      case "remove"⇒
        require(params.length==1 && actualDslTypes.head == IntType,msg)
        Some(remove(calleeDslType,callee,params.head))
      case "update"⇒
        require(params.length==2 && actualDslTypes.head == IntType && calleeDslType.carryDslType.isSuperDslType( programScope.importManager, actualDslTypes.last),msg)
        Some(update(calleeDslType,callee,params.head,params.last))
      case _⇒
        super.visit(calleeDslType, callee, name, params)
    }
  }
}
