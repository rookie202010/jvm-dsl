package com.dongjiaqiang.jvm.dsl.java.api.expression

import com.dongjiaqiang.jvm.dsl.api.`type`.FutureType
import com.dongjiaqiang.jvm.dsl.api.expression.Expression
import com.dongjiaqiang.jvm.dsl.api.expression.block.Block
import com.dongjiaqiang.jvm.dsl.api.scope.FieldScope

case class JavaAsync(body:Block, executor: Option[FieldScope], returnType: FutureType) extends Expression
