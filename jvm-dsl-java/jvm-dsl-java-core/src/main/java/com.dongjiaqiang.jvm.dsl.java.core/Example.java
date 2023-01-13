package com.dongjiaqiang.jvm.dsl.java.core;

import com.dongjiaqiang.jvm.dsl.java.core.extend.Either;
import com.dongjiaqiang.jvm.dsl.java.core.extend.Left;
import com.dongjiaqiang.jvm.dsl.java.core.extend.Try;
import com.dongjiaqiang.jvm.dsl.java.core.lambda.function._1_Function;
import com.dongjiaqiang.jvm.dsl.java.core.lambda.function._2_Function;
import com.dongjiaqiang.jvm.dsl.java.core.tuple.Tuple3;

import java.io.IOException;
import java.util.*;


class A<T> {
    public static void foo() throws Exception {
        String[] keys = new String[]{"xx", "11"};
        Integer[] values = new Integer[]{1, 2};
        //Map<String,Integer> map = CodeUtils.ofMap(keys,values);
        // System.out.println(map.size());
    }

    public static void foo(_1_Function<Integer, Integer> function) {

    }

    public static void bar(List<List<A<Integer>>> xx) {

    }
}

class B<T> extends A<T> {

    public T b;

    public B(T b) {
        this.b = b;
    }
}

public class Example {

    public static void main(String[] args) throws Exception {

        //assert 1==1;
        CodeUtils.ofMap();

        //  A.foo();
//        Object x = null;
//        SimpleCompiler simpleCompiler = new SimpleCompiler();
//        simpleCompiler.setParentClassLoader(CodeUtils.class.getClassLoader());
//
//        simpleCompiler.cook("" +
//                "import java.util.Map;" +
//                "import com.dongjiaqiang.jvm.dsl.java.core.CollectionUtils;" +
//                "class A{\n" +
//                "    public static void foo(){\n" +
//                "        String[] keys = new String[]{\"xx\",\"11\"};\n" +
//                "        Integer[] values = new Integer[]{1,2};\n" +
//                "        Map<String,Integer> map = CollectionUtils.ofMap(keys,values);\n" +
//                "        System.out.println(map.size());\n" +
//                "    }\n" +
//                "}");
//       Method method = simpleCompiler.getClassLoader().loadClass("A").getMethod("foo");
//       method.setAccessible(true);
//       method.invoke(null);
//
        new java.util.ArrayList<Integer>();

        Either<Integer, Long> either = new Left<>(12);

        B<Integer> b = new B<>(1);

        int[] x = new int[]{1, 2, 3};


        // byte[] x = new byte[]{1,2};
//        Try<A> aTry = new Success<>(new B());
//        Try<A> nTry = aTry.flatMap(a->{
//            return new Success<>(new B());
//        });
//
//  //      java.util.ArrayList<java.util.ArrayList<Double>> x;
//
//
//        A[] a = new A[]{new B()};

        Map<Integer, A[]> map = new HashMap<>();
        //map.put(1,new HashSet<A>());

        java.util.Optional.ofNullable(1);
        new Tuple3<>(1, 2, 3);

        int xd = 1 & 2;

        // Object x = CodeUtils.ofArray(1,2,3);
        // A<Integer> a = new ();
        //    boolean dd = x instanceof int;

        Object x1 = new A();
        int x2 = (Integer) x1 + 1;

        boolean a = true;
        boolean b1 = !a;
        //  CodeUtils.ofMap()

        xd = 10;
        A.bar(Collections.singletonList(Arrays.asList(new A<>())));

        int finalXd = xd;
        _2_Function<Integer, Integer, Integer> function = (a1, b2) -> {
            return finalXd;
        };

        function.apply(11, 12);

        Try.apply(() -> {
            throw new IOException("xx");
        });

        CodeUtils.supplyAsync(() -> {
            throw new IOException("");
        });

        List<Integer> list = CodeUtils.ofList(1);
        for (int i : list) {

        }
        Map<Integer, String> map1 = new HashMap<>();


    }
}
