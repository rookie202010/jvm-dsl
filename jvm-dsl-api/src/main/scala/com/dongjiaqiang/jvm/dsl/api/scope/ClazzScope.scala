package com.dongjiaqiang.jvm.dsl.api.scope

import com.dongjiaqiang.jvm.dsl.api.scope

import scala.collection.mutable.{LinkedHashMap ⇒ MutableMap}

class ClazzScope(val outerScopeIndex: Int, val name: String,
                 val fields: MutableMap[String, FieldScope],
                 val methods: MutableMap[String, MethodScope]) extends Scope {

  override def toString: String = {
    val methodStr = if(methods.isEmpty) "" else "methods:\n  "+methods.toList.map(_._2.toString).mkString("\n")
    val fieldStr = if(fields.isEmpty) "" else "fields:\n  "+fields.toList.map(_._2.toString).mkString("\n")
    s"""
        ClazzScope:
        outerScopeIndex = $outerScopeIndex
        clazzName = $name
        $fieldStr
        $methodStr
    """
  }

  def this(outScopeIndex: Int, name: String) {
    this( outScopeIndex, name, MutableMap( ), MutableMap( ) )
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
   *<pre><code>
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
   *<pre><code>
   * resolve ref in current or outer scope
   *
   * @param index ref index
   * @param refs   ref names
   */
  override def resolveVarRefs(index: Int, refs: List[String], arrayRefsIndex:Set[Int]): Option[FieldScope] = {
      scope.resolveVarRefs(index, refs,arrayRefsIndex,this, fields, skipCurrentScope = false, backRef = true, None)
  }

  override def resolveMethod(name: String): Option[MethodScope] = { methods.get(name)}

}
