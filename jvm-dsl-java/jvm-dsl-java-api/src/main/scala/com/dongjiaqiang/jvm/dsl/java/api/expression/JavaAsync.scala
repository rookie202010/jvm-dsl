package com.dongjiaqiang.jvm.dsl.java.api.expression

import com.dongjiaqiang.jvm.dsl.api.`type`.FutureType
import com.dongjiaqiang.jvm.dsl.api.expression.{ Block, Expression}
import com.dongjiaqiang.jvm.dsl.api.scope.FieldScope
/**
 * @author: rookie
 * @mail: dongjiaqiang@qiniu.com
 * @date: 2023/1/18 
 * */
case class JavaAsync(body:Block, executor: Option[FieldScope], returnType: FutureType) extends Expression
