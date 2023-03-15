package com.dongjiaqiang.jvm.dsl.api.expression.statement

import com.dongjiaqiang.jvm.dsl.api.expression.Expression

/**
 * <pre><code>
 * program{
 *      def method(Int i)=Unit{
 *          while(i>0){
 *              if(i==10){
 *                break;  // break => Break
 *           }
 *          foo();
 *          i++;
 *      }
 * }
 * }
 * <pre><code>
 */

object Break extends Expression {
  override def toString: String = "break"

  override def equals(obj: Any): Boolean = obj.isInstanceOf[Break.type]
}
