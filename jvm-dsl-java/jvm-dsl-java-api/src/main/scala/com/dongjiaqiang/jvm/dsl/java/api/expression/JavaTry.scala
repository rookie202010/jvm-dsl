package com.dongjiaqiang.jvm.dsl.java.api.expression

import com.dongjiaqiang.jvm.dsl.api.`type`.TryType
import com.dongjiaqiang.jvm.dsl.api.expression.{Block, Expression, Try}

/**
 * @author: rookie
 * @mail: dongjiaqiang@qiniu.com
 * @date: 2023/1/18 
 * */
case class JavaTry(body:Block, returnType: TryType) extends Expression
