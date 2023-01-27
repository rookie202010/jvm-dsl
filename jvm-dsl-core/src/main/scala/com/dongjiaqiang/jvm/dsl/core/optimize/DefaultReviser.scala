package com.dongjiaqiang.jvm.dsl.core.optimize

import com.dongjiaqiang.jvm.dsl.api.`type`._
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.ExpressionVisitor
import com.dongjiaqiang.jvm.dsl.api.expression._
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope
import com.dongjiaqiang.jvm.dsl.core.expression.visitor.ExpressionReviser

import scala.collection.mutable.ArrayBuffer
import scala.util.control.Breaks

class DefaultReviser(val programScope: ProgramScope) extends ExpressionReviser {
  def revise(oldExpressions: ArrayBuffer[Expression], newExpressions: ArrayBuffer[Expression]): Unit = {
    if (oldExpressions.isEmpty) {
      return
    }
    val expression = oldExpressions.remove( 0 )
    expression match {
      case whileCondition: WhileCondition ⇒
        val block = oldExpressions.remove( 0 ).asInstanceOf[WhileBlock]
        newExpressions.append( While( whileCondition.expression, new Block( block.expressions ) ) )
      case doWhileBlock: DoWhileBlock ⇒
        val condition = oldExpressions.remove( 0 ).asInstanceOf[DoWhileCondition]
        newExpressions.append( DoWhile( condition.expression, new Block( doWhileBlock.expressions ) ) )
      case forLoop: ForLoop ⇒
        newExpressions.append( For( forLoop.loopVarDef, forLoop.loopVarCondition,
          forLoop.loopVarUpdate, new Block( forLoop.expressions ) ) )
      case forLoopCollection: ForLoopCollection ⇒
        newExpressions.append( ForCollection( forLoopCollection.localVarDef,
          forLoopCollection.looped, new Block( forLoopCollection.expressions ) ) )
      case forLoopMap: ForLoopMap ⇒
        newExpressions.append( ForMap( forLoopMap.loopKeyDef, forLoopMap.loopValueDef, forLoopMap.looped, new Block( forLoopMap.expressions ) ) )
      case syncCondition: SyncCondition ⇒
        val block = oldExpressions.remove( 0 ).asInstanceOf[SyncBlock]
        newExpressions.append( Sync( syncCondition.expression, new Block( block.expressions ) ) )
      case ifCondition: IfCondition ⇒
        val cases = ArrayBuffer[(Expression, Block)]( )
        cases.append( (ifCondition.expression, new Block( oldExpressions.remove( 0 ).asInstanceOf[IfBlock].expressions )) )

        Breaks.breakable {
          while (oldExpressions.nonEmpty) {
            val nextExpression = oldExpressions( 0 )
            nextExpression match {
              case ifBlock: IfBlock ⇒
                newExpressions.append( If( cases.toArray, Some( new Block( ifBlock.expressions ) ) ) )
                oldExpressions.remove( 0 )
                Breaks.break( )
              case ifCondition: IfCondition ⇒
                val block = oldExpressions( 1 ).asInstanceOf[IfBlock]
                cases.append( (ifCondition.expression, new Block( block.expressions )) )
                oldExpressions.remove( 0, 2 )
              case _ ⇒
                newExpressions.append( If( cases.toArray, None ) )
                Breaks.break( )
            }
          }
        }
      case tryBlock: TryBlock ⇒
        val catches = ArrayBuffer[((String, DslType), Block)]( )

        Breaks.breakable {
          while (oldExpressions.nonEmpty) {
            val nextExpression = oldExpressions( 0 )
            nextExpression match {
              case finallyBlock: FinallyBlock ⇒
                newExpressions.append( TryCatch( new Block( tryBlock.expressions ), catches.toArray, Some( new Block( finallyBlock.expressions ) ) ) )
                oldExpressions.remove( 0 )
                Breaks.break( )
              case catchParameter: CatchParameter ⇒
                val block = oldExpressions( 1 ).asInstanceOf[CatchBlock]
                catches.append( ((catchParameter.name, catchParameter.dslType), new Block( block.expressions )) )
                oldExpressions.remove( 0, 2 )
              case _ ⇒
                newExpressions.append( TryCatch( new Block( tryBlock.expressions ), catches.toArray, None ) )
                Breaks.break( )
            }
          }
        }
      case _ ⇒ newExpressions.append( expression )
    }
    revise( oldExpressions, newExpressions )
  }

  override def visit(block: Block, visitor: ExpressionVisitor[Expression]): Block = {
    if (block.expressions.exists( expr ⇒ {
      expr.isInstanceOf[WhileCondition] ||
        expr.isInstanceOf[DoWhileBlock] ||
        expr.isInstanceOf[ForLoop] ||
        expr.isInstanceOf[ForLoopCollection] ||
        expr.isInstanceOf[ForLoopMap] ||
        expr.isInstanceOf[SyncCondition] ||
        expr.isInstanceOf[IfCondition] ||
        expr.isInstanceOf[TryBlock]
    } )) {
      val newExpression = ArrayBuffer[Expression]( )
      revise( block.expressions.clone( ), newExpression )
      super.visit( new Block( newExpression ), visitor )
    } else {
      super.visit( block, visitor )
    }
  }

  override def visit(literal: ClazzLiteral, visitor: ExpressionVisitor[Expression]): Expression = {
    literal.dslType.clazzName match {
      case "Array" ⇒ new ArrayLiteral( literal.literal, new ArrayType( literal.dslType.parameterTypes.headOption.getOrElse( UnResolvedType ) ) )
      case "Left" ⇒
        val list = literal.literal
        new EitherLiteral( Left( list.head ), new EitherType(
          literal.dslType.parameterTypes.headOption.getOrElse( UnResolvedType ),
          literal.dslType.parameterTypes.lastOption.getOrElse( UnResolvedType )
        ) )
      case "Right" ⇒
        val list = literal.literal
        new EitherLiteral( Right( list.head ), new EitherType(
          literal.dslType.parameterTypes.headOption.getOrElse( UnResolvedType ),
          literal.dslType.parameterTypes.lastOption.getOrElse( UnResolvedType )
        ) )
      case _ ⇒ literal
    }
  }

  override def visit(matchClass: MatchClass, visitor: ExpressionVisitor[Expression]): Expression = {
    MatchClass( dslType = matchClass.dslType match {
      case clazzType: ClazzType ⇒
        clazzType.clazzName match {
          //EitherType
          case "Left" ⇒
            new LeftType( UnResolvedType )
          case "Right" ⇒
            new RightType( UnResolvedType )

          //TryType
          case "Success" ⇒
            new SuccessType( UnResolvedType )
          case "Failure" ⇒
            FailureType

          //OptionType
          case "Some" ⇒
            new SomeType( UnResolvedType )

          case _ ⇒
            clazzType
        }
    }, revise( matchClass.expressions, visitor ) )
  }
}
