package com.dongjiaqiang.jvm.dsl.core.scope
import com.dongjiaqiang.jvm.dsl.core.`type`.DslType
import com.dongjiaqiang.jvm.dsl.core.scope

class FieldScope(val index:Int,val symbolName:String, val dslType: DslType, val volatile:Boolean = false) extends Scope {

  override val size: Int = 1

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
            volatile == fieldScope.volatile
        case _⇒false
      }

}
