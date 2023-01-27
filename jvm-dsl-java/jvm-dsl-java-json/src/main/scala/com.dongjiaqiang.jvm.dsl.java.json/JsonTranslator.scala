package com.dongjiaqiang.jvm.dsl.java.json

import com.dongjiaqiang.jvm.dsl.api.`type`.{MapType, UnResolvedType}
import com.dongjiaqiang.jvm.dsl.api.expression.{Assign, BoolLiteral, CharLiteral, ClazzLiteral, DoubleLiteral, Expression, FloatLiteral, IntLiteral, ListLiteral, LongLiteral, MapLiteral, StringLiteral, VarRef}
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.ExpressionVisitor
import com.dongjiaqiang.jvm.dsl.java.api
import com.dongjiaqiang.jvm.dsl.java.api.exception.JavaTranslatorException
import com.dongjiaqiang.jvm.dsl.java.api.expression.{CustomBlockExpressionTranslator, JavaCustomBlockExpression, JavaTranslatorContext}


/**
 * <pre><code>
 * the following syntax is supported in json block expression
 *
 * 1. convert map to json object
 * Json{
 *    {1:"aa",2:"bb",3:"cc"}
 * }
 *
 * 2. convert list to json list
 * Json{
 *    [1,2,3,4,5]
 * }
 *
 * 3. definition field in block then convert to json object
 * Json{
 *    body.v="abc";
 *    size=10;
 * }
 * <pre><code>
 */
class JsonTranslator(override val javaTranslatorContext: JavaTranslatorContext)
  extends CustomBlockExpressionTranslator(javaTranslatorContext){


  override def name: String = "Json"

  def translate(expression: Expression):String={
    expression match {
      case intLiteral: IntLiteral⇒intLiteral.toString
      case longLiteral: LongLiteral⇒s"${longLiteral.toString}L"
      case doubleLiteral: DoubleLiteral⇒s"${doubleLiteral.toString}d"
      case floatLiteral: FloatLiteral⇒s"${floatLiteral.toString}f"
      case boolLiteral: BoolLiteral⇒boolLiteral.toString
      case stringLiteral: StringLiteral⇒stringLiteral.literal
      case charLiteral: CharLiteral⇒charLiteral.toString
      case clazzLiteral: ClazzLiteral⇒
          val literal = clazzLiteral.literal
          val clazzType = clazzLiteral.dslType
          val params  = literal.map(translate).mkString(",")
          s"new ${api.toJavaType(clazzType,javaTranslatorContext)}($params)"
      case varRef: VarRef⇒
          varRef.name.mkString(".")
      case listLiteral: ListLiteral⇒
          val literal = listLiteral.literal
          val params = literal.map(e⇒s"fluentAdd(${translate(e)})").mkString(".")
          s"new JSONArray().$params"
      case mapLiteral: MapLiteral⇒
          val literal = mapLiteral.literal
          val params = literal.map(e⇒s"fluentAdd(${translate(e._1)},${translate(e._2)})").mkString(".")
          s"new JSONObject().$params"
      case _⇒
          throw JavaTranslatorException(s"definite single expression in json block not accept $expression")
    }
  }

  override def translate(javaCustomBlockExpression: JavaCustomBlockExpression,
                         visitor: ExpressionVisitor[String]): String = {
    val returnType = javaCustomBlockExpression.returnType
    val body = javaCustomBlockExpression.customBlockExpression.body

    returnType match {
      case _: JsonArrayType⇒
        if(body.expressions.length==1){
            translate(body.expressions.head)
        }else{
            if(body.expressions.exists(! _.isInstanceOf[Assign ])){
              throw JavaTranslatorException("definite multi expression in json block only accept assign")
            }else{
              translate(new MapLiteral(body.expressions.map(_.asInstanceOf[Assign])
                .map(assign⇒{
                  (assign.varRef,assign.assigned)
                }).toArray,new MapType(UnResolvedType,UnResolvedType)))
            }
        }
      case _⇒
        throw JavaTranslatorException("json block only accept json type")
    }
  }

}
