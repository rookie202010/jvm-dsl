package com.dongjiaqiang.jvm.dsl.java.core.translate.method

import com.dongjiaqiang.jvm.dsl.api.`type`.{MonadDslType, TryType}
import com.dongjiaqiang.jvm.dsl.api.`type`.visitor.{MethodVisitor, TryMethodVisitor}
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope
import com.dongjiaqiang.jvm.dsl.java.api.extend.Try
import com.dongjiaqiang.jvm.dsl.java.core.translate.expression.JavaTranslator

class TryMethodJavaTranslator(override val programScope: ProgramScope,
                              val javaTranslator: JavaTranslator) extends MonadMethodJavaTranslator with TryMethodVisitor[String]{
  override val monadPath: Option[String] = None

  override def getException(calleeType: TryType, callee: ValueExpression): String =
    s"""
      |${javaTranslator.visit(callee)}.getException()
      |""".stripMargin

  override def get(calleeType: TryType, callee: ValueExpression): String = {
    val code = s"""
       |${javaTranslator.visit(callee)}.get()
       |""".stripMargin
    javaTranslator.specifyDslType(code,calleeType.carryDslType)
  }

  override def isSuccess(calleeType: TryType, callee: ValueExpression): String =
    s"""
       |${javaTranslator.visit(callee)}.success()
       |""".stripMargin

  override def isFailure(calleeType: TryType, callee: ValueExpression): String =
    s"""
       |${javaTranslator.visit(callee)}.failure()
       |""".stripMargin

  override def reverse(calleeType: MonadDslType, callee: ValueExpression): String = {
    throw new UnsupportedOperationException("does not supported")
  }

  override def sort(calleeType: MonadDslType, callee: ValueExpression, param: ValueExpression): String = {
    throw new UnsupportedOperationException("does not supported")
  }

  override def flatten(calleeType: MonadDslType, callee: ValueExpression): String = {
    MonadMethodJavaTranslator.transform(programScope,javaTranslator,Some(Try.CLAZZ_NAME),MethodVisitor.FLATTEN,calleeType,callee)
  }

  override def toSortedSet(calleeType: MonadDslType, callee: ValueExpression): String = {
    MonadMethodJavaTranslator.transform(programScope,javaTranslator,Some(Try.CLAZZ_NAME),MethodVisitor.TO_SORTED_SET,calleeType,callee)
  }

  override def toSortedSet(calleeType: MonadDslType, callee: ValueExpression, param: ValueExpression): String = {
    MonadMethodJavaTranslator.transform(programScope,javaTranslator,Some(Try.CLAZZ_NAME),MethodVisitor.TO_SORTED_SET,calleeType,callee,param)
  }

  override def toSortedMap(calleeType: MonadDslType, callee: ValueExpression): String = {
    MonadMethodJavaTranslator.transform(programScope,javaTranslator,Some(Try.CLAZZ_NAME),MethodVisitor.TO_SORTED_MAP,calleeType,callee)
  }

  override def toSortedMap(calleeType: MonadDslType, callee: ValueExpression, param: ValueExpression): String = {
    MonadMethodJavaTranslator.transform(programScope,javaTranslator,Some(Try.CLAZZ_NAME),MethodVisitor.TO_SORTED_MAP,calleeType,callee,param)
  }

  override def fold(calleeType: TryType, callee: ValueExpression, function1: ValueExpression, function2: ValueExpression): String = {
    val code = MonadMethodJavaTranslator.transform(programScope,javaTranslator, monadPath, MethodVisitor.FOLD, calleeType, callee, function1,function2 )
    javaTranslator.specifyDslType( code, calleeType.carryDslType )
  }

  override def orElse(calleeType: TryType, callee: ValueExpression, param: ValueExpression): String = {
    val code = MonadMethodJavaTranslator.transform(programScope,javaTranslator, monadPath, MethodVisitor.OR_ELSE, calleeType, callee, param )
    javaTranslator.specifyDslType( code, calleeType.carryDslType )
  }

  override def getOrElse(calleeType: TryType, callee: ValueExpression, param: ValueExpression): String = {
    val code = MonadMethodJavaTranslator.transform(programScope,javaTranslator, monadPath, MethodVisitor.GET_OR_ELSE, calleeType, callee, param )
    javaTranslator.specifyDslType( code, calleeType.carryDslType )
  }
}
