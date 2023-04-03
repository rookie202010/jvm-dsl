package com.dongjiaqiang.jvm.dsl.java.core.translate.method

import com.dongjiaqiang.jvm.dsl.api.`type`.MonadDslType
import com.dongjiaqiang.jvm.dsl.api.`type`.visitor.{ListMethodVisitor, MethodVisitor}
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.expression.unary.Cast
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope
import com.dongjiaqiang.jvm.dsl.java.api.codes.{_SYS_COL_CODES, _SYS_LIST_CODES}
import com.dongjiaqiang.jvm.dsl.java.api.expression.JavaCode
import com.dongjiaqiang.jvm.dsl.java.core.translate.expression.JavaTranslator

class ListMethodJavaTranslator(override val programScope: ProgramScope,
                               val javaTranslator: JavaTranslator) extends MonadMethodJavaTranslator with SeqMethodJavaTranslator with ListMethodVisitor[String]{
  val monadPath: Option[String] = Some( _SYS_LIST_CODES.CLAZZ_NAME )
  override val seqPath: String = _SYS_COL_CODES.CLAZZ_NAME
}
