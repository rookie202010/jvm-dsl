package com.dongjiaqiang.jvm.dsl.java.core.translate

import com.dongjiaqiang.jvm.dsl.api.expression.visitor.ExpressionVisitor
import com.dongjiaqiang.jvm.dsl.api.expression.visitor.callchain.CallChainExpressionVisitor
import com.dongjiaqiang.jvm.dsl.api.expression.{IntLiteralCallChain, MethodCall, Part, VarName}
import com.dongjiaqiang.jvm.dsl.java.api.enhance.DslTypeEnhance

import scala.collection.mutable.ArrayBuffer


trait CallChainExpressionJavaTranslator extends CallChainExpressionVisitor[String]{

  private def groupParts(parts:List[Part]):List[List[Part]]={
    val groups = ArrayBuffer[List[Part]]()
    groupParts(parts,groups,ArrayBuffer())
    groups.toList
  }

  def groupParts(parts:List[Part],groups:ArrayBuffer[List[Part]],group:ArrayBuffer[Part]):Unit={
    parts match {
      case Nil⇒
        groups.append(group.toList)
      case head :: tail ⇒
        head match {
          case MethodCall( _, name, _ ) ⇒
            if (DslTypeEnhance.transformMethods.contains( name )) {

              group.append( head )
              if(tail.isEmpty){
                  group.append(MethodCall(None,"toList",Array()))
              }
              groupParts(tail,groups,group)

            } else if(DslTypeEnhance.actionMethods.contains(name)) {

              group.append(head)
              groups.append(group.toList)
              groupParts(tail,groups,ArrayBuffer())

            }else{

              group.append(MethodCall(None,"toList",Array()))
              groups.append(group.toList)
              groupParts(tail,groups,ArrayBuffer(head))

            }
          case _: VarName ⇒
              groups.append(group.toList)
              groupParts(tail,groups,new ArrayBuffer())
        }
    }
  }

  /**
   * <pre><code>
   * String str = 10.until(20).map(i=>i+1).map(j=>j.toString()).mkString(",");
   * <pre><code>
   *
   *  --->
   *
   * <pre><code>
   * <pre><code>
   *
   *
   */
  override def visit(literalCallChain: IntLiteralCallChain, visitor: ExpressionVisitor[String]): String = {
      if(literalCallChain.tails.isEmpty){
          s"${visitor.visit(literalCallChain.head)}"
      }else{
        val groups = groupParts(literalCallChain.tails)
        val headGroup = groups.head
        val tailGroups = groups.tail


      }

      val groups = groupParts(literalCallChain.tails)
      val head = groups.head
      val tail = groups.tail
//      if(head.length==1){
//          head.head match {
//            case MethodCall(_, name, params)⇒
//                  name match {
//                    case "until"⇒
//                  }
//          }
//      }
    ""
  }


}
