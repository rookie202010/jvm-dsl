package com.dongjiaqiang.jvm.dsl.api.expression.unary

import com.dongjiaqiang.jvm.dsl.api.`type`._
import com.dongjiaqiang.jvm.dsl.api.exception.ExpressionParseException
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope

/**
 * <pre><code>
 * program{
 *      def method(Long j)=Unit{
 *        Long i = (-j)*2; // (-j) => Opposite
 *      }
 * }
 * <pre><code>
 */
case class Opposite(child: ValueExpression) extends UnaryExpression {
  override def toString: String = s"(-$child)"

  override def equals(obj: Any): Boolean = obj match {
    case opposite: Opposite ⇒ opposite.child == child
    case _ ⇒ false
  }

  override def getValueType(programScope: ProgramScope): DslType = {
    super.getValueType( programScope: ProgramScope ) match {
      case IntType ⇒ IntType
      case LongType ⇒ LongType
      case FloatType ⇒ FloatType
      case DoubleType ⇒ DoubleType
      case _ ⇒
        throw ExpressionParseException( "opposite expression accept only int long float and double type" )
    }
  }
}
