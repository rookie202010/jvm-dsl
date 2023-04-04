package com.dongjiaqiang.jvm.dsl.java.core.translate.method

import com.dongjiaqiang.jvm.dsl.api.`type`.{ListType, MapType, MonadDslType}
import com.dongjiaqiang.jvm.dsl.api.`type`.visitor.MapMethodVisitor
import com.dongjiaqiang.jvm.dsl.api.`type`.visitor.MethodVisitor.{FILTER, MAP_VALUE}
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope
import com.dongjiaqiang.jvm.dsl.java.api.codes._SYS_MAP_CODES
import com.dongjiaqiang.jvm.dsl.java.api.expression.JavaCode
import com.dongjiaqiang.jvm.dsl.java.core.translate.expression.{JavaTranslator, LambdaToAnonymousClassTranslator}
class MapMethodJavaTranslator(override val programScope: ProgramScope, val javaTranslator: JavaTranslator) extends MonadMethodJavaTranslator with MapMethodVisitor[String] {

  override val monadPath: Option[String] = Some( _SYS_MAP_CODES.CLAZZ_NAME )


  def transform(callee: ValueExpression, mapType: MapType): ValueExpression = {
    val keyType = com.dongjiaqiang.jvm.dsl.java.api.toJavaType( mapType.keyParameterType, javaTranslator.javaTranslatorContext )
    val valueType = com.dongjiaqiang.jvm.dsl.java.api.toJavaType( mapType.valueParameterType, javaTranslator.javaTranslatorContext )
    if (mapType.seq) {
      JavaCode( s"${javaTranslator.visit( callee )}," +
        s"new java.util.LinkedHashMap<$keyType,$valueType>()", mapType )
    } else if (mapType.sorted) {
      mapType.sorter match {
        case None ⇒
          JavaCode( s"${javaTranslator.visit( callee )},new java.util.TreeMap<$keyType,$valueType>()", mapType )
        case Some( lambda ) ⇒
          val comparator = LambdaToAnonymousClassTranslator.translateComparator( javaTranslator, javaTranslator.javaTranslatorContext, lambda )
          JavaCode( s"${javaTranslator.visit( callee )},new java.util.TreeMap<$keyType,$valueType>($comparator)", mapType )
      }
    } else {
      JavaCode( s"${javaTranslator.visit( callee )},new java.util.HashMap<$keyType,$valueType>()", mapType )
    }
  }
  override def mapValue(calleeType: MonadDslType, callee: ValueExpression, param: ValueExpression): String = {
    val mapType = calleeType.asInstanceOf[MapType]
    transform( MAP_VALUE,ListType(mapType.valueParameterType), transform(callee,mapType), param )
  }


  override def filter(calleeType: MonadDslType, callee: ValueExpression, param: ValueExpression): String = {
    transform(FILTER,calleeType,transform(callee,calleeType.asInstanceOf[MapType]),param)
  }

  override def filterNot(calleeType: MonadDslType, callee: ValueExpression, param: ValueExpression): String = super.filterNot( calleeType, callee, param )

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
