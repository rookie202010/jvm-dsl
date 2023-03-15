package com.dongjiaqiang.jvm.dsl.java.api.util;

import com.dongjiaqiang.jvm.dsl.java.api.extend.Either;
import com.dongjiaqiang.jvm.dsl.java.api.extend.Success;
import com.dongjiaqiang.jvm.dsl.java.api.extend.Try;
import com.dongjiaqiang.jvm.dsl.java.api.lambda.consumer._1_Consumer;
import com.dongjiaqiang.jvm.dsl.java.api.lambda.function.*;
import com.dongjiaqiang.jvm.dsl.java.api.lambda.predicate._1_Predicate;
import com.dongjiaqiang.jvm.dsl.java.api.tuple.Tuple2;
import com.dongjiaqiang.jvm.dsl.java.api.tuple.Tuple3;
import com.dongjiaqiang.jvm.dsl.java.api.tuple.Tuple4;
import com.dongjiaqiang.jvm.dsl.java.api.tuple.Tuple5;

import java.util.*;

public class ListMethodUtils {

    //map
    public static <V,K> List<? extends K> map(List<V> list, _1_Function<? super V,? extends K> mapper) throws Exception {
            List<K> newList = new ArrayList<>();
            for(V v:list){
                newList.add(mapper.apply(v));
            }
            return newList;
    }
    public static <V1,V2,K>  List<? extends K> map(List<Tuple2<V1,V2>> list, _2_Function<? super V1,? super V2,? extends K> mapper) throws Exception {
        List<K> newList = new ArrayList<>();
        for(Tuple2<V1,V2> tuple2:list){
            newList.add(mapper.apply(tuple2._1,tuple2._2));
        }
        return newList;
    }
    public static <V1,V2,V3,K>  List<? extends K> map(List<Tuple3<V1,V2,V3>> list, _3_Function<? super V1, ? super V2, ? super V3, ? extends K> mapper) throws Exception {
        List<K> newList = new ArrayList<>();
        for(Tuple3<V1,V2,V3> tuple3:list){
            newList.add(mapper.apply(tuple3._1,tuple3._2,tuple3._3));
        }
        return newList;
    }
    public static <V1,V2,V3,V4,K>  List<? extends K> map(List<Tuple4<V1,V2,V3,V4>> list, _4_Function<? super V1, ? super V2, ? super V3,? super V4, ? extends K> mapper) throws Exception {
        List<K> newList = new ArrayList<>();
        for(Tuple4<V1,V2,V3,V4> tuple4:list){
            newList.add(mapper.apply(tuple4._1,tuple4._2,tuple4._3,tuple4._4));
        }
        return newList;
    }
    public static <V1,V2,V3,V4,V5,K>  List<? extends K> map(List<Tuple5<V1,V2,V3,V4,V5>> list, _5_Function<? super V1, ? super V2, ? super V3,? super V4, ? super V5,? extends K> mapper) throws Exception {
        List<K> newList = new ArrayList<>();
        for(Tuple5<V1,V2,V3,V4,V5> tuple5:list){
            newList.add(mapper.apply(tuple5._1,tuple5._2,tuple5._3,tuple5._4,tuple5._5));
        }
        return newList;
    }


    //flatten
    public static <V> List<V> flatten(List<Collection<V>> list){
            List<V> newList = new ArrayList<>();
            for(Collection<V> collection:list){
                newList.addAll(collection);
            }
            return newList;
    }
    public static <V> List<V> flattenArray(List<V[]> list){
        List<V> newList = new ArrayList<>();
        for(V[] vs:list){
            newList.addAll(Arrays.asList(vs));
        }
        return newList;
    }
    public static <V> List<V> flattenTry(List<Try<V>> list){
        List<V> newList = new ArrayList<>();
        for(Try<V> vTry:list){
            if(vTry.failure()){
                continue;
            }
            newList.add(((Success<V>)vTry).getValue());
        }
        return newList;
    }
    public static <V,K> List<K> flattenEither(List<Either<V,K>> list){
        List<K> newList = new ArrayList<>();
        for(Either<V,K> either:list){
            if(either.isRight()){
                newList.add(either.right());
            }
        }
        return newList;
    }
    public static <V> List<V> flattenOptional(List<Optional<V>> list){
            List<V> newList = new ArrayList<>();
            for(Optional<V> optional:list){
                if (!optional.isPresent()) {
                    continue;
                }
                newList.add(optional.get());
            }
            return newList;
    }

    //flatMap
    public static <V,K> List<? extends K> flatMap(List<Collection<V>> list, _1_Function<? super V,? extends K> mapper) throws Exception {
        List<K> newList = new ArrayList<>();
        for(Collection<V> vs:list){
            for(V v:vs){
                newList.add(mapper.apply(v));
            }
        }
        return newList;
    }
    public static <V,K> List<K> flatMapArray(List<V[]> list, _1_Function<? super V,? extends K> mapper) throws Exception {
        List<K> newList = new ArrayList<>();
        for(V[] vs:list){
            for(V v:vs){
                newList.add(mapper.apply(v));
            }
        }
        return newList;
    }
    public static <V,K> List<K> flatMapOptional(List<Optional<V>> list, _1_Function<? super V,? extends K> mapper) throws Exception {
        List<K> newList = new ArrayList<>();
        for(Optional<V> optional:list){
            if(!optional.isPresent()){
                continue;
            }
            newList.add(mapper.apply(optional.get()));
        }
        return newList;
    }
    public static <V,K> List<K> flatMapTry(List<Try<V>> list, _1_Function<? super V,? extends K> mapper) throws Exception {
        List<K> newList = new ArrayList<>();
        for(Try<V> vTry:list){
            if(vTry.failure()){
                continue;
            }
            newList.add(mapper.apply((((Success<V>)vTry).get())));
        }
        return newList;
    }

    public static <K,L,R> List<K> flatMapEither(List<Either<L,R>> list, _1_Function<? super R,? extends K> mapper) throws Exception {
        List<K> newList = new ArrayList<>();
        for(Either<L,R> either:list){
            if(either.isLeft()){
                continue;
            }
            newList.add(mapper.apply(either.right()));
        }
        return newList;
    }

    //filter
    public static <V> List<V> filter(List<V> list, _1_Predicate<V> filter) throws Exception{
        List<V> newList = new ArrayList<>();
        for(V v:list){
            if(filter.test(v)){
                newList.add(v);
            }
        }
        return newList;
    }

    //foreach
    public static <V> void foreach(List<V> list, _1_Consumer<V> consumer) throws Exception{
        List<V> newList = new ArrayList<>();
        for(V v:list){
            consumer.accept(v);
        }
    }

    //exist
    public static <V> boolean exist(List<V> list, _1_Predicate<V> predicate) throws Exception{
        for(V v:list){
            if(predicate.test(v)){
                return true;
            }
        }
        return false;
    }

    //find
    public static <V> Optional<V> find(List<V> list, _1_Predicate<V> predicate) throws Exception{
        for(V v:list){
            if(predicate.test(v)){
                return Optional.of(v);
            }
        }
        return Optional.empty();
    }


    //toMap
    public static <K,V> Map<K,V> toMap(List<Tuple2<K,V>> list){
        Map<K,V> map = new HashMap<>();
        for(Tuple2<K,V> tuple2:list){
            map.put(tuple2._1,tuple2._2);
        }
        return map;
    }

    //sort
    public static <V> List<V> sort(List<V> list, Comparator<V> comparator){
        list.sort(comparator);
        return list;
    }

    //zipWithIndex
    public static <V> List<Tuple2<V,Integer>> zipWithIndex(List<V> list){
        List<Tuple2<V,Integer>>  newList = new ArrayList<>();
        for(int i=0;i<list.size();i++){
            newList.add(new Tuple2<>(list.get(i),i));
        }
        return newList;
    }

    //reduce
    public static <V> V reduce(List<V> list, _2_Function<? super V,? super V,? extends V> reducer) throws Exception {
        V v = list.get(0);
        for(int i=1;i<list.size();i++){
            v = reducer.apply(v,list.get(i));
        }
        return v;
    }

    //reduceOption
    public static <V> Optional<V> reduceOption(List<V> list, _2_Function<? super V,? super V,? extends V> reducer) throws Exception {
        if(list.isEmpty()){
            return Optional.empty();
        }
        return Optional.of(reduce(list,reducer));
    }


}
