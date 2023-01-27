package com.dongjiaqiang.jvm.dsl.java.json

import com.alibaba.fastjson2.JSONObject
import com.dongjiaqiang.jvm.dsl.api.`type`.DslType
import com.dongjiaqiang.jvm.dsl.java.api.`type`.JavaTypeTranslator

class JavaObjectType extends DslType with JavaTypeTranslator{
  override val name: String = "JavaObject"

  override def translate(): String = classOf[JSONObject].getCanonicalName
}
