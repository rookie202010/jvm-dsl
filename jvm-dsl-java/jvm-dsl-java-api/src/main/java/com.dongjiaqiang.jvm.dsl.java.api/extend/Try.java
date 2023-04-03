package com.dongjiaqiang.jvm.dsl.java.api.extend;

import com.dongjiaqiang.jvm.dsl.java.api.lambda.consumer._1_Consumer;
import com.dongjiaqiang.jvm.dsl.java.api.lambda.function._1_Function;
import com.dongjiaqiang.jvm.dsl.java.api.lambda.function._2_Function;
import com.dongjiaqiang.jvm.dsl.java.api.lambda.predicate._1_Predicate;
import com.dongjiaqiang.jvm.dsl.java.api.lambda.supplier._1_Supplier;
import com.dongjiaqiang.jvm.dsl.java.api.tuple.Tuple2;

import java.util.*;

public interface Try<T> {

    String CLAZZ_NAME = "com.dongjiaqiang.jvm.dsl.java.api.extend.Try";
    boolean success();

    boolean failure();

    <K> Try<K> map(_1_Function<? super T, ? extends K> function);

    static <T> Try<T> flatten(Try<Try<T>> tryTry){
        if(tryTry.success()){
            return ((Success<Try<T>>)tryTry).getValue();
        }else{
            return new Failure<>(tryTry.getException());
        }
    }

    <K> Try<K> flatMap(_1_Function<? super T, Try<K>> function);

    Try<T> filter(_1_Predicate<? super T> predicate) throws Exception;

    default Try<T> filterNot(_1_Predicate<? super T> predicate) throws Exception{
        return filter(new _1_Predicate<T>() {
            @Override
            public boolean test(T t) throws Exception {
                return !predicate.test(t);
            }
        });
    }

    void foreach(_1_Consumer<? super T> consumer) throws Exception;

    boolean exist(_1_Predicate<? super T> predicate) throws Exception;

    Optional<T> find(_1_Predicate<? super T> predicate) throws Exception;

    List<T> toList();

    T[] toArray();

    Set<T> toSet();

    Set<T> toSeqSet();

    static <V extends Comparable<V>> Set<V> toSortedSet(Try<V> tTry){
        Set<V> set = new TreeSet<>();
        if(tTry.success()){
            set.add(((Success<V>)tTry).getValue());
        }
        return set;
    }

    Set<T> toSortedSet(Try<T> tTry,Comparator<T> comparator);

    static <K,V> Map<K,V> toMap(Try<Tuple2<K,V>> t){
        Map<K,V> map = new HashMap<>();
        if(t.success()){
            Success<Tuple2<K,V>> success = (Success<Tuple2<K, V>>) t;
            map.put(success.getValue()._1,success.getValue()._2);
        }
        return map;
    }

    static <K,V> Map<K,V> toSeqMap(Try<Tuple2<K,V>> t){
        Map<K,V> map = new LinkedHashMap<>();
        if(t.success()){
            Success<Tuple2<K,V>> success = (Success<Tuple2<K, V>>) t;
            map.put(success.getValue()._1,success.getValue()._2);
        }
        return map;
    }

    static <K extends Comparable<K>,V> Map<K,V> toSortedMap(Try<Tuple2<K,V>> t){
        Map<K,V> map = new TreeMap<>();
        if(t.success()){
            Success<Tuple2<K,V>> success = (Success<Tuple2<K, V>>) t;
            map.put(success.getValue()._1,success.getValue()._2);
        }
        return map;
    }

    static <K,V> Map<K,V> toSortedMap(Try<Tuple2<K,V>> t,Comparator<K> comparator){
        Map<K,V> map = new TreeMap<>(comparator);
        if(t.success()){
            Success<Tuple2<K,V>> success = (Success<Tuple2<K, V>>) t;
            map.put(success.getValue()._1,success.getValue()._2);
        }
        return map;
    }

    boolean contains(Try<T> tTry, T t);

    //not supported sort

    //not supported reverse

    List<Tuple2<T,Integer>> zipWithIndex();

    T reduce(_2_Function<? super T,? super T,T> reducer);

    default Optional<T> reduceOption(_2_Function<? super T,? super T,T> reducer){
        return toOption();
    }

    default int length(){
        return success()?1:0;
    }

    <U> U fold(_1_Function<? super T, U> function1, _1_Function<Throwable, U> function2) throws Exception;

    Optional<T> toOption();


    T getOrElse(_1_Supplier<T> supplier) throws Exception;

    T orElse(T t) throws Exception;

    Either<Throwable, T> toEither();

    T get() throws Throwable;

    Throwable getException();

    static <T> Try<T> apply(_1_Supplier<T> supplier) {
        try {
            return new Success<>(supplier.get());
        } catch (VirtualMachineError | ThreadDeath | InterruptedException | LinkageError e) {
            throw new RuntimeException(e);
        } catch (Throwable e) {
            return new Failure<>(e);
        }
    }

}

