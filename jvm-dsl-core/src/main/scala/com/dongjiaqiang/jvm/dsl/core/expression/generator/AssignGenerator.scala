package com.dongjiaqiang.jvm.dsl.core.expression.generator

import com.dongjiaqiang.jvm.dsl.api
import com.dongjiaqiang.jvm.dsl.api.expression.`var`.{Assign, VarRef}
import com.dongjiaqiang.jvm.dsl.api.expression.binary._
import com.dongjiaqiang.jvm.dsl.core.JvmDslParserParser._
import com.dongjiaqiang.jvm.dsl.core.parser.ExprContext


object AssignGenerator extends IExpressionGenerator[AssignmentContext, Assign,GeneratorContext] {

  private def generate(varRef: VarRef,
                       assignOperatorContext: AssignOperatorContext,
                       exprContext: ExprContext,
                       assignmentContext: AssignmentContext): Assign = {
    val expression = ExpressionGenerator.generate( exprContext, assignmentContext.expression( ) )

    if (assignOperatorContext.ASSIGN( ) != null) {
      Assign( varRef, expression )
    } else if (assignOperatorContext.OR_ASSIGN( ) != null) {
      api.expression.`var`.Assign( varRef, Or( varRef, expression ) )
    } else if (assignOperatorContext.AND_ASSIGN( ) != null) {
      api.expression.`var`.Assign( varRef, And( varRef, expression ) )
    } else if (assignOperatorContext.ADD_ASSIGN( ) != null) {
      api.expression.`var`.Assign( varRef, Add( varRef, expression ) )
    } else if (assignOperatorContext.DIV_ASSIGN( ) != null) {
      api.expression.`var`.Assign( varRef, Div( varRef, expression ) )
    } else if (assignOperatorContext.LSHIFT_ASSIGN( ) != null) {
      api.expression.`var`.Assign( varRef, LeftShift( varRef, expression ) )
    } else if (assignOperatorContext.MOD_ASSIGN( ) != null) {
      api.expression.`var`.Assign( varRef, Mod( varRef, expression ) )
    } else if (assignOperatorContext.MUL_ASSIGN( ) != null) {
      api.expression.`var`.Assign( varRef, Mul( varRef, expression ) )
    } else if (assignOperatorContext.RSHIFT_ASSIGN( ) != null) {
      api.expression.`var`.Assign( varRef, RightShift( varRef, expression ) )
    } else if (assignOperatorContext.SUB_ASSIGN( ) != null) {
      api.expression.`var`.Assign( varRef, Sub( varRef, expression ) )
    } else if (assignOperatorContext.URSHIFT_ASSIGN( ) != null) {
      api.expression.`var`.Assign( varRef, UnsignedRightShift( varRef, expression ) )
    } else if (assignOperatorContext.XOR_ASSIGN( ) != null) {
      api.expression.`var`.Assign( varRef, Caret( varRef, expression ) )
    } else {
      null
    }
  }

  override def generate(exprContext: ExprContext,
                        ruleContext: AssignmentContext,
                        generatorContext: GeneratorContext=NoneGeneratorContext): Assign = {
      val variable = VarRefGenerator.generate( exprContext, ruleContext.variable( ),VarGeneratorContext(true) )
      generate( variable, ruleContext.assignOperator( ), exprContext, ruleContext )
  }
}

