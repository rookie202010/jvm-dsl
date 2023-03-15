package com.dongjiaqiang.jvm.dsl.api.`type`.visitor

import com.dongjiaqiang.jvm.dsl.api.`type`.visitor.MonadMethodVisitor._
import com.dongjiaqiang.jvm.dsl.api.`type`.{DslType, MonadDslType, TupleType}
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.expression.block.Lambda

object MonadMethodVisitor{
  private val MAP = "map"
  private val FLATTEN = "flatten"
  private val FLAT_MAP = "flatMap"
  private val FILTER = "filter"
  private val FILTER_NOT = "filterNot"
  private val FOREACH = "foreach"
  private val EXIST = "exits"
  private val FIND = "find"
  private val TO_LIST = "toList"
  private val TO_SET = "toSet"
  private val TO_MAP = "toMap"
  private val CONTAINS = "contains"
  private val ZIP_WITH_INDEX = "zipWithIndex"
  private val REDUCE = "reduce"
  private val REDUCE_OPTION = "reduceOption"
  private val IS_EMPTY = "isEmpty"
  private val NON_EMPTY = "nonEmpty"
  private val LENGTH = "length"
}
trait MonadMethodVisitor[T] extends MethodVisitor[T]{
  def map(callee:ValueExpression,param:ValueExpression):T
  def flatten(callee:ValueExpression):T
  def flatMap(callee:ValueExpression,param:ValueExpression):T
  def filter(callee:ValueExpression,param:ValueExpression):T
  def filterNot(callee: ValueExpression, param: ValueExpression): T
  def foreach(callee:ValueExpression,param:ValueExpression):T
  def exist(callee:ValueExpression,param:ValueExpression):T
  def find(callee:ValueExpression,param:ValueExpression):T
  def toList(callee:ValueExpression):T
  def toSet(callee:ValueExpression):T
  def toMap(callee:ValueExpression):T
  def contains(callee:ValueExpression,param:ValueExpression):T
  def sort(callee:ValueExpression,param:ValueExpression):T
  def zipWithIndex(callee:ValueExpression):T
  def reduce(callee:ValueExpression,param:ValueExpression):T
  def reduceOption(callee:ValueExpression,param:ValueExpression):T
  def isEmpty(callee:ValueExpression):T
  def nonEmpty(callee:ValueExpression):T
  def length(callee:ValueExpression):T
  override def visit(calleeType:DslType,callee: ValueExpression, name: String, params: Array[ValueExpression]): Option[T] = {
      val carryDslType = calleeType.asInstanceOf[MonadDslType].carryDslType
      name match {
        case MAP | FLAT_MAP | FILTER | FILTER_NOT | FOREACH | EXIST | FIND ⇒
          require( params.length == 1 && params.head.isInstanceOf[Lambda] && {
            val lambda = params.head.asInstanceOf[Lambda]
            carryDslType match {
              case TupleType( parameterTypes ) ⇒
                lambda.inputs.length == parameterTypes.length
              case _ ⇒
                lambda.inputs.length == 1
            }
          }, s"method $name accept lambda param but actual is ${params.map(_.toString).mkString(",")}" )
          Some(name match {
            case MAP ⇒ map( callee, params.head )
            case FLAT_MAP ⇒ flatMap( callee, params.head )
            case FILTER_NOT ⇒ filterNot( callee, params.head )
            case FILTER ⇒ filter( callee, params.head )
            case FOREACH ⇒ foreach( callee, params.head )
            case EXIST ⇒ exist( callee, params.head )
            case FIND ⇒ exist( callee, params.head )
          })
        case FLATTEN | TO_LIST | TO_SET | TO_MAP | ZIP_WITH_INDEX | IS_EMPTY | NON_EMPTY | LENGTH ⇒
          require( params.isEmpty, s"method $name accept no params but actual is ${params.map(_.toString).mkString(",")}"  )
          Some(name match {
            case FLATTEN ⇒ flatten( callee )
            case TO_LIST ⇒ toList( callee )
            case TO_SET ⇒ toSet( callee )
            case TO_MAP ⇒
              require( carryDslType.isInstanceOf[TupleType] && {
                carryDslType.asInstanceOf[TupleType].parameterTypes.length == 2
              }, s"method $name accept no params but actual is ${params.map(_.toString).mkString(",")}" )
              toMap( callee )
            case ZIP_WITH_INDEX ⇒
              zipWithIndex( callee )
            case IS_EMPTY ⇒ isEmpty( callee )
            case LENGTH ⇒ length( callee )
          })
        case CONTAINS⇒
            require(params.length==1 && carryDslType.
              isSuperDslType(programScope.importManager,params.head.getValueType(programScope)),
              s"method $name accept type $carryDslType but actual is ${params.map(_.toString).mkString(",")}")
            Some(contains(callee,params.head))
        case REDUCE | REDUCE_OPTION⇒
            require(params.length==1 && params.head.isInstanceOf[Lambda] && {
              val lambda = params.head.asInstanceOf[Lambda]
              lambda.inputs.length==2
            },s"method $name accept lambda param but actual is ${params.map(_.toString).mkString(",")}" )
            Some(name match {
              case REDUCE⇒reduce(callee,params.head)
              case REDUCE_OPTION⇒reduceOption(callee,params.head)
            })
        case _⇒
          super.visit(calleeType,callee, name, params)
      }
  }
}


