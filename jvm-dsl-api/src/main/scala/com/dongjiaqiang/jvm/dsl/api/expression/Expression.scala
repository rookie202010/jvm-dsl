package com.dongjiaqiang.jvm.dsl.api.expression

import com.dongjiaqiang.jvm.dsl.api.`type`._
import com.dongjiaqiang.jvm.dsl.api.expression.`var`.LocalVarDef
import com.dongjiaqiang.jvm.dsl.api.expression.block.Block
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.ExpressionVisitor
import com.dongjiaqiang.jvm.dsl.api.scope.ProgramScope

import scala.collection.mutable.ArrayBuffer
import scala.language.{existentials, postfixOps}

/**
 * expressions are the basic structure of code logic.
 * code logic is implemented through expressions.
 * common expression include assignment statement expressions and function call expressions.
 */
trait Expression {
    def visit[T](expressionVisitor: ExpressionVisitor[T]): T = {
        expressionVisitor.visit( this )
    }
}

trait BlockExpression extends Expression

trait ValueExpression extends Expression{
    def  getValueType(programScope: ProgramScope):DslType
}

//for statement expression

/**
 * <pre><code>
 *program{
 *      def method(Int i)=Unit{
 *         for(Int i=0;i<100;i=i+1){  // for(...){...} => ForLoop
 *              foo();
 *         }
 *      }
 * }
 * <pre><code>
 */

 class ForLoop(val loopVarDef:LocalVarDef,
               val loopVarCondition:ValueExpression,
               val loopVarUpdate:Expression,
              override val expressions:ArrayBuffer[Expression] = ArrayBuffer()) extends Block(expressions ) {
    override def toString: String =
        s"""
           |for($loopVarDef;$loopVarCondition;$loopVarUpdate){
           |     ${expressions.map( e ⇒ e + ";" ).mkString( "\n" )}
           |}
           |""".stripMargin

    override def equals(obj: Any): Boolean = obj match {
        case forLoop: ForLoop ⇒
            forLoop.loopVarDef == loopVarDef &&
              forLoop.loopVarCondition == loopVarCondition &&
              forLoop.loopVarUpdate == loopVarUpdate &&
              forLoop.expressions == expressions
        case _ ⇒ false
    }
}

case class For(loopVarDef:LocalVarDef,
               loopVarCondition:ValueExpression,
               loopVarUpdate:Expression,
               body:Block) extends BlockExpression {
    override def toString: String =
        s"""
           |for($loopVarDef;$loopVarCondition;$loopVarUpdate)
           | $body
           |""".stripMargin

    override def equals(obj: Any): Boolean = obj match {
        case `for`: For ⇒
            `for`.loopVarDef == loopVarDef &&
              `for`.loopVarCondition == loopVarCondition &&
              `for`.loopVarUpdate == loopVarUpdate &&
              `for`.body == body
        case _ ⇒ false
    }
}

/**
 * <pre><code>
 *program{
 *      def method(List[Int] list)=Unit{
 *         for(Int i:list){  // for(...){...} => ForLoopCollection
 *              foo();
 *         }
 *      }
 * }
 * <pre><code>
 */

class ForLoopCollection(val localVarDef: LocalVarDef,
                        val looped:ValueExpression,
                        override val expressions:ArrayBuffer[Expression] = ArrayBuffer()) extends Block(expressions ) {
    override def toString: String =
        s"""
           |for($localVarDef:$looped){
           |    ${expressions.map( e ⇒ e + ";" ).mkString( "\n" )}
           |}
           |""".stripMargin

    override def equals(obj: Any): Boolean = obj match {
        case forLoopCollection: ForLoopCollection ⇒
            forLoopCollection.localVarDef == localVarDef &&
              forLoopCollection.looped == looped &&
              forLoopCollection.expressions == expressions
        case _ ⇒ false
    }
}

case class ForCollection(localVarDef: LocalVarDef,
                    looped:ValueExpression,body:Block) extends BlockExpression {
    override def toString: String =
        s"""
           |for($localVarDef:$looped)
           | $body
           |""".stripMargin

    override def equals(obj: Any): Boolean = {
        obj match {
            case forCollection: ForCollection ⇒
                forCollection.localVarDef == localVarDef &&
                  forCollection.looped == looped &&
                  forCollection.body == body
            case _ ⇒ false
        }
    }
}
/**
 * <pre><code>
 *program{
 *      def method(Map[Int,String] map)=Unit{
 *         for(Int key,String value:map){  // for(...){...} => ForLoopMap
 *              foo();
 *         }
 *      }
 * }
 * <pre><code>
 */
class ForLoopMap(val loopKeyDef:LocalVarDef,
                 val loopValueDef:LocalVarDef,
                 val looped:ValueExpression,
                 override val expressions:ArrayBuffer[Expression] = ArrayBuffer()) extends Block(expressions ) {
    override def toString: String =
        s"""
           |for($loopKeyDef,$loopValueDef:$looped){
           |     ${expressions.map( e ⇒ e + ";" ).mkString( "\n" )}
           |}
           |""".stripMargin

    override def equals(obj: Any): Boolean = obj match {
        case forLoopMap: ForLoopMap ⇒
            forLoopMap.loopKeyDef == loopKeyDef &&
              forLoopMap.loopValueDef == loopValueDef &&
              forLoopMap.looped == looped &&
              forLoopMap.expressions == expressions
        case _ ⇒ false
    }

}

case class ForMap(loopKeyDef:LocalVarDef,
             loopValueDef:LocalVarDef,
             looped:ValueExpression,body:Block) extends BlockExpression {
    override def toString: String =
        s"""
           |for($loopKeyDef,$loopValueDef:$looped)
           |     $body
           |""".stripMargin

    override def equals(obj: Any): Boolean = obj match {
        case forMap: ForMap ⇒
            forMap.loopKeyDef == loopKeyDef &&
              forMap.loopValueDef == loopValueDef &&
              forMap.looped == looped &&
              forMap.body == body
        case _ ⇒ false
    }
}

//while statement expression

/**
 * <pre><code>
 *program{
 *      def method(Int i)=Unit{
 *          while(i>0){         //while(i>0) => WhileCondition
 *              if(i==10){
 *                  break;  // {...} => WhileBlock
 *              }
 *              foo();
 *              i++;
 *          }
 *      }
 * }        while(...){...} => While
 * <pre><code>
 */

case class WhileCondition(expression: ValueExpression) extends Expression {
    override def toString: String = s"while($expression)"

    override def equals(obj: Any): Boolean = obj match {
        case whileCondition: WhileCondition => whileCondition.expression == expression
        case _ ⇒ false
    }
}
class WhileBlock(override val expressions:ArrayBuffer[Expression] = new ArrayBuffer[Expression]()) extends Block(expressions ) {
    override def toString: String = s"${expressions.map( e ⇒ e + ";" ).mkString( "\n" )}"

    override def equals(obj: Any): Boolean = obj match {
        case whileBlock: WhileBlock ⇒ whileBlock.expressions == expressions
        case _ ⇒ false
    }
}

case class While(condition: ValueExpression, body:Block) extends BlockExpression {
    override def toString: String =
        s"""
           |while($condition)
           |$body
           |""".stripMargin

    override def equals(obj: Any): Boolean = obj match {
        case `while`: While ⇒
            `while`.condition == condition &&
              `while`.body == body
        case _ ⇒ false
    }
}

//do while statement expression

/**
 * <pre><code>
 *program{
 *      def method(Int i)=Unit{
 *          do{                 //do{...} => DoWhileBlock
 *              if(i==10){
 *                  continue;
 *              }
 *              foo();
 *              i++;
 *          }while(i<20);   //while(i<20) => DoWhileCondition
 *      }
 * }    //do{...}while(..) => //DoWhile
 * <pre><code>
 */

case class DoWhileCondition(expression: ValueExpression) extends Expression {
    override def toString: String = s"while($expression)"

    override def equals(obj: Any): Boolean = obj match {
        case doWhileCondition: DoWhileCondition ⇒
            doWhileCondition.expression == expression
        case _ ⇒ false
    }
}
class DoWhileBlock(override val expressions:ArrayBuffer[Expression] = new ArrayBuffer[Expression]()) extends Block(expressions ) {
    override def toString: String =
        s"""
           |do{
           |    ${expressions.map( e ⇒ e + ";" ).mkString( "\n" )}
           |};
           |""".stripMargin

    override def equals(obj: Any): Boolean = obj match {
        case doWhileBlock: DoWhileBlock ⇒ doWhileBlock.expressions == expressions
        case _ ⇒ false
    }
}

case class DoWhile(condition:ValueExpression,body:Block) extends BlockExpression {
    override def toString: String =
        s"""
           |do
           | $body
           |while($condition)
           |""".stripMargin

    override def equals(obj: Any): Boolean = obj match {
        case doWhile: DoWhile ⇒
            doWhile.condition == condition &&
              doWhile.body == body
        case _ ⇒ false
    }
}

//synchronized statement

/**
 * <pre><code>
 *program{
 *      def method(Int i)=Unit{
 *          sync(lock){   //sync(lock) => SyncCondition
 *                  foo();   //{ foo();} => SyncBlock
 *          }
 *      }   //sync(...){ ...} => Sync
 * }
 * <pre><code>
 */

case class SyncCondition(expression: Expression) extends Expression {
    override def toString: String = s"sync($expression)"

    override def equals(obj: Any): Boolean = obj match {
        case syncCondition: SyncCondition ⇒
            syncCondition.expression == expression
        case _ ⇒ false
    }
}

class SyncBlock(override val expressions: ArrayBuffer[Expression] = ArrayBuffer( )) extends Block( expressions ) {
    override def toString: String =
        s"""
           |{
           |   ${expressions.map( e ⇒ e + ";" ).mkString( "\n" )}
           |}
           |""".stripMargin

    override def equals(obj: Any): Boolean = obj match {
        case syncBlock: SyncBlock ⇒
            syncBlock.expressions == expressions
        case _ ⇒ false
    }
}

case class Sync(condition: Expression,body:Block) extends BlockExpression {
    override def toString: String =
        s"""
           |sync($condition)
           | $body
           |""".stripMargin

    override def equals(obj: Any): Boolean = obj match {
        case sync: Sync ⇒
            sync.condition == condition &&
              sync.body == body
        case _ ⇒ false
    }

}

//if statement

/**
 * <pre><code>
 *program{
 *      def method(Int i)=Unit{
 *           if(i==0){          // i==0 => IfCondition
 *              foo();          // { foo(); } => IfBlock
 *           }else if(i<0){
 *              foo1();
 *           }else{
 *              foo2();
 *           }
 *      }
 * }         //if(...){...}else if(...){...}else{...} => If
 * <pre><code>
 */

case class IfCondition(expression: ValueExpression,first:Boolean) extends Expression {
    override def toString: String = if (first) s"if($expression)" else s"else if($expression)"

    override def equals(obj: Any): Boolean = obj match {
        case ifCondition: IfCondition ⇒
            ifCondition.expression == expression && ifCondition.first == first
        case _ ⇒ false
    }
}
class IfBlock(override val expressions:ArrayBuffer[Expression] = ArrayBuffer()) extends Block(expressions ) {
    override def toString: String =
        s"""
           |{
           |    ${expressions.map( e ⇒ e + ";" ).mkString( "\n" )}
           |}
           |""".stripMargin

    override def equals(obj: Any): Boolean = obj match {
        case ifBlock: IfBlock ⇒
            ifBlock.expressions == expressions
        case _ ⇒ false
    }

}

case class If(cases:Array[(ValueExpression,Block)], default:Option[Block]) extends BlockExpression {

    override def toString:String = {
        cases.map {
            case (condition, block) ⇒
                s"""
                   |if($condition)
                   |    $block
                   |""".stripMargin

        }.mkString( "\n" ) + default.map( _.toString ).getOrElse( "" )
    }

    override def equals(obj: Any): Boolean = obj match {
        case `if`: If ⇒
            `if`.cases.sameElements( cases ) && default == `if`.default
        case _ ⇒ false
    }

}

//try statement

/**
 * <pre><code>
 *program{
 *      def method()=Unit{
 *          try{                       //   try { foo(); } => TryBlock
 *              foo();                 //   catch(Exception1 ex) => CatchParameter
 *          }catch(Exception1 ex){     //   {   handleEx1();   } => CatchBlock
 *              handleEx1();           //
 *          }catch(Exception2 ex){     //
 *              handleEx2();           //
 *          }finally{                  //   {   handleFinally();    } => FinallyBlock
 *              handleFinally();       //
 *          }                          //
 *      }
 * }         //try{...}catch(...){...}finally{...}=>TryCatch
 * <pre><code>
 */

class TryBlock(override val expressions: ArrayBuffer[Expression] = ArrayBuffer( )) extends Block( expressions ) {
    override def toString: String =
        s"""
           |try{
           |    ${expressions.map( e ⇒ e + ";" ).mkString( "\n" )}
           |}
           |""".stripMargin

    override def equals(obj: Any): Boolean = obj match {
        case tryBlock: TryBlock ⇒
            tryBlock.expressions == expressions
        case _ ⇒ false
    }
}

case class CatchParameter(name: String, dslType: DslType) extends Expression {
    override def toString: String =
        s"""
           |catch($dslType $name)
           |""".stripMargin

    override def equals(obj: Any): Boolean = obj match {
        case catchParameter: CatchParameter ⇒
            catchParameter.name == name && catchParameter.dslType == dslType
        case _ ⇒ false
    }
}

class CatchBlock(override val expressions: ArrayBuffer[Expression] = ArrayBuffer( )) extends Block( expressions ) {
    override def toString: String =
        s"""
           |{
           |     ${expressions.map( e ⇒ e + ";" ).mkString( "\n" )}
           |}
           |""".stripMargin

    override def equals(obj: Any): Boolean = obj match {
        case catchBlock: CatchBlock ⇒
            catchBlock.expressions == expressions
        case _ ⇒ false
    }
}

class FinallyBlock(override val expressions:ArrayBuffer[Expression] = ArrayBuffer()) extends Block(expressions ) {
    override def toString: String =
        s"""
           |finally{
           |      ${expressions.map( e ⇒ e + ";" ).mkString( "\n" )}
           |}
           |""".stripMargin

    override def equals(obj: Any): Boolean = obj match {
        case finallyBlock: FinallyBlock ⇒
            finallyBlock.expressions == expressions
        case _ ⇒ false
    }
}

case class TryCatch(tryBlock:Block,catches:Array[((String,DslType),Block)],finallyBlock:Option[Block]) extends BlockExpression {
    override def toString: String =
        s"""
           |try
           | $tryBlock

           |${catches.map{
            `catch`⇒ s"catch(${`catch`._1._2} ${`catch`._1._1}) ${`catch`._2}"
        }.mkString( "\n" )
        }
           |${
            finallyBlock match {
                case Some(
                block ) ⇒
                    "finally" + block
                case None ⇒ ""
            }
        }
           |""".stripMargin

    override def equals(obj: Any): Boolean = obj match {
        case tryCatch: TryCatch ⇒
            tryCatch.tryBlock == tryBlock &&
              tryCatch.catches.sameElements( catches ) &&
              tryCatch.finallyBlock == finallyBlock
        case _ ⇒ false
    }

}

package object expression {

    def getString[T<:Expression](start: String, end: String, sep: String, expressions: Array[Either[T, String]]): String =
        s"$start${
            expressions.map {
                case Left( i ) ⇒ i.toString
                case Right( j ) ⇒ j
            }.mkString( sep )
        }$end"

    def getString[T,K<:Expression](t: T, name: String, params: Array[K]): String = {
        s"$t.$name(${params.mkString( "." )})"
    }
}