package com.dongjiaqiang.jvm.dsl.core.expression.visitor.statement

import com.dongjiaqiang.jvm.dsl.core.expression._


trait StatementExpressionReviser extends StatementExpressionVisitor[Expression]{
  override def visitAssign(assign: Assign): Expression = assign

  override def visitBreak(break: Break.type): Expression = break

  override def visitContinue(continue: Continue.type): Expression = continue

  override def visitThrow(throwExpr: Throw): Expression = throwExpr

  override def visitReturn(returnExpr: Return): Expression = returnExpr

  override def visitAssert(assert: Assert): Expression = assert
}
