package com.dongjiaqiang.jvm.dsl.api.expression.visitor.call

import com.dongjiaqiang.jvm.dsl.api.expression.call.{LiteralCall, MethodCall, StaticCall, VarCall}
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.ExpressionVisitor

trait CallExpressionVisitor[T] {

  def visit(staticCall: StaticCall, visitor: ExpressionVisitor[T]): T = {
    throw new UnsupportedOperationException( "not supported" )
  }

  def visit(varCall: VarCall,visitor: ExpressionVisitor[T]):T={
    throw new UnsupportedOperationException( "not supported" )
  }

  def visit(methodCall: MethodCall,visitor: ExpressionVisitor[T]):T={
    throw new UnsupportedOperationException( "not supported" )
  }

  def visit(literalCall: LiteralCall,visitor: ExpressionVisitor[T]):T={
    throw new UnsupportedOperationException( "not supported" )
  }
}
