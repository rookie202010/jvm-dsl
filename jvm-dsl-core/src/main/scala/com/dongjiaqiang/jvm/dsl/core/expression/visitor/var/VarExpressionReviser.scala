package com.dongjiaqiang.jvm.dsl.core.expression.visitor.`var`

import com.dongjiaqiang.jvm.dsl.core.expression._

trait VarExpressionReviser extends VarExpressionVisitor[Expression]{
  override def visitLocalVarDef(localVarDef: LocalVarDef): Expression = localVarDef

  override def visitVarRef(varRef: VarRef): Expression = varRef

  override def visitArrayVarRef(arrayVarRef: ArrayVarRef): Expression = arrayVarRef

  override def visitMapVarRef(mapVarRef: MapVarRef): Expression = mapVarRef

  override def visitLambda(lambda: Lambda): Expression = lambda
}
