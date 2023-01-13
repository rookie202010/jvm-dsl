package com.dongjiaqiang.jvm.dsl.java.core.translate

import com.dongjiaqiang.jvm.dsl.core.`type`.NumberDslType
import com.dongjiaqiang.jvm.dsl.core.expression.visitor.ExpressionVisitor
import com.dongjiaqiang.jvm.dsl.core.expression._
import com.dongjiaqiang.jvm.dsl.core.scope.ProgramScope
import com.dongjiaqiang.jvm.dsl.java.core

class JavaTranslator(override val programScope: ProgramScope) extends ExpressionVisitor[String] {

  //literal
  override def visit(literal: IntLiteral, visitor: ExpressionVisitor[String]): String = literal.toString

  override def visit(literal: LongLiteral, visitor: ExpressionVisitor[String]): String = literal.toString

  override def visit(literal: DoubleLiteral, visitor: ExpressionVisitor[String]): String = literal.toString

  override def visit(literal: FloatLiteral, visitor: ExpressionVisitor[String]): String = literal.toString

  override def visit(literal: BoolLiteral, visitor: ExpressionVisitor[String]): String = literal.toString

  override def visit(literal: StringLiteral, visitor: ExpressionVisitor[String]): String = literal.toString

  override def visit(literal: CharLiteral, visitor: ExpressionVisitor[String]): String = literal.toString

  override def visit(literal: ListLiteral, visitor: ExpressionVisitor[String]): String = {
    val list = literal.literal
    s"com.dongjiaqiang.jvm.dsl.java.core.ofList(${list.map( visitor.visit ).mkString( "," )})"
  }

  override def visit(literal: MapLiteral, visitor: ExpressionVisitor[String]): String = {
    val list = literal.literal
    val tuples = list.map {
      case (k, v) ⇒ s"new com.dongjiaqiang.jvm.dsl.java.core.tuple.Tuple2(${visitor.visit( k )},${visitor.visit( v )})"
    }.mkString( "," )
    s"com.dongjiaqiang.jvm.dsl.java.core.ofMap($tuples)"
  }

  override def visit(literal: SetLiteral, visitor: ExpressionVisitor[String]): String = {
    val list = literal.literal
    s"com.dongjiaqiang.jvm.dsl.java.core.ofSet(${list.map( visitor.visit ).mkString( "," )})"
  }

  override def visit(literal: TupleLiteral, visitor: ExpressionVisitor[String]): String = {
    val list = literal.literal
    s"new com.dongjiaqiang.jvm.dsl.java.core.tuple.Tuple${list.length}<>(${list.map( visitor.visit ).mkString( "," )})"
  }

  override def visit(literal: ClazzLiteral, visitor: ExpressionVisitor[String]): String = {
    val list = literal.literal
    s"new {literal.dslType.clazzName}(${list.map( e ⇒ visitor.visit( e ) ).mkString( "," )})"
  }

  override def visit(literal: OptionLiteral, visitor: ExpressionVisitor[String]): String = {
    s"java.util.Optional.ofNullable(${visitor.visit( literal.literal )})"
  }


  override def visit(literal: ArrayLiteral, visitor: ExpressionVisitor[String]): String = {
    val list = literal.literal
    val params = list.map( visitor.visit ).mkString( "," )
    if (list.forall( _.isInstanceOf[IntLiteral] )) {
      s"new int[]{$params}"
    } else if (list.forall( _.isInstanceOf[FloatLiteral] )) {
      s"new float[]{$params}"
    } else if (list.forall( _.isInstanceOf[DoubleLiteral] )) {
      s"new double[]{$params}"
    } else if (list.forall( _.isInstanceOf[LongLiteral] )) {
      s"new long[]{$params}"
    } else if (list.forall( _.isInstanceOf[BoolLiteral] )) {
      s"new boolean[]{$params}"
    } else if (list.forall( _.isInstanceOf[CharLiteral] )) {
      s"new char[]{$params}"
    } else {
      s"com.dongjiaqiang.jvm.dsl.java.core.ofArray($params)"
    }
  }

  override def visit(literal: EitherLiteral, visitor: ExpressionVisitor[String]): String = {
    literal.literal match {
      case Left( left ) ⇒
        s"new com.dongjiaqiang.jvm.dsl.java.core.extend.Left<>(${visitor.visit( left )})"
      case Right( right ) ⇒
        s"new com.dongjiaqiang.jvm.dsl.java.core.extend.Right<>(${visitor.visit( right )})"
    }
  }

  //block compute expression

  override def visit(asyncBlock: Async, visitor: ExpressionVisitor[String]): String = {
    if (asyncBlock.body.expressions.exists( _.isInstanceOf[Return] )) {
      asyncBlock.executor match {
        case Some( executor ) ⇒
          s"com.dongjiaqiang.jvm.dsl.java.core.supplyAsync(()->${visitor.visit( asyncBlock.body )},${executor.symbolName})"
        case None ⇒
          s"com.dongjiaqiang.jvm.dsl.java.core.supplyAsync(()->${visitor.visit( asyncBlock.body )})"
      }
    } else {
      asyncBlock.executor match {
        case Some( executor ) ⇒
          s"com.dongjiaqiang.jvm.dsl.java.core.runAsync(()->${visitor.visit( asyncBlock.body )},${executor.symbolName})"
        case None ⇒
          s"com.dongjiaqiang.jvm.dsl.java.core.runAsync(()->${visitor.visit( asyncBlock.body )})"
      }
    }
  }

  override def visit(tryBlock: Try, visitor: ExpressionVisitor[String]): String = {
    s"com.dongjiaqiang.jvm.dsl.java.core.extend.Try.apply(()->${visitor.visit( tryBlock.body )})"
  }


  override def visit(forExpr: For, visitor: ExpressionVisitor[String]): String = {
    s"""
       |for(${visitor.visit( forExpr.loopVarDef )};${visitor.visit( forExpr.loopVarCondition )};${visitor.visit( forExpr.loopVarUpdate )})
       |   ${visitor.visit( forExpr.body )}
       |""".stripMargin
  }

  override def visit(forCollection: ForCollection, visitor: ExpressionVisitor[String]): String = {
    s"""
       |for(${visitor.visit( forCollection.localVarDef )}:${visitor.visit( forCollection.looped )})
       |  ${visitor.visit( forCollection.body )}
       |""".stripMargin
  }

  override def visit(forMap: ForMap, visitor: ExpressionVisitor[String]): String = {
    ""
  }

  override def visit(whileExpr: While, visitor: ExpressionVisitor[String]): String = super.visit( whileExpr, visitor )

  override def visit(doWhile: DoWhile, visitor: ExpressionVisitor[String]): String = super.visit( doWhile, visitor )

  override def visit(ifExpr: If, visitor: ExpressionVisitor[String]): String = super.visit( ifExpr, visitor )

  override def visit(assign: Assign, visitor: ExpressionVisitor[String]): String = {
    s"${assign.varRef.name.mkString( "." )} = ${visitor.visit( assign.assigned )}"
  }


  override def visit(assert: Assert, visitor: ExpressionVisitor[String]): String = {
    s"assert ${visitor.visit( assert.expression )}"
  }

  override def visit(returnExpr: Return, visitor: ExpressionVisitor[String]): String = {
    returnExpr.expression match {
      case UnitLiteral ⇒ "return"
      case _ ⇒ s"return ${visitor.visit( returnExpr.expression )}"
    }
  }

  override def visit(break: Break.type, visitor: ExpressionVisitor[String]): String = {
    "break"
  }

  override def visit(continue: Continue.type, visitor: ExpressionVisitor[String]): String = {
    "continue"
  }

  override def visit(throwExpr: Throw, visitor: ExpressionVisitor[String]): String = {
    s"throw ${visitor.visit( throwExpr.expression )}"
  }

  //var expression

  override def visit(localVarDef: LocalVarDef, visitor: ExpressionVisitor[String]): String = {
    val javaType = localVarDef.dslType match {
      case numberType: NumberDslType ⇒
        core.toBasicType( numberType )
      case _ ⇒ core.toJavaType( localVarDef.dslType )
    }
    localVarDef.assigned match {
      case None ⇒ s"$javaType ${localVarDef.fieldScope.symbolName}"
      case Some( expression ) ⇒ s"$javaType ${localVarDef.fieldScope.symbolName} = ${visitor.visit( expression )}"
    }
  }

  override def visit(arrayVarRef: ArrayVarRef, visitor: ExpressionVisitor[String]): String = {
    s"${arrayVarRef.name.mkString( "." )}[${visitor.visit( arrayVarRef.indexExpression )}]"
  }

  override def visit(mapVarRef: MapVarRef, visitor: ExpressionVisitor[String]): String = {
    s"${mapVarRef.name.mkString( "." )}(${visitor.visit( mapVarRef.KeyExpression )})}"
  }

  override def visit(varRef: VarRef, visitor: ExpressionVisitor[String]): String = {
    varRef.name.mkString( "." )
  }


  override def visit(lambda: Lambda, visitor: ExpressionVisitor[String]): String = {
    s"(${lambda.inputs.mkString( "," )})->${visitor.visit( lambda )}"
  }

  //unary expression
  override def visit(cast: Cast, visitor: ExpressionVisitor[String]): String = {
    s"(${core.toJavaType( cast.castType )})${visitor.visit( cast.child )}"
  }

  override def visit(negate: Negate, visitor: ExpressionVisitor[String]): String = {
    s"!${visitor.visit( negate )}"
  }

  override def visit(opposite: Opposite, visitor: ExpressionVisitor[String]): String = {
    s"(-${visitor.visit( opposite.child )})"
  }

  override def visit(instanceof: Instanceof, visitor: ExpressionVisitor[String]): String = {
    s"${visitor.visit( instanceof.child )} instanceof ${core.toJavaType( instanceof.judgeType )}"
  }

  override def visit(paren: Paren, visitor: ExpressionVisitor[String]): String = {
    s"(${visitor.visit( paren.child )})"
  }

  //binary expression

  override def visit(div: Div, visitor: ExpressionVisitor[String]): String = {
    s"${visitor.visit( div.left )} / ${visitor.visit( div.right )}"
  }

  override def visit(mod: Mod, visitor: ExpressionVisitor[String]): String = {
    s"${visitor.visit( mod.left )} % ${visitor.visit( mod.right )}"
  }

  override def visit(mul: Mul, visitor: ExpressionVisitor[String]): String = {
    s"${visitor.visit( mul.left )} * ${visitor.visit( mul.right )}"
  }

  override def visit(add: Add, visitor: ExpressionVisitor[String]): String = {
    s"${visitor.visit( add.left )} + ${visitor.visit( add.right )}"
  }

  override def visit(sub: Sub, visitor: ExpressionVisitor[String]): String = {
    s"${visitor.visit( sub.left )} - ${visitor.visit( sub.right )}"
  }

  override def visit(leftShift: LeftShift, visitor: ExpressionVisitor[String]): String = {
    s"${visitor.visit( leftShift.left )} << ${visitor.visit( leftShift.right )}"
  }

  override def visit(rightShift: RightShift, visitor: ExpressionVisitor[String]): String = {
    s"${visitor.visit( rightShift.left )} >> ${visitor.visit( rightShift.right )}"
  }

  override def visit(unsignedRightShift: UnsignedRightShift, visitor: ExpressionVisitor[String]): String = {
    s"${visitor.visit( unsignedRightShift.left )} >>> ${visitor.visit( unsignedRightShift.right )}"
  }

  override def visit(lt: Lt, visitor: ExpressionVisitor[String]): String = {
    s"${visitor.visit( lt.left )} < ${visitor.visit( lt.right )}"
  }

  override def visit(gt: Gt, visitor: ExpressionVisitor[String]): String = {
    s"${visitor.visit( gt.left )} > ${visitor.visit( gt.right )}"
  }

  override def visit(le: Le, visitor: ExpressionVisitor[String]): String = {
    s"${visitor.visit( le.left )} <= ${visitor.visit( le.right )}"
  }

  override def visit(ge: Ge, visitor: ExpressionVisitor[String]): String = {
    s"${visitor.visit( ge.left )} >= ${visitor.visit( ge.right )}"
  }

  override def visit(eq: Eq, visitor: ExpressionVisitor[String]): String = {
    s"${visitor.visit( eq.left )} == ${visitor.visit( eq.right )}"
  }

  override def visit(ne: NotEq, visitor: ExpressionVisitor[String]): String = {
    s"${visitor.visit( ne.left )} != ${visitor.visit( ne.right )}"
  }

  override def visit(bitAnd: BitAnd, visitor: ExpressionVisitor[String]): String = {
    s"${visitor.visit( bitAnd.left )} & ${visitor.visit( bitAnd.right )}"
  }

  override def visit(caret: Caret, visitor: ExpressionVisitor[String]): String = {
    s"${visitor.visit( caret.left )} ^ ${visitor.visit( caret.right )}"
  }

  override def visit(bitOr: BitOr, visitor: ExpressionVisitor[String]): String = {
    s"${visitor.visit( bitOr.left )} | ${visitor.visit( bitOr.right )}"
  }

  override def visit(and: And, visitor: ExpressionVisitor[String]): String = {
    s"${visitor.visit( and.left )} && ${visitor.visit( and.right )}"
  }

  override def visit(or: Or, visitor: ExpressionVisitor[String]): String = {
    s"${visitor.visit( or.left )} || ${visitor.visit( or.right )}"
  }


}
