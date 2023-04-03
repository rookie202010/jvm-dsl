package com.dongjiaqiang.jvm.dsl.api.`type`.visitor

import com.dongjiaqiang.jvm.dsl.api.`type`._
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope

object MethodVisitor{
    //monad method name
    val MAP = "map"
    val FLATTEN = "flatten"
    val FLAT_MAP = "flatMap"
    val FILTER = "filter"
    val FILTER_NOT = "filterNot"
    val FOREACH = "foreach"
    val EXIST = "exist"
    val FIND = "find"
    val TO_ARRAY = "toArray"
    val TO_LIST = "toList"
    val TO_SET = "toSet"
    val TO_SEQ_SET = "toSeqSet"
    val TO_SORTED_SET = "toSortedSet"
    val TO_MAP = "toMap"
    val TO_SEQ_MAP = "toSeqMap"
    val TO_SORTED_MAP = "toSortedMap"
    val CONTAINS = "contains"
    val ZIP_WITH_INDEX = "zipWithIndex"
    val REDUCE = "reduce"
    val REDUCE_OPTION = "reduceOption"
    val IS_EMPTY = "isEmpty"
    val NON_EMPTY = "nonEmpty"
    val LENGTH = "length"
    val SORT = "sort"
    val REVERSE = "reverse"
    val MK_STRING = "mkString"

    //seq method names
    val INDEX_OF = "indexOf"
    val GET = "get"
    val SPLIT_AT = "splitAt"
    val ADD = "add"
    val ADD_ALL = "addAll"
    val REMOVE = "remove"
    val UPDATE = "update"
    val HEAD = "head"
    val HEAD_OPTION = "headOption"
    val TAIL = "tail"
    val TAIL_OPTION = "tailOption"
    val MAX = "max"
    val MIN = "min"
    val SUM = "sum"

    //map method names
    val KEYS = "keys"
    val VALUES = "values"
    val PUT = "put"
    val CONTAIN_KEY = "containKey"

    //try method names
    val GET_EXCEPTION = "getException"
    val IS_SUCCESS = "isSuccess"
    val IS_FAILURE = "isFailure"
    val fold = "fold"

    //option method names
    val GET_OR_ELSE = "getOrElse"
    val OR_ELSE = "orElse"
    val IS_DEFINE = "isDefine"

    //string method names
    val CHAR_AT = "charAt"
    val SPLIT = "split"
    val FORMAT = "format"
    val TRIM = "trim"
    val TO_UPPER_CASE = "toUpperCase"
    val TO_LOWER_CASE = "toLowerCase"
    val GET_BYTES = "getBytes"
    val SUB_STRING = "subString"
    val IS_NUMERIC = "isNumeric"

    //range method names
    val TO = "to"
    val UNTIL = "until"

    //lambda method names
    val SUPPLY = "supply"
    val APPLY = "apply"

    //future method names
    val IS_COMPLETE = "isComplete"
    val IS_CANCELLED = "isCancelled"
    val IS_COMPLETE_EXCEPTIONALLY = "isCompleteExceptionally"
    val GET_NOW = "getNow"
    val COMPLETE = "complete"
    val COMPLETE_EXCEPTIONALLY = "completeExceptionally"

    //convert method names
    val TO_CHAR = "toChar"
    val TO_BYTE = "toByte"
    val TO_LONG = "toLong"
    val TO_FLOAT = "toFloat"
    val TO_INT = "toInt"
    val TO_DOUBLE = "toDouble"

    //any method names
    val HASH_CODE = "hashCode"
    val TO_STRING = "toString"
    val EQUALS = "equals"

    //either method names
    val LEFT = "left"
    val RIGHT = "right"
    val IS_LEFT = "isLeft"
    val IS_RIGHT = "isRight"
    val TO_LEFT = "toLeft"
    val TO_RIGHT = "toRight"
    val FOLD = "fold"
    val MAP_LEFT = "mapLeft"
    val SWAP = "swap"
    val TO_TRY = "toTry"
}
trait MethodVisitor[T] {

  val programScope:ProgramScope

  val paramsChecker:Boolean = true

  def generate(params: Array[ValueExpression],
               generator: () ⇒ T):Option[T]={
      generateOption(params,()⇒ Some(generator.apply()))
  }

  def generateOption(params: Array[ValueExpression],
                     generator: () ⇒ Option[T]): Option[T] = {
    if (params.isEmpty) {
     generator.apply( )
    } else {
      None
    }
  }

  def generate(requireTypes: Array[DslType],
               params: Array[ValueExpression],
               generator:()⇒T): Option[T] = {
    generateOption(requireTypes,params,()⇒Some(generator.apply()))
  }

  def generateOption(requireTypes: Array[DslType],
                     params: Array[ValueExpression],
                     generator: () ⇒ Option[T]): Option[T] = {
    generateOverloadOption( Array( requireTypes ), params, generator )
  }


  def generate(requireTypes: Array[DslType],
               params: Array[ValueExpression],
               checker:(Array[DslType],Array[DslType])⇒Boolean,
               generator: () ⇒ T): Option[T] = {
    generateOption( requireTypes, params,checker, ()⇒Some(generator.apply()))
  }

  def generateOption(requireTypes: Array[DslType],
                     params: Array[ValueExpression],
                     checker: (Array[DslType], Array[DslType]) ⇒ Boolean,
                     generator: () ⇒ Option[T]): Option[T] = {
    generateOverloadOption( Array( requireTypes ), params, checker, generator )
  }

  def generateOverload(requireTypeGroup:Array[Array[DslType]],
                       params: Array[ValueExpression],
                       generator:()⇒T):Option[T]={
    generateOverloadOption(requireTypeGroup,params, ()⇒Some(generator.apply()))
  }

  def generateOverloadOption(requireTypeGroup: Array[Array[DslType]],
                             params: Array[ValueExpression],
                             generator: () ⇒ Option[T]): Option[T] = {
    generateOverloadOption( requireTypeGroup, params, (requireTypes, actualTypes) ⇒ requireTypes.zip( actualTypes ).forall( d ⇒ d._1.isSuperDslType( programScope.importManager, d._2 ) ), generator )
  }


  def generateOverloadOption(requireTypeGroup: Array[Array[DslType]],
                             params: Array[ValueExpression],
                             checker: (Array[DslType], Array[DslType]) ⇒ Boolean,
                             generator: () ⇒ Option[T]): Option[T] = {
    val actualTypes = params.map( _.getValueType( programScope ) )
    if (requireTypeGroup.exists( requireTypes ⇒ {
      (requireTypes.isEmpty && params.isEmpty) ||
        (requireTypes.length == params.length && checker.apply( requireTypes, actualTypes ))
    } )) {
      generator.apply( )
    } else {
      None
    }
  }

  def generateOverload(requireTypeGroup: Array[Array[DslType]],
                       params: Array[ValueExpression],
                       checker:(Array[DslType],Array[DslType])⇒Boolean,
                       generator: () ⇒ T): Option[T] = {
      generateOverloadOption(requireTypeGroup,params,checker, ()⇒Some(generator.apply()))
  }

  def visit(calleeDslType:DslType,callee:ValueExpression,name:String,params:Array[ValueExpression]):Option[T] = None

}

/**
 * IntType and LongType method visitor
 */
trait IntMethodVisitor[T] extends ConvertTypeMethodVisitor[T]

/**
 *FloatType and DoubleType method visitor
 */
trait FloatMethodVisitor[T] extends ConvertTypeMethodVisitor[T]

/**
 *CharType method visitor
 */
trait CharMethodVisitor[T] extends ConvertTypeMethodVisitor[T] {
  def toLowerCase(callee: ValueExpression): T

  def toUpperCase(callee: ValueExpression): T

  override def visit(calleeDslType: DslType, callee: ValueExpression, name: String, params: Array[ValueExpression]): Option[T] = {
    (name match {
      case MethodVisitor.TO_LOWER_CASE ⇒
        generate( params,()⇒toLowerCase(callee))
      case MethodVisitor.TO_UPPER_CASE ⇒
        generate( params,()⇒toUpperCase(callee))
        Some( toUpperCase( callee ) )
      case _ ⇒
        None
    }).orElse(super.visit( calleeDslType, callee, name, params ))
  }
}

/**
 *ByteType method visitor
 */
trait ByteMethodVisitor[T] extends ConvertTypeMethodVisitor[T]

/**
 * ListType method visitor
 */
trait ListMethodVisitor[T] extends MonadMethodVisitor[T] with SeqMethodVisitor[T]

/**
 * ArrayType method visitor
 */
trait ArrayMethodVisitor[T] extends MonadMethodVisitor[T]

/**
 * SetType method visitor
 */
trait SetMethodVisitor[T] extends MonadMethodVisitor[T] with SeqMethodVisitor[T]








