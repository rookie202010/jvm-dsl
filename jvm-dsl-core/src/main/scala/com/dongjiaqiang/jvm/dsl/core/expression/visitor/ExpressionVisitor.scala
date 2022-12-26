package com.dongjiaqiang.jvm.dsl.core.expression.visitor

import com.dongjiaqiang.jvm.dsl.core.expression.{Add, And, ArrayVarRef, Assert, Assign, Async, BitAnd, BitOr, Block, BoolLiteral, Break, Caret, Cast, CharLiteral, ClazzLiteral, Continue, Div, DoWhile, DoubleLiteral, Eq, Expression, FloatLiteral, For, ForCollection, ForMap, FuncCallChain, Ge, Gt, If, Instanceof, IntLiteral, IntLiteralCallChain, Lambda, Le, LeftShift, ListLiteral, LocalVarDef, LongLiteral, LongLiteralCallChain, Lt, MapLiteral, MapVarRef, MatchCase, Mod, Mul, Negate, NotEq, Opposite, OptionLiteral, Or, Paren, Return, RightShift, SetLiteral, StringLiteral, Sub, Sync, Throw, Try, TryCatch, TupleLiteral, UnsignedRightShift, VarRef, While}
import com.dongjiaqiang.jvm.dsl.core.expression.visitor.`var`.VarExpressionVisitor
import com.dongjiaqiang.jvm.dsl.core.expression.visitor.binary.expression.BinaryExpressionVisitor
import com.dongjiaqiang.jvm.dsl.core.expression.visitor.block.BlockExpressionVisitor
import com.dongjiaqiang.jvm.dsl.core.expression.visitor.callchain.CallChainExpressionVisitor
import com.dongjiaqiang.jvm.dsl.core.expression.visitor.literal.LiteralExpressionVisitor
import com.dongjiaqiang.jvm.dsl.core.expression.visitor.statement.StatementExpressionVisitor
import com.dongjiaqiang.jvm.dsl.core.expression.visitor.unary.expression.UnaryExpressionVisitor


trait ExpressionVisitor[T] extends LiteralExpressionVisitor[T]
  with UnaryExpressionVisitor[T]
  with BinaryExpressionVisitor[T]
  with CallChainExpressionVisitor[T]
  with BlockExpressionVisitor[T]
  with StatementExpressionVisitor[T]
  with VarExpressionVisitor[T]{

    def visit(expression:Expression):T={
        expression match {
          case v:IntLiteral⇒ visitInt(v)
          case v:LongLiteral⇒ visitLong(v)
          case v:FloatLiteral ⇒ visitFloat(v)
          case v:DoubleLiteral ⇒ visitDouble(v)
          case v:BoolLiteral ⇒ visitBool(v)
          case v:StringLiteral ⇒ visitString(v)
          case v:CharLiteral ⇒ visitChar(v)
          case v:ListLiteral⇒visitList(v)
          case v:MapLiteral⇒visitMap(v)
          case v:TupleLiteral⇒visitTuple(v)
          case v:SetLiteral⇒visitSet(v)
          case v:ClazzLiteral⇒visitClazz(v)
          case v:OptionLiteral⇒visitOption(v)

          case v:Div⇒ visitDiv(v)
          case v:Mul⇒visitMul(v)
          case v:Mod⇒visitMod(v)
          case v:Add⇒visitAdd(v)
          case v:Sub⇒visitSub(v)
          case v:LeftShift⇒visitLeftShift(v)
          case v:RightShift⇒visitRightShift(v)
          case v:UnsignedRightShift⇒visitUnsignedRightShift(v)
          case v:Lt⇒visitLt(v)
          case v:Gt⇒visitGt(v)
          case v:Le⇒visitLe(v)
          case v:Ge⇒visitGe(v)
          case v:Eq⇒visitEq(v)
          case v:NotEq⇒visitNotEq(v)
          case v:BitAnd⇒visitBitAnd(v)
          case v:Caret⇒visitCaret(v)
          case v:BitOr⇒visitBitOr(v)
          case v:And⇒visitAnd(v)
          case v:Or⇒visitOr(v)

          case v:Block⇒visitBlock(v)
          case v:For⇒visitFor(v)
          case v:ForCollection⇒visitForCollection(v)
          case v:ForMap⇒visitForMap(v)
          case v:While⇒visitWhile(v)
          case v:DoWhile⇒visitDoWhile(v)
          case v:Sync⇒visitSync(v)
          case v:If⇒visitIf(v)
          case v:TryCatch⇒visitTryCatch(v)
          case v:Async⇒visitAsync(v)
          case v:Try⇒visitTry(v)
          case v:MatchCase⇒visitMatchCase(v)

          case v:FuncCallChain⇒ visitFuncCallChain(v)
          case v:IntLiteralCallChain⇒ visitIntLiteralCallChain(v)
          case v:LongLiteralCallChain⇒visitLongLiteralCallChain(v)

          case v:Assign⇒ visitAssign(v)
          case v:Break.type ⇒visitBreak(v)
          case v:Continue.type⇒visitContinue(v)
          case v:Throw⇒visitThrow(v)
          case v:Return⇒visitReturn(v)
          case v:Assert⇒visitAssert(v)

          case v:Negate⇒visitNegate(v)
          case v:Opposite⇒visitOpposite(v)
          case v:Cast⇒visitCast(v)
          case v:Instanceof⇒visitInstanceof(v)
          case v:Paren⇒visitParen(v)

          case v:LocalVarDef⇒visitLocalVarDef(v)
          case v:VarRef⇒visitVarRef(v)
          case v:ArrayVarRef⇒visitArrayVarRef(v)
          case v:MapVarRef⇒visitMapVarRef(v)
          case v:Lambda⇒visitLambda(v)
        }
    }
}
