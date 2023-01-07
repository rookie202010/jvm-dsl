package com.dongjiaqiang.jvm.dsl.core

package object expression {

  def getString(start: String, end: String, sep: String, expressions: Array[Either[Expression, String]]): String =
    s"$start${
      expressions.map {
        case Left(i) ⇒ i.toString
        case Right(j) ⇒ j
      }.mkString(sep)
    }$end"

  def getString[T](t:T,name:String,params:Array[Expression]):String={
    s"$t.$name(${params.mkString(".")})"
  }
}
