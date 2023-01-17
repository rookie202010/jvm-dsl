package com.dongjiaqiang.jvm.dsl.core.expression.visitor.callchain
import com.dongjiaqiang.jvm.dsl.core.expression.visitor.ExpressionVisitor
import com.dongjiaqiang.jvm.dsl.core.expression._


trait CallChainExpressionScanner extends CallChainExpressionVisitor[Unit]{
  override def visit(funcCallChain: FuncCallChain, visitor: ExpressionVisitor[Unit]): Unit = {

  }

  override def visit(literalCallChain: IntLiteralCallChain, visitor: ExpressionVisitor[Unit]): Unit = {

  }

  override def visit(literalCallChain: LongLiteralCallChain, visitor: ExpressionVisitor[Unit]): Unit = {

  }

  override def visit(literalCallChain: DoubleLiteralCallChain, visitor: ExpressionVisitor[Unit]): Unit = ???

  override def visit(literalCallChain: FloatLiteralCallChain, visitor: ExpressionVisitor[Unit]): Unit = ???

  override def visit(literalCallChain: BoolLiteralCallChain, visitor: ExpressionVisitor[Unit]): Unit = ???

  override def visit(literalCallChain: StringLiteralCallChain, visitor: ExpressionVisitor[Unit]): Unit = ???

  override def visit(literalCallChain: CharLiteralCallChain, visitor: ExpressionVisitor[Unit]): Unit = ???

  override def visit(literalCallChain: ListLiteralCallChain, visitor: ExpressionVisitor[Unit]): Unit = ???

  override def visit(literalCallChain: OptionLiteralCallChain, visitor: ExpressionVisitor[Unit]): Unit = ???

  override def visit(literalCallChain: TupleLiteralCallChain, visitor: ExpressionVisitor[Unit]): Unit = ???

  override def visit(literalCallChain: MapLiteralCallChain, visitor: ExpressionVisitor[Unit]): Unit = ???

  override def visit(literalCallChain: SetLiteralCallChain, visitor: ExpressionVisitor[Unit]): Unit = ???

  override def visit(literalCallChain: ClazzLiteralCallChain, visitor: ExpressionVisitor[Unit]): Unit = ???
}
