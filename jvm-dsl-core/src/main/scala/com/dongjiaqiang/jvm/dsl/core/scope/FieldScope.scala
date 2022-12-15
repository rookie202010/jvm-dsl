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
   *
   * program {
   * class A(D d,Long k){
   *
   * def method(Long p)=Unit{
   * Int j1 = d.i;
   * Int j2 = k.i;
   * Int j3 = p;
   *
   * {
   * Int j2 = j1;
   * }
   *
   * }
   * }
   *
   * class D(Int i,Int j){
   *
   * }
   *
   * A a = new A(new D(1,2),3L);
   * Int b = 1;
   *
   * def method()=Unit{
   * Int i1 = a.d.i;
   * Int i2 = b;
   * Int i3 = c;
   * }
   * }
   *
   * resolve localRefs in fieldScope
   *
   * @param index localRefs index in blockScope
   * @param refs  localRefs names
   * @return
   */
  override def resolve(index: Int, refs: List[String]): Resolved.Value = {
    refs match {
      case ref :: Nil ⇒
        if (ref == symbolName) {
          Resolved.TRUE
        } else {
          Resolved.FALSE
        }
      case ref :: childRef ⇒
        if(ref != symbolName){
            return Resolved.FALSE
        }
        dslType match {
          case clazzType: ClazzType ⇒
            resolve( childRef, clazzType )
          case _ ⇒ Resolved.FALSE
        }
    }

  }

  def resolve(childRef: List[String], dslType: ClazzType): Resolved.Value = {
    programScope.classes.get( dslType.clazzName ) match {
      case Some( clazzScope ) ⇒
        val fields = clazzScope.fields
        if (childRef.size == 1) {
          fields.get( childRef.head ) match {
            case Some( _ ) ⇒ Resolved.TRUE
            case None ⇒ Resolved.FALSE
          }
        } else {
          fields.get( childRef.head ) match {
            case Some( fieldScope ) ⇒
              fieldScope.dslType match {
                case childDslType: ClazzType ⇒
                  resolve( childRef.tail, childDslType )
                case _ ⇒ Resolved.FALSE
              }
            case None ⇒ Resolved.FALSE
          }
        }
      case None ⇒ Resolved.UNDEFINED
    }
  }

}
