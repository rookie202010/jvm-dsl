package com.dongjiaqiang.jvm.dsl.java.api.codes;

import com.dongjiaqiang.jvm.dsl.java.api.extend.Either;
import com.dongjiaqiang.jvm.dsl.java.api.extend.Success;
import com.dongjiaqiang.jvm.dsl.java.api.extend.Try;
import com.dongjiaqiang.jvm.dsl.java.api.lambda.consumer._1_Consumer;
import com.dongjiaqiang.jvm.dsl.java.api.lambda.function.*;
import com.dongjiaqiang.jvm.dsl.java.api.lambda.predicate._1_Predicate;
import com.dongjiaqiang.jvm.dsl.java.api.tuple.*;

import java.util.*;

public class _SYS_SET_CODES {

    public static final String CLAZZ_NAME = "_SYS_SET_CODES";

    //map functions

    /**
     * arr.map(i=> { return i+1;});
     */
    public static <V,K> Set<K> map(Set<V> list,
                                   Set<K> newList,
                                    _1_Function<? super V,? extends K> mapper) throws Exception {
        _SYS_COL_CODES.map(list, newList, mapper);
        return newList;
    }

    public static <V1,V2,K>  Set<K> map(Set<Tuple2<V1,V2>> list,
                                        Set<K> newList,
                                         _2_Function<? super V1,? super V2,? extends K> mapper) throws Exception {
        _SYS_COL_CODES.map(list, newList, mapper);
        return newList;
    }
    public static <V1,V2,V3,K>  Set<K> map(Set<Tuple3<V1,V2,V3>> list,
                                           Set<K> newList,
                                            _3_Function<? super V1, ? super V2, ? super V3, ? extends K> mapper) throws Exception {
        _SYS_COL_CODES.map(list, newList, mapper);
        return newList;
    }
    public static <V1,V2,V3,V4,K>  Set<K> map(Set<Tuple4<V1,V2,V3,V4>> list,
                                              Set<K> newList,
                                               _4_Function<? super V1, ? super V2, ? super V3,? super V4, ? extends K> mapper) throws Exception {
        _SYS_COL_CODES.map(list, newList, mapper);
        return newList;
    }
    public static <V1,V2,V3,V4,V5,K>  Set<K> map(Set<Tuple5<V1,V2,V3,V4,V5>> list,
                                                 Set<K> newList,
                                                  _5_Function<? super V1, ? super V2, ? super V3,? super V4, ? super V5,? extends K> mapper) throws Exception {
        _SYS_COL_CODES.map(list, newList, mapper);
        return newList;
    }
    public static <V1,V2,V3,V4,V5,V6,K>  Set<K> map(Set<Tuple6<V1,V2,V3,V4,V5,V6>> list,
                                                    Set<K> newList,
                                                               _6_Function<? super V1, ? super V2, ? super V3,? super V4, ? super V5,? super V6,? extends K> mapper) throws Exception {
        _SYS_COL_CODES.map(list, newList, mapper);
        return newList;
    }
    public static <V1,V2,V3,V4,V5,V6,V7,K>  Set<K> map(Set<Tuple7<V1,V2,V3,V4,V5,V6,V7>> list,
                                                       Set<K> newList,
                                                                  _7_Function<? super V1, ? super V2, ? super V3,? super V4, ? super V5,? super V6,? super V7,? extends K> mapper) throws Exception {
        _SYS_COL_CODES.map(list, newList, mapper);
        return newList;
    }
    public static <V1,V2,V3,V4,V5,V6,V7,V8,K>  Set<K> map(List<Tuple8<V1,V2,V3,V4,V5,V6,V7,V8>> list,
                                                          Set<K> newList,
                                                                     _8_Function<? super V1, ? super V2, ? super V3,? super V4, ? super V5,? super V6,? super V7,? super V8,? extends K> mapper) throws Exception {
        _SYS_COL_CODES.map(list, newList, mapper);
        return newList;
    }

    //flatten functions
    /**
     * arr.flatten()
     */
    public static <V> Set<V> flatten_CC(Set<Collection<V>> list,Set<V> newList){
        _SYS_COL_CODES.flatten(list,newList);
        return newList;
    }
    public static <V> Set<V> flatten_AC(Set<V[]> list,Set<V> newList){
        _SYS_COL_CODES.flattenArray(list,newList);
        return newList;
    }
    public static <V> Set<V> flatten_TC(Set<Try<V>> list,Set<V> newList){
        _SYS_COL_CODES.flattenTry(list,newList);
        return newList;
    }
    public static <V,K> Set<K> flatten_EC(Set<Either<V,K>> list,Set<K> newList){
        _SYS_COL_CODES.flattenEither(list,newList);
        return newList;
    }
    public static <V> Set<V> flatten_OC(Set<Optional<V>> list,Set<V> newList){
        _SYS_COL_CODES.flattenOptional(list, newList);
        return newList;
    }

    //flatMap
    public static <V,K> Set<K> flatMap_C_C(Set<V> list,
                                       Set<K> newList,
                                       _1_Function<? super V,? extends Collection<K>> mapper) throws Exception {
        _SYS_COL_CODES.flatMap_C_C(list,newList,mapper);
        return newList;
    }
    public static <V,K> Set<K> flatMap_C_A(Set<V> list,
                                       Set<K> newList,
                                       _1_Function<? super V,? extends K[]> mapper) throws Exception {
        _SYS_COL_CODES.flatMap_C_A(list,newList,mapper);
        return newList;
    }
    public static <V, K> Set<K> flatMap_C_T(Set<V> list, Set<K> newList, _1_Function<? super V, ? extends Try<K>> mapper) throws Exception {
        _SYS_COL_CODES.flatMap_C_T(list,newList,mapper);
        return newList;
    }

    public static <V, K> Set<K> flatMap_C_O(Set<V> list, Set<K> newList, _1_Function<? super V, ? extends Optional<K>> mapper) throws Exception {
        _SYS_COL_CODES.flatMap_C_O(list,newList,mapper);
        return newList;
    }

    public static <V, L, R> Set<R> flatMap_C_E(Set<V> list, Set<R> newList, _1_Function<? super V, ? extends Either<L, R>> mapper) throws Exception {
        _SYS_COL_CODES.flatMap_C_E(list,newList,mapper);
        return newList;
    }


    public static <V, K> Set<K> flatMap_OC_C(Set<Optional<V>> list,
                                           Set<K> newList, _1_Function<? super Optional<V>, ? extends Collection<K>> mapper) throws Exception {
        for (Optional<V> v : list) {
            for (K k : mapper.apply(v)) {
                newList.add(k);
            }
        }
        return newList;
    }

    public static <V, K> Set<K> flatMap_OC_A(Set<Optional<V>> list,
                                           Set<K> newList, _1_Function<? super Optional<V>, ? extends K[]> mapper) throws Exception {
        for (Optional<V> v : list) {
            for (K k : mapper.apply(v)) {
                newList.add(k);
            }
        }
        return newList;
    }

    public static <V, K> Set<K> flatMap_OC_T(Set<Optional<V>> list,
                                           Set<K> newList, _1_Function<? super Optional<V>, ? extends Try<K>> mapper) throws Exception {
        for (Optional<V> v : list) {
            Try<K> vtry = mapper.apply(v);
            if (vtry.failure()) {
                continue;
            }
            newList.add(((Success<K>) vtry).getValue());
        }
        return newList;
    }

    public static <V, K> Set<K> flatMap_OC_O(Set<Optional<V>> list,
                                           Set<K> newList, _1_Function<? super Optional<V>, ? extends Optional<K>> mapper) throws Exception {
        for (Optional<V> v : list) {
            Optional<K> optional = mapper.apply(v);
            if (!optional.isPresent()) {
                continue;
            }
            newList.add((optional.get()));
        }
        return newList;
    }

    public static <V, L, R> Set<R> flatMap_OC_E(Set<Optional<V>> list,
                                              Set<R> newList, _1_Function<? super Optional<V>, ? extends Either<L, R>> mapper) throws Exception {
        for (Optional<V> v : list) {
            Either<L, R> either = mapper.apply(v);
            if (either.isLeft()) {
                continue;
            }
            newList.add(either.right());
        }
        return newList;
    }

    public static <V1,V2,K> Set<K> flatMap_T2_C_C(Set<Tuple2<V1,V2>> list,
                                                     Set<K> newList,
                                                     _2_Function<? super V1,? super V2,? extends Collection<K>> mapper) throws Exception {
        _SYS_COL_CODES.flatMap_T2_C_C(list,newList,mapper);
        return newList;
    }
    public static <V1,V2,K> Set<K> flatMap_T2_C_A(Set<Tuple2<V1,V2>> list,
                                           Set<K> newList,
                                           _2_Function<? super V1,? super V2,? extends K[]> mapper) throws Exception {
        _SYS_COL_CODES.flatMap_T2_C_A(list,newList,mapper);
        return newList;
    }
    public static <V1, V2, K> Set<K> flatMap_T2_C_T(Set<Tuple2<V1, V2>> list,
                                              Set<K> newList,
                                              _2_Function<? super V1, ? super V2, ? extends Try<K>> mapper) throws Exception {
       _SYS_COL_CODES.flatMap_T2_C_T(list,newList,mapper);
       return newList;
    }

    public static <V1, V2, K> Set<K> flatMap_T2_C_O(Set<Tuple2<V1, V2>> list,
                                                   Set<K> newList,
                                                     _2_Function<? super V1, ? super V2, ? extends Optional<K>> mapper) throws Exception {
        _SYS_COL_CODES.flatMap_T2_C_O(list,newList,mapper);
        return newList;
    }

    public static <V1, V2, L, R> Set<R> flatMap_T2_C_E(Set<Tuple2<V1, V2>> list, Set<R> newList,
                                                    _2_Function<? super V1, ? super V2, ? extends Either<L, R>> mapper) throws Exception {
        _SYS_COL_CODES.flatMap_T2_C_E(list,newList,mapper);
        return newList;
    }

    public static <V1,V2,V3,K> Set<K> flatMap_T3_C_C(Set<Tuple3<V1,V2,V3>> list,
                                                        Set<K> newList,
                                                        _3_Function<? super V1,? super V2,? super V3,? extends Collection<K>> mapper) throws Exception {
        _SYS_COL_CODES.flatMap_T3_C_C(list,newList,mapper);
        return newList;
    }
    public static <V1,V2,V3,K> Set<K> flatMap_T3_C_A(Set<Tuple3<V1,V2,V3>> list,
                                                        Set<K> newList,
                                                        _3_Function<? super V1,? super V2,? super V3,? extends K[]> mapper) throws Exception {
        _SYS_COL_CODES.flatMap_T3_C_A(list,newList,mapper);
        return newList;
    }
    public static <V1, V2, V3, K> Set<K> flatMap_T3_C_T(Set<Tuple3<V1, V2, V3>> list, Set<K> newList,
                                                  _3_Function<? super V1, ? super V2, ? super V3, ? extends Try<K>> mapper) throws Exception {
        _SYS_COL_CODES.flatMap_T3_C_T(list,newList,mapper);
        return newList;
    }

    public static <V1, V2, V3, K> Set<K> flatMap_T3_C_O(Set<Tuple3<V1, V2, V3>> list, Set<K> newList,
                                                       _3_Function<? super V1, ? super V2, ? super V3, ? extends Optional<K>> mapper) throws Exception {
       _SYS_COL_CODES.flatMap_T3_C_O(list,newList,mapper);
       return newList;
    }

    public static <V1, V2, V3, L, R> Set<R> flatMap_T3_C_E(Set<Tuple3<V1, V2, V3>> list, Set<R> newList,
                                                        _3_Function<? super V1, ? super V2, ? super V3, ? extends Either<L, R>> mapper) throws Exception {
        _SYS_COL_CODES.flatMap_T3_C_E(list,newList,mapper);
        return newList;
    }

    public static <V1,V2,V3,V4,K> Set<K> flatMap_T4_C_C(Set<Tuple4<V1,V2,V3,V4>> list,
                                                           Set<K> newList,
                                                         _4_Function<? super V1,? super V2,? super V3,? super V4,? extends Collection<K>> mapper) throws Exception {
        _SYS_COL_CODES.flatMap_T4_C_C(list,newList,mapper);
        return newList;
    }
    public static <V1,V2,V3,V4,K> Set<K> flatMap_T4_C_A(Set<Tuple4<V1,V2,V3,V4>> list,
                                                 Set<K> newList,
                                                 _4_Function<? super V1,? super V2,? super V3,? super V4,? extends K[]> mapper) throws Exception {
        _SYS_COL_CODES.flatMap_T4_C_A(list,newList,mapper);
        return newList;
    }
    public static <V1, V2, V3, V4, K> Set<K> flatMap_T4_C_T(Set<Tuple4<V1, V2, V3, V4>> list, Set<K> newList,
                                                      _4_Function<? super V1, ? super V2, ? super V3, ? super V4, ? extends Try<K>> mapper) throws Exception {
       _SYS_COL_CODES.flatMap_T4_C_T(list,newList,mapper);
       return newList;
    }

    public static <V1, V2, V3, V4, K> Set<K> flatMap_T4_C_O(Set<Tuple4<V1, V2, V3, V4>> list, Set<K> newList,
                                                           _4_Function<? super V1, ? super V2, ? super V3, ? super V4, ? extends Optional<K>> mapper) throws Exception {
       _SYS_COL_CODES.flatMap_T4_C_O(list,newList,mapper);
       return newList;
    }

    public static <V1, V2, V3, V4, L, R> Set<R> flatMap_T4_C_E(Set<Tuple4<V1, V2, V3, V4>> list, Set<R> newList,
                                                            _4_Function<? super V1, ? super V2, ? super V3, ? super V4, ? extends Either<L, R>> mapper) throws Exception {
        _SYS_COL_CODES.flatMap_T4_C_E(list,newList,mapper);
        return newList;
    }

    public static <V1,V2,V3,V4,V5,K> Set<K> flatMap_T5_C_C(Set<Tuple5<V1,V2,V3,V4,V5>> list,
                                                    Set<K> newList,
                                                    _5_Function<? super V1,? super V2,? super V3,? super V4,? super V5,? extends Collection<K>> mapper) throws Exception {
        _SYS_COL_CODES.flatMap_T5_C_C(list,newList,mapper);
        return newList;
    }
    public static <V1,V2,V3,V4,V5,K> Set<K> flatMap_T5_C_A(Set<Tuple5<V1,V2,V3,V4,V5>> list,
                                                    Set<K> newList,
                                                    _5_Function<? super V1,? super V2,? super V3,? super V4,? super V5,? extends K[]> mapper) throws Exception {
        _SYS_COL_CODES.flatMap_T5_C_A(list,newList,mapper);
        return newList;
    }

    public static <V1, V2, V3, V4, V5, K> Set<K> flatMap_T5_C_T(Set<Tuple5<V1, V2, V3, V4, V5>> list, Set<K> newList,
                                                          _5_Function<? super V1, ? super V2, ? super V3, ? super V4, ? super V5, ? extends Try<K>> mapper) throws Exception {
       _SYS_COL_CODES.flatMap_T5_C_T(list,newList,mapper);
       return newList;
    }

    public static <V1, V2, V3, V4, V5, K> Set<K> flatMap_T5_C_O(Set<Tuple5<V1, V2, V3, V4, V5>> list, Set<K> newList,
                                                               _5_Function<? super V1, ? super V2, ? super V3, ? super V4, ? super V5, ? extends Optional<K>> mapper) throws Exception {
       _SYS_COL_CODES.flatMap_T5_C_O(list,newList,mapper);
       return newList;
    }

    public static <V1, V2, V3, V4, V5, L, R> Set<R> flatMap_T5_C_E(Set<Tuple5<V1, V2, V3, V4, V5>> list, Set<R> newList,
                                                                _5_Function<? super V1, ? super V2, ? super V3, ? super V4, ? super V5, ? extends Either<L, R>> mapper) throws Exception {
        _SYS_COL_CODES.flatMap_T5_C_E(list,newList,mapper);
        return newList;
    }

    public static <V1,V2,V3,V4,V5,V6,K> Set<K> flatMap_T6_C_C(Set<Tuple6<V1,V2,V3,V4,V5,V6>> list,
                                                       Set<K> newList,
                                                       _6_Function<? super V1,? super V2,? super V3,? super V4,? super V5,? super V6,? extends Collection<K>> mapper) throws Exception {
        _SYS_COL_CODES.flatMap_T6_C_C(list,newList,mapper);
        return newList;
    }
    public static <V1,V2,V3,V4,V5,V6,K> Set<K> flatMap_T6_C_A(Set<Tuple6<V1,V2,V3,V4,V5,V6>> list,
                                                       Set<K> newList,
                                                       _6_Function<? super V1,? super V2,? super V3,? super V4,? super V5,? super V6,? extends K[]> mapper) throws Exception {
        _SYS_COL_CODES.flatMap_T6_C_A(list,newList,mapper);
        return newList;
    }
    public static <V1, V2, V3, V4, V5, V6, K> Set<K> flatMap_T6_C_T(Set<Tuple6<V1, V2, V3, V4, V5, V6>> list, Set<K> newList,
                                                              _6_Function<? super V1, ? super V2, ? super V3, ? super V4, ? super V5, ? super V6, ? extends Try<K>> mapper) throws Exception {
        _SYS_COL_CODES.flatMap_T6_C_T(list,newList,mapper);
        return newList;
    }

    public static <V1, V2, V3, V4, V5, V6, K> Set<K> flatMap_T6_C_O(Set<Tuple6<V1, V2, V3, V4, V5, V6>> list, Set<K> newList,
                                                                   _6_Function<? super V1, ? super V2, ? super V3, ? super V4, ? super V5, ? super V6, ? extends Optional<K>> mapper) throws Exception {
        _SYS_COL_CODES.flatMap_T6_C_O(list,newList,mapper);
        return newList;
    }
    public static <V1, V2, V3, V4, V5, V6, L, R> Set<R> flatMap_T6_C_E(Set<Tuple6<V1, V2, V3, V4, V5, V6>> list, Set<R> newList,
                                                                    _6_Function<? super V1, ? super V2, ? super V3, ? super V4, ? super V5, ? super V6, ? extends Either<L, R>> mapper) throws Exception {
        _SYS_COL_CODES.flatMap_T6_C_E(list,newList,mapper);
        return newList;
    }

    //filter functions
    public static <V> Set<V> filter(Set<V> list, Set<V> newSet,
                                    _1_Predicate<V> filter) throws Exception{
        _SYS_COL_CODES.filter(list,newSet,filter);
        return newSet;
    }

    //foreach functions
    public static <V> void foreach(Set<V> list,
                                   _1_Consumer<V> consumer) throws Exception{
       _SYS_COL_CODES.foreach(list,consumer);
    }

    //exist
    public static <V> boolean exist(Set<V> list, _1_Predicate<V> predicate) throws Exception{
        return _SYS_COL_CODES.exist(list,predicate);
    }

    //find
    public static <V> Optional<V> find(Set<V> list, _1_Predicate<V> predicate) throws Exception{
        return _SYS_COL_CODES.find(list,predicate);
    }


    //toMap
    public static <K,V> Map<K,V> toMap(Set<Tuple2<K,V>> list){
        return _SYS_COL_CODES.toMap(list);
    }

    //reduce
    public static <V> V reduce(Set<V> list, _2_Function<? super V,? super V,? extends V> reducer) throws Exception {
        return _SYS_COL_CODES.reduce(list,reducer);
    }
    public static <V> V reduce(Set<V> list, _2_Function<? super V,? super V,? extends V> reducer,V init) throws Exception {
        return _SYS_COL_CODES.reduce(list,reducer,init);
    }

    //reduceOption
    public static <V> Optional<V> reduceOption(Set<V> list, _2_Function<? super V,? super V,? extends V> reducer) throws Exception {
        return _SYS_COL_CODES.reduceOption(list,reducer);
    }

    //splitAt
    public static <V> Tuple2<Set<V>,Set<V>> splitAt(Set<V> list,int index,Set<V> first,Set<V> second){
        _SYS_COL_CODES.splitAt(list,index,first,second);
        return new Tuple2<>(first,second);
    }

    //head
    public static <V> V head(Set<V> set){
        return _SYS_COL_CODES.head(set);
    }
    public static <V> Optional<V> headOption(Set<V> set){
        return _SYS_COL_CODES.headOption(set);
    }

    //last
    public static <V> V last(Set<V> set){
        return _SYS_COL_CODES.last(set);
    }
    public static <V> Optional<V> lastOption(Set<V> set){
        return _SYS_COL_CODES.lastOption(set);
    }

    //mkString
    public static <V> String mkString(Set<V> set,String separator){
        return _SYS_COL_CODES.mkString(set,separator);
    }
    public static <V> String mkString(Set<V> set,String start,String separator,String end){
        return _SYS_COL_CODES.mkString(set,start,separator,end);
    }

    //set builder functions
    @SafeVarargs
    public static <V> Set<V> ofSet(V... vs) {
        Set<V> set = new HashSet<>();
        Collections.addAll(set, vs);
        return set;
    }

    public static <V> Set<V> toSet(Set<V> set) {
        return new HashSet<>(set);
    }

    @SafeVarargs
    public static <V extends Comparable<V>> Set<V> ofSortedSet(V... vs) {
        Set<V> set = new TreeSet<>();
        Collections.addAll(set, vs);
        return set;
    }


    @SafeVarargs
    public static <V> Set<V> ofSortedSet(Comparator<V> comparator,V... vs) {
        Set<V> set = new TreeSet<>(comparator);
        Collections.addAll(set, vs);
        return set;
    }


    public static <V> Set<V> toSortedSet(Set<V> set,Comparator<V> comparator) {
        Set<V> newSet = new TreeSet<>(comparator);
        newSet.addAll(set);
        return newSet;
    }

    public static <V extends Comparable<V>> Set<V> toSortedSet(Set<V> set) {
        return new TreeSet<>(set);
    }


    public static <V> Set<V> toSeqSet(Set<V> set) {
        return new LinkedHashSet<>(set);
    }

    @SafeVarargs
    public static <V> Set<V> ofSeqSet(V... vs) {
        Set<V> set = new LinkedHashSet<>();
        Collections.addAll(set, vs);
        return set;
    }
}
