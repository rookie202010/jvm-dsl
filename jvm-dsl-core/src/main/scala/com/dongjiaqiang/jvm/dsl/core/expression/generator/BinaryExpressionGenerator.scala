package com.dongjiaqiang.jvm.dsl.core.expression.generator

import com.dongjiaqiang.jvm.dsl.api.expression.binary._
import com.dongjiaqiang.jvm.dsl.api.expression.unary.Paren
import com.dongjiaqiang.jvm.dsl.api.expression._
import com.dongjiaqiang.jvm.dsl.core.JvmDslParserParser._
import com.dongjiaqiang.jvm.dsl.core.parser.ExprContext

import scala.collection.convert.ImplicitConversionsToScala._

object ParenGenerator extends IExpressionGenerator[ParenExpressionContext, ValueExpression,GeneratorContext] {
  override def generate(exprContext: ExprContext,
                        ruleContext: ParenExpressionContext,
                        generatorContext: GeneratorContext=NoneGeneratorContext): ValueExpression = {
    Paren( OrGenerator.generate( exprContext, ruleContext.conditionalOrExpression( ) ) )
  }
}

object OrGenerator extends IExpressionGenerator[ConditionalOrExpressionContext, ValueExpression,GeneratorContext] {
  override def generate(exprContext: ExprContext,
                        ruleContext: ConditionalOrExpressionContext,
                        generatorContext: GeneratorContext=NoneGeneratorContext): ValueExpression = {
    ruleContext match {
      case c: ConditionalAndExprContext ⇒
        AndGenerator.generate( exprContext, c.conditionalAndExpression( ) )
      case c: OrOpExprContext ⇒
        Or( generate( exprContext, c.conditionalOrExpression( ) ),
          AndGenerator.generate( exprContext, c.conditionalAndExpression( ) ) )
      case c: OrOpParenExprContext ⇒
        binary.Or( generate( exprContext, c.conditionalOrExpression( ) ),
          ParenGenerator.generate( exprContext, c.parenExpression( ) ) )
      case c: ParenOrOpExprContext ⇒
        binary.Or( ParenGenerator.generate( exprContext, c.parenExpression( ) ),
          generate( exprContext, c.conditionalOrExpression( ) ) )
      case c: ParenOrOpParenExprContext ⇒
        binary.Or( ParenGenerator.generate( exprContext, c.parenExpression( ).head ),
          ParenGenerator.generate( exprContext, c.parenExpression( ).last ) )
    }
  }
}

object AndGenerator extends IExpressionGenerator[ConditionalAndExpressionContext,ValueExpression,GeneratorContext]{
  override def generate(exprContext: ExprContext,
                        ruleContext: ConditionalAndExpressionContext,
                        generatorContext: GeneratorContext=NoneGeneratorContext): ValueExpression = {
    ruleContext match {
      case c: InclusiveOrExprContext ⇒
        InclusiveOrGenerator.generate( exprContext, c.inclusiveOrExpression( ) )
      case c: AndOpExprContext ⇒
        And( generate( exprContext, c.conditionalAndExpression( ) ),
          InclusiveOrGenerator.generate( exprContext, c.inclusiveOrExpression( ) ) )
      case c: AndOpParenExprContext ⇒
        binary.And( generate( exprContext, c.conditionalAndExpression( ) ),
          ParenGenerator.generate( exprContext, c.parenExpression( ) ) )
      case c: ParenAndOpExprContext ⇒
        BitOr( ParenGenerator.generate( exprContext, c.parenExpression( ) ),
          generate( exprContext, c.conditionalAndExpression( ) ) )
      case c: ParenAndOpParenExprContext ⇒
        binary.BitOr( ParenGenerator.generate( exprContext, c.parenExpression( ).head ),
          ParenGenerator.generate( exprContext, c.parenExpression( ).last ) )
    }
  }
}

object InclusiveOrGenerator extends IExpressionGenerator[InclusiveOrExpressionContext,ValueExpression,GeneratorContext]{
  override def generate(exprContext: ExprContext,
                        ruleContext: InclusiveOrExpressionContext,
                        generatorContext: GeneratorContext=NoneGeneratorContext): ValueExpression = {
    ruleContext match {
      case c: ExclusiveOrExprContext ⇒
        ExclusiveOrGenerator.generate( exprContext, c.exclusiveOrExpression( ) )
      case c: BitOrOpExprContext ⇒
        binary.BitOr( generate( exprContext, c.inclusiveOrExpression( ) ),
          ExclusiveOrGenerator.generate( exprContext, c.exclusiveOrExpression( ) ) )
      case c: BitOrParenOpExprContext ⇒
        binary.BitOr( generate( exprContext, c.inclusiveOrExpression( ) ),
          ParenGenerator.generate( exprContext, c.parenExpression( ) ) )
      case c: ParenBitOrOpExprContext ⇒
        binary.BitOr( ParenGenerator.generate( exprContext, c.parenExpression( ) ),
          generate( exprContext, c.inclusiveOrExpression( ) ) )
      case c: ParenBitOrOpParenExprContext ⇒
        binary.BitOr( ParenGenerator.generate( exprContext, c.parenExpression( ).last ),
          ParenGenerator.generate( exprContext, c.parenExpression( ).head ) )
    }
  }
}


object ExclusiveOrGenerator extends IExpressionGenerator[ExclusiveOrExpressionContext,ValueExpression,GeneratorContext]{
  override def generate(exprContext: ExprContext,
                        ruleContext: ExclusiveOrExpressionContext,
                        generatorContext: GeneratorContext=NoneGeneratorContext): ValueExpression = {
    ruleContext match {
      case c: BitAndExprContext ⇒
        BitAndGenerator.generate( exprContext, c.bitAndExpression( ) )
      case c: CaretOpExprContext ⇒
        val left = generate( exprContext, c.exclusiveOrExpression( ) )
        val right = BitAndGenerator.generate( exprContext, c.bitAndExpression( ) )
        Caret( left, right )
      case c: CaretOpParenExprContext ⇒
        val left = generate( exprContext, c.exclusiveOrExpression( ) )
        val right = ParenGenerator.generate( exprContext, c.parenExpression( ) )
        binary.Caret( left, right )
      case c: ParenCaretOpExprContext ⇒
        val left = ParenGenerator.generate( exprContext, c.parenExpression( ) )
        val right = generate( exprContext, c.exclusiveOrExpression( ) )
        binary.Caret( left, right )
      case c: ParenCaretOpParenExprContext ⇒
        val left = ParenGenerator.generate( exprContext, c.parenExpression( ).head )
        val right = ParenGenerator.generate( exprContext, c.parenExpression( ).last )
        binary.Caret( left, right )
    }
  }
}

object BitAndGenerator extends IExpressionGenerator[BitAndExpressionContext,ValueExpression,GeneratorContext]{
  override def generate(exprContext: ExprContext,
                        ruleContext: BitAndExpressionContext,
                        generatorContext: GeneratorContext=NoneGeneratorContext): ValueExpression = {
    ruleContext match {
      case c: EualityExprContext ⇒
        EqualityGenerator.generate( exprContext, c.equalityExpression( ) )
      case c: BitAndOpExprContext ⇒
        val left = generate( exprContext, c.bitAndExpression( ) )
        val right = EqualityGenerator.generate( exprContext, c.equalityExpression( ) )
        BitAnd( left, right )
      case c: BitAndOpParenExprContext ⇒
        val left = generate( exprContext, c.bitAndExpression( ) )
        val right = ParenGenerator.generate( exprContext, c.parenExpression( ) )
        binary.BitAnd( left, right )
      case c: ParenBitAnOpExprContext ⇒
        val left = ParenGenerator.generate( exprContext, c.parenExpression( ) )
        val right = generate( exprContext, c.bitAndExpression( ) )
        binary.BitAnd( left, right )
      case c: ParenBitAndOpParenExprContext ⇒
        val left = ParenGenerator.generate( exprContext, c.parenExpression( ).head )
        val right = ParenGenerator.generate( exprContext, c.parenExpression( ).last )
        binary.BitAnd( left, right )
      }
  }
}

object EqualityGenerator extends IExpressionGenerator[EqualityExpressionContext,ValueExpression,GeneratorContext]{

  def generator(left: ValueExpression,
                right: ValueExpression,
                c: EqualityOperationContext): ValueExpression = {
    if (c.EQUAL()!=null) {
      Eq( left, right )
    } else{
      NotEq( left, right )
    }
  }

  override def generate(exprContext: ExprContext,
                        ruleContext: EqualityExpressionContext,
                        generatorContext: GeneratorContext=NoneGeneratorContext): ValueExpression = {
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

object RelationGenerator extends IExpressionGenerator[RelationExpressionContext,ValueExpression,GeneratorContext]{

  def generator(left: ValueExpression,
                right: ValueExpression,
                c: RelationOperationContext): ValueExpression = {
    if (c.LT()!=null) {
      Lt( left, right )
    } else if (c.GT()!=null) {
      Gt( left, right )
    } else if(c.LE()!=null){
      Le( left, right )
    }else{
      Ge( left, right )
    }
  }

  override def generate(exprContext: ExprContext,
                        ruleContext: RelationExpressionContext,
                        generatorContext: GeneratorContext=NoneGeneratorContext): ValueExpression = {
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

object ShiftGenerator extends IExpressionGenerator[ShiftExpressionContext,ValueExpression,GeneratorContext]{

  def generator(left:ValueExpression,
                right:ValueExpression,
                c:ShiftOperationContext):ValueExpression={
      if(c.GT().size()==3){
        UnsignedRightShift( left, right )
      }else if(c.LT().size()==2){
        LeftShift( left, right )
      }else{
        RightShift( left, right )
      }
  }

  override def generate(exprContext: ExprContext,
                        ruleContext: ShiftExpressionContext,
                        generatorContext: GeneratorContext=NoneGeneratorContext): ValueExpression = {
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

object AdditiveGenerator extends IExpressionGenerator[AdditiveExpressionContext,ValueExpression,GeneratorContext]{

  def generator(left: ValueExpression,
                right: ValueExpression,
                c: AdditiveOperationContext): ValueExpression = {
    if (c.ADD() != null) {
      Add( left, right )
    } else {
      Sub( left, right )
    }
  }

  override def generate(exprContext: ExprContext,
                        ruleContext: AdditiveExpressionContext,
                        generatorContext: GeneratorContext = NoneGeneratorContext): ValueExpression = {
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

object MultiplicativeGenerator extends IExpressionGenerator[MultiplicativeExpressionContext,ValueExpression,GeneratorContext]{

  def generator(left:ValueExpression,
                right:ValueExpression,
                c:MultiplicativeOperationContext): ValueExpression ={
    if (c.DIV( ) != null) {
      Div( left, right )
    } else if (c.MOD( ) != null) {
      Mod( left, right )
    } else {
      Mul( left, right )
    }
  }

  override def generate(exprContext: ExprContext,
                        ruleContext: MultiplicativeExpressionContext,
                        generatorContext: GeneratorContext = NoneGeneratorContext): ValueExpression = {
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
