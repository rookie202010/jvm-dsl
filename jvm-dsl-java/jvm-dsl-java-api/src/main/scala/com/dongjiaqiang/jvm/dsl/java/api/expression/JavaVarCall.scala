package com.dongjiaqiang.jvm.dsl.java.api.expression

import com.dongjiaqiang.jvm.dsl.api.expression.Expression

case class JavaVarCall(varRef: List[String], name: String, params: Array[Expression]) extends Expression
