package com.dongjiaqiang.jvm.dsl.api.expression.block

import com.dongjiaqiang.jvm.dsl.api.`type`.{AnyType, DslType, LambdaType, TupleType}
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope

/**
 * <pre><code>
 * program{
 * def method()=Unit{
 * Int=>Int lambda = p=>{  // p=> { ... } => Lambda
 * return p*2;
 * }
 * lambda.apply(2);
 * }
 * }<pre><code>
 */
case class Lambda(inputs: Array[String], body: Block) extends ValueExpression {
  override def toString: String =
    s"""
       |(${inputs.mkString( "," )}=>$body
       |""".stripMargin

  override def equals(obj: Any): Boolean = {
    obj match {
      case lambda: Lambda ⇒
        lambda.inputs.sameElements( inputs ) && lambda.body == body
      case _ ⇒ false
    }
  }

  override def getValueType(programScope: ProgramScope): DslType = {
    val inputType = if (inputs.isEmpty) {
      None
    } else if (inputs.length == 1) {
      Some( AnyType )
    } else {
      Some( TupleType( inputs.map( _ ⇒ AnyType ) ) )
    }
    LambdaType( inputType, body.getValueType( programScope ) )
  }
}
