package com.dongjiaqiang.jvm.dsl.api.expression.literal

import com.dongjiaqiang.jvm.dsl.api.`type`.{DslType, ListType}
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
 * def method()=Unit{
 * List[Foo] option = [new Foo(1,2),new Foo(2,3)]; // [new Foo(1,2),new Foo(2,3)] => ListLiteral
 * }
 * }<pre><code>
 */
class ListLiteral(literal: Array[ValueExpression],
                  override val dslType: ListType)
  extends Literal[Array[ValueExpression], ListType]( literal ) {
  override def toString: String = s"[${literal.mkString( "," )}]"

  override def equals(obj: Any): Boolean = {
    obj match {
      case listLiteral: ListLiteral ⇒
        listLiteral.literal.sameElements( literal ) && listLiteral.dslType == dslType
      case _ ⇒ false
    }
  }

  override def getValueType(programScope: ProgramScope): ListType = ListType( literal.map( _.getValueType( programScope ) )
    .reduce( (t1, t2) ⇒ t1.commonDslType( programScope.importManager, t2 ) ) )
}
