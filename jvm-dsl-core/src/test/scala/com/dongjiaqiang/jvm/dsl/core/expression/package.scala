package com.dongjiaqiang.jvm.dsl.core

import com.dongjiaqiang.jvm.dsl.api.`type`.{ClazzType, DslType}
import com.dongjiaqiang.jvm.dsl.api.expression._
import com.dongjiaqiang.jvm.dsl.api.scope._
import com.dongjiaqiang.jvm.dsl.core.parser.{ExpressionParser, SymbolDefParser}
import com.dongjiaqiang.jvm.dsl.core.program.{Clazz, Method, Program}
import org.antlr.v4.runtime.tree.ParseTreeWalker
import org.antlr.v4.runtime.{CharStreams, CommonTokenStream}

import java.io.StringReader
import scala.collection.mutable.{ArrayBuffer, ListMap ⇒ MutableMap}


case class BlockWithScope(block: Block,
                          blockScope:BlockScope,
                          belongMethod:Option[BelongMethod]){
    var parent:BlockWithScope = _
    var childBlockIndex = 0

  def nextLambdaBlock(ignoreVarRefResolved:Boolean): BlockWithScope = {
    val scope = blockScope.lambdaScopes.remove( 0 )
    BlockWithScope( Block(ignoreVarRefResolved = ignoreVarRefResolved ), scope, None )
  }
}

trait BelongMethod{
    val method:Method

    def varRef(name:String): Option[FieldScope] ={
        method.methodScope.params.get(name)
    }
}

case class ClazzMethod(method:Method,clazz:Clazz) extends BelongMethod{
  override def varRef(name: String): Option[FieldScope] = {
    super.varRef( name ) match {
      case some: Some[_] ⇒ some
      case None ⇒ clazz.clazzScope.fields.get(name)
    }
  }
}

case class ProgramMethod(method: Method,program: Program) extends BelongMethod{
  override def varRef(name: String): Option[FieldScope] = {
    super.varRef( name ) match {
      case some:Some[_]⇒some
      case None⇒ program.programScope.fields.get(name)
    }
  }
}


package object expression {

  def generateProgram(input: String): Program = {
    val jvmDslLexer = new JvmDslLexer( CharStreams.fromReader( new StringReader( input ) ) )
    val jvmDslParser = new JvmDslParserParser( new CommonTokenStream( jvmDslLexer ) )
    val symbolDefParser = new SymbolDefParser( )
    ParseTreeWalker.DEFAULT.walk( symbolDefParser, jvmDslParser.program( ) )


    val expressionParser = new ExpressionParser( symbolDefParser.programScope )
    ParseTreeWalker.DEFAULT.walk( expressionParser, new JvmDslParserParser( new CommonTokenStream( new JvmDslLexer( CharStreams.fromReader( new StringReader( input ) ) ) ) )
      .program( ) )
    expressionParser.program
  }

  implicit class EnhanceInt(value:Int){
      def int:IntLiteral={
          new IntLiteral(value)
      }
  }

  implicit class EnhanceFloat(value:Float){
      def float:FloatLiteral={
          new FloatLiteral((value))
      }
  }

  implicit class EnhanceDouble(value:Double){
      def double:DoubleLiteral={
          new DoubleLiteral(value)
      }
  }

  implicit class EnhanceLong(value:Long){
    def long:LongLiteral={
        new LongLiteral(value)
    }
  }

  implicit class EnhanceChar(value:Char){
      def char:CharLiteral={
          new CharLiteral(value)
      }
  }

  implicit class EnhanceBool(value:Boolean){
      def bool:BoolLiteral={
          new BoolLiteral(value)
      }
  }

  implicit class EnhanceString(value:String){
      def str:StringLiteral={
          new StringLiteral(value)
      }
      def clazz(expressions:Expression*):ClazzLiteral={
          new ClazzLiteral(expressions.toArray,ClazzType(value,Array()))
      }
  }

  implicit class EnhanceClazzMethod(clazzMethod:ClazzMethod ) {

    def bodyBlock(): BlockWithScope = {
      BlockWithScope( clazzMethod.method.body, clazzMethod.method.methodScope.blockScope,Some(clazzMethod ))
    }

    def belongClazz():Clazz={
          clazzMethod.clazz
    }

  }

  implicit class EnhanceProgramMethod(programMethod: ProgramMethod){

    def bodyBlock():BlockWithScope={
        BlockWithScope(programMethod.method.body,programMethod.method.methodScope.blockScope,Some(programMethod))
    }

    def belongProgram(): Program = {
      programMethod.program
    }

  }

  implicit class EnhanceClazz(clazz: Clazz){
      def method(name:String,ignoreVarRefResolved:Boolean = false):ClazzMethod={
        val methodScope = clazz.clazzScope.methods(name)
        val method = Method( methodScope, Block( ignoreVarRefResolved = ignoreVarRefResolved ) )
        clazz.methods.put( name, method )
        ClazzMethod(method,clazz)
      }

  }


  implicit class EnhanceBlock(blockWithScope: BlockWithScope) {
    def expression(expression: BlockWithScope ⇒ Expression): BlockWithScope = {
      blockWithScope.block.expressions.append( expression.apply( blockWithScope ) )
      blockWithScope
    }

    def statementBlock(): BlockWithScope = {
      block( _ ⇒ Block( ) )
    }

    def ifBlock(): BlockWithScope = {
      block( _ ⇒ new IfBlock( ) )
    }

    def tryBlock(): BlockWithScope = {
      block( _ ⇒ new TryBlock( ) )
    }

    def catchBlock(): BlockWithScope = {
      block( _ ⇒ new CatchBlock( ) )
    }

    def finallyBlock(): BlockWithScope = {
      block( _ ⇒ new FinallyBlock( ) )
    }

    def syncBlock(): BlockWithScope = {
      block( _ ⇒ new SyncBlock( ) )
    }

    def doWhileBlock(): BlockWithScope = {
      block( _ ⇒ new DoWhileBlock( ) )
    }

    def whileBlock(): BlockWithScope = {
      block( _ ⇒ new WhileBlock( ) )
    }

    def forLoopBlock(loopVarDef: BlockScope ⇒ LocalVarDef,
                     loopVarCondition: BlockScope ⇒ Expression,
                     loopVarUpdate: BlockScope ⇒ Expression): BlockWithScope = {
      block( blockScope ⇒ new ForLoop( loopVarDef.apply( blockScope ),
        loopVarCondition.apply( blockScope ),
        loopVarUpdate.apply( blockScope ) ) )
    }

    def forCollectionBlock(loopVarDef: BlockScope ⇒ LocalVarDef,
                           looped: BlockScope ⇒ Expression):BlockWithScope={
        block(blockScope⇒new ForLoopCollection(loopVarDef.apply(blockScope),looped.apply(blockScope)))
    }

    def forMapBlock(loopKeyDef: BlockScope ⇒ LocalVarDef,
                    loopVarDef: BlockScope ⇒ LocalVarDef,
                    looped: BlockScope ⇒ Expression):BlockWithScope={
      block(blockScope⇒new ForLoopMap(loopKeyDef.apply(blockScope),loopVarDef.apply(blockScope),looped.apply(blockScope)))
    }

    def block(block: BlockScope ⇒ Block): BlockWithScope = {
      val childBlockScope = blockWithScope.blockScope.childrenScopes( blockWithScope.childBlockIndex )
      val childBlock = block.apply( childBlockScope )
      blockWithScope.childBlockIndex = blockWithScope.childBlockIndex + 1
      blockWithScope.block.expressions.append( childBlock )
      val childBlockWithScope = BlockWithScope( childBlock, childBlockScope, blockWithScope.belongMethod )
      childBlockWithScope.parent = blockWithScope
      childBlockWithScope
    }

    def lambdaBlock(ignoreVarRefResolved: Boolean = false): BlockWithScope = {
      val lambdaBlockWithScope = blockWithScope.nextLambdaBlock( ignoreVarRefResolved )
      lambdaBlockWithScope.parent = blockWithScope
      lambdaBlockWithScope
    }

    def belongBlock(): BlockWithScope = blockWithScope.parent

    def belongClazzMethod(): ClazzMethod = {
      blockWithScope.belongMethod.get.asInstanceOf[ClazzMethod]
    }

    def belongProgramMethod(): ProgramMethod = {
      blockWithScope.belongMethod.get.asInstanceOf[ProgramMethod]
    }


    def updateVarDef(name: String, dslType: DslType, assign: Option[Expression] = None): BlockWithScope = {
      blockWithScope.block.expressions.append( LocalVarDef( blockWithScope.blockScope.fields( name ), dslType, assign ) )
      blockWithScope
    }

    def varDef(name: String, dslType: DslType, assign: Option[Expression] = None): LocalVarDef = {
      LocalVarDef( blockWithScope.blockScope.fields( name ), dslType, assign )
    }

    def varRef(name: String*): VarRef = {
      blockWithScope.blockScope.fields.get( name.head ) match {
        case Some( fieldScope ) ⇒ VarRef( name.toList, Some( fieldScope ) )
        case None ⇒

          blockWithScope.blockScope match {
            case forStatementBlockScope: ForStatementBlockScope ⇒
              if (forStatementBlockScope.initFields.contains( name.head )) {
                return VarRef( name.toList, Some( forStatementBlockScope.initFields( name.head ) ) )
              }
            case _ ⇒
          }

          if (blockWithScope.parent != null) {
            blockWithScope.parent.varRef( name: _* )
          } else {
            blockWithScope.blockScope.parentScope match {
              case methodScope: MethodScope ⇒
                methodScope.params.get( name.head ) match {
                  case Some( fieldScope ) ⇒ VarRef( name.toList, Some( fieldScope ) )
                  case None ⇒ blockWithScope.blockScope.topScope match {
                    case clazzScope: ClazzScope ⇒
                      VarRef( name.toList, clazzScope.fields.get( name.head ) )
                    case programScope: ProgramScope ⇒
                      VarRef( name.toList, programScope.fields.get( name.head ) )
                  }
                }
              case programScope: ProgramScope ⇒
                VarRef( name.toList, programScope.fields.get( name.head ) )
            }
          }
      }
    }
  }

  implicit class EnhanceBlockScope(blockScope: BlockScope){

    def varDef(name:String,dslType: DslType, assign:Option[Expression] = None):LocalVarDef={
      val fieldScope = blockScope match {
        case forStatementBlockScope:ForStatementBlockScope⇒
              if(forStatementBlockScope.initFields.contains(name)){
                forStatementBlockScope.initFields(name)
              }else{
                forStatementBlockScope.fields(name)
              }
        case _⇒ blockScope.fields(name)
      }

      LocalVarDef(fieldScope,dslType,assign)
    }

    def varRef(name: String*): VarRef = {


      blockScope.fields.get( name.head ) match {
        case Some( fieldScope ) ⇒ VarRef( name.toList, Some( fieldScope ) )
        case None ⇒

            blockScope match {
              case forStatementBlockScope: ForStatementBlockScope ⇒
                if (forStatementBlockScope.initFields.contains( name.head )) {
                  return VarRef( name.toList, Some( forStatementBlockScope.initFields( name.head ) ) )
                }
              case _ ⇒
            }

            blockScope.parentScope match {
              case methodScope: MethodScope ⇒
                methodScope.params.get( name.head ) match {
                  case Some( fieldScope ) ⇒ VarRef( name.toList, Some( fieldScope ) )
                  case None ⇒ blockScope.topScope match {
                    case clazzScope: ClazzScope ⇒
                      VarRef( name.toList, clazzScope.fields.get( name.head ) )
                    case programScope: ProgramScope ⇒
                      VarRef( name.toList, programScope.fields.get( name.head ) )
                  }
                }
              case blockScope: BlockScope⇒
                  blockScope.varRef(name:_*)
              case programScope: ProgramScope ⇒
                VarRef( name.toList, programScope.fields.get( name.head ) )
            }
          }
      }
    }

  implicit class EnhanceProgram(program:Program){

    def assign(name: String, expression: Expression): Program = {
      program.assigned.put( name, expression )
      program
    }

    def nextLambdaBlock(): BlockWithScope = {
      val scope = program.programScope.lambdaScopes.remove( 0 )
      BlockWithScope( Block( ), scope, None )
    }

    def varRef(name:String*):VarRef={
        VarRef(name.toList,program.programScope.fields.get(name.head))
    }

    def method(name:String,ignoreVarRefResolved:Boolean = false):ProgramMethod={
        val methodScope = program.programScope.methods(name)
        val method = Method(methodScope,Block(ArrayBuffer(),ignoreVarRefResolved))
        program.methods.put(name,method)
        ProgramMethod(method, program)
    }

    def clazz(name:String):Clazz={
        val clazzScope = program.programScope.classes(name)
        val clazz = Clazz(clazzScope,MutableMap[String,Method]())
        program.classes.put(name,clazz)
        clazz
    }

    def lambdaBlock():BlockWithScope={
        program.nextLambdaBlock()
    }

  }
}
