package com.dongjiaqiang.jvm.dsl.core.expression.visitor.statement
import com.dongjiaqiang.jvm.dsl.core.expression.{Assert, Assign, Break, Continue, Return, Throw}

/**
 * @author: rookie
 * @mail: dongjiaqiang@qiniu.com
 * @date: 2022/12/26 
 * */
trait StatementExpressionScanner extends StatementExpressionVisitor[Unit]{
  override def visitAssign(assign: Assign): Unit = {}

  override def visitBreak(break: Break.type): Unit = {}

  override def visitContinue(continue: Continue.type): Unit = {}

  override def visitThrow(throwExpr: Throw): Unit = {}

  override def visitReturn(returnExpr: Return): Unit = {}

  override def visitAssert(assert: Assert): Unit = {}
}
