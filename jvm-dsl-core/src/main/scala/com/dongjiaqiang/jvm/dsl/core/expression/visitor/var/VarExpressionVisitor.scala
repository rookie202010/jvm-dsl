package com.dongjiaqiang.jvm.dsl.core.expression.visitor.`var`

import com.dongjiaqiang.jvm.dsl.core.expression._

var

/**
 * @author: rookie
 * @mail: dongjiaqiang@qiniu.com
 * @date: 2022/12/26 
 * */
trait VarExpressionVisitor[T] {

  def visitLocalVarDef(localVarDef: LocalVarDef): T

  def visitVarRef(varRef: VarRef): T

  def visitArrayVarRef(arrayVarRef: ArrayVarRef): T

  def visitMapVarRef(mapVarRef: MapVarRef): T

  def visitLambda(lambda: Lambda): T
}
