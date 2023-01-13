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
     *
     * @param ctx the parse tree
     */
    void exitProgram(JvmDslParserParser.ProgramContext ctx);

    /**
     * Enter a parse tree produced by {@link JvmDslParserParser#member}.
     *
     * @param ctx the parse tree
     */
    void enterMember(JvmDslParserParser.MemberContext ctx);

    /**
     * Exit a parse tree produced by {@link JvmDslParserParser#member}.
     *
     * @param ctx the parse tree
     */
    void exitMember(JvmDslParserParser.MemberContext ctx);

    /**
     * Enter a parse tree produced by {@link JvmDslParserParser#importDependency}.
     *
     * @param ctx the parse tree
     */
    void enterImportDependency(JvmDslParserParser.ImportDependencyContext ctx);

    /**
     * Exit a parse tree produced by {@link JvmDslParserParser#importDependency}.
     *
     * @param ctx the parse tree
     */
    void exitImportDependency(JvmDslParserParser.ImportDependencyContext ctx);

    /**
     * Enter a parse tree produced by {@link JvmDslParserParser#block}.
     *
     * @param ctx the parse tree
     */
    void enterBlock(JvmDslParserParser.BlockContext ctx);

    /**
     * Exit a parse tree produced by {@link JvmDslParserParser#block}.
     *
     * @param ctx the parse tree
     */
    void exitBlock(JvmDslParserParser.BlockContext ctx);

    /**
     * Enter a parse tree produced by {@link JvmDslParserParser#lambdaBlock}.
     *
     * @param ctx the parse tree
     */
    void enterLambdaBlock(JvmDslParserParser.LambdaBlockContext ctx);

    /**
     * Exit a parse tree produced by {@link JvmDslParserParser#lambdaBlock}.
     *
     * @param ctx the parse tree
     */
    void exitLambdaBlock(JvmDslParserParser.LambdaBlockContext ctx);

    /**
     * Enter a parse tree produced by {@link JvmDslParserParser#matchCaseBlock}.
     *
     * @param ctx the parse tree
     */
    void enterMatchCaseBlock(JvmDslParserParser.MatchCaseBlockContext ctx);

    /**
     * Exit a parse tree produced by {@link JvmDslParserParser#matchCaseBlock}.
     *
     * @param ctx the parse tree
     */
    void exitMatchCaseBlock(JvmDslParserParser.MatchCaseBlockContext ctx);

    /**
     * Enter a parse tree produced by {@link JvmDslParserParser#blockStatements}.
     *
     * @param ctx the parse tree
     */
    void enterBlockStatements(JvmDslParserParser.BlockStatementsContext ctx);

    /**
     * Exit a parse tree produced by {@link JvmDslParserParser#blockStatements}.
     *
     * @param ctx the parse tree
     */
    void exitBlockStatements(JvmDslParserParser.BlockStatementsContext ctx);

    /**
     * Enter a parse tree produced by the {@code VarDefExpr}
     * labeled alternative in {@link JvmDslParserParser#blockStatement}.
     *
     * @param ctx the parse tree
     */
    void enterVarDefExpr(JvmDslParserParser.VarDefExprContext ctx);

    /**
     * Exit a parse tree produced by the {@code VarDefExpr}
     * labeled alternative in {@link JvmDslParserParser#blockStatement}.
     *
     * @param ctx the parse tree
     */
    void exitVarDefExpr(JvmDslParserParser.VarDefExprContext ctx);

    /**
     * Enter a parse tree produced by the {@code StatementExpr}
     * labeled alternative in {@link JvmDslParserParser#blockStatement}.
     *
     * @param ctx the parse tree
     */
    void enterStatementExpr(JvmDslParserParser.StatementExprContext ctx);

    /**
     * Exit a parse tree produced by the {@code StatementExpr}
     * labeled alternative in {@link JvmDslParserParser#blockStatement}.
     *
     * @param ctx the parse tree
     */
    void exitStatementExpr(JvmDslParserParser.StatementExprContext ctx);

    /**
     * Enter a parse tree produced by the {@code DoWhileExpr}
     * labeled alternative in {@link JvmDslParserParser#statement}.
     *
     * @param ctx the parse tree
     */
    void enterDoWhileExpr(JvmDslParserParser.DoWhileExprContext ctx);

    /**
     * Exit a parse tree produced by the {@code DoWhileExpr}
     * labeled alternative in {@link JvmDslParserParser#statement}.
     *
     * @param ctx the parse tree
     */
    void exitDoWhileExpr(JvmDslParserParser.DoWhileExprContext ctx);

    /**
     * Enter a parse tree produced by the {@code WhileExpr}
     * labeled alternative in {@link JvmDslParserParser#statement}.
     *
     * @param ctx the parse tree
     */
    void enterWhileExpr(JvmDslParserParser.WhileExprContext ctx);

    /**
     * Exit a parse tree produced by the {@code WhileExpr}
     * labeled alternative in {@link JvmDslParserParser#statement}.
     *
     * @param ctx the parse tree
     */
    void exitWhileExpr(JvmDslParserParser.WhileExprContext ctx);

    /**
     * Enter a parse tree produced by the {@code ForExpr}
     * labeled alternative in {@link JvmDslParserParser#statement}.
     *
     * @param ctx the parse tree
     */
    void enterForExpr(JvmDslParserParser.ForExprContext ctx);

    /**
     * Exit a parse tree produced by the {@code ForExpr}
     * labeled alternative in {@link JvmDslParserParser#statement}.
     *
     * @param ctx the parse tree
     */
    void exitForExpr(JvmDslParserParser.ForExprContext ctx);

    /**
     * Enter a parse tree produced by the {@code IfExpr}
     * labeled alternative in {@link JvmDslParserParser#statement}.
     *
     * @param ctx the parse tree
     */
    void enterIfExpr(JvmDslParserParser.IfExprContext ctx);

    /**
     * Exit a parse tree produced by the {@code IfExpr}
     * labeled alternative in {@link JvmDslParserParser#statement}.
     *
     * @param ctx the parse tree
     */
    void exitIfExpr(JvmDslParserParser.IfExprContext ctx);

    /**
     * Enter a parse tree produced by the {@code AssignExpr}
	 * labeled alternative in {@link JvmDslParserParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterAssignExpr(JvmDslParserParser.AssignExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AssignExpr}
	 * labeled alternative in {@link JvmDslParserParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitAssignExpr(JvmDslParserParser.AssignExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SyncExpr}
	 * labeled alternative in {@link JvmDslParserParser#statement}.
     * @param ctx the parse tree
     */
    void enterSyncExpr(JvmDslParserParser.SyncExprContext ctx);

    /**
     * Exit a parse tree produced by the {@code SyncExpr}
     * labeled alternative in {@link JvmDslParserParser#statement}.
     *
     * @param ctx the parse tree
     */
    void exitSyncExpr(JvmDslParserParser.SyncExprContext ctx);

    /**
     * Enter a parse tree produced by the {@code ThrowOrSideEffectExpr}
     * labeled alternative in {@link JvmDslParserParser#statement}.
     *
     * @param ctx the parse tree
     */
    void enterThrowOrSideEffectExpr(JvmDslParserParser.ThrowOrSideEffectExprContext ctx);

    /**
     * Exit a parse tree produced by the {@code ThrowOrSideEffectExpr}
     * labeled alternative in {@link JvmDslParserParser#statement}.
     *
     * @param ctx the parse tree
     */
    void exitThrowOrSideEffectExpr(JvmDslParserParser.ThrowOrSideEffectExprContext ctx);

    /**
     * Enter a parse tree produced by the {@code BreakExpr}
     * labeled alternative in {@link JvmDslParserParser#statement}.
     *
     * @param ctx the parse tree
     */
    void enterBreakExpr(JvmDslParserParser.BreakExprContext ctx);

    /**
     * Exit a parse tree produced by the {@code BreakExpr}
     * labeled alternative in {@link JvmDslParserParser#statement}.
     *
     * @param ctx the parse tree
     */
    void exitBreakExpr(JvmDslParserParser.BreakExprContext ctx);

    /**
     * Enter a parse tree produced by the {@code ContinueExpr}
     * labeled alternative in {@link JvmDslParserParser#statement}.
     *
     * @param ctx the parse tree
     */
    void enterContinueExpr(JvmDslParserParser.ContinueExprContext ctx);

    /**
     * Exit a parse tree produced by the {@code ContinueExpr}
     * labeled alternative in {@link JvmDslParserParser#statement}.
     *
     * @param ctx the parse tree
     */
    void exitContinueExpr(JvmDslParserParser.ContinueExprContext ctx);

    /**
     * Enter a parse tree produced by the {@code TryExpr}
     * labeled alternative in {@link JvmDslParserParser#statement}.
     *
     * @param ctx the parse tree
     */
    void enterTryExpr(JvmDslParserParser.TryExprContext ctx);

    /**
     * Exit a parse tree produced by the {@code TryExpr}
     * labeled alternative in {@link JvmDslParserParser#statement}.
     *
     * @param ctx the parse tree
     */
    void exitTryExpr(JvmDslParserParser.TryExprContext ctx);

    /**
     * Enter a parse tree produced by the {@code AssertExpr}
     * labeled alternative in {@link JvmDslParserParser#statement}.
     *
     * @param ctx the parse tree
     */
    void enterAssertExpr(JvmDslParserParser.AssertExprContext ctx);

    /**
     * Exit a parse tree produced by the {@code AssertExpr}
     * labeled alternative in {@link JvmDslParserParser#statement}.
     *
     * @param ctx the parse tree
     */
    void exitAssertExpr(JvmDslParserParser.AssertExprContext ctx);

    /**
     * Enter a parse tree produced by the {@code BlockExpr}
     * labeled alternative in {@link JvmDslParserParser#statement}.
     *
     * @param ctx the parse tree
     */
    void enterBlockExpr(JvmDslParserParser.BlockExprContext ctx);

    /**
     * Exit a parse tree produced by the {@code BlockExpr}
     * labeled alternative in {@link JvmDslParserParser#statement}.
     *
     * @param ctx the parse tree
     */
    void exitBlockExpr(JvmDslParserParser.BlockExprContext ctx);

    /**
     * Enter a parse tree produced by the {@code SemiExpr}
     * labeled alternative in {@link JvmDslParserParser#statement}.
     *
     * @param ctx the parse tree
     */
    void enterSemiExpr(JvmDslParserParser.SemiExprContext ctx);

    /**
     * Exit a parse tree produced by the {@code SemiExpr}
     * labeled alternative in {@link JvmDslParserParser#statement}.
     *
     * @param ctx the parse tree
     */
    void exitSemiExpr(JvmDslParserParser.SemiExprContext ctx);

    /**
     * Enter a parse tree produced by {@link JvmDslParserParser#synchronizedStatement}.
     *
     * @param ctx the parse tree
     */
    void enterSynchronizedStatement(JvmDslParserParser.SynchronizedStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link JvmDslParserParser#synchronizedStatement}.
     *
     * @param ctx the parse tree
     */
    void exitSynchronizedStatement(JvmDslParserParser.SynchronizedStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link JvmDslParserParser#throwReturnOrSideEffectStatement}.
     *
     * @param ctx the parse tree
     */
    void enterThrowReturnOrSideEffectStatement(JvmDslParserParser.ThrowReturnOrSideEffectStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link JvmDslParserParser#throwReturnOrSideEffectStatement}.
     *
     * @param ctx the parse tree
     */
    void exitThrowReturnOrSideEffectStatement(JvmDslParserParser.ThrowReturnOrSideEffectStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link JvmDslParserParser#breakStatement}.
     *
     * @param ctx the parse tree
     */
    void enterBreakStatement(JvmDslParserParser.BreakStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link JvmDslParserParser#breakStatement}.
     *
     * @param ctx the parse tree
     */
    void exitBreakStatement(JvmDslParserParser.BreakStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link JvmDslParserParser#continueStatement}.
     *
     * @param ctx the parse tree
     */
    void enterContinueStatement(JvmDslParserParser.ContinueStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link JvmDslParserParser#continueStatement}.
     *
     * @param ctx the parse tree
     */
    void exitContinueStatement(JvmDslParserParser.ContinueStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link JvmDslParserParser#importClazzStatement}.
     *
     * @param ctx the parse tree
     */
    void enterImportClazzStatement(JvmDslParserParser.ImportClazzStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link JvmDslParserParser#importClazzStatement}.
     *
     * @param ctx the parse tree
     */
    void exitImportClazzStatement(JvmDslParserParser.ImportClazzStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link JvmDslParserParser#usingPackageStatement}.
     *
     * @param ctx the parse tree
     */
    void enterUsingPackageStatement(JvmDslParserParser.UsingPackageStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link JvmDslParserParser#usingPackageStatement}.
     *
     * @param ctx the parse tree
     */
    void exitUsingPackageStatement(JvmDslParserParser.UsingPackageStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link JvmDslParserParser#assertStatement}.
     *
     * @param ctx the parse tree
     */
    void enterAssertStatement(JvmDslParserParser.AssertStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link JvmDslParserParser#assertStatement}.
     *
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
	 * Enter a parse tree produced by {@link JvmDslParserParser#arrayVariable}.
	 * @param ctx the parse tree
	 */
	void enterArrayVariable(JvmDslParserParser.ArrayVariableContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#arrayVariable}.
	 * @param ctx the parse tree
	 */
	void exitArrayVariable(JvmDslParserParser.ArrayVariableContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#mapVariable}.
	 * @param ctx the parse tree
	 */
	void enterMapVariable(JvmDslParserParser.MapVariableContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#mapVariable}.
	 * @param ctx the parse tree
	 */
	void exitMapVariable(JvmDslParserParser.MapVariableContext ctx);
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
	 * Enter a parse tree produced by {@link JvmDslParserParser#parenExpression}.
	 * @param ctx the parse tree
	 */
	void enterParenExpression(JvmDslParserParser.ParenExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#parenExpression}.
	 * @param ctx the parse tree
	 */
	void exitParenExpression(JvmDslParserParser.ParenExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code OrOpExpr}
	 * labeled alternative in {@link JvmDslParserParser#conditionalOrExpression}.
	 * @param ctx the parse tree
	 */
	void enterOrOpExpr(JvmDslParserParser.OrOpExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code OrOpExpr}
	 * labeled alternative in {@link JvmDslParserParser#conditionalOrExpression}.
	 * @param ctx the parse tree
	 */
	void exitOrOpExpr(JvmDslParserParser.OrOpExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ParenOrOpParenExpr}
	 * labeled alternative in {@link JvmDslParserParser#conditionalOrExpression}.
	 * @param ctx the parse tree
	 */
	void enterParenOrOpParenExpr(JvmDslParserParser.ParenOrOpParenExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ParenOrOpParenExpr}
	 * labeled alternative in {@link JvmDslParserParser#conditionalOrExpression}.
	 * @param ctx the parse tree
	 */
	void exitParenOrOpParenExpr(JvmDslParserParser.ParenOrOpParenExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code OrOpParenExpr}
	 * labeled alternative in {@link JvmDslParserParser#conditionalOrExpression}.
	 * @param ctx the parse tree
	 */
	void enterOrOpParenExpr(JvmDslParserParser.OrOpParenExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code OrOpParenExpr}
	 * labeled alternative in {@link JvmDslParserParser#conditionalOrExpression}.
	 * @param ctx the parse tree
	 */
	void exitOrOpParenExpr(JvmDslParserParser.OrOpParenExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ConditionalAndExpr}
	 * labeled alternative in {@link JvmDslParserParser#conditionalOrExpression}.
	 * @param ctx the parse tree
	 */
	void enterConditionalAndExpr(JvmDslParserParser.ConditionalAndExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ConditionalAndExpr}
	 * labeled alternative in {@link JvmDslParserParser#conditionalOrExpression}.
	 * @param ctx the parse tree
	 */
	void exitConditionalAndExpr(JvmDslParserParser.ConditionalAndExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ParenOrOpExpr}
	 * labeled alternative in {@link JvmDslParserParser#conditionalOrExpression}.
	 * @param ctx the parse tree
	 */
	void enterParenOrOpExpr(JvmDslParserParser.ParenOrOpExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ParenOrOpExpr}
	 * labeled alternative in {@link JvmDslParserParser#conditionalOrExpression}.
	 * @param ctx the parse tree
	 */
	void exitParenOrOpExpr(JvmDslParserParser.ParenOrOpExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code InclusiveOrExpr}
	 * labeled alternative in {@link JvmDslParserParser#conditionalAndExpression}.
	 * @param ctx the parse tree
	 */
	void enterInclusiveOrExpr(JvmDslParserParser.InclusiveOrExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code InclusiveOrExpr}
	 * labeled alternative in {@link JvmDslParserParser#conditionalAndExpression}.
	 * @param ctx the parse tree
	 */
	void exitInclusiveOrExpr(JvmDslParserParser.InclusiveOrExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ParenAndOpExpr}
	 * labeled alternative in {@link JvmDslParserParser#conditionalAndExpression}.
	 * @param ctx the parse tree
	 */
	void enterParenAndOpExpr(JvmDslParserParser.ParenAndOpExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ParenAndOpExpr}
	 * labeled alternative in {@link JvmDslParserParser#conditionalAndExpression}.
	 * @param ctx the parse tree
	 */
	void exitParenAndOpExpr(JvmDslParserParser.ParenAndOpExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AndOpExpr}
	 * labeled alternative in {@link JvmDslParserParser#conditionalAndExpression}.
	 * @param ctx the parse tree
	 */
	void enterAndOpExpr(JvmDslParserParser.AndOpExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AndOpExpr}
	 * labeled alternative in {@link JvmDslParserParser#conditionalAndExpression}.
	 * @param ctx the parse tree
	 */
	void exitAndOpExpr(JvmDslParserParser.AndOpExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ParenAndOpParenExpr}
	 * labeled alternative in {@link JvmDslParserParser#conditionalAndExpression}.
	 * @param ctx the parse tree
	 */
	void enterParenAndOpParenExpr(JvmDslParserParser.ParenAndOpParenExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ParenAndOpParenExpr}
	 * labeled alternative in {@link JvmDslParserParser#conditionalAndExpression}.
	 * @param ctx the parse tree
	 */
	void exitParenAndOpParenExpr(JvmDslParserParser.ParenAndOpParenExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AndOpParenExpr}
	 * labeled alternative in {@link JvmDslParserParser#conditionalAndExpression}.
	 * @param ctx the parse tree
	 */
	void enterAndOpParenExpr(JvmDslParserParser.AndOpParenExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AndOpParenExpr}
	 * labeled alternative in {@link JvmDslParserParser#conditionalAndExpression}.
	 * @param ctx the parse tree
	 */
	void exitAndOpParenExpr(JvmDslParserParser.AndOpParenExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BitOrOpExpr}
	 * labeled alternative in {@link JvmDslParserParser#inclusiveOrExpression}.
	 * @param ctx the parse tree
	 */
	void enterBitOrOpExpr(JvmDslParserParser.BitOrOpExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BitOrOpExpr}
	 * labeled alternative in {@link JvmDslParserParser#inclusiveOrExpression}.
	 * @param ctx the parse tree
	 */
	void exitBitOrOpExpr(JvmDslParserParser.BitOrOpExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ParenBitOrOpExpr}
	 * labeled alternative in {@link JvmDslParserParser#inclusiveOrExpression}.
	 * @param ctx the parse tree
	 */
	void enterParenBitOrOpExpr(JvmDslParserParser.ParenBitOrOpExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ParenBitOrOpExpr}
	 * labeled alternative in {@link JvmDslParserParser#inclusiveOrExpression}.
	 * @param ctx the parse tree
	 */
	void exitParenBitOrOpExpr(JvmDslParserParser.ParenBitOrOpExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExclusiveOrExpr}
	 * labeled alternative in {@link JvmDslParserParser#inclusiveOrExpression}.
	 * @param ctx the parse tree
	 */
	void enterExclusiveOrExpr(JvmDslParserParser.ExclusiveOrExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExclusiveOrExpr}
	 * labeled alternative in {@link JvmDslParserParser#inclusiveOrExpression}.
	 * @param ctx the parse tree
	 */
	void exitExclusiveOrExpr(JvmDslParserParser.ExclusiveOrExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BitOrParenOpExpr}
	 * labeled alternative in {@link JvmDslParserParser#inclusiveOrExpression}.
	 * @param ctx the parse tree
	 */
	void enterBitOrParenOpExpr(JvmDslParserParser.BitOrParenOpExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BitOrParenOpExpr}
	 * labeled alternative in {@link JvmDslParserParser#inclusiveOrExpression}.
	 * @param ctx the parse tree
	 */
	void exitBitOrParenOpExpr(JvmDslParserParser.BitOrParenOpExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ParenBitOrOpParenExpr}
	 * labeled alternative in {@link JvmDslParserParser#inclusiveOrExpression}.
	 * @param ctx the parse tree
	 */
	void enterParenBitOrOpParenExpr(JvmDslParserParser.ParenBitOrOpParenExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ParenBitOrOpParenExpr}
	 * labeled alternative in {@link JvmDslParserParser#inclusiveOrExpression}.
	 * @param ctx the parse tree
	 */
	void exitParenBitOrOpParenExpr(JvmDslParserParser.ParenBitOrOpParenExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ParenCaretOpExpr}
	 * labeled alternative in {@link JvmDslParserParser#exclusiveOrExpression}.
	 * @param ctx the parse tree
	 */
	void enterParenCaretOpExpr(JvmDslParserParser.ParenCaretOpExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ParenCaretOpExpr}
	 * labeled alternative in {@link JvmDslParserParser#exclusiveOrExpression}.
	 * @param ctx the parse tree
	 */
	void exitParenCaretOpExpr(JvmDslParserParser.ParenCaretOpExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ParenCaretOpParenExpr}
	 * labeled alternative in {@link JvmDslParserParser#exclusiveOrExpression}.
	 * @param ctx the parse tree
	 */
	void enterParenCaretOpParenExpr(JvmDslParserParser.ParenCaretOpParenExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ParenCaretOpParenExpr}
	 * labeled alternative in {@link JvmDslParserParser#exclusiveOrExpression}.
	 * @param ctx the parse tree
	 */
	void exitParenCaretOpParenExpr(JvmDslParserParser.ParenCaretOpParenExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CaretOpExpr}
	 * labeled alternative in {@link JvmDslParserParser#exclusiveOrExpression}.
	 * @param ctx the parse tree
	 */
	void enterCaretOpExpr(JvmDslParserParser.CaretOpExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CaretOpExpr}
	 * labeled alternative in {@link JvmDslParserParser#exclusiveOrExpression}.
	 * @param ctx the parse tree
	 */
	void exitCaretOpExpr(JvmDslParserParser.CaretOpExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CaretOpParenExpr}
	 * labeled alternative in {@link JvmDslParserParser#exclusiveOrExpression}.
	 * @param ctx the parse tree
	 */
	void enterCaretOpParenExpr(JvmDslParserParser.CaretOpParenExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CaretOpParenExpr}
	 * labeled alternative in {@link JvmDslParserParser#exclusiveOrExpression}.
	 * @param ctx the parse tree
	 */
	void exitCaretOpParenExpr(JvmDslParserParser.CaretOpParenExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BitAndExpr}
	 * labeled alternative in {@link JvmDslParserParser#exclusiveOrExpression}.
	 * @param ctx the parse tree
	 */
	void enterBitAndExpr(JvmDslParserParser.BitAndExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BitAndExpr}
	 * labeled alternative in {@link JvmDslParserParser#exclusiveOrExpression}.
	 * @param ctx the parse tree
	 */
	void exitBitAndExpr(JvmDslParserParser.BitAndExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BitAndOpParenExpr}
	 * labeled alternative in {@link JvmDslParserParser#bitAndExpression}.
	 * @param ctx the parse tree
	 */
	void enterBitAndOpParenExpr(JvmDslParserParser.BitAndOpParenExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BitAndOpParenExpr}
	 * labeled alternative in {@link JvmDslParserParser#bitAndExpression}.
	 * @param ctx the parse tree
	 */
	void exitBitAndOpParenExpr(JvmDslParserParser.BitAndOpParenExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code EualityExpr}
	 * labeled alternative in {@link JvmDslParserParser#bitAndExpression}.
	 * @param ctx the parse tree
	 */
	void enterEualityExpr(JvmDslParserParser.EualityExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code EualityExpr}
	 * labeled alternative in {@link JvmDslParserParser#bitAndExpression}.
	 * @param ctx the parse tree
	 */
	void exitEualityExpr(JvmDslParserParser.EualityExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ParenBitAndOpParenExpr}
	 * labeled alternative in {@link JvmDslParserParser#bitAndExpression}.
	 * @param ctx the parse tree
	 */
	void enterParenBitAndOpParenExpr(JvmDslParserParser.ParenBitAndOpParenExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ParenBitAndOpParenExpr}
	 * labeled alternative in {@link JvmDslParserParser#bitAndExpression}.
	 * @param ctx the parse tree
	 */
	void exitParenBitAndOpParenExpr(JvmDslParserParser.ParenBitAndOpParenExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ParenBitAnOpExpr}
	 * labeled alternative in {@link JvmDslParserParser#bitAndExpression}.
	 * @param ctx the parse tree
	 */
	void enterParenBitAnOpExpr(JvmDslParserParser.ParenBitAnOpExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ParenBitAnOpExpr}
	 * labeled alternative in {@link JvmDslParserParser#bitAndExpression}.
	 * @param ctx the parse tree
	 */
	void exitParenBitAnOpExpr(JvmDslParserParser.ParenBitAnOpExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BitAndOpExpr}
	 * labeled alternative in {@link JvmDslParserParser#bitAndExpression}.
	 * @param ctx the parse tree
	 */
	void enterBitAndOpExpr(JvmDslParserParser.BitAndOpExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BitAndOpExpr}
	 * labeled alternative in {@link JvmDslParserParser#bitAndExpression}.
	 * @param ctx the parse tree
	 */
	void exitBitAndOpExpr(JvmDslParserParser.BitAndOpExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#equalityOperation}.
	 * @param ctx the parse tree
	 */
	void enterEqualityOperation(JvmDslParserParser.EqualityOperationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#equalityOperation}.
	 * @param ctx the parse tree
	 */
	void exitEqualityOperation(JvmDslParserParser.EqualityOperationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code EqualExpr}
	 * labeled alternative in {@link JvmDslParserParser#equalityExpression}.
	 * @param ctx the parse tree
	 */
	void enterEqualExpr(JvmDslParserParser.EqualExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code EqualExpr}
	 * labeled alternative in {@link JvmDslParserParser#equalityExpression}.
	 * @param ctx the parse tree
	 */
	void exitEqualExpr(JvmDslParserParser.EqualExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ParenEqualParenExpr}
	 * labeled alternative in {@link JvmDslParserParser#equalityExpression}.
	 * @param ctx the parse tree
	 */
	void enterParenEqualParenExpr(JvmDslParserParser.ParenEqualParenExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ParenEqualParenExpr}
	 * labeled alternative in {@link JvmDslParserParser#equalityExpression}.
	 * @param ctx the parse tree
	 */
	void exitParenEqualParenExpr(JvmDslParserParser.ParenEqualParenExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RelationExpr}
	 * labeled alternative in {@link JvmDslParserParser#equalityExpression}.
	 * @param ctx the parse tree
	 */
	void enterRelationExpr(JvmDslParserParser.RelationExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RelationExpr}
	 * labeled alternative in {@link JvmDslParserParser#equalityExpression}.
	 * @param ctx the parse tree
	 */
	void exitRelationExpr(JvmDslParserParser.RelationExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ParenEqualExpr}
	 * labeled alternative in {@link JvmDslParserParser#equalityExpression}.
	 * @param ctx the parse tree
	 */
	void enterParenEqualExpr(JvmDslParserParser.ParenEqualExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ParenEqualExpr}
	 * labeled alternative in {@link JvmDslParserParser#equalityExpression}.
	 * @param ctx the parse tree
	 */
	void exitParenEqualExpr(JvmDslParserParser.ParenEqualExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code EqualParenExpr}
	 * labeled alternative in {@link JvmDslParserParser#equalityExpression}.
	 * @param ctx the parse tree
	 */
	void enterEqualParenExpr(JvmDslParserParser.EqualParenExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code EqualParenExpr}
	 * labeled alternative in {@link JvmDslParserParser#equalityExpression}.
	 * @param ctx the parse tree
	 */
	void exitEqualParenExpr(JvmDslParserParser.EqualParenExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#relationOperation}.
	 * @param ctx the parse tree
	 */
	void enterRelationOperation(JvmDslParserParser.RelationOperationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#relationOperation}.
	 * @param ctx the parse tree
	 */
	void exitRelationOperation(JvmDslParserParser.RelationOperationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ParenRelationOpParenExpr}
	 * labeled alternative in {@link JvmDslParserParser#relationExpression}.
	 * @param ctx the parse tree
	 */
	void enterParenRelationOpParenExpr(JvmDslParserParser.ParenRelationOpParenExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ParenRelationOpParenExpr}
	 * labeled alternative in {@link JvmDslParserParser#relationExpression}.
	 * @param ctx the parse tree
	 */
	void exitParenRelationOpParenExpr(JvmDslParserParser.ParenRelationOpParenExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RelationOpParenExpr}
	 * labeled alternative in {@link JvmDslParserParser#relationExpression}.
	 * @param ctx the parse tree
	 */
	void enterRelationOpParenExpr(JvmDslParserParser.RelationOpParenExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RelationOpParenExpr}
	 * labeled alternative in {@link JvmDslParserParser#relationExpression}.
	 * @param ctx the parse tree
	 */
	void exitRelationOpParenExpr(JvmDslParserParser.RelationOpParenExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ParenRelationOpExpr}
	 * labeled alternative in {@link JvmDslParserParser#relationExpression}.
	 * @param ctx the parse tree
	 */
	void enterParenRelationOpExpr(JvmDslParserParser.ParenRelationOpExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ParenRelationOpExpr}
	 * labeled alternative in {@link JvmDslParserParser#relationExpression}.
	 * @param ctx the parse tree
	 */
	void exitParenRelationOpExpr(JvmDslParserParser.ParenRelationOpExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RelationOpExpr}
	 * labeled alternative in {@link JvmDslParserParser#relationExpression}.
	 * @param ctx the parse tree
	 */
	void enterRelationOpExpr(JvmDslParserParser.RelationOpExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RelationOpExpr}
	 * labeled alternative in {@link JvmDslParserParser#relationExpression}.
	 * @param ctx the parse tree
	 */
	void exitRelationOpExpr(JvmDslParserParser.RelationOpExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ShiftExpr}
	 * labeled alternative in {@link JvmDslParserParser#relationExpression}.
	 * @param ctx the parse tree
	 */
	void enterShiftExpr(JvmDslParserParser.ShiftExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ShiftExpr}
	 * labeled alternative in {@link JvmDslParserParser#relationExpression}.
	 * @param ctx the parse tree
	 */
	void exitShiftExpr(JvmDslParserParser.ShiftExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#shiftOperation}.
	 * @param ctx the parse tree
	 */
	void enterShiftOperation(JvmDslParserParser.ShiftOperationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#shiftOperation}.
	 * @param ctx the parse tree
	 */
	void exitShiftOperation(JvmDslParserParser.ShiftOperationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AddExpr}
	 * labeled alternative in {@link JvmDslParserParser#shiftExpression}.
	 * @param ctx the parse tree
	 */
	void enterAddExpr(JvmDslParserParser.AddExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AddExpr}
	 * labeled alternative in {@link JvmDslParserParser#shiftExpression}.
	 * @param ctx the parse tree
	 */
	void exitAddExpr(JvmDslParserParser.AddExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ShiftOpExpr}
	 * labeled alternative in {@link JvmDslParserParser#shiftExpression}.
	 * @param ctx the parse tree
	 */
	void enterShiftOpExpr(JvmDslParserParser.ShiftOpExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ShiftOpExpr}
	 * labeled alternative in {@link JvmDslParserParser#shiftExpression}.
	 * @param ctx the parse tree
	 */
	void exitShiftOpExpr(JvmDslParserParser.ShiftOpExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ParenShiftOpExpr}
	 * labeled alternative in {@link JvmDslParserParser#shiftExpression}.
	 * @param ctx the parse tree
	 */
	void enterParenShiftOpExpr(JvmDslParserParser.ParenShiftOpExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ParenShiftOpExpr}
	 * labeled alternative in {@link JvmDslParserParser#shiftExpression}.
	 * @param ctx the parse tree
	 */
	void exitParenShiftOpExpr(JvmDslParserParser.ParenShiftOpExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ParenShiftOpParenExpr}
	 * labeled alternative in {@link JvmDslParserParser#shiftExpression}.
	 * @param ctx the parse tree
	 */
	void enterParenShiftOpParenExpr(JvmDslParserParser.ParenShiftOpParenExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ParenShiftOpParenExpr}
	 * labeled alternative in {@link JvmDslParserParser#shiftExpression}.
	 * @param ctx the parse tree
	 */
	void exitParenShiftOpParenExpr(JvmDslParserParser.ParenShiftOpParenExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ShiftOpParenExpr}
	 * labeled alternative in {@link JvmDslParserParser#shiftExpression}.
	 * @param ctx the parse tree
	 */
	void enterShiftOpParenExpr(JvmDslParserParser.ShiftOpParenExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ShiftOpParenExpr}
	 * labeled alternative in {@link JvmDslParserParser#shiftExpression}.
	 * @param ctx the parse tree
	 */
	void exitShiftOpParenExpr(JvmDslParserParser.ShiftOpParenExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#additiveOperation}.
	 * @param ctx the parse tree
	 */
	void enterAdditiveOperation(JvmDslParserParser.AdditiveOperationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#additiveOperation}.
	 * @param ctx the parse tree
	 */
	void exitAdditiveOperation(JvmDslParserParser.AdditiveOperationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MultiExpr}
	 * labeled alternative in {@link JvmDslParserParser#additiveExpression}.
	 * @param ctx the parse tree
	 */
	void enterMultiExpr(JvmDslParserParser.MultiExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MultiExpr}
	 * labeled alternative in {@link JvmDslParserParser#additiveExpression}.
	 * @param ctx the parse tree
	 */
	void exitMultiExpr(JvmDslParserParser.MultiExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ParenAddOpExpr}
	 * labeled alternative in {@link JvmDslParserParser#additiveExpression}.
	 * @param ctx the parse tree
	 */
	void enterParenAddOpExpr(JvmDslParserParser.ParenAddOpExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ParenAddOpExpr}
	 * labeled alternative in {@link JvmDslParserParser#additiveExpression}.
	 * @param ctx the parse tree
	 */
	void exitParenAddOpExpr(JvmDslParserParser.ParenAddOpExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AddOpParenExpr}
	 * labeled alternative in {@link JvmDslParserParser#additiveExpression}.
	 * @param ctx the parse tree
	 */
	void enterAddOpParenExpr(JvmDslParserParser.AddOpParenExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AddOpParenExpr}
	 * labeled alternative in {@link JvmDslParserParser#additiveExpression}.
	 * @param ctx the parse tree
	 */
	void exitAddOpParenExpr(JvmDslParserParser.AddOpParenExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AddOpExpr}
	 * labeled alternative in {@link JvmDslParserParser#additiveExpression}.
	 * @param ctx the parse tree
	 */
	void enterAddOpExpr(JvmDslParserParser.AddOpExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AddOpExpr}
	 * labeled alternative in {@link JvmDslParserParser#additiveExpression}.
	 * @param ctx the parse tree
	 */
	void exitAddOpExpr(JvmDslParserParser.AddOpExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ParenAddOpParenExpr}
	 * labeled alternative in {@link JvmDslParserParser#additiveExpression}.
	 * @param ctx the parse tree
	 */
	void enterParenAddOpParenExpr(JvmDslParserParser.ParenAddOpParenExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ParenAddOpParenExpr}
	 * labeled alternative in {@link JvmDslParserParser#additiveExpression}.
	 * @param ctx the parse tree
	 */
	void exitParenAddOpParenExpr(JvmDslParserParser.ParenAddOpParenExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#multiplicativeOperation}.
	 * @param ctx the parse tree
	 */
	void enterMultiplicativeOperation(JvmDslParserParser.MultiplicativeOperationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#multiplicativeOperation}.
	 * @param ctx the parse tree
	 */
	void exitMultiplicativeOperation(JvmDslParserParser.MultiplicativeOperationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MultiOpExpr}
	 * labeled alternative in {@link JvmDslParserParser#multiplicativeExpression}.
	 * @param ctx the parse tree
	 */
	void enterMultiOpExpr(JvmDslParserParser.MultiOpExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MultiOpExpr}
	 * labeled alternative in {@link JvmDslParserParser#multiplicativeExpression}.
	 * @param ctx the parse tree
	 */
	void exitMultiOpExpr(JvmDslParserParser.MultiOpExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ParenMultiOpExpr}
	 * labeled alternative in {@link JvmDslParserParser#multiplicativeExpression}.
	 * @param ctx the parse tree
	 */
	void enterParenMultiOpExpr(JvmDslParserParser.ParenMultiOpExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ParenMultiOpExpr}
	 * labeled alternative in {@link JvmDslParserParser#multiplicativeExpression}.
	 * @param ctx the parse tree
	 */
	void exitParenMultiOpExpr(JvmDslParserParser.ParenMultiOpExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ParenMultiOpParenExpr}
	 * labeled alternative in {@link JvmDslParserParser#multiplicativeExpression}.
	 * @param ctx the parse tree
	 */
	void enterParenMultiOpParenExpr(JvmDslParserParser.ParenMultiOpParenExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ParenMultiOpParenExpr}
	 * labeled alternative in {@link JvmDslParserParser#multiplicativeExpression}.
	 * @param ctx the parse tree
	 */
	void exitParenMultiOpParenExpr(JvmDslParserParser.ParenMultiOpParenExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MultiOpParenExpr}
	 * labeled alternative in {@link JvmDslParserParser#multiplicativeExpression}.
	 * @param ctx the parse tree
	 */
	void enterMultiOpParenExpr(JvmDslParserParser.MultiOpParenExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MultiOpParenExpr}
	 * labeled alternative in {@link JvmDslParserParser#multiplicativeExpression}.
	 * @param ctx the parse tree
	 */
	void exitMultiOpParenExpr(JvmDslParserParser.MultiOpParenExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code UnaryExpr}
	 * labeled alternative in {@link JvmDslParserParser#multiplicativeExpression}.
	 * @param ctx the parse tree
	 */
	void enterUnaryExpr(JvmDslParserParser.UnaryExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code UnaryExpr}
	 * labeled alternative in {@link JvmDslParserParser#multiplicativeExpression}.
	 * @param ctx the parse tree
	 */
	void exitUnaryExpr(JvmDslParserParser.UnaryExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LiteralAndFuncCallExpr}
	 * labeled alternative in {@link JvmDslParserParser#unaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterLiteralAndFuncCallExpr(JvmDslParserParser.LiteralAndFuncCallExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LiteralAndFuncCallExpr}
	 * labeled alternative in {@link JvmDslParserParser#unaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitLiteralAndFuncCallExpr(JvmDslParserParser.LiteralAndFuncCallExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code OppositeExpr}
	 * labeled alternative in {@link JvmDslParserParser#unaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterOppositeExpr(JvmDslParserParser.OppositeExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code OppositeExpr}
	 * labeled alternative in {@link JvmDslParserParser#unaryExpression}.
	 * @param ctx the parse tree
     */
    void exitOppositeExpr(JvmDslParserParser.OppositeExprContext ctx);

    /**
     * Enter a parse tree produced by the {@code NegateExpr}
     * labeled alternative in {@link JvmDslParserParser#unaryExpression}.
     *
     * @param ctx the parse tree
     */
    void enterNegateExpr(JvmDslParserParser.NegateExprContext ctx);

    /**
     * Exit a parse tree produced by the {@code NegateExpr}
     * labeled alternative in {@link JvmDslParserParser#unaryExpression}.
     *
     * @param ctx the parse tree
     */
    void exitNegateExpr(JvmDslParserParser.NegateExprContext ctx);

    /**
     * Enter a parse tree produced by the {@code CastExpr}
     * labeled alternative in {@link JvmDslParserParser#unaryExpression}.
     *
     * @param ctx the parse tree
     */
    void enterCastExpr(JvmDslParserParser.CastExprContext ctx);

    /**
     * Exit a parse tree produced by the {@code CastExpr}
     * labeled alternative in {@link JvmDslParserParser#unaryExpression}.
     *
     * @param ctx the parse tree
     */
    void exitCastExpr(JvmDslParserParser.CastExprContext ctx);

    /**
     * Enter a parse tree produced by the {@code InstanceofExpr}
     * labeled alternative in {@link JvmDslParserParser#unaryExpression}.
     *
     * @param ctx the parse tree
     */
    void enterInstanceofExpr(JvmDslParserParser.InstanceofExprContext ctx);

    /**
     * Exit a parse tree produced by the {@code InstanceofExpr}
     * labeled alternative in {@link JvmDslParserParser#unaryExpression}.
     *
     * @param ctx the parse tree
     */
    void exitInstanceofExpr(JvmDslParserParser.InstanceofExprContext ctx);

    /**
     * Enter a parse tree produced by the {@code ParenExpr}
     * labeled alternative in {@link JvmDslParserParser#unaryExpression}.
     *
     * @param ctx the parse tree
     */
    void enterParenExpr(JvmDslParserParser.ParenExprContext ctx);

    /**
     * Exit a parse tree produced by the {@code ParenExpr}
     * labeled alternative in {@link JvmDslParserParser#unaryExpression}.
     *
     * @param ctx the parse tree
     */
    void exitParenExpr(JvmDslParserParser.ParenExprContext ctx);

    /**
     * Enter a parse tree produced by the {@code ParamsLambdaExpr}
     * labeled alternative in {@link JvmDslParserParser#lambdaExpression}.
     *
     * @param ctx the parse tree
     */
    void enterParamsLambdaExpr(JvmDslParserParser.ParamsLambdaExprContext ctx);

    /**
     * Exit a parse tree produced by the {@code ParamsLambdaExpr}
     * labeled alternative in {@link JvmDslParserParser#lambdaExpression}.
     *
     * @param ctx the parse tree
     */
    void exitParamsLambdaExpr(JvmDslParserParser.ParamsLambdaExprContext ctx);

    /**
     * Enter a parse tree produced by the {@code NoParamLambdaExpr}
     * labeled alternative in {@link JvmDslParserParser#lambdaExpression}.
     *
     * @param ctx the parse tree
     */
    void enterNoParamLambdaExpr(JvmDslParserParser.NoParamLambdaExprContext ctx);

    /**
     * Exit a parse tree produced by the {@code NoParamLambdaExpr}
     * labeled alternative in {@link JvmDslParserParser#lambdaExpression}.
     *
     * @param ctx the parse tree
     */
    void exitNoParamLambdaExpr(JvmDslParserParser.NoParamLambdaExprContext ctx);

    /**
     * Enter a parse tree produced by the {@code OneParamLambdaExpr}
     * labeled alternative in {@link JvmDslParserParser#lambdaExpression}.
     *
     * @param ctx the parse tree
     */
    void enterOneParamLambdaExpr(JvmDslParserParser.OneParamLambdaExprContext ctx);

    /**
     * Exit a parse tree produced by the {@code OneParamLambdaExpr}
     * labeled alternative in {@link JvmDslParserParser#lambdaExpression}.
     *
     * @param ctx the parse tree
     */
    void exitOneParamLambdaExpr(JvmDslParserParser.OneParamLambdaExprContext ctx);

    /**
     * Enter a parse tree produced by {@link JvmDslParserParser#matchCaseExpression}.
     *
     * @param ctx the parse tree
     */
    void enterMatchCaseExpression(JvmDslParserParser.MatchCaseExpressionContext ctx);

    /**
     * Exit a parse tree produced by {@link JvmDslParserParser#matchCaseExpression}.
     *
     * @param ctx the parse tree
     */
    void exitMatchCaseExpression(JvmDslParserParser.MatchCaseExpressionContext ctx);

    /**
     * Enter a parse tree produced by {@link JvmDslParserParser#caseExpression}.
     *
     * @param ctx the parse tree
     */
    void enterCaseExpression(JvmDslParserParser.CaseExpressionContext ctx);

    /**
     * Exit a parse tree produced by {@link JvmDslParserParser#caseExpression}.
     *
     * @param ctx the parse tree
     */
    void exitCaseExpression(JvmDslParserParser.CaseExpressionContext ctx);

    /**
     * Enter a parse tree produced by the {@code unapplyTupleExpr}
     * labeled alternative in {@link JvmDslParserParser#unapplyExpression}.
     *
     * @param ctx the parse tree
     */
    void enterUnapplyTupleExpr(JvmDslParserParser.UnapplyTupleExprContext ctx);

    /**
     * Exit a parse tree produced by the {@code unapplyTupleExpr}
     * labeled alternative in {@link JvmDslParserParser#unapplyExpression}.
     *
     * @param ctx the parse tree
     */
    void exitUnapplyTupleExpr(JvmDslParserParser.UnapplyTupleExprContext ctx);

    /**
     * Enter a parse tree produced by the {@code unapplyListExpr}
     * labeled alternative in {@link JvmDslParserParser#unapplyExpression}.
     *
     * @param ctx the parse tree
     */
    void enterUnapplyListExpr(JvmDslParserParser.UnapplyListExprContext ctx);

    /**
     * Exit a parse tree produced by the {@code unapplyListExpr}
     * labeled alternative in {@link JvmDslParserParser#unapplyExpression}.
     *
     * @param ctx the parse tree
     */
    void exitUnapplyListExpr(JvmDslParserParser.UnapplyListExprContext ctx);

    /**
     * Enter a parse tree produced by the {@code unapplyClazzExpr}
     * labeled alternative in {@link JvmDslParserParser#unapplyExpression}.
     *
     * @param ctx the parse tree
     */
    void enterUnapplyClazzExpr(JvmDslParserParser.UnapplyClazzExprContext ctx);

    /**
     * Exit a parse tree produced by the {@code unapplyClazzExpr}
     * labeled alternative in {@link JvmDslParserParser#unapplyExpression}.
     *
     * @param ctx the parse tree
     */
    void exitUnapplyClazzExpr(JvmDslParserParser.UnapplyClazzExprContext ctx);

    /**
     * Enter a parse tree produced by the {@code unapplyVarExpr}
     * labeled alternative in {@link JvmDslParserParser#unapplyExpression}.
     *
     * @param ctx the parse tree
     */
    void enterUnapplyVarExpr(JvmDslParserParser.UnapplyVarExprContext ctx);

    /**
     * Exit a parse tree produced by the {@code unapplyVarExpr}
     * labeled alternative in {@link JvmDslParserParser#unapplyExpression}.
     *
     * @param ctx the parse tree
     */
    void exitUnapplyVarExpr(JvmDslParserParser.UnapplyVarExprContext ctx);

    /**
     * Enter a parse tree produced by the {@code unapplyHeadExpr}
     * labeled alternative in {@link JvmDslParserParser#unapplyExpression}.
     *
     * @param ctx the parse tree
     */
    void enterUnapplyHeadExpr(JvmDslParserParser.UnapplyHeadExprContext ctx);

    /**
     * Exit a parse tree produced by the {@code unapplyHeadExpr}
     * labeled alternative in {@link JvmDslParserParser#unapplyExpression}.
     *
     * @param ctx the parse tree
     */
    void exitUnapplyHeadExpr(JvmDslParserParser.UnapplyHeadExprContext ctx);

    /**
     * Enter a parse tree produced by the {@code unapplyLiteralExpr}
     * labeled alternative in {@link JvmDslParserParser#unapplyExpression}.
     *
     * @param ctx the parse tree
     */
    void enterUnapplyLiteralExpr(JvmDslParserParser.UnapplyLiteralExprContext ctx);

    /**
     * Exit a parse tree produced by the {@code unapplyLiteralExpr}
     * labeled alternative in {@link JvmDslParserParser#unapplyExpression}.
     *
     * @param ctx the parse tree
     */
    void exitUnapplyLiteralExpr(JvmDslParserParser.UnapplyLiteralExprContext ctx);

    /**
     * Enter a parse tree produced by {@link JvmDslParserParser#typeMatchExpression}.
	 * @param ctx the parse tree
	 */
	void enterTypeMatchExpression(JvmDslParserParser.TypeMatchExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#typeMatchExpression}.
	 * @param ctx the parse tree
	 */
	void exitTypeMatchExpression(JvmDslParserParser.TypeMatchExpressionContext ctx);
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
     * Enter a parse tree produced by the {@code LambdaType}
     * labeled alternative in {@link JvmDslParserParser#type}.
     *
     * @param ctx the parse tree
     */
    void enterLambdaType(JvmDslParserParser.LambdaTypeContext ctx);

    /**
     * Exit a parse tree produced by the {@code LambdaType}
     * labeled alternative in {@link JvmDslParserParser#type}.
     *
     * @param ctx the parse tree
     */
    void exitLambdaType(JvmDslParserParser.LambdaTypeContext ctx);

    /**
     * Enter a parse tree produced by the {@code BoolType}
     * labeled alternative in {@link JvmDslParserParser#type}.
     *
     * @param ctx the parse tree
     */
    void enterBoolType(JvmDslParserParser.BoolTypeContext ctx);

    /**
     * Exit a parse tree produced by the {@code BoolType}
     * labeled alternative in {@link JvmDslParserParser#type}.
     *
     * @param ctx the parse tree
     */
    void exitBoolType(JvmDslParserParser.BoolTypeContext ctx);

    /**
     * Enter a parse tree produced by the {@code FutureType}
     * labeled alternative in {@link JvmDslParserParser#type}.
     *
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
	 * Enter a parse tree produced by the {@code SupplierType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 */
	void enterSupplierType(JvmDslParserParser.SupplierTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SupplierType}
	 * labeled alternative in {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 */
	void exitSupplierType(JvmDslParserParser.SupplierTypeContext ctx);
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
     *
     * @param ctx the parse tree
     */
    void exitFloatType(JvmDslParserParser.FloatTypeContext ctx);

    /**
     * Enter a parse tree produced by the {@code SetType}
     * labeled alternative in {@link JvmDslParserParser#type}.
     *
     * @param ctx the parse tree
     */
    void enterSetType(JvmDslParserParser.SetTypeContext ctx);

    /**
     * Exit a parse tree produced by the {@code SetType}
     * labeled alternative in {@link JvmDslParserParser#type}.
     *
     * @param ctx the parse tree
     */
    void exitSetType(JvmDslParserParser.SetTypeContext ctx);

    /**
     * Enter a parse tree produced by the {@code DoubleType}
     * labeled alternative in {@link JvmDslParserParser#type}.
     *
     * @param ctx the parse tree
     */
    void enterDoubleType(JvmDslParserParser.DoubleTypeContext ctx);

    /**
     * Exit a parse tree produced by the {@code DoubleType}
     * labeled alternative in {@link JvmDslParserParser#type}.
     *
     * @param ctx the parse tree
     */
    void exitDoubleType(JvmDslParserParser.DoubleTypeContext ctx);

    /**
     * Enter a parse tree produced by the {@code ListType}
     * labeled alternative in {@link JvmDslParserParser#type}.
     *
     * @param ctx the parse tree
     */
    void enterListType(JvmDslParserParser.ListTypeContext ctx);

    /**
     * Exit a parse tree produced by the {@code ListType}
     * labeled alternative in {@link JvmDslParserParser#type}.
     *
     * @param ctx the parse tree
     */
    void exitListType(JvmDslParserParser.ListTypeContext ctx);

    /**
     * Enter a parse tree produced by the {@code TupleType}
     * labeled alternative in {@link JvmDslParserParser#type}.
     *
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
     *
     * @param ctx the parse tree
     */
    void exitVarDef(JvmDslParserParser.VarDefContext ctx);

    /**
     * Enter a parse tree produced by {@link JvmDslParserParser#classDef}.
     *
     * @param ctx the parse tree
     */
    void enterClassDef(JvmDslParserParser.ClassDefContext ctx);

    /**
     * Exit a parse tree produced by {@link JvmDslParserParser#classDef}.
     *
     * @param ctx the parse tree
     */
    void exitClassDef(JvmDslParserParser.ClassDefContext ctx);

    /**
     * Enter a parse tree produced by {@link JvmDslParserParser#funcCallChain}.
     *
     * @param ctx the parse tree
     */
    void enterFuncCallChain(JvmDslParserParser.FuncCallChainContext ctx);

    /**
     * Exit a parse tree produced by {@link JvmDslParserParser#funcCallChain}.
     *
     * @param ctx the parse tree
     */
    void exitFuncCallChain(JvmDslParserParser.FuncCallChainContext ctx);

    /**
     * Enter a parse tree produced by {@link JvmDslParserParser#literalCallChain}.
     *
     * @param ctx the parse tree
     */
    void enterLiteralCallChain(JvmDslParserParser.LiteralCallChainContext ctx);

    /**
     * Exit a parse tree produced by {@link JvmDslParserParser#literalCallChain}.
     *
     * @param ctx the parse tree
     */
    void exitLiteralCallChain(JvmDslParserParser.LiteralCallChainContext ctx);

    /**
     * Enter a parse tree produced by {@link JvmDslParserParser#part}.
     *
     * @param ctx the parse tree
     */
    void enterPart(JvmDslParserParser.PartContext ctx);

    /**
     * Exit a parse tree produced by {@link JvmDslParserParser#part}.
     *
     * @param ctx the parse tree
     */
    void exitPart(JvmDslParserParser.PartContext ctx);

    /**
     * Enter a parse tree produced by the {@code VarCallNoArgs}
     * labeled alternative in {@link JvmDslParserParser#funcCall}.
     *
     * @param ctx the parse tree
     */
    void enterVarCallNoArgs(JvmDslParserParser.VarCallNoArgsContext ctx);

    /**
     * Exit a parse tree produced by the {@code VarCallNoArgs}
     * labeled alternative in {@link JvmDslParserParser#funcCall}.
     *
     * @param ctx the parse tree
     */
    void exitVarCallNoArgs(JvmDslParserParser.VarCallNoArgsContext ctx);

    /**
     * Enter a parse tree produced by the {@code VarCallArgs}
     * labeled alternative in {@link JvmDslParserParser#funcCall}.
     *
     * @param ctx the parse tree
     */
    void enterVarCallArgs(JvmDslParserParser.VarCallArgsContext ctx);

    /**
     * Exit a parse tree produced by the {@code VarCallArgs}
     * labeled alternative in {@link JvmDslParserParser#funcCall}.
     *
     * @param ctx the parse tree
     */
    void exitVarCallArgs(JvmDslParserParser.VarCallArgsContext ctx);

    /**
     * Enter a parse tree produced by the {@code LiteralCallNoArgs}
     * labeled alternative in {@link JvmDslParserParser#funcCall}.
     *
     * @param ctx the parse tree
     */
    void enterLiteralCallNoArgs(JvmDslParserParser.LiteralCallNoArgsContext ctx);

    /**
     * Exit a parse tree produced by the {@code LiteralCallNoArgs}
     * labeled alternative in {@link JvmDslParserParser#funcCall}.
     *
     * @param ctx the parse tree
     */
    void exitLiteralCallNoArgs(JvmDslParserParser.LiteralCallNoArgsContext ctx);

    /**
     * Enter a parse tree produced by the {@code LiteralCallArgs}
     * labeled alternative in {@link JvmDslParserParser#funcCall}.
     *
     * @param ctx the parse tree
     */
    void enterLiteralCallArgs(JvmDslParserParser.LiteralCallArgsContext ctx);

    /**
     * Exit a parse tree produced by the {@code LiteralCallArgs}
     * labeled alternative in {@link JvmDslParserParser#funcCall}.
     *
     * @param ctx the parse tree
     */
    void exitLiteralCallArgs(JvmDslParserParser.LiteralCallArgsContext ctx);

    /**
     * Enter a parse tree produced by the {@code TypeCallNoArgs}
     * labeled alternative in {@link JvmDslParserParser#funcCall}.
     *
     * @param ctx the parse tree
     */
    void enterTypeCallNoArgs(JvmDslParserParser.TypeCallNoArgsContext ctx);

    /**
     * Exit a parse tree produced by the {@code TypeCallNoArgs}
     * labeled alternative in {@link JvmDslParserParser#funcCall}.
     *
     * @param ctx the parse tree
     */
    void exitTypeCallNoArgs(JvmDslParserParser.TypeCallNoArgsContext ctx);

    /**
     * Enter a parse tree produced by the {@code TypeCallArgs}
     * labeled alternative in {@link JvmDslParserParser#funcCall}.
     *
     * @param ctx the parse tree
     */
    void enterTypeCallArgs(JvmDslParserParser.TypeCallArgsContext ctx);

    /**
     * Exit a parse tree produced by the {@code TypeCallArgs}
     * labeled alternative in {@link JvmDslParserParser#funcCall}.
     *
     * @param ctx the parse tree
     */
    void exitTypeCallArgs(JvmDslParserParser.TypeCallArgsContext ctx);

    /**
     * Enter a parse tree produced by {@link JvmDslParserParser#funcDef}.
     *
     * @param ctx the parse tree
     */
    void enterFuncDef(JvmDslParserParser.FuncDefContext ctx);

    /**
     * Exit a parse tree produced by {@link JvmDslParserParser#funcDef}.
     *
     * @param ctx the parse tree
     */
    void exitFuncDef(JvmDslParserParser.FuncDefContext ctx);

    /**
     * Enter a parse tree produced by {@link JvmDslParserParser#fieldDef}.
     *
     * @param ctx the parse tree
     */
    void enterFieldDef(JvmDslParserParser.FieldDefContext ctx);

    /**
     * Exit a parse tree produced by {@link JvmDslParserParser#fieldDef}.
     *
     * @param ctx the parse tree
     */
    void exitFieldDef(JvmDslParserParser.FieldDefContext ctx);

    /**
     * Enter a parse tree produced by the {@code FuncCallChainExpr}
     * labeled alternative in {@link JvmDslParserParser#literalAndCallChain}.
     *
     * @param ctx the parse tree
     */
    void enterFuncCallChainExpr(JvmDslParserParser.FuncCallChainExprContext ctx);

    /**
     * Exit a parse tree produced by the {@code FuncCallChainExpr}
     * labeled alternative in {@link JvmDslParserParser#literalAndCallChain}.
     *
     * @param ctx the parse tree
     */
    void exitFuncCallChainExpr(JvmDslParserParser.FuncCallChainExprContext ctx);

    /**
     * Enter a parse tree produced by the {@code LiteralCallChainExpr}
     * labeled alternative in {@link JvmDslParserParser#literalAndCallChain}.
     *
     * @param ctx the parse tree
     */
    void enterLiteralCallChainExpr(JvmDslParserParser.LiteralCallChainExprContext ctx);

    /**
     * Exit a parse tree produced by the {@code LiteralCallChainExpr}
     * labeled alternative in {@link JvmDslParserParser#literalAndCallChain}.
     *
     * @param ctx the parse tree
     */
    void exitLiteralCallChainExpr(JvmDslParserParser.LiteralCallChainExprContext ctx);

    /**
     * Enter a parse tree produced by the {@code LiteralExpr}
     * labeled alternative in {@link JvmDslParserParser#literalAndCallChain}.
     *
     * @param ctx the parse tree
     */
    void enterLiteralExpr(JvmDslParserParser.LiteralExprContext ctx);

    /**
     * Exit a parse tree produced by the {@code LiteralExpr}
     * labeled alternative in {@link JvmDslParserParser#literalAndCallChain}.
     *
     * @param ctx the parse tree
     */
    void exitLiteralExpr(JvmDslParserParser.LiteralExprContext ctx);

    /**
     * Enter a parse tree produced by {@link JvmDslParserParser#literal}.
     *
     * @param ctx the parse tree
     */
    void enterLiteral(JvmDslParserParser.LiteralContext ctx);

    /**
     * Exit a parse tree produced by {@link JvmDslParserParser#literal}.
     *
     * @param ctx the parse tree
     */
    void exitLiteral(JvmDslParserParser.LiteralContext ctx);

    /**
     * Enter a parse tree produced by {@link JvmDslParserParser#baseLiteral}.
     *
     * @param ctx the parse tree
     */
    void enterBaseLiteral(JvmDslParserParser.BaseLiteralContext ctx);

    /**
     * Exit a parse tree produced by {@link JvmDslParserParser#baseLiteral}.
     *
     * @param ctx the parse tree
     */
    void exitBaseLiteral(JvmDslParserParser.BaseLiteralContext ctx);

    /**
     * Enter a parse tree produced by {@link JvmDslParserParser#numberLiteral}.
     *
     * @param ctx the parse tree
     */
    void enterNumberLiteral(JvmDslParserParser.NumberLiteralContext ctx);

    /**
     * Exit a parse tree produced by {@link JvmDslParserParser#numberLiteral}.
     *
     * @param ctx the parse tree
     */
    void exitNumberLiteral(JvmDslParserParser.NumberLiteralContext ctx);

    /**
     * Enter a parse tree produced by {@link JvmDslParserParser#listLiteral}.
     *
     * @param ctx the parse tree
     */
    void enterListLiteral(JvmDslParserParser.ListLiteralContext ctx);

    /**
     * Exit a parse tree produced by {@link JvmDslParserParser#listLiteral}.
     *
     * @param ctx the parse tree
     */
    void exitListLiteral(JvmDslParserParser.ListLiteralContext ctx);

    /**
     * Enter a parse tree produced by {@link JvmDslParserParser#blockExpression}.
     *
     * @param ctx the parse tree
     */
    void enterBlockExpression(JvmDslParserParser.BlockExpressionContext ctx);

    /**
     * Exit a parse tree produced by {@link JvmDslParserParser#blockExpression}.
     *
     * @param ctx the parse tree
     */
    void exitBlockExpression(JvmDslParserParser.BlockExpressionContext ctx);

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
	 * Enter a parse tree produced by {@link JvmDslParserParser#packagePath}.
	 * @param ctx the parse tree
	 */
	void enterPackagePath(JvmDslParserParser.PackagePathContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#packagePath}.
	 * @param ctx the parse tree
	 */
	void exitPackagePath(JvmDslParserParser.PackagePathContext ctx);
	/**
	 * Enter a parse tree produced by {@link JvmDslParserParser#packageName}.
	 * @param ctx the parse tree
	 */
	void enterPackageName(JvmDslParserParser.PackageNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link JvmDslParserParser#packageName}.
	 * @param ctx the parse tree
	 */
	void exitPackageName(JvmDslParserParser.PackageNameContext ctx);
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