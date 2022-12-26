package com.dongjiaqiang.jvm.dsl.core.expression.visitor.block

import com.dongjiaqiang.jvm.dsl.core.expression._

/**
 * @author: rookie
 * @mail: dongjiaqiang@qiniu.com
 * @date: 2022/12/26 
 * */
trait BlockExpressionVisitor[T] {

  def visitBlock(block: Block): T

  def visitFor(forExpr: For): T

  def visitForCollection(forCollection: ForCollection): T

  def visitForMap(forMap: ForMap): T

  def visitWhile(whileExpr: While): T

  def visitDoWhile(doWhile: DoWhile): T

  def visitSync(sync: Sync): T

  def visitIf(ifExpr: If): T

  def visitTryCatch(tryCatch: TryCatch): T

  def visitAsync(asyncBlock: Async): T

  def visitTry(tryBlock: Try): T

  def visitMatchCase(matchCase: MatchCase): T

}
