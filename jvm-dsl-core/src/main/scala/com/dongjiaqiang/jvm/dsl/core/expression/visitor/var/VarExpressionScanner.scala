package com.dongjiaqiang.jvm.dsl.core.expression.visitor.`var`
import com.dongjiaqiang.jvm.dsl.core.expression._

trait VarExpressionScanner extends VarExpressionVisitor[Unit]{
  override def visitLocalVarDef(localVarDef: LocalVarDef): Unit = {}

  override def visitVarRef(varRef: VarRef): Unit = {}

  override def visitArrayVarRef(arrayVarRef: ArrayVarRef): Unit = {}

  override def visitMapVarRef(mapVarRef: MapVarRef): Unit = {}

  override def visitLambda(lambda: Lambda): Unit = {}
}
