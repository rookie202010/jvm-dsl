package com.dongjiaqiang.jvm.dsl.java.core.translate.method

import com.dongjiaqiang.jvm.dsl.api.`type`.{MonadDslType, OptionType}
import com.dongjiaqiang.jvm.dsl.api.`type`.visitor.{MethodVisitor, OptionMethodVisitor}
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope
import com.dongjiaqiang.jvm.dsl.java.api.codes._SYS_OPTION_CODES
import com.dongjiaqiang.jvm.dsl.java.core.translate.expression.JavaTranslator

class OptionMethodJavaTranslator(override val programScope: ProgramScope,
                                 val javaTranslator: JavaTranslator) extends MonadMethodJavaTranslator with OptionMethodVisitor[String]{


  override val monadPath: Option[String] = Some( _SYS_OPTION_CODES.CLAZZ_NAME )

  override def get(calleeType: OptionType, callee: ValueExpression): String = {
      val code = MonadMethodJavaTranslator.transform(programScope,javaTranslator,None ,MethodVisitor.GET,calleeType,callee)
      javaTranslator.specifyDslType(code,calleeType.carryDslType)
  }

  override def orElse(calleeType: OptionType, callee: ValueExpression, default: ValueExpression): String = {
     val code = MonadMethodJavaTranslator.transform(programScope,javaTranslator,None,MethodVisitor.OR_ELSE,calleeType,callee,default)
    javaTranslator.specifyDslType(code,calleeType.carryDslType)
  }


  override def getOrElse(calleeType: OptionType, callee: ValueExpression, default: ValueExpression): String = {
    val code = MonadMethodJavaTranslator.transform(programScope,javaTranslator,monadPath, MethodVisitor.GET_OR_ELSE, calleeType, callee,default )
    javaTranslator.specifyDslType( code, calleeType.carryDslType )
  }

  override def isDefine(calleeType: OptionType, callee: ValueExpression): String = {
      MonadMethodJavaTranslator.transform(programScope,javaTranslator,None,"isPresent",calleeType,callee)
  }
}
