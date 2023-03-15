package com.dongjiaqiang.jvm.dsl.core.`type`

import com.dongjiaqiang.jvm.dsl.core.expression.generateProgram
import com.dongjiaqiang.jvm.dsl.core.optimize.{OptimizeDslType, OptimizeExpression}
import org.scalatest.funsuite.AnyFunSuite

class OptimizeDslTypeSuit extends AnyFunSuite{


  test( "define xxx" ) {

    val input =
      """
        |program{
        |
        |   def method1(Int=>Int la,Int i)=Int{
        |       return la.apply(i);
        |   }
        |
        |   def method2(List[Int] a)=Int{
        |       a match{
        |         case a1::a2::tail=> { return a1+a2;}
        |       };
        |   }
        |
        |   def method3((Long,Long)=>Long plus)=Long=>Long{
        |      return i=>{
        |         return plus.apply(200)+i;
        |      };
        |   }
        |
        |   class A(Int a){
        |       def method(Int=>Long b)=Array[Long]{
        |           return new Array(b.apply(a));
        |       }
        |   }
        |
        |
        |   def method(Int a) = (Array[Int],Int,Long)=>Int {
        |
        |       List[Int] iii = [1,2,3,4,5,6];
        |
        |       Int jjj = iii match{
        |           case vv:String=>{
        |               return vv.toInt();
        |           }
        |           case head::two::tail=>{
        |               return head+two+tail.length();
        |           }
        |       };
        |
        |       (List[Long],List[Int],String) nss = ([1,2,3,4],[11],"xx");
        |
        |       nss match{
        |           case (a,b,c)=> { return c.toInt()+b[0];}
        |           case ([1,b],e::f::tail,"xx") => { return b+e+f;}
        |       };
        |
        |      (Int,Long,String) tuple = (12,122L,"xxx");
        |       Long uxx = tuple match{
        |           case (12,a,"xxx")=>{ return a;}
        |       };
        |
        |       List[Option[A]] as1 = [? new A(10),? new A(20)];
        |       Int ass = as1 match{
        |         case [Some(A(10)),Some(A(a))]=> { return a;}
        |       };
        |
        |       Long i=10;
        |
        |       [i,10].map(i=> { return i+10;});
        |
        |       method3((i,j)=>{ return i+j;}).apply(10);
        |
        |       A aa = new A(10);
        |       aa.method( xx=> { return xx+12;}).filter(yy=>{ return yy!=10;});
        |
        |
        |       List[List[A]] list = [[new A(10),new A(20)],[new A(30)]];
        |       list.map( xx => { return xx.length(); });
        |
        |       return (i,j,k)=> {
        |
        |       Array[(Int,Long)] uu = Array((1,2),(3,4))
        |
        |        Array[(Int,Long)] ww = uu.map((jj,kk)=>{ return jj+kk+1;});
        |
        |
        |        Int=>Long xs = k=>{ return k.toLong()+j;};
        |
        |        Long=>Double xss;
        |
        |        xss = dd=>{
        |           return dd.toDouble();
        |        };
        |
        |        method1(i=>{ return i+k;},i);
        |
        |
        |       return i[10]+j+a+k+xs.apply(10);
        |
        |
        |       };
        |   }
        |}
        |
        |""".stripMargin

    val generateP = generateProgram( input )
    val xx = generateP.revise(new OptimizeExpression(generateP.programScope))
    val optimizeProgram = generateP.revise(new OptimizeDslType(generateP.programScope))

    assert(generateP!=null)
  }

}
