package com.dongjiaqiang.jvm.dsl.api.`type`

import com.dongjiaqiang.jvm.dsl.api.exception.ExpressionParseException
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression

package object visitor {
  def require(requirement: Boolean, message: => Any): Unit = {
    if (!requirement)
      throw ExpressionParseException( "requirement failed: " + message )
  }

  def requireNoParams(name: String, array: Array[ValueExpression]): Unit = {
    require( array.isEmpty, s"method $name is a parameterless method" )
  }

  def requireCalleeType(name: String, calleeType: DslType, requireType: DslType*): Unit = {
    require( requireType.contains( calleeType ),
      s"method $name only accept these type [${requireType.mkString( "," )}] but is $calleeType" )
  }

  def requireParamType(name: String, paramName: String, paramType: DslType, requireType: DslType*): Unit = {
    require( requireType.contains( paramType ),
      s"method $name param $paramName only call by [${requireType.mkString( "," )}] but is $paramType" )
  }

  def errorMsg(methodName:String,calleeType:DslType,requestInputTypes:Array[String],actualTypes:Array[DslType]):String={
        s"""
          |method $methodName in type $calleeType
          |require types ${requestInputTypes.mkString(",")}
          |actual types ${actualTypes.mkString(",")}
          |""".stripMargin
  }

  def errorMsg(methodName: String, requestInputTypes: Array[String], actualTypes: Array[DslType]): String = {
    s"""
       |method $methodName in program
       |require types ${requestInputTypes.mkString( "," )}
       |actual types ${actualTypes.mkString( "," )}
       |""".stripMargin
  }
}
