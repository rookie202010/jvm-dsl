package com.dongjiaqiang.jvm.dsl.core.expression.generator

import com.dongjiaqiang.jvm.dsl.core.JvmDslParserParser._
import com.dongjiaqiang.jvm.dsl.core.expression._

import scala.collection.convert.ImplicitConversionsToScala._

object StatementGenerator extends IExpressionGenerator[StatementContext,Expression] {
  override def generate(expressionContext: ExpressionContext, ruleContext: StatementContext): Expression = {
     ruleContext match {
       case c:AssertExprContext⇒
          new Assert(OrGenerator.generate(expressionContext,c.assertStatement().conditionalOrExpression()))
       case _:ContinueExprContext⇒
         Continue
       case c:AssignExprContext⇒
         AssignGenerator.generate(expressionContext,c.assignment())
       case c:BlockExprContext⇒
         BlockGenerator.generate(expressionContext,c.block())
       case _:BreakExprContext⇒
          Break
       case c:DoWhileExprContext⇒
          new DoWhile(OrGenerator.generate(expressionContext,c.doWhileStatement().conditionalOrExpression()),
            BlockGenerator.generate(expressionContext,c.doWhileStatement().block()).expressions)
       case c:WhileExprContext⇒
          new While( OrGenerator.generate( expressionContext, c.whileStatement().conditionalOrExpression( ) ),
            BlockGenerator.generate( expressionContext, c.whileStatement().block( ) ).expressions )
       case c:SyncExprContext⇒
          new Synchronized(ExpressionGenerator.generate(expressionContext,c.synchronizedStatement().expression()),
            BlockGenerator.generate( expressionContext, c.synchronizedStatement().block( ) ).expressions )
       case c:IfExprContext⇒
            IfStatementGenerator.generate(expressionContext,c.ifStatement())
       case c:ThrowOrSideEffectExprContext⇒
            if(c.throwReturnOrSideEffectStatement().THROW()!=null){
                new Throw(ExpressionGenerator.generate(expressionContext,
                  c.throwReturnOrSideEffectStatement().expression()))
            }else{
                new Return( ExpressionGenerator.generate( expressionContext,
                  c.throwReturnOrSideEffectStatement( ).expression( ) ) )
            }
       case c:ForExprContext⇒
          ForStatementGenerator.generate(expressionContext,c)
     }
  }
}

object AssignGenerator extends IExpressionGenerator[AssignmentContext,Assign] {
  override def generate(expressionContext: ExpressionContext, ruleContext: AssignmentContext): Assign = {
    if (ruleContext.variable( ) != null) {
      val variable = VariableGenerator.generate( expressionContext, ruleContext.variable( ) )
      new Assign( variable, ExpressionGenerator.generate( expressionContext, ruleContext.expression( ) ) )
    } else if (ruleContext.arrayVariable( ) != null) {
      val variable = VariableGenerator.generate( expressionContext, ruleContext.arrayVariable( ).variable( ) )
      val indexExpr = ExpressionGenerator.generate( expressionContext, ruleContext.arrayVariable( ).expression( ) )
      new Assign( ArrayVarRef( indexExpr, variable.name, variable.fieldScope ),
        ExpressionGenerator.generate( expressionContext, ruleContext.expression( ) ) )
    } else {
      val variable = VariableGenerator.generate( expressionContext, ruleContext.mapVariable( ).variable( ) )
      val indexExpr = ExpressionGenerator.generate( expressionContext, ruleContext.mapVariable( ).expression( ) )
      new Assign( ArrayVarRef( indexExpr, variable.name, variable.fieldScope ),
        ExpressionGenerator.generate( expressionContext, ruleContext.expression( ) ) )
    }
  }
}

object ForStatementGenerator extends IExpressionGenerator[ForExprContext,Expression]{
  override def generate(expressionContext: ExpressionContext, ruleContext: ForExprContext): Expression = {
      ???
  }
}

object IfStatementGenerator extends IExpressionGenerator[IfStatementContext,Expression]{

  def generate(expressionContext: ExpressionContext,
                 conditions:Seq[ConditionalOrExpressionContext],
                conditionBlocks:Seq[BlockContext]):Option[Expression]={
        if(conditions.isEmpty){
            if(conditionBlocks.nonEmpty){
                Some(BlockGenerator.generate(expressionContext,conditionBlocks.head))
            } else{
                None
            }
        }else{
            Some(If(OrGenerator.generate(expressionContext,conditions.head),
              BlockGenerator.generate(expressionContext,conditionBlocks.head),
              generate(expressionContext,conditions.tail,conditionBlocks.tail)))
        }
  }

  override def generate(expressionContext: ExpressionContext, ruleContext: IfStatementContext): Expression = {
      val condition = OrGenerator.generate(expressionContext,ruleContext.conditionalOrExpression().head)
      val block = BlockGenerator.generate(expressionContext,ruleContext.block().head)
      If(condition,block,generate(expressionContext,ruleContext.conditionalOrExpression().tail,
        ruleContext.block().tail))

  }
}
