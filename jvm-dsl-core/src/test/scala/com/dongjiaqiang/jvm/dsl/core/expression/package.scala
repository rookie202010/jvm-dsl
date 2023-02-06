package com.dongjiaqiang.jvm.dsl.core

import com.dongjiaqiang.jvm.dsl.api.`type`.{ClazzType, DslType}
import com.dongjiaqiang.jvm.dsl.api.expression._
import com.dongjiaqiang.jvm.dsl.api.scope._
import com.dongjiaqiang.jvm.dsl.core.program.{Clazz, Method, Program}

import scala.collection.mutable.{ArrayBuffer, ListMap ⇒ MutableMap}

case class BlockWithScope(block: Block,
                          blockScope:BlockScope,
                          abstractMethod:AbstractMethod){
    var parent:BlockWithScope = _
    var childBlockIndex = 0
}

trait AbstractMethod{
    val method:Method

    def varRef(name:String): Option[FieldScope] ={
        method.methodScope.params.get(name)
    }
}

case class ClazzMethod(method:Method,clazz:Clazz) extends AbstractMethod{
  override def varRef(name: String): Option[FieldScope] = {
    super.varRef( name ) match {
      case some: Some[_] ⇒ some
      case None ⇒ clazz.clazzScope.fields.get(name)
    }
  }
}

case class ProgramMethod(method: Method,program: Program) extends AbstractMethod{
  override def varRef(name: String): Option[FieldScope] = {
    super.varRef( name ) match {
      case some:Some[_]⇒some
      case None⇒ program.programScope.fields.get(name)
    }
  }
}

package object expression {

  implicit class EnhanceInt(value:Int){
      def int:IntLiteral={
          new IntLiteral(value)
      }
  }

  implicit class EnhanceFloat(value:Float){
      def float:FloatLiteral={
          new FloatLiteral((value))
      }
  }

  implicit class EnhanceDouble(value:Double){
      def double:DoubleLiteral={
          new DoubleLiteral(value)
      }
  }

  implicit class EnhanceLong(value:Long){
    def long:LongLiteral={
        new LongLiteral(value)
    }
  }

  implicit class EnhanceChar(value:Char){
      def char:CharLiteral={
          new CharLiteral(value)
      }
  }

  implicit class EnhanceBool(value:Boolean){
      def bool:BoolLiteral={
          new BoolLiteral(value)
      }
  }

  implicit class EnhanceString(value:String){
      def str:StringLiteral={
          new StringLiteral(value)
      }
      def clazz(expressions:Expression*):ClazzLiteral={
          new ClazzLiteral(expressions.toArray,ClazzType(value,Array()))
      }
  }

  implicit class EnhanceClazzMethod(clazzMethod:ClazzMethod ) {

    def bodyBlock(): BlockWithScope = {
      BlockWithScope( clazzMethod.method.body, clazzMethod.method.methodScope.blockScope,clazzMethod )
    }

    def belongClazz():Clazz={
          clazzMethod.clazz
    }

  }

  implicit class EnhanceProgramMethod(programMethod: ProgramMethod){

    def bodyBlock():BlockWithScope={
        BlockWithScope(programMethod.method.body,programMethod.method.methodScope.blockScope,programMethod)
    }

    def belongProgram():Program={
        programMethod.program
    }

  }

  implicit class EnhanceClazz(clazz: Clazz){
      def method(name:String,ignoreVarRefResolved:Boolean = false):ClazzMethod={
        val methodScope = clazz.clazzScope.methods(name)
        val method = Method( methodScope, Block( ignoreVarRefResolved = ignoreVarRefResolved ) )
        clazz.methods.put( name, method )
        ClazzMethod(method,clazz)
      }

  }

  implicit class EnhanceBlock(blockWithScope: BlockWithScope){
      def expression(expression:BlockWithScope⇒Expression):BlockWithScope={
          blockWithScope.block.expressions.append(expression.apply(blockWithScope))
          blockWithScope
      }

      def block():BlockWithScope={
          val childBlockScope = blockWithScope.blockScope.childrenScopes(blockWithScope.childBlockIndex)
          val childBlock = Block()
          blockWithScope.childBlockIndex = blockWithScope.childBlockIndex + 1
          blockWithScope.block.expressions.append(childBlock)
          val childBlockWithScope = BlockWithScope(childBlock,childBlockScope,blockWithScope.abstractMethod)
          childBlockWithScope.parent = blockWithScope
          childBlockWithScope
      }

      def belongBlock():BlockWithScope= blockWithScope.parent

      def belongClazzMethod():ClazzMethod = {
        blockWithScope.abstractMethod.asInstanceOf[ClazzMethod]
      }

      def belongProgramMethod():ProgramMethod={
        blockWithScope.abstractMethod.asInstanceOf[ProgramMethod]
      }


      def varDef(name:String,dslType: DslType,assign:Option[Expression]):BlockWithScope={
          blockWithScope.block.expressions.append(LocalVarDef(blockWithScope.blockScope.fields(name),dslType,assign))
          blockWithScope
      }

      def varRef(name:String*):VarRef={
          blockWithScope.blockScope.fields.get(name.head) match {
            case Some(fieldScope)⇒VarRef(name.toList,Some(fieldScope))
            case None⇒
                if(blockWithScope.parent!=null){
                    blockWithScope.varRef(name:_*)
                }else{
                    val methodScope = blockWithScope.blockScope.parentScope.asInstanceOf[MethodScope]
                    methodScope.params.get(name.head) match {
                      case Some(fieldScope)⇒ VarRef(name.toList,Some(fieldScope))
                      case None⇒ blockWithScope.blockScope.topScope match {
                        case clazzScope: ClazzScope⇒
                            VarRef(name.toList,clazzScope.fields.get(name.head))
                        case programScope: ProgramScope⇒
                            VarRef(name.toList,programScope.fields.get(name.head))
                      }
                    }
                }
          }
      }
  }

  implicit class EnhanceProgram(program:Program){

    def assign(name:String,expression:Expression): Program ={
        program.assigned.put(name,expression)
        program
    }

    def varRef(name:String*):VarRef={
        VarRef(name.toList,program.programScope.fields.get(name.head))
    }

    def method(name:String)(ignoreVarRefResolved:Boolean = false):ProgramMethod={
        val methodScope = program.programScope.methods(name)
        val method = Method(methodScope,Block(ArrayBuffer(),ignoreVarRefResolved))
        program.methods.put(name,method)
        ProgramMethod(method, program)
    }

    def clazz(name:String):Clazz={
        val clazzScope = program.programScope.classes(name)
        val clazz = Clazz(clazzScope,MutableMap[String,Method]())
        program.classes.put(name,clazz)
        clazz
    }



  }
}
