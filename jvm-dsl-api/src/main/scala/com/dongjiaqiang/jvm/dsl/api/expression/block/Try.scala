package com.dongjiaqiang.jvm.dsl.api.expression.block

import com.dongjiaqiang.jvm.dsl.api.`type`.{DslType, TryType}
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope

/**
 * <pre><code>
 * program{
 * def method()=Unit{
 * Try[Int] maySuccess = Try{ // Try{ ... } => Try
 * return foo();
 * }
 * }
 * }<pre><code>
 */
case class Try(body: Block, dslType: TryType) extends ValueExpression {
  override def toString: String = s"Try $body"

  override def equals(obj: Any): Boolean = {
    obj match {
      case `try`: Try ⇒
        `try`.body == body && `try`.dslType == dslType
      case _ ⇒ false
    }
  }

  override def getValueType(programScope: ProgramScope): DslType = TryType( body.getValueType( programScope ) )
}
