package com.dongjiaqiang.jvm.dsl.java.core.translate.method

import com.dongjiaqiang.jvm.dsl.api.`type`.DefinitionClazzType
import com.dongjiaqiang.jvm.dsl.api.`type`.visitor.DefinitionClazzMethodVisitor
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.scope.{MethodScope, ProgramScope}
import com.dongjiaqiang.jvm.dsl.java.core.translate.expression.JavaTranslator

class DefinitionClazzMethodJavaTranslator (override val programScope: ProgramScope,
                                           val javaTranslator: JavaTranslator) extends DefinitionClazzMethodVisitor[String]{
  override def visit(methodScope: MethodScope, calleeDslType: DefinitionClazzType, callee: ValueExpression, name: String, params: Array[ValueExpression]): String = {
        s"${javaTranslator.visit(callee)}.$name(${params.map(p⇒javaTranslator.visit(p)).mkString(",")})"
  }
}
