package com.dongjiaqiang.jvm.dsl.java.core.translate.method

import com.dongjiaqiang.jvm.dsl.api.`type`.MonadDslType
import com.dongjiaqiang.jvm.dsl.api.`type`.visitor.MonadMethodVisitor
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression

trait MonadTranslator extends MonadMethodVisitor[String]{
  override def map(callee: ValueExpression, param: ValueExpression): String = {
      val monadDslType = callee.getValueType(programScope).asInstanceOf[MonadDslType]

  }

  override def flatten(callee: ValueExpression): String = ???

  override def flatMap(callee: ValueExpression, param: ValueExpression): String = ???

  override def filter(callee: ValueExpression, param: ValueExpression): String = ???

  override def filterNot(callee: ValueExpression, param: ValueExpression): String = ???

  override def foreach(callee: ValueExpression, param: ValueExpression): String = ???

  override def exist(callee: ValueExpression, param: ValueExpression): String = ???

  override def find(callee: ValueExpression, param: ValueExpression): String = ???

  override def toList(callee: ValueExpression): String = ???

  override def toSet(callee: ValueExpression): String = ???

  override def toMap(callee: ValueExpression): String = ???

  override def contains(callee: ValueExpression, param: ValueExpression): String = ???

  override def zipWithIndex(callee: ValueExpression): String = ???

  override def reduce(callee: ValueExpression, param: ValueExpression): String = ???

  override def reduceOption(callee: ValueExpression, param: ValueExpression): String = ???

  override def isEmpty(callee: ValueExpression): String = ???

  override def nonEmpty(callee: ValueExpression): String = ???

  override def length(callee: ValueExpression): String = ???
}
