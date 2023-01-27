package com.dongjiaqiang.jvm.dsl.api.scope

import com.dongjiaqiang.jvm.dsl.api.scope

import scala.collection.mutable.{ArrayBuffer, ListMap ⇒ MutableMap}

class ProgramScope(val fields: MutableMap[String, FieldScope],
                   val classes: MutableMap[String, ClazzScope],
                   val methods: MutableMap[String, MethodScope],
                   val importClasses: ArrayBuffer[String] = ArrayBuffer( ),
                   val importClassesFromPackage: MutableMap[String, String] = MutableMap( ),
                   val importPackages: MutableMap[String, String] = MutableMap( ),
                   val lambdaBlockScope: ArrayBuffer[BlockScope] = ArrayBuffer( )
                  ) extends Scope {

  override def toString:String = "ProgramScope"

  override val outerScopeIndex: Int = 0

  def this() {
    this( MutableMap( ), MutableMap( ), MutableMap( ) )
  }

  def isClazzType(name:String):Boolean={
      importClasses.contains(name) || importClassesFromPackage.contains(name)
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

  /**
   * resolve var refs in program (defined fields)
   */
  override def resolveVarRefs(index: Int, refs: List[String]): Option[FieldScope] = {
    scope.resolveVarRefs(index,refs,this, fields,skipCurrentScope = false,backRef = true,None)
  }

  /**
   * resolve method in current scope
   */
  override def resolveMethod(name: String): Option[MethodScope] = { methods.get(name)}
}
