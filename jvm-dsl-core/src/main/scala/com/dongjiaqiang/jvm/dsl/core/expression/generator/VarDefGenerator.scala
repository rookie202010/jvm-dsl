package com.dongjiaqiang.jvm.dsl.core.expression.generator

import com.dongjiaqiang.jvm.dsl.api.expression
import com.dongjiaqiang.jvm.dsl.api.expression.LocalVarDef
import com.dongjiaqiang.jvm.dsl.core.JvmDslParserParser.VarDefContext
import com.dongjiaqiang.jvm.dsl.core.parser.ExprContext

object VarDefGenerator extends IExpressionGenerator[VarDefContext, LocalVarDef] {

  override def generate(exprContext: ExprContext, ruleContext: VarDefContext): LocalVarDef = {
    val fieldScope = exprContext.getContextScope.fields( ruleContext.parameter( ).localVariable( ).getText )
    val assigned = Option.apply( ruleContext.expression( ) ).map( c ⇒ ExpressionGenerator.generate( exprContext, c ) )
    expression.LocalVarDef( fieldScope, fieldScope.dslType, assigned )
  }
}
