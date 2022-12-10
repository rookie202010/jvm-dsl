package com.dongjiaqiang.jvm.dsl.core.scope

import scala.collection.mutable.{ListMap⇒MutableMap}

class ProgramScope(val fields:MutableMap[String,FieldScope],
                   val classes: MutableMap[String,ClazzScope],
                   val methods:MutableMap[String,MethodScope]) extends Scope {

  def this(){
    this(MutableMap(),MutableMap(),MutableMap())
  }

  override def addScope(symbolName: String, fieldScope: FieldScope): Unit = {
    duplicateSymbol(symbolName)
    fields.put(symbolName,fieldScope)
  }

  override def addScope(symbolName: String, clazzScope: ClazzScope): Unit = {
    duplicateSymbol(symbolName)
    classes.put(symbolName,clazzScope)
  }


  override def addScope(symbolName: String, methodScope: MethodScope): Unit = {
    duplicateSymbol(symbolName)
    methods.put(symbolName,methodScope)
  }

  override def getSymbolType(symbolName:String): SymbolType.Value={
      if(fields.contains(symbolName)){
          SymbolType.FIELD
      }else if(classes.contains(symbolName)){
          SymbolType.CLAZZ
      }else if(methods.contains(symbolName)) {
        SymbolType.METHOD
      }else{
        SymbolType.UNDEFINED
    }
  }

  override def equals(obj: Any): scala.Boolean =
      obj match {
        case programScope: ProgramScope⇒
          (fields sameElements programScope.fields) &&
          (classes sameElements programScope.classes) &&
          (methods sameElements programScope.methods)
      }


}
