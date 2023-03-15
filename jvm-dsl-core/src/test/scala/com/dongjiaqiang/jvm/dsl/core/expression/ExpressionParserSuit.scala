package com.dongjiaqiang.jvm.dsl.core.expression

import com.dongjiaqiang.jvm.dsl.api
import com.dongjiaqiang.jvm.dsl.api.`type`._
import com.dongjiaqiang.jvm.dsl.api.expression.`var`.{Assign, VarRef}
import com.dongjiaqiang.jvm.dsl.api.expression.binary.{Add, Lt, NotEq}
import com.dongjiaqiang.jvm.dsl.api.expression.block._
import com.dongjiaqiang.jvm.dsl.api.expression.call.{FuncCallChain, MethodCall, VarCall}
import com.dongjiaqiang.jvm.dsl.api.expression.literal.TupleLiteral
import com.dongjiaqiang.jvm.dsl.api.expression.statement.Return
import com.dongjiaqiang.jvm.dsl.api.expression._
import com.dongjiaqiang.jvm.dsl.core.program.Program
import com.dongjiaqiang.jvm.dsl.core.symbol.generateProgramScope
import org.scalatest.funsuite.AnyFunSuite

import scala.collection.mutable.{ListMap ⇒ MutableMap}
import scala.language.postfixOps

class ExpressionParserSuit extends AnyFunSuite {


  test( "define program one" ) {
    val input =
      """
        |program {
        |                  Int=>Int lambda = a=>{
        |                    while(i<10){
        |                      i=i+1;
        |                    }
        |                    return a+1+foo1(j=>{
        |                     return j.toInt() + foo1(z=>{return z.toInt()+122;},"121L"); },"12.1f")+foo1(k=>{
        |                      return k.toLong();},"2121L");
        |                  };
        |                  Int i = 10;
        |                  def foo1(String=>String format,String str)=Any{
        |
        |                       Int uu = str match{
        |                        case "dd"=>{ return 1;}
        |                        case 12=>{ return 2;}
        |                        case "yy"=>{ return 3;}
        |                        default=>{return 5;}
        |                      };
        |
        |                      return uu;
        |                  }
        |                  def foo2(String format,String str)=String{
        |                            try{
        |                              return "1";
        |                            }catch(Ex ex){
        |                              return ex.a;
        |                            }
        |                        }
        |                  def foo()=Int{
        |                 i  match{
        |
        |                                  case 12=>{ return 2;}
        |                                  case (a1,a2,a3)=>{ return a1+a2+a3;}
        |                                  case i1::i2::i3=>{ return i2;}
        |                                  default=>{
        |                                    return 2200;
        |                                  }
        |                                };
        |
        |                      Int j = 2;
        |
        |                      for(Int k=0;k<100;k=k+1){
        |                        lambda.apply(j);
        |                      }
        |
        |                      (Int,Long,Int)=>(Int,Long) lambda1 = (a,b,c)=>{
        |
        |                          Int sum=0;
        |
        |                          (Int,Int)=>Int lambda2 = (a,b)=>{
        |                              return a+b;
        |                          };
        |
        |                          for(Int a=0;a<100;a=a+1){
        |                              sum=sum+lambda2.apply(a,c);
        |                          }
        |
        |                          return (sum,b);
        |
        |                      };
        |
        |                      return lambda1.apply(1,2,3);
        |                  }
        |              }
        |""".stripMargin

    val program = Program( generateProgramScope( input ), assigned = MutableMap( ), classes = MutableMap( ), methods = MutableMap( ) )
    val programOptimize = Program( generateProgramScope( input ), assigned = MutableMap( ), classes = MutableMap( ), methods = MutableMap( ) )

    val foo1 = program.programScope.methods.get("foo1")
    program assign("lambda",{
      Lambda(Array("a"),(program lambdaBlock() expression(scope⇒{
        WhileCondition(Lt(scope.varRef("i"),10 int))
      }) whileBlock() expression(scope⇒{
        Assign(scope.varRef("i"),Add(scope.varRef("i"),1 int))
      }) belongBlock() expression(scope⇒ {
        Return( Some(binary.Add( binary.Add( binary.Add( scope.varRef( "a" ), 1 int ),
          MethodCall(
            foo1,"foo1",Array(
                Lambda(Array("j"),(scope.lambdaBlock() expression(scope⇒ {
                  Return(Some(binary.Add(VarCall(scope.varRef("j"),"toInt",Array()),MethodCall(
                    foo1,"foo1",Array(Lambda(Array("z"),(scope.lambdaBlock() expression(scope⇒Return(Some(binary.Add(call.VarCall(scope.varRef("z"),"toInt",Array()),122 int))))).block),"\"121L\"" str)
                  ) )))
                })).block)
              ,"\"12.1f\"" str
            )
          )),MethodCall(
          foo1,"foo1",Array(

            Lambda(Array("k"),(scope.lambdaBlock() expression(scope⇒{ Return(Some(call.VarCall(scope.varRef("k"),"toLong",Array())))})).block)

          ,"\"2121L\"" str)
        ))))})).block)})


      program assign("i",10 int)

      program method(
        "foo1"
      ) bodyBlock() expression(scope⇒{
        val case1 = scope.lambdaBlock() expression(_⇒{
          Return(Some(1 int))
        })
        val case2 = scope.lambdaBlock() expression(_⇒{
          Return(Some(2 int))
        })
        val case3 = scope.lambdaBlock() expression(_⇒{
          Return(Some(3 int))
        })
        val default = scope.lambdaBlock() expression(_⇒{
          Return(Some(5 int))
        })
        val matchCase =  MatchCase(scope.varRef("str"),Array(("\"dd\"" str,case1.block),(12 int,case2.block),("\"yy\"" str,case3.block)),Some(default.block))
        scope.varDef("uu",IntType,Some(matchCase))
      }) expression(scope⇒{
        Return(Some(scope.varRef("uu")))
      })

    program method(
      "foo2"
    ) bodyBlock() tryBlock() expression(_⇒Return(Some("\"1\"" str))) belongBlock() expression(_⇒{
        CatchParameter("ex",ClazzType("Ex"))
    }) catchBlock() expression(scope⇒{
      Return(Some(scope.varRef("ex","a")))
    })

    program method(
      "foo"
    ) bodyBlock() expression(scope⇒{

      val case1 = scope.lambdaBlock().expression(_⇒Return(Some(2 int)))
      val case2 = scope.lambdaBlock().expression(s⇒Return(Some(binary.Add(binary.Add(s.varRef("a1"),s.varRef("a2")),s.varRef("a3")))))
      val case3 = scope.lambdaBlock().expression(s⇒Return(Some(s.varRef("i2"))))
      val default = scope.lambdaBlock().expression(_⇒Return(Some(2200 int)))

      block.MatchCase(scope.varRef("i"),Array((12 int,case1.block),(MatchTuple(
          Array(Right("a1"),Right("a2"),Right("a3"))
      ),case2.block),(MatchHead(Array(Right("i1"),Right("i2")),Right("i3")),case3.block)),Some(default.block))

    }) updateVarDef("j",IntType,Some(2 int)) forLoopBlock(b⇒b.varDef("k",IntType,Some(0 int)),b⇒
      binary.Lt(b.varRef("k"),100 int), b⇒api.expression.`var`.Assign(b.varRef("k"),binary.Add(b.varRef("k"),1 int))) expression(scope⇒{

      call.VarCall(scope.varRef("lambda"),"apply",Array(scope.varRef("j")))
    }) belongBlock() expression(scope⇒{

      val body = Lambda(Array("a","b","c"),(scope.lambdaBlock() updateVarDef("sum",IntType,Some(0 int)) expression(scope⇒{

        scope.varDef("lambda2",LambdaType(Some(TupleType(Array(IntType,IntType))),IntType),
          Some(Lambda(Array("a","b"),(scope.lambdaBlock() expression(scope⇒Return(Some(binary.Add(scope.varRef("a"),scope.varRef("b")))))).block)))
      }) forLoopBlock(
        b⇒b.varDef("a",IntType,Some(0 int)),
        b⇒binary.Lt(b.varRef("a"),100 int),
        b⇒api.expression.`var`.Assign(b.varRef("a"),binary.Add(b.varRef("a"),1 int))) expression(scope⇒

            api.expression.`var`.Assign(scope.varRef("sum"),binary.Add(scope.varRef("sum"),
              call.VarCall(scope.varRef("lambda2"),"apply",Array(scope.varRef("a"),scope.varRef("c")))))


      ) belongBlock()  expression(scope⇒{

        Return(Some(new TupleLiteral(Array(scope.varRef("sum"),scope.varRef("b")),TupleType(Array(UnResolvedType,UnResolvedType)))))

      })).block)

      scope.varDef("lambda1",LambdaType(Some(TupleType(Array(IntType,LongType,IntType))),TupleType(Array(IntType,LongType))),Some(body))

    }) expression(scope⇒{
      Return(Some(call.VarCall(scope.varRef("lambda1"),"apply",Array(1 int,2 int,3 int))))
    })


    val generateP = generateProgram( input )
    assert( generateP == program )

  }

  test( "define xx empty program" ) {
    val input =
      """program {

         import com.xx.C;

              class A(Int a1,Int b1){}
                  def xx(C c)=Bool{

                    return ;

                    return c.d.b.f.map(k=>{

                    (Int,Long)=>Int lambda = (a,d)=>{ return k+d+a;};

                    return lambda.apply(k,10L).toString();}).a.b.map(i=>{ return i+1;}).filter(j=>{ return j!=1;}).max()+10;
                  }
                    def foo1(String=>String format,String str)=Any{

                          C c;
                         Int x = str match{
                          case ["dd",A(dd,[1,1,x])]=>{ return 1;}
                          case Left(12)=>{ return 2;}
                          case Array(1,2,None)=>{ return 3;}
                          default => {

                              Int d = 100;
                              while(xx(c)){
                                  d=d+10;
                              }

                              str match {
                                  case 10=>{ return 10;}
                              };

                              return d;
                          }
                        };


                    }

                   }
          """

    val programScope = generateProgramScope( input )
    val program = Program( programScope, assigned = MutableMap( ), classes = MutableMap( ), methods = MutableMap( ) )

    program.clazz("A")
    program method("xx") bodyBlock() expression(_⇒{
      Return(None)
    }) expression(scope⇒{

      Return(Some(binary.Add(FuncCallChain(call.VarCall(scope.varRef("c","d","b","f"),"map",Array(Lambda(Array("k"),(scope lambdaBlock() expression (scope ⇒ {
        scope.varDef( "lambda", LambdaType( Some(TupleType( Array( IntType, LongType ) )), IntType ),
          Some( Lambda( Array( "a", "d" ), (scope.lambdaBlock( ) expression (scope ⇒ {
            Return( Some( binary.Add( binary.Add( scope.varRef( "k" ), scope.varRef( "d" ) ), scope.varRef( "a" ) ) ) )
          })).block ) ) )
      }) expression (scope ⇒ {
        Return( Some( FuncCallChain( call.VarCall( scope.varRef( "lambda" ), "apply", Array( scope.varRef( "k" ), 10 long ) ), List( MethodCall( None, "toString", Array( ) ) ) ) ) )
      })).block))),

        List(

          VarRef(List("a","b"),MutableMap(),None),

        MethodCall(None,"map",Array(
        Lambda(Array("i"), (scope.lambdaBlock() expression(s⇒Return(Some(binary.Add(s.varRef("i"),1 int))))).block),
      )),
          MethodCall(None,"filter",Array(
            Lambda(Array("j"),((scope.lambdaBlock() expression(s⇒Return(Some(NotEq(s.varRef("j"),1 int)))))).block))
          ),
          MethodCall(None,"max",Array())

        )),10 int)))
    })

    program.method("foo1") bodyBlock() updateVarDef("c",ClazzType("C"),None) expression(scope⇒{

      val caseCondition1 = MatchList(Array(Left("\"dd\"" str),Left(MatchClass(DefinitionClazzType("A",programScope.classes("A")),Array(Right("dd"),Left(MatchList(Array(Left(1 int),Left(1 int),Right("x")))))))))
      val caseBlock1 = scope lambdaBlock() expression (_⇒Return(Some(1 int)))

      val caseCondition2 = block.MatchClass(LeftType(UnResolvedType),Array(Left(12 int)))
      val caseBlock2 = scope lambdaBlock() expression (_⇒Return(Some(2 int)))

      val caseCondition3 = block.MatchClass(ClazzType("Array"),Array(Left(1 int),Left(2 int),Right("None")))
      val caseBlock3 = scope lambdaBlock() expression(_⇒Return(Some(3 int)))

      val default = scope lambdaBlock() updateVarDef("d",IntType,Some(100 int)) expression(s⇒{
        WhileCondition(MethodCall(program.programScope.methods.get("xx"),"xx",Array(s.varRef("c"))))
      }) whileBlock() expression(s⇒{
        api.expression.`var`.Assign(s.varRef("d"),binary.Add(s.varRef("d"),10 int))
      }) belongBlock() expression(s⇒{

        val case1 = s lambdaBlock() expression(_⇒Return(Some(10 int)))
        block.MatchCase(s.varRef("str"),Array((10 int,case1.block)),None)

      }) expression(s⇒Return(Some(s.varRef("d"))))


      scope.varDef("x",IntType,Some(block.MatchCase(scope.varRef("str"),Array(((caseCondition1,caseBlock1.block)),(caseCondition2,caseBlock2.block),(caseCondition3,caseBlock3.block)),Some(default.block))))

    })


    val generateP = generateProgram( input )
    assert( generateP == program )


  }
}
