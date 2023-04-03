package com.dongjiaqiang.jvm.dsl.api.`type`.visitor
import com.dongjiaqiang.jvm.dsl.api.`type`.{DefinitionClazzType, DslType}
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.scope.MethodScope

/**
 * DefinitionClazzType method visitor
 */
trait DefinitionClazzMethodVisitor[T] extends MethodVisitor[T]{
  def visit(methodScope:MethodScope,calleeDslType:DefinitionClazzType, callee: ValueExpression, name:String, params:Array[ValueExpression]):T
  override def visit(calleeDslType:DslType,callee: ValueExpression, name: String, params: Array[ValueExpression]): Option[T] = {
    val definitionClazzType = calleeDslType.asInstanceOf[DefinitionClazzType]
    definitionClazzType.clazzScope.methods.get(name) match {
        case Some(methodScope)⇒
            val fs = methodScope.params.values
            generate(fs.map(_.dslType).toArray,params,()⇒{
              visit(methodScope,definitionClazzType,callee,methodScope.name,params)
            })
        case _⇒
          super.visit(calleeDslType, callee, name, params)
      }
  }
}
