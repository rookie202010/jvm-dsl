// Generated from java-escape by ANTLR 4.11.1
package com.dongjiaqiang.jvm.dsl.core;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link JvmDslParserParser}.
 */
public interface JvmDslParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(JvmDslParserParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(JvmDslParserParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#member}.
	 * @param ctx the parse tree
	 */
	void enterMember(JvmDslParserParser.MemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#member}.
	 * @param ctx the parse tree
	 */
	void exitMember(JvmDslParserParser.MemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(JvmDslParserParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(JvmDslParserParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#blockStatements}.
	 * @param ctx the parse tree
	 */
	void enterBlockStatements(JvmDslParserParser.BlockStatementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#blockStatements}.
	 * @param ctx the parse tree
	 */
	void exitBlockStatements(JvmDslParserParser.BlockStatementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#blockStatement}.
	 * @param ctx the parse tree
	 */
	void enterBlockStatement(JvmDslParserParser.BlockStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#blockStatement}.
	 * @param ctx the parse tree
	 */
	void exitBlockStatement(JvmDslParserParser.BlockStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(JvmDslParserParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(JvmDslParserParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#synchronizedStatement}.
	 * @param ctx the parse tree
	 */
	void enterSynchronizedStatement(JvmDslParserParser.SynchronizedStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#synchronizedStatement}.
	 * @param ctx the parse tree
	 */
	void exitSynchronizedStatement(JvmDslParserParser.SynchronizedStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#throwStatement}.
	 * @param ctx the parse tree
	 */
	void enterThrowStatement(JvmDslParserParser.ThrowStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#throwStatement}.
	 * @param ctx the parse tree
	 */
	void exitThrowStatement(JvmDslParserParser.ThrowStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#returnStatement}.
	 * @param ctx the parse tree
	 */
	void enterReturnStatement(JvmDslParserParser.ReturnStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#returnStatement}.
	 * @param ctx the parse tree
	 */
	void exitReturnStatement(JvmDslParserParser.ReturnStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#breakStatement}.
	 * @param ctx the parse tree
	 */
	void enterBreakStatement(JvmDslParserParser.BreakStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#breakStatement}.
	 * @param ctx the parse tree
	 */
	void exitBreakStatement(JvmDslParserParser.BreakStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#continueStatement}.
	 * @param ctx the parse tree
	 */
	void enterContinueStatement(JvmDslParserParser.ContinueStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#continueStatement}.
	 * @param ctx the parse tree
	 */
	void exitContinueStatement(JvmDslParserParser.ContinueStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#importClazzStatement}.
	 * @param ctx the parse tree
	 */
	void enterImportClazzStatement(JvmDslParserParser.ImportClazzStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#importClazzStatement}.
	 * @param ctx the parse tree
	 */
	void exitImportClazzStatement(JvmDslParserParser.ImportClazzStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#usingJarStatement}.
	 * @param ctx the parse tree
	 */
	void enterUsingJarStatement(JvmDslParserParser.UsingJarStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#usingJarStatement}.
	 * @param ctx the parse tree
	 */
	void exitUsingJarStatement(JvmDslParserParser.UsingJarStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#assertStatement}.
	 * @param ctx the parse tree
	 */
	void enterAssertStatement(JvmDslParserParser.AssertStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#assertStatement}.
	 * @param ctx the parse tree
	 */
	void exitAssertStatement(JvmDslParserParser.AssertStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#tryStatement}.
	 * @param ctx the parse tree
	 */
	void enterTryStatement(JvmDslParserParser.TryStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#tryStatement}.
	 * @param ctx the parse tree
	 */
	void exitTryStatement(JvmDslParserParser.TryStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#catches}.
	 * @param ctx the parse tree
	 */
	void enterCatches(JvmDslParserParser.CatchesContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#catches}.
	 * @param ctx the parse tree
	 */
	void exitCatches(JvmDslParserParser.CatchesContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#catcheClause}.
	 * @param ctx the parse tree
	 */
	void enterCatcheClause(JvmDslParserParser.CatcheClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#catcheClause}.
	 * @param ctx the parse tree
	 */
	void exitCatcheClause(JvmDslParserParser.CatcheClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#whileStatement}.
	 * @param ctx the parse tree
	 */
	void enterWhileStatement(JvmDslParserParser.WhileStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#whileStatement}.
	 * @param ctx the parse tree
	 */
	void exitWhileStatement(JvmDslParserParser.WhileStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#doWhileStatement}.
	 * @param ctx the parse tree
	 */
	void enterDoWhileStatement(JvmDslParserParser.DoWhileStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#doWhileStatement}.
	 * @param ctx the parse tree
	 */
	void exitDoWhileStatement(JvmDslParserParser.DoWhileStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ForStatementOne}
	 * labeled alternative in {@link JvmDslParserParser#forStatement}.
	 * @param ctx the parse tree
	 */
	void enterForStatementOne(JvmDslParserParser.ForStatementOneContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ForStatementOne}
	 * labeled alternative in {@link JvmDslParserParser#forStatement}.
	 * @param ctx the parse tree
	 */
	void exitForStatementOne(JvmDslParserParser.ForStatementOneContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ForStatementTwo}
	 * labeled alternative in {@link JvmDslParserParser#forStatement}.
	 * @param ctx the parse tree
	 */
	void enterForStatementTwo(JvmDslParserParser.ForStatementTwoContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ForStatementTwo}
	 * labeled alternative in {@link JvmDslParserParser#forStatement}.
	 * @param ctx the parse tree
	 */
	void exitForStatementTwo(JvmDslParserParser.ForStatementTwoContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ForStatementThree}
	 * labeled alternative in {@link JvmDslParserParser#forStatement}.
	 * @param ctx the parse tree
	 */
	void enterForStatementThree(JvmDslParserParser.ForStatementThreeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ForStatementThree}
	 * labeled alternative in {@link JvmDslParserParser#forStatement}.
	 * @param ctx the parse tree
	 */
	void exitForStatementThree(JvmDslParserParser.ForStatementThreeContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#ifStatement}.
	 * @param ctx the parse tree
	 */
	void enterIfStatement(JvmDslParserParser.IfStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#ifStatement}.
	 * @param ctx the parse tree
	 */
	void exitIfStatement(JvmDslParserParser.IfStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(JvmDslParserParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(JvmDslParserParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#assignment}.
	 * @param ctx the parse tree
	 */
	void enterAssignment(JvmDslParserParser.AssignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#assignment}.
	 * @param ctx the parse tree
	 */
	void exitAssignment(JvmDslParserParser.AssignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#assignOperator}.
	 * @param ctx the parse tree
	 */
	void enterAssignOperator(JvmDslParserParser.AssignOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#assignOperator}.
	 * @param ctx the parse tree
	 */
	void exitAssignOperator(JvmDslParserParser.AssignOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#conditionalOrExpression}.
	 * @param ctx the parse tree
	 */
	void enterConditionalOrExpression(JvmDslParserParser.ConditionalOrExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#conditionalOrExpression}.
	 * @param ctx the parse tree
	 */
	void exitConditionalOrExpression(JvmDslParserParser.ConditionalOrExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#conditionalAndExpression}.
	 * @param ctx the parse tree
	 */
	void enterConditionalAndExpression(JvmDslParserParser.ConditionalAndExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#conditionalAndExpression}.
	 * @param ctx the parse tree
	 */
	void exitConditionalAndExpression(JvmDslParserParser.ConditionalAndExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#inclusiveOrExpression}.
	 * @param ctx the parse tree
	 */
	void enterInclusiveOrExpression(JvmDslParserParser.InclusiveOrExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#inclusiveOrExpression}.
	 * @param ctx the parse tree
	 */
	void exitInclusiveOrExpression(JvmDslParserParser.InclusiveOrExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#exclusiveOrExpression}.
	 * @param ctx the parse tree
	 */
	void enterExclusiveOrExpression(JvmDslParserParser.ExclusiveOrExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#exclusiveOrExpression}.
	 * @param ctx the parse tree
	 */
	void exitExclusiveOrExpression(JvmDslParserParser.ExclusiveOrExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#andExpression}.
	 * @param ctx the parse tree
	 */
	void enterAndExpression(JvmDslParserParser.AndExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#andExpression}.
	 * @param ctx the parse tree
	 */
	void exitAndExpression(JvmDslParserParser.AndExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Relation}
	 * labeled alternative in {@link JvmDslParserParser#equalityExpression}.
	 * @param ctx the parse tree
	 */
	void enterRelation(JvmDslParserParser.RelationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Relation}
	 * labeled alternative in {@link JvmDslParserParser#equalityExpression}.
	 * @param ctx the parse tree
	 */
	void exitRelation(JvmDslParserParser.RelationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NotEqual}
	 * labeled alternative in {@link JvmDslParserParser#equalityExpression}.
	 * @param ctx the parse tree
	 */
	void enterNotEqual(JvmDslParserParser.NotEqualContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NotEqual}
	 * labeled alternative in {@link JvmDslParserParser#equalityExpression}.
	 * @param ctx the parse tree
	 */
	void exitNotEqual(JvmDslParserParser.NotEqualContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Equal}
	 * labeled alternative in {@link JvmDslParserParser#equalityExpression}.
	 * @param ctx the parse tree
	 */
	void enterEqual(JvmDslParserParser.EqualContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Equal}
	 * labeled alternative in {@link JvmDslParserParser#equalityExpression}.
	 * @param ctx the parse tree
	 */
	void exitEqual(JvmDslParserParser.EqualContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Shift}
	 * labeled alternative in {@link JvmDslParserParser#relationExpression}.
	 * @param ctx the parse tree
	 */
	void enterShift(JvmDslParserParser.ShiftContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Shift}
	 * labeled alternative in {@link JvmDslParserParser#relationExpression}.
	 * @param ctx the parse tree
	 */
	void exitShift(JvmDslParserParser.ShiftContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Lt}
	 * labeled alternative in {@link JvmDslParserParser#relationExpression}.
	 * @param ctx the parse tree
	 */
	void enterLt(JvmDslParserParser.LtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Lt}
	 * labeled alternative in {@link JvmDslParserParser#relationExpression}.
	 * @param ctx the parse tree
	 */
	void exitLt(JvmDslParserParser.LtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Le}
	 * labeled alternative in {@link JvmDslParserParser#relationExpression}.
	 * @param ctx the parse tree
	 */
	void enterLe(JvmDslParserParser.LeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Le}
	 * labeled alternative in {@link JvmDslParserParser#relationExpression}.
	 * @param ctx the parse tree
	 */
	void exitLe(JvmDslParserParser.LeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Instanceof}
	 * labeled alternative in {@link JvmDslParserParser#relationExpression}.
	 * @param ctx the parse tree
	 */
	void enterInstanceof(JvmDslParserParser.InstanceofContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Instanceof}
	 * labeled alternative in {@link JvmDslParserParser#relationExpression}.
	 * @param ctx the parse tree
	 */
	void exitInstanceof(JvmDslParserParser.InstanceofContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Gt}
	 * labeled alternative in {@link JvmDslParserParser#relationExpression}.
	 * @param ctx the parse tree
	 */
	void enterGt(JvmDslParserParser.GtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Gt}
	 * labeled alternative in {@link JvmDslParserParser#relationExpression}.
	 * @param ctx the parse tree
	 */
	void exitGt(JvmDslParserParser.GtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Ge}
	 * labeled alternative in {@link JvmDslParserParser#relationExpression}.
	 * @param ctx the parse tree
	 */
	void enterGe(JvmDslParserParser.GeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Ge}
	 * labeled alternative in {@link JvmDslParserParser#relationExpression}.
	 * @param ctx the parse tree
	 */
	void exitGe(JvmDslParserParser.GeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Additive}
	 * labeled alternative in {@link JvmDslParserParser#shiftExpression}.
	 * @param ctx the parse tree
	 */
	void enterAdditive(JvmDslParserParser.AdditiveContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Additive}
	 * labeled alternative in {@link JvmDslParserParser#shiftExpression}.
	 * @param ctx the parse tree
	 */
	void exitAdditive(JvmDslParserParser.AdditiveContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RightShift}
	 * labeled alternative in {@link JvmDslParserParser#shiftExpression}.
	 * @param ctx the parse tree
	 */
	void enterRightShift(JvmDslParserParser.RightShiftContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RightShift}
	 * labeled alternative in {@link JvmDslParserParser#shiftExpression}.
	 * @param ctx the parse tree
	 */
	void exitRightShift(JvmDslParserParser.RightShiftContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LeftShift}
	 * labeled alternative in {@link JvmDslParserParser#shiftExpression}.
	 * @param ctx the parse tree
	 */
	void enterLeftShift(JvmDslParserParser.LeftShiftContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LeftShift}
	 * labeled alternative in {@link JvmDslParserParser#shiftExpression}.
	 * @param ctx the parse tree
	 */
	void exitLeftShift(JvmDslParserParser.LeftShiftContext ctx);
	/**
	 * Enter a parse tree produced by the {@code UnsignedRightShift}
	 * labeled alternative in {@link JvmDslParserParser#shiftExpression}.
	 * @param ctx the parse tree
	 */
	void enterUnsignedRightShift(JvmDslParserParser.UnsignedRightShiftContext ctx);
	/**
	 * Exit a parse tree produced by the {@code UnsignedRightShift}
	 * labeled alternative in {@link JvmDslParserParser#shiftExpression}.
	 * @param ctx the parse tree
	 */
	void exitUnsignedRightShift(JvmDslParserParser.UnsignedRightShiftContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Add}
	 * labeled alternative in {@link JvmDslParserParser#additiveExpression}.
	 * @param ctx the parse tree
	 */
	void enterAdd(JvmDslParserParser.AddContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Add}
	 * labeled alternative in {@link JvmDslParserParser#additiveExpression}.
	 * @param ctx the parse tree
	 */
	void exitAdd(JvmDslParserParser.AddContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Sub}
	 * labeled alternative in {@link JvmDslParserParser#additiveExpression}.
	 * @param ctx the parse tree
	 */
	void enterSub(JvmDslParserParser.SubContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Sub}
	 * labeled alternative in {@link JvmDslParserParser#additiveExpression}.
	 * @param ctx the parse tree
	 */
	void exitSub(JvmDslParserParser.SubContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Multiplicative}
	 * labeled alternative in {@link JvmDslParserParser#additiveExpression}.
	 * @param ctx the parse tree
	 */
	void enterMultiplicative(JvmDslParserParser.MultiplicativeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Multiplicative}
	 * labeled alternative in {@link JvmDslParserParser#additiveExpression}.
	 * @param ctx the parse tree
	 */
	void exitMultiplicative(JvmDslParserParser.MultiplicativeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Div}
	 * labeled alternative in {@link JvmDslParserParser#multiplicativeExpression}.
	 * @param ctx the parse tree
	 */
	void enterDiv(JvmDslParserParser.DivContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Div}
	 * labeled alternative in {@link JvmDslParserParser#multiplicativeExpression}.
	 * @param ctx the parse tree
	 */
	void exitDiv(JvmDslParserParser.DivContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Mod}
	 * labeled alternative in {@link JvmDslParserParser#multiplicativeExpression}.
	 * @param ctx the parse tree
	 */
	void enterMod(JvmDslParserParser.ModContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Mod}
	 * labeled alternative in {@link JvmDslParserParser#multiplicativeExpression}.
	 * @param ctx the parse tree
	 */
	void exitMod(JvmDslParserParser.ModContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Mul}
	 * labeled alternative in {@link JvmDslParserParser#multiplicativeExpression}.
	 * @param ctx the parse tree
	 */
	void enterMul(JvmDslParserParser.MulContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Mul}
	 * labeled alternative in {@link JvmDslParserParser#multiplicativeExpression}.
	 * @param ctx the parse tree
	 */
	void exitMul(JvmDslParserParser.MulContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Unary}
	 * labeled alternative in {@link JvmDslParserParser#multiplicativeExpression}.
	 * @param ctx the parse tree
	 */
	void enterUnary(JvmDslParserParser.UnaryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Unary}
	 * labeled alternative in {@link JvmDslParserParser#multiplicativeExpression}.
	 * @param ctx the parse tree
	 */
	void exitUnary(JvmDslParserParser.UnaryContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#unaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterUnaryExpression(JvmDslParserParser.UnaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#unaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitUnaryExpression(JvmDslParserParser.UnaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#lambdaExpression}.
	 * @param ctx the parse tree
	 */
	void enterLambdaExpression(JvmDslParserParser.LambdaExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#lambdaExpression}.
	 * @param ctx the parse tree
	 */
	void exitLambdaExpression(JvmDslParserParser.LambdaExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#unapplyExpression}.
	 * @param ctx the parse tree
	 */
	void enterUnapplyExpression(JvmDslParserParser.UnapplyExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#unapplyExpression}.
	 * @param ctx the parse tree
	 */
	void exitUnapplyExpression(JvmDslParserParser.UnapplyExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code VoidType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 */
	void enterVoidType(JvmDslParserParser.VoidTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code VoidType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 */
	void exitVoidType(JvmDslParserParser.VoidTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LambdaOneInOneOutType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 */
	void enterLambdaOneInOneOutType(JvmDslParserParser.LambdaOneInOneOutTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LambdaOneInOneOutType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 */
	void exitLambdaOneInOneOutType(JvmDslParserParser.LambdaOneInOneOutTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BoolType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 */
	void enterBoolType(JvmDslParserParser.BoolTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BoolType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 */
	void exitBoolType(JvmDslParserParser.BoolTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FutureType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 */
	void enterFutureType(JvmDslParserParser.FutureTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FutureType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 */
	void exitFutureType(JvmDslParserParser.FutureTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LongType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 */
	void enterLongType(JvmDslParserParser.LongTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LongType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 */
	void exitLongType(JvmDslParserParser.LongTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code StringType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 */
	void enterStringType(JvmDslParserParser.StringTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code StringType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 */
	void exitStringType(JvmDslParserParser.StringTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ParameterizedClassType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 */
	void enterParameterizedClassType(JvmDslParserParser.ParameterizedClassTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ParameterizedClassType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 */
	void exitParameterizedClassType(JvmDslParserParser.ParameterizedClassTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ByteType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 */
	void enterByteType(JvmDslParserParser.ByteTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ByteType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 */
	void exitByteType(JvmDslParserParser.ByteTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LambdaZeroInMoreOutType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 */
	void enterLambdaZeroInMoreOutType(JvmDslParserParser.LambdaZeroInMoreOutTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LambdaZeroInMoreOutType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 */
	void exitLambdaZeroInMoreOutType(JvmDslParserParser.LambdaZeroInMoreOutTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ClassType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 */
	void enterClassType(JvmDslParserParser.ClassTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ClassType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 */
	void exitClassType(JvmDslParserParser.ClassTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LambdaMoreInOneOutType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 */
	void enterLambdaMoreInOneOutType(JvmDslParserParser.LambdaMoreInOneOutTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LambdaMoreInOneOutType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 */
	void exitLambdaMoreInOneOutType(JvmDslParserParser.LambdaMoreInOneOutTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MapType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 */
	void enterMapType(JvmDslParserParser.MapTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MapType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 */
	void exitMapType(JvmDslParserParser.MapTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IntType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 */
	void enterIntType(JvmDslParserParser.IntTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IntType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 */
	void exitIntType(JvmDslParserParser.IntTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FloatType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 */
	void enterFloatType(JvmDslParserParser.FloatTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FloatType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 */
	void exitFloatType(JvmDslParserParser.FloatTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LambdaMoreInMoreOutType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 */
	void enterLambdaMoreInMoreOutType(JvmDslParserParser.LambdaMoreInMoreOutTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LambdaMoreInMoreOutType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 */
	void exitLambdaMoreInMoreOutType(JvmDslParserParser.LambdaMoreInMoreOutTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ArrayType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 */
	void enterArrayType(JvmDslParserParser.ArrayTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ArrayType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 */
	void exitArrayType(JvmDslParserParser.ArrayTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SetType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 */
	void enterSetType(JvmDslParserParser.SetTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SetType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 */
	void exitSetType(JvmDslParserParser.SetTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LambdaZeroInOneOutType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 */
	void enterLambdaZeroInOneOutType(JvmDslParserParser.LambdaZeroInOneOutTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LambdaZeroInOneOutType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 */
	void exitLambdaZeroInOneOutType(JvmDslParserParser.LambdaZeroInOneOutTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DoubleType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 */
	void enterDoubleType(JvmDslParserParser.DoubleTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DoubleType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 */
	void exitDoubleType(JvmDslParserParser.DoubleTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ListType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 */
	void enterListType(JvmDslParserParser.ListTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ListType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 */
	void exitListType(JvmDslParserParser.ListTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TupleType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 */
	void enterTupleType(JvmDslParserParser.TupleTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TupleType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 */
	void exitTupleType(JvmDslParserParser.TupleTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CharType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 */
	void enterCharType(JvmDslParserParser.CharTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CharType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 */
	void exitCharType(JvmDslParserParser.CharTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code OptionType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 */
	void enterOptionType(JvmDslParserParser.OptionTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code OptionType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 */
	void exitOptionType(JvmDslParserParser.OptionTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LambdaOneInMoreOutType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 */
	void enterLambdaOneInMoreOutType(JvmDslParserParser.LambdaOneInMoreOutTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LambdaOneInMoreOutType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 */
	void exitLambdaOneInMoreOutType(JvmDslParserParser.LambdaOneInMoreOutTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#types}.
	 * @param ctx the parse tree
	 */
	void enterTypes(JvmDslParserParser.TypesContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#types}.
	 * @param ctx the parse tree
	 */
	void exitTypes(JvmDslParserParser.TypesContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#clazzType}.
	 * @param ctx the parse tree
	 */
	void enterClazzType(JvmDslParserParser.ClazzTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#clazzType}.
	 * @param ctx the parse tree
	 */
	void exitClazzType(JvmDslParserParser.ClazzTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#varDef}.
	 * @param ctx the parse tree
	 */
	void enterVarDef(JvmDslParserParser.VarDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#varDef}.
	 * @param ctx the parse tree
	 */
	void exitVarDef(JvmDslParserParser.VarDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#classDef}.
	 * @param ctx the parse tree
	 */
	void enterClassDef(JvmDslParserParser.ClassDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#classDef}.
	 * @param ctx the parse tree
	 */
	void exitClassDef(JvmDslParserParser.ClassDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#funcCall}.
	 * @param ctx the parse tree
	 */
	void enterFuncCall(JvmDslParserParser.FuncCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#funcCall}.
	 * @param ctx the parse tree
	 */
	void exitFuncCall(JvmDslParserParser.FuncCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#singleFuncCall}.
	 * @param ctx the parse tree
	 */
	void enterSingleFuncCall(JvmDslParserParser.SingleFuncCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#singleFuncCall}.
	 * @param ctx the parse tree
	 */
	void exitSingleFuncCall(JvmDslParserParser.SingleFuncCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#funcDef}.
	 * @param ctx the parse tree
	 */
	void enterFuncDef(JvmDslParserParser.FuncDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#funcDef}.
	 * @param ctx the parse tree
	 */
	void exitFuncDef(JvmDslParserParser.FuncDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#fieldDef}.
	 * @param ctx the parse tree
	 */
	void enterFieldDef(JvmDslParserParser.FieldDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#fieldDef}.
	 * @param ctx the parse tree
	 */
	void exitFieldDef(JvmDslParserParser.FieldDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#literalAndFuncCall}.
	 * @param ctx the parse tree
	 */
	void enterLiteralAndFuncCall(JvmDslParserParser.LiteralAndFuncCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#literalAndFuncCall}.
	 * @param ctx the parse tree
	 */
	void exitLiteralAndFuncCall(JvmDslParserParser.LiteralAndFuncCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterLiteral(JvmDslParserParser.LiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitLiteral(JvmDslParserParser.LiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#baseLiteral}.
	 * @param ctx the parse tree
	 */
	void enterBaseLiteral(JvmDslParserParser.BaseLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#baseLiteral}.
	 * @param ctx the parse tree
	 */
	void exitBaseLiteral(JvmDslParserParser.BaseLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#numberLiteral}.
	 * @param ctx the parse tree
	 */
	void enterNumberLiteral(JvmDslParserParser.NumberLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#numberLiteral}.
	 * @param ctx the parse tree
	 */
	void exitNumberLiteral(JvmDslParserParser.NumberLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#listLiteral}.
	 * @param ctx the parse tree
	 */
	void enterListLiteral(JvmDslParserParser.ListLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#listLiteral}.
	 * @param ctx the parse tree
	 */
	void exitListLiteral(JvmDslParserParser.ListLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#asyncLiteral}.
	 * @param ctx the parse tree
	 */
	void enterAsyncLiteral(JvmDslParserParser.AsyncLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#asyncLiteral}.
	 * @param ctx the parse tree
	 */
	void exitAsyncLiteral(JvmDslParserParser.AsyncLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#setLiteral}.
	 * @param ctx the parse tree
	 */
	void enterSetLiteral(JvmDslParserParser.SetLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#setLiteral}.
	 * @param ctx the parse tree
	 */
	void exitSetLiteral(JvmDslParserParser.SetLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#optionalLiteral}.
	 * @param ctx the parse tree
	 */
	void enterOptionalLiteral(JvmDslParserParser.OptionalLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#optionalLiteral}.
	 * @param ctx the parse tree
	 */
	void exitOptionalLiteral(JvmDslParserParser.OptionalLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#mapLiteral}.
	 * @param ctx the parse tree
	 */
	void enterMapLiteral(JvmDslParserParser.MapLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#mapLiteral}.
	 * @param ctx the parse tree
	 */
	void exitMapLiteral(JvmDslParserParser.MapLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#tupleLiteral}.
	 * @param ctx the parse tree
	 */
	void enterTupleLiteral(JvmDslParserParser.TupleLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#tupleLiteral}.
	 * @param ctx the parse tree
	 */
	void exitTupleLiteral(JvmDslParserParser.TupleLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#classLiteral}.
	 * @param ctx the parse tree
	 */
	void enterClassLiteral(JvmDslParserParser.ClassLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#classLiteral}.
	 * @param ctx the parse tree
	 */
	void exitClassLiteral(JvmDslParserParser.ClassLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#localVariable}.
	 * @param ctx the parse tree
	 */
	void enterLocalVariable(JvmDslParserParser.LocalVariableContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#localVariable}.
	 * @param ctx the parse tree
	 */
	void exitLocalVariable(JvmDslParserParser.LocalVariableContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#variable}.
	 * @param ctx the parse tree
	 */
	void enterVariable(JvmDslParserParser.VariableContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#variable}.
	 * @param ctx the parse tree
	 */
	void exitVariable(JvmDslParserParser.VariableContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#importClazz}.
	 * @param ctx the parse tree
	 */
	void enterImportClazz(JvmDslParserParser.ImportClazzContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#importClazz}.
	 * @param ctx the parse tree
	 */
	void exitImportClazz(JvmDslParserParser.ImportClazzContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#jarPath}.
	 * @param ctx the parse tree
	 */
	void enterJarPath(JvmDslParserParser.JarPathContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#jarPath}.
	 * @param ctx the parse tree
	 */
	void exitJarPath(JvmDslParserParser.JarPathContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#jarName}.
	 * @param ctx the parse tree
	 */
	void enterJarName(JvmDslParserParser.JarNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#jarName}.
	 * @param ctx the parse tree
	 */
	void exitJarName(JvmDslParserParser.JarNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#parameters}.
	 * @param ctx the parse tree
	 */
	void enterParameters(JvmDslParserParser.ParametersContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#parameters}.
	 * @param ctx the parse tree
	 */
	void exitParameters(JvmDslParserParser.ParametersContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#throwDef}.
	 * @param ctx the parse tree
	 */
	void enterThrowDef(JvmDslParserParser.ThrowDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#throwDef}.
	 * @param ctx the parse tree
	 */
	void exitThrowDef(JvmDslParserParser.ThrowDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#parameter}.
	 * @param ctx the parse tree
	 */
	void enterParameter(JvmDslParserParser.ParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#parameter}.
	 * @param ctx the parse tree
	 */
	void exitParameter(JvmDslParserParser.ParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#funcName}.
	 * @param ctx the parse tree
	 */
	void enterFuncName(JvmDslParserParser.FuncNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#funcName}.
	 * @param ctx the parse tree
	 */
	void exitFuncName(JvmDslParserParser.FuncNameContext ctx);
}