package com.dongjiaqiang.jvm.dsl.java.core;


import com.dongjiaqiang.jvm.dsl.java.api.codes.*;
import com.dongjiaqiang.jvm.dsl.java.api.lambda.function._1_Function;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class A<T> {
    public static String foo() throws Exception {
        String[] keys = new String[]{"xx", "11"};
        Integer[] values = new Integer[]{1, 2};
        //Map<String,Integer> map = CodeUtils.ofMap(keys,values);
        // System.out.println(map.size());
        return "";
    }


    public static void foo(_1_Function<Integer, Integer> function) {

    }

    public static void bar(List<List<A<Integer>>> xx) {

    }

    public static Object foo(Object x) {
        return x;
    }
}


class B<T> extends A<T> {

    public T b;

    public B(T b) {
        this.b = b;
    }

    public static int m(){
        try{
            return 1;
        }catch (Exception e){
            return 2;
        }finally {

        }
    }
}

class XX{
    private int i = 0;

    private _1_Function<Integer,Integer> function = new _1_Function<Integer, Integer>() {
        @Override
        public Integer apply(Integer integer) throws Exception {
            if(i>10){
                i++;
            }
            return i;
        }
    };
}

class Super{

}
class Super1 extends Super{}

class Supper2 extends Super1{}

interface FF{}
interface SS{}



class AA implements FF,SS{}
public class Example {
//    public static void main(String[] args) {
//        Object[] longs = _SYS_GEN_CODES.ofList(1L,2L).toArray(new Long[]{});
//        System.out.println((Long[])longs);
//    }

    public  static int method(int x,int y) throws Exception

    {

        Long l = 10L;
        byte b = 1;
        l = l+b;

        java.util.Optional<Long> opp = java.util.Optional.ofNullable(10l);
        long opp1 = ((Long)(

                _SYS_OPTION_CODES.map(opp,
                                new com.dongjiaqiang.jvm.dsl.java.api.lambda.function._1_Function<Object,Object>(){
                                    @Override
                                    public Object apply(Object i) throws Exception

                                    {
                                        java.util.Optional<Integer> opx = java.util.Optional.ofNullable(5);
                                        return ((Long)(i)) + 10 + ((((Integer)(
                                                opx.get()
                                        )))).longValue();
                                    }

                                }
                        )
                        .orElse(100l)
        ));
        return 10;
    }


    public synchronized boolean match(int i){
            return true;
    }

    public static void main1(String[] args) throws Exception {

        System.out.println(method(1,2));

//        java.util.List<Object> list = com.dongjiaqiang.jvm.dsl.java.api.codes.GeneralCodes.ofList(1,2,3);
//        java.util.List<Object> newList =
//                com.dongjiaqiang.jvm.dsl.java.api.codes.ListCodes.map(list,
//                        new com.dongjiaqiang.jvm.dsl.java.api.lambda.function._1_Function<Object,Object>(){
//                            @Override
//                            public Object apply(Object i) throws Exception
//
//                            {
//                                return ((Integer)(i)) + 1;
//                            }
//
//                        }
//                )
//                ;
//
//        CompletableFuture<Integer> completableFuture = new CompletableFuture<>();
//
//
//        System.out.println(method(1,2));
//        Optional<String> optional = Optional.of("xx");
//
//
//
//       // List<Integer> list = new ArrayList<>();
//        list.add(12);
//      //  int x = list.get(0)+10;
////        int[][][] ii = null;
////       int[][] xx =  ArrayUtils.flattenArray(ii);
////        ArrayUtils.flatMapArray(new Integer[]{1, 2, 3}, new _1_Function<Integer, Integer[]>() {
////            @Override
////            public Integer[] apply(Integer integer) throws Exception {
////                return new Integer[]{1,2,3};
////            }
////        });
////
////        Tuple2<Integer,Integer>[] tuple2 = ArrayUtils.zipWithIndex(new Integer[]{1,2,3});
////        Success<Long> success = null;
////        Success<Integer> success1 = null;
////        final Success<? extends Number>[] successes = CodeUtils.ofArray(success1, success);
////
////        Consumer<String> consumer = null;
////
////        Consumer<? extends CharSequence> consumer1 = consumer;
////        consumer1.accept("xx");
////
////        Function<CharSequence,String> function = null;
////
////        Function<? super String,? extends CharSequence> function1 = function;
////        function1.apply("x");
////
////
////       List<Function<? super CharSequence,? extends Object>> list =  CodeUtils.ofList(function1,function);
////
////       list.get(0).apply()
////
////        _1_Consumer<FF> consumer = null;
////        _1_Consumer<SS> consumer1 = null;
////
////        _1_Consumer<? super AA> consumer2 = consumer;
////
////        consumer2.accept(new AA());
////
////        final List<_1_Consumer<? super AA>> consumers = CodeUtils.ofList(consumer1, consumer);
////
////        consumers.get(0).accept(new AA());
////
////        int x = 'x';
////
////
////        List<Success<? extends Object>> ofList= CodeUtils.ofList(x,y);
////
////
////        Either<Integer,? extends CharSequence > either = new Left<Integer,String>(10);
////        Either<Integer,? extends CharSequence> either1 = new Right<>("x");
////
////     //   Either<? extends CharSequence,? extends CharSequence> either2 = either1;
////
////        List<Either<Integer,? extends CharSequence>> list = CodeUtils.ofList(either1,either);
////
////
////
////        Tuple2<String ,Integer> xx = new Tuple2<String,Integer>("x",1);
////        Tuple2<Integer,String > xx1 = new Tuple2<>(1,"x");
////
////        List<Tuple2<? extends Object,? extends Object>> list1 = CodeUtils.ofList(xx,xx1);
////
////       Try<Try<Integer>> tryTry = new Success<>(new Success<>(10));
////
////
////       Try<? extends Try<? extends Object>> tryTry1 = tryTry;
////
////        _1_Function<String,CharSequence> function = new _1_Function<String, CharSequence>() {
////            @Override
////            public CharSequence apply(String s) throws Exception {
////                return "x";
////            }
////        };
////
////        _1_Function<? extends CharSequence,? super CharSequence> function1 = function;
////
////
////        class B {
////
////        }
////
////        class C extends B{
////
////        }
////
////        class D extends C{
////
////        }
//
//    //    B[] bs = CodeUtils.ofArray(new B(),new B());
//
//    //    final B[][] bs1 = CodeUtils.ofArray(CodeUtils.ofArray(new D(), new C()),CodeUtils.ofArray(new D(), new B()));
//
////        _LongConsumer longConsumer = new _LongConsumer(){
////            @Override
////            public void accept(long value) throws Exception {
////                System.out.println(value);
////            }
////        };
////        _IntFunction<Long> intFunction  = new _IntFunction<Long>(){
////            @Override
////            public Long apply(int value) throws Exception {
////                return Long.valueOf(value);
////            }
////        };
//
//        /**
//         *
//         * a.map(i=>i+1).filter(i=>i!=1).map(i=>i.toString()).mkString(",")
//         *
//         *
//         */
//
//        _1_Function<_1_Function<String,Long>,String> function2 = null;
//
//        _1_Function<String,String> function4 = new _1_Function<String, String>() {
//            @Override
//            public String apply(String str) throws Exception {
//                return null;
//            }
//        };
//
//        _1_Function<? super String ,? extends String> function11 =function4;
//
//
//
//      function11.apply("xx");
//
//        function4.apply(new StringBuilder().toString());
//
//        _1_Function<_1_Function<StringBuilder,Long>,String> function3 = null;
//
//       List< _1_Function<? extends _1_Function<? extends CharSequence,Long>,? extends CharSequence>> list2 =  GeneralCodes.ofList(function3,function2);
//
//
//        System.out.println(GeneralCodes.mkString(GeneralCodes.ofList(1,2,3,4),"#",new _2_Predicate<Integer,StringBuilder>(){
//
//           @Override
//           public boolean test(Integer integer,StringBuilder stringBuilder) throws Exception {
//
//
//               _1_Function<Integer,Integer> function1 = new _1_Function<Integer, Integer>() {
//                   @Override
//                   public Integer apply(Integer integer) throws Exception {
//                       return integer+1;
//                   }
//               };
//
//
//               _1_Predicate<Integer> predicate = new _1_Predicate<Integer>(){
//                   @Override
//                   public boolean test(Integer integer) throws Exception {
//                       return integer!=2;
//                   }
//               };
//
//               _1_Function<Integer,String> function2 = new _1_Function<Integer, String>() {
//                   @Override
//                   public String apply(Integer integer) throws Exception {
//                       return String.valueOf(integer);
//                   }
//               };
//
//               Integer v1 = function1.apply(integer);
//               boolean v2 = predicate.test(v1);
//
//               if(v2){
//
//                   String v3 = function2.apply(v1);
//                   stringBuilder.append(v3);
//                   return true;
//               }else{
//                   return false;
//               }
//
//           }
//       }));
//
//
////        CodeUtils.dropWhile("xx", new _1_Predicate<Character>() {
////            @Override
////            public boolean test(Character character) throws Exception {
////                return false;
////            }
////        });
////
////        String.valueOf()
////
////
////        String uu = "xxx";
////
////        Optional<Character> optionalCharacter = uu.isEmpty()?Optional.empty():Optional.of(uu.charAt(0));
////
////       longConsumer.accept( intFunction.apply(10));
//
//
//
//
//
//       // System.out.println();
////       // CodeUtils.ofList(xx.s)
////        List<Integer> xx = new ArrayList<>();
////        new ArrayList<>(xx.subList(0,10));
////        CodeUtils.ofList(xx.subList(xx.size()));
////
////        B<Integer>  x = new B<>(1);
////
////        x instanceof Success
////
////        System.out.println(x.equals(java.util.Optional.empty()));
//
////        {
////            A.foo();
////        }
////
////        _1_Supplier<Integer> supplier = ()->{
////            return 1;
////        };
////        supplier.get();
////        supplier.get();
////
////        Object xx = null;
////
////        List<Integer> dd = (List<Integer>)(xx);
////
////
////     //   CodeUtils.compare(dd,CodeUtils.ofList(new A(),199));
////        //assert 1==1;
////        CodeUtils.ofMap();
////
////        //  A.foo();
////        Object x = null;
////        SimpleCompiler simpleCompiler = new SimpleCompiler();
////        simpleCompiler.setParentClassLoader(CodeUtils.class.getClassLoader());
////
////        _1_Supplier<Integer> supplier1 = new _1_Supplier<Integer>() {
////            @Override
////            public Integer get() throws Exception {
////                return 10;
////            }
////        };
////        supplier1.get();
////
////        simpleCompiler.cook("" +
////                "import java.util.Map;" +
////                "import com.dongjiaqiang.jvm.dsl.java.core.util.CodeUtils;" +
////                "import com.dongjiaqiang.jvm.dsl.java.core.lambda.supplier._1_Supplier;"+
////                "class A{\n" +
////                "    public static void foo() throws Exception{\n" +
////
////
////                "_1_Supplier<Integer> supplier1 = new _1_Supplier<Integer>() {\n" +
////                "            @Override\n" +
////                "            public Integer get() throws Exception {\n" +
////                "                return 10;\n" +
////                "            }\n" +
////                "        };"+
////
////                "        String[] keys = new String[]{\"xx\",\"11\"};\n" +
////                "        Integer[] values = new Integer[]{1,2};\n" +
////                "        System.out.println( supplier1.get());\n" +
////                "    }\n" +
////                "}");
////       Method method = simpleCompiler.getClassLoader().loadClass("A").getMethod("foo");
////       method.setAccessible(true);
////       method.invoke(null);
//
////       new _1_Function<Integer,Integer>(){
////           @Override
////           public Integer apply(Integer a) throws Exception {
////               return null;
////           }
////       };
////
////       boolean xx  = method.equals(Optional.empty());
//
////        new java.util.ArrayList<Integer>();
////
////        Either<Integer, Long> either = new Left<>(12);
////
////        B<Integer> b = new B<>(1);
////
////        int[] x = new int[]{1, 2, 3};
//
//
//        // byte[] x = new byte[]{1,2};
////        Try<A> aTry = new Success<>(new B());
////        Try<A> nTry = aTry.flatMap(a->{
////            return new Success<>(new B());
////        });
////
////  //      java.util.ArrayList<java.util.ArrayList<Double>> x;
////
////
////        A[] a = new A[]{new B()};
//
////        Map<Integer, A[]> map = new HashMap<>();
////        //map.put(1,new HashSet<A>());
////
////        java.util.Optional.ofNullable(1);
////        new Tuple3<>(1, 2, 3);
////
////        int xd = 1 & 2;
////
////        // Object x = CodeUtils.ofArray(1,2,3);
////        // A<Integer> a = new ();
////        //    boolean dd = x instanceof int;
////
////        Object x1 = new A();
////        int x2 = (Integer) x1 + 1;
////
////        boolean a = true;
////        boolean b1 = !a;
////        //  CodeUtils.ofMap()
////
////        xd = 10;
////        A.bar(Collections.singletonList(Arrays.asList(new A<>())));
////
////        int finalXd = xd;
////        _2_Function<Integer, Integer, Integer> function = (a1, b2) -> {
////            return finalXd;
////        };
////
////        function.apply(11, 12);
////
////        Try.apply(() -> {
////            throw new IOException("xx");
////        });
////
////        CodeUtils.supplyAsync(() -> {
////            throw new IOException("");
////        });
////
////        List<Integer> list = CodeUtils.ofList(1);
////        for (int i : list) {
////
////        }
////        Map<Integer, String> map1 = new HashMap<>();
////
////        for (java.util.Map.Entry<Integer, String> entry : map1.entrySet()) {
////            int k = entry.getKey();
////            String v = entry.getValue();
////        }
////        int xxxx = 10;
////
////        do
////        {
////            xxxx+=1;
////        }while (xxxx>20);
////
////        synchronized (list){
////
////        }
////
////        try{
////
////        }catch (Exception e){
////
////        }
////
////        List<Object> xxss = (List<Object>)Thread.currentThread();
////
////        xxss.g
////
////       // xxss.si
////        boolean xs = xxss instanceof java.util.List;
////
////        Object xss = Thread.currentThread();
////
////        List<B<Integer>> dddd = (List<B<Integer>>)(xss);
////
////        int sdd = dddd.get(0).b;
////
////        List<List<Tuple3<Integer,Integer,Object>>> p = new ArrayList<>();
////
////        int xds = ((List<Integer>)(((Tuple3<Integer,Integer,Object>)(p.get(0)))._3)).get(1);
////
////        boolean xdz = !(xxss instanceof List && true ==false);
////
////
////        List xsff = (List)dddd;
////        xsff.size()
//        List<CharSequence> charSequences = GeneralCodes.ofList("xx",new StringBuilder());
//
//
//        List<Integer> objects  =  GeneralCodes.ofList(1,2,3);
//
//        Integer[] objects1 = GeneralCodes.ofArray(1,2,3);
//        Object[] xxx = objects1;
//
//
//
//
//        List<List<Object>> xx = GeneralCodes.ofList(GeneralCodes.ofList(1,2), GeneralCodes.ofList());
    }
}
