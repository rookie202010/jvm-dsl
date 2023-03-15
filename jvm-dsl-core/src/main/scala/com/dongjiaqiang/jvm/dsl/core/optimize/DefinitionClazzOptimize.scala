package com.dongjiaqiang.jvm.dsl.core.optimize

import com.dongjiaqiang.jvm.dsl.api.`type`.visitor.DefinitionClazzMethodVisitor
import com.dongjiaqiang.jvm.dsl.api.`type`.{DefinitionClazzType, DslType}
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.scope.{MethodScope, ProgramScope}

import scala.collection.mutable.ArrayBuffer

/**
 *DefinitionClazzOptimize use for determines the input parameters of a method of type DefinitionClazzType that accepts a lambda type
 *
 * for example
 * <pre><code>
 * program{
 *    class A(Int a){
 *        def method(Int=>Long lam)=Long{
 *            return lam.apply(a);
 *        }
 *    }
 *    def method()=Unit{
 *        A a = new A(20);
 *        a.method(e=> { return e.toLong; }); //e is UnResolvedType and we should determine e to IntType;
 *    }
 * }
 *
 * <pre><code>
 */
class DefinitionClazzOptimize(override val programScope: ProgramScope,val optimizeDslType: OptimizeDslType) extends DefinitionClazzMethodVisitor[Array[ValueExpression]] {

  override def visit(methodScope: MethodScope, calleeDslType: DefinitionClazzType,
                     callee: ValueExpression, name: String, params: Array[ValueExpression]): Array[ValueExpression] = {
    val newParams = ArrayBuffer[ValueExpression]( )
    val zips = methodScope.params.toList.zip( params )
    zips.foreach {
      case ((_, fieldScope), param) ⇒
          ClazzOptimize.visit(fieldScope.dslType,param,optimizeDslType,newParams)
    }
    newParams.toArray
  }

  override def visit(calleeDslType: DslType, callee: ValueExpression, name: String, params: Array[ValueExpression]): Option[Array[ValueExpression]] = {
    val definitionClazzType = calleeDslType.asInstanceOf[DefinitionClazzType]
    definitionClazzType.clazzScope.methods.get( name ) match {
      case Some( methodScope ) ⇒
        Some( visit( methodScope, definitionClazzType, callee, methodScope.name, params ) )
      case _ ⇒ None
    }
  }
}
