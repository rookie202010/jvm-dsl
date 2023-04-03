package com.dongjiaqiang.jvm.dsl.api.expression.literal

import com.dongjiaqiang.jvm.dsl.api.`type`.{DslType, OptionType}
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
 * def method(Foo foo)=Unit{
 * Option[Foo] option = ?foo; // ?foo => OptionLiteral
 *
 * Foo foo =  option match{
 * case Some(f)=>f // Some(f) => OptionalLiteral  happen in optimizer phase
 * case None()=>null // None => OptionalLiteral happen in optimizer phase
 * }
 *
 * }
 * }<pre><code>
 */
class OptionLiteral(literal: ValueExpression,
                    override val dslType: OptionType)
  extends Literal[ValueExpression, OptionType]( literal ) {
  override def toString: String = s"?$literal"

  override def equals(obj: Any): Boolean = {
    obj match {
      case optionLiteral: OptionLiteral ⇒
        optionLiteral.literal == literal && optionLiteral.dslType == dslType
      case _ ⇒ false
    }
  }

  override def getValueType(programScope: ProgramScope): OptionType = {
    new OptionType( literal.getValueType( programScope ) )
  }
}
