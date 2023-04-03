package com.dongjiaqiang.jvm.dsl.api.expression.literal

import com.dongjiaqiang.jvm.dsl.api.`type`.{DslType, MapType}
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope

/**
 * <pre><code>
 * program{
 * Map[Int,Foo] fooMap = {0:new Foo(2,3),1:new Foo(1,2)}; //{0:new Foo(2,3),1:new Foo(1,2)} => MapLiteral
 *
 * def method()=Unit{
 * Int i =0;
 * Foo b = fooMap(i);  // fooMap(i) => MapVarRef
 * }
 * }<pre><code>
 */
class MapLiteral(literal: Array[(ValueExpression, ValueExpression)],
                 override val dslType: MapType)
  extends Literal[Array[(ValueExpression, ValueExpression)], MapType]( literal ) {
  override def toString: String = s"{${literal.map( kv ⇒ kv._1.toString + ":" + kv._2.toString ).mkString( "," )}}"

  override def equals(obj: Any): Boolean = {
    obj match {
      case mapLiteral: MapLiteral ⇒
        literal.sameElements( mapLiteral.literal ) && dslType == mapLiteral.dslType
      case _ ⇒ false
    }
  }

  override def getValueType(programScope: ProgramScope): MapType = {
    val keyType = literal.map( _._1 ).map( _.getValueType( programScope ) ).reduce( (t1, t2) ⇒ t1.commonDslType( programScope.importManager, t2 ) )
    val valueType = literal.map( _._2 ).map( _.getValueType( programScope ) ).reduce( (t1, t2) ⇒ t1.commonDslType( programScope.importManager, t2 ) )
    MapType( keyType, valueType,dslType.seq,dslType.sorted,dslType.sorter )
  }
}
