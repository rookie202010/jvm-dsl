package com.dongjiaqiang.jvm.dsl.api

import com.dongjiaqiang.jvm.dsl.api.`type`.{AnyType, ArrayType, ClazzType, DefinitionClazzType, DslType, TupleType, UnResolvedType}

import scala.annotation.tailrec
import scala.language.postfixOps

package object scope {

  import scala.collection.mutable.{LinkedHashMap ⇒ MutableMap}

  object SymbolType extends Enumeration {
    val FIELD, CLAZZ, METHOD, UNDEFINED = Value
  }


  @tailrec
  def resolveArrayVarRefs(dimension:Int, dslType: DslType):Option[DslType]={
      if(dimension==0){
        Some(dslType)
      }else{
        dslType match {
          case arrayType: ArrayType⇒
            resolveArrayVarRefs(dimension-1,arrayType.carryDslType)
          case _⇒None
        }
      }
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
                     arrayRefsIndex:Map[Int,Int],
                     currentScope: Scope,
                     params: MutableMap[String, FieldScope],
                     skipCurrentScope: Boolean,
                     backRef: Boolean,
                     parentScope: Option[Scope]): Option[FieldScope] = {

    def resolve(fieldScope: FieldScope,supplier:()⇒Option[FieldScope]):Option[FieldScope]={
      if (!backRef) {
        if (index >= fieldScope.outerScopeIndex) {
          supplier.apply()
        } else {
          None
        }
      } else {
        supplier.apply()
      }
    }

    //skip current scope and resolve in parent scope
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
                  resolve(fieldScope,()⇒{
                    fieldScope.resolve( childRef,arrayRefsIndex, clazzType )
                  })
              case arrayType: ArrayType⇒
                  resolve(fieldScope,()⇒{
                    fieldScope.resolve(childRef,arrayRefsIndex,arrayType,refIndex)
                  })
              case tupleType: TupleType⇒
                  resolve(fieldScope,()⇒{
                    fieldScope.resolve(childRef,arrayRefsIndex,tupleType)
                  })
              case definitionClazzType: DefinitionClazzType⇒
                  resolve(fieldScope,()⇒{
                    fieldScope.resolve( childRef, arrayRefsIndex, definitionClazzType )
                  })
              case UnResolvedType | AnyType⇒
                  Some(fieldScope)
              case _ ⇒ None
            }
          case None ⇒
            parentScope.flatMap( _.resolveVarRefs( currentScope.outerScopeIndex, refs,arrayRefsIndex ) )
        }
    }
  }
}
