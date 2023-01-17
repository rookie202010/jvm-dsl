package com.dongjiaqiang.jvm.dsl.java.api.expression

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
case class JavaTranslatorContext(packageName: String,
                                 javaTranslateConfig: Config,
                                 systemGenerateMethods: MMap[String, ArrayBuffer[String]] = MMap( ),
                                 systemGenerateProgramMethods: ArrayBuffer[String] = ArrayBuffer( ),
                                 systemVarIndex: AtomicInteger = new AtomicInteger( 0 ))
