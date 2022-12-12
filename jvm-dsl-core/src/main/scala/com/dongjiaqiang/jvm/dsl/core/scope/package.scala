package com.dongjiaqiang.jvm.dsl.core

import com.dongjiaqiang.jvm.dsl.core.symbol.BlockScopeIndex

import scala.collection.mutable.{Map ⇒ MMap}

package object scope {

  object SymbolType extends Enumeration {
    val FIELD, CLAZZ, METHOD, UNDEFINED = Value
  }
}
