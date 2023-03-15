package com.dongjiaqiang.jvm.dsl.java.api.util;

import com.dongjiaqiang.jvm.dsl.java.api.lambda.function._1_Function;
import com.dongjiaqiang.jvm.dsl.java.api.lambda.function._2_Function;
import com.dongjiaqiang.jvm.dsl.java.api.lambda.supplier._1_Supplier;
import com.dongjiaqiang.jvm.dsl.java.api.tuple.Tuple2;

import java.util.*;

public class MapMethodUtils {

    public static <K,V,T> List<T> map(Map<K,V> map,
                                      _2_Function<? super K,? super V,? extends T> mapper) throws Exception {
        List<T> list = new ArrayList<>();
        for(Map.Entry<K,V> entry:map.entrySet()){
            list.add(mapper.apply(entry.getKey(),entry.getValue()));
        }
        return list;
    }

    public static <K,V,T> List<T> map(Map<K,V> map,
                                      _1_Function<Tuple2<? super K,? super V>,? extends T> mapper) throws Exception {
        List<T> list = new ArrayList<>();
        for(Map.Entry<K,V> entry:map.entrySet()){
            list.add(mapper.apply(new Tuple2<>(entry.getKey(),entry.getValue())));
        }
        return list;
    }

    public static <K,V> Set<K> keys(Map<K,V> map){
        return map.keySet();
    }

    public static <K,V,NV> Map<K,NV> mapValue(Map<K,V> map,
                                      _1_Function<? super V,? extends NV> mapper,
                                           _1_Supplier<Map<K,NV>> supplier) throws Exception {
        Map<K,NV> newMap = supplier.get();
        for(Map.Entry<K,V> entry:map.entrySet()){
            newMap.put(entry.getKey(),mapper.apply(entry.getValue()));
        }
        return newMap;
    }

    public static <K,V> Tuple2<K,V>  head(Map<K,V> map){
        Map.Entry<K,V> entry = map.entrySet().iterator().next();
        return new Tuple2<>(entry.getKey(),entry.getValue());
    }

    public static <K,V> Optional<Tuple2<K,V>>  headOption(Map<K,V> map){
        for (Map.Entry<K, V> entry : map.entrySet()) {
            return Optional.of(new Tuple2<>(entry.getKey(), entry.getValue()));
        }
        return Optional.empty();
    }

    public static <K,V> Tuple2<K,V> last(Map<K,V> map){
        Iterator<Map.Entry<K,V>> entry = map.entrySet().iterator();
        Map.Entry<K,V> head = entry.next();
        while (entry.hasNext()){
            head = entry.next();
        }
        return new Tuple2<>(head.getKey(),head.getValue());
    }

    public static <K,V> Optional<Tuple2<K,V>> lastOption(Map<K,V> map){
        Iterator<Map.Entry<K,V>> entry = map.entrySet().iterator();
        Map.Entry<K,V> head = null;
        while (entry.hasNext()){
            head = entry.next();
        }
        return head==null?Optional.empty():Optional.of(new Tuple2<>(head.getKey(),head.getValue()));
    }
}
