package com.dongjiaqiang.jvm.dsl.api.expression.`var`

import com.dongjiaqiang.jvm.dsl.api.`type`.{ClazzType, DslType}
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope

import scala.collection.mutable.{ListMap ⇒ MutableMap}

/**
 * <p>var ref can refer to static field in import clazz<p>
 *
 * <pre><code>
 * program{
 *      import com.xxx.Foo;
 *      Int i = Foo.i; //StaticVarRef;
 * }
 * <pre><code>
 */
class StaticVarRef(val clazzType: ClazzType, val programScope: ProgramScope, override val refs: List[String],
                        override val arrayRefIndexExpressions: MutableMap[Int, List[ValueExpression]]) extends VarRef( refs, arrayRefIndexExpressions, None ) {
  override def equals(obj: Any): Boolean = {
    obj match {
      case staticVarRef: StaticVarRef ⇒ staticVarRef.clazzType == clazzType && staticVarRef.refs == refs && staticVarRef.arrayRefIndexExpressions == arrayRefIndexExpressions
      case _ ⇒ false
    }
  }

  override def toString: String = s"${clazzType.name}.${super.toString}"

  override def merge(varRefs: List[VarRef]): VarRef = {
    val varRef = super.merge( varRefs )
    new StaticVarRef( clazzType, programScope, varRef.refs, varRef.arrayRefIndexExpressions )
  }

  override def getValueType: DslType = {
    VarRef.getDslType( refs.zipWithIndex,arrayRefIndexExpressions, clazzType, programScope )
  }
}
