package com.dongjiaqiang.jvm.dsl.api.expression.`var`

import com.dongjiaqiang.jvm.dsl.api.`type`._
import com.dongjiaqiang.jvm.dsl.api.exception.ExpressionParseException
import com.dongjiaqiang.jvm.dsl.api.expression.ValueExpression
import com.dongjiaqiang.jvm.dsl.api.expression.call.Part
import com.dongjiaqiang.jvm.dsl.api.scope.{FieldScope, ProgramScope}

import scala.annotation.tailrec
import scala.collection.mutable.{ArrayBuffer, LinkedHashMap ⇒ MutableMap}

/**
 * <p>var ref can refer to a local var def or a global var def<p>
 *
 * <pre><code>
 * program{
 *      Foo foo = new Foo(a,b);
 *
 *      def method()=Unit{
 *        Int i = foo()*10; //LocalVarDef
 *        foo.a * 10; // foo.a => VarRef
 *        i*10; // i => VarRef
 *      }
 * }
 * <pre><code>
 */
case class VarRef(refs: List[String],
                  arrayRefIndexExpressions: MutableMap[Int, List[ValueExpression]],
                  fieldScope: Option[FieldScope],castIndex:MutableMap[Int,DslType] = MutableMap()) extends ValueExpression with Part {

  override def toString: String = refs.zipWithIndex.map {
    case (n, index) ⇒
      if (arrayRefIndexExpressions.contains( index )) {
        arrayRefIndexExpressions( index ).map( expr ⇒ s"[${expr}]" ).mkString( s"$n", "", "" )
      } else {
        n
      }
  }.mkString( "." )

  override def equals(obj: Any): Boolean = {
    obj match {
      case varRef: VarRef ⇒
        varRef.refs == refs &&
          varRef.fieldScope == fieldScope &&
          varRef.arrayRefIndexExpressions == arrayRefIndexExpressions
      case _ ⇒ false
    }
  }

  def merge(varRefs: List[VarRef]): VarRef = {
    val mergeRefs = ArrayBuffer[String]( refs: _* )
    val mergeArrayRefIndexExpressions = MutableMap[Int, List[ValueExpression]]( arrayRefIndexExpressions.toArray: _* )

    varRefs.zipWithIndex.foreach {
      case (varRef, _) ⇒
        varRef.arrayRefIndexExpressions.foreach {
          case (arrayIndex, expression) ⇒
            mergeArrayRefIndexExpressions.put( arrayIndex + mergeRefs.size, expression )
        }
        mergeRefs.appendAll( varRef.refs )
    }
    VarRef( mergeRefs.toList, mergeArrayRefIndexExpressions, fieldScope )
  }

  def getValueType: DslType = {
    fieldScope match {
      case None ⇒ UnResolvedType
      case Some( scope ) ⇒
        VarRef.getValueType(refs,arrayRefIndexExpressions,scope.dslType,scope.programScope,this)
    }
  }

  override def getValueType(programScope: ProgramScope): DslType = getValueType
}

object VarRef {

  def getDslType(arrayRefIndexExpressions: MutableMap[Int, List[ValueExpression]],
                 index:Int,calleeType:DslType,
                 tail:List[(String,Int)],programScope: ProgramScope,varRef: VarRef):DslType={
    val arrayRefIndexExpression = arrayRefIndexExpressions.get( index )
    calleeType match {
      case clazzType: ClazzType ⇒
        if (arrayRefIndexExpression.isEmpty) {
          getDslType( tail, arrayRefIndexExpressions, clazzType, programScope,varRef)
        } else {
          throw ExpressionParseException( s"ref is clazz type but have index expression: $toString" )
        }
      case definitionClazzType: DefinitionClazzType ⇒
        if (arrayRefIndexExpression.isEmpty) {
          getDslType( tail, arrayRefIndexExpressions, definitionClazzType, programScope,varRef)
        } else {
          throw ExpressionParseException( s"ref is definition clazz type but have index expression: $toString" )
        }
      case tupleType: TupleType⇒
        if (arrayRefIndexExpression.isEmpty) {
          getDslType( tail, arrayRefIndexExpressions, tupleType, programScope,varRef)
        } else {
          throw ExpressionParseException( s"ref is tuple type but have index expression: $toString" )
        }
      case arrayType: ArrayType ⇒
        if (arrayRefIndexExpression.isDefined) {
          getArrayValueType( arrayType, arrayRefIndexExpression.get, programScope )
        } else {
          throw ExpressionParseException( s"ref has array ref but have not index expression: $toString" )
        }
      case _ ⇒
        throw ExpressionParseException( s"dot separator work for $calleeType refs: ${varRef}" )
    }
  }

  def getDslType(refs: List[(String, Int)],
                 arrayRefIndexExpressions: MutableMap[Int, List[ValueExpression]],
                 clazzType: ClazzType,
                 programScope: ProgramScope,varRef: VarRef): DslType = {
    if (programScope.isImportClazz( clazzType.clazzName )) {
      val resolvedClazz = programScope.importManager.resolve( clazzType.clazzName )
      refs match {
        case (ref, index) :: Nil ⇒
          val field = resolvedClazz.fields.get( ref )
          field match {
            case Some( ImportClazzField( _, dslType ) ) ⇒
              if (arrayRefIndexExpressions.contains( index )) {
                dslType match {
                  case arrayType: ArrayType ⇒ getArrayValueType( arrayType, arrayRefIndexExpressions( index ), programScope )
                  case _ ⇒ throw ExpressionParseException( s"refs is not array ref: $toString" )
                }
              } else {
                dslType
              }
            case None ⇒
              throw ExpressionParseException( s"${clazzType.clazzName} can't find field $ref" )
          }
        case (ref, index) :: tail ⇒
          val field = resolvedClazz.fields.get( ref )
          field match {
            case Some( ImportClazzField( _, dslType ) ) ⇒
              getDslType(arrayRefIndexExpressions,index,dslType,tail,programScope,varRef)
            case None ⇒
              throw ExpressionParseException( s"${clazzType.clazzName} can't find field $ref" )
          }
      }
    } else {
      throw ExpressionParseException( s"can not find ${clazzType.clazzName} represent ${refs.mkString( "," )}" )
    }
  }

  @tailrec
  private def getArrayValueType(arrayType: ArrayType,
                                arrayRefIndexExpression: List[ValueExpression],
                                programScope: ProgramScope): DslType = {
    arrayRefIndexExpression match {
      case head :: Nil ⇒
        if (head.getValueType( programScope ) == IntType) {
          arrayType.carryDslType
        } else {
          throw ExpressionParseException( f"refs index expression must be value expression and value type must be IntType: $toString" )
        }
      case head :: tail ⇒ arrayType.carryDslType match {
        case arrayType: ArrayType ⇒
          if (head.getValueType( programScope ) == IntType) {
            getArrayValueType( arrayType, tail, programScope )
          } else {
            throw ExpressionParseException( f"refs index expression must be value expression and value type must be int: $toString" )
          }
        case _ ⇒
          throw ExpressionParseException( s"refs is not array ref: $toString" )
      }
    }
  }

  def getDslType(refs:List[(String,Int)],
                 arrayRefIndexExpressions: MutableMap[Int, List[ValueExpression]],
                 tupleType: TupleType,
                 programScope: ProgramScope, varRef: VarRef):DslType={
    val tupleParameterTypes = tupleType.parameterTypes.zipWithIndex.map(kv⇒(kv._2,kv._1)).toMap
    refs match {
      case (ref,index)::Nil⇒
          val fieldIndex = ref.drop(1).toInt
          tupleParameterTypes.get(fieldIndex) match {
            case Some(dslType)⇒
              varRef.castIndex.put(index,dslType)
              if(!arrayRefIndexExpressions.contains(index)){
                dslType
              }else{
                if(!dslType.isInstanceOf[ArrayType]){
                  throw ExpressionParseException(s"refs is not array ref: $ref")
                }else{
                  getArrayValueType(dslType.asInstanceOf[ArrayType], arrayRefIndexExpressions(index), programScope)
                }
              }
            case None⇒
              throw ExpressionParseException( s"$tupleType can't find ref: $ref" )
          }
      case (ref,index)::tail⇒
        println(ref)
        val fieldIndex = ref.drop(1).toInt
        tupleParameterTypes.get(fieldIndex) match {
          case Some(dslType)⇒
            varRef.castIndex.put(index,dslType)
            getDslType(arrayRefIndexExpressions,index,dslType,tail,programScope,varRef)
        }

    }
  }

  def getDslType(refs: List[(String, Int)],
                 arrayRefIndexExpressions: MutableMap[Int, List[ValueExpression]],
                 definitionClazzType: DefinitionClazzType,
                 programScope: ProgramScope,varRef: VarRef): DslType = {
    val clazzScope = definitionClazzType.clazzScope
    refs match {
      case (ref, index) :: Nil ⇒
        clazzScope.fields.get( ref ) match {
          case Some( fieldScope ) ⇒

            if (arrayRefIndexExpressions.contains( index )) {
              fieldScope.dslType match {
                case arrayType: ArrayType ⇒ getArrayValueType( arrayType, arrayRefIndexExpressions( index ), programScope )
                case _ ⇒ throw ExpressionParseException( s"refs is not array ref: $toString" )
              }
            } else {
              fieldScope.dslType
            }

          case None ⇒
            throw ExpressionParseException( s"${clazzScope.name} can't find field $toString" )
        }
      case (ref, index) :: tail ⇒
        clazzScope.fields.get( ref ) match {
          case Some( fieldScope ) ⇒
            getDslType(arrayRefIndexExpressions,index,fieldScope.dslType,tail, programScope, varRef)
          case None ⇒
            throw ExpressionParseException( s"${clazzScope.name} can't find field $ref" )
        }
    }
  }

  def getValueType(refs: List[String],
                   arrayRefIndexExpressions: MutableMap[Int, List[ValueExpression]],
                   calleeType: DslType,
                   programScope: ProgramScope,varRef: VarRef): DslType = {
    if (refs.length == 1) {
      arrayRefIndexExpressions.get( 0 ) match {
        case None ⇒ calleeType // a
        case Some( arrayRefIndexExpression ) ⇒
          calleeType match {
            // a[10] or a[10][100]
            case arrayType: ArrayType ⇒
              getArrayValueType( arrayType, arrayRefIndexExpression, programScope )
            case _ ⇒
              throw ExpressionParseException( s"refs is not array ref: $toString" )
          }
      }
    } else {
      arrayRefIndexExpressions.get( 0 ) match {
        case None ⇒
          calleeType match {
            //a.b a must be class type
            case clazzType: ClazzType ⇒
              getDslType( refs.tail.zipWithIndex, arrayRefIndexExpressions, clazzType, programScope,varRef )
            case definitionClazzType: DefinitionClazzType ⇒
              getDslType( refs.tail.zipWithIndex, arrayRefIndexExpressions, definitionClazzType, programScope,varRef)
            case tupleType: TupleType⇒
              getDslType(refs.tail.zipWithIndex,arrayRefIndexExpressions,tupleType,programScope,varRef)
            case _ ⇒
              throw ExpressionParseException( s"dot separator work for clazzType : $toString" )
          }
        case Some( arrayRefIndexExpression ) ⇒
          calleeType match {
            case arrayType: ArrayType ⇒
              getArrayValueType( arrayType, arrayRefIndexExpression, programScope ) match {
                case clazzType: ClazzType ⇒
                  getDslType( refs.tail.zipWithIndex, arrayRefIndexExpressions, clazzType, programScope,varRef )
                case definitionClazzType: DefinitionClazzType ⇒
                  getDslType( refs.tail.zipWithIndex, arrayRefIndexExpressions, definitionClazzType, programScope,varRef)
                case tupleType: TupleType⇒
                  getDslType(refs.tail.zipWithIndex,arrayRefIndexExpressions,tupleType,programScope,varRef)
                case dslType: DslType ⇒
                  throw ExpressionParseException( s"dot separator work for clazzType : $dslType" )
              }
            case _ ⇒
              throw ExpressionParseException( s"refs is not array ref: $toString" )
          }
      }

    }
  }

}