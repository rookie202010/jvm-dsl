package com.dongjiaqiang.jvm.dsl.api.`type`.visitor

import com.dongjiaqiang.jvm.dsl.api.`type`._
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope

trait MethodVisitor[T] {

  val programScope:ProgramScope

  def actualTypes(programScope: ProgramScope,params:Array[ValueExpression],methodName:String,calleeType:DslType):(Array[DslType],Array[String]=>String)={
      val actualTypes = params.map(_.getValueType(programScope))
    (actualTypes,requestInputTypes⇒errorMsg(methodName,calleeType, requestInputTypes, actualTypes))
  }

  def actualTypes(programScope: ProgramScope, params: Array[ValueExpression], methodName: String, calleeType: DslType,requestInputTypes:Array[String]): (Array[DslType], () => String) = {
    val actualTypes = params.map( _.getValueType( programScope ) )
    (actualTypes, () ⇒ errorMsg( methodName, calleeType, requestInputTypes, actualTypes ))
  }

  def visit(calleeDslType:DslType,callee:ValueExpression,name:String,params:Array[ValueExpression]):Option[T] = None

}

/**
 * IntType and LongType method visitor
 */
trait IntMethodVisitor[T] extends ConvertTypeMethodVisitor[T] with MethodVisitor[T]

/**
 *FloatType and DoubleType method visitor
 */
trait FloatMethodVisitor[T] extends ConvertTypeMethodVisitor[T] with MethodVisitor[T]

/**
 *CharType method visitor
 */
trait CharMethodVisitor[T] extends ConvertTypeMethodVisitor[T] with MethodVisitor[T]{
    def toLower(callee:ValueExpression):T
    def toUpper(callee:ValueExpression):T
}

/**
 *ByteType method visitor
 */
trait ByteMethodVisitor[T] extends ConvertTypeMethodVisitor[T] with MethodVisitor[T]

/**
 * ListType method visitor
 */
trait ListMethodVisitor[T] extends MonadMethodVisitor[T] with SeqMethodVisitor[T] with MethodVisitor[T]

/**
 * ArrayType method visitor
 */
trait ArrayMethodVisitor[T] extends MonadMethodVisitor[T] with MethodVisitor[T]








