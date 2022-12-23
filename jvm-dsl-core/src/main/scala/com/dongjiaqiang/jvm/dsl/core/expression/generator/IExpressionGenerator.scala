package com.dongjiaqiang.jvm.dsl.core.expression.generator

import com.dongjiaqiang.jvm.dsl.core.expression.Expression
import com.dongjiaqiang.jvm.dsl.core.parser.ExprContext
import org.antlr.v4.runtime.RuleContext

trait IExpressionGenerator[T <: RuleContext, R <: Expression] {
    def generate(exprContext: ExprContext,
                 ruleContext: T): R
}

