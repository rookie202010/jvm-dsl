package com.dongjiaqiang.jvm.dsl.core.scope

import com.dongjiaqiang.jvm.dsl.core
import com.dongjiaqiang.jvm.dsl.core.`type`.{ClazzType, DslType}
import com.dongjiaqiang.jvm.dsl.core.scope

import scala.collection.mutable.{ArrayBuffer, ListMap ⇒ MutableMap}
class MethodScope(val name:String,
                  val outerScopeIndex:Int,
                  val params:MutableMap[String,FieldScope],
                  val returnType:DslType,
                  val throws:ArrayBuffer[ClazzType],
                  val parentScope:Scope,
                  var blockScope: BlockScope) extends Scope {

  def this(name: String, outerScopeIndex: Int, parentScope: Scope, returnType: DslType) {
    this( name, outerScopeIndex, MutableMap( ), returnType, ArrayBuffer( ), parentScope, null )
  }


  override def addScope(symbolName: String, fieldScope: FieldScope): MethodScope = {
    duplicateSymbol( symbolName )
    params.put( symbolName, fieldScope )
    this
  }


  override def addScope(blockScope: BlockScope):  MethodScope = {
    if (this.blockScope == null) {
      this.blockScope = blockScope
      this
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
          blockScope == methodScope.blockScope &&
          outerScopeIndex == methodScope.outerScopeIndex &&
          statements == methodScope.statements
      case _ ⇒ false
    }

  /**
   *
   * program {
   *    class A(D d,String b){
   *    }
   *    class D(Long i,Int j){
   *    }
   *
   *    Int a = 100;
   *
   *    def method(A a)=Unit{
   *        Int i1 = a.d.j;
   *        Int i2 = a.c;
   *    }
   *
   *    def method(Int b)=Unit{
   *        Int j1 = a;
   *    }
   *
   * }
   * resolve ref in current or outer scope
   *
   * @param index ref index
   * @param refs  ref names
   */
  override def resolve(index: Int, refs: List[String]): scope.Resolved.Value = {
      refs match {
        case "this"::refs⇒
            core.scope.resolve(index,refs,params,skipCurrentScope = true,Some(parentScope))
        case _ ⇒ core.scope.resolve(index,refs,params,skipCurrentScope = false,Some(parentScope))

      }
  }
}
