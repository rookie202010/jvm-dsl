package com.dongjiaqiang.jvm.dsl.api.exception

/**
  *represents exceptions thrown during the symbol parser phase
 */
case class SymbolParseException(msg: String) extends Exception( msg )
