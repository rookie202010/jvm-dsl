package com.dongjiaqiang.jvm.dsl.java.api.enhance

import com.dongjiaqiang.jvm.dsl.api.expression.visitor.ExpressionVisitor
import com.dongjiaqiang.jvm.dsl.api.expression.{Expression, Lambda}

import scala.collection.mutable.{Map ⇒ MMap}


object DslTypeEnhance {

  val transformMethods: Set[String] = Set( "to", "until", "map", "flatMap", "filter", "filterNot", "flatten","zip","zipWithIndex" )

  val actionMethods: Set[String] = Set("reduce","mkString","head","headOption","toList","toSet","toMap","distinct")

  val toMethod: Method = Method( "to", (called, expressions, visitor) ⇒ {
    expressions.length match {
      case 1 ⇒
        s"com.dongjiaqiang.jvm.dsl.java.api.util.to(${visitor.visit( called )},${visitor.visit( expressions.head )})"
      case 2 ⇒
        s"com.dongjiaqiang.jvm.dsl.java.api.util.to(${visitor.visit( called )},${visitor.visit( expressions.head )},${visitor.visit( expressions.last )})"
    }
  } )

  val untilMethod: Method = Method( "until", (called, expressions, visitor) ⇒ {

    expressions.length match {
      case 1 ⇒
        s"com.dongjiaqiang.jvm.dsl.java.api.util.until(${visitor.visit( called )},${visitor.visit( expressions.head )})"
      case 2 ⇒
        s"com.dongjiaqiang.jvm.dsl.java.api.util.until(${visitor.visit( called )},${visitor.visit( expressions.head )},${visitor.visit( expressions.last )})"
    }
  } )

  val toStringMethod:Method=Method("toString",(called,_,visitor)⇒{
    s"String.valueOf(${visitor.visit(called)})"
  })
}



case class Method(name:String,
                  body:(Expression,Array[Expression],ExpressionVisitor[String])⇒String)
trait DslTypeEnhance{

  val methods:MMap[String,Method] = MMap()

  def addMethod(method: Method):Unit = methods.put(method.name,method)

  def findMethod(name:String):Method = methods(name)
}

trait NumberDslTypeEnhance extends DslTypeEnhance{

  override val methods: MMap[String,Method] = MMap[String,Method](
    "toInt"→Method( "toInt", (called, _, visitor) ⇒ {
      s"""
         |((long)(${visitor.visit( called )}))
         |""".stripMargin
    } ),
    "toLong"→Method( "toLong", (called, _, visitor) ⇒ {
      s"""
         |((long)(${visitor.visit( called )}))
         |""".stripMargin
    } ),
    "toFloat"→Method( "toFloat", (called, _, visitor) ⇒ {
      s"""
         |((float)(${visitor.visit( called )}))
         |""".stripMargin
    } ),
    "toDouble"→Method( "toDouble", (called, _, visitor) ⇒ {
      s"""
         |((double)(${visitor.visit( called )}))
         |""".stripMargin
    } )
  )

}


object ListType extends DslTypeEnhance{



}


object IntTypeEnhance extends NumberDslTypeEnhance{

  {
    methods.put( DslTypeEnhance.toMethod.name, DslTypeEnhance.toMethod )
    methods.put( DslTypeEnhance.untilMethod.name, DslTypeEnhance.untilMethod )
    methods.put( DslTypeEnhance.toStringMethod.name, DslTypeEnhance.toStringMethod )
  }

}

object LongTypeEnhance extends NumberDslTypeEnhance{

  {
    methods.put( DslTypeEnhance.toStringMethod.name, DslTypeEnhance.toStringMethod )
    methods.put( DslTypeEnhance.toMethod.name, DslTypeEnhance.toMethod )
    methods.put( DslTypeEnhance.untilMethod.name, DslTypeEnhance.untilMethod )
  }
}

object FloatTypeEnhance extends NumberDslTypeEnhance{

  {
    methods.put(DslTypeEnhance.toStringMethod.name,DslTypeEnhance.toStringMethod)
  }
}

object DoubleTypeEnhance extends NumberDslTypeEnhance{

  {
    methods.put(DslTypeEnhance.toStringMethod.name,DslTypeEnhance.toStringMethod)
  }
}

object BoolTypeEnhance extends DslTypeEnhance{

  {
    methods.put(DslTypeEnhance.toStringMethod.name,DslTypeEnhance.toStringMethod)
  }
}


object StringTypeEnhance extends DslTypeEnhance{

  override val methods: MMap[String,Method] = MMap(
    "toInt"→Method( "toInt", (called, _, visitor) ⇒ {
      s"""
         |Integer.parseInt(${visitor.visit( called )})
         |""".stripMargin
    } ),
    "toLong"→Method( "toLong",  (called, _, visitor) ⇒ {
      s"""
         |Long.parseLong(${visitor.visit( called )})
         |""".stripMargin
    } ),
    "toFloat"→Method( "toFloat", (called, _, visitor) ⇒ {
      s"""
         |Float.parseFloat(${visitor.visit( called )})
         |""".stripMargin
    } ),
    "toDouble"→Method( "toDouble", (called, _, visitor) ⇒ {
      s"""
         |Double.parseDouble(${visitor.visit( called )})
         |""".stripMargin
    } ),
    "mkString"→Method("mkString",(called,expressions,visitor)⇒{
      s"""
         |com.dongjiaqiang.jvm.dsl.java.api.util.mkString(${visitor.visit(called)},${visitor.visit(expressions.head)})
         |""".stripMargin
    }),
    "head"→Method("head",(called,_,visitor)⇒{
      s"""
         |${visitor.visit(called)}.charAt(0)
         |""".stripMargin
    }),
    "headOption"→Method("headOption",(called,_,visitor)⇒{
      s"""
         |com.dongjiaqiang.jvm.dsl.java.api.util.headOption(${visitor.visit(called)})
         |""".stripMargin
    }),
    "last"→Method("last",(called,_,visitor)⇒{
      s"""
         |com.dongjiaqiang.jvm.dsl.java.api.util.last(${visitor.visit(called)})
         |""".stripMargin
    }),
    "lastOption"→Method("lastOption",(called, _, visitor) ⇒ {
      s"""
         |com.dongjiaqiang.jvm.dsl.java.api.util.lastOption(${visitor.visit( called )})
         |""".stripMargin
    } ),
    "nonEmpty"→Method("nonEmpty",(called,_,visitor)⇒{
      s"""
         |!${visitor.visit(called)}.isEmpty()
         |""".stripMargin
    }),
    "tail"→Method("tail",(called,_,visitor)⇒{
      s"""
         |${visitor.visit(called)}.substring(1)
         |""".stripMargin
    }),
    "take"→Method("take",(called,expressions,visitor)⇒{
      s"""
         |${visitor.visit(called)}.substring(0,${visitor.visit(expressions.head)})
         |""".stripMargin
    }),
    "drop"→Method("drop",(called,expressions,visitor)⇒{
      s"""
         |${visitor.visit(called)}.substring(${visitor.visit(expressions.head)})
         |""".stripMargin
    }),
    "size"→Method("size",(called,_,visitor)⇒{
      s"""
         |${visitor.visit(called)}.length
         |""".stripMargin
    }),
    "dropWhile"→Method("dropWhile",(called, expressions, visitor) ⇒ {
      val lambda = expressions.head.asInstanceOf[Lambda]
      s"""
         |com.dongjiaqiang.jvm.dsl.java.api.util.dropWhile(${visitor.visit( called )},new _1_Predicate<Character>() {
         |            @Override
         |            public boolean test(Character ${lambda.inputs.head}) throws Exception
         |                ${visitor.visit(lambda)}
         |        })
         |""".stripMargin
    } ),
    "takeWhile"→Method( "takeWhile", (called, expressions, visitor) ⇒ {
      val lambda = expressions.head.asInstanceOf[Lambda]
      s"""
         |com.dongjiaqiang.jvm.dsl.java.api.util.takeWhile(${visitor.visit( called )},new _1_Predicate<Character>() {
         |            @Override
         |            public boolean test(Character ${lambda.inputs.head}) throws Exception
         |                ${visitor.visit( lambda )}
         |        })
         |""".stripMargin
    } )
  )
}
