package com.dongjiaqiang.jvm.dsl.api.`type`

import com.dongjiaqiang.jvm.dsl.api.exception.ExpressionParseException
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression

package object visitor {
//  def require(requirement: Boolean, message: => Any): Unit = {
//    if (!requirement)
//      throw ExpressionParseException( "requirement failed: " + message )
//  }

  def requireMsg(methodName:String, calleeType:DslType, requestInputTypes:Array[DslType], actualTypes:Array[DslType]):String={
        s"""
          |method $methodName in type $calleeType
          |require types ${requestInputTypes.mkString(",")}
          |actual types ${actualTypes.mkString(",")}
          |""".stripMargin
  }

  def requireMsg(methodName: String, requestInputTypes: Array[DslType], actualTypes: Array[DslType]): String = {
    s"""
       |method $methodName in program
       |require types ${requestInputTypes.mkString( "," )}
       |actual types ${actualTypes.mkString( "," )}
       |""".stripMargin
  }

//  def errorMsg(methodName: String, calleeType:DslType,requestInputTypes: Array[Array[String]], actualTypes: Array[DslType]): String = {
//    s"""
//       |method $methodName in type $calleeType
//       |require types ${requestInputTypes.mkString( "," )}
//       |actual types ${actualTypes.mkString( "," )}
//       |""".stripMargin
//  }
}
