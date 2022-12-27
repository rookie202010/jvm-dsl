package com.dongjiaqiang.jvm.dsl.core.expression.visitor.`var`

import com.dongjiaqiang.jvm.dsl.core.expression._


trait VarExpressionVisitor[T] {

  def visitLocalVarDef(localVarDef: LocalVarDef): T

  def visitVarRef(varRef: VarRef): T

  def visitArrayVarRef(arrayVarRef: ArrayVarRef): T

  def visitMapVarRef(mapVarRef: MapVarRef): T

  def visitLambda(lambda: Lambda): T
}
