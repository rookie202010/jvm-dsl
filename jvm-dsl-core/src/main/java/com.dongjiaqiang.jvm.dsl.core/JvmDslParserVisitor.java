// Generated from java-escape by ANTLR 4.11.1
package com.dongjiaqiang.jvm.dsl.core;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link JvmDslParserParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface JvmDslParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(JvmDslParserParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#member}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMember(JvmDslParserParser.MemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#importDeppendency}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImportDeppendency(JvmDslParserParser.ImportDeppendencyContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#block}.
	 * @param ctx the parse tree
     * @return the visitor result
     */
    T visitBlock(JvmDslParserParser.BlockContext ctx);

    /**
     * Visit a parse tree produced by {@link JvmDslParserParser#blockStatements}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitBlockStatements(JvmDslParserParser.BlockStatementsContext ctx);

    /**
     * Visit a parse tree produced by the {@code VarDefExpr}
     * labeled alternative in {@link JvmDslParserParser#blockStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitVarDefExpr(JvmDslParserParser.VarDefExprContext ctx);

    /**
     * Visit a parse tree produced by the {@code StatementExpr}
     * labeled alternative in {@link JvmDslParserParser#blockStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitStatementExpr(JvmDslParserParser.StatementExprContext ctx);

    /**
     * Visit a parse tree produced by the {@code DoWhileExpr}
     * labeled alternative in {@link JvmDslParserParser#statement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitDoWhileExpr(JvmDslParserParser.DoWhileExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code WhileExpr}
	 * labeled alternative in {@link JvmDslParserParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileExpr(JvmDslParserParser.WhileExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ForExpr}
	 * labeled alternative in {@link JvmDslParserParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForExpr(JvmDslParserParser.ForExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IfExpr}
	 * labeled alternative in {@link JvmDslParserParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfExpr(JvmDslParserParser.IfExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AssignExpr}
	 * labeled alternative in {@link JvmDslParserParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignExpr(JvmDslParserParser.AssignExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SyncExpr}
	 * labeled alternative in {@link JvmDslParserParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSyncExpr(JvmDslParserParser.SyncExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ThrowOrSideEffectExpr}
	 * labeled alternative in {@link JvmDslParserParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitThrowOrSideEffectExpr(JvmDslParserParser.ThrowOrSideEffectExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BreakExpr}
	 * labeled alternative in {@link JvmDslParserParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBreakExpr(JvmDslParserParser.BreakExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ContinueExpr}
	 * labeled alternative in {@link JvmDslParserParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContinueExpr(JvmDslParserParser.ContinueExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TryExpr}
	 * labeled alternative in {@link JvmDslParserParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTryExpr(JvmDslParserParser.TryExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AssertExpr}
	 * labeled alternative in {@link JvmDslParserParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssertExpr(JvmDslParserParser.AssertExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BlockExpr}
     * labeled alternative in {@link JvmDslParserParser#statement}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitBlockExpr(JvmDslParserParser.BlockExprContext ctx);

    /**
     * Visit a parse tree produced by the {@code SemiExpr}
     * labeled alternative in {@link JvmDslParserParser#statement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitSemiExpr(JvmDslParserParser.SemiExprContext ctx);

    /**
     * Visit a parse tree produced by {@link JvmDslParserParser#synchronizedStatement}.
     * @param ctx the parse tree
     * @return the visitor result
     */
	T visitSynchronizedStatement(JvmDslParserParser.SynchronizedStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#throwReturnOrSideEffectStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitThrowReturnOrSideEffectStatement(JvmDslParserParser.ThrowReturnOrSideEffectStatementContext ctx);
	/**
     * Visit a parse tree produced by {@link JvmDslParserParser#breakStatement}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitBreakStatement(JvmDslParserParser.BreakStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link JvmDslParserParser#continueStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitContinueStatement(JvmDslParserParser.ContinueStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link JvmDslParserParser#importClazzStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitImportClazzStatement(JvmDslParserParser.ImportClazzStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link JvmDslParserParser#usingPackageStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitUsingPackageStatement(JvmDslParserParser.UsingPackageStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link JvmDslParserParser#assertStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitAssertStatement(JvmDslParserParser.AssertStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link JvmDslParserParser#tryStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitTryStatement(JvmDslParserParser.TryStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link JvmDslParserParser#catches}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCatches(JvmDslParserParser.CatchesContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#catcheClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCatcheClause(JvmDslParserParser.CatcheClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#whileStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileStatement(JvmDslParserParser.WhileStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#doWhileStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDoWhileStatement(JvmDslParserParser.DoWhileStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ForStatementOne}
	 * labeled alternative in {@link JvmDslParserParser#forStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForStatementOne(JvmDslParserParser.ForStatementOneContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ForStatementTwo}
	 * labeled alternative in {@link JvmDslParserParser#forStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForStatementTwo(JvmDslParserParser.ForStatementTwoContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ForStatementThree}
	 * labeled alternative in {@link JvmDslParserParser#forStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForStatementThree(JvmDslParserParser.ForStatementThreeContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#ifStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStatement(JvmDslParserParser.IfStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(JvmDslParserParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#assignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment(JvmDslParserParser.AssignmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#arrayVariable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayVariable(JvmDslParserParser.ArrayVariableContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#mapVariable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapVariable(JvmDslParserParser.MapVariableContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#assignOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignOperator(JvmDslParserParser.AssignOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#parenExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenExpression(JvmDslParserParser.ParenExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code OrOpExpr}
	 * labeled alternative in {@link JvmDslParserParser#conditionalOrExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrOpExpr(JvmDslParserParser.OrOpExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParenOrOpParenExpr}
	 * labeled alternative in {@link JvmDslParserParser#conditionalOrExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenOrOpParenExpr(JvmDslParserParser.ParenOrOpParenExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code OrOpParenExpr}
	 * labeled alternative in {@link JvmDslParserParser#conditionalOrExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrOpParenExpr(JvmDslParserParser.OrOpParenExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ConditionalAndExpr}
	 * labeled alternative in {@link JvmDslParserParser#conditionalOrExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditionalAndExpr(JvmDslParserParser.ConditionalAndExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParenOrOpExpr}
	 * labeled alternative in {@link JvmDslParserParser#conditionalOrExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenOrOpExpr(JvmDslParserParser.ParenOrOpExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code InclusiveOrExpr}
	 * labeled alternative in {@link JvmDslParserParser#conditionalAndExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInclusiveOrExpr(JvmDslParserParser.InclusiveOrExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParenAndOpExpr}
	 * labeled alternative in {@link JvmDslParserParser#conditionalAndExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenAndOpExpr(JvmDslParserParser.ParenAndOpExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AndOpExpr}
	 * labeled alternative in {@link JvmDslParserParser#conditionalAndExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndOpExpr(JvmDslParserParser.AndOpExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParenAndOpParenExpr}
	 * labeled alternative in {@link JvmDslParserParser#conditionalAndExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenAndOpParenExpr(JvmDslParserParser.ParenAndOpParenExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AndOpParenExpr}
	 * labeled alternative in {@link JvmDslParserParser#conditionalAndExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndOpParenExpr(JvmDslParserParser.AndOpParenExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BitOrOpExpr}
	 * labeled alternative in {@link JvmDslParserParser#inclusiveOrExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBitOrOpExpr(JvmDslParserParser.BitOrOpExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParenBitOrOpExpr}
	 * labeled alternative in {@link JvmDslParserParser#inclusiveOrExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenBitOrOpExpr(JvmDslParserParser.ParenBitOrOpExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExclusiveOrExpr}
	 * labeled alternative in {@link JvmDslParserParser#inclusiveOrExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExclusiveOrExpr(JvmDslParserParser.ExclusiveOrExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BitOrParenOpExpr}
	 * labeled alternative in {@link JvmDslParserParser#inclusiveOrExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBitOrParenOpExpr(JvmDslParserParser.BitOrParenOpExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParenBitOrOpParenExpr}
	 * labeled alternative in {@link JvmDslParserParser#inclusiveOrExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenBitOrOpParenExpr(JvmDslParserParser.ParenBitOrOpParenExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParenCaretOpExpr}
	 * labeled alternative in {@link JvmDslParserParser#exclusiveOrExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenCaretOpExpr(JvmDslParserParser.ParenCaretOpExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParenCaretOpParenExpr}
	 * labeled alternative in {@link JvmDslParserParser#exclusiveOrExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenCaretOpParenExpr(JvmDslParserParser.ParenCaretOpParenExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CaretOpExpr}
	 * labeled alternative in {@link JvmDslParserParser#exclusiveOrExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCaretOpExpr(JvmDslParserParser.CaretOpExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CaretOpParenExpr}
	 * labeled alternative in {@link JvmDslParserParser#exclusiveOrExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCaretOpParenExpr(JvmDslParserParser.CaretOpParenExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BitAndExpr}
	 * labeled alternative in {@link JvmDslParserParser#exclusiveOrExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBitAndExpr(JvmDslParserParser.BitAndExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BitAndOpParenExpr}
	 * labeled alternative in {@link JvmDslParserParser#bitAndExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBitAndOpParenExpr(JvmDslParserParser.BitAndOpParenExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code EualityExpr}
	 * labeled alternative in {@link JvmDslParserParser#bitAndExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEualityExpr(JvmDslParserParser.EualityExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParenBitAndOpParenExpr}
	 * labeled alternative in {@link JvmDslParserParser#bitAndExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenBitAndOpParenExpr(JvmDslParserParser.ParenBitAndOpParenExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParenBitAnOpExpr}
	 * labeled alternative in {@link JvmDslParserParser#bitAndExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenBitAnOpExpr(JvmDslParserParser.ParenBitAnOpExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BitAndOpExpr}
	 * labeled alternative in {@link JvmDslParserParser#bitAndExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBitAndOpExpr(JvmDslParserParser.BitAndOpExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#equalityOperation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqualityOperation(JvmDslParserParser.EqualityOperationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code EqualExpr}
	 * labeled alternative in {@link JvmDslParserParser#equalityExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqualExpr(JvmDslParserParser.EqualExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParenEqualParenExpr}
	 * labeled alternative in {@link JvmDslParserParser#equalityExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenEqualParenExpr(JvmDslParserParser.ParenEqualParenExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code RelationExpr}
	 * labeled alternative in {@link JvmDslParserParser#equalityExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelationExpr(JvmDslParserParser.RelationExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParenEqualExpr}
	 * labeled alternative in {@link JvmDslParserParser#equalityExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenEqualExpr(JvmDslParserParser.ParenEqualExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code EqualParenExpr}
	 * labeled alternative in {@link JvmDslParserParser#equalityExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqualParenExpr(JvmDslParserParser.EqualParenExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#relationOperation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelationOperation(JvmDslParserParser.RelationOperationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParenRelationOpParenExpr}
	 * labeled alternative in {@link JvmDslParserParser#relationExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenRelationOpParenExpr(JvmDslParserParser.ParenRelationOpParenExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code RelationOpParenExpr}
	 * labeled alternative in {@link JvmDslParserParser#relationExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelationOpParenExpr(JvmDslParserParser.RelationOpParenExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParenRelationOpExpr}
	 * labeled alternative in {@link JvmDslParserParser#relationExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenRelationOpExpr(JvmDslParserParser.ParenRelationOpExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code RelationOpExpr}
	 * labeled alternative in {@link JvmDslParserParser#relationExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelationOpExpr(JvmDslParserParser.RelationOpExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ShiftExpr}
	 * labeled alternative in {@link JvmDslParserParser#relationExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShiftExpr(JvmDslParserParser.ShiftExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#shiftOperation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShiftOperation(JvmDslParserParser.ShiftOperationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AddExpr}
	 * labeled alternative in {@link JvmDslParserParser#shiftExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddExpr(JvmDslParserParser.AddExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ShiftOpExpr}
	 * labeled alternative in {@link JvmDslParserParser#shiftExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShiftOpExpr(JvmDslParserParser.ShiftOpExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParenShiftOpExpr}
	 * labeled alternative in {@link JvmDslParserParser#shiftExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenShiftOpExpr(JvmDslParserParser.ParenShiftOpExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParenShiftOpParenExpr}
	 * labeled alternative in {@link JvmDslParserParser#shiftExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenShiftOpParenExpr(JvmDslParserParser.ParenShiftOpParenExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ShiftOpParenExpr}
	 * labeled alternative in {@link JvmDslParserParser#shiftExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShiftOpParenExpr(JvmDslParserParser.ShiftOpParenExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#additiveOperation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdditiveOperation(JvmDslParserParser.AdditiveOperationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MultiExpr}
	 * labeled alternative in {@link JvmDslParserParser#additiveExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiExpr(JvmDslParserParser.MultiExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParenAddOpExpr}
	 * labeled alternative in {@link JvmDslParserParser#additiveExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenAddOpExpr(JvmDslParserParser.ParenAddOpExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AddOpParenExpr}
	 * labeled alternative in {@link JvmDslParserParser#additiveExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddOpParenExpr(JvmDslParserParser.AddOpParenExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AddOpExpr}
	 * labeled alternative in {@link JvmDslParserParser#additiveExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddOpExpr(JvmDslParserParser.AddOpExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParenAddOpParenExpr}
	 * labeled alternative in {@link JvmDslParserParser#additiveExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenAddOpParenExpr(JvmDslParserParser.ParenAddOpParenExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#multiplicativeOperation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiplicativeOperation(JvmDslParserParser.MultiplicativeOperationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MultiOpExpr}
	 * labeled alternative in {@link JvmDslParserParser#multiplicativeExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiOpExpr(JvmDslParserParser.MultiOpExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParenMultiOpExpr}
	 * labeled alternative in {@link JvmDslParserParser#multiplicativeExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenMultiOpExpr(JvmDslParserParser.ParenMultiOpExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParenMultiOpParenExpr}
	 * labeled alternative in {@link JvmDslParserParser#multiplicativeExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenMultiOpParenExpr(JvmDslParserParser.ParenMultiOpParenExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MultiOpParenExpr}
	 * labeled alternative in {@link JvmDslParserParser#multiplicativeExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiOpParenExpr(JvmDslParserParser.MultiOpParenExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UnaryExpr}
	 * labeled alternative in {@link JvmDslParserParser#multiplicativeExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryExpr(JvmDslParserParser.UnaryExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LiteralAndFuncCallExpr}
	 * labeled alternative in {@link JvmDslParserParser#unaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralAndFuncCallExpr(JvmDslParserParser.LiteralAndFuncCallExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code OppositeExpr}
	 * labeled alternative in {@link JvmDslParserParser#unaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOppositeExpr(JvmDslParserParser.OppositeExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NegateExpr}
	 * labeled alternative in {@link JvmDslParserParser#unaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNegateExpr(JvmDslParserParser.NegateExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CastExpr}
	 * labeled alternative in {@link JvmDslParserParser#unaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCastExpr(JvmDslParserParser.CastExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code InstanceofExpr}
	 * labeled alternative in {@link JvmDslParserParser#unaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInstanceofExpr(JvmDslParserParser.InstanceofExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParenExpr}
	 * labeled alternative in {@link JvmDslParserParser#unaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenExpr(JvmDslParserParser.ParenExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParamsLambdaExpr}
	 * labeled alternative in {@link JvmDslParserParser#lambdaExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParamsLambdaExpr(JvmDslParserParser.ParamsLambdaExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NoParamLambdaExpr}
	 * labeled alternative in {@link JvmDslParserParser#lambdaExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNoParamLambdaExpr(JvmDslParserParser.NoParamLambdaExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code OneParamLambdaExpr}
	 * labeled alternative in {@link JvmDslParserParser#lambdaExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOneParamLambdaExpr(JvmDslParserParser.OneParamLambdaExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MatchCaseExpr}
	 * labeled alternative in {@link JvmDslParserParser#lambdaExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMatchCaseExpr(JvmDslParserParser.MatchCaseExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#caseExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCaseExpression(JvmDslParserParser.CaseExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#unapplyExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnapplyExpression(JvmDslParserParser.UnapplyExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code VoidType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVoidType(JvmDslParserParser.VoidTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LambdaOneInOneOutType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLambdaOneInOneOutType(JvmDslParserParser.LambdaOneInOneOutTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BoolType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolType(JvmDslParserParser.BoolTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FutureType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFutureType(JvmDslParserParser.FutureTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LongType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLongType(JvmDslParserParser.LongTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code StringType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringType(JvmDslParserParser.StringTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParameterizedClassType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameterizedClassType(JvmDslParserParser.ParameterizedClassTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ByteType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitByteType(JvmDslParserParser.ByteTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LambdaZeroInMoreOutType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLambdaZeroInMoreOutType(JvmDslParserParser.LambdaZeroInMoreOutTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ClassType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassType(JvmDslParserParser.ClassTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LambdaMoreInOneOutType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLambdaMoreInOneOutType(JvmDslParserParser.LambdaMoreInOneOutTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MapType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapType(JvmDslParserParser.MapTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IntType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntType(JvmDslParserParser.IntTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FloatType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFloatType(JvmDslParserParser.FloatTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LambdaMoreInMoreOutType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLambdaMoreInMoreOutType(JvmDslParserParser.LambdaMoreInMoreOutTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ArrayType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayType(JvmDslParserParser.ArrayTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SetType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetType(JvmDslParserParser.SetTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LambdaZeroInOneOutType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLambdaZeroInOneOutType(JvmDslParserParser.LambdaZeroInOneOutTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DoubleType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDoubleType(JvmDslParserParser.DoubleTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ListType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListType(JvmDslParserParser.ListTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TupleType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTupleType(JvmDslParserParser.TupleTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CharType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCharType(JvmDslParserParser.CharTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code OptionType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptionType(JvmDslParserParser.OptionTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LambdaOneInMoreOutType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLambdaOneInMoreOutType(JvmDslParserParser.LambdaOneInMoreOutTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#types}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypes(JvmDslParserParser.TypesContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#clazzType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClazzType(JvmDslParserParser.ClazzTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#varDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDef(JvmDslParserParser.VarDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#classDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassDef(JvmDslParserParser.ClassDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#callChain}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCallChain(JvmDslParserParser.CallChainContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPart(JvmDslParserParser.PartContext ctx);
	/**
	 * Visit a parse tree produced by the {@code VarCallNoArgs}
	 * labeled alternative in {@link JvmDslParserParser#funcCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarCallNoArgs(JvmDslParserParser.VarCallNoArgsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code VarCallArgs}
	 * labeled alternative in {@link JvmDslParserParser#funcCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarCallArgs(JvmDslParserParser.VarCallArgsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LiteralCallNoArgs}
	 * labeled alternative in {@link JvmDslParserParser#funcCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralCallNoArgs(JvmDslParserParser.LiteralCallNoArgsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LiteralCallArgs}
	 * labeled alternative in {@link JvmDslParserParser#funcCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralCallArgs(JvmDslParserParser.LiteralCallArgsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TypeCallNoArgs}
	 * labeled alternative in {@link JvmDslParserParser#funcCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeCallNoArgs(JvmDslParserParser.TypeCallNoArgsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TypeCallArgs}
	 * labeled alternative in {@link JvmDslParserParser#funcCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeCallArgs(JvmDslParserParser.TypeCallArgsContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#funcDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncDef(JvmDslParserParser.FuncDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#fieldDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFieldDef(JvmDslParserParser.FieldDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#literalAndCallChain}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralAndCallChain(JvmDslParserParser.LiteralAndCallChainContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral(JvmDslParserParser.LiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#baseLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBaseLiteral(JvmDslParserParser.BaseLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#numberLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumberLiteral(JvmDslParserParser.NumberLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#listLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListLiteral(JvmDslParserParser.ListLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#asyncLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAsyncLiteral(JvmDslParserParser.AsyncLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#setLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetLiteral(JvmDslParserParser.SetLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#optionalLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptionalLiteral(JvmDslParserParser.OptionalLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#mapLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapLiteral(JvmDslParserParser.MapLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#tupleLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTupleLiteral(JvmDslParserParser.TupleLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#classLiteral}.
	 * @param ctx the parse tree
     * @return the visitor result
     */
    T visitClassLiteral(JvmDslParserParser.ClassLiteralContext ctx);

    /**
     * Visit a parse tree produced by {@link JvmDslParserParser#localVariable}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitLocalVariable(JvmDslParserParser.LocalVariableContext ctx);

    /**
     * Visit a parse tree produced by {@link JvmDslParserParser#variable}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitVariable(JvmDslParserParser.VariableContext ctx);

    /**
     * Visit a parse tree produced by {@link JvmDslParserParser#importClazz}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitImportClazz(JvmDslParserParser.ImportClazzContext ctx);

    /**
     * Visit a parse tree produced by {@link JvmDslParserParser#packagePath}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitPackagePath(JvmDslParserParser.PackagePathContext ctx);

    /**
     * Visit a parse tree produced by {@link JvmDslParserParser#packageName}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitPackageName(JvmDslParserParser.PackageNameContext ctx);

    /**
     * Visit a parse tree produced by {@link JvmDslParserParser#parameters}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitParameters(JvmDslParserParser.ParametersContext ctx);

    /**
     * Visit a parse tree produced by {@link JvmDslParserParser#throwDef}.
     * @param ctx the parse tree
     * @return the visitor result
	 */
	T visitThrowDef(JvmDslParserParser.ThrowDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#parameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameter(JvmDslParserParser.ParameterContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#funcName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncName(JvmDslParserParser.FuncNameContext ctx);
}