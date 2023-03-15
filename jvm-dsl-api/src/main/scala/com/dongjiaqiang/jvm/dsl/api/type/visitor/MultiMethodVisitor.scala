package com.dongjiaqiang.jvm.dsl.api.`type`.visitor

import com.dongjiaqiang.jvm.dsl.api.`type`.DslType
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression

import scala.collection.mutable.{ArrayBuffer, ListMap ⇒ MutableMap}
trait MultiMethodVisitor[T] extends MethodVisitor[T] {

  private val sysVisitors: MutableMap[Class[_<:DslType], MethodVisitor[T]] = MutableMap( )

  private val extendVisitors: MutableMap[Class[_<:DslType], ArrayBuffer[MethodVisitor[T]]] = MutableMap( )

  val anyVisitor:AnyMethodVisitor[T]

  def registerSysVisitor(dslType: Class[_<:DslType], dslTypMethodVisitor: MethodVisitor[T]): Unit = {
    sysVisitors.put( dslType, dslTypMethodVisitor )
  }

  def registerExtendVisitor(dslType: Class[_<:DslType], dslTypMethodVisitor: MethodVisitor[T]): Unit = {
    extendVisitors.getOrElseUpdate( dslType, ArrayBuffer( ) ).append( dslTypMethodVisitor )
  }


  override def visit(calleeType: DslType, callee: ValueExpression, name: String, params: Array[ValueExpression]): Option[T] = {
    val sysMrt = sysVisitors.get( calleeType.getClass ).flatMap( _.visit( calleeType, callee, name, params ) )
    if (sysMrt.isDefined) {
      return sysMrt
    }
    extendVisitors.get( calleeType.getClass ) match {
      case Some( visitors ) ⇒
        val iterator = visitors.iterator
        while (iterator.hasNext) {
          val result = iterator.next( ).visit( calleeType, callee, name, params )
          if (result.isDefined) {
            return result
          }
        }
        anyVisitor.visit(calleeType,callee, name, params)
      case _ ⇒ anyVisitor.visit(calleeType,callee, name, params)
    }
  }
}



