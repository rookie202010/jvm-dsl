package com.dongjiaqiang.jvm.dsl.core.program

import com.dongjiaqiang.jvm.dsl.core.expression.{Block, Expression}
import com.dongjiaqiang.jvm.dsl.core.scope.{ClazzScope, MethodScope, ProgramScope}

import scala.collection.mutable.{ListMap ⇒ MutableMap}

case class Method(methodScope: MethodScope, body: Block)

case class Clazz(clazzScope: ClazzScope, methods: MutableMap[String, Method])

case class Program(programScope: ProgramScope,
                   assigned: MutableMap[String, Expression],
                   classes: MutableMap[String, Clazz],
                   methods: MutableMap[String, Method])

