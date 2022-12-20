package com.dongjiaqiang.jvm.dsl.core.expression.generator

import com.dongjiaqiang.jvm.dsl.core.JvmDslParserParser._
import com.dongjiaqiang.jvm.dsl.core.expression._


object AssignGenerator extends IExpressionGenerator[AssignmentContext,Assign] {
  override def generate(expressionContext: ExpressionContext, ruleContext: AssignmentContext): Assign = {
    if (ruleContext.variable( ) != null) {
      val variable = VariableGenerator.generate( expressionContext, ruleContext.variable( ) )
      new Assign( variable, ExpressionGenerator.generate( expressionContext, ruleContext.expression( ) ) )
    } else if (ruleContext.arrayVariable( ) != null) {
      val variable = VariableGenerator.generate( expressionContext, ruleContext.arrayVariable( ).variable( ) )
      val indexExpr = ExpressionGenerator.generate( expressionContext, ruleContext.arrayVariable( ).expression( ) )
      new Assign( ArrayVarRef( indexExpr, variable.name, variable.fieldScope ),
        ExpressionGenerator.generate( expressionContext, ruleContext.expression( ) ) )
    } else {
      val variable = VariableGenerator.generate( expressionContext, ruleContext.mapVariable( ).variable( ) )
      val indexExpr = ExpressionGenerator.generate( expressionContext, ruleContext.mapVariable( ).expression( ) )
      new Assign( ArrayVarRef( indexExpr, variable.name, variable.fieldScope ),
        ExpressionGenerator.generate( expressionContext, ruleContext.expression( ) ) )
    }
  }
}

