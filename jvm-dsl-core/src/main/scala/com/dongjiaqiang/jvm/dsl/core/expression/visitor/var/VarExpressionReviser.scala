package com.dongjiaqiang.jvm.dsl.core.expression.visitor.`var`

import com.dongjiaqiang.jvm.dsl.api.expression._
import com.dongjiaqiang.jvm.dsl.api.expression.`var`.{LocalVarDef, VarRef}
import com.dongjiaqiang.jvm.dsl.api.expression.block.{Block, Lambda}
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.ExpressionVisitor
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.`var`.VarExpressionVisitor
import com.dongjiaqiang.jvm.dsl.core.expression.visitor.ExpressionReviser

trait VarExpressionReviser extends VarExpressionVisitor[Expression] {
  override def visit(localVarDef: LocalVarDef, visitor: ExpressionVisitor[Expression]): Expression = {
    val assigned = ExpressionReviser.revise[ValueExpression, ValueExpression]( localVarDef.assigned, visitor )
    if (assigned.isDefined) {
      LocalVarDef( localVarDef.fieldScope, localVarDef.dslType, assigned.get )
    } else {
      localVarDef
    }
  }

  override def visit(varRef: VarRef,visitor: ExpressionVisitor[Expression]): Expression={
      varRef
  }


  override def visit(lambda: Lambda,visitor: ExpressionVisitor[Expression]): Expression={
    val body = visitor.visit(lambda.body).asInstanceOf[Block]
    if(body!=lambda.body){
      Lambda( lambda.inputs, body )
    }else{
        lambda
    }
  }
}
