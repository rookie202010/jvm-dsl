package com.dongjiaqiang.jvm.dsl.core.expression.visitor.`var`

import com.dongjiaqiang.jvm.dsl.api.expression.visitor.ExpressionVisitor
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.`var`.VarExpressionVisitor
import com.dongjiaqiang.jvm.dsl.api.expression._

trait VarExpressionScanner extends VarExpressionVisitor[Unit] {
  override def visit(localVarDef: LocalVarDef, visitor: ExpressionVisitor[Unit]): Unit = {
    localVarDef.assigned.foreach( visitor.visit )
  }

  override def visit(varRef: VarRef, visitor: ExpressionVisitor[Unit]): Unit = {

  }

  override def visit(arrayVarRef: ArrayVarRef, visitor: ExpressionVisitor[Unit]): Unit = {
    visitor.visit( arrayVarRef.indexExpression )
  }

  override def visit(mapVarRef: MapVarRef, visitor: ExpressionVisitor[Unit]): Unit = {
    visitor.visit( mapVarRef.keyExpression )
  }

  override def visit(lambda: Lambda, visitor: ExpressionVisitor[Unit]): Unit = {
    visitor.visit( lambda.body )
  }
}
