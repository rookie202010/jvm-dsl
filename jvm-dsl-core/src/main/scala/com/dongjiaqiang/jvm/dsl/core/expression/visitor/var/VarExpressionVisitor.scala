package com.dongjiaqiang.jvm.dsl.core.expression.visitor.`var`

import com.dongjiaqiang.jvm.dsl.core.expression._
import com.dongjiaqiang.jvm.dsl.core.expression.visitor.ExpressionVisitor


trait VarExpressionVisitor[T] {

  def visit(localVarDef: LocalVarDef, visitor: ExpressionVisitor[T]): T={
    throw new UnsupportedOperationException("not supported")
  }

  def visit(varRef: VarRef, visitor: ExpressionVisitor[T]): T={
    throw new UnsupportedOperationException("not supported")
  }

  def visit(arrayVarRef: ArrayVarRef, visitor: ExpressionVisitor[T]): T={
    throw new UnsupportedOperationException("not supported")
  }

  def visit(mapVarRef: MapVarRef, visitor: ExpressionVisitor[T]): T={
    throw new UnsupportedOperationException("not supported")
  }

  def visit(lambda: Lambda, visitor: ExpressionVisitor[T]): T={
    throw new UnsupportedOperationException("not supported")
  }
}
