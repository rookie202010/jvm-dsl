package com.dongjiaqiang.jvm.dsl.core.expression.visitor.block
import com.dongjiaqiang.jvm.dsl.core.expression.{Async, Block, DoWhile, For, ForCollection, ForMap, If, Lambda, MatchCase, Sync, Try, TryCatch, While}

/**
 * @author: rookie
 * @mail: dongjiaqiang@qiniu.com
 * @date: 2022/12/26 
 * */
trait BlockExpressionScanner extends BlockExpressionVisitor[Unit]{

  override def visitBlock(block: Block): Unit = {}

  override def visitFor(forExpr: For): Unit = {}

  override def visitForCollection(forCollection: ForCollection): Unit =  {}

  override def visitForMap(forMap: ForMap): Unit =  {}

  override def visitWhile(whileExpr: While): Unit =  {}

  override def visitDoWhile(doWhile: DoWhile): Unit =  {}

  override def visitSync(sync: Sync): Unit =  {}

  override def visitIf(ifExpr: If): Unit =  {}

  override def visitTryCatch(tryCatch: TryCatch): Unit =  {}

  override def visitAsync(asyncBlock: Async): Unit =  {}

  override def visitTry(tryBlock: Try): Unit =  {}

  override def visitMatchCase(matchCase: MatchCase): Unit =  {}
}
