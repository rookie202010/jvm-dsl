package com.dongjiaqiang.jvm.dsl.core.scope

import com.dongjiaqiang.jvm.dsl.core.scope

import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.{ListMap ⇒ MutableMap}

class BlockScope(val outerScopeIndex:Int, val fields: MutableMap[String, FieldScope],
                 val parentScope:Scope,
                 val childrenScopes:ArrayBuffer[BlockScope]) extends Scope {

  def this(outScopeIndex: Int, parentScope: Scope) {
    this( outScopeIndex, MutableMap( ), parentScope, ArrayBuffer( ) )
  }

  override def addScope(symbolName: String, fieldScope: FieldScope): BlockScope = {
    duplicateSymbol( symbolName )
    fields.put( symbolName, fieldScope )
    this
  }

  override def addScope(blockScope: BlockScope): BlockScope = {
    childrenScopes.append( blockScope )
    this
  }

  override def getSymbolType(symbolName: String): scope.SymbolType.Value = {
    if (parentScope.isInstanceOf[MethodScope] &&
      parentScope.getSymbolType( symbolName ) != SymbolType.UNDEFINED) {
      return parentScope.getSymbolType( symbolName )
    }

    if (fields.contains( symbolName )) {
      SymbolType.FIELD
    } else {
      SymbolType.UNDEFINED
    }
  }

  override def equals(obj: Any): scala.Boolean =
    obj match {
      case blockScope: BlockScope ⇒
        fields.sameElements( blockScope.fields ) && childrenScopes.sameElements( blockScope.childrenScopes ) &&
          outerScopeIndex == blockScope.outerScopeIndex &&
          statements == blockScope.statements
      case _ ⇒ false
    }

  /**
   * resolve ref in current or outer scope
   *
   * @param index ref index
   * @param refs  ref names
   */
  override def resolve(index: Int, refs: List[String]): scope.Resolved.Value = {
      Resolved.FALSE
  }
}
