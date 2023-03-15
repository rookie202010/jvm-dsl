package com.dongjiaqiang.jvm.dsl.core.expression.visitor.statement

import com.dongjiaqiang.jvm.dsl.api.expression.`var`.{Assign, VarRef}
import com.dongjiaqiang.jvm.dsl.api.expression.statement._
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.ExpressionVisitor
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.statement.StatementExpressionVisitor
import com.dongjiaqiang.jvm.dsl.api.expression._


trait StatementExpressionReviser extends StatementExpressionVisitor[Expression] {
  override def visit(assign: Assign, visitor: ExpressionVisitor[Expression]): Expression = {
    val newVarRef = visitor.visit( assign.varRef ).asInstanceOf[VarRef]
    val newAssigned = visitor.visit( assign.assigned )
    if (newVarRef != assign.varRef || newAssigned != assign.assigned) {
      Assign( newVarRef, newAssigned.asInstanceOf[ValueExpression])
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
        statement.Throw(newExpr)
    }else{
        throwExpr
    }
  }

  override def visit(returnExpr: Return, visitor: ExpressionVisitor[Expression]): Expression={
    val newExpr = returnExpr.expression.map(visitor.visit)
    if(newExpr!=returnExpr.expression){
        Return(newExpr.map(_.asInstanceOf[ValueExpression]))
    }else{
        returnExpr
    }
  }

  override def visit(assert: Assert, visitor: ExpressionVisitor[Expression]): Expression={
    val newExpr = visitor.visit(assert.expression)
    if(newExpr!=assert.expression){
        statement.Assert(newExpr.asInstanceOf[ValueExpression])
    }else{
        assert
    }
  }
}
