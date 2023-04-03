package com.dongjiaqiang.jvm.dsl.core.optimize

import com.dongjiaqiang.jvm.dsl.api
import com.dongjiaqiang.jvm.dsl.api.`type`._
import com.dongjiaqiang.jvm.dsl.api.expression.block.Block
import com.dongjiaqiang.jvm.dsl.api.expression.literal._
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.ExpressionVisitor
import com.dongjiaqiang.jvm.dsl.api.expression._
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope
import com.dongjiaqiang.jvm.dsl.core.expression.visitor.ExpressionReviser

import scala.collection.mutable.ArrayBuffer
import scala.util.control.Breaks

class OptimizeExpression(val programScope: ProgramScope) extends ExpressionReviser {

  def revise(oldExpressions:ArrayBuffer[Expression]):ArrayBuffer[Expression]={
    val newExpression = ArrayBuffer[Expression]( )
    revise(oldExpressions.clone(), newExpression )
    newExpression
  }

  def revise(oldExpressions: ArrayBuffer[Expression], newExpressions: ArrayBuffer[Expression]): Unit = {
    if (oldExpressions.isEmpty) {
      return
    }
    val expression = oldExpressions.remove( 0 )
    expression match {
      case whileCondition: WhileCondition ⇒
        val block = oldExpressions.remove( 0 ).asInstanceOf[WhileBlock]
        newExpressions.append( While( whileCondition.expression, Block( revise(block.expressions) ) ) )
      case doWhileBlock: DoWhileBlock ⇒
        val condition = oldExpressions.remove( 0 ).asInstanceOf[DoWhileCondition]
        newExpressions.append( DoWhile( condition.expression, block.Block( revise(doWhileBlock.expressions) ) ) )
      case forLoop: ForLoop ⇒
        newExpressions.append( For( forLoop.loopVarDef, forLoop.loopVarCondition,
          forLoop.loopVarUpdate, block.Block( revise(forLoop.expressions) ) ) )
      case forLoopCollection: ForLoopCollection ⇒
        newExpressions.append( ForCollection( forLoopCollection.localVarDef,
          forLoopCollection.looped, block.Block( revise(forLoopCollection.expressions) ) ) )
      case forLoopMap: ForLoopMap ⇒
        newExpressions.append( ForMap( forLoopMap.loopKeyDef, forLoopMap.loopValueDef, forLoopMap.looped,
          block.Block( revise(forLoopMap.expressions) ) ) )
      case syncCondition: SyncCondition ⇒
        val block = oldExpressions.remove( 0 ).asInstanceOf[SyncBlock]
        newExpressions.append( Sync( syncCondition.expression, api.expression.block.Block( revise(block.expressions) ) ) )
      case ifCondition: IfCondition ⇒
        val cases = ArrayBuffer[(ValueExpression, Block)]( )
        cases.append( (ifCondition.expression, block.Block( revise(oldExpressions.remove( 0 ).asInstanceOf[IfBlock].expressions) )) )

        if(oldExpressions.isEmpty){
          newExpressions.append( If( cases.toArray, None ) )
        }else {
          Breaks.breakable {
            while (oldExpressions.nonEmpty) {
              val nextExpression = oldExpressions( 0 )
              nextExpression match {
                case ifBlock: IfBlock ⇒
                  newExpressions.append( If( cases.toArray, Some( block.Block( revise( ifBlock.expressions ) ) ) ) )
                  oldExpressions.remove( 0 )
                  Breaks.break( )
                case ifCondition: IfCondition ⇒
                  val block = oldExpressions( 1 ).asInstanceOf[IfBlock]
                  cases.append( (ifCondition.expression, api.expression.block.Block( revise( block.expressions ) )) )
                  oldExpressions.remove( 0, 2 )

                  if (oldExpressions.isEmpty) {
                    newExpressions.append( If( cases.toArray, None ) )
                  }

                case _ ⇒
                  newExpressions.append( If( cases.toArray, None ) )
                  Breaks.break( )
              }
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
                newExpressions.append( TryCatch( block.Block( revise(tryBlock.expressions) ), catches.toArray, Some( block.Block( revise(finallyBlock.expressions ) ) ) ))
                oldExpressions.remove( 0 )
                Breaks.break( )
              case catchParameter: CatchParameter ⇒
                val block = oldExpressions( 1 ).asInstanceOf[CatchBlock]
                catches.append( ((catchParameter.name, catchParameter.dslType), api.expression.block.Block( revise(block.expressions) )) )
                oldExpressions.remove( 0, 2 )
                if(oldExpressions.isEmpty){
                  newExpressions.append( TryCatch( api.expression.block.Block( revise(tryBlock.expressions) ), catches.toArray, None ) )
                }
              case _ ⇒
                newExpressions.append( TryCatch( block.Block( revise(tryBlock.expressions) ), catches.toArray, None ) )
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
      super.visit( api.expression.block.Block( revise(block.expressions.clone()) ), visitor )
    } else {
      super.visit( block, visitor )
    }
  }


  override def visit(literal: ClazzLiteral, visitor: ExpressionVisitor[Expression]): Expression = {
    literal.dslType match {
      case clazzType: ClazzType⇒
        clazzType.clazzName match {
          case "Array" ⇒ new ArrayLiteral( literal.literal, ArrayType( clazzType.parameterTypes.headOption.getOrElse( UnResolvedType ) ) )
          case "Left" ⇒
            val list = literal.literal
            new EitherLiteral( Left( list.head ), new EitherType(
              clazzType.parameterTypes.headOption.getOrElse( UnResolvedType ),
              clazzType.parameterTypes.lastOption.getOrElse( UnResolvedType )
            ) )
          case "Right" ⇒
            val list = literal.literal
            new EitherLiteral( Right( list.head ), new EitherType(
              clazzType.parameterTypes.headOption.getOrElse( UnResolvedType ),
              clazzType.parameterTypes.lastOption.getOrElse( UnResolvedType )
            ) )
          case "Some" ⇒
            val list = literal.literal
            new OptionLiteral( list.head, new OptionType( UnResolvedType ) )
          case "None" ⇒
            NoneLiteral
          case _ ⇒ literal
        }
      case _⇒literal
    }

  }
}
