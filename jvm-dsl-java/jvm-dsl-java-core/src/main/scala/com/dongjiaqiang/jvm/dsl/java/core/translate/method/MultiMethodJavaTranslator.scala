package com.dongjiaqiang.jvm.dsl.java.core.translate.method

import com.dongjiaqiang.jvm.dsl.api.`type`.{ArrayType, ByteType, CharType, DefinitionClazzType, DoubleType, DslType, FloatType, IntType, ListType, LongType, MapType, OptionType, SetType, StringType, TryType}
import com.dongjiaqiang.jvm.dsl.api.`type`.visitor.{AnyMethodVisitor, MethodVisitor, MultiMethodVisitor}
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope
import com.dongjiaqiang.jvm.dsl.java.core.translate.expression.JavaTranslator


class AnyMethodJavaTranslator(override val programScope: ProgramScope,
                              val javaTranslator: JavaTranslator) extends AnyMethodVisitor[String]{
  override def hashCode(callee: ValueExpression): String = s"${javaTranslator.visit(callee)}.hashCode()"

  override def equals(callee: ValueExpression, param: ValueExpression): String =  s"${javaTranslator.visit(callee)}.equals(${javaTranslator.visit(param)})"

  override def toString(callee: ValueExpression): String =  s"${javaTranslator.visit(callee)}.toString()"
}
class MultiMethodJavaTranslator(override val programScope: ProgramScope,
                                val javaTranslator: JavaTranslator) extends MultiMethodVisitor[String]{

  {
    registerSysVisitor(classOf[ArrayType],new ArrayMethodJavaTranslator(programScope, javaTranslator))
    registerSysVisitor(classOf[SetType],new SetMethodJavaTranslator(programScope, javaTranslator))
    registerSysVisitor(classOf[MapType],new MapMethodJavaTranslator(programScope, javaTranslator))
    registerSysVisitor(classOf[ListType],new ListMethodJavaTranslator(programScope, javaTranslator))
    registerSysVisitor(classOf[OptionType],new OptionMethodJavaTranslator(programScope, javaTranslator))
    registerSysVisitor(classOf[TryType],new TryMethodJavaTranslator(programScope, javaTranslator))

    registerSysVisitor(StringType.getClass.asSubclass(classOf[DslType]),new StringMethodJavaTranslator(programScope, javaTranslator))
    registerSysVisitor(IntType.getClass.asSubclass(classOf[DslType]),new IntMethodJavaTranslator(programScope, javaTranslator))
    registerSysVisitor(LongType.getClass.asSubclass(classOf[DslType]),new IntMethodJavaTranslator(programScope, javaTranslator))
    registerSysVisitor(FloatType.getClass.asSubclass(classOf[DslType]),new FloatMethodJavaTranslator(programScope, javaTranslator))
    registerSysVisitor(DoubleType.getClass.asSubclass(classOf[DslType]),new FloatMethodJavaTranslator(programScope, javaTranslator))
    registerSysVisitor(CharType.getClass.asSubclass(classOf[DslType]),new CharMethodJavaTranslator(programScope, javaTranslator))
    registerSysVisitor(ByteType.getClass.asSubclass(classOf[DslType]),new ByteMethodJavaTranslator(programScope, javaTranslator))

    registerSysVisitor(classOf[DefinitionClazzType],new DefinitionClazzMethodJavaTranslator(programScope, javaTranslator))

  }

  override val anyVisitor: AnyMethodVisitor[String] = new AnyMethodJavaTranslator(programScope, javaTranslator)
}
