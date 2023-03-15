package com.dongjiaqiang.jvm.dsl.java.api.expression

import com.dongjiaqiang.jvm.dsl.api.`type`.DslType
import com.dongjiaqiang.jvm.dsl.api.expression.Expression
import com.dongjiaqiang.jvm.dsl.api.expression.block.CustomBlockExpression

case class JavaCustomBlockExpression(customBlockExpression:CustomBlockExpression, returnType: DslType) extends Expression
