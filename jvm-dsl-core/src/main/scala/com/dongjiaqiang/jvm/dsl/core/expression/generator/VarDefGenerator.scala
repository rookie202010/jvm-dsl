package com.dongjiaqiang.jvm.dsl.core.expression.generator

import com.dongjiaqiang.jvm.dsl.api.expression.`var`
import com.dongjiaqiang.jvm.dsl.api.expression.`var`.LocalVarDef
import com.dongjiaqiang.jvm.dsl.core.JvmDslParserParser.VarDefContext
import com.dongjiaqiang.jvm.dsl.core.parser.ExprContext

object VarDefGenerator extends IExpressionGenerator[VarDefContext, LocalVarDef,GeneratorContext] {

  override def generate(exprContext: ExprContext,
                        ruleContext: VarDefContext,
                        generatorContext: GeneratorContext = NoneGeneratorContext): LocalVarDef = {
    val fieldScope = exprContext.getContextScope.fields( ruleContext.parameter( ).localVariable( ).getText )
    val assigned = Option.apply( ruleContext.expression( ) ).map( c ⇒ ExpressionGenerator.generate( exprContext, c ) )
    `var`.LocalVarDef( fieldScope, fieldScope.dslType, assigned )
  }
}
