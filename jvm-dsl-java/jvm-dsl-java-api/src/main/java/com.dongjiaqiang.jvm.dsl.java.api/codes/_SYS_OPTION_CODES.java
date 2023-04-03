package com.dongjiaqiang.jvm.dsl.java.api.codes;

import com.dongjiaqiang.jvm.dsl.java.api.lambda.consumer._1_Consumer;
import com.dongjiaqiang.jvm.dsl.java.api.lambda.function._1_Function;
import com.dongjiaqiang.jvm.dsl.java.api.lambda.function._2_Function;
import com.dongjiaqiang.jvm.dsl.java.api.lambda.predicate._1_Predicate;
import com.dongjiaqiang.jvm.dsl.java.api.lambda.supplier._1_Supplier;
import com.dongjiaqiang.jvm.dsl.java.api.tuple.Tuple2;

import java.util.*;

public class _SYS_OPTION_CODES {

    public static final String CLAZZ_NAME = "_SYS_OPTION_CODES";

    public static <T,K> Optional<K> map(Optional<T> optional, _1_Function<? super T,? extends K> mapper) throws Exception{
        if(optional.isPresent()){
            return Optional.of(mapper.apply(optional.get()));
        }else {
            return Optional.empty();
        }
    }

    public static <T> Optional<T> filter(Optional<T> optional, _1_Predicate<? super T> predicate) throws Exception {
        if(optional.isPresent()){
            if(predicate.test(optional.get())){
                return optional;
            }
        }
        return Optional.empty();
    }

    public static <T> Optional<T> filterNot(Optional<T> optional, _1_Predicate<? super T> predicate) throws Exception {
        return filter(optional, new _1_Predicate<T>() {
            @Override
            public boolean test(T t) throws Exception {
                return !predicate.test(t);
            }
        });
    }

    public static <T> void foreach(Optional<T> optional, _1_Consumer<? super T> consumer) throws Exception {
        if(optional.isPresent()){
            consumer.accept(optional.get());
        }
    }

    public static <T> boolean exist(Optional<T> optional,_1_Predicate<? super T> predicate) throws Exception {
        return optional.isPresent() && predicate.test(optional.get());
    }

    public static <T> Optional<T> find(Optional<T> optional,_1_Predicate<? super T> predicate) throws Exception{
        if(exist(optional,predicate)){
            return optional;
        }else{
            return Optional.empty();
        }
    }
    public static <K> Optional<K> flatten(Optional<Optional<K>> optional){
        if (optional.isPresent()) {
            return optional.get();
        } else {
            return Optional.empty();
        }
    }
    public static <T,K> Optional<K> flatMap(Optional<T> optional,_1_Function<? super T,Optional<K>> mapper) throws Exception {
        if(optional.isPresent()){
            return mapper.apply(optional.get());
        }else{
            return Optional.empty();
        }
    }
    public static <K> boolean contains(Optional<K> optional,K k){
        return optional.isPresent() && optional.get().equals(k);
    }

    public static <K> List<Tuple2<K,Integer>> zipWithIndex(Optional<K> optional){
        List<Tuple2<K,Integer>> list = new ArrayList<>();
        if(optional.isPresent()){
            list.add(new Tuple2<>(optional.get(),0));
        }
        return list;
    }


    public static <V> V reduce(Optional<V> optional, _2_Function<? super V,? super V,? extends V> reducer) throws Exception {
        if(optional.isPresent()){
            return optional.get();
        }else {
            throw new UnsupportedOperationException("Empty Reduce");
        }
    }


    public static <V> Optional<V> reduceOption(Optional<V> optional, _2_Function<? super V,? super V,? extends V> reducer) throws Exception {
        return optional;
    }

    public static <V> V getOrElse(Optional<V> optional, _1_Supplier<? extends V> supplier) throws Exception{
        if(optional.isPresent()){
            return optional.get();
        }else{
            return supplier.get();
        }
    }

    public static <V> int length(Optional<V> optional){
        return optional.isPresent()?1:0;
    }
    public static <T> List<T> toList(Optional<T> optional){
        ArrayList<T> arrayList = new ArrayList<>();
        if(optional.isPresent()){
            arrayList.add(optional.get());
        }
        return arrayList;
    }

    public static <T> Set<T> toSet(Optional<T> optional){
        Set<T> set = new HashSet<>();
        if(optional.isPresent()){
            set.add(optional.get());
        }
        return set;
    }

    public static <T> Set<T> toSeqSet(Optional<T> optional){
        Set<T> set = new LinkedHashSet<>();
        if(optional.isPresent()){
            set.add(optional.get());
        }
        return set;
    }


    public static <K,V> Map<K,V> toMap(Optional<Tuple2<K,V>> optional){
        Map<K,V> map = new HashMap<>();
        if(optional.isPresent()){
            Tuple2<K,V> tuple2 = optional.get();
            map.put(tuple2._1,tuple2._2);
        }
        return map;
    }

}
