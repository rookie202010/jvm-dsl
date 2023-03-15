package com.dongjiaqiang.jvm.dsl.java.api.expression

import com.dongjiaqiang.jvm.dsl.api.`type`.TryType
import com.dongjiaqiang.jvm.dsl.api.expression.Expression
import com.dongjiaqiang.jvm.dsl.api.expression.block.Block

case class JavaTry(body:Block, returnType: TryType) extends Expression
