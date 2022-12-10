package com.dongjiaqiang.jvm.dsl.core.scope

import com.dongjiaqiang.jvm.dsl.core.`type`.DslType
import com.dongjiaqiang.jvm.dsl.core.{JvmDslParserParser, scope}

import scala.collection.mutable.{ListMap ⇒ MutableMap}

class ClazzScope(val name:String,
                 val fields:MutableMap[String,FieldScope],
                 val methods:MutableMap[String,MethodScope]) extends Scope {
  def this(name:String) {
    this( name, MutableMap( ), MutableMap( ) )
  }

  override def getSymbolType(symbolName: String): scope.SymbolType.Value = {
    if (fields.contains( symbolName )) {
      SymbolType.FIELD
    } else if (methods.contains( symbolName )) {
      SymbolType.METHOD
    } else {
      SymbolType.UNDEFINED
    }
  }

  override def addScope(symbolName:String,fieldScope: FieldScope):Unit = {
    duplicateSymbol( symbolName )
    fields.put( symbolName, fieldScope )
  }

  override def addScope(symbolName:String,methodScope: MethodScope):Unit={
    duplicateSymbol( symbolName )
    methods.put(symbolName,methodScope)
  }

  override def equals(obj: Any): scala.Boolean =
      obj match {
        case clazzScope: ClazzScope⇒
          fields.sameElements(clazzScope.fields) && methods.sameElements(clazzScope.methods)
        case _⇒false
      }
}
