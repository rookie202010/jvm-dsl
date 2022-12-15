package com.dongjiaqiang.jvm.dsl.core.statement

import com.dongjiaqiang.jvm.dsl.core.`type`.{ClazzType, DslType}

import scala.collection.mutable.ArrayBuffer

sealed trait Def extends Statement {

}

/**
 * field def
 * @param name field name
 * @param dslType field type
 * @param volatile is volatile
 */
case class FieldDef (name:String, dslType: DslType, volatile:Boolean = false) extends Def

///**
// * local var def
// * @param name
// * @param dslType
// */
//case class LocalVarDef(name:String, dslType: DslType) extends Def

/**
 * method def
 * @param name method name
 * @param params method params
 * @param returnType method return type
 * @param throws method throws
 * @param body method body
 */
case class MethodDef(name:String,
                     params:List[FieldDef],
                     returnType:DslType,
                     throws:ArrayBuffer[ClazzType],
                     body:BlockExpression) extends Def

/**
 * class def
 * @param name class name
 * @param fields fields
 * @param methods methods
 */
case class ClassDef(name:String,fields:List[FieldDef],methods:List[MethodDef]) extends Def


/**
 * program def
 * @param fields fields
 * @param methods methods
 * @param classes classes
 */
case class ProgramDef(fields:List[FieldDef],methods:List[MethodDef],classes:List[ClassDef]) extends Def

