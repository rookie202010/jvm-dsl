package com.dongjiaqiang.jvm.dsl.core.scope
import com.dongjiaqiang.jvm.dsl.core.`type`.{ClazzType, DslType}
import com.dongjiaqiang.jvm.dsl.core.scope

import scala.collection.immutable.Stream.Empty

class FieldScope(val outerScopeIndex:Int,
                 val symbolName:String,
                 val dslType: DslType,
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
   * fieldScope in program,class,method params
   *
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
   *
   * resolve localRefs in fieldScope
   *
   * @param index localRefs index in blockScope
   * @param refs  localRefs names
   * @return
   */
  override def resolveVarRefs(index: Int, refs: List[String]): Resolved = {
    refs match {
      case ref :: Nil ⇒
        if (ref == symbolName) {
          Success(dslType)
        } else {
          Failure
        }
      case ref :: childRef ⇒
        if(ref != symbolName){
            Failure
        }else {
          dslType match {
            case clazzType: ClazzType ⇒
              resolve(childRef, clazzType)
            case _ ⇒ Failure
          }
        }
    }
  }

  def resolve(childRef: List[String], dslType: ClazzType): Resolved = {
    programScope.classes.get( dslType.clazzName ) match {
      case Some( clazzScope ) ⇒
        val fields = clazzScope.fields
        if (childRef.size == 1) {
          fields.get( childRef.head ) match {
            case Some( fieldScope ) ⇒ Success(fieldScope.dslType)
            case None ⇒ Failure
          }
        } else {
          fields.get( childRef.head ) match {
            case Some( fieldScope ) ⇒
              fieldScope.dslType match {
                case childDslType: ClazzType ⇒
                  resolve( childRef.tail, childDslType )
                case _ ⇒ Failure
              }
            case None ⇒ Failure
          }
        }
      case None ⇒ Undefined(dslType)
    }
  }

}
