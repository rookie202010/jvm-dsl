package com.dongjiaqiang.jvm.dsl.api.`type`.visitor

import com.dongjiaqiang.jvm.dsl.api.`type`.visitor.MethodVisitor._
import com.dongjiaqiang.jvm.dsl.api.`type`.{BoolType, ByteType, CharType, DslType, LambdaType, MonadDslType, NumberDslType, StringType, TupleType}
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.expression.block.Lambda

trait MonadMethodVisitor[T] extends MethodVisitor[T] {
  def map(calleeType: MonadDslType, callee: ValueExpression, param: ValueExpression): T

  def mapValue(calleeType:MonadDslType,callee:ValueExpression,param:ValueExpression):T

  def flatten(calleeType: MonadDslType, callee: ValueExpression): T

  def flatMap(calleeType: MonadDslType, callee: ValueExpression, param: ValueExpression): T

  def filter(calleeType: MonadDslType, callee: ValueExpression, param: ValueExpression): T

  def filterNot(calleeType: MonadDslType, callee: ValueExpression, param: ValueExpression): T

  def foreach(calleeType: MonadDslType, callee: ValueExpression, param: ValueExpression): T

  def exist(calleeType: MonadDslType, callee: ValueExpression, param: ValueExpression): T

  def find(calleeType: MonadDslType, callee: ValueExpression, param: ValueExpression): T

  def toList(calleeType: MonadDslType, callee: ValueExpression): T

  def toArray(calleeType: MonadDslType, callee: ValueExpression): T

  def toSet(calleeType: MonadDslType, callee: ValueExpression): T

  def toSeqSet(calleeType: MonadDslType, callee: ValueExpression): T

  def toSortedSet(calleeType: MonadDslType, callee: ValueExpression): T

  def toSortedSet(calleeType: MonadDslType, callee: ValueExpression, param: ValueExpression): T

  def toMap(calleeType: MonadDslType, callee: ValueExpression): T

  def toSeqMap(calleeType: MonadDslType, callee: ValueExpression): T

  def toSortedMap(calleeType: MonadDslType, callee: ValueExpression): T

  def toSortedMap(calleeType: MonadDslType, callee: ValueExpression, param: ValueExpression): T

  def contains(calleeType: MonadDslType, callee: ValueExpression, param: ValueExpression): T

  def sort(calleeType: MonadDslType, callee: ValueExpression, param: ValueExpression): T

  def reverse(calleeType: MonadDslType, callee: ValueExpression): T

  def zipWithIndex(calleeType: MonadDslType, callee: ValueExpression): T

  def reduce(calleeType: MonadDslType, callee: ValueExpression, param: ValueExpression): T
  def reduce(calleeType:MonadDslType,callee:ValueExpression,init:ValueExpression,param:ValueExpression):T
  def reduceOption(calleeType: MonadDslType, callee: ValueExpression, param: ValueExpression): T

  def isEmpty(calleeType: MonadDslType, callee: ValueExpression): T

  def nonEmpty(calleeType: MonadDslType, callee: ValueExpression): T

  def length(calleeType: MonadDslType, callee: ValueExpression): T

  def mkString(calleeType:MonadDslType,callee:ValueExpression,sep:ValueExpression):T

  def mkString(calleeType:MonadDslType,
               callee:ValueExpression,
               start:ValueExpression,sep:ValueExpression,end:ValueExpression):T

  def head(calleeType: MonadDslType, callee: ValueExpression): T

  def tail(calleeType: MonadDslType, callee: ValueExpression): T

  def headOption(calleeType: MonadDslType, callee: ValueExpression): T

  def tailOption(calleeType: MonadDslType, callee: ValueExpression): T


  override def visit(calleeType: DslType, callee: ValueExpression, name: String, params: Array[ValueExpression]): Option[T] = {
    val calleeDslType = calleeType.asInstanceOf[MonadDslType]
    val carryDslType = calleeDslType.carryDslType

    val generator = () ⇒ {
      name match {
        case MAP_VALUE⇒mapValue(calleeDslType,callee,params.head)
        case MAP ⇒ map( calleeDslType, callee, params.head )
        case FLAT_MAP ⇒ flatMap( calleeDslType, callee, params.head )
        case FILTER_NOT ⇒ filterNot( calleeDslType, callee, params.head )
        case FILTER ⇒ filter( calleeDslType, callee, params.head )
        case FOREACH ⇒ foreach( calleeDslType, callee, params.head )
        case EXIST ⇒ exist( calleeDslType, callee, params.head )
        case FIND ⇒ find( calleeDslType, callee, params.head )
      }
    }

    (name match {
      case MAP_VALUE | MAP | FLAT_MAP | FILTER | FILTER_NOT | FOREACH | EXIST | FIND ⇒
        if(paramsChecker) {
          generate( Array( carryDslType ), params, (_, actualTypes) ⇒ {
            actualTypes.head match {
              case LambdaType(mayInputType,_) ⇒
                carryDslType match {
                  case TupleType( parameterTypes ) ⇒
                    mayInputType.isDefined && mayInputType.get.isInstanceOf[TupleType] &&
                      mayInputType.get.asInstanceOf[TupleType].parameterTypes.length == parameterTypes.length
                  case _ ⇒
                    mayInputType.isDefined
                }
              case _ ⇒
                false
            }
          }, generator )
        }else {
          Some(generator.apply( ) )
        }
      case REVERSE | FLATTEN | TO_LIST | TO_ARRAY | TO_SET | TO_SORTED_SET
           | TO_SEQ_SET | TO_MAP | TO_SEQ_MAP | TO_SORTED_MAP |
           ZIP_WITH_INDEX | IS_EMPTY | NON_EMPTY | LENGTH | HEAD | HEAD_OPTION | TAIL | TAIL_OPTION ⇒
        val generator = ()⇒ name match {
          case REVERSE ⇒ Some( reverse( calleeDslType, callee ) )
          case FLATTEN ⇒ Some( flatten( calleeDslType, callee ) )
          case TO_LIST ⇒ Some( toList( calleeDslType, callee ) )
          case TO_ARRAY ⇒ Some( toArray( calleeDslType, callee ) )
          case TO_SET ⇒ Some( toSet( calleeDslType, callee ) )
          case TO_SORTED_SET ⇒
            if(params.isEmpty) {
              Some( toSortedSet( calleeDslType, callee ) )
            }else{
              None
            }
          case TO_SEQ_SET ⇒ Some( toSeqSet( calleeDslType, callee ) )
          case TO_MAP | TO_SEQ_MAP | TO_SORTED_MAP ⇒
            carryDslType match {
              case TupleType( parameterTypes ) if parameterTypes.length == 2 ⇒
                Some( name match {
                  case TO_MAP ⇒ toMap( calleeDslType, callee )
                  case TO_SEQ_MAP ⇒ toSeqMap( calleeDslType, callee )
                  case TO_SORTED_MAP ⇒ toSortedMap( calleeDslType, callee )
                } )
              case _ ⇒
                None
            }
          case ZIP_WITH_INDEX ⇒
            Some( zipWithIndex( calleeDslType, callee ) )
          case IS_EMPTY ⇒ Some( isEmpty( calleeDslType, callee ) )
          case NON_EMPTY ⇒ Some( nonEmpty( calleeDslType, callee ) )
          case LENGTH ⇒ Some( length( calleeDslType, callee ) )
          case HEAD ⇒ Some(head(calleeDslType,callee))
          case HEAD_OPTION⇒Some(headOption(calleeDslType,callee))
          case TAIL⇒Some(tail(calleeDslType,callee))
          case TAIL_OPTION⇒Some(tailOption(calleeDslType,callee))
        }
        val result = if(paramsChecker) {
          generateOption( params, generator )
        }else{
          generator.apply()
        }
        if (result.isDefined) {
          result
        } else {
          val generator = () ⇒ {
            name match {
              case TO_SORTED_SET ⇒
                Some( toSortedSet( calleeDslType, callee, params.head ) )
              case TO_SORTED_MAP ⇒
                carryDslType match {
                  case TupleType( parameterTypes ) if parameterTypes.length == 2 ⇒
                    Some( toSortedMap( calleeDslType, callee, params.head ) )
                  case _ ⇒
                    None
                }
            }
          }
          name match {
            case TO_SORTED_SET | TO_SORTED_MAP ⇒
              if(paramsChecker) {
                generateOption( Array( carryDslType ), params, (_, _) ⇒ {
                  val lambda = params.head.asInstanceOf[Lambda]
                  lambda.inputs.length == 2
                }, generator )
              }else{
                generator.apply()
              }
            case _ ⇒ None
          }
        }
      case CONTAINS ⇒
        if(paramsChecker) {
          generate( Array( carryDslType ), params, () ⇒ contains( calleeDslType, callee, params.head ) )
        }else{
          Some(contains( calleeDslType, callee, params.head))
        }
      case MK_STRING ⇒
        if(paramsChecker){
          generateOverload(Array(Array(StringType),Array(StringType,StringType,StringType)),params, () ⇒ {
            if (params.length == 3) {
              mkString(calleeDslType,callee,params.head,params(1),params.last)
            }else{
              mkString(calleeDslType,callee,params.head)
            }
          })
        }else{
          if(params.length==1){
            Some(mkString(calleeDslType,callee,params.head))
          }else{
            Some(mkString(calleeDslType,callee,params.head,params(1),params.last))
          }
        }
      case REDUCE | REDUCE_OPTION | SORT ⇒
        val generator = () ⇒ {
          name match {
            case REDUCE ⇒
              if (params.length == 2) {
                Some( reduce( calleeDslType, callee, params.head, params.last ) )
              } else {
                Some( reduce( calleeDslType, callee, params.head ) )
              }
            case REDUCE_OPTION ⇒
              if (params.length == 1) {
                Some( reduceOption( calleeDslType, callee, params.head ) )
              } else {
                None
              }
            case SORT ⇒
              if (params.length == 1) {
                Some( sort( calleeDslType, callee, params.head ) )
              } else {
                None
              }
          }
        }
        if(paramsChecker) {
          generateOverloadOption( Array( Array( carryDslType ), Array( carryDslType, carryDslType ) ), params, (_, actualTypes) ⇒ {
            if (params.length == 2) {
              carryDslType.isSuperDslType( programScope.importManager, actualTypes.head ) &&
                actualTypes.last.isInstanceOf[LambdaType] &&
                actualTypes.last.asInstanceOf[LambdaType].getInputParamNum==2
            } else {
              actualTypes.head.isInstanceOf[LambdaType] && {
                actualTypes.head.asInstanceOf[LambdaType].getInputParamNum==2
              }
            }
          }, generator )
        }else{
          generator.apply()
        }
      case _ ⇒
        None
    }).orElse(super.visit(calleeType,callee,name,params))
  }
}


