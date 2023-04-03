package com.dongjiaqiang.jvm.dsl.api.`type`.visitor

import com.dongjiaqiang.jvm.dsl.api.`type`.{DslType, IntType, StringType}
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.`type`.visitor.MethodVisitor._

/**
 * StringType method visitor
 */
trait StringMethodVisitor[T] extends MonadMethodVisitor[T] with ConvertTypeMethodVisitor[T] {

  def charAt(callee: ValueExpression, param: ValueExpression): T
  def split(callee: ValueExpression, param: ValueExpression): T
  def head(callee:ValueExpression):T
  def headOption(callee:ValueExpression):T
  def tail(callee:ValueExpression):T
  def tailOption(callee:ValueExpression):T
  def split(callee: ValueExpression, param: ValueExpression,limit:ValueExpression): T
  def format(callee: ValueExpression, param: Array[ValueExpression]): T
  def trim(callee: ValueExpression): T
  def toUpperCase(callee: ValueExpression): T
  def toLowerCase(callee: ValueExpression): T
  def getBytes(callee: ValueExpression): T
  def getBytes(callee: ValueExpression, param: ValueExpression): T
  def subString(callee: ValueExpression, startIndex: ValueExpression, endIndex: ValueExpression): T
  def isNumeric(callee:ValueExpression):T
  override def visit(calleeType: DslType, callee: ValueExpression, name: String, params: Array[ValueExpression]): Option[T] = {
    (name match {
      case CHAR_AT ⇒
        generate( Array( IntType ), params, () ⇒ {
           charAt( callee, params.head )
        } )
      case IS_NUMERIC⇒
        generate(params,()⇒ isNumeric(callee))
      case HEAD⇒
        generate(params,()⇒head(callee))
      case HEAD_OPTION⇒
        generate(params,()⇒headOption(callee))
      case TAIL⇒
        generate(params,()⇒tail(callee))
      case TAIL_OPTION⇒
        generate(params,()⇒tailOption(callee))
      case SPLIT⇒
          generateOverload(Array(Array(StringType),Array(StringType,IntType)),params,()⇒{
              if(params.length==1){
                  split(callee,params.head)
              }else{
                  split(callee,params.head,params.last)
              }
          })
      case FORMAT ⇒
        Some( format( callee, params ) )
      case TRIM ⇒
        generate( params, () ⇒ trim( callee ) )
      case TO_UPPER_CASE ⇒
        generate( params, () ⇒ toUpperCase( callee ) )
      case TO_LOWER_CASE ⇒
        generate( params, () ⇒ toLowerCase( callee ) )
      case GET_BYTES⇒
        generateOverload( Array( Array( ), Array( StringType ) ), params, () ⇒ {
          if (params.isEmpty) {
            getBytes( callee )
          } else {
            getBytes( callee, params.head )
          }
        } )
      case SUB_STRING ⇒
        generate( Array( IntType, IntType ), params, () ⇒ subString( callee, params.head, params.last ) )
      case _ ⇒
        None
    }).orElse( super.visit( calleeType, callee, name, params ) )
  }
}
