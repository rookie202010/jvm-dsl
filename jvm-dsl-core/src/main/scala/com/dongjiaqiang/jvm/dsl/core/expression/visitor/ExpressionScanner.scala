package com.dongjiaqiang.jvm.dsl.core.expression.visitor
import com.dongjiaqiang.jvm.dsl.core.expression.visitor.`var`.VarExpressionScanner
import com.dongjiaqiang.jvm.dsl.core.expression.visitor.binary.expression.BinaryExpressionScanner
import com.dongjiaqiang.jvm.dsl.core.expression.visitor.block.BlockExpressionScanner
import com.dongjiaqiang.jvm.dsl.core.expression.visitor.callchain.CallChainExpressionScanner
import com.dongjiaqiang.jvm.dsl.core.expression.visitor.literal.LiteralExpressionScanner
import com.dongjiaqiang.jvm.dsl.core.expression.visitor.statement.StatementExpressionScanner
import com.dongjiaqiang.jvm.dsl.core.expression.visitor.unary.expression.UnaryExpressionScanner


trait ExpressionScanner extends ExpressionVisitor[Unit]
  with BinaryExpressionScanner
  with BlockExpressionScanner
  with CallChainExpressionScanner
  with LiteralExpressionScanner
  with StatementExpressionScanner
  with UnaryExpressionScanner
  with VarExpressionScanner

