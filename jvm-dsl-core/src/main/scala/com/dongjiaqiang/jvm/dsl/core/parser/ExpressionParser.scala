package com.dongjiaqiang.jvm.dsl.core.parser

import com.dongjiaqiang.jvm.dsl.core.exception.SymbolParseException
import com.dongjiaqiang.jvm.dsl.core.{JvmDslParserBaseListener, JvmDslParserParser, scope}
import com.dongjiaqiang.jvm.dsl.core.scope.{BlockScope, ClazzScope, Failure, ForStatementBlockScope, MethodScope, ProgramScope, Success}
import com.dongjiaqiang.jvm.dsl.core.expression.Block

import scala.collection.convert.ImplicitConversionsToScala._
import java.util.{LinkedList ⇒ Stack}

/**
 *
 * statement parser response for parse statement to expression
 *
//program{
//    Int i = 100;
//    Int j = 200;
//
//    def f()=Unit // method scope
//    {  // block scope
//        Int k = 100;  // sIndex = 0
//        k = 100+i;  // sIndex = 1
//
//        {  // sIndex = 2
//            Int k = k;  // sIndex = 0,bIndex = 0
//            j = 300+k; // sIndex = 1, bIndex = 0
//            i = i*2; // sIndex = 2, bIndex = 0
//
//        }
//
//        k = 100+i; // sIndex = 3
//
//        {  // sIndex = 4
//            j+100;  // sIndex = 0,bIndex = 1
//            k+300; // sIndex = 1,bIndex = 1
//
//            { // sIndex = 2
//                Int k = k; // sIndex = 0, bIndex = 0
//                k+=1; // sIndex = 1,bIndex = 0
//            }
//        }
//
//        {  // sIndex = 5
//
//        }
//    }
//}
 *
 *
 * @param programScope program scope
 */
class ExpressionParser(val programScope:ProgramScope) extends JvmDslParserBaseListener {

  var currentClazzScope: ClazzScope = _
  var currentMethodScope: MethodScope = _
  var currentBlockScope: BlockScope = _

  var currentBlockIndex: Int = _
  var blockIndexStack: Stack[Int] = new Stack[Int]()

  var currentStatementIndex: Int = _
  var statementIndexStack: Stack[Int] = new Stack[Int]()

  var ignoreBlock = false

  override def enterClassDef(ctx: JvmDslParserParser.ClassDefContext): Unit = {
      val symbolName = ctx.IDENTIFIER( ).getText
      currentClazzScope = programScope.classes(symbolName)
  }

  override def exitClassDef(ctx: JvmDslParserParser.ClassDefContext): Unit = {
     currentClazzScope = null
  }

  override def enterFuncDef(ctx: JvmDslParserParser.FuncDefContext): Unit = {
    val symbolName = ctx.funcName( ).getText
    if(currentClazzScope!=null){
        currentMethodScope = currentClazzScope.methods(symbolName)
    }else{
        currentMethodScope = programScope.methods(symbolName)
    }
  }

  override def exitFuncDef(ctx: JvmDslParserParser.FuncDefContext): Unit = {
      currentMethodScope = null
  }

  override def enterForStatementOne(ctx: JvmDslParserParser.ForStatementOneContext): Unit = {
    enterBlock(ctx.block())
    currentStatementIndex = currentStatementIndex+1
    val forStatementBlockScope = currentBlockScope.asInstanceOf[ForStatementBlockScope]

    val varContext = ctx.varDef()
    ctx.v
    val initFieldScope = forStatementBlockScope.initFields.get(varContext.getText)
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

  override def enterWhileStatement(ctx: JvmDslParserParser.WhileStatementContext): Unit = {

  }

  override def enterDoWhileStatement(ctx: JvmDslParserParser.DoWhileStatementContext): Unit = {

  }

  override def enterSynchronizedStatement(ctx: JvmDslParserParser.SynchronizedStatementContext): Unit = {

  }

  override def enterIfStatement(ctx: JvmDslParserParser.IfStatementContext): Unit = {

  }

  override def enterBlock(ctx: JvmDslParserParser.BlockContext): Unit = {
      if(ignoreBlock){
          return
      }
      if(currentBlockScope==null){
          currentBlockScope = currentMethodScope.blockScope
          currentBlockIndex = 0
          blockIndexStack.push(currentBlockIndex)

          currentStatementIndex = 0
          statementIndexStack.push(currentStatementIndex)

      } else{
          currentStatementIndex = currentStatementIndex+1

          currentBlockScope = currentBlockScope.childrenScopes(currentBlockIndex)
          currentBlockIndex = currentBlockIndex+1
          blockIndexStack.push(currentBlockIndex)

          currentBlockIndex = 0

          statementIndexStack.push(currentStatementIndex)
          currentStatementIndex = 0
      }
  }


  override def exitBlock(ctx: JvmDslParserParser.BlockContext): Unit = {
      currentBlockScope.parentScope match {
        case blockScope: BlockScope⇒
            currentBlockScope = blockScope
            currentBlockIndex = blockIndexStack.poll()
            currentStatementIndex = statementIndexStack.poll()
        case _: MethodScope⇒
            blockIndexStack.poll()
            statementIndexStack.poll()
      }
  }

  override def enterVarDef(ctx: JvmDslParserParser.VarDefContext): Unit = {
      currentStatementIndex = currentStatementIndex+1
      currentBlockScope match {
        case forStatementBlockScope:ForStatementBlockScope⇒

      }
      Option.apply(ctx.ASSIGN()).foreach(_⇒{

      })
  }
  override def enterAssignment(ctx: JvmDslParserParser.AssignmentContext): Unit = {
    val variable = ctx.variable( ).IDENTIFIER( ).map( _.getText ).toList
    val optional = currentBlockScope.resolveVarRefs( currentBlockScope.outerScopeIndex, variable )
    if (optional.isEmpty) {
      throw SymbolParseException( f"refs resolved fail ${variable.mkString( "." )} in scope ${currentBlockScope.toString}" )
    }
    val array = ctx.LBRACK( ) != null && ctx.RBRACK( ) != null
    val operator = ctx.assignOperator( )

    if (array) {
      val subscriptExpression = ctx.expression( 0 )
      val assignExpression = ctx.expression( 1 )
      assignExpression.conditionalOrExpression()
    } else {
      val assignExpression = ctx.expression( 0 )
      assignExpression.conditionalOrExpression()

      // assignExpression.conditionalOrExpression().
    }
  //  currentStatementIndex = currentStatementIndex + 1
  }



}
