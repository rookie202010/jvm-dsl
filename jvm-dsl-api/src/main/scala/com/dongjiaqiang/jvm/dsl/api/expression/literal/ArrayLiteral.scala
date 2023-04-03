package com.dongjiaqiang.jvm.dsl.api.expression.literal

import com.dongjiaqiang.jvm.dsl.api.`type`.{ArrayType, DslType}
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope

/**
 * in optimizer phase clazzLiteral represent array will be converted to arrayLiteral
 * <pre><code>
 * program{
 * class Foo(Int a,Int b){
 * def foo()=Int{
 * return a+b;
 * }
 *
 * def method(Int a,Int b)=Unit{
 * Array[Foo] array = new Array(new Foo(1,2),new Foo(2,3)); // new Array(new Foo(1,2),new Foo(2,3)) => ArrayLiteral
 * }
 * <pre><code>
 */
class ArrayLiteral(literal: Array[ValueExpression], override val dslType: ArrayType)
  extends Literal[Array[ValueExpression], ArrayType]( literal ) {
  override def toString: String = s"new Array(${literal.mkString( "," )})"

  override def getValueType(programScope: ProgramScope): ArrayType = {
    literal.map( v ⇒ v.getValueType( programScope ) )
      .reduceOption[DslType] {
        case (d1, d2) ⇒ d1.commonDslType( programScope.importManager, d2 )
      } match {
      case Some( dslType ) ⇒ ArrayType( dslType )
      case _ ⇒ dslType
    }
  }

  override def equals(obj: Any): Boolean = {
    obj match {
      case arrayLiteral: ArrayLiteral ⇒
        arrayLiteral.literal.sameElements( literal ) && arrayLiteral.dslType == dslType
      case _ ⇒ false
    }
  }
}
