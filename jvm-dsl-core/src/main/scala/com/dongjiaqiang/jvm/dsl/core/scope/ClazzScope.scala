package com.dongjiaqiang.jvm.dsl.core.scope

import com.dongjiaqiang.jvm.dsl.core.`type`.{ClazzType, DslType}
import com.dongjiaqiang.jvm.dsl.core.{JvmDslParserParser, scope}

import scala.collection.mutable.{ListMap ⇒ MutableMap}

class ClazzScope(val outerScopeIndex:Int, val name:String,
                 val fields:MutableMap[String,FieldScope],
                 val methods:MutableMap[String,MethodScope]) extends Scope {
  def this(outScopeIndex:Int, name:String) {
    this( outScopeIndex,name, MutableMap( ), MutableMap( ) )
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

  override def addScope(symbolName:String,fieldScope: FieldScope):ClazzScope = {
    duplicateSymbol( symbolName )
    fields.put( symbolName, fieldScope )
    this
  }

  override def addScope(symbolName:String,methodScope: MethodScope):ClazzScope={
    duplicateSymbol( symbolName )
    methods.put(symbolName,methodScope)
    this
  }

  override def equals(obj: Any): scala.Boolean =
      obj match {
        case clazzScope: ClazzScope⇒
          fields.sameElements(clazzScope.fields) && methods.sameElements(clazzScope.methods) &&
            outerScopeIndex == clazzScope.outerScopeIndex &&
            statements == clazzScope.statements
        case _⇒false
      }

  /**
   *
   *  clazz scope in program
   *
   *  program{
   *
   *      class A(B b,Int c){
   *          def method()=Unit{
   *              Int i1 = b.i; //resolved
   *              Int i2 = c.c; //unresolved
   *              Int i4 = d; //unresolved
   *          }
   *      }
   *      class B(Int i,Int j);
   *
   *
   *  }
   *
   * resolve ref in current or outer scope
   *
   * @param index ref index
   * @param refs   ref names
   */
  override def resolveVarRefs(index: Int, refs: List[String]): Resolved = {
      scope.resolveVarRefs(index, refs,this, fields, skipCurrentScope = false, backRef = true, None)
  }

}
