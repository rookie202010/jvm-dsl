package com.dongjiaqiang.jvm.dsl.core.expression.generator

import com.dongjiaqiang.jvm.dsl.api
import com.dongjiaqiang.jvm.dsl.api.expression._
import com.dongjiaqiang.jvm.dsl.core.JvmDslParserParser._
import com.dongjiaqiang.jvm.dsl.core.parser.ExprContext


object AssignGenerator extends IExpressionGenerator[AssignmentContext, Assign,GeneratorContext] {

  def generate(varRef: VarRef,
               assignOperatorContext: AssignOperatorContext,
               exprContext: ExprContext,
               assignmentContext: AssignmentContext): Assign = {
    val expression = ExpressionGenerator.generate( exprContext, assignmentContext.expression( ) )

    if (assignOperatorContext.ASSIGN( ) != null) {
      api.expression.Assign( varRef, expression )
    } else if (assignOperatorContext.OR_ASSIGN( ) != null) {
      Assign( varRef, Or( varRef, expression ) )
    } else if (assignOperatorContext.AND_ASSIGN( ) != null) {
      Assign( varRef, And( varRef, expression ) )
    } else if (assignOperatorContext.ADD_ASSIGN( ) != null) {
      Assign( varRef, Add( varRef, expression ) )
    } else if (assignOperatorContext.DIV_ASSIGN( ) != null) {
      Assign( varRef, Div( varRef, expression ) )
    } else if (assignOperatorContext.LSHIFT_ASSIGN( ) != null) {
      Assign( varRef, LeftShift( varRef, expression ) )
    } else if (assignOperatorContext.MOD_ASSIGN( ) != null) {
      Assign( varRef, Mod( varRef, expression ) )
    } else if (assignOperatorContext.MUL_ASSIGN( ) != null) {
      Assign( varRef, Mul( varRef, expression ) )
    } else if (assignOperatorContext.RSHIFT_ASSIGN( ) != null) {
      Assign( varRef, RightShift( varRef, expression ) )
    } else if (assignOperatorContext.SUB_ASSIGN( ) != null) {
      Assign( varRef, Sub( varRef, expression ) )
    } else if (assignOperatorContext.URSHIFT_ASSIGN( ) != null) {
      Assign( varRef, UnsignedRightShift( varRef, expression ) )
    } else if (assignOperatorContext.XOR_ASSIGN( ) != null) {
      Assign( varRef, Caret( varRef, expression ) )
    } else {
      null
    }
  }

  override def generate(exprContext: ExprContext,
                        ruleContext: AssignmentContext,
                        generatorContext: GeneratorContext=NoneGeneratorContext): Assign = {
    if (ruleContext.variable( ) != null) {

      val variable = VarGenerator.generate( exprContext, ruleContext.variable( ),VarGeneratorContext(true) )
      generate( variable, ruleContext.assignOperator( ), exprContext, ruleContext )

    } else if (ruleContext.arrayVariable( ) != null) {
      val variable = VarGenerator.generate( exprContext, ruleContext.arrayVariable( ).variable( ) )
      val indexExpr = OrGenerator.generate( exprContext, ruleContext.arrayVariable( ).conditionalOrExpression( ) )

      generate( new ArrayVarRef( indexExpr, variable.name,  variable.fieldScope ),
        ruleContext.assignOperator( ), exprContext, ruleContext )

    } else {
      val variable = VarGenerator.generate( exprContext, ruleContext.mapVariable( ).variable( ) )
      val indexExpr = OrGenerator.generate( exprContext, ruleContext.mapVariable( ).conditionalOrExpression( ) )

      generate( new MapVarRef( indexExpr, variable.name,  variable.fieldScope ), ruleContext.assignOperator( ),
        exprContext, ruleContext )
    }
  }
}

