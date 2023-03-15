package com.dongjiaqiang.jvm.dsl.java.api.util;

import com.dongjiaqiang.jvm.dsl.java.api.extend.Either;
import com.dongjiaqiang.jvm.dsl.java.api.extend.Success;
import com.dongjiaqiang.jvm.dsl.java.api.extend.Try;
import com.dongjiaqiang.jvm.dsl.java.api.lambda.consumer._1_Consumer;
import com.dongjiaqiang.jvm.dsl.java.api.lambda.function.*;
import com.dongjiaqiang.jvm.dsl.java.api.lambda.predicate._1_Predicate;
import com.dongjiaqiang.jvm.dsl.java.api.lambda.supplier._1_Supplier;
import com.dongjiaqiang.jvm.dsl.java.api.tuple.*;

import java.util.*;

public class SetMethodUtils {

    //map
    public static <V,K> Set<? extends K> map(Set<V> set,
                                             _1_Function<? super V,? extends K> mapper,
                                             _1_Supplier<Set<K>>supplier) throws Exception {
            Set<K> newSet = supplier.get();
            for(V v:set){
                newSet.add(mapper.apply(v));
            }
            return newSet;
    }
    public static <V1,V2,K>  Set<? extends K> map(Set<Tuple2<V1,V2>> set,
                                                  _2_Function<? super V1,? super V2,? extends K> mapper,
                                                  _1_Supplier<Set<K>> supplier) throws Exception {
        Set<K> newSet = supplier.get();
        for(Tuple2<V1,V2> tuple2:set){
            newSet.add(mapper.apply(tuple2._1,tuple2._2));
        }
        return newSet;
    }

    public static <V1,V2,V3,K>  Set<? extends K> map(Set<Tuple3<V1,V2,V3>> list,
                                                      _3_Function<? super V1, ? super V2, ? super V3, ? extends K> mapper,
                                                      _1_Supplier<Set<K>> supplier) throws Exception {
        Set<K> newSet = supplier.get();
        for(Tuple3<V1,V2,V3> tuple3:list){
            newSet.add(mapper.apply(tuple3._1,tuple3._2,tuple3._3));
        }
        return newSet;
    }
    public static <V1,V2,V3,V4,K>  Set<? extends K> map(Set<Tuple4<V1,V2,V3,V4>> list,
                                                         _4_Function<? super V1, ? super V2, ? super V3,? super V4, ? extends K> mapper,
                                                         _1_Supplier<Set<K>> supplier) throws Exception {
        Set<K> newSet = supplier.get();
        for(Tuple4<V1,V2,V3,V4> tuple4:list){
            newSet.add(mapper.apply(tuple4._1,tuple4._2,tuple4._3,tuple4._4));
        }
        return newSet;
    }
    public static <V1,V2,V3,V4,V5,K>  Set<? extends K> map(Set<Tuple5<V1,V2,V3,V4,V5>> set,
                                                            _5_Function<? super V1, ? super V2, ? super V3,? super V4, ? super V5,? extends K> mapper,
                                                           _1_Supplier<Set<K>> supplier) throws Exception {
        Set<K> newSet = supplier.get();
        for(Tuple5<V1,V2,V3,V4,V5> tuple5:set){
            newSet.add(mapper.apply(tuple5._1,tuple5._2,tuple5._3,tuple5._4,tuple5._5));
        }
        return newSet;
    }
    public static <V1,V2,V3,V4,V5,V6,K>  Set<? extends K> map(Set<Tuple6<V1,V2,V3,V4,V5,V6>> set,
                                                               _6_Function<? super V1, ? super V2, ? super V3,? super V4, ? super V5,? super V6,? extends K> mapper,
                                                               _1_Supplier<Set<K>> supplier) throws Exception {
        Set<K> newSet = supplier.get();
        for(Tuple6<V1,V2,V3,V4,V5,V6> tuple6:set){
            newSet.add(mapper.apply(tuple6._1,tuple6._2,tuple6._3,tuple6._4,tuple6._5,tuple6._6));
        }
        return newSet;
    }
    public static <V1,V2,V3,V4,V5,V6,V7,K>  Set<? extends K> map(Set<Tuple7<V1,V2,V3,V4,V5,V6,V7>> set,
                                                                  _7_Function<? super V1, ? super V2, ? super V3,? super V4, ? super V5,? super V6,? super V7,? extends K> mapper,
                                                                 _1_Supplier<Set<K>> supplier) throws Exception {
        Set<K> newSet = supplier.get();
        for(Tuple7<V1,V2,V3,V4,V5,V6,V7> tuple7:set){
            newSet.add(mapper.apply(tuple7._1,tuple7._2,tuple7._3,tuple7._4,tuple7._5,tuple7._6,tuple7._7));
        }
        return newSet;
    }
    public static <V1,V2,V3,V4,V5,V6,V7,V8,K>  Set<? extends K> map(Set<Tuple8<V1,V2,V3,V4,V5,V6,V7,V8>> set,
                                                                     _8_Function<? super V1, ? super V2, ? super V3,? super V4, ? super V5,? super V6,? super V7,? super V8,? extends K> mapper,
                                                                    _1_Supplier<Set<K>> supplier) throws Exception {
        Set<K> newSet = supplier.get();
        for(Tuple8<V1,V2,V3,V4,V5,V6,V7,V8> tuple8:set){
            newSet.add(mapper.apply(tuple8._1,tuple8._2,tuple8._3,
                    tuple8._4,tuple8._5,tuple8._6,tuple8._7,tuple8._8));
        }
        return newSet;
    }

    //flatten
    public static <V> Set<V> flatten(Set<Collection<V>> list,_1_Supplier<Set<V>> supplier) throws Exception{
            Set<V> newSet = supplier.get();
            for(Collection<V> collection:list){
                newSet.addAll(collection);
            }
            return newSet;
    }
    public static <V> Set<V>  flattenArray(Set<V[]> list,_1_Supplier<Set<V>> supplier) throws Exception{
        Set<V> newSet = supplier.get();
        for(V[] vs:list){
            newSet.addAll(Arrays.asList(vs));
        }
        return newSet;
    }
    public static <V> Set<V> flattenTry(Set<Try<V>> list,_1_Supplier<Set<V>> supplier) throws Exception{
        Set<V> newSet = supplier.get();
        for(Try<V> vTry:list){
            if(vTry.failure()){
                continue;
            }
            newSet.add(((Success<V>)vTry).getValue());
        }
        return newSet;
    }
    public static <V,K> Set<K> flattenEither(Set<Either<V,K>> list,_1_Supplier<Set<K>> supplier) throws Exception{
        Set<K> newSet = supplier.get();
        for(Either<V,K> either:list){
            if(either.isRight()){
                newSet.add(either.right());
            }
        }
        return newSet;
    }
    public static <V> Set<V> flattenOptional(List<Optional<V>> list,
                                             _1_Supplier<Set<V>> supplier) throws Exception{
        Set<V> newSet = supplier.get();
            for(Optional<V> optional:list){
                if (!optional.isPresent()) {
                    continue;
                }
                newSet.add(optional.get());
            }
            return newSet;
    }

    //flatMap
    public static <V,K> Set<? extends K> flatMap(Set<Collection<V>> list, _1_Function<? super V,? extends K> mapper,
                                                 _1_Supplier<Set<K>> supplier) throws Exception {
        Set<K> newSet = supplier.get();
        for(Collection<V> vs:list){
            for(V v:vs){
                newSet.add(mapper.apply(v));
            }
        }
        return newSet;
    }
    public static <V1,V2,K> Set<? extends K> flatMap(Set<Collection<Tuple2<V1,V2>>> list,
                                                      _2_Function<? super V1,? super V2,? extends K> mapper,
                                                      _1_Supplier<Set<K>> supplier) throws Exception {
        Set<K> newSet = supplier.get();
        for(Collection<Tuple2<V1,V2>> vs:list){
            for(Tuple2<V1,V2> v:vs){
                newSet.add(mapper.apply(v._1,v._2));
            }
        }
        return newSet;
    }
    public static <V1,V2,V3,K> Set<? extends K> flatMap(Set<Collection<Tuple3<V1,V2,V3>>> list,
                                                      _3_Function<? super V1,? super V2,? super V3,? extends K> mapper,
                                                        _1_Supplier<Set<K>> supplier) throws Exception {
        Set<K> newSet = supplier.get();
        for(Collection<Tuple3<V1,V2,V3>> vs:list){
            for(Tuple3<V1,V2,V3> v:vs){
                newSet.add(mapper.apply(v._1,v._2,v._3));
            }
        }
        return newSet;
    }
    public static <V1,V2,V3,V4,K> Set<? extends K> flatMap(Set<Collection<Tuple4<V1,V2,V3,V4>>> list,
                                                         _4_Function<? super V1,? super V2,? super V3,? super V4,? extends K> mapper,
                                                            _1_Supplier<Set<K>> supplier) throws Exception {
        Set<K> newSet = supplier.get();
        for(Collection<Tuple4<V1,V2,V3,V4>> vs:list){
            for(Tuple4<V1,V2,V3,V4> v:vs){
                newSet.add(mapper.apply(v._1,v._2,v._3,v._4));
            }
        }
        return newSet;
    }
    public static <V1,V2,V3,V4,V5,K> Set<? extends K> flatMap(Set<Collection<Tuple5<V1,V2,V3,V4,V5>>> list,
                                                            _5_Function<? super V1,? super V2,? super V3,? super V4,? super V5,? extends K> mapper,
                                                              _1_Supplier<Set<K>> supplier) throws Exception {
        Set<K> newSet = supplier.get();
        for(Collection<Tuple5<V1,V2,V3,V4,V5>> vs:list){
            for(Tuple5<V1,V2,V3,V4,V5> v:vs){
                newSet.add(mapper.apply(v._1,v._2,v._3,v._4,v._5));
            }
        }
        return newSet;
    }
    public static <V1,V2,V3,V4,V5,V6,K> Set<? extends K> flatMap(Set<Collection<Tuple6<V1,V2,V3,V4,V5,V6>>> list,
                                                               _6_Function<? super V1,? super V2,? super V3,? super V4,? super V5,? super V6,? extends K> mapper,
                                                                 _1_Supplier<Set<K>> supplier) throws Exception {
        Set<K> newSet = supplier.get();
        for(Collection<Tuple6<V1,V2,V3,V4,V5,V6>> vs:list){
            for(Tuple6<V1,V2,V3,V4,V5,V6> v:vs){
                newSet.add(mapper.apply(v._1,v._2,v._3,v._4,v._5,v._6));
            }
        }
        return newSet;
    }

    public static <V,K> Set<? extends K> flatMapArray(Set<V[]> list,
                                                      _1_Function<? super V,? extends K> mapper,
                                                      _1_Supplier<Set<K>> supplier) throws Exception {
        Set<K> newSet = supplier.get();
        for(V[] vs:list){
            for(V v:vs){
                newSet.add(mapper.apply(v));
            }
        }
        return newSet;
    }
    public static <V,K> Set<? extends K> flatMapOptional(Set<Optional<V>> list, _1_Function<? super V,? extends K> mapper,
                                               _1_Supplier<Set<K>> supplier) throws Exception {
        Set<K> newSet = supplier.get();
        for(Optional<V> optional:list){
            if(!optional.isPresent()){
                continue;
            }
            newSet.add(mapper.apply(optional.get()));
        }
        return newSet;
    }
    public static <V,K> Set<? extends K> flatMapTry(Set<Try<V>> list,
                                                    _1_Function<? super V,? extends K> mapper,
                                                    _1_Supplier<Set<K>> supplier) throws Exception {
        Set<K> newSet = supplier.get();
        for(Try<V> vTry:list){
            if(vTry.failure()){
                continue;
            }
            newSet.add(mapper.apply((((Success<V>)vTry).get())));
        }
        return newSet;
    }

    public static <K,L,R> Set<K> flatMapEither(List<Either<L,R>> list,
                                                _1_Function<? super R,? extends K> mapper,
                                                _1_Supplier<Set<K>> supplier) throws Exception {
        Set<K> newSet = supplier.get();
        for(Either<L,R> either:list){
            if(either.isLeft()){
                continue;
            }
            newSet.add(mapper.apply(either.right()));
        }
        return newSet;
    }

    //filter
    public static <V> Set<V> filter(Set<V> list,
                                    _1_Predicate<V> filter,
                                    _1_Supplier<Set<V>> supplier) throws Exception{
        Set<V> newSet = supplier.get();
        for(V v:list){
            if(filter.test(v)){
                newSet.add(v);
            }
        }
        return newSet;
    }

    //foreach
    public static <V> void foreach(Set<V> list,
                                   _1_Consumer<V> consumer) throws Exception{
        for(V v:list){
            consumer.accept(v);
        }
    }

    //exist
    public static <V> boolean exist(Set<V> list, _1_Predicate<V> predicate) throws Exception{
        for(V v:list){
            if(predicate.test(v)){
                return true;
            }
        }
        return false;
    }

    //find
    public static <V> Optional<V> find(Set<V> list, _1_Predicate<V> predicate) throws Exception{
        for(V v:list){
            if(predicate.test(v)){
                return Optional.of(v);
            }
        }
        return Optional.empty();
    }


    //toMap
    public static <K,V> Map<K,V> toMap(Set<Tuple2<K,V>> list){
        Map<K,V> map = new HashMap<>();
        for(Tuple2<K,V> tuple2:list){
            map.put(tuple2._1,tuple2._2);
        }
        return map;
    }

    //reduce
    public static <V> V reduce(Set<V> list, _2_Function<? super V,? super V,? extends V> reducer) throws Exception {
        Iterator<V> iterator = list.iterator();
        V v = iterator.next();
        while (iterator.hasNext()){
            v = reducer.apply(v,iterator.next());
        }
        return v;
    }
    public static <V> V reduce(Set<V> list, _2_Function<? super V,? super V,? extends V> reducer,V init) throws Exception {
        V v = init;
        for (V value : list) {
            v = reducer.apply(v, value);
        }
        return v;
    }

    //reduceOption
    public static <V> Optional<V> reduceOption(Set<V> list, _2_Function<? super V,? super V,? extends V> reducer) throws Exception {
        if(list.isEmpty()){
            return Optional.empty();
        }
        return Optional.of(reduce(list,reducer));
    }


}
