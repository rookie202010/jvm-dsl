package com.dongjiaqiang.jvm.dsl.api.`type`.visitor

import com.dongjiaqiang.jvm.dsl.api.`type`.{ByteType, CharType, DslType, IntType, MonadDslType, NumberDslType}
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.`type`.visitor.MethodVisitor._
trait SeqMethodVisitor[T] extends MethodVisitor[T]{

  def indexOf(calleeDslType:MonadDslType, callee:ValueExpression, element:ValueExpression):T
  def get(calleeDslType:MonadDslType,callee:ValueExpression,index:ValueExpression):T
  def splitAt(calleeDslType:MonadDslType,callee:ValueExpression,index:ValueExpression):T
  def add(calleeDslType:MonadDslType,callee:ValueExpression,index:ValueExpression,element:ValueExpression):T
  def add(calleeDslType:MonadDslType,callee:ValueExpression,element:ValueExpression):T
  def addAll(calleeDslType:MonadDslType,callee:ValueExpression,index:ValueExpression,element:ValueExpression):T
  def addAll(calleeDslType:MonadDslType,callee:ValueExpression,element:ValueExpression):T
  def remove(calleeDslType:MonadDslType, callee:ValueExpression, indexOrElement:ValueExpression):T
  def update(calleeDslType:MonadDslType,callee:ValueExpression,index:ValueExpression,element:ValueExpression):T
  def max(calleeType: MonadDslType, callee: ValueExpression): T
  def min(calleeType: MonadDslType, callee: ValueExpression): T
  def sum(calleeType: MonadDslType, callee: ValueExpression): T

  override def visit(calleeType:DslType, callee: ValueExpression, name: String, params: Array[ValueExpression]): Option[T] = {
    val calleeDslType = calleeType.asInstanceOf[MonadDslType]
    (name match {
      case INDEX_OF⇒
        generate(Array(calleeDslType.carryDslType),params,()⇒indexOf(calleeDslType,callee,params.head))
      case  GET | SPLIT_AT⇒
        generate(Array(IntType),params,()⇒{
          name match {
            case GET ⇒ get( calleeDslType, callee, params.head )
            case SPLIT_AT ⇒ splitAt( calleeDslType, callee, params.head )
          }
        })
      case SUM | MAX | MIN ⇒
        calleeDslType.carryDslType match {
          case _: NumberDslType | ByteType | CharType ⇒
            name match {
              case SUM ⇒ Some( sum( calleeDslType, callee ) )
              case MAX ⇒ Some( max( calleeDslType, callee ) )
              case MIN ⇒ Some( min( calleeDslType, callee ) )
            }
          case _ ⇒ None
        }
      case ADD⇒
        generateOverload(Array(Array(IntType,calleeDslType.carryDslType),
          Array(calleeDslType.carryDslType)),params,()⇒{
          if (params.length == 2) {
            add( calleeDslType, callee, params.head, params.last )
          } else {
            add( calleeDslType, callee, params.head )
          }
        })
      case ADD_ALL⇒
        generateOverload(Array(Array(calleeDslType.carryDslType),Array(IntType,calleeDslType.carryDslType)),params,(requireTypes,actualTypes)⇒{
           if(requireTypes.length==1){
             actualTypes.head.isInstanceOf[MonadDslType] &&
               requireTypes.head.isSuperDslType( programScope.importManager,actualTypes.head.asInstanceOf[MonadDslType].carryDslType )
           }else if(requireTypes.length==2){
             actualTypes.last.isInstanceOf[MonadDslType] &&
               requireTypes.last.isSuperDslType( programScope.importManager, actualTypes.last.asInstanceOf[MonadDslType].carryDslType )
           }else{
             false
           }
        },()⇒{
          if(params.length==1){
            addAll(calleeDslType,callee,params.head)
          }else{
            addAll(calleeDslType,callee,params.head,params.last)
          }
        })

      case REMOVE⇒
        generateOverload(Array(Array(IntType),Array(calleeDslType.carryDslType)),params,()⇒remove(calleeDslType,callee,params.head))
      case UPDATE⇒
        generate(Array(IntType,calleeDslType.carryDslType),params,()⇒update(calleeDslType,callee,params.head,params.last))
      case _⇒
        None
    }).orElse(super.visit(calleeDslType, callee, name, params))
  }
}
