package com.dongjiaqiang.jvm.dsl.api.exception


/**
 * represents exceptions thrown during the expression parser phase
 */
case class ExpressionParseException(msg: String) extends Exception(msg)
