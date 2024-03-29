package com.dongjiaqiang.jvm.dsl.core.program

import com.dongjiaqiang.jvm.dsl.api.expression.Expression
import com.dongjiaqiang.jvm.dsl.api.expression.block.Block
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.ExpressionVisitor
import com.dongjiaqiang.jvm.dsl.api.scope.{ClazzScope, MethodScope, ProgramScope}
import com.dongjiaqiang.jvm.dsl.core.expression.visitor.{ExpressionReviser, ExpressionScanner}

import scala.collection.mutable.{LinkedHashMap ⇒ MutableMap}


case class Method(methodScope: MethodScope, body: Block)

case class Clazz(clazzScope: ClazzScope, methods: MutableMap[String, Method])

case class Program(programScope: ProgramScope,
                   assigned: MutableMap[String, Expression],
                   classes: MutableMap[String, Clazz],
                   methods: MutableMap[String, Method]) {
  def visit[K](expressionVisitor: ExpressionVisitor[K]): (MutableMap[String, K],
    MutableMap[String, K],
    MutableMap[String, MutableMap[String, K]]) = {
    val assignedResult = assigned.map {
      case (fieldName, expression) ⇒
        val result = expressionVisitor.visit( expression )
        (fieldName, result)
    }

    val methodResult = methods.map {
      case (methodName, method) ⇒
        expressionVisitor.setCurrentMethodScope( method.methodScope)
        val result = expressionVisitor.visit( method.body )
        (methodName, result)
    }

    val clazzResult = classes.map {
      case (clazzName, clazz) ⇒
        (clazzName, clazz.methods.map {
          case (methodName, method) ⇒
            expressionVisitor.setCurrentMethodScope(method.methodScope)
            expressionVisitor.currentClazzScope = clazz.clazzScope
            val result = expressionVisitor.visit( method.body )
            (methodName, result)
        })
    }
    (assignedResult, methodResult, clazzResult)
  }

  def scan(expressionScan: ExpressionScanner): Unit = {
    visit( expressionScan )
  }

  def revise(expressionReviser: ExpressionReviser): Program = {
    val (fieldAssigned, methodAssigned, clazzAssigned) = visit( expressionReviser )

    Program(
      programScope,

      fieldAssigned,

      classes.map {
        case (clazzName, clazz) ⇒
          val methods = clazzAssigned( clazzName )
          (clazzName, Clazz( clazz.clazzScope, clazz.methods.map {
            case (methodName, methodScope) ⇒
              (methodName, Method( methodScope.methodScope, methods( methodName ).asInstanceOf[Block] ))
          } ))
      },

      methods.map {
        case (methodName, method) ⇒
          val body = methodAssigned( methodName ).asInstanceOf[Block]
          (methodName, Method( method.methodScope, body ))
      } )
  }
}

