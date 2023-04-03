package com.dongjiaqiang.jvm.dsl.api.expression.call

import com.dongjiaqiang.jvm.dsl.api.`type`.visitor.requireMsg
import com.dongjiaqiang.jvm.dsl.api.`type`.{DslType, ImportClazzMethod, ImportClazzType}
import com.dongjiaqiang.jvm.dsl.api.exception.ExpressionParseException
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.expression.`var`.VarRef
import com.dongjiaqiang.jvm.dsl.api.expression.expression.getString
import com.dongjiaqiang.jvm.dsl.api.scope.{MethodScope, ProgramScope}

//func call expression
sealed trait FuncCall extends ValueExpression {
  val name: String
  val params: Array[ValueExpression]
}

//var call expression
case class VarCall(varRef: ValueExpression,
                   override val name: String,
                   override val params: Array[ValueExpression]) extends FuncCall {
  override def toString: String = getString( varRef, name, params )

  override def equals(obj: Any): Boolean = obj match {
    case varCall: VarCall ⇒
      varCall.params.sameElements( params ) &&
        varCall.varRef == varRef &&
        varCall.name == name
    case _ ⇒ false
  }

  override def getValueType(programScope: ProgramScope): DslType = programScope.callType(varRef,name,params)
}

//static call expression
case class StaticCall(`type`: DslType,
                      override val name: String,
                      override val params: Array[ValueExpression]) extends FuncCall {
  override def toString: String = getString( `type`, name, params )

  override def equals(obj: Any): Boolean = obj match {
    case staticCall: StaticCall ⇒
      staticCall.params.sameElements( params ) &&
        staticCall.`type` == `type` &&
        staticCall.name == name
    case _ ⇒ false
  }

  override def getValueType(programScope: ProgramScope): DslType = {
    programScope.importManager.resolve(`type`) match {
      case Some(ImportClazzType(_,_,_,_,staticMethods,_))⇒
          staticMethods.get(name) match {
            case Some(ImportClazzMethod(_, params, returnType))⇒
                if(params.zip(this.params).forall {
                  case (p1,p2)⇒p1.isSuperDslType(programScope.importManager,p2.getValueType(programScope))
                }){
                  returnType
                }else{
                  val msg = requireMsg(name,`type`,params,this.params.map(_.getValueType(programScope)))
                  throw ExpressionParseException(msg)
                }
            case None⇒ throw ExpressionParseException(s"type ${`type`} not find static methods $name")
          }
      case None⇒
        throw ExpressionParseException(s"type ${`type`} not find")
    }
  }
}


//method call expression
case class MethodCall(methodScope: Option[MethodScope],
                      override val name: String,
                      override val params: Array[ValueExpression]) extends FuncCall with Part {
  override def toString: String = getString( "", name, params )


  override def equals(obj: Any): Boolean = obj match {
    case methodCall: MethodCall ⇒
      methodCall.params.sameElements( params ) &&
        methodCall.methodScope == methodScope &&
        methodCall.name == name
    case _ ⇒ false
  }

  override def getValueType(programScope: ProgramScope): DslType = {
    methodScope match {
      case None ⇒ throw ExpressionParseException(s"method scope missing name: $name params: ${params.map( _.getValueType( programScope ).name ).mkString(",")}")
      case Some( scope ) ⇒
        if (scope.params.values.zip( params ).forall {
          case (p1, p2) ⇒ p1.dslType.isSuperDslType( programScope.importManager, p2.getValueType( programScope ) )
        }) {
          scope.returnType
        } else {
          val msg = requireMsg(name,scope.params.values.map(_.dslType).toArray,params.map(_.getValueType(programScope)))
          throw ExpressionParseException( msg )
        }
    }
  }
}

//literal call expression
class LiteralCall(val literal: ValueExpression,
                  override val name: String,
                  override val params: Array[ValueExpression]) extends FuncCall {
  override def toString: String = getString( literal, name, params )

  override def equals(obj: Any): Boolean = obj match {
    case literalCall: LiteralCall ⇒
      literalCall.params.sameElements( params ) &&
        literalCall.literal == literal &&
        literalCall.name == name
    case _ ⇒ false
  }

  override def getValueType(programScope: ProgramScope): DslType = {
     programScope.callType(literal,name, params)
  }
}