package com.dongjiaqiang.jvm.dsl.java.api.codes;

import com.dongjiaqiang.jvm.dsl.java.api.extend.Either;
import com.dongjiaqiang.jvm.dsl.java.api.extend.Try;
import com.dongjiaqiang.jvm.dsl.java.api.lambda.consumer._1_Consumer;
import com.dongjiaqiang.jvm.dsl.java.api.lambda.function.*;
import com.dongjiaqiang.jvm.dsl.java.api.lambda.predicate._1_Predicate;
import com.dongjiaqiang.jvm.dsl.java.api.tuple.*;

import java.util.*;

public class _SYS_LIST_CODES {

    public static final String CLAZZ_NAME = "_SYS_LIST_CODES";


    @SafeVarargs
    public static <V> List<V> ofList(V... vs) {
        List<V> list = new ArrayList<>();
        Collections.addAll(list, vs);
        return list;
    }


    //map functions
    /**
     * arr.map(i=> { return i+1;});
     */
    public static <V,K> List<K> map(List<V> list,
                                              _1_Function<? super V,? extends K> mapper) throws Exception {
        List<K> newList = new ArrayList<>();
        _SYS_COL_CODES.map(list, newList, mapper);
        return newList;
    }

    public static <V> int length(List<V> list){
        return list.size();
    }

    public static <V1,V2,K>  List<K> map(List<Tuple2<V1,V2>> list,
                                                   _2_Function<? super V1,? super V2,? extends K> mapper) throws Exception {
        List<K> newList = new ArrayList<>();
        _SYS_COL_CODES.map(list, newList, mapper);
        return newList;
    }
    public static <V1,V2,V3,K>  List<K> map(List<Tuple3<V1,V2,V3>> list,
                                                      _3_Function<? super V1, ? super V2, ? super V3, ? extends K> mapper) throws Exception {
        List<K> newList = new ArrayList<>();
        _SYS_COL_CODES.map(list, newList, mapper);
        return newList;
    }
    public static <V1,V2,V3,V4,K>  List<K> map(List<Tuple4<V1,V2,V3,V4>> list,
                                                         _4_Function<? super V1, ? super V2, ? super V3,? super V4, ? extends K> mapper) throws Exception {
        List<K> newList = new ArrayList<>();
        _SYS_COL_CODES.map(list, newList, mapper);
        return newList;
    }
    public static <V1,V2,V3,V4,V5,K>  List<K> map(List<Tuple5<V1,V2,V3,V4,V5>> list,
                                                            _5_Function<? super V1, ? super V2, ? super V3,? super V4, ? super V5,? extends K> mapper) throws Exception {
        List<K> newList = new ArrayList<>();
        _SYS_COL_CODES.map(list, newList, mapper);
        return newList;
    }
    public static <V1,V2,V3,V4,V5,V6,K>  List<K> map(List<Tuple6<V1,V2,V3,V4,V5,V6>> list,
                                                               _6_Function<? super V1, ? super V2, ? super V3,? super V4, ? super V5,? super V6,? extends K> mapper) throws Exception {
        List<K> newList = new ArrayList<>();
        _SYS_COL_CODES.map(list, newList, mapper);
        return newList;
    }
    public static <V1,V2,V3,V4,V5,V6,V7,K>  List<K> map(List<Tuple7<V1,V2,V3,V4,V5,V6,V7>> list,
                                                                  _7_Function<? super V1, ? super V2, ? super V3,? super V4, ? super V5,? super V6,? super V7,? extends K> mapper) throws Exception {
        List<K> newList = new ArrayList<>();
        _SYS_COL_CODES.map(list, newList, mapper);
        return newList;
    }
    public static <V1,V2,V3,V4,V5,V6,V7,V8,K>  List<K> map(List<Tuple8<V1,V2,V3,V4,V5,V6,V7,V8>> list,
                                                                     _8_Function<? super V1, ? super V2, ? super V3,? super V4, ? super V5,? super V6,? super V7,? super V8,? extends K> mapper) throws Exception {
        List<K> newList = new ArrayList<>();
        _SYS_COL_CODES.map(list, newList, mapper);
        return newList;
    }

    //flatten functions
    /**
     * arr.flatten()
     */
    public static <V> List<V> flatten_CC(List<Collection<V>> list){
            List<V> newList = new ArrayList<>();
            _SYS_COL_CODES.flatten(list,newList);
            return newList;
    }
    public static <V> List<V> flatten_AC(List<V[]> list){
        List<V> newList = new ArrayList<>();
        _SYS_COL_CODES.flattenArray(list,newList);
        return newList;
    }
    public static <V> List<V> flatten_TC(List<Try<V>> list){
        List<V> newList = new ArrayList<>();
        _SYS_COL_CODES.flattenTry(list,newList);
        return newList;
    }
    public static <V,K> List<K> flatten_EC(List<Either<V,K>> list){
        List<K> newList = new ArrayList<>();
        _SYS_COL_CODES.flattenEither(list,newList);
        return newList;
    }
    public static <V> List<V> flatten_OC(List<Optional<V>> list) {
        List<V> newList = new ArrayList<>();
        _SYS_COL_CODES.flattenOptional(list, newList);
        return newList;
    }


    //flatMap functions
    /**
     * arr.flatMap(a=>{ return [1,2,a] ; });
     */
    public static <V,K> List<K> flatMap_C_C(List<V> list,
                                        _1_Function<? super V,? extends Collection<K>> mapper) throws Exception {
        List<K> newList = new ArrayList<>();
        _SYS_COL_CODES.flatMap_C_C(list,newList,mapper);
        return newList;
    }

    public static <V,K> List<K> flatMap_C_A(List<V> list,
                                            _1_Function<? super V,? extends K[]> mapper) throws Exception {
        List<K> newList = new ArrayList<>();
        _SYS_COL_CODES.flatMap_C_A(list,newList,mapper);
        return newList;
    }
    public static <V, K> List<K> flatMap_C_T(List<V> list,
                                            _1_Function<? super V, ? extends Try<K>> mapper) throws Exception {
        List<K> newList = new ArrayList<>();
        _SYS_COL_CODES.flatMap_C_T(list,newList,mapper);
        return newList;
    }

    public static <V, K> List<K> flatMap_C_O(List<V> list,_1_Function<? super V, ? extends Optional<K>> mapper) throws Exception {
        List<K> newList = new ArrayList<>();
        _SYS_COL_CODES.flatMap_C_O(list,newList,mapper);
        return newList;
    }

    public static <V, L, R> List<R> flatMap_C_E(List<V> list,_1_Function<? super V, ? extends Either<L, R>> mapper) throws Exception {
        List<R> newList = new ArrayList<>();
        _SYS_COL_CODES.flatMap_C_E(list,newList,mapper);
        return newList;
    }


    public static <V1,V2,K> List<K> flatMap_T2_C_C(List<Tuple2<V1,V2>> list,
                                                      _2_Function<? super V1,? super V2,? extends Collection<K>> mapper) throws Exception {
        List<K> newList = new ArrayList<>();
        _SYS_COL_CODES.flatMap_T2_C_C(list,newList,mapper);
        return newList;
    }
    public static <V1,V2,K> List<K> flatMap_T2_C_A(List<Tuple2<V1,V2>> list,
                                                _2_Function<? super V1,? super V2,? extends K[]> mapper) throws Exception {
        List<K> newList = new ArrayList<>();
        _SYS_COL_CODES.flatMap_T2_C_A(list,newList,mapper);
        return newList;
    }
    public static <V1, V2, K> List<K> flatMap_T2_C_T(List<Tuple2<V1, V2>> list,
                                                _2_Function<? super V1, ? super V2, ? extends Try<K>> mapper) throws Exception {
        List<K> newList = new ArrayList<>();
        _SYS_COL_CODES.flatMap_T2_C_T(list,newList,mapper);
        return newList;
    }

    public static <V1, V2, K> List<K> flatMap_T2_C_O(List<Tuple2<V1, V2>> list,
                                                     _2_Function<? super V1, ? super V2, ? extends Optional<K>> mapper) throws Exception {
        List<K> newList = new ArrayList<>();
        _SYS_COL_CODES.flatMap_T2_C_O(list,newList,mapper);
        return newList;
    }

    public static <V1, V2, L, R> List<R> flatMap_T2_C_E(List<Tuple2<V1, V2>> list,
                                                      _2_Function<? super V1, ? super V2, ? extends Either<L, R>> mapper) throws Exception {
        List<R> newList = new ArrayList<>();
        _SYS_COL_CODES.flatMap_T2_C_E(list,newList,mapper);
        return newList;
    }


    public static <V1,V2,V3,K> List<K> flatMap_T3_C_C(List<Tuple3<V1,V2,V3>> list,
                                                      _3_Function<? super V1,? super V2,? super V3,? extends Collection<K>> mapper) throws Exception {
        List<K> newList = new ArrayList<>();
       _SYS_COL_CODES.flatMap_T3_C_C(list,newList,mapper);
        return newList;
    }
    public static <V1,V2,V3,K> List<K> flatMap_T3_C_A(List<Tuple3<V1,V2,V3>> list,
                                                   _3_Function<? super V1,? super V2,? super V3,? extends K[]> mapper) throws Exception {
        List<K> newList = new ArrayList<>();
        _SYS_COL_CODES.flatMap_T3_C_A(list,newList,mapper);
        return newList;
    }
    public static <V1, V2, V3, K> List<K> flatMap_T3_C_T(List<Tuple3<V1, V2, V3>> list,
                                                    _3_Function<? super V1, ? super V2, ? super V3, ? extends Try<K>> mapper) throws Exception {
        List<K> newList = new ArrayList<>();
        _SYS_COL_CODES.flatMap_T3_C_T(list,newList,mapper);
        return newList;
    }

    public static <V1, V2, V3, K> List<K> flatMap_T3_C_O(List<Tuple3<V1, V2, V3>> list,
                                                         _3_Function<? super V1, ? super V2, ? super V3, ? extends Optional<K>> mapper) throws Exception {
        List<K> newList = new ArrayList<>();
        _SYS_COL_CODES.flatMap_T3_C_O(list,newList,mapper);
        return newList;
    }

    public static <V1, V2, V3, L, R> List<R> flatMap_T3_C_E(Set<Tuple3<V1, V2, V3>> list,
                                                          _3_Function<? super V1, ? super V2, ? super V3, ? extends Either<L, R>> mapper) throws Exception {
        List<R> newList = new ArrayList<>();
        _SYS_COL_CODES.flatMap_T3_C_E(list,newList,mapper);
        return newList;
    }

    public static <V1,V2,V3,V4,K> List<K> flatMap_T4_C_C(List<Tuple4<V1,V2,V3,V4>> list,
                                                         _4_Function<? super V1,? super V2,? super V3,? super V4,? extends Collection<K>> mapper) throws Exception {
        List<K> newList = new ArrayList<>();
        _SYS_COL_CODES.flatMap_T4_C_C(list,newList,mapper);
        return newList;
    }
    public static <V1,V2,V3,V4,K> List<K> flatMap_T4_C_A(List<Tuple4<V1,V2,V3,V4>> list,
                                                      _4_Function<? super V1,? super V2,? super V3,? super V4,? extends K[]> mapper) throws Exception {
        List<K> newList = new ArrayList<>();
        _SYS_COL_CODES.flatMap_T4_C_A(list,newList,mapper);
        return newList;
    }
    public static <V1, V2, V3, V4, K> List<K> flatMap_T4_C_T(List<Tuple4<V1, V2, V3, V4>> list,
                                                        _4_Function<? super V1, ? super V2, ? super V3, ? super V4, ? extends Try<K>> mapper) throws Exception {
        List<K> newList = new ArrayList<>();
        _SYS_COL_CODES.flatMap_T4_C_T(list,newList,mapper);
        return newList;
    }

    public static <V1, V2, V3, V4, K> List<K> flatMap_T4_C_O(List<Tuple4<V1, V2, V3, V4>> list,
                                                             _4_Function<? super V1, ? super V2, ? super V3, ? super V4, ? extends Optional<K>> mapper) throws Exception {
        List<K> newList = new ArrayList<>();
        _SYS_COL_CODES.flatMap_T4_C_O(list,newList,mapper);
        return newList;
    }

    public static <V1, V2, V3, V4, L, R> List<R> flatMap_T4_C_E(List<Tuple4<V1, V2, V3, V4>> list,
                                                              _4_Function<? super V1, ? super V2, ? super V3, ? super V4, ? extends Either<L, R>> mapper) throws Exception {
        List<R> newList = new ArrayList<>();
        _SYS_COL_CODES.flatMap_T4_C_E(list,newList,mapper);
        return newList;
    }

    public static <V1,V2,V3,V4,V5,K> List<K> flatMap_T5_C_C(List<Tuple5<V1,V2,V3,V4,V5>> list,
                                                            _5_Function<? super V1,? super V2,? super V3,? super V4,? super V5,? extends Collection<K>> mapper) throws Exception {
        List<K> newList = new ArrayList<>();
        _SYS_COL_CODES.flatMap_T5_C_C(list,newList,mapper);
        return newList;
    }

    public static <V1,V2,V3,V4,V5,K> List<K> flatMap_T5_C_A(List<Tuple5<V1,V2,V3,V4,V5>> list,
                                                         _5_Function<? super V1,? super V2,? super V3,? super V4,? super V5,? extends K[]> mapper) throws Exception {
        List<K> newList = new ArrayList<>();
        _SYS_COL_CODES.flatMap_T5_C_A(list,newList,mapper);
        return newList;
    }

    public static <V1, V2, V3, V4, V5, K> List<K> flatMap_T5_C_T(List<Tuple5<V1, V2, V3, V4, V5>> list,
                                                            _5_Function<? super V1, ? super V2, ? super V3, ? super V4, ? super V5, ? extends Try<K>> mapper) throws Exception {
        List<K> newList = new ArrayList<>();
        _SYS_COL_CODES.flatMap_T5_C_T(list,newList,mapper);
        return newList;
    }

    public static <V1, V2, V3, V4, V5, K> List<K> flatMap_T5_C_O(List<Tuple5<V1, V2, V3, V4, V5>> list,
                                                                 _5_Function<? super V1, ? super V2, ? super V3, ? super V4, ? super V5, ? extends Optional<K>> mapper) throws Exception {
        List<K> newList = new ArrayList<>();
        _SYS_COL_CODES.flatMap_T5_C_O(list,newList,mapper);
        return newList;
    }

    public static <V1, V2, V3, V4, V5, L, R> List<R> flatMap_T5_C_E(List<Tuple5<V1, V2, V3, V4, V5>> list,
                                                                  _5_Function<? super V1, ? super V2, ? super V3, ? super V4, ? super V5, ? extends Either<L, R>> mapper) throws Exception {
        List<R> newList = new ArrayList<>();
        _SYS_COL_CODES.flatMap_T5_C_E(list,newList,mapper);
        return newList;
    }


    public static <V1,V2,V3,V4,V5,V6,K> List<K> flatMap_T6_C_C(List<Tuple6<V1,V2,V3,V4,V5,V6>> list,
                                                               _6_Function<? super V1,? super V2,? super V3,? super V4,? super V5,? super V6,? extends Collection<K>> mapper) throws Exception {
        List<K> newList = new ArrayList<>();
        _SYS_COL_CODES.flatMap_T6_C_C(list,newList,mapper);
        return newList;
    }
    public static <V1,V2,V3,V4,V5,V6,K> List<K> flatMap_T6_C_A(List<Tuple6<V1,V2,V3,V4,V5,V6>> list,
                                                            _6_Function<? super V1,? super V2,? super V3,? super V4,? super V5,? super V6,? extends K[]> mapper) throws Exception {
        List<K> newList = new ArrayList<>();
        _SYS_COL_CODES.flatMap_T6_C_A(list,newList,mapper);
        return newList;
    }
    public static <V1, V2, V3, V4, V5, V6, K> List<K> flatMap_T6_C_T(List<Tuple6<V1, V2, V3, V4, V5, V6>> list,
                                                                _6_Function<? super V1, ? super V2, ? super V3, ? super V4, ? super V5, ? super V6, ? extends Try<K>> mapper) throws Exception {
        List<K> newList = new ArrayList<>();
        _SYS_COL_CODES.flatMap_T6_C_T(list,newList,mapper);
        return newList;
    }

    public static <V1, V2, V3, V4, V5, V6, K> List<K> flatMap_T6_C_O(List<Tuple6<V1, V2, V3, V4, V5, V6>> list,
                                                                     _6_Function<? super V1, ? super V2, ? super V3, ? super V4, ? super V5, ? super V6, ? extends Optional<K>> mapper) throws Exception {
        List<K> newList = new ArrayList<>();
        _SYS_COL_CODES.flatMap_T6_C_O(list,newList,mapper);
        return newList;
    }
    public static <V1, V2, V3, V4, V5, V6, L, R> List<R> flatMap_T6_C_E(List<Tuple6<V1, V2, V3, V4, V5, V6>> list,
                                                                      _6_Function<? super V1, ? super V2, ? super V3, ? super V4, ? super V5, ? super V6, ? extends Either<L, R>> mapper) throws Exception {
        List<R> newList = new ArrayList<>();
        _SYS_COL_CODES.flatMap_T6_C_E(list,newList,mapper);
        return newList;
    }


    //filter
    public static <V> List<V> filter(List<V> list, _1_Predicate<V> filter) throws Exception{
        List<V> newList = new ArrayList<>();
        _SYS_COL_CODES.filter(list,newList,filter);
        return newList;
    }
    public static <V> List<V> filterNot(List<V> list, _1_Predicate<V> filter) throws Exception{
        List<V> newList = new ArrayList<>();
        _SYS_COL_CODES.filterNot(list,newList,filter);
        return newList;

    }

    //foreach
    public static <V> void foreach(List<V> list, _1_Consumer<V> consumer) throws Exception{
        _SYS_COL_CODES.foreach(list,consumer);
    }

    //exist
    public static <V> boolean exist(List<V> list, _1_Predicate<V> predicate) throws Exception{
        return _SYS_COL_CODES.exist(list,predicate);
    }

    //find
    public static <V> Optional<V> find(List<V> list, _1_Predicate<V> predicate) throws Exception{
        return _SYS_COL_CODES.find(list,predicate);
    }


    //mkString
    public static <V> String mkString(List<V> set,String separator){
        return _SYS_COL_CODES.mkString(set,separator);
    }
    public static <V> String mkString(List<V> set,String start,String separator,String end){
        return _SYS_COL_CODES.mkString(set,start,separator,end);
    }

    //toMap
    public static <K,V> Map<K,V> toMap(List<Tuple2<K,V>> list){
        return _SYS_COL_CODES.toMap(list);
    }

    //sort
    public static <V> List<V> sort(List<V> list, Comparator<V> comparator){
        list.sort(comparator);
        return list;
    }

    //zipWithIndex
    public static <V> List<Tuple2<V,Integer>> zipWithIndex(List<V> list){
        List<Tuple2<V,Integer>>  newList = new ArrayList<>();
        _SYS_COL_CODES.zipWithIndex(list,newList);
        return newList;
    }

    //reduce
    public static <V> V reduce(List<V> list, _2_Function<? super V,? super V,? extends V> reducer) throws Exception {
       return _SYS_COL_CODES.reduce(list,reducer);
    }
    public static <V> V reduce(List<V> list, _2_Function<? super V,? super V,? extends V> reducer,V init) throws Exception {
       return _SYS_COL_CODES.reduce(list,reducer,init);
    }

    //reduceOption
    public static <V> Optional<V> reduceOption(List<V> list, _2_Function<? super V,? super V,? extends V> reducer) throws Exception {
       return _SYS_COL_CODES.reduceOption(list,reducer);
    }


    public static <V> List<V> toList(List<V> list){
        List<V> newList = new ArrayList<>(list.size());
        newList.addAll(list);
        return newList;
    }

    public static <V> Set<V> toSet(List<V> list){
        return new HashSet<>(list);
    }

    public static <V> Set<V> toSortedSet(List<V> list, Comparator<V> comparator){
        TreeSet<V> treeSet =  new TreeSet<>(comparator);
        treeSet.addAll(list);
        return treeSet;
    }

    public static <V extends Comparator<V>> Set<V> toSortedSet(List<V> list){
        TreeSet<V> treeSet =  new TreeSet<>();
        treeSet.addAll(list);
        return treeSet;
    }

    public static <V> Set<V> toSeqSet(List<V> list){
        return new LinkedHashSet<>(list);
    }

    public static <K,V> Map<K,V> toLinkedMap(List<Tuple2<K,V>> list){
        Map<K,V> map = new LinkedHashMap<>();
        for(Tuple2<K,V> tuple2:list){
            map.put(tuple2._1,tuple2._2);
        }
        return map;
    }

    public static <V> Tuple2<List<V>,List<V>> splitAt(List<V> list,int index){
        List<V> first = new ArrayList<>();
        List<V> second = new ArrayList<>();
        _SYS_COL_CODES.splitAt(list,index,first,second);
        return new Tuple2<>(first,second);
    }
    @SuppressWarnings("unchecked")
    public static <V> V[] toArray(List<V> list){
        return (V[]) list.toArray();
    }

    public static <V> List<V> reverse(List<V> list){
        List<V> newList = new ArrayList<>();
        for(int i = list.size()-1;i>=0;i--){
            newList.add(list.get(i));
        }
        return newList;
    }

}
