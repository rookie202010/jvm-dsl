package com.dongjiaqiang.jvm.dsl.api.`type`.visitor

import com.dongjiaqiang.jvm.dsl.api.`type`.{ClazzType, DslType, ImportClazzMethod, ImportClazzType}
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression

/**
 * clazzType method visitor
 */
trait ClazzMethodVisitor[T] extends MethodVisitor[T]{
  def visit(importClazzType:ImportClazzType,
            importClazzMethod:ImportClazzMethod,
            callee: ValueExpression,
            name:String,
            params:Array[ValueExpression]):T

  override def visit(calleeDslType:DslType, callee: ValueExpression, name: String, params: Array[ValueExpression]): Option[T] = {
      val clazzType = calleeDslType.asInstanceOf[ClazzType]
      val resolvedClazzType = programScope.importManager.resolve(clazzType.clazzName)
        resolvedClazzType.methods.get(name)
        .flatMap(method⇒{
          if(method.params.length==params.length && method.params.zip(params).forall({
            case (d,p)⇒ d.isSuperDslType(programScope.importManager,p.getValueType(programScope))
          })){
              Some(visit(resolvedClazzType,method,callee, name, params))
          }else{
              super.visit(calleeDslType, callee, name, params)
          }
        })
  }
}
