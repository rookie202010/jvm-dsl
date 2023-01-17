package com.dongjiaqiang.jvm.dsl.core.expression.visitor.`var`

import com.dongjiaqiang.jvm.dsl.core.expression._
import com.dongjiaqiang.jvm.dsl.core.expression.visitor.{ExpressionReviser, ExpressionVisitor}

trait VarExpressionReviser extends VarExpressionVisitor[Expression]{
  override def visit(localVarDef: LocalVarDef,visitor: ExpressionVisitor[Expression]): Expression={
      val assigned = ExpressionReviser.revise[Expression,Expression](localVarDef.assigned,visitor)
      if(assigned.isDefined){
        LocalVarDef( localVarDef.fieldScope, localVarDef.dslType, assigned.get )
      }else{
          localVarDef
      }
  }

  override def visit(varRef: VarRef,visitor: ExpressionVisitor[Expression]): Expression={
      varRef
  }

  override def visit(arrayVarRef: ArrayVarRef,visitor: ExpressionVisitor[Expression]): Expression={
      val indexExpression = visitor.visit(arrayVarRef.indexExpression)
      if(indexExpression!=arrayVarRef.indexExpression){
        new ArrayVarRef( indexExpression, arrayVarRef.name, arrayVarRef.fieldScope.dslType, arrayVarRef.fieldScope )
      }else{
          arrayVarRef
      }
  }

  override def visit(mapVarRef: MapVarRef,visitor: ExpressionVisitor[Expression]): Expression= {
    val keyExpression = visitor.visit( mapVarRef.keyExpression )
    if (keyExpression != mapVarRef.keyExpression) {
      new MapVarRef( keyExpression, mapVarRef.name, mapVarRef.fieldScope.dslType, mapVarRef.fieldScope )
    } else {
      mapVarRef
    }
  }

  override def visit(lambda: Lambda,visitor: ExpressionVisitor[Expression]): Expression={
    val body = visitor.visit(lambda.body).asInstanceOf[Block]
    if(body!=lambda.body){
        Lambda(lambda.inputs,body)
    }else{
        lambda
    }
  }
}
