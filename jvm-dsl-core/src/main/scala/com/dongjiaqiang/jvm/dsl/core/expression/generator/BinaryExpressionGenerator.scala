package com.dongjiaqiang.jvm.dsl.core.expression.generator

import com.dongjiaqiang.jvm.dsl.core.JvmDslParserParser._
import com.dongjiaqiang.jvm.dsl.core.expression.{Add, And, BitAnd, BitOr, Caret, Div, Eq, Expression, Ge, Gt, Le, LeftShift, Lt, Mod, Mul, NotEq, Or, Paren, RightShift, Sub, UnsignedRightShift}

import scala.collection.convert.ImplicitConversionsToScala._

object ParenGenerator extends IExpressionGenerator[ParenExpressionContext,Expression]{
  override def generate(expressionContext: ExpressionContext, ruleContext: ParenExpressionContext): Expression = {
      new Paren(OrGenerator.generate(expressionContext,ruleContext.conditionalOrExpression()))
  }
}

object OrGenerator extends IExpressionGenerator[ConditionalOrExpressionContext,Expression]{
  override def generate(expressionContext: ExpressionContext, ruleContext: ConditionalOrExpressionContext): Expression = {
    ruleContext match {
      case c: ConditionalAndExprContext ⇒
        AndGenerator.generate( expressionContext, c.conditionalAndExpression() )
      case c: OrOpExprContext ⇒
        new Or( generate( expressionContext, c.conditionalOrExpression() ),
          AndGenerator.generate( expressionContext, c.conditionalAndExpression() ) )
      case c: OrOpParenExprContext ⇒
        new Or( generate( expressionContext, c.conditionalOrExpression()),
          ParenGenerator.generate( expressionContext, c.parenExpression( ) ) )
      case c: ParenOrOpExprContext ⇒
        new Or( ParenGenerator.generate( expressionContext, c.parenExpression( ) ),
          generate( expressionContext, c.conditionalOrExpression()) )
      case c: ParenOrOpParenExprContext ⇒
        new Or( ParenGenerator.generate( expressionContext, c.parenExpression( ).head ),
          ParenGenerator.generate( expressionContext, c.parenExpression( ).last ) )
    }
  }
}

object AndGenerator extends IExpressionGenerator[ConditionalAndExpressionContext,Expression]{
  override def generate(expressionContext: ExpressionContext, ruleContext: ConditionalAndExpressionContext): Expression = {
    ruleContext match {
      case c: InclusiveOrExprContext ⇒
        InclusiveOrGenerator.generate( expressionContext, c.inclusiveOrExpression())
      case c: AndOpExprContext⇒
        new And( generate( expressionContext, c.conditionalAndExpression()),
          InclusiveOrGenerator.generate( expressionContext, c.inclusiveOrExpression()))
      case c: AndOpParenExprContext ⇒
        new And( generate( expressionContext, c.conditionalAndExpression()),
          ParenGenerator.generate( expressionContext, c.parenExpression( ) ) )
      case c: ParenAndOpExprContext ⇒
        new BitOr( ParenGenerator.generate( expressionContext, c.parenExpression( ) ),
          generate( expressionContext, c.conditionalAndExpression()) )
      case c: ParenAndOpParenExprContext ⇒
        new BitOr( ParenGenerator.generate( expressionContext, c.parenExpression( ).head ),
          ParenGenerator.generate( expressionContext, c.parenExpression( ).last ) )
    }
  }
}

object InclusiveOrGenerator extends IExpressionGenerator[InclusiveOrExpressionContext,Expression]{
  override def generate(expressionContext: ExpressionContext, ruleContext: InclusiveOrExpressionContext): Expression = {
    ruleContext match {
      case c: ExclusiveOrExprContext ⇒
        ExclusiveOrGenerator.generate( expressionContext, c.exclusiveOrExpression() )
      case c: BitOrOpExprContext ⇒
        new BitOr( generate( expressionContext, c.inclusiveOrExpression()),
          ExclusiveOrGenerator.generate( expressionContext, c.exclusiveOrExpression() ))
      case c: BitOrParenOpExprContext ⇒
        new BitOr( generate( expressionContext, c.inclusiveOrExpression() ),
          ParenGenerator.generate( expressionContext, c.parenExpression( ) ) )
      case c: ParenBitOrOpExprContext ⇒
        new BitOr( ParenGenerator.generate( expressionContext, c.parenExpression( ) ),
          generate( expressionContext, c.inclusiveOrExpression()) )
      case c: ParenBitOrOpParenExprContext ⇒
        new BitOr( ParenGenerator.generate( expressionContext, c.parenExpression( ).last ),
          ParenGenerator.generate( expressionContext, c.parenExpression( ).head ) )
    }
  }
}


object ExclusiveOrGenerator extends IExpressionGenerator[ExclusiveOrExpressionContext,Expression]{
  override def generate(expressionContext: ExpressionContext, ruleContext: ExclusiveOrExpressionContext): Expression = {
    ruleContext match {
      case c: BitAndExprContext ⇒
        BitAndGenerator.generate( expressionContext, c.bitAndExpression() )
      case c: CaretOpExprContext ⇒
        val left = generate( expressionContext, c.exclusiveOrExpression() )
        val right = BitAndGenerator.generate( expressionContext, c.bitAndExpression() )
        new Caret( left, right )
      case c: CaretOpParenExprContext ⇒
        val left = generate( expressionContext, c.exclusiveOrExpression() )
        val right = ParenGenerator.generate( expressionContext, c.parenExpression( ) )
        new Caret( left, right )
      case c: ParenCaretOpExprContext ⇒
        val left = ParenGenerator.generate( expressionContext, c.parenExpression( ) )
        val right = generate( expressionContext, c.exclusiveOrExpression() )
        new Caret( left, right )
      case c: ParenCaretOpParenExprContext ⇒
        val left = ParenGenerator.generate( expressionContext, c.parenExpression( ).head )
        val right = ParenGenerator.generate( expressionContext, c.parenExpression( ).last )
        new Caret( left, right )
    }
  }
}

object BitAndGenerator extends IExpressionGenerator[BitAndExpressionContext,Expression]{
  override def generate(expressionContext: ExpressionContext, ruleContext: BitAndExpressionContext): Expression = {
      ruleContext match {
        case c:EualityExprContext⇒
            EqualityGenerator.generate(expressionContext,c.equalityExpression())
        case c:BitAndOpExprContext⇒
            val left = generate(expressionContext,c.bitAndExpression())
            val right = EqualityGenerator.generate(expressionContext,c.equalityExpression())
            new BitAnd(left, right)
        case c:BitAndOpParenExprContext⇒
          val left = generate(expressionContext,c.bitAndExpression())
          val right = ParenGenerator.generate(expressionContext,c.parenExpression())
          new BitAnd(left, right)
        case c:ParenBitAnOpExprContext⇒
          val left = ParenGenerator.generate(expressionContext,c.parenExpression())
          val right = generate(expressionContext,c.bitAndExpression())
          new BitAnd(left, right)
        case c:ParenBitAndOpParenExprContext⇒
          val left = ParenGenerator.generate( expressionContext, c.parenExpression( ).head )
          val right = ParenGenerator.generate( expressionContext, c.parenExpression( ).last )
          new BitAnd(left, right)
      }
  }
}

object EqualityGenerator extends IExpressionGenerator[EqualityExpressionContext,Expression]{

  def generator(left: Expression, right: Expression, c: EqualityOperationContext): Expression = {
    if (c.EQUAL()!=null) {
      new Eq( left, right )
    } else{
      new NotEq( left, right )
    }
  }

  override def generate(expressionContext: ExpressionContext, ruleContext: EqualityExpressionContext): Expression = {
        ruleContext match {
          case c:RelationExprContext⇒
            RelationGenerator.generate(expressionContext,c.relationExpression())
          case c:EqualExprContext⇒
            val left = generate(expressionContext,c.equalityExpression())
            val right = RelationGenerator.generate(expressionContext,c.relationExpression())
            generator(left,right,c.equalityOperation())
          case c:EqualParenExprContext⇒
            val left = generate(expressionContext,c.equalityExpression())
            val right = ParenGenerator.generate(expressionContext,c.parenExpression())
            generator(left,right,c.equalityOperation())
          case c:ParenEqualExprContext⇒
            val left = ParenGenerator.generate(expressionContext,c.parenExpression())
            val right = generate(expressionContext, c.equalityExpression())
            generator(left,right,c.equalityOperation())
          case c:ParenEqualParenExprContext⇒
            val left = ParenGenerator.generate( expressionContext, c.parenExpression( ).head )
            val right = ParenGenerator.generate( expressionContext, c.parenExpression( ).last )
            generator( left, right, c.equalityOperation() )
        }
  }
}

object RelationGenerator extends IExpressionGenerator[RelationExpressionContext,Expression]{

  def generator(left: Expression, right: Expression, c: RelationOperationContext): Expression = {
    if (c.LT()!=null) {
      new Lt( left, right )
    } else if (c.GT()!=null) {
      new Gt( left, right )
    } else if(c.LE()!=null){
      new Le(left,right)
    }else{
      new Ge(left,right)
    }
  }

  override def generate(expressionContext: ExpressionContext, ruleContext: RelationExpressionContext): Expression = {
      ruleContext match {
        case c:ShiftExprContext⇒
            ShiftGenerator.generate(expressionContext,c.shiftExpression())
        case c:RelationOpExprContext⇒
            val left = generate(expressionContext, c.relationExpression())
            val right = ShiftGenerator.generate(expressionContext,c.shiftExpression())
            generator(left,right,c.relationOperation())
        case c:RelationOpParenExprContext⇒
            val left = generate(expressionContext, c.relationExpression())
            val right = ParenGenerator.generate(expressionContext,c.parenExpression())
            generator(left,right,c.relationOperation())
        case c:ParenRelationOpExprContext⇒
            val left = ParenGenerator.generate(expressionContext,c.parenExpression())
            val right = generate(expressionContext,c.relationExpression())
            generator(left,right,c.relationOperation())
        case c:ParenRelationOpParenExprContext⇒
            val left = ParenGenerator.generate(expressionContext,c.parenExpression().head)
            val right = ParenGenerator.generate(expressionContext,c.parenExpression().last)
            generator(left,right,c.relationOperation())
      }
  }
}

object ShiftGenerator extends IExpressionGenerator[ShiftExpressionContext,Expression]{

  def generator(left:Expression,right:Expression,c:ShiftOperationContext):Expression={
      if(c.GT().size()==3){
        new UnsignedRightShift(left,right)
      }else if(c.LT().size()==2){
        new LeftShift(left,right)
      }else{
        new RightShift(left,right)
      }
  }

  override def generate(expressionContext: ExpressionContext, ruleContext: ShiftExpressionContext): Expression = {
      ruleContext match {
        case c:AddExprContext⇒
          AdditiveGenerator.generate(expressionContext,c.additiveExpression())
        case c:ShiftOpExprContext⇒
          val left = generate(expressionContext, c.shiftExpression())
          val right = AdditiveGenerator.generate(expressionContext,c.additiveExpression())
          generator(left,right,c.shiftOperation())
        case c:ShiftOpParenExprContext⇒
          val left = generate(expressionContext,c.shiftExpression())
          val right = ParenGenerator.generate(expressionContext,c.parenExpression())
          generator(left,right,c.shiftOperation())
        case c:ParenShiftOpExprContext⇒
          val left = ParenGenerator.generate(expressionContext,c.parenExpression())
          val right = generate(expressionContext, c.shiftExpression())
          generator(left,right,c.shiftOperation())
        case c:ParenShiftOpParenExprContext⇒
          val left = ParenGenerator.generate(expressionContext,c.parenExpression().head)
          val right = ParenGenerator.generate(expressionContext,c.parenExpression().last)
          generator(left,right,c.shiftOperation())
      }
  }
}

object AdditiveGenerator extends IExpressionGenerator[AdditiveExpressionContext,Expression]{

  def generator(left: Expression, right: Expression, c: AdditiveOperationContext): Expression = {
    if (c.ADD() != null) {
      new Add( left, right )
    } else {
      new Sub( left, right )
    }
  }

  override def generate(expressionContext:ExpressionContext,
                        ruleContext: AdditiveExpressionContext): Expression = {
      ruleContext match {
        case c:MultiExprContext⇒
            MultiplicativeGenerator.generate(expressionContext,c.multiplicativeExpression())
        case c:AddOpExprContext⇒
           val left = generate(expressionContext,c.additiveExpression())
           val right = MultiplicativeGenerator.generate(expressionContext,c.multiplicativeExpression())
           generator(left,right,c.additiveOperation())
        case c:AddOpParenExprContext⇒
            val left = generate(expressionContext,c.additiveExpression())
            val right = ParenGenerator.generate(expressionContext,c.parenExpression())
            generator(left,right,c.additiveOperation())
        case c:ParenAddOpExprContext⇒
            val left = ParenGenerator.generate(expressionContext,c.parenExpression())
            val right = generate(expressionContext,c.additiveExpression())
            generator(left,right,c.additiveOperation())
        case c:ParenAddOpParenExprContext⇒
            val left = ParenGenerator.generate(expressionContext,c.parenExpression().head)
            val right = ParenGenerator.generate(expressionContext,c.parenExpression().last)
            generator(left,right,c.additiveOperation())
      }
  }
}

object MultiplicativeGenerator extends IExpressionGenerator[MultiplicativeExpressionContext,Expression]{

  def generator(left:Expression,right:Expression,c:MultiplicativeOperationContext): Expression ={
    if (c.DIV( ) != null) {
      new Div( left, right )
    } else if (c.MOD( ) != null) {
      new Mod( left, right )
    } else {
      new Mul( left, right )
    }
  }

  override def generate(expressionContext:ExpressionContext,
                        ruleContext: MultiplicativeExpressionContext): Expression = {
      ruleContext match {
        case c:UnaryExprContext⇒
            UnaryIExpressionGenerator.generate(expressionContext,c.unaryExpression())
        case c:MultiOpExprContext⇒
            generator(generate(expressionContext, c.multiplicativeExpression()),
              UnaryIExpressionGenerator.generate(expressionContext,c.unaryExpression()),
              c.multiplicativeOperation())
        case c:MultiOpParenExprContext⇒
            val left = generate(expressionContext, c.multiplicativeExpression())
            val right = ParenGenerator.generate(expressionContext,c.parenExpression())
            generator(left,right,c.multiplicativeOperation())
        case c:ParenMultiOpExprContext⇒
            val left = ParenGenerator.generate(expressionContext,c.parenExpression())
            val right = generate(expressionContext, c.multiplicativeExpression())
            generator(left,right,c.multiplicativeOperation())
        case c:ParenMultiOpParenExprContext⇒
            val left = ParenGenerator.generate(expressionContext,c.parenExpression().head)
            val right = ParenGenerator.generate(expressionContext,c.parenExpression().last)
            generator(left,right,c.multiplicativeOperation())
      }
  }
}
