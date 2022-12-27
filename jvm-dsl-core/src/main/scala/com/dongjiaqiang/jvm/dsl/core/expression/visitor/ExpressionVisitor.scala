package com.dongjiaqiang.jvm.dsl.core.expression.visitor

import com.dongjiaqiang.jvm.dsl.core.expression._
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
          case v: BoolLiteral ⇒ visitBool( v )
          case v: StringLiteral ⇒ visitString( v )
          case v: CharLiteral ⇒ visitChar( v )
          case v: ListLiteral ⇒ visitList( v )
          case v: MapLiteral ⇒ visitMap( v )
          case v: TupleLiteral ⇒ visitTuple( v )
          case v: SetLiteral ⇒ visitSet( v )
          case v: ClazzLiteral ⇒ visitClazz( v )
          case v: OptionLiteral ⇒ visitOption( v )

          case v: Div ⇒ visit( v, this )
          case v: Mul ⇒ visit( v, this )
          case v: Mod ⇒ visit( v, this )
          case v: Add ⇒ visit( v, this )
          case v: Sub ⇒ visit( v, this )
          case v: LeftShift ⇒ visit( v, this )
          case v: RightShift ⇒ visit( v, this )
          case v: UnsignedRightShift ⇒ visit( v, this )
          case v: Lt ⇒ visit( v, this )
          case v: Gt ⇒ visit( v, this )
          case v: Le ⇒ visit( v, this )
          case v: Ge ⇒ visit( v, this )
          case v: Eq ⇒ visit( v, this )
          case v: NotEq ⇒ visit( v, this )
          case v: BitAnd ⇒ visit( v, this )
          case v: Caret ⇒ visit( v, this )
          case v: BitOr ⇒ visit( v, this )
          case v: And ⇒ visit( v, this )
          case v: Or ⇒ visit( v, this )

          case v: Block ⇒ visit( v, this )
          case v: For ⇒ visit( v )
          case v: ForCollection ⇒ visit( v )
          case v: ForMap ⇒ visit( v )
          case v: While ⇒ visit( v )
          case v: DoWhile ⇒ visit( v )
          case v: Sync ⇒ visit( v )
          case v: If ⇒ visit( v )
          case v: TryCatch ⇒ visit( v )
          case v: Async ⇒ visit( v )
          case v: Try ⇒ visit( v )
          case v: MatchCase ⇒ visit( v )

          case v: FuncCallChain ⇒ visitFuncCallChain( v )
          case v: IntLiteralCallChain ⇒ visitIntLiteralCallChain( v )
          case v: LongLiteralCallChain ⇒ visitLongLiteralCallChain( v )

          case v: Assign ⇒ visitAssign( v )
          case v: Break.type ⇒ visitBreak( v )
          case v: Continue.type ⇒ visitContinue( v )
          case v: Throw ⇒ visitThrow( v )
          case v: Return ⇒ visitReturn( v )
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
