package com.dongjiaqiang.jvm.dsl.api.expression.statement

import com.dongjiaqiang.jvm.dsl.api.expression.Expression

/**
 * <pre><code>
 * program{
 * def method(Int i)=Unit{
 * while(i>0){
 * if(i==10){
 * continue;  // continue => Continue
 * }
 * foo();
 * i++;
 * }
 * }
 * }
 * <pre><code>
 */

object Continue extends Expression {
  override def toString: String = "continue"

  override def equals(obj: Any): Boolean = obj.isInstanceOf[Continue.type]
}
