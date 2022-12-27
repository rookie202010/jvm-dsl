package com.dongjiaqiang.jvm.dsl.core.expression.visitor.block

import com.dongjiaqiang.jvm.dsl.core.expression.visitor.ExpressionVisitor
import com.dongjiaqiang.jvm.dsl.core.expression._

trait BlockExpressionReviser extends BlockExpressionVisitor[Expression] {
  override def visit(block: Block,
                     visitor: ExpressionVisitor[Expression]): Expression = {
    val newExpressions = block.expressions.map( visitor.visit )
    if (newExpressions.zip( block.expressions ).exists {
      case (e1, e2) ⇒ e1 != e2
    }) {
      new Block( newExpressions )
    } else {
      block
    }
  }

  override def visit(forExpr: For,
                     visitor: ExpressionVisitor[Expression]): Expression = {
    val newLoopVarDef = visitor.visit( forExpr.loopVarDef ).asInstanceOf[LocalVarDef]
    val newLoopVarCondition = visitor.visit( forExpr.loopVarCondition )
    val newLoopVarUpdate = visitor.visit( forExpr.loopVarUpdate )
    val newBlock = visit( forExpr.body, visitor )
    if (newLoopVarDef != forExpr.loopVarDef ||
      newLoopVarCondition != forExpr.loopVarCondition ||
      newLoopVarUpdate != forExpr.loopVarUpdate) {
      new For( newLoopVarDef, newLoopVarCondition, newLoopVarUpdate, newBlock.asInstanceOf[Block] )
    } else {
      forExpr
    }
  }

  override def visit(forCollection: ForCollection,
                     visitor: ExpressionVisitor[Expression]): Expression = {
    val newLocalVarDef = visitor.visit( forCollection.localVarDef ).asInstanceOf[LocalVarDef]
    val newLooped = visitor.visit( forCollection.looped )
  }

  override def visit(forMap: ForMap): Expression = forMap

  override def visit(whileExpr: While): Expression = whileExpr

  override def visit(doWhile: DoWhile): Expression = doWhile

  override def visit(sync: Sync): Expression = sync

  override def visit(ifExpr: If): Expression = ifExpr

  override def visit(tryCatch: TryCatch): Expression = tryCatch

  override def visit(asyncBlock: Async): Expression = asyncBlock

  override def visit(tryBlock: Try): Expression = tryBlock

  override def visit(matchCase: MatchCase): Expression = matchCase
}
