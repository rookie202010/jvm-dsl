package com.dongjiaqiang.jvm.dsl.core.parser

import com.dongjiaqiang.jvm.dsl.core.expression.generator.{AssignGenerator, OrGenerator, VariableGenerator}
import com.dongjiaqiang.jvm.dsl.core.expression.{Block, Expression, generator}
import com.dongjiaqiang.jvm.dsl.core.program.{Clazz, Method, Program}
import com.dongjiaqiang.jvm.dsl.core.scope._
import com.dongjiaqiang.jvm.dsl.core.{JvmDslParserBaseListener, JvmDslParserParser}

import java.util.{LinkedList ⇒ Stack}
import scala.collection.mutable.{ArrayBuffer, ListMap ⇒ MutableMap}

/**
 *
 * statement parser response for parse statement to expression
 *
 * //program{
 * //    Int i = 100;
 * //    Int j = 200;
 * //
 * //    def f()=Unit // method scope
 * //    {  // block scope
 * //        Int k = 100;  // sIndex = 0
 * //        k = 100+i;  // sIndex = 1
 * //
 * //        {  // sIndex = 2
 * //            Int k = k;  // sIndex = 0,bIndex = 0
 * //            j = 300+k; // sIndex = 1, bIndex = 0
 * //            i = i*2; // sIndex = 2, bIndex = 0
 * //
 * //        }
 * //
 * //        k = 100+i; // sIndex = 3
 * //
 * //        {  // sIndex = 4
 * //            j+100;  // sIndex = 0,bIndex = 1
 * //            k+300; // sIndex = 1,bIndex = 1
 * //
 * //            { // sIndex = 2
 * //                Int k = k; // sIndex = 0, bIndex = 0
 * //                k+=1; // sIndex = 1,bIndex = 0
 * //            }
 * //        }
 * //
 * //        {  // sIndex = 5
 * //
 * //        }
 * //    }
 * //}
 *
 *
 * program scope
 */
case class SpecialContext(
                           expression: ArrayBuffer[Expression] = ArrayBuffer( ),
                           blocks: ArrayBuffer[Block] = ArrayBuffer( )
                         )

class ExpressionParser(val programScope: ProgramScope) extends JvmDslParserBaseListener {

  //scopes
  var currentClazzScope: ClazzScope = _
  var currentMethodScope: MethodScope = _
  var currentBlockScope: BlockScope = _

  var currentBlockIndex: Int = _
  var blockIndexStack: Stack[Int] = new Stack[Int]( )

  var currentStatementIndex: Int = _
  var statementIndexStack: Stack[Int] = new Stack[Int]( )

  //expressions
  var program: Program
  var currentClazz: Clazz
  var currentMethod: Method
  var currentBlock: Block
  var blockStack: Stack[Block] = new Stack[Block]( )

  //special context
  var specialContext = new SpecialContext( )

  override def enterProgram(ctx: JvmDslParserParser.ProgramContext): Unit = {
    program = Program( programScope, MutableMap( ), MutableMap( ) )
  }

  override def enterClassDef(ctx: JvmDslParserParser.ClassDefContext): Unit = {
    /**
     * 进入当前类作用域
     */
    val symbolName = ctx.IDENTIFIER( ).getText
    currentClazzScope = programScope.classes( symbolName )

    currentClazz = Clazz( currentClazzScope, MutableMap( ) )
    program.classes.put( currentClazzScope.name, currentClazz )
  }

  override def exitClassDef(ctx: JvmDslParserParser.ClassDefContext): Unit = {
    /**
     * 退出当前类作用域
     */
    currentClazzScope = null
     currentClazz = null
  }

  override def enterFuncDef(ctx: JvmDslParserParser.FuncDefContext): Unit = {
    /**
     * 进入当前方法域
     */
    val symbolName = ctx.funcName( ).getText
    if (currentClazzScope != null) {
      currentMethodScope = currentClazzScope.methods( symbolName )
    } else {
      currentMethodScope = programScope.methods( symbolName )
    }

    if (currentClazz != null) {
      currentClazz.methods
    } else {
      program.methods
    }.put( currentMethodScope.name, Method( currentMethodScope, new ArrayBuffer() ))

  }

  override def exitFuncDef(ctx: JvmDslParserParser.FuncDefContext): Unit = {
    /**
     * 退出当前方法域
     */
    currentMethodScope = null

      currentMethod = null
  }

  override def enterBlock(ctx: JvmDslParserParser.BlockContext): Unit = {
    if (currentBlockScope == null) {
      currentBlockScope = currentMethodScope.blockScope.childrenScopes

      currentBlockIndex = 0
      blockIndexStack.push( currentBlockIndex )

      currentStatementIndex = 0
      statementIndexStack.push( currentStatementIndex )

      currentBlock = currentMethod.block

    } else {

      currentStatementIndex = currentStatementIndex + 1
      statementIndexStack.push( currentStatementIndex )
      currentStatementIndex = 0

      currentBlockScope = currentBlockScope.childrenScopes( currentBlockIndex )
      currentBlockIndex = currentBlockIndex + 1
      blockIndexStack.push( currentBlockIndex )
      currentBlockIndex = 0

      blockStack.push( currentBlock )

    }

    if (specialContext != null) {
      specialContext.blocks.append( new Block( ) )
    }
  }


  override def exitBlock(ctx: JvmDslParserParser.BlockContext): Unit = {
    currentBlockScope.parentScope match {
      case blockScope: BlockScope ⇒
        currentBlockScope = blockScope
        currentBlockIndex = blockIndexStack.poll( )
        currentStatementIndex = statementIndexStack.poll( )
      case _: MethodScope ⇒
        blockIndexStack.poll( )
        statementIndexStack.poll( )
    }

    val parentBlock = blockStack.poll( )
    parentBlock.expressions.append( currentBlock )
    currentBlock = parentBlock

  }

  override def enterAssignExpr(ctx: JvmDslParserParser.AssignExprContext): Unit = {
    val expression = AssignGenerator.generate( generator.ExpressionContext( currentStatementIndex, currentBlockScope,
      Option.apply( currentClazzScope ).getOrElse( programScope ) ), ctx.assignment( ) )

    currentBlock.expressions.append( expression )
    currentStatementIndex = currentStatementIndex + 1
  }

  override def enterWhileStatement(ctx: JvmDslParserParser.WhileStatementContext): Unit = {
    specialContext = SpecialContext( )
    val expressionContext = generator.ExpressionContext( currentBlockScope.outerScopeIndex + 1,
      currentBlockScope, Option.apply( currentClazzScope ).getOrElse( programScope ) )

    specialContext.expression.append( OrGenerator.generate( expressionContext, ctx.conditionalOrExpression( ) ) )
  }

  override def exitWhileStatement(ctx: JvmDslParserParser.WhileStatementContext): Unit = {
    specialContext = null

  }

  override def enterDoWhileStatement(ctx: JvmDslParserParser.DoWhileStatementContext): Unit = {
    enterBlock( ctx.block( ) )
  }


  override def enterForStatementOne(ctx: JvmDslParserParser.ForStatementOneContext): Unit = {
    enterBlock( ctx.block( ) )
    currentStatementIndex = currentStatementIndex + 1
    val forStatementBlockScope = currentBlockScope.asInstanceOf[ForStatementBlockScope]

    val varContext = VariableGenerator.generate( )
    ctx
    .
    val initFieldScope = forStatementBlockScope.initFields.get( varContext.getText )
    varContext.

      ignoreBlock = true
  }

  override def exitForStatementOne(ctx: JvmDslParserParser.ForStatementOneContext): Unit = {
    ignoreBlock = false
  }

  override def enterForStatementTwo(ctx: JvmDslParserParser.ForStatementTwoContext): Unit = {

  }

  override def enterForStatementThree(ctx: JvmDslParserParser.ForStatementThreeContext): Unit = {

  }

  override def enterSynchronizedStatement(ctx: JvmDslParserParser.SynchronizedStatementContext): Unit = {

  }

  override def enterIfStatement(ctx: JvmDslParserParser.IfStatementContext): Unit = {

  }

  override def enterVarDef(ctx: JvmDslParserParser.VarDefContext): Unit = {
    if (currentBlockScope != null) {
      currentStatementIndex = currentStatementIndex + 1
      currentBlockScope match {
        case forStatementBlockScope: ForStatementBlockScope ⇒

      }
      Option.apply( ctx.ASSIGN( ) ).foreach( _ ⇒ {

        } )
      }
  }





}
