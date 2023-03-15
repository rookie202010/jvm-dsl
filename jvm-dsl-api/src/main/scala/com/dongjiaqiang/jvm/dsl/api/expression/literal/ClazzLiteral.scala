package com.dongjiaqiang.jvm.dsl.api.expression.literal

import com.dongjiaqiang.jvm.dsl.api.`type`.{ClazzType, DefinitionClazzType, DslType}
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope

/**
 *
 * <pre><code>
 * program{
 * class Foo(Int a,Int b){
 * def foo()=Int{
 * return a+b;
 * }
 * }
 *
 * def method(Int a,Int b)=Unit{
 * Foo foo = new Foo(a,b); // new Foo(a,b) => ClazzLiteral
 * Foo foo = new Foo(1,2); // new Foo(1,2) => ClazzLiteral
 * }
 * }<pre><code>
 */
class ClazzLiteral(literal: Array[ValueExpression],
                   override val dslType: DslType) extends Literal[Array[ValueExpression], DslType]( literal ) {
  override def toString: String = dslType match {
    case clazzType: ClazzType ⇒
      s"new ${clazzType.clazzName}(${literal.mkString( "," )})"
    case definitionClazzType: DefinitionClazzType ⇒
      s"new ${definitionClazzType.clazzName}(${literal.mkString( "," )})"
  }

  override def equals(obj: Any): Boolean = {
    obj match {
      case clazzLiteral: ClazzLiteral =>
        clazzLiteral.literal.sameElements( literal ) && clazzLiteral.dslType == dslType
      case _ ⇒ false
    }
  }

  override def getValueType(programScope: ProgramScope): DslType = dslType
}
