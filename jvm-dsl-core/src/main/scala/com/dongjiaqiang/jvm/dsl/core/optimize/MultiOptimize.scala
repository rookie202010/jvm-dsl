package com.dongjiaqiang.jvm.dsl.core.optimize

import com.dongjiaqiang.jvm.dsl.api.`type`.visitor.MethodVisitor
import com.dongjiaqiang.jvm.dsl.api.`type`.{ClazzType, DefinitionClazzType, DslType, MonadDslType, OptionType}
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope


class MultiOptimize(val programScope: ProgramScope,val optimizeDslType: OptimizeDslType) extends MethodVisitor[Array[ValueExpression]]{


  private val monadOptimize:MonadOptimize = new MonadOptimize(programScope, optimizeDslType)
  private val optionOptimize:OptionOptimize = new OptionOptimize(programScope, optimizeDslType)
  private val clazzOptimize:ClazzOptimize = new ClazzOptimize(programScope, optimizeDslType)
  private val definitionClazzOptimize:DefinitionClazzOptimize = new DefinitionClazzOptimize(programScope, optimizeDslType)


  override def visit(calleeDslType: DslType, callee: ValueExpression, name: String, params: Array[ValueExpression]): Option[Array[ValueExpression]] = {
      val newParams = calleeDslType match {
        case optionType:OptionType ⇒
            optionOptimize.visit(optionType,callee, name, params)
        case monadDslType: MonadDslType⇒
            monadOptimize.visit(monadDslType,callee, name, params)
        case clazzType: ClazzType⇒
            clazzOptimize.visit(clazzType, callee, name, params)
        case definitionClazzType: DefinitionClazzType⇒
            definitionClazzOptimize.visit(definitionClazzType, callee, name, params)
        case _⇒ Some(params.map(param⇒optimizeDslType.visit(param).asInstanceOf[ValueExpression]))
      }
      if(newParams.isDefined){
          newParams
      }else{
          Some(params.map(param⇒optimizeDslType.visit(param).asInstanceOf[ValueExpression]))
      }
  }

}



