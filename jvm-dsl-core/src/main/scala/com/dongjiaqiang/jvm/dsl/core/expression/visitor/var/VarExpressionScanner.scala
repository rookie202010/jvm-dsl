package com.dongjiaqiang.jvm.dsl.core.expression.visitor.`var`
import com.dongjiaqiang.jvm.dsl.core.expression.{ArrayVarRef, Lambda, LocalVarDef, MapVarRef, VarRef}

/**
 * @author: rookie
 * @mail: dongjiaqiang@qiniu.com
 * @date: 2022/12/26 
 * */
trait VarExpressionScanner extends VarExpressionVisitor[Unit]{
  override def visitLocalVarDef(localVarDef: LocalVarDef): Unit = {}

  override def visitVarRef(varRef: VarRef): Unit = {}

  override def visitArrayVarRef(arrayVarRef: ArrayVarRef): Unit = {}

  override def visitMapVarRef(mapVarRef: MapVarRef): Unit = {}

  override def visitLambda(lambda: Lambda): Unit = {}
}
