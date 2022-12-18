package com.dongjiaqiang.jvm.dsl.core.expression.generator

import com.dongjiaqiang.jvm.dsl.core.JvmDslParserParser.{AssignmentContext, VariableContext}
import com.dongjiaqiang.jvm.dsl.core.expression.{ArrayVarRef, Assign, VarRef}
import com.dongjiaqiang.jvm.dsl.core.scope.Scope

import scala.collection.convert.ImplicitConversions.`collection AsScalaIterable`

object VariableGenerator extends IExpressionGenerator[VariableContext,VarRef]{

  override def generate(expressionContext:ExpressionContext, ruleContext: VariableContext): VarRef = {
      val variable = ruleContext.IDENTIFIER( ).map( _.getText ).toList
      val optional = expressionContext.contextScope.resolveVarRefs(expressionContext.currentExpressionIndex,variable)
      VarRef(variable,optional.get)
  }
}
