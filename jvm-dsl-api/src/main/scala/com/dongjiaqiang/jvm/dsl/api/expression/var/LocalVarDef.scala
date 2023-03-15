package com.dongjiaqiang.jvm.dsl.api.expression.`var`

import com.dongjiaqiang.jvm.dsl.api.`type`.DslType
import com.dongjiaqiang.jvm.dsl.api.expression.{Expression, ValueExpression}
import com.dongjiaqiang.jvm.dsl.api.scope.FieldScope


/**
 * <p>local var def should appear within a block expression including within a method body within a lambda expression</p>
 *
 *
 * <pre><code>
 * program{
 *      def method()=Unit{
 *          Int i = foo()*10; //LocalVarDef
 *          Long j; //LocalVarDef
 *      }
 * }
 * <pre><code>
 */
case class LocalVarDef(fieldScope: FieldScope, dslType: DslType, assigned: Option[ValueExpression]) extends Expression {
  override def toString: String = assigned match {
    case Some( v ) ⇒ s"${fieldScope.dslType} ${fieldScope.symbolName} = $v"
    case None ⇒ s"${fieldScope.dslType} ${fieldScope.symbolName}"
  }

  override def equals(obj: Any): Boolean = {
    obj match {
      case localVarDef: LocalVarDef ⇒
        localVarDef.fieldScope == fieldScope &&
          localVarDef.dslType == dslType &&
          localVarDef.assigned == assigned
      case _ ⇒ false
    }
  }
}
