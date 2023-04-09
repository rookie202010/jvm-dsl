package com.dongjiaqiang.jvm.dsl.api.scope

import com.dongjiaqiang.jvm.dsl.api.`type`._
import com.dongjiaqiang.jvm.dsl.api.scope

/**
 * field scope
 * <pre><code>
 * program{
 *
 *    Int a = 1;// outerScopeIndex = 0, symbolName = a,dslType = IntType,belong to programScope
 *
 *    def foo(Long a)=Unit{ // outerScopeIndex = 0,symbolName = a,dslType = LongType,belong to methodScope foo
 *        a = 10;
 *        {
 *          c = 100;
 *          a = 200;
 *          String s; //outerScopeIndex = 2,symbolName = s,dslType = StringType,belong to blockScope block1
 *        } // block1
 *
 *        Int c = 10; //outerScopeIndex = 2,symbolName = c,dslType = IntType,belong to blockScope block0
 *
 *    } //block0
 *
 *    Int b = 1; // outerScopeIndex = 2, symbolName = b,dslType = IntType,belong to programScope
 *
 *    class Foo(Int a,Long b){
 *      //outerScopeIndex = 0,symbolName = a,dslType = IntType,belong to clazzScope Foo
 *      //outerScopeIndex = 0,symbolName = b,dslType = LongType,belong to clazzScope Foo
 *    }
 * }
 *<pre><code>
 */

class FieldScope(val outerScopeIndex:Int,
                 val symbolName:String,
                 val dslType: DslType,
                 val belongScope:Scope,
                 val programScope: ProgramScope,
                 val volatile:Boolean = false) extends Scope {

  def resolveDslType(dslType: DslType): FieldScope = {
   new  FieldScope(outerScopeIndex, symbolName, dslType, belongScope, programScope, volatile)
  }

  override def toString: String =
    s"""
        FieldScope:
        statements $statements
        outerScopeIndex = $outerScopeIndex
        fieldName = $symbolName
        fieldType = $dslType
        volatile = $volatile
      """

  override def getSymbolType(symbolName: String): scope.SymbolType.Value = {
    if (symbolName == this.symbolName) {
      SymbolType.FIELD
    } else {
      SymbolType.UNDEFINED
    }
  }

  override def equals(obj: Any): scala.Boolean =
    obj match {
      case fieldScope: FieldScope ⇒
        symbolName == fieldScope.symbolName &&
          dslType == fieldScope.dslType &&
          volatile == fieldScope.volatile &&
          outerScopeIndex == fieldScope.outerScopeIndex &&
          statements == fieldScope.statements
      case _ ⇒ false
    }

  /**
   *
   * fieldScope in program,class,method params
   *<pre><code>
   * program{
   *    Class A(B a,Long i){}
   *    Class B(Int j,Int k){}
   *
   *    A a = new A(new B(1,2),11L);
   *    Int b = 10L;
   *
   *    D d = new D(Int k,Int z);
   *
   *    def method()=Unit{
   *        Int i1 = a.a; // resolved
   *        Int i2 = a.b; // unresolved
   *
   *        Int i3 = b; // resolved
   *        Int i4 = c; // unresolved
   *
   *        Int i5 = d.k; // undefined because d may be imported class but we can not resolve in this phase
   *        Int i6 = e.a; // unresolved
   *
   *        Int i7 = b.a; // unresolved
   *
   *    }
   * }
   *<pre><code>
   * resolve localRefs in fieldScope
   *
   * @param index localRefs index in blockScope
   * @param arrayRefsIndex array refs index
   * @param refs  localRefs names
   * @return
   */
  override def resolveVarRefs(index: Int, refs: List[String], arrayRefsIndex:Map[Int,Int]): Option[FieldScope] = {
    refs.zipWithIndex match {
      /**
       * for example
       * Int a = 10;
       * Int b = a;
       */
      case (ref,_) :: Nil ⇒
        if (ref == symbolName) {
          Some(this)
        } else {
          None
        }
      case (ref,index) :: childRef ⇒
        if(ref != symbolName){
            None
        }else {
          dslType match {
            case arrayType: ArrayType⇒
                resolve(childRef,arrayRefsIndex,arrayType,index)
            case clazzType: ClazzType ⇒
                resolve(childRef,arrayRefsIndex,clazzType)
            case definitionClazzType: DefinitionClazzType⇒
                resolve(childRef,arrayRefsIndex,definitionClazzType)
            case tupleType: TupleType⇒
                resolve(childRef,arrayRefsIndex,tupleType)
            case UnResolvedType | AnyType ⇒ Some( this )
            case _ ⇒ None
          }
        }
    }
  }

  def resolve(childRefWithIndex:List[(String,Int)],arrayRefsIndex:Map[Int,Int],dslType:ClazzType):Option[FieldScope] = {
    val importClazzType = programScope.importManager.resolve(dslType)
    importClazzType match {
      case Some( value ) ⇒
        val (childRef, index) = childRefWithIndex.head
        val fields = value.fields
        if(childRefWithIndex.size==1){
          fields.get(childRef) match {
            case Some(_) ⇒ Some(this)
            case None⇒None
          }
        }else{
          fields.get( childRef ) match {
            case Some( fieldScope ) ⇒
              fieldScope.dslType match {
                case arrayType: ArrayType⇒
                    resolve(childRefWithIndex.tail,arrayRefsIndex,arrayType,index)
                case childDslType: ClazzType ⇒
                    resolve( childRefWithIndex.tail, arrayRefsIndex, childDslType )
                case definitionClazzType: DefinitionClazzType⇒
                    resolve( childRefWithIndex.tail, arrayRefsIndex, definitionClazzType )
                case tupleType: TupleType⇒
                    resolve(childRefWithIndex.tail,arrayRefsIndex,tupleType)
                case _ ⇒ None
              }
            case None ⇒ None
          }
        }
      case None ⇒ None
    }
  }
  def resolve(childRefWithIndex: List[(String,Int)], arrayRefsIndex:Map[Int,Int], dslType: DefinitionClazzType): Option[FieldScope] = {
    val (childRef, index) = childRefWithIndex.head
    val fields = dslType.clazzScope.fields
    if (childRefWithIndex.size == 1) {
      fields.get( childRef ) match {
        case Some( fieldScope ) ⇒ Some( fieldScope )
        case None ⇒ None
      }
    } else {
      fields.get( childRef ) match {
        case Some( fieldScope ) ⇒
          fieldScope.dslType match {
            case arrayType: ArrayType⇒
                resolve(childRefWithIndex.tail,arrayRefsIndex,arrayType,index)
            case clazzType: ClazzType ⇒
                resolve( childRefWithIndex.tail, arrayRefsIndex, clazzType )
            case definitionClazzType: DefinitionClazzType⇒ resolve( childRefWithIndex, arrayRefsIndex, definitionClazzType )
            case tupleType: TupleType⇒
                resolve(childRefWithIndex.tail,arrayRefsIndex,tupleType)
            case _ ⇒ None
          }
        case None ⇒ None
      }
    }
  }

  def resolve(childRefWithIndex:List[(String,Int)],arrayRefsIndex:Map[Int,Int],dslType:ArrayType,index:Int):Option[FieldScope]= {
    if(arrayRefsIndex.contains(index)) {
        val dim = arrayRefsIndex(index)
        dslType.carryDslType match {
          case definitionClazzType: DefinitionClazzType ⇒
            if(dim>0){
              None
            }else {
              resolve( childRefWithIndex, arrayRefsIndex, definitionClazzType )
            }
          case clazzType: ClazzType ⇒
            if(dim>0){
              None
            }else {
              resolve( childRefWithIndex, arrayRefsIndex, clazzType )
            }
          case tupleType: TupleType⇒
            if(dim>0){
              None
            }else{
              resolve(childRefWithIndex,arrayRefsIndex,tupleType)
            }
          case arrayType: ArrayType ⇒
            scope.resolveArrayVarRefs( dim, arrayType ) match {
              case Some( dslType ) ⇒
                dslType match {
                  case definitionClazzType: DefinitionClazzType ⇒ resolve( childRefWithIndex, arrayRefsIndex, definitionClazzType )
                  case clazzType: ClazzType ⇒ resolve( childRefWithIndex, arrayRefsIndex, clazzType )
                  case tupleType: TupleType⇒ resolve(childRefWithIndex,arrayRefsIndex,tupleType)
                  case _ ⇒ None
                }
              case None ⇒ None
            }
          case _ ⇒
            None
        }
    }else{
      None
    }
  }

  def resolve(childRefWithIndex:List[(String,Int)],arrayRefsIndex:Map[Int,Int],dslType:TupleType):Option[FieldScope]={
    val (childRef, index) = childRefWithIndex.head
    if(childRefWithIndex.size==1){
      "^_\\d$".r.findFirstIn( childRef )  match {
        case Some(_)⇒
          val tIndex = childRef.drop( 1 ).toInt
          if(tIndex <= dslType.parameterTypes.length){
              Some(this)
          }else{
              None
          }
        case _⇒None
      }
    }else {
      "^_\\d$".r.findFirstIn( childRef ) match {
        case Some( _ ) ⇒
          val tIndex = childRef.drop( 1 ).toInt
          if (tIndex <= dslType.parameterTypes.length) {
            val parameterType = dslType.parameterTypes( tIndex-1 )
            parameterType match {
              case clazzType: ClazzType ⇒ resolve( childRefWithIndex.tail, arrayRefsIndex, clazzType )
              case definitionClazzType: DefinitionClazzType ⇒ resolve( childRefWithIndex.tail, arrayRefsIndex, definitionClazzType )
              case arrayType: ArrayType ⇒ resolve( childRefWithIndex.tail, arrayRefsIndex, arrayType, index )
              case tupleType: TupleType ⇒ resolve( childRefWithIndex.tail, arrayRefsIndex, tupleType )
            }
          } else {
            None
          }
        case _ ⇒
          None
      }
    }
  }


}
