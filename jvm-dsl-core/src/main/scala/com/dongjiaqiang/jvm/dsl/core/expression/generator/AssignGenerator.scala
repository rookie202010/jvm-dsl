package com.dongjiaqiang.jvm.dsl.core.expression.generator

import com.dongjiaqiang.jvm.dsl.core.JvmDslParserParser._
import com.dongjiaqiang.jvm.dsl.core.expression._
import com.dongjiaqiang.jvm.dsl.core.parser.ExprContext


object AssignGenerator extends IExpressionGenerator[AssignmentContext,Assign] {
  override def generate(exprContext: ExprContext, ruleContext: AssignmentContext): Assign = {
    if (ruleContext.variable( ) != null) {
      val variable = VarGenerator.generate( exprContext, ruleContext.variable( ) )
      new Assign( variable, ExpressionGenerator.generate( exprContext, ruleContext.expression( ) ) )
    } else if (ruleContext.arrayVariable( ) != null) {
      val variable = VarGenerator.generate( exprContext, ruleContext.arrayVariable( ).variable( ) )
      val indexExpr = OrGenerator.generate( exprContext, ruleContext.arrayVariable( ).conditionalOrExpression( ) )
      new Assign( ArrayVarRef( indexExpr, variable.name, variable.fieldScope ),
        ExpressionGenerator.generate( exprContext, ruleContext.expression( ) ) )
    } else {
      val variable = VarGenerator.generate( exprContext, ruleContext.mapVariable( ).variable( ) )
      val indexExpr = OrGenerator.generate( exprContext, ruleContext.mapVariable( ).conditionalOrExpression( ) )
      new Assign( MapVarRef( indexExpr, variable.name, variable.fieldScope ),
        ExpressionGenerator.generate( exprContext, ruleContext.expression( ) ) )
    }
  }
}

