package com.dongjiaqiang.jvm.dsl.core.expression.visitor.`var`

import com.dongjiaqiang.jvm.dsl.core.expression.{ArrayVarRef, Expression, Lambda, LocalVarDef, MapVarRef, VarRef}

/**
 * @author: rookie
 * @mail: dongjiaqiang@qiniu.com
 * @date: 2022/12/26 
 * */
trait VarExpressionReviser extends VarExpressionVisitor[Expression]{
  override def visitLocalVarDef(localVarDef: LocalVarDef): Expression = localVarDef

  override def visitVarRef(varRef: VarRef): Expression = varRef

  override def visitArrayVarRef(arrayVarRef: ArrayVarRef): Expression = arrayVarRef

  override def visitMapVarRef(mapVarRef: MapVarRef): Expression = mapVarRef

  override def visitLambda(lambda: Lambda): Expression = lambda
}
