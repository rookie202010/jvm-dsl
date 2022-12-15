package com.dongjiaqiang.jvm.dsl.core

import com.dongjiaqiang.jvm.dsl.core.`type`.ClazzType

import scala.collection.mutable.{Map ⇒ MMap}

package object scope {

  object SymbolType extends Enumeration {
    val FIELD, CLAZZ, METHOD, UNDEFINED = Value
  }

  object Resolved extends Enumeration {
    val TRUE, FALSE, UNDEFINED = Value
  }

  import scala.collection.mutable.{ArrayBuffer, ListMap ⇒ MutableMap}

  def resolve(index: Int,
              refs: List[String],
              params: MutableMap[String, FieldScope],
              skipCurrentScope:Boolean,
              parentScope: Option[Scope]): scope.Resolved.Value = {
    if(skipCurrentScope){
      parentScope.map( _.resolve( index, refs ) ).getOrElse( Resolved.FALSE )
    }
    refs match {
      case ref :: Nil ⇒
        params.get( ref ) match {
          case Some( _ ) ⇒
            Resolved.TRUE
          case None ⇒ parentScope.map( _.resolve( index, refs ) ).getOrElse( Resolved.FALSE )
        }
      case ref :: childRef ⇒
        params.get( ref ) match {
          case Some( fieldScope ) ⇒
            fieldScope.dslType match {
              case clazzType: ClazzType ⇒ fieldScope.resolve( childRef, clazzType )
              case _ ⇒ Resolved.FALSE
            }
          case None ⇒
            parentScope.map( _.resolve( index, refs ) ).getOrElse( Resolved.FALSE )
        }
    }
  }
}
