package com.dongjiaqiang.jvm.dsl.core.expression.visitor.block

import com.dongjiaqiang.jvm.dsl.core.expression.{Async, Block, DoWhile, Expression, For, ForCollection, ForMap, If, Lambda, MatchCase, Sync, Try, TryCatch, While}

/**
 * @author: rookie
 * @mail: dongjiaqiang@qiniu.com
 * @date: 2022/12/26 
 * */
trait BlockExpressionReviser extends BlockExpressionVisitor[Expression]{
  override def visitBlock(block: Block): Expression = block

  override def visitFor(forExpr: For): Expression = forExpr

  override def visitForCollection(forCollection: ForCollection): Expression = forCollection

  override def visitForMap(forMap: ForMap): Expression = forMap

  override def visitWhile(whileExpr: While): Expression = whileExpr

  override def visitDoWhile(doWhile: DoWhile): Expression = doWhile

  override def visitSync(sync: Sync): Expression = sync

  override def visitIf(ifExpr: If): Expression = ifExpr

  override def visitTryCatch(tryCatch: TryCatch): Expression = tryCatch

  override def visitAsync(asyncBlock: Async): Expression = asyncBlock

  override def visitTry(tryBlock: Try): Expression = tryBlock

  override def visitMatchCase(matchCase: MatchCase): Expression = matchCase
}
