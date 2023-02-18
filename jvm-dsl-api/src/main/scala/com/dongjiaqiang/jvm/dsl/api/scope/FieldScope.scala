package com.dongjiaqiang.jvm.dsl.api.scope

import com.dongjiaqiang.jvm.dsl.api.`type`._
import com.dongjiaqiang.jvm.dsl.api.scope

/**
 * field scope
 * <pre><code>
 * program{
 *
 *    Int a = 1;// outerScopeIndex = 0, symbolName = a,dslType = IntType,belong to programScope
 *
 *    def foo(Long a)=Unit{ // outerScopeIndex = 0,symbolName = a,dslType = LongType,belong to methodScope foo
 *        a = 10;
 *        {
 *          c = 100;
 *          a = 200;
 *          String s; //outerScopeIndex = 2,symbolName = s,dslType = StringType,belong to blockScope block1
 *        } // block1
 *
 *        Int c = 10; //outerScopeIndex = 2,symbolName = c,dslType = IntType,belong to blockScope block0
 *
 *    } //block0
 *
 *    Int b = 1; // outerScopeIndex = 2, symbolName = b,dslType = IntType,belong to programScope
 *
 *    class Foo(Int a,Long b){
 *      //outerScopeIndex = 0,symbolName = a,dslType = IntType,belong to clazzScope Foo
 *      //outerScopeIndex = 0,symbolName = b,dslType = LongType,belong to clazzScope Foo
 *    }
 * }
 *<pre><code>
 */

class FieldScope(val outerScopeIndex:Int,
                 val symbolName:String,
                 val dslType: DslType,
                 val belongScope:Scope,
                 val programScope: ProgramScope,
                 val volatile:Boolean = false) extends Scope {

  override def toString: String =
    s"""
        FieldScope:
        statements $statements
        outerScopeIndex = $outerScopeIndex
        fieldName = $symbolName
        fieldType = $dslType
        volatile = $volatile
      """

  override def getSymbolType(symbolName: String): scope.SymbolType.Value = {
    if (symbolName == this.symbolName) {
      SymbolType.FIELD
    } else {
      SymbolType.UNDEFINED
    }
  }

  override def equals(obj: Any): scala.Boolean =
    obj match {
      case fieldScope: FieldScope ⇒
        symbolName == fieldScope.symbolName &&
          dslType == fieldScope.dslType &&
          volatile == fieldScope.volatile &&
          outerScopeIndex == fieldScope.outerScopeIndex &&
          statements == fieldScope.statements
      case _ ⇒ false
    }

  /**
   *
   * fieldScope in program,class,method params
   *<pre><code>
   * program{
   *    Class A(B a,Long i){}
   *    Class B(Int j,Int k){}
   *
   *    A a = new A(new B(1,2),11L);
   *    Int b = 10L;
   *
   *    D d = new D(Int k,Int z);
   *
   *    def method()=Unit{
   *        Int i1 = a.a; // resolved
   *        Int i2 = a.b; // unresolved
   *
   *        Int i3 = b; // resolved
   *        Int i4 = c; // unresolved
   *
   *        Int i5 = d.k; // undefined because d may be imported class but we can not resolve in this phase
   *        Int i6 = e.a; // unresolved
   *
   *        Int i7 = b.a; // unresolved
   *
   *    }
   * }
   *<pre><code>
   * resolve localRefs in fieldScope
   *
   * @param index localRefs index in blockScope
   * @param arrayRefsIndex array refs index
   * @param refs  localRefs names
   * @return
   */
  override def resolveVarRefs(index: Int, refs: List[String], arrayRefsIndex:Set[Int]): Option[FieldScope] = {
    refs.zipWithIndex match {
      case (ref,_) :: Nil ⇒
        if (ref == symbolName) {
          Some(this)
        } else {
          None
        }
      case (ref,index) :: childRef ⇒
        if(ref != symbolName){
            None
        }else {
          dslType match {
            case clazzType: ClazzType ⇒
              if(arrayRefsIndex.contains(index) && clazzType.clazzName=="Array"){
                val parameterType = clazzType.parameterTypes.head
                parameterType match {
                  case clazzType: ClazzType⇒ resolve(childRef,arrayRefsIndex,clazzType)
                  case _⇒None
                }
              }else {
                resolve( childRef,arrayRefsIndex, clazzType )
              }
            case UnResolvedType | AnyType ⇒ Some( this )
            case _ ⇒ None
          }
        }
    }
  }

  def resolve(childRefWithIndex: List[(String,Int)], arrayRefsIndex:Set[Int], dslType: ClazzType): Option[FieldScope] = {
    val (childRef,index) = childRefWithIndex.head

    programScope.classes.get( dslType.clazzName ) match {
      case Some( clazzScope ) ⇒
        val fields = clazzScope.fields
        if (childRefWithIndex.size == 1) {
          fields.get( childRef ) match {
            case Some( fieldScope ) ⇒ Some(fieldScope)
            case None ⇒ None
          }
        } else {
          fields.get( childRef ) match {
            case Some( fieldScope ) ⇒
              fieldScope.dslType match {
                case childDslType: ClazzType ⇒
                  if(arrayRefsIndex.contains(index) && childDslType.clazzName=="Array"){
                    childDslType.parameterTypes.head match {
                      case clazzType: ClazzType ⇒ resolve( childRefWithIndex, arrayRefsIndex, clazzType )
                      case _ ⇒ None
                    }
                  }else {
                    resolve( childRefWithIndex.tail, arrayRefsIndex, childDslType )
                  }
                case _ ⇒ None
              }
            case None ⇒ None
          }
        }
      case None ⇒ Some(this)
    }
  }

}
