package com.dongjiaqiang.jvm.dsl.core.expression.visitor.statement

import com.dongjiaqiang.jvm.dsl.core.expression._

/**
 * @author: rookie
 * @mail: dongjiaqiang@qiniu.com
 * @date: 2022/12/26 
 * */
trait StatementExpressionVisitor[T] {

  def visitAssign(assign: Assign): T

  def visitBreak(break: Break.type): T

  def visitContinue(continue: Continue.type): T

  def visitThrow(throwExpr: Throw): T

  def visitReturn(returnExpr: Return): T

  def visitAssert(assert: Assert): T

}
