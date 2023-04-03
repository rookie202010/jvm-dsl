package com.dongjiaqiang.jvm.dsl.api.expression.literal

import com.dongjiaqiang.jvm.dsl.api.`type`.{DslType, EitherType, LeftType, MonadDslType, RightType}
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope

/**
 * in optimizer phase clazzLiteral represent either(left or right) will be converted to eitherLiteral
 * <pre><code>
 *
 * program{
 * def foo(Either[Int,Long] a)=String{
 * a match {
 * case Left(1)=> { return "left";}  // Left(1) => EitherLiteral
 * case right(1L)=> { return "right";} // Right(1L) => EitherLiteral
 * }
 * }
 *
 * Either[Int,Long] b = Left(1) // Left(1) => EitherLiteral
 * }
 *
 * <pre><code>
 */
class EitherLiteral(literal: Either[ValueExpression, ValueExpression],
                    override val dslType: EitherType)
  extends Literal[Either[ValueExpression, ValueExpression], EitherType]( literal ) {

  override def toString: String = {
    literal match {
      case Left( left ) ⇒ s"Left($left)"
      case Right( right ) ⇒ s"Right($right)"
    }
  }


  override def getValueType(programScope: ProgramScope): MonadDslType = literal match {
    case Left( expression ) ⇒ LeftType( expression.getValueType( programScope ),dslType.rightParameterType )
    case Right( expression ) ⇒ RightType( dslType.leftParameterType,expression.getValueType( programScope ) )
  }


  override def equals(obj: Any): Boolean = {
    obj match {
      case eitherLiteral: EitherLiteral ⇒
        eitherLiteral.literal == literal && eitherLiteral.dslType == dslType
      case _ ⇒ false
    }
  }
}
