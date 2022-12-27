package com.dongjiaqiang.jvm.dsl.core.expression.visitor.`var`
import com.dongjiaqiang.jvm.dsl.core.expression._

trait VarExpressionScanner extends VarExpressionVisitor[Unit]{
  override def visit(localVarDef: LocalVarDef): Unit = {}

  override def visit(varRef: VarRef): Unit = {}

  override def visit(arrayVarRef: ArrayVarRef): Unit = {}

  override def visit(mapVarRef: MapVarRef): Unit = {}

  override def visit(lambda: Lambda): Unit = {}
}
