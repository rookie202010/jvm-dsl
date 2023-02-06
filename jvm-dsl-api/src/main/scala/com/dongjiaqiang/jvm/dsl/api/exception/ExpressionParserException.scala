package com.dongjiaqiang.jvm.dsl.api.exception


/**
 * represents exceptions thrown during the expression parser phase
 */
case class ExpressionParserException(msg: String) extends Exception(msg)
