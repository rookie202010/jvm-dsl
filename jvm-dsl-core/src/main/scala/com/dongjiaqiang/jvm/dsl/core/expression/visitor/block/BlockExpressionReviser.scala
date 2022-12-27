package com.dongjiaqiang.jvm.dsl.core.expression.visitor.block

import com.dongjiaqiang.jvm.dsl.core.expression.visitor.{ExpressionReviser, ExpressionVisitor}
import com.dongjiaqiang.jvm.dsl.core.expression._

trait BlockExpressionReviser extends BlockExpressionVisitor[Expression] {
  override def visit(block: Block,
                     visitor: ExpressionVisitor[Expression]): Expression = {
    val expressions = ExpressionReviser.revise[Expression,Expression](block.expressions.toArray,visitor)
    if (expressions.isDefined) {
      val newBlock = new Block()
      expressions.get.foreach(e⇒newBlock.expressions.append(e))
      newBlock
    } else {
      block
    }
  }

  override def visit(forExpr: For,
                     visitor: ExpressionVisitor[Expression]): Expression = {
    val newLoopVarDef = visitor.visit( forExpr.loopVarDef ).asInstanceOf[LocalVarDef]
    val newLoopVarCondition = visitor.visit( forExpr.loopVarCondition )
    val newLoopVarUpdate = visitor.visit( forExpr.loopVarUpdate )
    val newBody = visit( forExpr.body, visitor )
    if (newLoopVarDef != forExpr.loopVarDef ||
      newLoopVarCondition != forExpr.loopVarCondition ||
      newLoopVarUpdate != forExpr.loopVarUpdate) {
      For( newLoopVarDef, newLoopVarCondition, newLoopVarUpdate, newBody.asInstanceOf[Block] )
    } else {
      forExpr
    }
  }

  override def visit(forCollection: ForCollection,
                     visitor: ExpressionVisitor[Expression]): Expression = {
    val newLocalVarDef = visitor.visit( forCollection.localVarDef ).asInstanceOf[LocalVarDef]
    val newLooped = visitor.visit( forCollection.looped )
    val newBody = visitor.visit(forCollection.body).asInstanceOf[Block]
    if(newLocalVarDef!=forCollection.localVarDef ||
       newLooped!=forCollection.looped ||
       newBody!=forCollection.body){
       ForCollection(newLocalVarDef,newLooped,newBody)
    }else{
      forCollection
    }
  }

  override def visit(forMap: ForMap,
                     visitor: ExpressionVisitor[Expression]): Expression = {
      val newLoopKeyDef = visitor.visit(forMap.loopKeyDef).asInstanceOf[LocalVarDef]
      val newLoopValueDef = visitor.visit(forMap.loopValueDef).asInstanceOf[LocalVarDef]
      val newLooped = visitor.visit(forMap.looped)
      val newBody = visitor.visit(forMap.body).asInstanceOf[Block]
    if(newLoopKeyDef!=forMap.loopKeyDef ||
       newLoopValueDef!=forMap.loopValueDef ||
       newLooped!=forMap.looped ||
       newBody!=forMap.body){
        ForMap(newLoopKeyDef,newLoopValueDef,newLooped,newBody)
    }else{
        forMap
    }
  }

  override def visit(whileExpr: While,
                     visitor: ExpressionVisitor[Expression]): Expression = {
    val newCondition = visitor.visit(whileExpr.condition)
    val newBody = visitor.visit(whileExpr.body).asInstanceOf[Block]
    if(newCondition!=whileExpr.condition || newBody!=whileExpr.body){
        While(newCondition,newBody)
    }else{
        whileExpr
    }
  }

  override def visit(doWhile: DoWhile,
                     visitor: ExpressionVisitor[Expression]): Expression = {
    val newCondition = visitor.visit(doWhile.condition)
    val newBody = visitor.visit(doWhile.body).asInstanceOf[Block]
    if(newCondition!=doWhile.condition || newBody!=doWhile.body){
      DoWhile(newCondition,newBody)
    }else{
      doWhile
    }
  }

  override def visit(sync: Sync,
                     visitor: ExpressionVisitor[Expression]): Expression = {
    val newCondition = visitor.visit(sync.condition)
    val newBody = visitor.visit(sync.body).asInstanceOf[Block]
    if(newCondition!=sync.condition || newBody!=sync.body){
      Sync(newCondition,newBody)
    }else{
      sync
    }
  }

  override def visit(ifExpr: If,
                     visitor: ExpressionVisitor[Expression]): Expression = {

      val reviseDefault = ExpressionReviser.revise[Expression,Block](ifExpr.default,visitor)
      val reviseCases = ExpressionReviser.revise[Expression,Block,Expression,Block](ifExpr.cases,visitor,visitor)
      if(reviseCases.isEmpty && reviseDefault.isEmpty ){
          ifExpr
      }else{
          If(reviseCases.getOrElse(ifExpr.cases),reviseDefault.getOrElse(ifExpr.default))
      }
  }

  override def visit(tryCatch: TryCatch,
                     visitor: ExpressionVisitor[Expression]): Expression = {
      val newTryBlock = visit(tryCatch.tryBlock,visitor).asInstanceOf[Block]
      val reviseFinallyBlock = ExpressionReviser.revise[Expression,Block](tryCatch.finallyBlock,visitor)

      val blocks = tryCatch.catches.map(_._2)
      val newBlocks = ExpressionReviser.revise[Block,Block](blocks,visitor)
      if(newTryBlock!=tryCatch.tryBlock || reviseFinallyBlock.isDefined || newBlocks.isDefined){
          TryCatch(newTryBlock,
            tryCatch.catches.map(_._1).zip(newBlocks.getOrElse(blocks)),
            reviseFinallyBlock.getOrElse(tryCatch.finallyBlock))
      }else{
          tryCatch
      }
  }

  override def visit(asyncBlock: Async,
                     visitor: ExpressionVisitor[Expression]): Expression = {
    val newBody = visit(asyncBlock.body,visitor).asInstanceOf[Block]
    if(newBody!=asyncBlock.body){
      Async(newBody,asyncBlock.executor,asyncBlock.dslType)
    }else{
      asyncBlock
    }
  }

  override def visit(tryBlock: Try,
                     visitor: ExpressionVisitor[Expression]): Expression = {
    val newBody = visit(tryBlock.body,visitor).asInstanceOf[Block]
    if(newBody!=tryBlock.body){
        Try(newBody,tryBlock.dslType)
    }else{
        tryBlock
    }
  }

  override def visit(matchCase: MatchCase,
                     visitor: ExpressionVisitor[Expression]): Expression = {
    val reviseCases = ExpressionReviser.revise[Expression,Block,Expression,Block](matchCase.cases,visitor,visitor)
    val reviseDefault = ExpressionReviser.revise[Expression,Block](matchCase.default,visitor)
    if(reviseCases.isDefined || reviseDefault.isDefined ){
      If(reviseCases.getOrElse(matchCase.cases),reviseDefault.getOrElse(matchCase.default))
    }else{
      matchCase
    }
  }
}
