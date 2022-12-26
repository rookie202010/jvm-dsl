package com.dongjiaqiang.jvm.dsl.core.expression.visitor

import com.dongjiaqiang.jvm.dsl.core.expression.Expression
import com.dongjiaqiang.jvm.dsl.core.expression.visitor.`var`.VarExpressionReviser
import com.dongjiaqiang.jvm.dsl.core.expression.visitor.binary.expression.BinaryExpressionReviser
import com.dongjiaqiang.jvm.dsl.core.expression.visitor.block.BlockExpressionReviser
import com.dongjiaqiang.jvm.dsl.core.expression.visitor.callchain.CallChainExpressionReviser
import com.dongjiaqiang.jvm.dsl.core.expression.visitor.literal.LiteralExpressionReviser
import com.dongjiaqiang.jvm.dsl.core.expression.visitor.statement.StatementExpressionReviser
import com.dongjiaqiang.jvm.dsl.core.expression.visitor.unary.expression.UnaryExpressionReviser

/**
 * @author: rookie
 * @mail: dongjiaqiang@qiniu.com
 * @date: 2022/12/26 
 * */
trait ExpressionReviser extends ExpressionVisitor[Expression]
                        with BinaryExpressionReviser
                        with BlockExpressionReviser
                        with CallChainExpressionReviser
                        with LiteralExpressionReviser
                        with StatementExpressionReviser
                        with UnaryExpressionReviser
                        with VarExpressionReviser
