package com.dongjiaqiang.jvm.dsl.core.scope
import com.dongjiaqiang.jvm.dsl.core.`type`.{AnyType, ClazzType, DslType, UnResolvedType}
import com.dongjiaqiang.jvm.dsl.core.scope

/**
 *  field scope
 * <pre><code>
 * program{
 *
 *    Int a = 1;// outerScopeIndex = 0, symbolName = a,dslType = IntType,belong to programScope
 *
 *    def foo(Long a)=Unit{ // outerScopeIndex = 0,symbolName = a,dslType = LongType,belong to methodScope foo
 *        a = 10;
 *
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
   * @param refs  localRefs names
   * @return
   */
  override def resolveVarRefs(index: Int, refs: List[String]): Option[FieldScope] = {
    refs match {
      case ref :: Nil ⇒
        if (ref == symbolName) {
          Some(this)
        } else {
          None
        }
      case ref :: childRef ⇒
        if(ref != symbolName){
            None
        }else {
          dslType match {
            case clazzType: ClazzType ⇒
              resolve( childRef, clazzType )
            case UnResolvedType | AnyType ⇒ Some( this )
            case _ ⇒ None
          }
        }
    }
  }

  def resolve(childRef: List[String], dslType: ClazzType): Option[FieldScope] = {
    programScope.classes.get( dslType.clazzName ) match {
      case Some( clazzScope ) ⇒
        val fields = clazzScope.fields
        if (childRef.size == 1) {
          fields.get( childRef.head ) match {
            case Some( fieldScope ) ⇒ Some(fieldScope)
            case None ⇒ None
          }
        } else {
          fields.get( childRef.head ) match {
            case Some( fieldScope ) ⇒
              fieldScope.dslType match {
                case childDslType: ClazzType ⇒
                  resolve( childRef.tail, childDslType )
                case _ ⇒ None
              }
            case None ⇒ None
          }
        }
      case None ⇒ Some(this)
    }
  }

}
