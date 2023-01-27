package com.dongjiaqiang.jvm.dsl.java.api.expression

import com.dongjiaqiang.jvm.dsl.api.`type`.DslType
import com.dongjiaqiang.jvm.dsl.api.expression.{CustomBlockExpression, Expression}

/**
 * @author: rookie
 * @mail: dongjiaqiang@qiniu.com
 * @date: 2023/1/18 
 * */
case class JavaCustomBlockExpression(customBlockExpression:CustomBlockExpression, returnType: DslType) extends Expression
