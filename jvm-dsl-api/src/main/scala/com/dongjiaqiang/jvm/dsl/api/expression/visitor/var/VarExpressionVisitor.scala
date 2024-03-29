package com.dongjiaqiang.jvm.dsl.api.expression.visitor.`var`

import com.dongjiaqiang.jvm.dsl.api.expression.`var`.{LocalVarDef, VarRef}
import com.dongjiaqiang.jvm.dsl.api.expression.block.Lambda
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.ExpressionVisitor


trait VarExpressionVisitor[T] {

  def visit(localVarDef: LocalVarDef, visitor: ExpressionVisitor[T]): T = {
    throw new UnsupportedOperationException( "not supported" )
  }

  def visit(varRef: VarRef, visitor: ExpressionVisitor[T]): T = {
    throw new UnsupportedOperationException( "not supported" )
  }

  def visit(lambda: Lambda, visitor: ExpressionVisitor[T]): T = {
    throw new UnsupportedOperationException( "not supported" )
  }
}
