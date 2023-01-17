package com.dongjiaqiang.jvm.dsl.api.scope

import com.dongjiaqiang.jvm.dsl.api.scope

import scala.collection.mutable.{ListMap ⇒ MutableMap}

class ForStatementBlockScope(override val outerScopeIndex: Int, val initFields: MutableMap[String, FieldScope], override val parentScope: Scope, override val topScope: Scope)
  extends BlockScope( outerScopeIndex, parentScope, topScope ) {


  override def getSymbolType(symbolName: String): scope.SymbolType.Value = {
    if (initFields.contains(symbolName)) {
        SymbolType.FIELD
    } else {
      super.getSymbolType( symbolName )
    }
  }


  def addInitScope(symbolName:String,fieldScope: FieldScope):Unit={
    duplicateSymbol(symbolName)
    initFields.put(symbolName,fieldScope)
  }

  override def equals(obj: Any): scala.Boolean =
    obj match {
      case forStatementBlockScope: ForStatementBlockScope⇒
          initFields.sameElements(forStatementBlockScope.initFields) && super.equals(obj) &&
            outerScopeIndex == forStatementBlockScope.outerScopeIndex &&
            statements == forStatementBlockScope.statements
      case _⇒false
    }

  /*
   *
   *
   * program{
   *    def method()=Unit{
   *        Int i = 100;
   *        for(Int i=0;i<100;i++){
   *            Int j = i;
   *        }
   *    }
   * }
   * resolve ref in current or outer scope
   *
   * @param index ref index
   * @param refs  ref names
   */
  override def resolveVarRefs(index: Int, refs: List[String]): Option[FieldScope] = {
    refs match {
      case "this"::childRef ⇒ scope.resolveVarRefs(index,childRef,this,fields,skipCurrentScope = true,backRef = true,Some(topScope))
      case _ ⇒
        val totalFields = MutableMap[String, FieldScope]()
        totalFields++=fields
        totalFields++=initFields
        scope.resolveVarRefs(index,refs,this,totalFields,skipCurrentScope = false,backRef = false,Some(parentScope))

    }
  }
}
