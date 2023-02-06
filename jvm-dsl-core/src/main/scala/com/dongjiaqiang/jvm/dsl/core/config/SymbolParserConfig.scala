package com.dongjiaqiang.jvm.dsl.core.config

import com.dongjiaqiang.jvm.dsl.core.config.SymbolParserConfig.SYMBOL_PARSER_COVER_PARENT_SCOPE_DEF
import com.typesafe.config.Config

object SymbolParserConfig {

  /**
   * for example
   * {
   *  Int i; {
   *    Int i // in java is not ok but in scala is ok
   *
   *  }
   *
   * }
   */
  val SYMBOL_PARSER_COVER_PARENT_SCOPE_DEF = "symbol.parser.cover.parent.scope.def"

}

case class SymbolParserConfig(val config:Config){
    def coverParentScopeDef():Boolean={
        if(config.hasPath(SYMBOL_PARSER_COVER_PARENT_SCOPE_DEF)){
            config.getBoolean(SYMBOL_PARSER_COVER_PARENT_SCOPE_DEF)
        }else{
            false
        }
    }
}
