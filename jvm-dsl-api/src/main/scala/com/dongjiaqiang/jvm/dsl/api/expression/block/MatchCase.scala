package com.dongjiaqiang.jvm.dsl.api.expression.block

import com.dongjiaqiang.jvm.dsl.api.`type`.DslType
import com.dongjiaqiang.jvm.dsl.api.expression.`var`.VarRef
import com.dongjiaqiang.jvm.dsl.api.expression.expression.getString
import com.dongjiaqiang.jvm.dsl.api.expression.{Expression, ValueExpression}
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope

/**
 * <pre><code>
 * program{
 * def method(a:Any)=Int{
 * a match {               // a match { ... } => MatchCases
 * case i:Int=>{ ... }
 * case (i,j,k)=>{ ... }
 * case [e1,e2,e3]=>{ ... }
 * default=>{ ... }
 * }
 * }
 * }
 * <pre><code>
 */
case class MatchCase(matched: VarRef, cases: Array[(Expression, Block)], default: Option[Block]) extends ValueExpression {
  override def toString: String =
    s"""
       |$matched match {
       |     ${
      cases.map( `case` ⇒ {
        "case " + `case`._1.toString + " => " + `case`._2.toString
      } ).mkString( "\n" )
    }
       |  ${if (default.isDefined) "default =>" + default.get}
       |}
       |""".stripMargin

  override def equals(obj: Any): Boolean = obj match {
    case matchCase: MatchCase ⇒
      matchCase.matched == matched && matchCase.cases.sameElements( cases ) && matchCase.default == default
    case _ ⇒ false
  }

  override def getValueType(programScope: ProgramScope): DslType = {
    val dslType = cases.map( _._2.getValueType( programScope ) )
      .reduce( (t1, t2) ⇒ t1.commonDslType( programScope.importManager, t2 ) )
    if (default.isEmpty) {
      dslType
    } else {
      dslType.commonDslType( programScope.importManager, default.get.getValueType( programScope ) )
    }
  }
}

/**
 * <pre><code>
 * a match {
 * case Foo(d,c,b)=>{}
 * }
 * <pre><code>
 *
 * @see MatchCase
 */
case class MatchClass(dslType: DslType, expressions: Array[Either[Expression, String]]) extends Expression {
  override def toString: String = {
    val clazzName =dslType.toString

    s"$clazzName${getString( "(", ")", ",", expressions )}"
  }

  override def equals(obj: Any): Boolean = {
    obj match {
      case matchClass: MatchClass ⇒
        matchClass.dslType == dslType && matchClass.expressions.sameElements( expressions )
      case _ ⇒ false
    }
  }
}

/**
 * <pre><code>
 * a match {
 * case one:two:tail=>{}
 * case one:Nil=>{}
 * }
 * <pre><code>
 *
 * @see MatchCase
 */
case class MatchHead(head: Array[Either[Expression, String]], tail: Either[Expression, String]) extends Expression {
  override def toString: String = s"${getString( "", "", ":", head )}:${
    tail match {
      case Left( i ) ⇒ i.toString
      case Right( j ) ⇒ j
    }
  }"

  override def equals(obj: Any): Boolean = obj match {
    case matchHead: MatchHead ⇒ matchHead.head.sameElements( head ) && matchHead.tail == tail
    case _ => false
  }
}

/**
 * <pre><code>
 * a match {
 * case [1,2,b]=>{}
 * }
 * <pre><code>
 *
 * @see MatchCase
 */
case class MatchList(expressions: Array[Either[Expression, String]]) extends Expression {
  override def toString: String = getString( "[", "]", ",", expressions )

  override def equals(obj: Any): Boolean = obj match {
    case matchList: MatchList =>
      matchList.expressions.sameElements( expressions )
    case _ ⇒ false
  }
}

/**
 * <pre><code>
 * a match {
 * case (a1,a2,a3)=>{}
 * }
 * <pre><code>
 *
 * @see MatchCase
 */
case class MatchTuple(expressions: Array[Either[Expression, String]]) extends Expression {
  override def toString: String = getString( "(", ")", ",", expressions )

  override def equals(obj: Any): Boolean = obj match {
    case matchTuple: MatchTuple ⇒ matchTuple.expressions.sameElements( expressions )
    case _ ⇒ false
  }
}

/** <pre><code>
 * a match {
 * case d:Int=> {}
 * }
 * <pre><code>
 *
 * @see MatchCase
 */
case class MatchType(name: String, dslType: DslType) extends Expression {
  override def toString: String = s"$name:$dslType"

  override def equals(obj: Any): Boolean = {
    obj match {
      case matchType: MatchType ⇒
        matchType.name == name && matchType.dslType == dslType
      case _ ⇒ false
    }
  }
}
