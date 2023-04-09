package com.dongjiaqiang.jvm.dsl.java.api.expression

import com.dongjiaqiang.jvm.dsl.api.`type`.DslType
import com.dongjiaqiang.jvm.dsl.java.api.`type`.JavaTypeTranslator
import com.typesafe.config.Config

import java.util.concurrent.atomic.AtomicInteger
import scala.collection.mutable.{ArrayBuffer, Map ⇒ MMap}
import com.dongjiaqiang.jvm.dsl.java.api.config._
/**
 * clazz methods generate by java translator
 *
 * clazzName -> methodName -> methodCode
 *
 * program methods generate by java translator
 *
 * methodName -> methodCode
 */
case class JavaTranslatorContext(customBlockExpressionJavaTranslators: MMap[String,CustomBlockExpressionTranslator]  = MMap(),
                                 customDslTypeTranslator:MMap[DslType,JavaTypeTranslator] = MMap() ,
                                 packageName: String,
                                 clazzName:String,
                                 javaTranslateConfig: Config,
                                 systemGenerateMethods: MMap[String, ArrayBuffer[String]] = MMap( ),
                                 systemGenerateProgramMethods: ArrayBuffer[String] = ArrayBuffer( ),
                                 systemVarIndex: AtomicInteger = new AtomicInteger( 0 )){

  def genericErasure():Boolean={
    true
  //  javaTranslateConfig.hasPath(JAVA_TRANSLATOR_GENERIC_ERASURE) &&
  //    javaTranslateConfig.getBoolean(JAVA_TRANSLATOR_GENERIC_ERASURE)
  }

  def lambdaGrammarEnable():Boolean={
      javaTranslateConfig.hasPath(JAVA_TRANSLATOR_CONFIG_LAMBDA_GRAMMAR_ENABLE) &&
        javaTranslateConfig.getBoolean(JAVA_TRANSLATOR_CONFIG_LAMBDA_GRAMMAR_ENABLE)
  }

}
