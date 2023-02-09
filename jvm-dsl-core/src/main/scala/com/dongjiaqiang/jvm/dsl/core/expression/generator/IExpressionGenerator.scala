package com.dongjiaqiang.jvm.dsl.core.expression.generator

import com.dongjiaqiang.jvm.dsl.api.expression.Expression
import com.dongjiaqiang.jvm.dsl.core.parser.ExprContext
import org.antlr.v4.runtime.RuleContext

trait GeneratorContext

object NoneGeneratorContext extends GeneratorContext

case class VarGeneratorContext(ignoreVarRefResolved:Boolean) extends GeneratorContext

trait IExpressionGenerator[T <: RuleContext, R <: Expression,G<:GeneratorContext] {
    def generate(exprContext: ExprContext,
                 ruleContext: T,generatorContext:G): R
}

