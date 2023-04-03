package com.dongjiaqiang.jvm.dsl.api.expression.block

import com.dongjiaqiang.jvm.dsl.api.`type`.{DslType, FutureType}
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.scope.{FieldScope, ProgramScope}

/**
 * <pre><code>
 * program{
 *      def method()=Unit{
 *            Future[Int] future = Async{ // Async{ ... } => Async
 *                return foo();
 *        }
 * }<pre><code>
 */
case class Async(body: Block, executor: Option[FieldScope], dslType: FutureType)
  extends ValueExpression {
  override def toString: String = {
    executor match {
      case Some( fieldScope ) ⇒
        s"Async(${fieldScope.symbolName})$body"
      case None ⇒
        s"Async $body"
    }
  }

  override def equals(obj: Any): Boolean = {
    obj match {
      case async: Async =>
        async.body == body && async.executor == executor && async.dslType == dslType
      case _ => false
    }
  }

  override def getValueType(programScope: ProgramScope): FutureType = FutureType( body.getValueType( programScope ) )
}
