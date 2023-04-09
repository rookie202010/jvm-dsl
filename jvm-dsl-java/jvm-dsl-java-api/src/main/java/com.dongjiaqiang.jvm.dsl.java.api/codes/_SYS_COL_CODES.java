package com.dongjiaqiang.jvm.dsl.java.api.codes;

import com.dongjiaqiang.jvm.dsl.java.api.extend.Either;
import com.dongjiaqiang.jvm.dsl.java.api.extend.Success;
import com.dongjiaqiang.jvm.dsl.java.api.extend.Try;
import com.dongjiaqiang.jvm.dsl.java.api.lambda.consumer._1_Consumer;
import com.dongjiaqiang.jvm.dsl.java.api.lambda.function.*;
import com.dongjiaqiang.jvm.dsl.java.api.lambda.predicate._1_Predicate;
import com.dongjiaqiang.jvm.dsl.java.api.tuple.*;

import java.util.*;

public class _SYS_COL_CODES {

    public static final String CLAZZ_NAME = "_SYS_COL_CODES";

    //map functions

    /**
     * arr.map(i=> { return i+1;});
     */
    public static <V, K> void map(Collection<V> list, Collection<K> newList,
                                  _1_Function<? super V, ? extends K> mapper) throws Exception {
        for (V v : list) {
            newList.add(mapper.apply(v));
        }
    }

    public static <V1, V2, K> void map(Collection<Tuple2<V1, V2>> list, Collection<K> newList,
                                       _2_Function<? super V1, ? super V2, ? extends K> mapper) throws Exception {
        for (Tuple2<V1, V2> tuple2 : list) {
            newList.add(mapper.apply(tuple2._1, tuple2._2));
        }
    }

    public static <V1, V2, V3, K> void map(Collection<Tuple3<V1, V2, V3>> list, Collection<K> newList,
                                           _3_Function<? super V1, ? super V2, ? super V3, ? extends K> mapper) throws Exception {
        for (Tuple3<V1, V2, V3> tuple3 : list) {
            newList.add(mapper.apply(tuple3._1, tuple3._2, tuple3._3));
        }
    }

    public static <V1, V2, V3, V4, K> void map(Collection<Tuple4<V1, V2, V3, V4>> list, Collection<K> newList,
                                               _4_Function<? super V1, ? super V2, ? super V3, ? super V4, ? extends K> mapper) throws Exception {
        for (Tuple4<V1, V2, V3, V4> tuple4 : list) {
            newList.add(mapper.apply(tuple4._1, tuple4._2, tuple4._3, tuple4._4));
        }
    }

    public static <V1, V2, V3, V4, V5, K> void map(Collection<Tuple5<V1, V2, V3, V4, V5>> list, Collection<K> newList,
                                                   _5_Function<? super V1, ? super V2, ? super V3, ? super V4, ? super V5, ? extends K> mapper) throws Exception {
        for (Tuple5<V1, V2, V3, V4, V5> tuple5 : list) {
            newList.add(mapper.apply(tuple5._1, tuple5._2, tuple5._3, tuple5._4, tuple5._5));
        }
    }

    public static <V1, V2, V3, V4, V5, V6, K> void map(Collection<Tuple6<V1, V2, V3, V4, V5, V6>> list, Collection<K> newList,
                                                       _6_Function<? super V1, ? super V2, ? super V3, ? super V4, ? super V5, ? super V6, ? extends K> mapper) throws Exception {
        for (Tuple6<V1, V2, V3, V4, V5, V6> tuple6 : list) {
            newList.add(mapper.apply(tuple6._1, tuple6._2, tuple6._3, tuple6._4, tuple6._5, tuple6._6));
        }
    }

    public static <V1, V2, V3, V4, V5, V6, V7, K> void map(Collection<Tuple7<V1, V2, V3, V4, V5, V6, V7>> list, Collection<K> newList,
                                                           _7_Function<? super V1, ? super V2, ? super V3, ? super V4, ? super V5, ? super V6, ? super V7, ? extends K> mapper) throws Exception {
        for (Tuple7<V1, V2, V3, V4, V5, V6, V7> tuple7 : list) {
            newList.add(mapper.apply(tuple7._1, tuple7._2, tuple7._3, tuple7._4, tuple7._5, tuple7._6, tuple7._7));
        }
    }

    public static <V1, V2, V3, V4, V5, V6, V7, V8, K> void map(Collection<Tuple8<V1, V2, V3, V4, V5, V6, V7, V8>> list, Collection<K> newList,
                                                               _8_Function<? super V1, ? super V2, ? super V3, ? super V4, ? super V5, ? super V6, ? super V7, ? super V8, ? extends K> mapper) throws Exception {
        for (Tuple8<V1, V2, V3, V4, V5, V6, V7, V8> tuple8 : list) {
            newList.add(mapper.apply(tuple8._1, tuple8._2, tuple8._3, tuple8._4, tuple8._5, tuple8._6, tuple8._7, tuple8._8));
        }
    }

    //flatten functions

    /**
     * arr.flatten()
     */
    public static <V> void flatten(Collection<Collection<V>> list, Collection<V> newList) {
        for (Collection<V> collection : list) {
            newList.addAll(collection);
        }
    }

    public static <V> void flattenArray(Collection<V[]> list, Collection<V> newList) {
        for (V[] vs : list) {
            newList.addAll(Arrays.asList(vs));
        }
    }

    public static <V> void flattenTry(Collection<Try<V>> list, Collection<V> newList) {
        for (Try<V> vTry : list) {
            if (vTry.failure()) {
                continue;
            }
            newList.add(((Success<V>) vTry).getValue());
        }
    }

    public static <V, K> void flattenEither(Collection<Either<V, K>> list, Collection<K> newList) {
        for (Either<V, K> either : list) {
            if (either.isRight()) {
                newList.add(either.right());
            }
        }
    }

    public static <V> void flattenOptional(Collection<Optional<V>> list, Collection<V> newList) {
        for (Optional<V> optional : list) {
            if (!optional.isPresent()) {
                continue;
            }
            newList.add(optional.get());
        }
    }

    //flatMap functions

    /**
     * arr.flatMap(a=>{ return [1,2,a] ; });
     */
    public static <V, K> void flatMap_C_C(Collection<V> list, Collection<K> newList, _1_Function<? super V, ? extends Collection<K>> mapper) throws Exception {
        for (V v : list) {
            for (K k : mapper.apply(v)) {
                newList.add(k);
            }
        }
    }

    public static <V, K> void flatMap_C_A(Collection<V> list, Collection<K> newList, _1_Function<? super V, ? extends K[]> mapper) throws Exception {
        for (V v : list) {
            for (K k : mapper.apply(v)) {
                newList.add(k);
            }
        }
    }

    public static <V, K> void flatMap_C_T(Collection<V> list, Collection<K> newList, _1_Function<? super V, ? extends Try<K>> mapper) throws Exception {
        for (V v : list) {
            Try<K> vtry = mapper.apply(v);
            if (vtry.failure()) {
                continue;
            }
            newList.add(((Success<K>) vtry).getValue());
        }
    }

    public static <V, K> void flatMap_C_O(Collection<V> list, Collection<K> newList, _1_Function<? super V, ? extends Optional<K>> mapper) throws Exception {
        for (V v : list) {
            Optional<K> optional = mapper.apply(v);
            if (!optional.isPresent()) {
                continue;
            }
            newList.add((optional.get()));
        }
    }

    public static <V, L, R> void flatMap_C_E(Collection<V> list, Collection<R> newList, _1_Function<? super V, ? extends Either<L, R>> mapper) throws Exception {
        for (V v : list) {
            Either<L, R> either = mapper.apply(v);
            if (either.isLeft()) {
                continue;
            }
            newList.add(either.right());
        }
    }

    public static <V, K> void flatMap_OC_C(Collection<Optional<V>> list,
                                           Collection<K> newList, _1_Function<? super Optional<V>, ? extends Collection<K>> mapper) throws Exception {
        for (Optional<V> v : list) {
            for (K k : mapper.apply(v)) {
                newList.add(k);
            }
        }
    }

    public static <V, K> void flatMap_OC_A(Collection<Optional<V>> list,
                                           Collection<K> newList, _1_Function<? super Optional<V>, ? extends K[]> mapper) throws Exception {
        for (Optional<V> v : list) {
            for (K k : mapper.apply(v)) {
                newList.add(k);
            }
        }
    }

    public static <V, K> void flatMap_OC_T(Collection<Optional<V>> list,
                                           Collection<K> newList, _1_Function<? super Optional<V>, ? extends Try<K>> mapper) throws Exception {
        for (Optional<V> v : list) {
            Try<K> vtry = mapper.apply(v);
            if (vtry.failure()) {
                continue;
            }
            newList.add(((Success<K>) vtry).getValue());
        }
    }

    public static <V, K> void flatMap_OC_O(Collection<Optional<V>> list,
                                           Collection<K> newList, _1_Function<? super Optional<V>, ? extends Optional<K>> mapper) throws Exception {
        for (Optional<V> v : list) {
            Optional<K> optional = mapper.apply(v);
            if (!optional.isPresent()) {
                continue;
            }
            newList.add((optional.get()));
        }
    }

    public static <V, L, R> void flatMap_OC_E(Collection<Optional<V>> list,
                                              Collection<R> newList, _1_Function<? super Optional<V>, ? extends Either<L, R>> mapper) throws Exception {
        for (Optional<V> v : list) {
            Either<L, R> either = mapper.apply(v);
            if (either.isLeft()) {
                continue;
            }
            newList.add(either.right());
        }
    }

    public static <V1, V2, K> void flatMap_T2_C_C(Collection<Tuple2<V1, V2>> list, Collection<K> newList,
                                           _2_Function<? super V1, ? super V2, ? extends Collection<K>> mapper) throws Exception {
        for (Tuple2<V1, V2> v : list) {
            for (K k : mapper.apply(v._1, v._2)) {
                newList.add(k);
            }
        }
    }

    public static <V1, V2, K> void flatMap_T2_C_A(Collection<Tuple2<V1, V2>> list, Collection<K> newList,
                                                _2_Function<? super V1, ? super V2, ? extends K[]> mapper) throws Exception {
        for (Tuple2<V1, V2> v : list) {
            for (K k : mapper.apply(v._1, v._2)) {
                newList.add(k);
            }
        }
    }

    public static <V1, V2, K> void flatMap_T2_C_T(Collection<Tuple2<V1, V2>> list,
                                              Collection<K> newList,
                                              _2_Function<? super V1, ? super V2, ? extends Try<K>> mapper) throws Exception {
        for (Tuple2<V1, V2> v : list) {
            Try<K> vtry = mapper.apply(v._1, v._2);
            if (vtry.failure()) {
                continue;
            }
            newList.add(((Success<K>) vtry).getValue());
        }
    }

    public static <V1, V2, K> void flatMap_T2_C_O(Collection<Tuple2<V1, V2>> list,
                                                   Collection<K> newList, _2_Function<? super V1, ? super V2, ? extends Optional<K>> mapper) throws Exception {
        for (Tuple2<V1, V2> v : list) {
            Optional<K> optional = mapper.apply(v._1, v._2);
            if (!optional.isPresent()) {
                continue;
            }
            newList.add((optional.get()));
        }
    }

    public static <V1, V2, L, R> void flatMap_T2_C_E(Collection<Tuple2<V1, V2>> list, Collection<R> newList,
                                                    _2_Function<? super V1, ? super V2, ? extends Either<L, R>> mapper) throws Exception {
        for (Tuple2<V1, V2> v : list) {
            Either<L, R> either = mapper.apply(v._1, v._2);
            if (either.isLeft()) {
                continue;
            }
            newList.add(either.right());
        }
    }

    public static <V1, V2, V3, K> void flatMap_T3_C_C(Collection<Tuple3<V1, V2, V3>> list, Collection<K> newList,
                                               _3_Function<? super V1, ? super V2, ? super V3, ? extends Collection<K>> mapper) throws Exception {
        for (Tuple3<V1, V2, V3> v : list) {
            for (K k : mapper.apply(v._1, v._2, v._3)) {
                newList.add(k);
            }
        }
    }

    public static <V1, V2, V3, K> void flatMap_T3_C_A(Collection<Tuple3<V1, V2, V3>> list, Collection<K> newList,
                                                    _3_Function<? super V1, ? super V2, ? super V3, ? extends K[]> mapper) throws Exception {
        for (Tuple3<V1, V2, V3> v : list) {
            for (K k : mapper.apply(v._1, v._2, v._3)) {
                newList.add(k);
            }
        }
    }

    public static <V1, V2, V3, K> void flatMap_T3_C_T(Collection<Tuple3<V1, V2, V3>> list, Collection<K> newList,
                                                  _3_Function<? super V1, ? super V2, ? super V3, ? extends Try<K>> mapper) throws Exception {
        for (Tuple3<V1, V2, V3> v : list) {
            Try<K> vtry = mapper.apply(v._1, v._2, v._3);
            if (vtry.failure()) {
                continue;
            }
            newList.add(((Success<K>) vtry).getValue());
        }
    }

    public static <V1, V2, V3, K> void flatMap_T3_C_O(Collection<Tuple3<V1, V2, V3>> list, Collection<K> newList,
                                                       _3_Function<? super V1, ? super V2, ? super V3, ? extends Optional<K>> mapper) throws Exception {
        for (Tuple3<V1, V2, V3> v : list) {
            Optional<K> optional = mapper.apply(v._1, v._2, v._3);
            if (!optional.isPresent()) {
                continue;
            }
            newList.add((optional.get()));
        }
    }

    public static <V1, V2, V3, L, R> void flatMap_T3_C_E(Collection<Tuple3<V1, V2, V3>> list, Collection<R> newList,
                                                        _3_Function<? super V1, ? super V2, ? super V3, ? extends Either<L, R>> mapper) throws Exception {
        for (Tuple3<V1, V2, V3> v : list) {
            Either<L, R> either = mapper.apply(v._1, v._2, v._3);
            if (either.isLeft()) {
                continue;
            }
            newList.add(either.right());
        }
    }

    public static <V1, V2, V3, V4, K> void flatMap_T4_C_C(Collection<Tuple4<V1, V2, V3, V4>> list, Collection<K> newList,
                                                   _4_Function<? super V1, ? super V2, ? super V3, ? super V4, ? extends Collection<K>> mapper) throws Exception {
        for (Tuple4<V1, V2, V3, V4> v : list) {
            for (K k : mapper.apply(v._1, v._2, v._3, v._4)) {
                newList.add(k);
            }
        }
    }

    public static <V1, V2, V3, V4, K> void flatMap_T4_C_A(Collection<Tuple4<V1, V2, V3, V4>> list, Collection<K> newList,
                                                        _4_Function<? super V1, ? super V2, ? super V3, ? super V4, ? extends K[]> mapper) throws Exception {
        for (Tuple4<V1, V2, V3, V4> v : list) {
            for (K k : mapper.apply(v._1, v._2, v._3, v._4)) {
                newList.add(k);
            }
        }
    }

    public static <V1, V2, V3, V4, K> void flatMap_T4_C_T(Collection<Tuple4<V1, V2, V3, V4>> list, Collection<K> newList,
                                                      _4_Function<? super V1, ? super V2, ? super V3, ? super V4, ? extends Try<K>> mapper) throws Exception {
        for (Tuple4<V1, V2, V3, V4> v : list) {
            Try<K> vtry = mapper.apply(v._1, v._2, v._3, v._4);
            if (vtry.failure()) {
                continue;
            }
            newList.add(((Success<K>) vtry).getValue());
        }
    }

    public static <V1, V2, V3, V4, K> void flatMap_T4_C_O(Collection<Tuple4<V1, V2, V3, V4>> list, Collection<K> newList,
                                                           _4_Function<? super V1, ? super V2, ? super V3, ? super V4, ? extends Optional<K>> mapper) throws Exception {
        for (Tuple4<V1, V2, V3, V4> v : list) {
            Optional<K> optional = mapper.apply(v._1, v._2, v._3, v._4);
            if (!optional.isPresent()) {
                continue;
            }
            newList.add((optional.get()));
        }
    }

    public static <V1, V2, V3, V4, L, R> void flatMap_T4_C_E(Collection<Tuple4<V1, V2, V3, V4>> list, Collection<R> newList,
                                                            _4_Function<? super V1, ? super V2, ? super V3, ? super V4, ? extends Either<L, R>> mapper) throws Exception {
        for (Tuple4<V1, V2, V3, V4> v : list) {
            Either<L, R> either = mapper.apply(v._1, v._2, v._3, v._4);
            if (either.isLeft()) {
                continue;
            }
            newList.add(either.right());
        }
    }

    public static <V1, V2, V3, V4, V5, K> void flatMap_T5_C_C(Collection<Tuple5<V1, V2, V3, V4, V5>> list, Collection<K> newList,
                                                       _5_Function<? super V1, ? super V2, ? super V3, ? super V4, ? super V5, ? extends Collection<K>> mapper) throws Exception {
        for (Tuple5<V1, V2, V3, V4, V5> v : list) {
            for (K k : mapper.apply(v._1, v._2, v._3, v._4, v._5)) {
                newList.add(k);
            }
        }
    }

    public static <V1, V2, V3, V4, V5, K> void flatMap_T5_C_A(Collection<Tuple5<V1, V2, V3, V4, V5>> list, Collection<K> newList,
                                                            _5_Function<? super V1, ? super V2, ? super V3, ? super V4, ? super V5, ? extends K[]> mapper) throws Exception {
        for (Tuple5<V1, V2, V3, V4, V5> v : list) {
            for (K k : mapper.apply(v._1, v._2, v._3, v._4, v._5)) {
                newList.add(k);
            }
        }
    }

    public static <V1, V2, V3, V4, V5, K> void flatMap_T5_C_T(Collection<Tuple5<V1, V2, V3, V4, V5>> list, Collection<K> newList,
                                                          _5_Function<? super V1, ? super V2, ? super V3, ? super V4, ? super V5, ? extends Try<K>> mapper) throws Exception {
        for (Tuple5<V1, V2, V3, V4, V5> v : list) {
            Try<K> vtry = mapper.apply(v._1, v._2, v._3, v._4, v._5);
            if (vtry.failure()) {
                continue;
            }
            newList.add(((Success<K>) vtry).getValue());
        }
    }

    public static <V1, V2, V3, V4, V5, K> void flatMap_T5_C_O(Collection<Tuple5<V1, V2, V3, V4, V5>> list, Collection<K> newList,
                                                               _5_Function<? super V1, ? super V2, ? super V3, ? super V4, ? super V5, ? extends Optional<K>> mapper) throws Exception {
        for (Tuple5<V1, V2, V3, V4, V5> v : list) {
            Optional<K> optional = mapper.apply(v._1, v._2, v._3, v._4, v._5);
            if (!optional.isPresent()) {
                continue;
            }
            newList.add((optional.get()));
        }
    }

    public static <V1, V2, V3, V4, V5, L, R> void flatMap_T5_C_E(Collection<Tuple5<V1, V2, V3, V4, V5>> list, Collection<R> newList,
                                                                _5_Function<? super V1, ? super V2, ? super V3, ? super V4, ? super V5, ? extends Either<L, R>> mapper) throws Exception {
        for (Tuple5<V1, V2, V3, V4, V5> v : list) {
            Either<L, R> either = mapper.apply(v._1, v._2, v._3, v._4, v._5);
            if (either.isLeft()) {
                continue;
            }
            newList.add(either.right());
        }
    }

    public static <V1, V2, V3, V4, V5, V6, K> void flatMap_T6_C_C(Collection<Tuple6<V1, V2, V3, V4, V5, V6>> list, Collection<K> newList,
                                                           _6_Function<? super V1, ? super V2, ? super V3, ? super V4, ? super V5, ? super V6, ? extends Collection<K>> mapper) throws Exception {
        for (Tuple6<V1, V2, V3, V4, V5, V6> v : list) {
            for (K k : mapper.apply(v._1, v._2, v._3, v._4, v._5, v._6)) {
                newList.add(k);
            }
        }
    }

    public static <V1, V2, V3, V4, V5, V6, K> void flatMap_T6_C_A(Collection<Tuple6<V1, V2, V3, V4, V5, V6>> list, Collection<K> newList,
                                                                _6_Function<? super V1, ? super V2, ? super V3, ? super V4, ? super V5, ? super V6, ? extends K[]> mapper) throws Exception {
        for (Tuple6<V1, V2, V3, V4, V5, V6> v : list) {
            for (K k : mapper.apply(v._1, v._2, v._3, v._4, v._5, v._6)) {
                newList.add(k);
            }
        }
    }
    public static <V1, V2, V3, V4, V5, V6, K> void flatMap_T6_C_T(Collection<Tuple6<V1, V2, V3, V4, V5, V6>> list, Collection<K> newList,
                                                              _6_Function<? super V1, ? super V2, ? super V3, ? super V4, ? super V5, ? super V6, ? extends Try<K>> mapper) throws Exception {
        for (Tuple6<V1, V2, V3, V4, V5, V6> v : list) {
            Try<K> vtry = mapper.apply(v._1, v._2, v._3, v._4, v._5, v._6);
            if (vtry.failure()) {
                continue;
            }
            newList.add(((Success<K>) vtry).getValue());
        }
    }

    public static <V1, V2, V3, V4, V5, V6, K> void flatMap_T6_C_O(Collection<Tuple6<V1, V2, V3, V4, V5, V6>> list, Collection<K> newList,
                                                                   _6_Function<? super V1, ? super V2, ? super V3, ? super V4, ? super V5, ? super V6, ? extends Optional<K>> mapper) throws Exception {
        for (Tuple6<V1, V2, V3, V4, V5, V6> v : list) {
            Optional<K> optional = mapper.apply(v._1, v._2, v._3, v._4, v._5, v._6);
            if (!optional.isPresent()) {
                continue;
            }
            newList.add((optional.get()));
        }
    }
    public static <V1, V2, V3, V4, V5, V6, L, R> void flatMap_T6_C_E(Collection<Tuple6<V1, V2, V3, V4, V5, V6>> list, Collection<R> newList,
                                                                    _6_Function<? super V1, ? super V2, ? super V3, ? super V4, ? super V5, ? super V6, ? extends Either<L, R>> mapper) throws Exception {
        for (Tuple6<V1, V2, V3, V4, V5, V6> v : list) {
            Either<L, R> either = mapper.apply(v._1, v._2, v._3, v._4, v._5, v._6);
            if (either.isLeft()) {
                continue;
            }
            newList.add(either.right());
        }
    }

    //filter functions
    public static <V> void filter(Collection<V> list, Collection<V> newList,
                                           _1_Predicate<V> filter) throws Exception {
        for (V v : list) {
            if (filter.test(v)) {
                newList.add(v);
            }
        }
    }

    public static <V> void filterNot(Collection<V> list,Collection<V> newList, _1_Predicate<V> filter) throws Exception{
       filter(list, newList,new _1_Predicate<V>() {
            @Override
            public boolean test(V v) throws Exception {
                return !filter.test(v);
            }
        });
    }

    //foreach functions
    public static <V> void foreach(Collection<V> list,
                                   _1_Consumer<V> consumer) throws Exception{
        for(V v:list){
            consumer.accept(v);
        }
    }

    //exists functions
    public static <V> boolean exist(Collection<V> list, _1_Predicate<V> predicate) throws Exception{
        for(V v:list){
            if(predicate.test(v)){
                return true;
            }
        }
        return false;
    }

    //find functions
    public static <V> Optional<V> find(Collection<V> list, _1_Predicate<V> predicate) throws Exception{
        for(V v:list){
            if(predicate.test(v)){
                return Optional.of(v);
            }
        }
        return Optional.empty();
    }

    //toMap
    public static <K,V> Map<K,V> toMap(Collection<Tuple2<K,V>> list){
        Map<K,V> map = new HashMap<>();
        for(Tuple2<K,V> tuple2:list){
            map.put(tuple2._1,tuple2._2);
        }
        return map;
    }


    //reduce
    public static <V> V reduce(Collection<V> list, _2_Function<? super V,? super V,? extends V> reducer) throws Exception {
        Iterator<V> iterator = list.iterator();
        V v = iterator.next();
        while (iterator.hasNext()){
            v = reducer.apply(v,iterator.next());
        }
        return v;
    }
    public static <V> V reduce(Collection<V> list, _2_Function<? super V,? super V,? extends V> reducer,V init) throws Exception {
        V v = init;
        for (V value : list) {
            v = reducer.apply(v, value);
        }
        return v;
    }

    //reduceOption
    public static <V> Optional<V> reduceOption(Collection<V> list, _2_Function<? super V,? super V,? extends V> reducer) throws Exception {
        if(list.isEmpty()){
            return Optional.empty();
        }
        return Optional.of(reduce(list,reducer));
    }

    //zipWithIndex
    public static <V> void zipWithIndex(Collection<V> list,Collection<Tuple2<V,Integer>> newList ){
        int index = 0;
        Iterator<V> iterator = list.iterator();
        while (iterator.hasNext()){
            newList.add(new Tuple2<>(iterator.next(),index++));
        }
    }

    //head
    public static <V> V head(Collection<V> collection){
        return collection.iterator().next();
    }

    //headOption
    public static <V> Optional<V> headOption(Collection<V> collection){
        Iterator<V> iterator = collection.iterator();
        if(iterator.hasNext()){
            return Optional.of(iterator.next());
        }else{
            return Optional.empty();
        }
    }

    //last
    public static <V> V last(Collection<V> collection){
        Iterator<V> iterator = collection.iterator();
        V v = iterator.next();
        while (iterator.hasNext()){
            v = iterator.next();
        }
        return v;
    }

    //lastOption
    public static <V> Optional<V> lastOption(Collection<V> collection){
        if(collection.isEmpty()){
            return Optional.empty();
        }else{
            return Optional.of(last(collection));
        }
    }


    //mkString
    public static <V> String mkString(Collection<V> collection,String start,String separator,String end){
        StringBuilder sb = new StringBuilder();
        sb.append(start);
        if(collection.isEmpty()){
            sb.append(end);
            return sb.toString();
        }
        Iterator<V> iterator = collection.iterator();
        do{
            sb.append(iterator.next());
            if(iterator.hasNext()) {
                sb.append(separator);
            }
        }while (iterator.hasNext());
        sb.append(end);
        return sb.toString();
    }
    public static <V> String mkString(Collection<V> collection,String separator){
        return mkString(collection,"",separator,"");
    }

    public static <V> void splitAt(Collection<V> list,int index,Collection<V> first,Collection<V> second){
        if(list.size()>index){
            first.addAll(list);
        }else{
            Iterator<V> iterator = list.iterator();
            int startIndex = 0;
            while (startIndex<index){
                first.add(iterator.next());
                startIndex++;
            }
            while (iterator.hasNext()){
                second.add(iterator.next());
            }
        }
    }

    //sum
    public static int sumInt(Collection<Integer> collection){
        int sum = 0;
        for(int i:collection){
            sum+=i;
        }
        return sum;
    }

    public static double sumDouble(Collection<Double> collection){
        double sum = 0;
        for(double i:collection){
            sum+=i;
        }
        return sum;
    }

    public static float sumFloat(Collection<Float> collection){
        float sum = 0;
        for(float i:collection){
            sum+=i;
        }
        return sum;
    }

    public static long sumLong(Collection<Long> collection){
        long sum = 0;
        for(long i:collection){
            sum+=i;
        }
        return sum;
    }

    public static char sumChar(Collection<Character> collection){
        char sum = 0;
        for(char i:collection){
            sum+=i;
        }
        return sum;
    }

    public static byte sumByte(Collection<Byte> collection){
        byte sum = 0;
        for(byte i:collection){
            sum+=i;
        }
        return sum;
    }

    //max
    public static int maxInt(Collection<Integer> collection){
        Iterator<Integer> iterator =  collection.iterator();
        int max = iterator.next();
        while (iterator.hasNext()){
            max = Integer.max(max,iterator.next());
        }
        return max;
    }

    public static double maxDouble(Collection<Double> collection){
        Iterator<Double> iterator = collection.iterator();
        double max = iterator.next();
        while (iterator.hasNext()){
            max = Double.max(max,iterator.next());
        }
        return max;
    }

    public static float maxFloat(Collection<Float> collection){
        Iterator<Float> iterator = collection.iterator();
        float max = iterator.next();
        while (iterator.hasNext()){
            max = Float.max(max,iterator.next());
        }
        return max;
    }

    public static long maxLong(Collection<Long> collection){
        Iterator<Long> iterator = collection.iterator();
        long max = iterator.next();
        while (iterator.hasNext()){
            max = Long.max(max,iterator.next());
        }
        return max;
    }

    public static char maxChar(Collection<Character> collection){
        Iterator<Character> iterator = collection.iterator();
        int max = iterator.next();
        while (iterator.hasNext()){
            max = Integer.max(max,iterator.next());
        }
        return (char) max;
    }

    public static byte maxByte(Collection<Byte> collection){
        Iterator<Byte> iterator = collection.iterator();
        int max = iterator.next();
        for(byte i:collection){
            max = Integer.max(max,i);
        }
        return (byte)max;
    }

    //min
    public static int minInt(Collection<Integer> collection){
        Iterator<Integer> iterator =  collection.iterator();
        int max = iterator.next();
        while (iterator.hasNext()){
            max = Integer.min(max,iterator.next());
        }
        return max;
    }

    public static double minDouble(Collection<Double> collection){
        Iterator<Double> iterator = collection.iterator();
        double max = iterator.next();
        while (iterator.hasNext()){
            max = Double.min(max,iterator.next());
        }
        return max;
    }

    public static float minFloat(Collection<Float> collection){
        Iterator<Float> iterator = collection.iterator();
        float max = iterator.next();
        while (iterator.hasNext()){
            max = Float.min(max,iterator.next());
        }
        return max;
    }

    public static long minLong(Collection<Long> collection){
        Iterator<Long> iterator = collection.iterator();
        long max = iterator.next();
        while (iterator.hasNext()){
            max = Long.min(max,iterator.next());
        }
        return max;
    }

    public static char minChar(Collection<Character> collection){
        Iterator<Character> iterator = collection.iterator();
        int max = iterator.next();
        while (iterator.hasNext()){
            max = Integer.min(max,iterator.next());
        }
        return (char) max;
    }

    public static byte minByte(Collection<Byte> collection){
        Iterator<Byte> iterator = collection.iterator();
        int max = iterator.next();
        for(byte i:collection){
            max = Integer.min(max,i);
        }
        return (byte)max;
    }

}
