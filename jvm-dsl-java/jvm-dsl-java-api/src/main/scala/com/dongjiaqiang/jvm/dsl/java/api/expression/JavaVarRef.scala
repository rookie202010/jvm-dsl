package com.dongjiaqiang.jvm.dsl.java.api.expression

import com.dongjiaqiang.jvm.dsl.api.expression.Expression

case class JavaVarRef(name: List[String]) extends Expression
