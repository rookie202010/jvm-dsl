package com.dongjiaqiang.jvm.dsl.api.expression.block

import com.dongjiaqiang.jvm.dsl.api.`type`.{DslType, UnitType}
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.scope.{FieldScope, ProgramScope}

/**
 * <pre><code>
 * program{
 * def method()=Unit{
 * String body = "body";
 * Int len = body.length;
 * Json maySuccess = Json{ // Json{ ... } => Json
 * {"body":body,"len":len}
 * }
 *
 * Json data = Json{
 * [1,2,3]
 * }
 * }
 * }<pre><code>
 */
case class CustomBlockExpression(name: String, body: Block, param: Option[FieldScope]) extends ValueExpression {
  override def toString: String = s"$name $body"

  override def equals(obj: Any): Boolean = {
    obj match {
      case customBlockExpression: CustomBlockExpression ⇒
        customBlockExpression.name == name &&
          customBlockExpression.body == body &&
          customBlockExpression.param == param
      case _ ⇒ false
    }
  }

  override def getValueType(programScope: ProgramScope): DslType = programScope.customExprTypeResolver.get( name ) match {
    case Some( value ) ⇒ value.apply(this)
    case None ⇒ UnitType
  }
}
