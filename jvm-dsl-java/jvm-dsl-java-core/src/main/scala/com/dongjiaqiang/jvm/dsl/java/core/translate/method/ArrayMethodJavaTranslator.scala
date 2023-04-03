package com.dongjiaqiang.jvm.dsl.java.core.translate.method

import com.dongjiaqiang.jvm.dsl.api.`type`.MonadDslType
import com.dongjiaqiang.jvm.dsl.api.`type`.visitor.SetMethodVisitor
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope
import com.dongjiaqiang.jvm.dsl.java.api.codes.{_SYS_ARRAY_CODES, _SYS_SET_CODES}
import com.dongjiaqiang.jvm.dsl.java.core.translate.expression.JavaTranslator

class ArrayMethodJavaTranslator(override val programScope: ProgramScope,
                                val javaTranslator: JavaTranslator) extends MonadMethodJavaTranslator with SeqMethodJavaTranslator with SetMethodVisitor[String]{

  override val monadPath: Option[String]= Some( _SYS_ARRAY_CODES.CLAZZ_NAME )
  override val seqPath: String = _SYS_ARRAY_CODES.CLAZZ_NAME

  override def indexOf(calleeDslType: MonadDslType, callee: ValueExpression, element: ValueExpression): String = super.indexOf( calleeDslType, callee, element )

  override def get(calleeDslType: MonadDslType, callee: ValueExpression, index: ValueExpression): String = {
    val code =
      s"""
         |${javaTranslator.visit( callee )}[${javaTranslator.visit( index )}]
         |""".stripMargin
    javaTranslator.specifyDslType( code, calleeDslType.carryDslType )
  }

  override def splitAt(calleeDslType: MonadDslType, callee: ValueExpression, index: ValueExpression): String = super.splitAt( calleeDslType, callee, index )

  override def add(calleeDslType: MonadDslType, callee: ValueExpression, index: ValueExpression, element: ValueExpression): String = {
    throw new UnsupportedOperationException("un supported")
  }

  override def add(calleeDslType: MonadDslType, callee: ValueExpression, element: ValueExpression): String = {
    throw new UnsupportedOperationException( "un supported" )
  }

  override def addAll(calleeDslType: MonadDslType, callee: ValueExpression, index: ValueExpression, element: ValueExpression): String = {
    throw new UnsupportedOperationException( "un supported" )
  }

  override def addAll(calleeDslType: MonadDslType, callee: ValueExpression, element: ValueExpression): String = {
    throw new UnsupportedOperationException( "un supported" )
  }

  override def remove(calleeDslType: MonadDslType, callee: ValueExpression, indexOrElement: ValueExpression): String = {
    throw new UnsupportedOperationException( "un supported" )
  }

  override def update(calleeDslType: MonadDslType, callee: ValueExpression, index: ValueExpression, element: ValueExpression): String = {
    s"""
       |${javaTranslator.visit( callee )}[${javaTranslator.visit( index )}]=${javaTranslator.visit( element )}
       |""".stripMargin
  }
}
