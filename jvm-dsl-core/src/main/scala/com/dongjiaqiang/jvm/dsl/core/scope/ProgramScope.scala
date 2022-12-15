package com.dongjiaqiang.jvm.dsl.core.scope

import scala.collection.mutable.{ListMap⇒MutableMap}

class ProgramScope(val fields:MutableMap[String,FieldScope],
                   val classes: MutableMap[String,ClazzScope],
                   val methods:MutableMap[String,MethodScope]) extends Scope {


  override val outerScopeIndex: Int = 0

  def this() {
    this( MutableMap( ), MutableMap( ), MutableMap( ) )
  }

  override def addScope(symbolName: String, fieldScope: FieldScope): ProgramScope = {
    duplicateSymbol( symbolName )
    fields.put( symbolName, fieldScope )
    this
  }

  override def addScope(symbolName: String, clazzScope: ClazzScope): ProgramScope = {
    duplicateSymbol( symbolName )
    classes.put( symbolName, clazzScope )
    this
  }


  override def addScope(symbolName: String, methodScope: MethodScope): ProgramScope = {
    duplicateSymbol( symbolName )
    methods.put( symbolName, methodScope )
    this
  }

  override def getSymbolType(symbolName: String): SymbolType.Value = {
    if (fields.contains( symbolName )) {
      SymbolType.FIELD
    } else if (classes.contains( symbolName )) {
      SymbolType.CLAZZ
    } else if (methods.contains( symbolName )) {
      SymbolType.METHOD
    } else {
      SymbolType.UNDEFINED
    }
  }

  override def equals(obj: Any): scala.Boolean =
    obj match {
      case programScope: ProgramScope ⇒
        (fields sameElements programScope.fields) &&
          (classes sameElements programScope.classes) &&
          (methods sameElements programScope.methods) &&
        statements == programScope.statements
    }

  override def resolve(index: Int, refs: List[String]): Resolved.Value = {
    import com.dongjiaqiang.jvm.dsl.core.scope
    scope.resolve(index,refs,fields,skipCurrentScope = false,None)
  }
}
