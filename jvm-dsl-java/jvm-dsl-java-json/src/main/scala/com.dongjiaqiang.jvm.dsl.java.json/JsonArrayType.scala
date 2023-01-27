package com.dongjiaqiang.jvm.dsl.java.json

import com.alibaba.fastjson2.JSONArray
import com.dongjiaqiang.jvm.dsl.api.`type`.DslType
import com.dongjiaqiang.jvm.dsl.java.api.`type`.JavaTypeTranslator

/**
 * @author: rookie
 * @mail: dongjiaqiang@qiniu.com
 * @date: 2023/1/18 
 * */
class JsonArrayType extends DslType with JavaTypeTranslator{
  override val name: String = "JsonArray"

  override def translate(): String = classOf[JSONArray].getCanonicalName
}

