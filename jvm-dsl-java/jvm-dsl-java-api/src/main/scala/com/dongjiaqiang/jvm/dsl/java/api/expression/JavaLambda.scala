package com.dongjiaqiang.jvm.dsl.java.api.expression

import com.dongjiaqiang.jvm.dsl.api.`type`.LambdaType
import com.dongjiaqiang.jvm.dsl.api.expression.{Expression, Lambda}

case class JavaLambda(dslType: LambdaType, lambda: Lambda) extends Expression
