// Generated from com.dongjiaqiang.jvm.dsl.core/JvmDslParser.g4 by ANTLR 4.9.3
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
	 * Visit a parse tree produced by {@link JvmDslParserParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(JvmDslParserParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#blockStatements}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockStatements(JvmDslParserParser.BlockStatementsContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#blockStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockStatement(JvmDslParserParser.BlockStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(JvmDslParserParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#synchronizedStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSynchronizedStatement(JvmDslParserParser.SynchronizedStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#throwStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitThrowStatement(JvmDslParserParser.ThrowStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#returnStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnStatement(JvmDslParserParser.ReturnStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#breakStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBreakStatement(JvmDslParserParser.BreakStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#continueStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContinueStatement(JvmDslParserParser.ContinueStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#importClazzStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImportClazzStatement(JvmDslParserParser.ImportClazzStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#usingJarStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUsingJarStatement(JvmDslParserParser.UsingJarStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#assertStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssertStatement(JvmDslParserParser.AssertStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#tryStatement}.
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
	 * Visit a parse tree produced by {@link JvmDslParserParser#forStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForStatement(JvmDslParserParser.ForStatementContext ctx);
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
	 * Visit a parse tree produced by {@link JvmDslParserParser#assignOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignOperator(JvmDslParserParser.AssignOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#conditionalOrExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditionalOrExpression(JvmDslParserParser.ConditionalOrExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#conditionalAndExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditionalAndExpression(JvmDslParserParser.ConditionalAndExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#inclusiveOrExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInclusiveOrExpression(JvmDslParserParser.InclusiveOrExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#exclusiveOrExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExclusiveOrExpression(JvmDslParserParser.ExclusiveOrExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#andExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndExpression(JvmDslParserParser.AndExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Relation}
	 * labeled alternative in {@link JvmDslParserParser#equalityExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelation(JvmDslParserParser.RelationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NotEqual}
	 * labeled alternative in {@link JvmDslParserParser#equalityExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotEqual(JvmDslParserParser.NotEqualContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Equal}
	 * labeled alternative in {@link JvmDslParserParser#equalityExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqual(JvmDslParserParser.EqualContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Shift}
	 * labeled alternative in {@link JvmDslParserParser#relationExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShift(JvmDslParserParser.ShiftContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Lt}
	 * labeled alternative in {@link JvmDslParserParser#relationExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLt(JvmDslParserParser.LtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Le}
	 * labeled alternative in {@link JvmDslParserParser#relationExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLe(JvmDslParserParser.LeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Instanceof}
	 * labeled alternative in {@link JvmDslParserParser#relationExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInstanceof(JvmDslParserParser.InstanceofContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Gt}
	 * labeled alternative in {@link JvmDslParserParser#relationExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGt(JvmDslParserParser.GtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Ge}
	 * labeled alternative in {@link JvmDslParserParser#relationExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGe(JvmDslParserParser.GeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Additive}
	 * labeled alternative in {@link JvmDslParserParser#shiftExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdditive(JvmDslParserParser.AdditiveContext ctx);
	/**
	 * Visit a parse tree produced by the {@code RightShift}
	 * labeled alternative in {@link JvmDslParserParser#shiftExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRightShift(JvmDslParserParser.RightShiftContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LeftShift}
	 * labeled alternative in {@link JvmDslParserParser#shiftExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLeftShift(JvmDslParserParser.LeftShiftContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UnsignedRightShift}
	 * labeled alternative in {@link JvmDslParserParser#shiftExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnsignedRightShift(JvmDslParserParser.UnsignedRightShiftContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Add}
	 * labeled alternative in {@link JvmDslParserParser#additiveExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdd(JvmDslParserParser.AddContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Sub}
	 * labeled alternative in {@link JvmDslParserParser#additiveExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSub(JvmDslParserParser.SubContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Multiplicative}
	 * labeled alternative in {@link JvmDslParserParser#additiveExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiplicative(JvmDslParserParser.MultiplicativeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Div}
	 * labeled alternative in {@link JvmDslParserParser#multiplicativeExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiv(JvmDslParserParser.DivContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Mod}
	 * labeled alternative in {@link JvmDslParserParser#multiplicativeExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMod(JvmDslParserParser.ModContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Mul}
	 * labeled alternative in {@link JvmDslParserParser#multiplicativeExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMul(JvmDslParserParser.MulContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Unary}
	 * labeled alternative in {@link JvmDslParserParser#multiplicativeExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnary(JvmDslParserParser.UnaryContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#unaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryExpression(JvmDslParserParser.UnaryExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#lambdaExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLambdaExpression(JvmDslParserParser.LambdaExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#unapplyExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnapplyExpression(JvmDslParserParser.UnapplyExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType(JvmDslParserParser.TypeContext ctx);
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
	 * Visit a parse tree produced by {@link JvmDslParserParser#funcCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncCall(JvmDslParserParser.FuncCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#singleFuncCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSingleFuncCall(JvmDslParserParser.SingleFuncCallContext ctx);
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
	 * Visit a parse tree produced by {@link JvmDslParserParser#literalAndFuncCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralAndFuncCall(JvmDslParserParser.LiteralAndFuncCallContext ctx);
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
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLocalVariable(JvmDslParserParser.LocalVariableContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#variable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariable(JvmDslParserParser.VariableContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#importClazz}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImportClazz(JvmDslParserParser.ImportClazzContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#jarPath}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJarPath(JvmDslParserParser.JarPathContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#jarName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJarName(JvmDslParserParser.JarNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link JvmDslParserParser#parameters}.
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