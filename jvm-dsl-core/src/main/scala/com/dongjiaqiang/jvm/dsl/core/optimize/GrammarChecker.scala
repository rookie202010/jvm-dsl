package com.dongjiaqiang.jvm.dsl.core.optimize

import com.dongjiaqiang.jvm.dsl.api.`type`._
import com.dongjiaqiang.jvm.dsl.api.exception.ExpressionParseException
import com.dongjiaqiang.jvm.dsl.api.expression._
import com.dongjiaqiang.jvm.dsl.api.expression.`var`.{Assign, LocalVarDef}
import com.dongjiaqiang.jvm.dsl.api.expression.binary._
import com.dongjiaqiang.jvm.dsl.api.expression.call._
import com.dongjiaqiang.jvm.dsl.api.expression.statement.Assert
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.ExpressionVisitor
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope

class GrammarChecker(override val programScope: ProgramScope) extends ExpressionVisitor[Unit] {

  private def check(requiredType:DslType, foundType:DslType, expression:Expression): Unit = {
    if (!requiredType.isSuperDslType( programScope.importManager, foundType )) {
      throw ExpressionParseException( s"type mismatch required type $requiredType found $foundType expression: $expression" )
    }
  }

  override def visit(assign: Assign, visitor: ExpressionVisitor[Unit]): Unit = {
      val foundType = assign.assigned.getValueType(programScope)
      val requiredType = assign.varRef.getValueType(programScope)
      check(foundType,requiredType,assign)
  }

  override def visit(assert: Assert, visitor: ExpressionVisitor[Unit]): Unit = {
      val foundType = assert.expression.getValueType(programScope)
      check(BoolType,foundType,assert)
  }

  override def visit(localVarDef: LocalVarDef, visitor: ExpressionVisitor[Unit]): Unit = {
      localVarDef.assigned match {
        case Some(expression)⇒
            val foundType = expression.getValueType(programScope)
            check(localVarDef.dslType,foundType,localVarDef)
      }
  }


  override def visit(sub: Sub, visitor: ExpressionVisitor[Unit]): Unit = super.visit( sub, visitor )

  //check call
  override def visit(methodCall: MethodCall, visitor: ExpressionVisitor[Unit]): Unit = methodCall.getValueType(programScope)
  override def visit(staticCall: StaticCall, visitor: ExpressionVisitor[Unit]): Unit = staticCall.getValueType(programScope)
  override def visit(varCall: VarCall, visitor: ExpressionVisitor[Unit]): Unit = varCall.getValueType(programScope)
  override def visit(literalCall: LiteralCall, visitor: ExpressionVisitor[Unit]): Unit = literalCall.getValueType(programScope)

  //check call chain
  override def visit(literalCallChain: BoolLiteralCallChain, visitor: ExpressionVisitor[Unit]): Unit = literalCallChain.getValueType(programScope)
  override def visit(funcCallChain: FuncCallChain, visitor: ExpressionVisitor[Unit]): Unit = funcCallChain.getValueType(programScope)
  override def visit(literalCallChain: IntLiteralCallChain, visitor: ExpressionVisitor[Unit]): Unit = literalCallChain.getValueType(programScope)
  override def visit(literalCallChain: LongLiteralCallChain, visitor: ExpressionVisitor[Unit]): Unit = literalCallChain.getValueType(programScope)
  override def visit(literalCallChain: DoubleLiteralCallChain, visitor: ExpressionVisitor[Unit]): Unit = literalCallChain.getValueType(programScope)
  override def visit(literalCallChain: FloatLiteralCallChain, visitor: ExpressionVisitor[Unit]): Unit = literalCallChain.getValueType(programScope)
  override def visit(literalCallChain: StringLiteralCallChain, visitor: ExpressionVisitor[Unit]): Unit = literalCallChain.getValueType(programScope)
  override def visit(literalCallChain: CharLiteralCallChain, visitor: ExpressionVisitor[Unit]): Unit = literalCallChain.getValueType(programScope)
  override def visit(literalCallChain: ListLiteralCallChain, visitor: ExpressionVisitor[Unit]): Unit = literalCallChain.getValueType(programScope)
  override def visit(literalCallChain: OptionLiteralCallChain, visitor: ExpressionVisitor[Unit]): Unit = literalCallChain.getValueType(programScope)
  override def visit(literalCallChain: TupleLiteralCallChain, visitor: ExpressionVisitor[Unit]): Unit = literalCallChain.getValueType(programScope)
  override def visit(literalCallChain: MapLiteralCallChain, visitor: ExpressionVisitor[Unit]): Unit = literalCallChain.getValueType(programScope)
  override def visit(literalCallChain: SetLiteralCallChain, visitor: ExpressionVisitor[Unit]): Unit = literalCallChain.getValueType(programScope)
  override def visit(literalCallChain: ClazzLiteralCallChain, visitor: ExpressionVisitor[Unit]): Unit = literalCallChain.getValueType(programScope)

  //check binary
  override def visit(div: Div, visitor: ExpressionVisitor[Unit]): Unit = div.getValueType(programScope)
  override def visit(mod: Mod, visitor: ExpressionVisitor[Unit]): Unit = mod.getValueType( programScope )
  override def visit(mul: Mul, visitor: ExpressionVisitor[Unit]): Unit = mul.getValueType(programScope)
  override def visit(add: Add, visitor: ExpressionVisitor[Unit]): Unit = add.getValueType(programScope)
  override def visit(leftShift: LeftShift, visitor: ExpressionVisitor[Unit]): Unit = leftShift.getValueType(programScope)
  override def visit(rightShift: RightShift, visitor: ExpressionVisitor[Unit]): Unit = rightShift.getValueType(programScope)
  override def visit(unsignedRightShift: UnsignedRightShift, visitor: ExpressionVisitor[Unit]): Unit = unsignedRightShift.getValueType(programScope)
  override def visit(lt: Lt, visitor: ExpressionVisitor[Unit]): Unit = lt.getValueType(programScope)
  override def visit(gt: Gt, visitor: ExpressionVisitor[Unit]): Unit = gt.getValueType(programScope)
  override def visit(le: Le, visitor: ExpressionVisitor[Unit]): Unit = le.getValueType(programScope)
  override def visit(ge: Ge, visitor: ExpressionVisitor[Unit]): Unit = ge.getValueType(programScope)
  override def visit(eq: Eq, visitor: ExpressionVisitor[Unit]): Unit = eq.getValueType(programScope)
  override def visit(ne: NotEq, visitor: ExpressionVisitor[Unit]): Unit = ne.visit( visitor )
  override def visit(bitAnd: BitAnd, visitor: ExpressionVisitor[Unit]): Unit = bitAnd.visit(visitor)
  override def visit(caret: Caret, visitor: ExpressionVisitor[Unit]): Unit = caret.visit(visitor)
  override def visit(bitOr: BitOr, visitor: ExpressionVisitor[Unit]): Unit = bitOr.visit(visitor)
  override def visit(and: And, visitor: ExpressionVisitor[Unit]): Unit = and.visit(visitor)
  override def visit(or: Or, visitor: ExpressionVisitor[Unit]): Unit = or.visit(visitor)


}
