package com.dongjiaqiang.jvm.dsl.core.expression.generator

import com.dongjiaqiang.jvm.dsl.api.expression
import com.dongjiaqiang.jvm.dsl.api.expression.VarRef
import com.dongjiaqiang.jvm.dsl.core.JvmDslParserParser.VariableContext
import com.dongjiaqiang.jvm.dsl.core.exception.ExpressionParserException
import com.dongjiaqiang.jvm.dsl.core.parser.ExprContext

import scala.collection.convert.ImplicitConversionsToScala._

object VarGenerator extends IExpressionGenerator[VariableContext, VarRef] {
  override def generate(exprContext: ExprContext, ruleContext: VariableContext): VarRef = {
    val variable = ruleContext.IDENTIFIER( ).map( _.getText ).toList
    exprContext.getContextScope.resolveVarRefs( exprContext.getCurrentExpressionIndex, variable ) match {
      case Some( fieldScope ) ⇒ expression.VarRef( variable, fieldScope.dslType, fieldScope )
      case None ⇒ throw ExpressionParserException( variable.mkString( "," ) )
    }

  }
}
