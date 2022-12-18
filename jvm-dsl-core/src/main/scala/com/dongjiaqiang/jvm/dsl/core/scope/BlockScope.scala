package com.dongjiaqiang.jvm.dsl.core.scope

import com.dongjiaqiang.jvm.dsl.core.scope

import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.{ListMap ⇒ MutableMap}

class BlockScope(val outerScopeIndex:Int, val fields: MutableMap[String, FieldScope],
                 val parentScope:Scope,
                 val topScope:Scope,
                 val childrenScopes:ArrayBuffer[BlockScope]) extends Scope {

  def this(outScopeIndex: Int, parentScope: Scope,topScope:Scope) {
    this( outScopeIndex, MutableMap( ), parentScope,topScope, ArrayBuffer( ) )
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
   *
   * blockScope in program method
   * program{
   *
   *    class A(D d,String b){
   *    }
   *    class D(Long i,Int j){
   *    }
   *    A a = new A(new D(1L,2),"xx");
   *
   *    def method()=Unit{
   *        this.b; //resolved
   *        this.a.d; //resolved
   *        this.a.c; //unresolved
   *
   *        Int j = 1;  //index 0
   *        Int i = 100;  //index 1
   *
   *        {
   *          Int z = i;  //index 0 resolved in parent scope
   *
   *          j+i;  //index 1 resolved in parent scope
   *
   *          k.i+100;  //index 2 unresolved
   *
   *          Int k = 300; //index 3
   *
   *          {
   *            k+100; //index 0  resolved in parent scope
   *          } //index 4
   *
   *        } //index 2
   *
   *        Int k = 100; //index 3
   *    }
   *
   *     Int b = 100;
   *
   * }
   *
   * resolve ref in current or outer scope
   *
   * @param index ref index
   * @param refs  ref names
   */
  override def resolveVarRefs(index: Int, refs: List[String]): Option[FieldScope] = {
        refs match {
          case "this"::childRef ⇒ scope.resolveVarRefs(index,childRef,this,fields,skipCurrentScope = true,backRef = true,Some(topScope))
          case _ ⇒  scope.resolveVarRefs(index,refs,this,fields,skipCurrentScope = false,backRef = false,Some(parentScope))
        }
  }

}
