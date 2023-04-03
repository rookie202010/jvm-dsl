package com.dongjiaqiang.jvm.dsl.api.expression.block

import com.dongjiaqiang.jvm.dsl.api.`type`.{DslType, UnitType}
import com.dongjiaqiang.jvm.dsl.api.expression.statement.Return
import com.dongjiaqiang.jvm.dsl.api.expression._
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope

import scala.collection.mutable.ArrayBuffer

/**
 * <pre><code>
 *program{
 *      def method(Int i)=Unit{
 *         {
 *              Int j = 10; // {..} => Block
 *         }
 *      }
 * }
 * <pre><code>
 */
case class Block(expressions: ArrayBuffer[Expression] = ArrayBuffer( ),var ignoreVarRefResolved:Boolean = false) extends ValueExpression {
    override def toString: String =
        s"""
           |{
           |     ${expressions.map( e ⇒ e + ";" ).mkString( "\n" )}
           |}
           |""".stripMargin

    override def equals(obj: Any): Boolean = obj match {
        case block: Block ⇒
            block.expressions == expressions && block.ignoreVarRefResolved == ignoreVarRefResolved
        case _ ⇒ false
    }

    override def getValueType(programScope: ProgramScope): DslType = {
        val returnTypes = expressions.flatMap {
            case For( _, _, _, body ) ⇒ Array(body.getValueType( programScope ))
            case ForCollection( _, _, body ) ⇒ Array(body.getValueType( programScope ))
            case ForMap(_, _, _, body) ⇒ Array(body.getValueType(programScope))
            case While(_,body) ⇒ Array(body.getValueType(programScope))
            case DoWhile(_,body)⇒ Array(body.getValueType(programScope))
            case Sync(_, body) ⇒ Array(body.getValueType(programScope))
            case If(cases, default) ⇒
                val types = cases.map(_._2).map(_.getValueType(programScope))
                default.foreach(body⇒types:+body.getValueType(programScope))
                types
            case TryCatch(tryBlock, catches, finallyBlock)⇒
                val types = catches.map(_._2).map(_.getValueType(programScope)):+tryBlock.getValueType(programScope)
                finallyBlock.foreach(body⇒types:+body.getValueType(programScope))
                types
            case Return(expression)⇒ expression.map(_.getValueType(programScope)) match {
                case Some(dslType)⇒Array(dslType)
                case None⇒Array[DslType]()
            }
            case _ ⇒ Array[DslType]()
        }
        returnTypes.filter(_!=UnitType).reduceOption[DslType]{
            case (t1,t2)⇒t1.commonDslType(programScope.importManager,t2)
        }.getOrElse(UnitType)
    }
}
