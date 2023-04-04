package com.dongjiaqiang.jvm.dsl.java.core.translate.method

import com.dongjiaqiang.jvm.dsl.api.`type`.DslType
import com.dongjiaqiang.jvm.dsl.api.`type`.visitor.{ByteMethodVisitor, CharMethodVisitor, FloatMethodVisitor, IntMethodVisitor, RangeMethodVisitor}
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.expression.unary.Cast
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope
import com.dongjiaqiang.jvm.dsl.java.api.codes._SYS_GEN_CODES
import com.dongjiaqiang.jvm.dsl.java.core.translate.expression.JavaTranslator

object BasicTranslator{
  def toBasicType(javaTranslator: JavaTranslator,callee:ValueExpression,typeName:String):String={
      callee match {
        case Cast(_,_,1)⇒
            typeName match {
              case "char"⇒
                s"(char)(${javaTranslator.visit( callee )}.intValue())"
              case _⇒
                s"${javaTranslator.visit( callee )}.${typeName}Value()"
            }
        case _⇒
          s"($typeName)(${javaTranslator.visit( callee )})"
      }
  }
}

class IntMethodJavaTranslator(override val programScope: ProgramScope,
                              val javaTranslator: JavaTranslator) extends IntMethodVisitor[String] with RangeMethodVisitor[String]{
  override def toInt(calleeType:DslType,callee: ValueExpression): String = {
      BasicTranslator.toBasicType(javaTranslator,callee,"int")
  }
  override def toLong(calleeType:DslType,callee: ValueExpression): String = {
    BasicTranslator.toBasicType(javaTranslator,callee,"long")
  }
  override def toFloat(calleeType:DslType,callee: ValueExpression): String = {
    BasicTranslator.toBasicType(javaTranslator,callee,"float")
  }
  override def toDouble(calleeType:DslType,callee: ValueExpression): String = {
    BasicTranslator.toBasicType(javaTranslator,callee,"double")
  }
  override def toChar(calleeType:DslType,callee: ValueExpression): String = {
    BasicTranslator.toBasicType(javaTranslator,callee,"char")
  }
  override def toByte(calleeType:DslType,callee: ValueExpression): String = {
    BasicTranslator.toBasicType(javaTranslator,callee,"byte")
  }

  override def to(callee: ValueExpression, end: ValueExpression): String =
    s"""
      |${_SYS_GEN_CODES.CLAZZ_NAME}.to(${javaTranslator.visit(callee)},${javaTranslator.visit(end)})
      |""".stripMargin

  override def to(callee: ValueExpression, end: ValueExpression, step: ValueExpression): String =
    s"""
       |${_SYS_GEN_CODES.CLAZZ_NAME}.to(${javaTranslator.visit( callee )},${javaTranslator.visit( end )},${javaTranslator.visit(step)})
       |""".stripMargin

  override def until(callee: ValueExpression, end: ValueExpression): String =
    s"""
       |${_SYS_GEN_CODES.CLAZZ_NAME}.until(${javaTranslator.visit( callee )},${javaTranslator.visit( end )})
       |""".stripMargin

  override def until(callee: ValueExpression, end: ValueExpression, step: ValueExpression): String =
    s"""
       |${_SYS_GEN_CODES.CLAZZ_NAME}.until(${javaTranslator.visit( callee )},${javaTranslator.visit( end )},${javaTranslator.visit(step)})
       |""".stripMargin
}

class FloatMethodJavaTranslator(override val programScope: ProgramScope,
                                val javaTranslator: JavaTranslator) extends FloatMethodVisitor[String] {
  override def toInt(calleeType:DslType,callee: ValueExpression): String = {
    BasicTranslator.toBasicType(javaTranslator,callee,"int")
  }
  override def toLong(calleeType:DslType,callee: ValueExpression): String = {
    BasicTranslator.toBasicType(javaTranslator,callee,"long")
  }
  override def toFloat(calleeType:DslType,callee: ValueExpression): String = {
    BasicTranslator.toBasicType(javaTranslator,callee,"float")
  }
  override def toDouble(calleeType:DslType,callee: ValueExpression): String = {
    BasicTranslator.toBasicType(javaTranslator,callee,"double")
  }
  override def toChar(calleeType:DslType,callee: ValueExpression): String = {
    BasicTranslator.toBasicType(javaTranslator,callee,"char")
  }
  override def toByte(calleeType:DslType,callee: ValueExpression): String = {
    BasicTranslator.toBasicType(javaTranslator,callee,"byte")
  }
}

class CharMethodJavaTranslator(override val programScope: ProgramScope,
                               val javaTranslator: JavaTranslator) extends CharMethodVisitor[String]{
  override def toLowerCase(callee: ValueExpression): String =
    s"""
       |Character.toLowerCase(${javaTranslator.visit( callee )})
       |""".stripMargin

  override def toUpperCase(callee: ValueExpression): String =
    s"""
       |Character.toUpperCase(${javaTranslator.visit(callee)})
       |""".stripMargin

  override def toInt(calleeType:DslType,callee: ValueExpression): String = {
    BasicTranslator.toBasicType(javaTranslator,callee,"int")
  }
  override def toLong(calleeType:DslType,callee: ValueExpression): String =  {
    BasicTranslator.toBasicType(javaTranslator,callee,"long")
  }
  override def toFloat(calleeType:DslType,callee: ValueExpression): String = {
    BasicTranslator.toBasicType(javaTranslator,callee,"float")
  }
  override def toDouble(calleeType:DslType,callee: ValueExpression): String = {
    BasicTranslator.toBasicType(javaTranslator,callee,"double")
  }
  override def toChar(calleeType:DslType,callee: ValueExpression): String = {
    BasicTranslator.toBasicType(javaTranslator,callee,"char")
  }
  override def toByte(calleeType:DslType,callee: ValueExpression): String = {
    BasicTranslator.toBasicType(javaTranslator,callee,"byte")
  }
}

class ByteMethodJavaTranslator(override val programScope: ProgramScope, val javaTranslator: JavaTranslator) extends ByteMethodVisitor[String]{
  override def toInt(calleeType:DslType,callee: ValueExpression): String = {
    BasicTranslator.toBasicType(javaTranslator,callee,"int")
  }

  override def toLong(calleeType:DslType,callee: ValueExpression): String = {
    BasicTranslator.toBasicType(javaTranslator,callee,"long")
  }

  override def toFloat(calleeType:DslType,callee: ValueExpression): String = {
    BasicTranslator.toBasicType(javaTranslator,callee,"float")
  }

  override def toDouble(calleeType:DslType,callee: ValueExpression): String = {
    BasicTranslator.toBasicType(javaTranslator,callee,"double")
  }

  override def toChar(calleeType:DslType,callee: ValueExpression): String = {
    BasicTranslator.toBasicType(javaTranslator,callee,"char")
  }

  override def toByte(calleeType:DslType,callee: ValueExpression): String = {
    BasicTranslator.toBasicType(javaTranslator,callee,"byte")
  }
}


