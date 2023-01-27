package com.dongjiaqiang.jvm.dsl.java.api.expression

import com.dongjiaqiang.jvm.dsl.api.`type`.DslType
import com.dongjiaqiang.jvm.dsl.java.api.`type`.JavaTypeTranslator
import com.typesafe.config.Config

import java.util.concurrent.atomic.AtomicInteger
import scala.collection.mutable.{ArrayBuffer, Map ⇒ MMap}

/**
 * clazz methods generate by java translator
 *
 * clazzName -> methodName -> methodCode
 *
 * program methods generate by java translator
 *
 * methodName -> methodCode
 */
case class JavaTranslatorContext(customBlockExpressionJavaTranslators: Map[String,CustomBlockExpressionTranslator],
                                 customDslTypeTranslator:Map[DslType,JavaTypeTranslator],
                                 packageName: String,
                                 javaTranslateConfig: Config,
                                 systemGenerateMethods: MMap[String, ArrayBuffer[String]] = MMap( ),
                                 systemGenerateProgramMethods: ArrayBuffer[String] = ArrayBuffer( ),
                                 systemVarIndex: AtomicInteger = new AtomicInteger( 0 ))
