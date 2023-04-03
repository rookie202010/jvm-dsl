package com.dongjiaqiang.jvm.dsl.api.expression.visitor

import com.dongjiaqiang.jvm.dsl.api.`type`.LambdaType
import com.dongjiaqiang.jvm.dsl.api.expression._
import com.dongjiaqiang.jvm.dsl.api.expression.`var`.{Assign, LocalVarDef, Null, VarRef}
import com.dongjiaqiang.jvm.dsl.api.expression.binary._
import com.dongjiaqiang.jvm.dsl.api.expression.block._
import com.dongjiaqiang.jvm.dsl.api.expression.call._
import com.dongjiaqiang.jvm.dsl.api.expression.literal._
import com.dongjiaqiang.jvm.dsl.api.expression.statement._
import com.dongjiaqiang.jvm.dsl.api.expression.unary._
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.`var`.VarExpressionVisitor
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.binary.expression.BinaryExpressionVisitor
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.block.BlockExpressionVisitor
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.call.CallExpressionVisitor
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.callchain.CallChainExpressionVisitor
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.literal.LiteralExpressionVisitor
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.statement.StatementExpressionVisitor
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.unary.expression.UnaryExpressionVisitor
import com.dongjiaqiang.jvm.dsl.api.scope.{ClazzScope, MethodScope, ProgramScope}

trait ExpressionVisitor[T] extends LiteralExpressionVisitor[T]
  with UnaryExpressionVisitor[T]
  with BinaryExpressionVisitor[T]
  with CallChainExpressionVisitor[T]
  with CallExpressionVisitor[T]
  with BlockExpressionVisitor[T]
  with StatementExpressionVisitor[T]
  with VarExpressionVisitor[T] {

  val programScope: ProgramScope

  var currentClazzScope: ClazzScope = _

  var currentMethodScope: MethodScope = _

  var currentLambdaScope: LambdaType = _

  def defaultVisit(expression: Expression, visitor: ExpressionVisitor[T]): T = {
    throw new UnsupportedOperationException( "not supported" )
  }

  def setCurrentMethodScope(currentMethodScope:MethodScope): Unit = {
      this.currentMethodScope = currentMethodScope
  }

  def visit(expression: Expression): T = {
    expression match {
      //visit literal expression
      case v: IntLiteral ⇒ visit( v, this )
      case v: LongLiteral ⇒ visit( v, this )
      case v: FloatLiteral ⇒ visit( v, this )
      case v: DoubleLiteral ⇒ visit( v, this )
      case v: BoolLiteral ⇒ visit( v, this )
      case v: StringLiteral ⇒ visit( v, this )
      case v: CharLiteral ⇒ visit( v, this )
      case v: ListLiteral ⇒ visit( v, this )
      case v: MapLiteral ⇒ visit( v, this )
      case v: TupleLiteral ⇒ visit( v, this )
      case v: SetLiteral ⇒ visit( v, this )
      case v: ClazzLiteral ⇒ visit( v, this )
      case v: OptionLiteral ⇒ visit( v, this )
      case v: ArrayLiteral ⇒ visit( v, this )
      case v: EitherLiteral ⇒ visit( v, this )
      case Null ⇒ visit( Null, this )

      //visit binary expression
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

      //visit block expression
      case v: Block ⇒ visit( v, this )
      case v: For ⇒ visit( v, this )
      case v: ForCollection ⇒ visit( v, this )
      case v: ForMap ⇒ visit( v, this )
      case v: While ⇒ visit( v, this )
      case v: DoWhile ⇒ visit( v, this )
      case v: Sync ⇒ visit( v, this )
      case v: If ⇒ visit( v, this )
      case v: TryCatch ⇒ visit( v, this )
      case v: Async ⇒ visit( v, this )
      case v: Try ⇒ visit( v, this )
      case v: CustomBlockExpression ⇒ visit( v, this )
      case v: MatchCase ⇒ visit( v, this )
      case v: MatchTuple ⇒ visit( v, this )
      case v: MatchType ⇒ visit( v, this )
      case v: MatchCase ⇒ visit( v, this )
      case v: MatchHead ⇒ visit( v, this )
      case v: MatchList ⇒ visit( v, this )

      //visit call chain expression
      case v: FuncCallChain ⇒
        val funcCallChain = visit( v, this )
        funcCallChain
      case v: IntLiteralCallChain ⇒ visit( v, this )
      case v: LongLiteralCallChain ⇒ visit( v, this )
      case v: FloatLiteralCallChain ⇒ visit( v, this )
      case v: DoubleLiteralCallChain ⇒ visit( v, this )
      case v: CharLiteralCallChain ⇒ visit( v, this )
      case v: StringLiteralCallChain ⇒ visit( v, this )
      case v: BoolLiteralCallChain ⇒ visit( v, this )
      case v: ListLiteralCallChain ⇒ visit( v, this )
      case v: SetLiteralCallChain ⇒ visit( v, this )
      case v: TupleLiteralCallChain ⇒ visit( v, this )
      case v: MapLiteralCallChain ⇒ visit( v, this )
      case v: ClazzLiteralCallChain ⇒ visit( v, this )
      case v: OptionLiteralCallChain ⇒ visit( v, this )

      //visit call expression
      case v:StaticCall ⇒ visit(v,this)
      case v:MethodCall ⇒ visit(v,this)
      case v:VarCall ⇒ visit(v,this)
      case v:LiteralCall⇒ visit(v,this)

      //visit statement expression
      case v: Assign ⇒
        v.varRef.getValueType(programScope) match {
          case lambdaType: LambdaType ⇒
            currentLambdaScope = lambdaType
          case _⇒   //visit( v, this )
        }
        visit( v, this )
      case v: Break.type ⇒ visit( v, this )
      case v: Continue.type ⇒ visit( v, this )
      case v: Throw ⇒ visit( v, this )
      case v: Return ⇒ visit( v, this )
      case v: Assert ⇒ visit( v, this )

      //visit unary expression
      case v: Negate ⇒ visit( v, this )
      case v: Opposite ⇒ visit( v, this )
      case v: Cast ⇒ visit( v, this )
      case v: Instanceof ⇒ visit( v, this )
      case v: Paren ⇒ visit( v, this )

      //visit var expression
      case v: LocalVarDef ⇒
        v.dslType match {
          case lambdaType: LambdaType ⇒
            if (v.assigned.isDefined) {
              currentLambdaScope = lambdaType
            }
          case _ ⇒ // visit( v, this )
        }
        visit( v, this )
      case v: VarRef ⇒ visit( v, this )
      case v: Lambda ⇒
        val t = visit( v, this )
        currentLambdaScope = null
        t
      case _ ⇒ defaultVisit( expression, this )
    }
  }
}
