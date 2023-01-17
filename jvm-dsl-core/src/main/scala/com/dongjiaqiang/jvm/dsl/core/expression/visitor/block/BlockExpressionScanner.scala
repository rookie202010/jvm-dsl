package com.dongjiaqiang.jvm.dsl.core.expression.visitor.block

import com.dongjiaqiang.jvm.dsl.core.expression._
import com.dongjiaqiang.jvm.dsl.core.expression.visitor.ExpressionVisitor


trait BlockExpressionScanner extends BlockExpressionVisitor[Unit] {

  override def visit(block: Block, visitor: ExpressionVisitor[Unit]): Unit = {
    block.expressions.foreach( visitor.visit )
  }

  override def visit(forExpr: For, visitor: ExpressionVisitor[Unit]): Unit = {
    visitor.visit( forExpr.loopVarDef )
    visitor.visit( forExpr.loopVarCondition )
    visitor.visit( forExpr.loopVarUpdate )
    visit( forExpr.body, visitor )
  }

  override def visit(forCollection: ForCollection, visitor: ExpressionVisitor[Unit]): Unit = {
    visitor.visit( forCollection.localVarDef )
    visitor.visit( forCollection.looped )
    visit( forCollection.body, visitor )
  }

  override def visit(forMap: ForMap, visitor: ExpressionVisitor[Unit]): Unit = {
    visitor.visit( forMap.loopKeyDef )
    visitor.visit( forMap.loopValueDef )
    visitor.visit( forMap.looped )
    visit( forMap.body, visitor )
  }

  override def visit(whileExpr: While, visitor: ExpressionVisitor[Unit]): Unit = {
    visitor.visit( whileExpr.condition )
    visit( whileExpr.body, visitor )
  }

  override def visit(doWhile: DoWhile, visitor: ExpressionVisitor[Unit]): Unit = {
    visit( doWhile.body, visitor )
    visitor.visit( doWhile.condition )
  }

  override def visit(sync: Sync, visitor: ExpressionVisitor[Unit]): Unit = {
    visitor.visit( sync.condition )
    visit( sync.body, visitor )
  }

  override def visit(ifExpr: If, visitor: ExpressionVisitor[Unit]): Unit = {
    ifExpr.cases.foreach {
      case (c, b) ⇒
        visitor.visit( c )
        visit( b, visitor )
    }
    ifExpr.default.foreach( visitor.visit )
  }

  override def visit(tryCatch: TryCatch, visitor: ExpressionVisitor[Unit]): Unit = {
    visit( tryCatch.tryBlock, visitor )
    tryCatch.catches.foreach {
      case (_, b) ⇒ visit( b, visitor )
    }
    tryCatch.finallyBlock.foreach( visitor.visit )
  }

  override def visit(asyncBlock: Async, visitor: ExpressionVisitor[Unit]): Unit = {
    visit( asyncBlock.body, visitor )
  }

  override def visit(tryBlock: Try, visitor: ExpressionVisitor[Unit]): Unit = {
    visit( tryBlock.body, visitor )
  }


  override def visit(customBlockExpression: CustomBlockExpression, visitor: ExpressionVisitor[Unit]): Unit = {
    visit( customBlockExpression.body, visitor )
  }

  override def visit(matchCase: MatchCase, visitor: ExpressionVisitor[Unit]): Unit = {
    visitor.visit( matchCase.matched )
    matchCase.cases.foreach {
      case (c, b) ⇒
        visitor.visit( c )
        visitor.visit( b )
    }
    matchCase.default.foreach( visitor.visit )
  }

  override def visit(matchType: MatchType, visitor: ExpressionVisitor[Unit]): Unit = {

  }

  override def visit(matchHead: MatchHead, visitor: ExpressionVisitor[Unit]): Unit = {
    matchHead.head.foreach {
      case Left( expression ) ⇒ visitor.visit( expression )
      case _ ⇒
    }
    matchHead.tail match {
      case Left( expression ) ⇒ visitor.visit( expression )
    }
  }

  override def visit(matchList: MatchList, visitor: ExpressionVisitor[Unit]): Unit = {
    matchList.expressions.foreach {
      case Left( expression ) ⇒ visitor.visit( expression )
      case _ ⇒
    }
  }

  override def visit(matchTuple: MatchTuple, visitor: ExpressionVisitor[Unit]): Unit = {
    matchTuple.expressions.foreach {
      case Left( expression ) ⇒ visitor.visit( expression )
      case _ ⇒
    }
  }

  override def visit(matchClass: MatchClass, visitor: ExpressionVisitor[Unit]): Unit = {
    matchClass.expressions.foreach {
      case Left( expression ) ⇒ visitor.visit( expression )
      case _ ⇒
    }
  }
}
