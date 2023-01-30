package com.dongjiaqiang.jvm.dsl.core

import com.dongjiaqiang.jvm.dsl.api.`type`.DslType
import com.dongjiaqiang.jvm.dsl.api.scope.{BlockScope, ClazzScope, FieldScope, MethodScope, ProgramScope, Scope}


package object symbol {

    implicit class EnhanceProgramScope(programScope: ProgramScope){

      def method(name:String, outerScopeIndex: Int, returnType: DslType, blockStatements:Int):MethodScope= {
        val methodScope = new MethodScope(name, outerScopeIndex, programScope, returnType)
        methodScope.addScope(new BlockScope(0, methodScope, methodScope.parentScope)).blockScope.incStatement(blockStatements)
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

      def statement(s:Int):ProgramScope={
        programScope.incStatement(s).asInstanceOf[ProgramScope]
      }
    }

    implicit class EnhanceClazzScope(clazzScope: ClazzScope){

      def statement(s:Int):ClazzScope={
        clazzScope.incStatement(s).asInstanceOf[ClazzScope]
      }

      def method(name:String,outerScopeIndex: Int,returnType: DslType,blockStatements:Int):MethodScope={
        val methodScope = new MethodScope(name, outerScopeIndex, clazzScope, returnType)
        methodScope.addScope(new BlockScope(0, methodScope, methodScope.parentScope)).blockScope.incStatement(blockStatements)
        clazzScope.addScope(name,methodScope)
        methodScope
      }

      def field(name:String,outerScopeIndex: Int,dslType: DslType,programScope: ProgramScope):ClazzScope={
        clazzScope.addScope(name,new FieldScope(outerScopeIndex, name, dslType, clazzScope, programScope))
      }
    }

    implicit class EnhanceMethodScope(methodScope: MethodScope) {

      def body():BlockScope = {
        methodScope.blockScope
      }

      def statement(s:Int):MethodScope={
        methodScope.incStatement(s).asInstanceOf[MethodScope]
      }

      def field(name:String,outerScopeIndex: Int,dslType: DslType,programScope: ProgramScope): MethodScope = {
        methodScope.addScope(name,new FieldScope(outerScopeIndex, name, dslType, methodScope, programScope))
      }

      def belongProgram(): ProgramScope = {
        methodScope.parentScope.asInstanceOf[ProgramScope]
      }

      def belongClazz():ClazzScope={
        methodScope.parentScope.asInstanceOf[ClazzScope]
      }
    }

    implicit class EnhanceBlockScope(blockScope: BlockScope){

      def statement(s:Int):BlockScope={
        blockScope.incStatement(s).asInstanceOf[BlockScope]
      }

      def field(name:String,outerScopeIndex: Int,dslType: DslType,programScope: ProgramScope): BlockScope = {
        blockScope.addScope(name,new FieldScope(outerScopeIndex, name, dslType, blockScope, programScope))
      }

      def block(outerScopeIndex: Int,topScope:Scope):BlockScope={
        val childBlockScope = new BlockScope(outerScopeIndex,blockScope,topScope)
        blockScope.addScope(childBlockScope)
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
