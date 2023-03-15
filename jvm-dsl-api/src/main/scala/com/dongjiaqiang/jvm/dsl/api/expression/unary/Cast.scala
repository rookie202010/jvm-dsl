package com.dongjiaqiang.jvm.dsl.api.expression.unary

import com.dongjiaqiang.jvm.dsl.api.`type`.DslType
import com.dongjiaqiang.jvm.dsl.api.exception.ExpressionParseException
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope

/**
 * <pre><code>
 * program{
 *      def method(Any j)=Unit{
 *          Foo i = (Foo)j; // (Foo)j => Cast
 *      }
 * }
 * <pre><code>
 */
case class Cast(child: ValueExpression, castType: DslType) extends UnaryExpression {
  override def toString: String = s"($castType)$child"

  override def equals(obj: Any): Boolean = obj match {
    case cast: Cast ⇒ cast.child == child && cast.castType == castType
    case _ ⇒ false
  }

  override def getValueType(programScope: ProgramScope): DslType = {
    val childType = super.getValueType( programScope )
    if (castType.isSuperDslType( programScope.importManager, childType )) {
      castType
    } else {
      throw ExpressionParseException( s"can not cast child expression to type: $castType child expression type: $childType " )
    }
  }
}
