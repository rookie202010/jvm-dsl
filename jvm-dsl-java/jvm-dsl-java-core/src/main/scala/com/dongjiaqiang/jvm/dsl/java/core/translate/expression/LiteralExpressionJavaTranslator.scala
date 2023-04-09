package com.dongjiaqiang.jvm.dsl.java.core.translate.expression

import com.dongjiaqiang.jvm.dsl.api.expression.`var`.Null
import com.dongjiaqiang.jvm.dsl.api.expression.literal._
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.ExpressionVisitor
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.literal.LiteralExpressionVisitor
import com.dongjiaqiang.jvm.dsl.java.api.codes.{_SYS_ARRAY_CODES, _SYS_GEN_CODES, _SYS_LIST_CODES, _SYS_MAP_CODES, _SYS_SET_CODES}
import com.dongjiaqiang.jvm.dsl.java.api.expression.JavaTranslatorContext

import java.util.Comparator

trait LiteralExpressionJavaTranslator extends LiteralExpressionVisitor[String] {

  val javaTranslatorContext: JavaTranslatorContext

  //literal
  override def visit(literal: IntLiteral, visitor: ExpressionVisitor[String]): String = literal.toString

  override def visit(literal: LongLiteral, visitor: ExpressionVisitor[String]): String = literal.toString

  override def visit(literal: DoubleLiteral, visitor: ExpressionVisitor[String]): String = literal.toString

  override def visit(literal: FloatLiteral, visitor: ExpressionVisitor[String]): String = literal.toString

  override def visit(literal: BoolLiteral, visitor: ExpressionVisitor[String]): String = literal.toString

  override def visit(literal: StringLiteral, visitor: ExpressionVisitor[String]): String = literal.literal

  override def visit(literal: CharLiteral, visitor: ExpressionVisitor[String]): String = literal.toString

  override def visit(literal: ListLiteral, visitor: ExpressionVisitor[String]): String = {
    val list = literal.literal
    s"${_SYS_LIST_CODES.CLAZZ_NAME}.ofList(${list.map( visitor.visit ).mkString( "," )})"
  }

  override def visit(literal: MapLiteral, visitor: ExpressionVisitor[String]): String = {
    val list = literal.literal
    val tuples = list.map {
      case (k, v) ⇒ s"new com.dongjiaqiang.jvm.dsl.java.api.tuple.Tuple2(${visitor.visit( k )},${visitor.visit( v )})"
    }.mkString( "," )

    if(literal.dslType.seq){
      s"${_SYS_MAP_CODES.CLAZZ_NAME}.ofSeqMap($tuples)"
    }else if(literal.dslType.sorted){
        literal.dslType.sorter match {
          case None⇒
            s"${_SYS_MAP_CODES.CLAZZ_NAME}.ofSortMap($tuples)"
          case Some(lambda)⇒
            val comparator = LambdaToAnonymousClassTranslator.translateComparator(visitor,javaTranslatorContext,lambda)
            s"${_SYS_MAP_CODES.CLAZZ_NAME}.ofSortMap($comparator,$tuples)"
        }
    }else{
      s"${_SYS_MAP_CODES.CLAZZ_NAME}.ofMap($tuples)"
    }
  }

  override def visit(literal: SetLiteral, visitor: ExpressionVisitor[String]): String = {
    val list = literal.literal
    val code  = list.map( visitor.visit ).mkString( "," )
    if(literal.dslType.seq){
        s"${_SYS_SET_CODES.CLAZZ_NAME}.ofSeqSet($code)"
    }else if(literal.dslType.sorted){
        literal.dslType.sorter match {
          case None⇒
            s"${_SYS_SET_CODES.CLAZZ_NAME}.ofSortedSet($code)"
          case Some(lambda)⇒
            val comparator = s"""
               |new ${classOf[Comparator[_]].getCanonicalName}<Object>(){
               |    @Override
               |    public int compare(Object ${lambda.inputs.head},Object ${lambda.inputs.last})
               |        ${visitor.visit(lambda)}
               |}
               |""".stripMargin
            s"${_SYS_SET_CODES.CLAZZ_NAME}.ofSortedSet($comparator,$code)"
        }
    }else{
      s"${_SYS_SET_CODES.CLAZZ_NAME}.ofSet($code)"
    }
  }

  override def visit(literal: TupleLiteral, visitor: ExpressionVisitor[String]): String = {
    val list = literal.literal
    val ps = literal.getValueType(visitor.programScope).parameterTypes.map(p⇒
      com.dongjiaqiang.jvm.dsl.java.api.toJavaType(p,javaTranslatorContext)
    ).mkString(",")

    s"new com.dongjiaqiang.jvm.dsl.java.api.tuple.Tuple${list.length}<$ps>(${list.map( visitor.visit ).mkString( "," )})"
  }

  override def visit(literal: ClazzLiteral, visitor: ExpressionVisitor[String]): String = {
    val list = literal.literal
    s"new ${literal.dslType.name}(${list.map( e ⇒ visitor.visit( e ) ).mkString( "," )})"
  }

  override def visit(literal: OptionLiteral, visitor: ExpressionVisitor[String]): String = {
    if (literal.literal.isInstanceOf[Null.type]) {
      s"java.util.Optional.Optional.empty()"
    } else {
      s"java.util.Optional.ofNullable(${visitor.visit( literal.literal )})"
    }
  }

  override def visit(literal: Null.type, visitor: ExpressionVisitor[String]): String = {
    "null"
  }

  override def visit(literal: ArrayLiteral, visitor: ExpressionVisitor[String]): String = {
    val list = literal.literal
    val params = list.map( visitor.visit ).mkString( "," )
    val carryDslType = literal.getValueType(visitor.programScope).carryDslType
    val typeName = com.dongjiaqiang.jvm.dsl.java.api.toJavaType(carryDslType,javaTranslatorContext)
    val newArray = s"new $typeName[${list.length}]"
    s"${_SYS_ARRAY_CODES.CLAZZ_NAME}.ofArray($newArray,$params)"
  }

  override def visit(literal: EitherLiteral, visitor: ExpressionVisitor[String]): String = {
    literal.literal match {
      case Left( left ) ⇒
        s"new com.dongjiaqiang.jvm.dsl.java.core.extend.Left<>(${visitor.visit( left )})"
      case Right( right ) ⇒
        s"new com.dongjiaqiang.jvm.dsl.java.core.extend.Right<>(${visitor.visit( right )})"
    }
  }
}
