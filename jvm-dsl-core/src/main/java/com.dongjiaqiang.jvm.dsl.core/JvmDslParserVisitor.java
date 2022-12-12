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
	 * Visit a parse tree produced by {@link JvmDslParserParser#funcCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncCall(JvmDslParserParser.FuncCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code VarCallNoArgs}
	 * labeled alternative in {@link JvmDslParserParser#singleFuncCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarCallNoArgs(JvmDslParserParser.VarCallNoArgsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code VarCallArgs}
	 * labeled alternative in {@link JvmDslParserParser#singleFuncCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarCallArgs(JvmDslParserParser.VarCallArgsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LiteralCallNoArgs}
	 * labeled alternative in {@link JvmDslParserParser#singleFuncCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralCallNoArgs(JvmDslParserParser.LiteralCallNoArgsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LiteralCallArgs}
	 * labeled alternative in {@link JvmDslParserParser#singleFuncCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralCallArgs(JvmDslParserParser.LiteralCallArgsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TypeCallNoArgs}
	 * labeled alternative in {@link JvmDslParserParser#singleFuncCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeCallNoArgs(JvmDslParserParser.TypeCallNoArgsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TypeCalArgs}
	 * labeled alternative in {@link JvmDslParserParser#singleFuncCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeCalArgs(JvmDslParserParser.TypeCalArgsContext ctx);
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