package com.dongjiaqiang.jvm.dsl.core

import com.dongjiaqiang.jvm.dsl.core.`type`.{ClazzType, DslType}

package object scope {

  object SymbolType extends Enumeration {
    val FIELD, CLAZZ, METHOD, UNDEFINED = Value
  }

  import scala.collection.mutable.{ArrayBuffer, ListMap ⇒ MutableMap}

  /**
   * resolve refs like i, a.b in current scope
   * @param index refs index in current scope
   * @param refs refs may separate by dot
   * @param params fields in current scope
   * @param skipCurrentScope should skip current scope than resolve in parent scope
   * @param backRef can we backRef
   * @param parentScope current scope may have parent scope
   * @return resolved result
   */
  def resolveVarRefs(index: Int,
                     refs: List[String],
                     currentScope:Scope,
                     params: MutableMap[String, FieldScope],
                     skipCurrentScope:Boolean,
                     backRef:Boolean,
                     parentScope: Option[Scope]): Option[FieldScope] = {
    if(skipCurrentScope){
      return parentScope.flatMap( _.resolveVarRefs( currentScope.outerScopeIndex, refs ) )
    }
    refs match {
      case ref :: Nil ⇒
        params.get( ref ) match {
          case Some( fieldScope ) ⇒
            if(!backRef){
              if(index >= fieldScope.outerScopeIndex){
                Some(fieldScope)
              }else{
                parentScope.flatMap( _.resolveVarRefs( currentScope.outerScopeIndex, refs ) )
              }
            }else {
              Some(fieldScope)
            }
          case None ⇒ parentScope.flatMap( _.resolveVarRefs( currentScope.outerScopeIndex, refs ) )
        }
      case ref :: childRef ⇒
        params.get( ref ) match {
          case Some( fieldScope ) ⇒
            fieldScope.dslType match {
              case clazzType: ClazzType ⇒
                if(!backRef){
                  if(index>=fieldScope.outerScopeIndex){
                      fieldScope.resolve(childRef,clazzType)
                  }else{
                      None
                  }
                }else {
                  fieldScope.resolve(childRef, clazzType)
                }
              case _ ⇒ None
            }
          case None ⇒
            parentScope.flatMap( _.resolveVarRefs( currentScope.outerScopeIndex, refs ) )
        }
    }
  }
}
