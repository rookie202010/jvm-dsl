package com.dongjiaqiang.jvm.dsl.core.scope
import com.dongjiaqiang.jvm.dsl.core.`type`.DslType
import com.dongjiaqiang.jvm.dsl.core.scope

class FieldScope(val outerScopeIndex:Int, val symbolName:String, val dslType: DslType, val volatile:Boolean = false) extends Scope {

  override def getSymbolType(symbolName: String): scope.SymbolType.Value = {
      if(symbolName==this.symbolName){
          SymbolType.FIELD
      }else{
          SymbolType.UNDEFINED
      }
  }

  override def equals(obj: Any): scala.Boolean =
      obj match {
        case fieldScope: FieldScope⇒
            symbolName == fieldScope.symbolName &&
            dslType == fieldScope.dslType &&
            volatile == fieldScope.volatile &&
              outerScopeIndex == fieldScope.outerScopeIndex &&
              statements == fieldScope.statements
        case _⇒false
      }

}
