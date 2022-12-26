package com.dongjiaqiang.jvm.dsl.core.expression.generator

import com.dongjiaqiang.jvm.dsl.core.JvmDslParserParser._
import com.dongjiaqiang.jvm.dsl.core.expression._
import com.dongjiaqiang.jvm.dsl.core.parser.ExprContext


object AssignGenerator extends IExpressionGenerator[AssignmentContext,Assign] {

  def generate(varRef: VarRef,
               assignOperatorContext: AssignOperatorContext,
               exprContext: ExprContext,
               assignmentContext: AssignmentContext): Assign = {
    val expression = ExpressionGenerator.generate( exprContext, assignmentContext.expression( ) )

    if (assignOperatorContext.ASSIGN( ) != null) {
      new Assign( varRef, expression )
    } else if (assignOperatorContext.OR_ASSIGN( ) != null) {
      new Assign( varRef, new Or( varRef, expression ) )
    } else if (assignOperatorContext.AND_ASSIGN( ) != null) {
      new Assign( varRef, new And( varRef, expression ) )
    } else if (assignOperatorContext.ADD_ASSIGN( ) != null) {
      new Assign( varRef, new Add( varRef, expression ) )
    } else if (assignOperatorContext.DIV_ASSIGN( ) != null) {
      new Assign( varRef, new Div( varRef, expression ) )
    } else if (assignOperatorContext.LSHIFT_ASSIGN( ) != null) {
      new Assign( varRef, new LeftShift( varRef, expression ) )
    } else if (assignOperatorContext.MOD_ASSIGN( ) != null) {
      new Assign( varRef, new Mod( varRef, expression ) )
    } else if (assignOperatorContext.MUL_ASSIGN( ) != null) {
      new Assign( varRef, new Mul( varRef, expression ) )
    } else if (assignOperatorContext.RSHIFT_ASSIGN( ) != null) {
      new Assign( varRef, new RightShift( varRef, expression ) )
    } else if (assignOperatorContext.SUB_ASSIGN( ) != null) {
      new Assign( varRef, new Sub( varRef, expression ) )
    } else if (assignOperatorContext.URSHIFT_ASSIGN( ) != null) {
      new Assign( varRef, new UnsignedRightShift( varRef, expression ) )
    } else if (assignOperatorContext.XOR_ASSIGN( ) != null) {
      new Assign( varRef, new Caret( varRef, expression ) )
    } else {
      null
    }
  }

  override def generate(exprContext: ExprContext, ruleContext: AssignmentContext): Assign = {
    if (ruleContext.variable( ) != null) {

      val variable = VarGenerator.generate( exprContext, ruleContext.variable( ) )
      generate( variable, ruleContext.assignOperator( ), exprContext, ruleContext )

    } else if (ruleContext.arrayVariable( ) != null) {
      val variable = VarGenerator.generate( exprContext, ruleContext.arrayVariable( ).variable( ) )
      val indexExpr = OrGenerator.generate( exprContext, ruleContext.arrayVariable( ).conditionalOrExpression( ) )

      generate( new ArrayVarRef( indexExpr, variable.name, variable.fieldScope ),
        ruleContext.assignOperator( ), exprContext, ruleContext )

    } else {
      val variable = VarGenerator.generate( exprContext, ruleContext.mapVariable( ).variable( ) )
      val indexExpr = OrGenerator.generate( exprContext, ruleContext.mapVariable( ).conditionalOrExpression( ) )

      generate( new MapVarRef( indexExpr, variable.name, variable.fieldScope ), ruleContext.assignOperator( ),
        exprContext, ruleContext )
    }
  }
}

