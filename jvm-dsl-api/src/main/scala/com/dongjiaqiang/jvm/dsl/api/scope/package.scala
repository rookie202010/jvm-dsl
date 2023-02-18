package com.dongjiaqiang.jvm.dsl.api

import com.dongjiaqiang.jvm.dsl.api.`type`.ClazzType

import scala.language.postfixOps

package object scope {

  import scala.collection.mutable.{ListMap ⇒ MutableMap}

  object SymbolType extends Enumeration {
    val FIELD, CLAZZ, METHOD, UNDEFINED = Value
  }

  /**
   * resolve refs like i, a.b in current scope
   *
   * @param index            refs index in current scope
   * @param refs             refs may separate by dot
   * @param params           fields in current scope
   * @param skipCurrentScope should skip current scope than resolve in parent scope
   * @param backRef          can we backRef
   * @param parentScope      current scope may have parent scope
   * @return resolved result
   */
  def resolveVarRefs(index: Int,
                     refs: List[String],
                     arrayRefsIndex:Set[Int],
                     currentScope: Scope,
                     params: MutableMap[String, FieldScope],
                     skipCurrentScope: Boolean,
                     backRef: Boolean,
                     parentScope: Option[Scope]): Option[FieldScope] = {

    def resolve(index:Int,clazzType: ClazzType,fieldScope: FieldScope,childRef:List[(String,Int)]):Option[FieldScope]={
      if (arrayRefsIndex.contains(index) && clazzType.clazzName == "Array") {
        val parameterType = clazzType.parameterTypes.head
        parameterType match {
          case parameterClazzType: ClazzType ⇒
            fieldScope.resolve( childRef,arrayRefsIndex, parameterClazzType ).map( _ ⇒ fieldScope )
          case _ ⇒
            fieldScope.resolve( childRef,arrayRefsIndex, clazzType ).map( _ ⇒ fieldScope )
        }
      } else {
        fieldScope.resolve( childRef,arrayRefsIndex, clazzType ).map( _ ⇒ fieldScope )
      }
    }

    if (skipCurrentScope) {
      return parentScope.flatMap( _.resolveVarRefs( currentScope.outerScopeIndex, refs,arrayRefsIndex ) )
    }
    refs.zipWithIndex match {
      case (ref,_) :: Nil ⇒
        params.get( ref ) match {
          case Some( fieldScope ) ⇒
            if (!backRef) {
              if (index >= fieldScope.outerScopeIndex) {
                Some( fieldScope )
              } else {
                parentScope.flatMap( _.resolveVarRefs( currentScope.outerScopeIndex, refs,arrayRefsIndex ) )
              }
            } else {
              Some( fieldScope )
            }
          case None ⇒ parentScope.flatMap( _.resolveVarRefs( currentScope.outerScopeIndex, refs,arrayRefsIndex ) )
        }
      case (ref,refIndex) :: childRef ⇒
        params.get( ref ) match {
          case Some( fieldScope ) ⇒
            fieldScope.dslType match {
              case clazzType: ClazzType ⇒
                if (!backRef) {
                  if (index >= fieldScope.outerScopeIndex) {
                    resolve(refIndex,clazzType, fieldScope, childRef)
                  } else {
                    None
                  }
                } else {
                  resolve(refIndex,clazzType, fieldScope, childRef)
                }
              case _ ⇒ None
            }
          case None ⇒
            parentScope.flatMap( _.resolveVarRefs( currentScope.outerScopeIndex, refs,arrayRefsIndex ) )
        }
    }
  }
}
