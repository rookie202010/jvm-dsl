package com.dongjiaqiang.jvm.dsl.core.optimize

import com.dongjiaqiang.jvm.dsl.api.`type`._
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.ExpressionVisitor
import com.dongjiaqiang.jvm.dsl.api.expression._
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope

case class ExpressionResultType(dslType: Option[DslType])

class DefaultTypeChecker(override val programScope: ProgramScope) extends ExpressionVisitor[ExpressionResultType] {
  override def visit(literal: IntLiteral,
                     visitor: ExpressionVisitor[ExpressionResultType]): ExpressionResultType = {
    ExpressionResultType( Some( IntType ) )
  }

  private def getPriority(expressionResultType: ExpressionResultType): Option[Int] = {
    expressionResultType.dslType match {
      case Some( dslType ) ⇒
        if (dslType.isInstanceOf[NumberDslType] || dslType == UnResolvedType) {
          dslType match {
                case IntType⇒Some(0)
                case LongType⇒Some(1)
                case FloatType⇒Some(2)
                case DoubleType⇒Some(3)
                case UnResolvedType⇒Some(4)
                case _⇒None
              }
            }else{
              None
            }
          case _⇒None
        }
  }

  private def checkBinaryExpression(expression:BinaryExpression,
                                    checker:(ExpressionResultType,ExpressionResultType)⇒ExpressionResultType,
                                    visitor: ExpressionVisitor[ExpressionResultType]):ExpressionResultType={
    val left = visitor.visit(expression.left)
    val right = visitor.visit(expression.right)
    checker.apply(left,right)
  }

  private def checkUnaryExpression(expression:UnaryExpression,
                                   checkType:DslType⇒Boolean,
                                   visitor: ExpressionVisitor[ExpressionResultType]):ExpressionResultType={
    val expressionResultType = visitor.visit(expression)
    val result = expressionResultType.dslType match {
      case Some(result)⇒ checkType.apply(result)
      case None⇒false
    }
    if(!result){
      throw new IllegalStateException(s"$expression is illegal")
    }else{
      expressionResultType
    }
  }

  private def checkBinaryExpression(expression: BinaryExpression,
                                    checkType:DslType,
                                    visitor: ExpressionVisitor[ExpressionResultType]):ExpressionResultType={
    checkBinaryExpression(expression,(left,right)⇒{

      val valid = left.dslType match {
        case Some(i)⇒ right.dslType match {
          case Some(j)⇒ (i == checkType || i == UnResolvedType) && (j==checkType || j == UnResolvedType)
          case None⇒false
        }
        case None⇒false
      }
      if(valid){
        ExpressionResultType(Some(checkType))
      }else{
        throw new IllegalStateException(s"$expression is illegal")
      }

    },visitor)
  }

  private def checkArithmetic(expression:BinaryExpression,
                              visitor: ExpressionVisitor[ExpressionResultType]):ExpressionResultType={
    checkBinaryExpression(expression,(left,right)⇒{
      val leftPriority = getPriority(left)
      val rightPriority = getPriority(right)
      if(leftPriority.isDefined && rightPriority.isDefined){
        if(leftPriority.get > rightPriority.get){
          left
        }else{
          right
        }
      }else{
        throw new IllegalStateException(s"$expression is illegal")
      }
    },visitor)
  }

  override def visit(div: Div, visitor: ExpressionVisitor[ExpressionResultType]): ExpressionResultType = {
      checkArithmetic(div,visitor)
  }
  override def visit(mul: Mul, visitor: ExpressionVisitor[ExpressionResultType]): ExpressionResultType = {
      checkArithmetic(mul,visitor)
  }
  override def visit(mod: Mod, visitor: ExpressionVisitor[ExpressionResultType]): ExpressionResultType = {
      checkArithmetic(mod,visitor)
  }
  override def visit(sub: Sub, visitor: ExpressionVisitor[ExpressionResultType]): ExpressionResultType = {
      checkArithmetic(sub,visitor)
  }
  override def visit(add:Add,visitor: ExpressionVisitor[ExpressionResultType]): ExpressionResultType = {
    checkArithmetic(add,visitor)
  }


  override def visit(leftShift: LeftShift,
                     visitor: ExpressionVisitor[ExpressionResultType]): ExpressionResultType = {
    checkBinaryExpression(leftShift,IntType,visitor)
  }
  override def visit(rightShift: RightShift,
                     visitor: ExpressionVisitor[ExpressionResultType]): ExpressionResultType ={
    checkBinaryExpression(rightShift,IntType,visitor)
  }
  override def visit(unsignedRightShift: UnsignedRightShift,
                     visitor: ExpressionVisitor[ExpressionResultType]): ExpressionResultType = {
    checkBinaryExpression(unsignedRightShift,IntType,visitor)
  }

  override def visit(and: And, visitor: ExpressionVisitor[ExpressionResultType]): ExpressionResultType = {
    checkBinaryExpression(and,BoolType,visitor)
  }

  override def visit(or: Or, visitor: ExpressionVisitor[ExpressionResultType]): ExpressionResultType = {
    checkBinaryExpression(or,BoolType,visitor)
  }

  override def visit(lt: Lt, visitor: ExpressionVisitor[ExpressionResultType]): ExpressionResultType = {
    checkArithmetic(lt,visitor)
  }

  override def visit(gt: Gt, visitor: ExpressionVisitor[ExpressionResultType]): ExpressionResultType = {
    checkArithmetic(gt,visitor)
  }

  override def visit(le: Le, visitor: ExpressionVisitor[ExpressionResultType]): ExpressionResultType = {
    checkArithmetic(le,visitor)
  }

  override def visit(ge: Ge, visitor: ExpressionVisitor[ExpressionResultType]): ExpressionResultType = {
    checkArithmetic(ge,visitor)
  }

  override def visit(negate: Negate, visitor: ExpressionVisitor[ExpressionResultType]): ExpressionResultType = {
    checkUnaryExpression(negate,dslType⇒dslType == BoolType || dslType == UnResolvedType,visitor)
  }

  override def visit(literal: LongLiteral, visitor: ExpressionVisitor[ExpressionResultType]): ExpressionResultType = {
      ExpressionResultType(Some(literal.dslType))
  }

  override def visit(literal: DoubleLiteral, visitor: ExpressionVisitor[ExpressionResultType]): ExpressionResultType = {
    ExpressionResultType(Some(literal.dslType))
  }

  override def visit(literal: FloatLiteral, visitor: ExpressionVisitor[ExpressionResultType]): ExpressionResultType = {
    ExpressionResultType(Some(literal.dslType))
  }

  override def visit(literal: BoolLiteral, visitor: ExpressionVisitor[ExpressionResultType]): ExpressionResultType = {
    ExpressionResultType(Some(literal.dslType))
  }

  override def visit(literal: StringLiteral, visitor: ExpressionVisitor[ExpressionResultType]): ExpressionResultType = {
    ExpressionResultType(Some(literal.dslType))
  }

  override def visit(literal: CharLiteral, visitor: ExpressionVisitor[ExpressionResultType]): ExpressionResultType = {
    ExpressionResultType(Some(literal.dslType))
  }

  override def visit(literal: ListLiteral, visitor: ExpressionVisitor[ExpressionResultType]): ExpressionResultType = {
    ExpressionResultType(Some(literal.dslType))
  }

  override def visit(literal: MapLiteral, visitor: ExpressionVisitor[ExpressionResultType]): ExpressionResultType = {
    ExpressionResultType(Some(literal.dslType))
  }

  override def visit(literal: SetLiteral, visitor: ExpressionVisitor[ExpressionResultType]): ExpressionResultType = {
    ExpressionResultType(Some(literal.dslType))
  }

  override def visit(literal: TupleLiteral, visitor: ExpressionVisitor[ExpressionResultType]): ExpressionResultType = {
    ExpressionResultType(Some(literal.dslType))
  }

  override def visit(literal: ClazzLiteral, visitor: ExpressionVisitor[ExpressionResultType]): ExpressionResultType = {
    ExpressionResultType(Some(literal.dslType))
  }

  override def visit(literal: OptionLiteral, visitor: ExpressionVisitor[ExpressionResultType]): ExpressionResultType = {
    ExpressionResultType(Some(literal.dslType))
  }

  override def visit(opposite: Opposite, visitor: ExpressionVisitor[ExpressionResultType]): ExpressionResultType = {
    checkUnaryExpression(opposite,dslType⇒dslType.isInstanceOf[NumberDslType] || dslType == UnResolvedType,visitor)
  }


}
