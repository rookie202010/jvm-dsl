package com.dongjiaqiang.jvm.dsl.java.api.codes;

import com.dongjiaqiang.jvm.dsl.java.api.lambda.consumer._1_Consumer;
import com.dongjiaqiang.jvm.dsl.java.api.lambda.consumer._2_Consumer;
import com.dongjiaqiang.jvm.dsl.java.api.lambda.consumer._Runnable;
import com.dongjiaqiang.jvm.dsl.java.api.lambda.function._2_Function;
import com.dongjiaqiang.jvm.dsl.java.api.lambda.function._IntBinaryOperator;
import com.dongjiaqiang.jvm.dsl.java.api.lambda.predicate._1_Predicate;
import com.dongjiaqiang.jvm.dsl.java.api.lambda.predicate._2_Predicate;
import com.dongjiaqiang.jvm.dsl.java.api.lambda.supplier._1_Supplier;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class _SYS_GEN_CODES {


    public static final String CLAZZ_NAME = " _SYS_GEN_CODES";

    public static <T> CompletableFuture<T> supplyAsync(_1_Supplier<T> supplier, Executor executor) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return supplier.get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }

    public static <T> CompletableFuture<T> supplyAsync(_1_Supplier<T> supplier) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return supplier.get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static CompletableFuture<Void> runAsync(_Runnable runnable) {
        return CompletableFuture.runAsync(() -> {
            try {
                runnable.run();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static CompletableFuture<Void> runAsync(_Runnable runnable, Executor executor) {
        return CompletableFuture.runAsync(() -> {
            try {
                runnable.run();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }


    //int methods
    public static List<Integer> range(int start, int end, boolean includeEnd, int sep) {
        List<Integer> range = new ArrayList<>();
        int i = start;
        while (i < end) {
            range.add(i);
            i += sep;
        }
        if (includeEnd && i == end) {
            range.add(end);
        }
        return range;
    }

    public static List<Integer> to(int start, int end) {
        return range(start, end, false, 1);
    }

    public static List<Integer> to(int start, int end, int sep) {
        return range(start, end, false, sep);
    }

    public static List<Integer> until(int start, int end) {
        return range(start, end, true, 1);
    }

    public static List<Integer> until(int start, int end, int sep) {
        return range(start, end, true, sep);
    }

    //long methods
    public static List<Long> range(long start, long end, boolean includeEnd, long sep) {
        List<Long> range = new ArrayList<>();
        long i = start;
        while (i < end) {
            range.add(i);
            i += sep;
        }
        if (includeEnd && i == end) {
            range.add(end);
        }
        return range;
    }

    public static List<Long> to(long start, long end) {
        return range(start, end, false, 1);
    }

    public static List<Long> to(long start, long end, long sep) {
        return range(start, end, false, sep);
    }

    public static List<Long> until(long start, long end) {
        return range(start, end, true, 1);
    }

    public static List<Long> until(long start, long end, long sep) {
        return range(start, end, true, sep);
    }


    //string methods

    public static String mkString(String str, String sep) {
        StringBuilder sb = new StringBuilder();
        char[] cs = str.toCharArray();
        for (int i = 0; i < cs.length; i++) {
            sb.append(cs[i]);
            if (i < cs.length - 1) {
                sb.append(sep);
            }
        }
        return sb.toString();
    }

    public static Optional<Character> headOption(String str) {
        return str.isEmpty() ? Optional.empty() : Optional.of(str.charAt(0));
    }

    public static Optional<Character> lastOption(String str) {
        return str.isEmpty() ? Optional.empty() : Optional.of(str.charAt(str.length() - 1));
    }

    public static char last(String str) {
        return str.charAt(str.length() - 1);
    }

    public static String takeWhile(String str, _1_Predicate<Character> predicate) throws Exception {
        StringBuilder sb = new StringBuilder();
        for (char ch : str.toCharArray()) {
            if (predicate.test(ch)) {
                sb.append(ch);
            } else {
                return sb.toString();
            }
        }
        return sb.toString();
    }

    public static String dropWhile(String str, _1_Predicate<Character> predicate) throws Exception {
        StringBuilder sb = new StringBuilder();
        boolean pass = false;
        for (char ch : str.toCharArray()) {
            if (!pass) {
                if (predicate.test(ch)) {
                    pass = true;
                }
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }


    //collection methods

    public static <E> List<E> takeWhile(List<E> oldC, _1_Predicate<E> predicate) throws Exception {
        List<E> newC = new ArrayList<>();
        for (E e : oldC) {
            if (predicate.test(e)) {
                newC.add(e);
            } else {
                return newC;
            }
        }
        return newC;
    }

    public static <E> List<E> dropWhile(List<E> oldC, _1_Predicate<E> predicate) throws Exception {
        List<E> newC = new ArrayList<>();
        boolean pass = false;
        for (E e : oldC) {
            if (!pass) {
                if (predicate.test(e)) {
                    pass = true;
                }
            } else {
                newC.add(e);
            }
        }
        return newC;
    }

    public static <E> E head(Collection<E> c){
        return c.iterator().next();
    }

    public static <E> Optional<E> headOption(Collection<E> c){
        return c.isEmpty()?Optional.empty():Optional.of(c.iterator().next());
    }

    public static <E> String mkString(Collection<E> c, String sep, _2_Predicate<E, StringBuilder> consumer) throws Exception {
        StringBuilder sb = new StringBuilder();
        Iterator<E> iterator = c.iterator();
        while (iterator.hasNext()) {
            if (consumer.test(iterator.next(), sb) && iterator.hasNext()) {
                sb.append(sep);
            }
        }
        return sb.toString();
    }

    public static <E> String mkString(Collection<E> c, String sep) throws Exception {
        StringBuilder sb = new StringBuilder();
        Iterator<E> iterator = c.iterator();
        while (iterator.hasNext()) {
            sb.append(iterator.next().toString());
            if (iterator.hasNext()) {
                sb.append(sep);
            }
        }
        return sb.toString();
    }

    public static <E> E reduce(Collection<E> c,_1_Supplier<E> seed,_2_Function<E,E,E> function) throws Exception{
        E result = seed.get();
        for(E e:c){
            result = function.apply(result,e);
        }
        return result;
    }

    public static <E> List<E> toList(Collection<E> c,_2_Consumer<E,List<E>> consumer ) throws Exception{
        List<E> list = new ArrayList<>();
        for(E e:c){
            consumer.accept(e,list);
        }
        return list;
    }

    public static <E> Optional<E> reduce(Collection<E> c,_2_Function<E,E,E> function) throws Exception{
        if (c.isEmpty()) {
            return Optional.empty();
        } else {
            E result;
            Iterator<E> iterator = c.iterator();
            result = iterator.next();
            while (iterator.hasNext()) {
                result = function.apply(result,iterator.next());
            }
            return Optional.of(result);
        }
    }

    public static Optional<Integer> reduce(Collection<Integer> c, _IntBinaryOperator intBinaryOperator) throws Exception {
        if (c.isEmpty()) {
            return Optional.empty();
        } else {
            int result;
            Iterator<Integer> iterator = c.iterator();
            result = iterator.next();
            while (iterator.hasNext()) {
                result = intBinaryOperator.applyAsInt(result,iterator.next());
            }
            return Optional.of(result);
        }
    }

    public static int reduce(Collection<Integer> c, int seed, _IntBinaryOperator intBinaryOperator) throws Exception {
        int result = seed;
        for (Integer integer : c) {
            result = intBinaryOperator.applyAsInt(result, integer);
        }
        return result;
    }

    public static <E> void foreach(Collection<E> c, _1_Consumer<E> consumer) throws Exception{
        for(E e:c){
            consumer.accept(e);
        }
    }


    //map methods



}
