package com.dongjiaqiang.jvm.dsl.core.scope

import com.dongjiaqiang.jvm.dsl.core.scope

import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.{ListMap ⇒ MutableMap}

class BlockScope(val fields: MutableMap[String, FieldScope],
                 val parentScope:Scope,
                 val childrenScopes:ArrayBuffer[BlockScope]) extends Scope {

  def this(parentScope:Scope){
    this(MutableMap(),parentScope,ArrayBuffer())
  }


  override def addScope(symbolName: String, fieldScope: FieldScope): Unit = {
    duplicateSymbol( symbolName )
    fields.put( symbolName, fieldScope )
  }

  override def addScope(blockScope:BlockScope):Unit = {
      childrenScopes.append(blockScope)
  }

  override def getSymbolType(symbolName: String): scope.SymbolType.Value = {
      if(parentScope.isInstanceOf[MethodScope] &&
        parentScope.getSymbolType(symbolName)!=SymbolType.UNDEFINED){
          return parentScope.getSymbolType(symbolName)
      }

      if(fields.contains(symbolName)){
          SymbolType.FIELD
      }else {
          SymbolType.UNDEFINED
      }
  }

  override def equals(obj: Any): scala.Boolean =
      obj match {
        case blockScope: BlockScope⇒
          fields.sameElements(blockScope.fields) && childrenScopes.sameElements(blockScope.childrenScopes)
        case _⇒false
      }
}
