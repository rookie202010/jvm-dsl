package com.dongjiaqiang.jvm.dsl.java.http

import com.dongjiaqiang.jvm.dsl.api.expression.visitor.ExpressionVisitor
import com.dongjiaqiang.jvm.dsl.java.api.expression.{CustomBlockExpressionTranslator, JavaCustomBlockExpression, JavaTranslatorContext}

/**
 *<pre><code>
 *   the following syntax is supported in http block expression
 *   Http(httpClient){
 *        head=a;
 *        url = "http://abc.com";
 *        query={a:"xx",b:1};
 *        get();
 *   }
 *<pre><code>
 */
class HttpTranslator(override val javaTranslatorContext: JavaTranslatorContext)
  extends CustomBlockExpressionTranslator(javaTranslatorContext){

  override def name: String = "Http"

  override def translate(javaCustomBlockExpression: JavaCustomBlockExpression,
                         visitor: ExpressionVisitor[String]): String = {
    ""
  }
}
