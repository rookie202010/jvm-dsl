package com.dongjiaqiang.jvm.dsl.api.expression.literal

import com.dongjiaqiang.jvm.dsl.api.`type`.{DslType, TupleType}
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope

/**
 *
 * <pre><code>
 * program{
 *
 * def method()=Unit{
 * (Int,Long,String) tuple = (1,12L,"xx"); //(1,12L,"xx") => TupleLiteral
 * }
 *
 * }<pre><code>
 */
class TupleLiteral(literal: Array[ValueExpression],
                   override val dslType: TupleType)
  extends Literal[Array[ValueExpression], TupleType]( literal ) {
  override def toString: String = s"(${literal.mkString( "," )})"


  override def equals(obj: Any): Boolean = {
    obj match {
      case tupleLiteral: TupleLiteral ⇒
        tupleLiteral.literal.sameElements( literal ) && tupleLiteral.dslType == dslType
      case _ ⇒ false
    }
  }

  override def getValueType(programScope: ProgramScope): DslType = TupleType( literal.map( _.getValueType( programScope ) ) )
}
