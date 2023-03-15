package com.dongjiaqiang.jvm.dsl.api.expression.unary

import com.dongjiaqiang.jvm.dsl.api.`type`.{BoolType, DslType}
import com.dongjiaqiang.jvm.dsl.api.exception.ExpressionParseException
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope

/**
 * <pre><code>
 * program{
 *      def method(Bool j)=Unit{
 *          Bool i = !j; // !j => Negate
 *      }
 * }
 * <pre><code>
 */
case class Negate(child: ValueExpression) extends UnaryExpression {
  override def toString: String = s"!$child"

  override def equals(obj: Any): Boolean = obj match {
    case negate: Negate ⇒ negate.child == child
    case _ ⇒ false
  }

  override def getValueType(programScope: ProgramScope): DslType = {
    super.getValueType( programScope ) match {
      case BoolType ⇒ BoolType
      case _ ⇒
        throw ExpressionParseException( "negate expression accept only bool type" )
    }
  }
}
