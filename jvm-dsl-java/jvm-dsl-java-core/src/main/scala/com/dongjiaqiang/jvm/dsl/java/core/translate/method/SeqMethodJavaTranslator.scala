package com.dongjiaqiang.jvm.dsl.java.core.translate.method

import com.dongjiaqiang.jvm.dsl.api.`type`.{ArrayType, MonadDslType}
import com.dongjiaqiang.jvm.dsl.api.`type`.visitor.{MethodVisitor, SeqMethodVisitor}
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.expression.unary.Cast
import com.dongjiaqiang.jvm.dsl.java.core.translate.expression.JavaTranslator

trait SeqMethodJavaTranslator extends SeqMethodVisitor[String]{
  val javaTranslator: JavaTranslator

  val seqPath:String

  override def max(calleeType: MonadDslType, callee: ValueExpression): String = MonadMethodJavaTranslator.transform(
    programScope,javaTranslator,
    Some( seqPath ),
    s"${MethodVisitor.MAX}${calleeType.carryDslType.name}", calleeType,

    {
      calleeType match {
        case ArrayType(parameterType)⇒ Cast(callee,ArrayType(parameterType))
        case _⇒callee
      }
    }

  )

  override def min(calleeType: MonadDslType, callee: ValueExpression): String = MonadMethodJavaTranslator.transform(
    programScope, javaTranslator,
    Some( seqPath ),
    s"${MethodVisitor.MIN}${calleeType.carryDslType.name}", calleeType, {
      calleeType match {
        case ArrayType( parameterType ) ⇒ Cast( callee, ArrayType( parameterType ) )
        case _ ⇒ callee
      }
    })

  override def sum(calleeType: MonadDslType, callee: ValueExpression): String = MonadMethodJavaTranslator.transform(
    programScope, javaTranslator,
    Some( seqPath ),
    s"${MethodVisitor.SUM}${calleeType.carryDslType.name}", calleeType, {

      calleeType match {
        case ArrayType( parameterType ) ⇒ Cast( callee, ArrayType( parameterType ) )
        case _ ⇒ callee
      }

    } )


  override def indexOf(calleeDslType: MonadDslType, callee: ValueExpression, element: ValueExpression): String = {
    s"""
       |${javaTranslator.visit( callee )}.indexOf(${javaTranslator.visit( element )})
       |""".stripMargin
  }

  override def get(calleeDslType: MonadDslType, callee: ValueExpression, index: ValueExpression): String = {
    val code =
      s"""
         |${javaTranslator.visit( callee )}.get(${javaTranslator.visit( index )})
         |""".stripMargin
    javaTranslator.specifyDslType(code,calleeDslType.carryDslType)
  }

  override def splitAt(calleeDslType: MonadDslType, callee: ValueExpression, index: ValueExpression): String = {
    s"""
       |${javaTranslator.visit( callee )}.splitAt(${javaTranslator.visit( index )})
       |""".stripMargin
  }

  override def add(calleeDslType: MonadDslType, callee: ValueExpression, index: ValueExpression, element: ValueExpression): String = {
    s"""
       |${javaTranslator.visit( callee )}.add(${javaTranslator.visit( index )},${javaTranslator.visit( element )})
       |""".stripMargin
  }

  override def add(calleeDslType: MonadDslType, callee: ValueExpression, element: ValueExpression): String = {
    s"""
       |${javaTranslator.visit( callee )}.add(${javaTranslator.visit( element )})
       |""".stripMargin
  }

  override def addAll(calleeDslType: MonadDslType, callee: ValueExpression, index: ValueExpression, element: ValueExpression): String = {
    s"""
       |${javaTranslator.visit( callee )}.addAll(${javaTranslator.visit( index )},${javaTranslator.visit( element )})
       |""".stripMargin
  }

  override def addAll(calleeDslType: MonadDslType, callee: ValueExpression, element: ValueExpression): String = {
    s"""
       |${javaTranslator.visit( callee )}.addAll(${javaTranslator.visit( element )})
       |""".stripMargin
  }

  override def remove(calleeDslType: MonadDslType, callee: ValueExpression, indexOrElement: ValueExpression): String = {
    s"""
       |${javaTranslator.visit( callee )}.remove(${javaTranslator.visit( indexOrElement )})
       |""".stripMargin
  }

  override def update(calleeDslType: MonadDslType, callee: ValueExpression, index: ValueExpression, element: ValueExpression): String = {
    s"""
       |${javaTranslator.visit( callee )}.set(${javaTranslator.visit( index )},${javaTranslator.visit( element )})
       |""".stripMargin
  }
}
