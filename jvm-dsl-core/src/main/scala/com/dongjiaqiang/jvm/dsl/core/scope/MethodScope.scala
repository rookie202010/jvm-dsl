package com.dongjiaqiang.jvm.dsl.core.scope

import com.dongjiaqiang.jvm.dsl.core.`type`.{ClazzType, DslType}
import com.dongjiaqiang.jvm.dsl.core.scope

import scala.collection.mutable.{ArrayBuffer, ListMap ⇒ MutableMap}

class MethodScope(val name:String,
                  val index:Int,
                  val params:MutableMap[String,FieldScope],
                  val returnType:DslType,
                  val throws:ArrayBuffer[ClazzType],
                  val parentScope:Scope,
                  var blockScope: BlockScope) extends Scope {


  override val size: Int = params.size + 1

  def this(name: String, index: Int, parentScope: Scope, returnType: DslType) {
    this( name: String, index, MutableMap( ), returnType, ArrayBuffer( ), parentScope, null )
  }


  override def addScope(symbolName: String, fieldScope: FieldScope): Unit = {
    duplicateSymbol( symbolName )
    params.put( symbolName, fieldScope )
  }


  override def addScope(blockScope: BlockScope): Unit = {
    if (this.blockScope == null) {
      this.blockScope = blockScope
    } else {
      throw new IllegalStateException( "can not set block scope twice" )
    }
  }

  override def getSymbolType(symbolName: String): scope.SymbolType.Value = {
    if (params.contains( symbolName )) {
      SymbolType.FIELD
    } else {
      if (blockScope.fields.contains( symbolName )) {
        SymbolType.FIELD
      } else {
        SymbolType.UNDEFINED
      }
    }
  }

  override def equals(obj: Any): scala.Boolean =
    obj match {
      case methodScope: MethodScope ⇒
        params.sameElements( methodScope.params ) &&
          throws.sameElements( methodScope.throws ) &&
          blockScope == methodScope.blockScope
      case _ ⇒ false
    }
}
