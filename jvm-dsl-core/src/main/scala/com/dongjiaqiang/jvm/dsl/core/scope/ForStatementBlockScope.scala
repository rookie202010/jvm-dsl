package com.dongjiaqiang.jvm.dsl.core.scope

import com.dongjiaqiang.jvm.dsl.core.scope

import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.{ListMap ⇒ MutableMap}

class ForStatementBlockScope(override val index:Int,val initFields:MutableMap[String,FieldScope], val parent: Scope)
extends BlockScope(index,parent) {


  override val size: Int = {
      initFields.size + super.size
  }

  override def getSymbolType(symbolName: String): scope.SymbolType.Value = {
    if (initFields.contains(symbolName)) {
        SymbolType.FIELD
    } else {
      super.getSymbolType( symbolName )
    }
  }


  def addInitScope(symbolName:String,fieldScope: FieldScope):Unit={
    duplicateSymbol(symbolName)
    initFields.put(symbolName,fieldScope)
  }

  override def equals(obj: Any): scala.Boolean =
    obj match {
      case forStatementBlockScope: ForStatementBlockScope⇒
          initFields.sameElements(forStatementBlockScope.initFields) && super.equals(obj)
      case _⇒false
    }
}
