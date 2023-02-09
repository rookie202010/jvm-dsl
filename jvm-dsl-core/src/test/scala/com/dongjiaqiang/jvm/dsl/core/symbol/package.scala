package com.dongjiaqiang.jvm.dsl.core

import com.dongjiaqiang.jvm.dsl.api.`type`.{ClazzType, DslType}
import com.dongjiaqiang.jvm.dsl.api.scope._
import com.dongjiaqiang.jvm.dsl.core.parser.SymbolDefParser
import org.antlr.v4.runtime.tree.ParseTreeWalker
import org.antlr.v4.runtime.{CharStreams, CommonTokenStream}

import java.io.StringReader


package object symbol {


    def generateProgramScope(input:String):ProgramScope={

      val jvmDslLexer = new JvmDslLexer( CharStreams.fromReader( new StringReader( input ) ) )
      val jvmDslParser = new JvmDslParserParser( new CommonTokenStream( jvmDslLexer ) )
      val symbolDefParser = new SymbolDefParser( )
      ParseTreeWalker.DEFAULT.walk( symbolDefParser, jvmDslParser.program( ) )
      symbolDefParser.programScope

    }

    implicit class EnhanceProgramScope(programScope: ProgramScope){

      def method(name:String, outerScopeIndex: Int, returnType: DslType, bodyStatements:Int,sync:Boolean=false):MethodScope= {
        val methodScope = new MethodScope(name, outerScopeIndex,programScope, returnType,sync)
        methodScope.addScope(new BlockScope(0, methodScope, methodScope.parentScope)).blockScope.incStatement(bodyStatements)
        programScope.addScope(name, methodScope)
        methodScope
      }

      def field(name:String,outerScopeIndex: Int,dslType: DslType,volatile:Boolean = false):ProgramScope= {
        programScope.addScope(name,new FieldScope(outerScopeIndex, name, dslType, programScope, programScope,volatile))
      }

      def clazz(name:String,outerScopeIndex: Int):ClazzScope={
        val clazzScope = new ClazzScope(outerScopeIndex,name)
        programScope.addScope(name,clazzScope)
        clazzScope
      }

      def members(s:Int):ProgramScope={
        programScope.incStatement(s).asInstanceOf[ProgramScope]
      }

      def lambdaBlock(outerScopeIndex: Int, blockStatements: Int): BlockScope = {
        val childBlockScope = new BlockScope( outerScopeIndex, programScope, programScope )
        childBlockScope.incStatement( blockStatements )
        programScope.lambdaScopes.append( childBlockScope )
        childBlockScope
      }
    }

    implicit class EnhanceClazzScope(clazzScope: ClazzScope){

      def members(s:Int):ClazzScope={
        clazzScope.incStatement(s).asInstanceOf[ClazzScope]
      }

      def method(name:String, outerScopeIndex: Int, returnType: DslType, bodyStatements:Int,sync:Boolean=false):MethodScope={
        val methodScope = new MethodScope(name, outerScopeIndex, clazzScope, returnType,sync)
        methodScope.addScope(new BlockScope(0, methodScope, methodScope.parentScope)).blockScope.incStatement(bodyStatements)
        clazzScope.addScope(name,methodScope)
        methodScope
      }

      def field(name:String,outerScopeIndex: Int,dslType: DslType)(implicit programScope: ProgramScope):ClazzScope={
        clazzScope.addScope(name,new FieldScope(outerScopeIndex, name, dslType, clazzScope, programScope))
      }
    }

    implicit class EnhanceMethodScope(methodScope: MethodScope) {

      def body():BlockScope = {
        methodScope.blockScope
      }

      def inputParams(s:Int):MethodScope={
        methodScope.incStatement(s).asInstanceOf[MethodScope]
      }

      def throwEx(clazzTypes: ClazzType*):MethodScope={
          methodScope.throws.appendAll(clazzTypes)
          methodScope
      }

      def field(name:String,outerScopeIndex: Int,dslType: DslType)(implicit programScope: ProgramScope): MethodScope = {
        methodScope.addScope(name,new FieldScope(outerScopeIndex, name, dslType, methodScope, programScope))
      }

      def belongProgram(): ProgramScope = {
        methodScope.parentScope.asInstanceOf[ProgramScope]
      }

      def belongClazz():ClazzScope={
        methodScope.parentScope.asInstanceOf[ClazzScope]
      }
    }


    implicit class EnhanceForBlockScope(blockScope: ForStatementBlockScope){

      def iField(name:String,dslType: DslType)(implicit programScope: ProgramScope): ForStatementBlockScope={
          blockScope.initFields.put(name,new FieldScope(0,name,dslType,blockScope,programScope))
          blockScope
      }
    }

    implicit class EnhanceBlockScope(blockScope: BlockScope){

      def statement(s:Int):BlockScope={
        blockScope.incStatement(s).asInstanceOf[BlockScope]
      }

      def field(name:String,outerScopeIndex: Int,dslType: DslType)(implicit programScope: ProgramScope): BlockScope = {
        blockScope.addScope(name,new FieldScope(outerScopeIndex, name, dslType, blockScope, programScope))
      }

      def lambdaBlock(outerScopeIndex: Int,blockStatements:Int):BlockScope={
        val childBlockScope = new BlockScope( outerScopeIndex, blockScope, blockScope.topScope )
        childBlockScope.incStatement( blockStatements )
        blockScope.lambdaScopes.append( childBlockScope )
        childBlockScope
      }

      def block(outerScopeIndex: Int,blockStatements:Int):BlockScope={
        val childBlockScope = new BlockScope(outerScopeIndex,blockScope,blockScope.topScope)
        childBlockScope.incStatement(blockStatements)
        blockScope.addScope(childBlockScope)
        childBlockScope
      }

      import scala.collection.mutable.{ListMap ⇒ MutableMap}

      def forBlock(outerScopeIndex: Int,blockStatements:Int):ForStatementBlockScope={
        val childBlockScope = new ForStatementBlockScope( outerScopeIndex,MutableMap(), blockScope, blockScope.topScope )
        childBlockScope.incStatement( blockStatements )
        blockScope.addScope( childBlockScope )
        childBlockScope
      }

      def belongMethod():MethodScope={
        blockScope.parentScope.asInstanceOf[MethodScope]
      }

      def belongBlock():BlockScope={
        blockScope.parentScope.asInstanceOf[BlockScope]
      }

      def belongProgram():ProgramScope={
        blockScope.topScope.asInstanceOf[ProgramScope]
      }

      def belongClazz():ClazzScope={
        blockScope.topScope.asInstanceOf[ClazzScope]
      }
    }
}
