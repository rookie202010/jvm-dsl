package com.dongjiaqiang.jvm.dsl.java.core;

import com.dongjiaqiang.jvm.dsl.java.core.lambda.consumer._Runnable;
import com.dongjiaqiang.jvm.dsl.java.core.lambda.supplier._1_Supplier;
import com.dongjiaqiang.jvm.dsl.java.core.tuple.Tuple2;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class CodeUtils {

    @SafeVarargs
    public static <K, V> Map<K, V> ofMap(Tuple2<K, V>... ts) {
        HashMap<K, V> map = new HashMap<>();
        for (Tuple2<K, V> t : ts) {
            map.put(t._1, t._2);
        }
        return map;
    }

    @SafeVarargs
    public static <V> List<V> ofList(V... vs) {
        List<V> list = new ArrayList<>();
        Collections.addAll(list, vs);
        return list;
    }

    @SafeVarargs
    public static <V> Set<V> ofSet(V... vs) {
        Set<V> set = new HashSet<>();
        Collections.addAll(set, vs);
        return set;
    }

    @SafeVarargs
    public static <V> V[] ofArray(V... vs) {
        return vs;
    }


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

}
