package com.dongjiaqiang.jvm.dsl.api.expression.binary

import com.dongjiaqiang.jvm.dsl.api.`type`._
import com.dongjiaqiang.jvm.dsl.api.exception.ExpressionParseException
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope

//binary expression
trait BinaryExpression extends ValueExpression {

  val left: ValueExpression
  val right: ValueExpression

  protected def checkArithmeticExpressionValueType(valueType: DslType): Unit = {
    valueType match {
      case DoubleType | FloatType | LongType | IntType | CharType | ByteType ⇒
      case _ ⇒
        throw ExpressionParseException( f"arithmetic expression only accept double float long int char and byte type $valueType $this" )
    }
  }

  protected def checkRelationExpressionValueType(leftValueType: DslType, rightValueType: DslType): Unit = {
    if (leftValueType != BoolType && rightValueType != BoolType) {
      throw ExpressionParseException( f"relation expression only accept bool type $this" )
    }
  }

  protected def checkBitExpressionValueTyp(valueType: DslType): Unit = {
    valueType match {
      case LongType | IntType | ByteType | CharType ⇒
      case _ ⇒
        throw ExpressionParseException( f"bit expression only accept long int byte and char type $this" )

    }
  }

  def checkValueType(leftType: DslType, rightType: DslType): Unit = {}

  override def getValueType(programScope: ProgramScope): DslType = {
    val leftType = left.getValueType( programScope )
    val rightType = right.getValueType( programScope )
    checkValueType( leftType, rightType )
    leftType.commonDslType( programScope.importManager, rightType )
  }
}
