package com.dongjiaqiang.jvm.dsl.core.config

import com.typesafe.config.Config

object Configs {

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

class SymbolParserConfig(val config:Config){
    def coverParentScopeDef():Boolean={
        if(config.hasPath(Configs.SYMBOL_PARSER_COVER_PARENT_SCOPE_DEF)){
            config.getBoolean(Configs.SYMBOL_PARSER_COVER_PARENT_SCOPE_DEF)
        }else{
            false
        }
    }
}
