package com.dongjiaqiang.jvm.dsl.core.expression.visitor.call

import com.dongjiaqiang.jvm.dsl.api.expression.`var`.VarRef
import com.dongjiaqiang.jvm.dsl.api.expression.call.{LiteralCall, MethodCall, StaticCall, VarCall}
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.ExpressionVisitor
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.call.CallExpressionVisitor
import com.dongjiaqiang.jvm.dsl.api.expression.{Expression, ValueExpression}

trait CallExpressionReviser extends CallExpressionVisitor[Expression]{
  override def visit(staticCall: StaticCall, visitor: ExpressionVisitor[Expression]): Expression = {
      StaticCall(staticCall.`type`,staticCall.name,
        staticCall.params.map(_.visit(visitor).asInstanceOf[ValueExpression]))
  }

  override def visit(varCall: VarCall, visitor: ExpressionVisitor[Expression]): Expression = {
      VarCall(varCall.varRef.visit(visitor).asInstanceOf[ValueExpression],varCall.name,
        varCall.params.map(_.visit(visitor).asInstanceOf[ValueExpression]))
  }

  override def visit(methodCall: MethodCall, visitor: ExpressionVisitor[Expression]): Expression = {
      MethodCall(methodCall.methodScope,methodCall.name,
        methodCall.params.map(_.visit(visitor).asInstanceOf[ValueExpression]))
  }

  override def visit(literalCall: LiteralCall, visitor: ExpressionVisitor[Expression]): Expression = {
      new LiteralCall(literalCall.literal.visit(visitor).asInstanceOf[ValueExpression],
        literalCall.name,literalCall.params.map(_.visit(visitor).asInstanceOf[ValueExpression]))
  }
}
