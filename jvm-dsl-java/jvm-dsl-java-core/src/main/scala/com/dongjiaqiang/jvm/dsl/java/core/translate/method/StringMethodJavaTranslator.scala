package com.dongjiaqiang.jvm.dsl.java.core.translate.method

import com.dongjiaqiang.jvm.dsl.api.`type`.{DslType, StringType}
import com.dongjiaqiang.jvm.dsl.api.`type`.visitor.{MethodVisitor, StringMethodVisitor}
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope
import com.dongjiaqiang.jvm.dsl.java.api.codes._SYS_STR_CODES
import com.dongjiaqiang.jvm.dsl.java.core.translate.expression.JavaTranslator

class StringMethodJavaTranslator(override val programScope: ProgramScope,
                                  val javaTranslator: JavaTranslator) extends MonadMethodJavaTranslator with StringMethodVisitor[String]{

  override val monadPath: Option[String] = Some(_SYS_STR_CODES.CLAZZ_NAME)

  override def charAt(callee: ValueExpression, param: ValueExpression): String = {
    s"${javaTranslator.visit(callee)}.charAt(${javaTranslator.visit(param)})"
  }

  override def split(callee: ValueExpression, param: ValueExpression): String = {
    s"${javaTranslator.visit(callee)}.split(${javaTranslator.visit(param)})"
  }

  override def split(callee: ValueExpression, param: ValueExpression,limit:ValueExpression): String = s"${javaTranslator.visit(callee)}.split(${javaTranslator.visit(param)},${javaTranslator.visit(limit)})"


  override def format(callee: ValueExpression, param: Array[ValueExpression]): String = {
      val ps = param.map(p⇒javaTranslator.visit(p)).mkString(",")
      s"String.format(${javaTranslator.visit(callee)},$ps)"
  }

  override def trim(callee: ValueExpression): String = s"${javaTranslator.visit(callee)}.trim()"

  override def toUpperCase(callee: ValueExpression): String = s"${javaTranslator.visit(callee)}.toUpperCase()"

  override def toLowerCase(callee: ValueExpression): String = s"${javaTranslator.visit(callee)}.toLowerCase()"

  override def getBytes(callee: ValueExpression): String = s"${javaTranslator.visit(callee)}.getBytes()"


  override def isNumeric(callee: ValueExpression): String =
    MonadMethodJavaTranslator.transform(programScope,javaTranslator,monadPath,MethodVisitor.IS_NUMERIC,StringType,callee)

  override def getBytes(callee: ValueExpression, param: ValueExpression): String = s"${javaTranslator.visit(callee)}.getBytes(${javaTranslator.visit(param)})"

  override def subString(callee: ValueExpression, startIndex: ValueExpression, endIndex: ValueExpression): String = s"${javaTranslator.visit(callee)}.substring(${javaTranslator.visit(startIndex)},${javaTranslator.visit(endIndex)})"

  override def toInt(calleeDslType: DslType, callee: ValueExpression): String = s"Integer.parseInt(${javaTranslator.visit(callee)})"

  override def toLong(calleeDslType: DslType, callee: ValueExpression): String = s"Long.parseLong(${javaTranslator.visit(callee)})"

  override def toFloat(calleeDslType: DslType, callee: ValueExpression): String = s"Float.parseFloat(${javaTranslator.visit(callee)})"

  override def toDouble(calleeDslType: DslType, callee: ValueExpression): String = s"Double.parseDouble(${javaTranslator.visit(callee)})"

  override def toChar(calleeDslType: DslType, callee: ValueExpression): String = throw new UnsupportedOperationException("un supported")

  override def toByte(calleeDslType: DslType, callee: ValueExpression): String = throw new UnsupportedOperationException("un supported")

  override def head(callee: ValueExpression): String = MonadMethodJavaTranslator.transform(programScope,javaTranslator,monadPath,MethodVisitor.HEAD,StringType,callee)

  override def headOption(callee: ValueExpression): String = MonadMethodJavaTranslator.transform(programScope,javaTranslator,monadPath,MethodVisitor.HEAD_OPTION,StringType,callee)

  override def tail(callee: ValueExpression): String = MonadMethodJavaTranslator.transform(programScope,javaTranslator,monadPath,MethodVisitor.TAIL,StringType,callee)
  override def tailOption(callee: ValueExpression): String = MonadMethodJavaTranslator.transform(programScope,javaTranslator,monadPath,MethodVisitor.TAIL_OPTION,StringType,callee)

  override def startsWith(callee: ValueExpression, param: ValueExpression): String = s"${javaTranslator.visit(callee)}.startsWith(${javaTranslator.visit(param)})"

  override def startsWith(callee: ValueExpression, param: ValueExpression, offset: ValueExpression): String = s"${javaTranslator.visit(callee)}.startsWith(${javaTranslator.visit(param)},${javaTranslator.visit(offset)})"

  override def endsWith(callee: ValueExpression, param: ValueExpression): String = s"${javaTranslator.visit(callee)}.endsWith(${javaTranslator.visit(param)})"

}
