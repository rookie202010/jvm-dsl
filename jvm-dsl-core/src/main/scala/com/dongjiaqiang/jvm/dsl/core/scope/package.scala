package com.dongjiaqiang.jvm.dsl.core

import scala.collection.mutable.{Map⇒MMap}

package object scope {

  object SymbolType extends Enumeration {
    val FIELD, CLAZZ, METHOD, UNDEFINED = Value
  }

}
