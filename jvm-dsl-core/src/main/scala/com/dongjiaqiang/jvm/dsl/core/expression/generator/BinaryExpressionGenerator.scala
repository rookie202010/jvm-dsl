package com.dongjiaqiang.jvm.dsl.core.expression.generator

import com.dongjiaqiang.jvm.dsl.core.JvmDslParserParser._
import com.dongjiaqiang.jvm.dsl.core.expression._
import com.dongjiaqiang.jvm.dsl.core.parser.ExprContext

import scala.collection.convert.ImplicitConversionsToScala._

object ParenGenerator extends IExpressionGenerator[ParenExpressionContext, Expression] {
  override def generate(exprContext: ExprContext, ruleContext: ParenExpressionContext): Expression = {
    new Paren( OrGenerator.generate( exprContext, ruleContext.conditionalOrExpression( ) ) )
  }
}

object OrGenerator extends IExpressionGenerator[ConditionalOrExpressionContext, Expression] {
  override def generate(exprContext: ExprContext, ruleContext: ConditionalOrExpressionContext): Expression = {
    ruleContext match {
      case c: ConditionalAndExprContext ⇒
        AndGenerator.generate( exprContext, c.conditionalAndExpression( ) )
      case c: OrOpExprContext ⇒
        new Or( generate( exprContext, c.conditionalOrExpression( ) ),
          AndGenerator.generate( exprContext, c.conditionalAndExpression( ) ) )
      case c: OrOpParenExprContext ⇒
        new Or( generate( exprContext, c.conditionalOrExpression( ) ),
          ParenGenerator.generate( exprContext, c.parenExpression( ) ) )
      case c: ParenOrOpExprContext ⇒
        new Or( ParenGenerator.generate( exprContext, c.parenExpression( ) ),
          generate( exprContext, c.conditionalOrExpression( ) ) )
      case c: ParenOrOpParenExprContext ⇒
        new Or( ParenGenerator.generate( exprContext, c.parenExpression( ).head ),
          ParenGenerator.generate( exprContext, c.parenExpression( ).last ) )
    }
  }
}

object AndGenerator extends IExpressionGenerator[ConditionalAndExpressionContext,Expression]{
  override def generate(exprContext: ExprContext, ruleContext: ConditionalAndExpressionContext): Expression = {
    ruleContext match {
      case c: InclusiveOrExprContext ⇒
        InclusiveOrGenerator.generate( exprContext, c.inclusiveOrExpression( ) )
      case c: AndOpExprContext ⇒
        new And( generate( exprContext, c.conditionalAndExpression( ) ),
          InclusiveOrGenerator.generate( exprContext, c.inclusiveOrExpression( ) ) )
      case c: AndOpParenExprContext ⇒
        new And( generate( exprContext, c.conditionalAndExpression( ) ),
          ParenGenerator.generate( exprContext, c.parenExpression( ) ) )
      case c: ParenAndOpExprContext ⇒
        new BitOr( ParenGenerator.generate( exprContext, c.parenExpression( ) ),
          generate( exprContext, c.conditionalAndExpression( ) ) )
      case c: ParenAndOpParenExprContext ⇒
        new BitOr( ParenGenerator.generate( exprContext, c.parenExpression( ).head ),
          ParenGenerator.generate( exprContext, c.parenExpression( ).last ) )
    }
  }
}

object InclusiveOrGenerator extends IExpressionGenerator[InclusiveOrExpressionContext,Expression]{
  override def generate(exprContext: ExprContext, ruleContext: InclusiveOrExpressionContext): Expression = {
    ruleContext match {
      case c: ExclusiveOrExprContext ⇒
        ExclusiveOrGenerator.generate( exprContext, c.exclusiveOrExpression( ) )
      case c: BitOrOpExprContext ⇒
        new BitOr( generate( exprContext, c.inclusiveOrExpression( ) ),
          ExclusiveOrGenerator.generate( exprContext, c.exclusiveOrExpression( ) ) )
      case c: BitOrParenOpExprContext ⇒
        new BitOr( generate( exprContext, c.inclusiveOrExpression( ) ),
          ParenGenerator.generate( exprContext, c.parenExpression( ) ) )
      case c: ParenBitOrOpExprContext ⇒
        new BitOr( ParenGenerator.generate( exprContext, c.parenExpression( ) ),
          generate( exprContext, c.inclusiveOrExpression( ) ) )
      case c: ParenBitOrOpParenExprContext ⇒
        new BitOr( ParenGenerator.generate( exprContext, c.parenExpression( ).last ),
          ParenGenerator.generate( exprContext, c.parenExpression( ).head ) )
    }
  }
}


object ExclusiveOrGenerator extends IExpressionGenerator[ExclusiveOrExpressionContext,Expression]{
  override def generate(exprContext: ExprContext, ruleContext: ExclusiveOrExpressionContext): Expression = {
    ruleContext match {
      case c: BitAndExprContext ⇒
        BitAndGenerator.generate( exprContext, c.bitAndExpression( ) )
      case c: CaretOpExprContext ⇒
        val left = generate( exprContext, c.exclusiveOrExpression( ) )
        val right = BitAndGenerator.generate( exprContext, c.bitAndExpression( ) )
        new Caret( left, right )
      case c: CaretOpParenExprContext ⇒
        val left = generate( exprContext, c.exclusiveOrExpression( ) )
        val right = ParenGenerator.generate( exprContext, c.parenExpression( ) )
        new Caret( left, right )
      case c: ParenCaretOpExprContext ⇒
        val left = ParenGenerator.generate( exprContext, c.parenExpression( ) )
        val right = generate( exprContext, c.exclusiveOrExpression( ) )
        new Caret( left, right )
      case c: ParenCaretOpParenExprContext ⇒
        val left = ParenGenerator.generate( exprContext, c.parenExpression( ).head )
        val right = ParenGenerator.generate( exprContext, c.parenExpression( ).last )
        new Caret( left, right )
    }
  }
}

object BitAndGenerator extends IExpressionGenerator[BitAndExpressionContext,Expression]{
  override def generate(exprContext: ExprContext, ruleContext: BitAndExpressionContext): Expression = {
    ruleContext match {
      case c: EualityExprContext ⇒
        EqualityGenerator.generate( exprContext, c.equalityExpression( ) )
      case c: BitAndOpExprContext ⇒
        val left = generate( exprContext, c.bitAndExpression( ) )
        val right = EqualityGenerator.generate( exprContext, c.equalityExpression( ) )
        new BitAnd( left, right )
      case c: BitAndOpParenExprContext ⇒
        val left = generate( exprContext, c.bitAndExpression( ) )
        val right = ParenGenerator.generate( exprContext, c.parenExpression( ) )
        new BitAnd( left, right )
      case c: ParenBitAnOpExprContext ⇒
        val left = ParenGenerator.generate( exprContext, c.parenExpression( ) )
        val right = generate( exprContext, c.bitAndExpression( ) )
        new BitAnd( left, right )
      case c: ParenBitAndOpParenExprContext ⇒
        val left = ParenGenerator.generate( exprContext, c.parenExpression( ).head )
        val right = ParenGenerator.generate( exprContext, c.parenExpression( ).last )
        new BitAnd( left, right )
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

  override def generate(exprContext: ExprContext, ruleContext: EqualityExpressionContext): Expression = {
    ruleContext match {
      case c: RelationExprContext ⇒
        RelationGenerator.generate( exprContext, c.relationExpression( ) )
      case c: EqualExprContext ⇒
        val left = generate( exprContext, c.equalityExpression( ) )
        val right = RelationGenerator.generate( exprContext, c.relationExpression( ) )
        generator( left, right, c.equalityOperation( ) )
      case c: EqualParenExprContext ⇒
        val left = generate( exprContext, c.equalityExpression( ) )
        val right = ParenGenerator.generate( exprContext, c.parenExpression( ) )
        generator( left, right, c.equalityOperation( ) )
      case c: ParenEqualExprContext ⇒
        val left = ParenGenerator.generate( exprContext, c.parenExpression( ) )
        val right = generate( exprContext, c.equalityExpression( ) )
        generator( left, right, c.equalityOperation( ) )
      case c: ParenEqualParenExprContext ⇒
        val left = ParenGenerator.generate( exprContext, c.parenExpression( ).head )
        val right = ParenGenerator.generate( exprContext, c.parenExpression( ).last )
        generator( left, right, c.equalityOperation( ) )
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

  override def generate(exprContext: ExprContext, ruleContext: RelationExpressionContext): Expression = {
    ruleContext match {
      case c: ShiftExprContext ⇒
        ShiftGenerator.generate( exprContext, c.shiftExpression( ) )
      case c: RelationOpExprContext ⇒
        val left = generate( exprContext, c.relationExpression( ) )
        val right = ShiftGenerator.generate( exprContext, c.shiftExpression( ) )
        generator( left, right, c.relationOperation( ) )
      case c: RelationOpParenExprContext ⇒
        val left = generate( exprContext, c.relationExpression( ) )
        val right = ParenGenerator.generate( exprContext, c.parenExpression( ) )
        generator( left, right, c.relationOperation( ) )
      case c: ParenRelationOpExprContext ⇒
        val left = ParenGenerator.generate( exprContext, c.parenExpression( ) )
        val right = generate( exprContext, c.relationExpression( ) )
        generator( left, right, c.relationOperation( ) )
      case c: ParenRelationOpParenExprContext ⇒
        val left = ParenGenerator.generate( exprContext, c.parenExpression( ).head )
        val right = ParenGenerator.generate( exprContext, c.parenExpression( ).last )
        generator( left, right, c.relationOperation( ) )
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

  override def generate(exprContext: ExprContext, ruleContext: ShiftExpressionContext): Expression = {
    ruleContext match {
      case c: AddExprContext ⇒
        AdditiveGenerator.generate( exprContext, c.additiveExpression( ) )
      case c: ShiftOpExprContext ⇒
        val left = generate( exprContext, c.shiftExpression( ) )
        val right = AdditiveGenerator.generate( exprContext, c.additiveExpression( ) )
        generator( left, right, c.shiftOperation( ) )
      case c: ShiftOpParenExprContext ⇒
        val left = generate( exprContext, c.shiftExpression( ) )
        val right = ParenGenerator.generate( exprContext, c.parenExpression( ) )
        generator( left, right, c.shiftOperation( ) )
      case c: ParenShiftOpExprContext ⇒
        val left = ParenGenerator.generate( exprContext, c.parenExpression( ) )
        val right = generate( exprContext, c.shiftExpression( ) )
        generator( left, right, c.shiftOperation( ) )
      case c: ParenShiftOpParenExprContext ⇒
        val left = ParenGenerator.generate( exprContext, c.parenExpression( ).head )
        val right = ParenGenerator.generate( exprContext, c.parenExpression( ).last )
        generator( left, right, c.shiftOperation( ) )
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

  override def generate(exprContext: ExprContext,
                        ruleContext: AdditiveExpressionContext): Expression = {
    ruleContext match {
      case c: MultiExprContext ⇒
        MultiplicativeGenerator.generate( exprContext, c.multiplicativeExpression( ) )
      case c: AddOpExprContext ⇒
        val left = generate( exprContext, c.additiveExpression( ) )
        val right = MultiplicativeGenerator.generate( exprContext, c.multiplicativeExpression( ) )
        generator( left, right, c.additiveOperation( ) )
      case c: AddOpParenExprContext ⇒
        val left = generate( exprContext, c.additiveExpression( ) )
        val right = ParenGenerator.generate( exprContext, c.parenExpression( ) )
        generator( left, right, c.additiveOperation( ) )
      case c: ParenAddOpExprContext ⇒
        val left = ParenGenerator.generate( exprContext, c.parenExpression( ) )
        val right = generate( exprContext, c.additiveExpression( ) )
        generator( left, right, c.additiveOperation( ) )
      case c: ParenAddOpParenExprContext ⇒
        val left = ParenGenerator.generate( exprContext, c.parenExpression( ).head )
        val right = ParenGenerator.generate( exprContext, c.parenExpression( ).last )
        generator( left, right, c.additiveOperation( ) )
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

  override def generate(exprContext: ExprContext,
                        ruleContext: MultiplicativeExpressionContext): Expression = {
    ruleContext match {
      case c: UnaryExprContext ⇒
        UnaryExpressionGenerator.generate( exprContext, c.unaryExpression( ) )
      case c: MultiOpExprContext ⇒
        generator( generate( exprContext, c.multiplicativeExpression( ) ),
          UnaryExpressionGenerator.generate( exprContext, c.unaryExpression( ) ),
          c.multiplicativeOperation( ) )
      case c: MultiOpParenExprContext ⇒
        val left = generate( exprContext, c.multiplicativeExpression( ) )
        val right = ParenGenerator.generate( exprContext, c.parenExpression( ) )
        generator( left, right, c.multiplicativeOperation( ) )
      case c: ParenMultiOpExprContext ⇒
        val left = ParenGenerator.generate( exprContext, c.parenExpression( ) )
        val right = generate( exprContext, c.multiplicativeExpression( ) )
        generator( left, right, c.multiplicativeOperation( ) )
      case c: ParenMultiOpParenExprContext ⇒
        val left = ParenGenerator.generate( exprContext, c.parenExpression( ).head )
        val right = ParenGenerator.generate( exprContext, c.parenExpression( ).last )
        generator( left, right, c.multiplicativeOperation( ) )
      }
  }
}
