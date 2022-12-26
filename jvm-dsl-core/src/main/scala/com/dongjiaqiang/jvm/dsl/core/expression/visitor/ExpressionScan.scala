package com.dongjiaqiang.jvm.dsl.core.expression.visitor
import com.dongjiaqiang.jvm.dsl.core.expression.visitor.`var`.VarExpressionScanner
import com.dongjiaqiang.jvm.dsl.core.expression.visitor.binary.expression.BinaryExpressionScanner
import com.dongjiaqiang.jvm.dsl.core.expression.visitor.block.BlockExpressionScanner
import com.dongjiaqiang.jvm.dsl.core.expression.visitor.callchain.CallChainExpressionScanner
import com.dongjiaqiang.jvm.dsl.core.expression.visitor.literal.LiteralExpressionScanner
import com.dongjiaqiang.jvm.dsl.core.expression.visitor.statement.StatementExpressionScanner
import com.dongjiaqiang.jvm.dsl.core.expression.visitor.unary.expression.UnaryExpressionScanner
import com.dongjiaqiang.jvm.dsl.core.expression.{Add, And, ArrayVarRef, Assert, Assign, Async, BitAnd, BitOr, Block, BoolLiteral, Break, Caret, Cast, CharLiteral, ClazzLiteral, Continue, Div, DoWhile, DoubleLiteral, Eq, Expression, FloatLiteral, For, ForCollection, ForMap, FuncCallChain, Ge, Gt, If, Instanceof, IntLiteral, IntLiteralCallChain, Lambda, Le, LeftShift, ListLiteral, LocalVarDef, LongLiteral, LongLiteralCallChain, Lt, MapLiteral, MapVarRef, MatchCase, Mod, Mul, Negate, NotEq, Opposite, OptionLiteral, Or, Paren, Return, RightShift, SetLiteral, StringLiteral, Sub, Sync, Throw, Try, TryCatch, TupleLiteral, UnsignedRightShift, VarRef, While}

/**
 * @author: rookie
 * @mail: dongjiaqiang@qiniu.com
 * @date: 2022/12/26 
 * */
trait ExpressionScan extends ExpressionVisitor[Unit]
                      with BinaryExpressionScanner
                      with BlockExpressionScanner
                      with CallChainExpressionScanner
                      with LiteralExpressionScanner
                      with StatementExpressionScanner
                      with UnaryExpressionScanner
                      with VarExpressionScanner