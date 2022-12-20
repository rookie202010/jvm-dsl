package com.dongjiaqiang.jvm.dsl.core.program

import com.dongjiaqiang.jvm.dsl.core.expression.Expression
import com.dongjiaqiang.jvm.dsl.core.scope.{ClazzScope, MethodScope, ProgramScope}

import scala.collection.mutable.{ArrayBuffer, ListMap ⇒ MutableMap}

case class Method(methodScope: MethodScope, expression: ArrayBuffer[Expression])

case class Clazz(clazzScope: ClazzScope, methods: MutableMap[String, Method])

case class Program(programScope: ProgramScope, classes: MutableMap[String, Clazz], methods: MutableMap[String, Method])

