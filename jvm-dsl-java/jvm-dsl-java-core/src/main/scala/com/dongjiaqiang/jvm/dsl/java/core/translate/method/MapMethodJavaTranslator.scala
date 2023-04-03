package com.dongjiaqiang.jvm.dsl.java.core.translate.method

import com.dongjiaqiang.jvm.dsl.api.`type`.{MapType, MonadDslType}
import com.dongjiaqiang.jvm.dsl.api.`type`.visitor.MapMethodVisitor
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope
import com.dongjiaqiang.jvm.dsl.java.api.codes._SYS_MAP_CODES
import com.dongjiaqiang.jvm.dsl.java.core.translate.expression.JavaTranslator
class MapMethodJavaTranslator(override val programScope: ProgramScope, val javaTranslator: JavaTranslator) extends MonadMethodJavaTranslator with MapMethodVisitor[String] {

  override val monadPath: Option[String] = Some( _SYS_MAP_CODES.CLAZZ_NAME )

  override def keys(calleeType: MapType, callee: ValueExpression): String =
    s"""
       |${javaTranslator.visit( callee )}.keySet()
       |""".stripMargin

  override def values(calleeType: MapType, callee: ValueExpression): String =
    s"""
       |${_SYS_MAP_CODES.CLAZZ_NAME}.values(${javaTranslator.visit( callee )})
       |""".stripMargin

  override def containKey(calleeType: MapType, callee: ValueExpression, key: ValueExpression): String =
    s"""
       |${javaTranslator.visit( callee )}.containsKey(${javaTranslator.visit( key )})
       |""".stripMargin

  override def get(calleeType: MapType, callee: ValueExpression, key: ValueExpression): String = {
    val code =
      s"""
         |${javaTranslator.visit( callee )}.get(${javaTranslator.visit( key )})
         |""".stripMargin
    javaTranslator.specifyDslType( code, calleeType.valueParameterType )
  }

  override def put(calleeType: MapType, callee: ValueExpression, key: ValueExpression, value: ValueExpression): String =
    s"""
      |${javaTranslator.visit(callee)}.put(${javaTranslator.visit(key)},${javaTranslator.visit(value)})
      |""".stripMargin
}
