package com.dongjiaqiang.jvm.dsl.api.scope

import com.dongjiaqiang.jvm.dsl.api.`type`._
import com.dongjiaqiang.jvm.dsl.api.scope

import scala.collection.mutable.{ArrayBuffer, ListMap ⇒ MutableMap}

/**
 * <pre><code>
 * program{
 *
 * Int i = 100;
 * Int j = 200;
 *
 *  def foo(Int i)=Int{ //name = foo, outerScopeIndex = 2, parentScope is programScope
 *    return i;
 *  }
 *
 *  class Foo(Int k,Int z){
 *    def foo1()=Int{ //name = foo1, outerScopeIndex = 2, parentScope is classScope foo
 *      return k;
 *    }
 *
 *    def foo2()=Int{ //name = foo2, outerScopeIndex = 3, parentScope is classScope foo
 *      return z;
 *    }
 *  }
 * }
 * method scope
 *<pre><code>
 */
class MethodScope(val name: String,
                  val outerScopeIndex: Int,
                  val params: MutableMap[String, FieldScope],
                  val returnType: DslType,
                  val throws: ArrayBuffer[ClazzType],
                  val parentScope: Scope,
                  var blockScope: BlockScope) extends Scope {

  override def toString: String = s"$parentScope:MethodScope($name)"

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
   * 1. methodScope in program
   * program {
   *    class A(D d,String b){
   *    }
   *    class D(Long i,Int j){
   *    }
   *
   *    def method(A a)=Unit{
   *        Int i1 = this.a; //resolved a in programScope
   *        Int i2 = a; //resolved a in methodScope
   *        Int i3 = a.c; //unresolved
   *        Int i4 = b; //unresolved
   *    }
   *
   *    Int a = 100;
   * }
   *
   * 2. methodScope in class
   *
   * program{
   *    class D(Long i,Int j){}
   *    class A(D d,String b){
   *
   *        def method(A a)=Unit{
   *
   *             Int i1 = a.d.i; //resolved
   *             Int i2 = a.b.i; //unresolved
   *        }
   *
   *    }
   * }
   *
   * resolve ref in current or parent scope
   *
   * @param index ref index
   * @param refs  ref names
   */
  override def resolveVarRefs(index: Int, refs: List[String]): Option[FieldScope]= {
      refs match {
        case "this" :: refs ⇒
          scope.resolveVarRefs( index, refs, this, params, skipCurrentScope = true, backRef = true, Some( parentScope ) )
        case _ ⇒ scope.resolveVarRefs( index, refs, this, params, skipCurrentScope = false, backRef = true, Some( parentScope ) )

      }
  }

}
