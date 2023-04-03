package com.dongjiaqiang.jvm.dsl.api.expression.literal

import com.dongjiaqiang.jvm.dsl.api.`type`.{DslType, SetType}
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.expression.block.Lambda
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope

/**
 *
 * <pre><code>
 * program{
 *
 * def method()=Unit{
 * Set[Int] set = {1,2,3}; //{1,2,3} => SetLiteral
 * }
 * }<pre><code>
 */
class SetLiteral(literal: Array[ValueExpression],
                 override val dslType: SetType)
  extends Literal[Array[ValueExpression], SetType]( literal ) {
  override def toString: String = s"(${literal.mkString( "," )})"

  def asSeq():SetLiteral={
      new SetLiteral(literal,dslType.asSeq())
  }

  def asSort():SetLiteral={
      new SetLiteral(literal,dslType.asSorted())
  }

  def asSort(sorter:Lambda):SetLiteral={
      new SetLiteral(literal,dslType.asSorted(sorter))
  }

  override def equals(obj: Any): Boolean = {
    obj match {
      case setLiteral: SetLiteral ⇒ setLiteral.literal.sameElements( literal ) && setLiteral.dslType == dslType
      case _ ⇒ false
    }
  }

  override def getValueType(programScope: ProgramScope): SetType = SetType( literal.map( _.getValueType( programScope ))
    .reduce( (t1, t2) ⇒ t1.commonDslType( programScope.importManager, t2 ) ),dslType.seq,dslType.sorted,dslType.sorter  )
}
