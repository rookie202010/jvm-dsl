package com.dongjiaqiang.jvm.dsl.core.expression.visitor.statement

import com.dongjiaqiang.jvm.dsl.api.expression._
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.ExpressionVisitor
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.statement.StatementExpressionVisitor


trait StatementExpressionReviser extends StatementExpressionVisitor[Expression] {
  override def visit(assign: Assign, visitor: ExpressionVisitor[Expression]): Expression = {
    val newVarRef = visitor.visit( assign.varRef ).asInstanceOf[VarRef]
    val newAssigned = visitor.visit( assign.assigned )
    if (newVarRef != assign.varRef || newAssigned != assign.assigned) {
      Assign( newVarRef, newAssigned )
    } else {
      assign
      }
  }

  override def visit(break: Break.type, visitor: ExpressionVisitor[Expression]): Expression={
      break
  }

  override def visit(continue: Continue.type, visitor: ExpressionVisitor[Expression]): Expression={
      continue
  }

  override def visit(throwExpr: Throw, visitor: ExpressionVisitor[Expression]): Expression={
    val newExpr = visitor.visit(throwExpr.expression)
    if(newExpr!=throwExpr.expression){
        Throw(newExpr)
    }else{
        throwExpr
    }
  }

  override def visit(returnExpr: Return, visitor: ExpressionVisitor[Expression]): Expression={
    val newExpr = returnExpr.expression.map(visitor.visit)
    if(newExpr!=returnExpr.expression){
        Return(newExpr)
    }else{
        returnExpr
    }
  }

  override def visit(assert: Assert, visitor: ExpressionVisitor[Expression]): Expression={
    val newExpr = visitor.visit(assert.expression)
    if(newExpr!=assert.expression){
        Assert(newExpr)
    }else{
        assert
    }
  }
}
