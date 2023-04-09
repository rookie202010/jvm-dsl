package com.dongjiaqiang.jvm.dsl.java.api.codes;

import com.dongjiaqiang.jvm.dsl.java.api.extend.Either;
import com.dongjiaqiang.jvm.dsl.java.api.extend.Success;
import com.dongjiaqiang.jvm.dsl.java.api.extend.Try;
import com.dongjiaqiang.jvm.dsl.java.api.lambda.consumer._1_Consumer;
import com.dongjiaqiang.jvm.dsl.java.api.lambda.function.*;
import com.dongjiaqiang.jvm.dsl.java.api.lambda.predicate._1_Predicate;
import com.dongjiaqiang.jvm.dsl.java.api.tuple.*;

import java.util.*;

public class _SYS_ARRAY_CODES {


    public static final String CLAZZ_NAME = "_SYS_ARRAY_CODES";

    //map functions
    /*
      arr.map(i=> { return i+1;});
     */
    public static <V,K> K[] map(V[] arr,K[] newArr,
                                             _1_Function<? super V,? extends K> mapper) throws Exception {
      //  Object[] newArr = new Object[arr.length];
        for(int i=0;i<arr.length;i++){
            newArr[i] = mapper.apply(arr[i]);
        }
        return newArr;
    }
    /**
     * arr.map((i,j)=>{ return i+j;});
     */
    @SuppressWarnings("unchecked")
    public static <V1,V2,K> K[] map(Tuple2<V1,V2>[] arr,K[] newArr,
                                        _2_Function<? super V1,? super V2,? extends K> mapper) throws Exception {
        for(int i=0;i<arr.length;i++){
            newArr[i] = mapper.apply(arr[i]._1,arr[i]._2);
        }
        return newArr;
    }
    /**
     * arr.map((i1,i2,i3)=>{ return i1+i2+i3;});
     */
    @SuppressWarnings("unchecked")
    public static <V1,V2,V3,K> K[] map(Tuple3<V1,V2,V3>[] arr,K[] newArr,
                                        _3_Function<? super V1,? super V2,? super V3,? extends K> mapper) throws Exception {
        for(int i=0;i<arr.length;i++){
            newArr[i] = mapper.apply(arr[i]._1,arr[i]._2,arr[i]._3);
        }
        return newArr;
    }
    /**
     * arr.map((i1,i2,i3,i4)=>{ return i1+i2+i3+i4;});
     */
    @SuppressWarnings("unchecked")
    public static <V1,V2,V3,V4,K> K[] map(Tuple4<V1,V2,V3,V4>[] arr,K[] newArr,
                                           _4_Function<? super V1,? super V2,? super V3,? super V4,? extends K> mapper) throws Exception {
        for(int i=0;i<arr.length;i++){
            newArr[i] = mapper.apply(arr[i]._1,arr[i]._2,arr[i]._3,arr[i]._4);
        }
        return newArr;
    }
    /**
     * arr.map((i1,i2,i3,i4,i5)=>{ return i1+i2+i3+i4+i5;});
     */
    @SuppressWarnings("unchecked")
    public static <V1,V2,V3,V4,V5,K> K[] map(Tuple5<V1,V2,V3,V4,V5>[] arr,K[] newArr,
                                              _5_Function<? super V1,? super V2,? super V3,? super V4,? super V5,? extends K> mapper) throws Exception {
        for(int i=0;i<arr.length;i++){
            newArr[i] = mapper.apply(arr[i]._1,arr[i]._2,arr[i]._3,arr[i]._4,arr[i]._5);
        }
        return newArr;
    }
    /**
     * arr.map((i1,i2,i3,i4,i5,i6)=>{ return i1+i2+i3+i4+i5+i6;});
     */
    @SuppressWarnings("unchecked")
    public static <V1,V2,V3,V4,V5,V6,K> K[] map(Tuple6<V1,V2,V3,V4,V5,V6>[] arr,K[] newArr,
                                                 _6_Function<? super V1,? super V2,? super V3,? super V4,? super V5,? super V6,? extends K> mapper) throws Exception {
        for(int i=0;i<arr.length;i++){
            newArr[i] = mapper.apply(arr[i]._1,arr[i]._2,arr[i]._3,arr[i]._4,arr[i]._5,arr[i]._6);
        }
        return newArr;
    }
    /**
     * arr.map((i1,i2,i3,i4,i5,i6,i7)=>{ return i1+i2+i3+i4+i5+i6+i7;});
     */
    @SuppressWarnings("unchecked")
    public static <V1,V2,V3,V4,V5,V6,V7,K> K[] map(Tuple7<V1,V2,V3,V4,V5,V6,V7>[] arr,K[] newArr,
                                                 _7_Function<? super V1,? super V2,? super V3,? super V4,? super V5,? super V6,? super V7,? extends K> mapper) throws Exception {
        for(int i=0;i<arr.length;i++){
            newArr[i] = mapper.apply(arr[i]._1,arr[i]._2,arr[i]._3,arr[i]._4,arr[i]._5,arr[i]._6,arr[i]._7);
        }
        return newArr;
    }
    /**
     * arr.map((i1,i2,i3,i4,i5,i6,i7,i8)=>{ return i1+i2+i3+i4+i5+i6+i7+i8;});
     */
    @SuppressWarnings("unchecked")
    public static <V1,V2,V3,V4,V5,V6,V7,V8,K> K[] map(Tuple8<V1,V2,V3,V4,V5,V6,V7,V8>[] arr,K[] newArr,
                                                    _8_Function<? super V1,? super V2,? super V3,? super V4,? super V5,? super V6,? super V7,? super V8,? extends K> mapper) throws Exception {
        for(int i=0;i<arr.length;i++){
            newArr[i] = mapper.apply(arr[i]._1,arr[i]._2,arr[i]._3,arr[i]._4,arr[i]._5,arr[i]._6,arr[i]._7,arr[i]._8);
        }
        return newArr;
    }

    //flatten functions
    /**
     * arr.flatten()
     */
    @SuppressWarnings("unchecked")
    public static <V> V[] flatten(Collection<V>[] arr){
        int size = 0;
        for(Collection<V> collection:arr){
            size+=collection.size();
        }
        Object[] newArr = new Object[size];
        int index = 0;
        for(Collection<V> collection:arr) {
            for (V v : collection) {
                newArr[index++] = v;
            }
        }
        return (V[])newArr;
    }
    @SuppressWarnings("unchecked")
    public static <V> V[]  flattenArray(V[][] arr){
        int size = 0;
        for(V[] vs:arr){
            size+=vs.length;
        }
        Object[] newArr = new Object[size];
        int index = 0;
        for(V[] vs:arr) {
            for (V v : vs) {
                newArr[index++] = v;
            }
        }
        return (V[])newArr;
    }
    @SuppressWarnings("unchecked")
    public static <V> V[] flattenTry(Try<V>[] arr){
        int size = 0;
        for(Try<V> vTry:arr){
            if(vTry.success()){
                size++;
            }
        }
        Object[] newArr = new Object[size];
        int index = 0;
        for(Try<V> vTry:arr) {
            if(vTry.success()){
                newArr[index++] = ((Success<V>)vTry).getValue();
            }
        }
        return (V[])newArr;
    }
    @SuppressWarnings("unchecked")
    public static <V,K> K[] flattenEither(Either<V,K>[] arr){
        int size = 0;
        for(Either<V,K> either:arr){
            if(either.isRight()){
                size++;
            }
        }
        Object[] newArr = new Object[size];
        int index = 0;
        for(Either<V,K> either:arr){
            if(either.isRight()){
                newArr[index++] = either.right();
            }
        }
        return (K[])newArr;
    }
    @SuppressWarnings("unchecked")
    public static <V> V[] flattenOptional(Optional<V>[] arr){
        int size = 0;
        for(Optional<V> optional:arr){
            if(optional.isPresent()){
                size++;
            }
        }
        Object[] newArr = new Object[size];
        int index = 0;
        for(Optional<V> optional:arr){
            if (!optional.isPresent()) {
                continue;
            }
            newArr[index++] = optional.get();
        }
        return (V[])newArr;
    }

    //flatMap functions

    public static <V,K> List<K> flatMap_A_C(V[] arr,
                                 _1_Function<? super V,? extends Collection<K>> mapper) throws Exception {
        List<K> list = new ArrayList<>();
        for (V v : arr) {
            for (K k : mapper.apply(v)) {
                list.add(k);
            }
        }
        return list;
    }

    public static <V,K> List<K> flatMap_A_A(V[] arr,
                                _1_Function<? super V,? extends K[]> mapper) throws Exception {
        List<K> list = new ArrayList<>();
        for (V v : arr) {
            for (K k : mapper.apply(v)) {
                list.add(k);
            }
        }
        return list;
    }

    public static <V,K> List<K> flatMap_A_O(V[] arr,
                                         _1_Function<? super V,? extends Optional<K>> mapper) throws Exception {
        List<K> list = new ArrayList<>();
        for (V v : arr) {
            Optional<K> optional = mapper.apply(v);
            if (!optional.isPresent()) {
                continue;
            }
            list.add(optional.get());
        }
        return list;
    }
    /**
     * arr.flatMap(a=> { return Success(a) ; });
     */
    public static <V,K> List<K> flatMap_A_T(V[] arr,
                                            _1_Function<? super V,? extends Try<K>> mapper) throws Exception {
        List<K> list = new ArrayList<>();
        for (V v : arr) {
            Try<K> vtry = mapper.apply(v);
            if (vtry.failure()) {
                continue;
            }
            list.add(((Success<K>)vtry).getValue());
        }
        return list;
    }

    public static <V,L,R> List<R> flatMap_A_E(V[] arr,
                                            _1_Function<? super V,? extends Either<L,R>> mapper) throws Exception {
        List<R> list = new ArrayList<>();
        for (V v : arr) {
            Either<L,R> either= mapper.apply(v);
            if (either.isLeft()) {
                continue;
            }
            list.add(either.right());
        }
        return list;
    }

    public static <V1,V2,K> List<K> flatMap_T2_A_C(Tuple2<V1,V2>[] oldArr,
                                                      _2_Function<? super V1,? super V2,? extends Collection<K>> mapper) throws Exception {
        List<K> list;
        list = new ArrayList<>();
        for (Tuple2<V1,V2> v : oldArr) {
            for (K k : mapper.apply(v._1,v._2)) {
                list.add(k);
            }
        }
        return list;
    }

    public static <V1,V2,K> List<K> flatMap_T2_A_A(Tuple2<V1,V2>[] oldArr,
                                        _2_Function<? super V1,? super V2,? extends K[]> mapper) throws Exception {
        List<K> list = new ArrayList<>();
        for (Tuple2<V1,V2> v : oldArr) {
            for (K k : mapper.apply(v._1,v._2)) {
                list.add(k);
            }
        }
        return list;
    }

    public static <V1,V2,K> List<K> flatMap_T2_A_O(Tuple2<V1,V2>[] oldArr,
                                             _2_Function<? super V1,? super V2,? extends Optional<K>> mapper) throws Exception {
        List<K> list = new ArrayList<>();
        for (Tuple2<V1,V2> v : oldArr) {
            Optional<K> optional = mapper.apply(v._1,v._2);
            if (!optional.isPresent()) {
                continue;
            }
            list.add(optional.get());
        }
        return list;
    }

    public static <V1,V2,L,R> List<R> flatMap_T2_A_E(Tuple2<V1,V2>[] oldArr,
                                                _2_Function<? super V1,? super V2,? extends Either<L,R>> mapper) throws Exception {
        List<R> list = new ArrayList<>();
        for (Tuple2<V1,V2> v : oldArr) {
            Either<L,R> either= mapper.apply(v._1,v._2);
            if (either.isLeft()) {
                continue;
            }
            list.add(either.right());
        }
        return list;
    }



    public static <V1,V2,V3,K> List<K> flatMap_T3_A_C(Tuple3<V1,V2,V3>[] oldArr,
                                         _3_Function<? super V1,? super V2,? super V3,? extends Collection<K>> mapper) throws Exception {
        List<K> list;
        list= new ArrayList<>();
        for (Tuple3<V1,V2,V3> v : oldArr)
            for (K k : mapper.apply(v._1,v._2,v._3)) {
                list.add(k);
            }
        return list;
    }


    public static <V1,V2,V3,K> List<K> flatMap_T3_A_A(Tuple3<V1,V2,V3>[] oldArr,
                                           _3_Function<? super V1,? super V2,? super V3,? extends K[]> mapper) throws Exception {
        List<K> list = new ArrayList<>();
        for (Tuple3<V1,V2,V3> v : oldArr)
            for (K k : mapper.apply(v._1,v._2,v._3)) {
                list.add(k);
            }
        return list;
    }

    public static <V1,V2,V3,K> List<K> flatMap_T3_A_O(Tuple3<V1,V2,V3>[] oldArr,
                                                _3_Function<? super V1,? super V2,? super V3,? extends Optional<K>> mapper) throws Exception {
        List<K> list = new ArrayList<>();
        for (Tuple3<V1,V2,V3> v : oldArr) {
            Optional<K> optional = mapper.apply(v._1,v._2,v._3);
            if (!optional.isPresent()) {
                continue;
            }
            list.add(optional.get());
        }
        return list;
    }

    public static <V1,V2,V3,L,R> List<R> flatMap_T3_A_E(Tuple3<V1,V2,V3>[] oldArr,
                                                _3_Function<? super V1,? super V2,? super V3,? extends Either<L,R>> mapper) throws Exception {
        List<R> list = new ArrayList<>();
        for (Tuple3<V1,V2,V3> v : oldArr) {
            Either<L,R> either= mapper.apply(v._1,v._2,v._3);
            if (either.isLeft()) {
                continue;
            }
            list.add(either.right());
        }
        return list;
    }


    public static <V1,V2,V3,V4,K> List<K> flatMap_T4_A_C(Tuple4<V1,V2,V3,V4>[] oldArr,
                                                            _4_Function<? super V1,? super V2,? super V3,? super V4,? extends Collection<K>> mapper) throws Exception {
        List<K> list = new ArrayList<>();
        for (Tuple4<V1,V2,V3,V4> v : oldArr)
            for (K k : mapper.apply(v._1,v._2,v._3,v._4)) {
                list.add(k);
            }
        return list;
    }

    public static <V1,V2,V3,V4,K> List<K> flatMap_T4_A_A(Tuple4<V1,V2,V3,V4>[] oldArr,
                                              _4_Function<? super V1,? super V2,? super V3,? super V4,? extends K[]> mapper) throws Exception {
        List<K> list;
        list = new ArrayList<>();
        for (Tuple4<V1,V2,V3,V4> v : oldArr)
            for (K k : mapper.apply(v._1,v._2,v._3,v._4)) {
                list.add(k);
            }
        return list;
    }

    public static <V1,V2,V3,V4,K> List<K> flatMap_T4_A_O(Tuple4<V1,V2,V3,V4>[] oldArr,
                                                   _4_Function<? super V1,? super V2,? super V3,? super V4,? extends Optional<K>> mapper) throws Exception {
        List<K> list = new ArrayList<>();
        for (Tuple4<V1,V2,V3,V4> v : oldArr) {
            Optional<K> optional = mapper.apply(v._1,v._2,v._3,v._4);
            if (!optional.isPresent()) {
                continue;
            }
            list.add(optional.get());
        }
        return list;
    }

    public static <V1,V2,V3,V4,L,R> List<R> flatMap_T4_A_E(Tuple4<V1,V2,V3,V4>[] oldArr,
                                                   _4_Function<? super V1,? super V2,? super V3,? super V4,? extends Either<L,R>> mapper) throws Exception {
        List<R> list = new ArrayList<>();
        for (Tuple4<V1,V2,V3,V4> v : oldArr) {
            Either<L,R> either= mapper.apply(v._1,v._2,v._3,v._4);
            if (either.isLeft()) {
                continue;
            }
            list.add(either.right());
        }
        return list;
    }


    public static <V1,V2,V3,V4,V5,K> List<K> flatMap_T5_A_C(Tuple5<V1,V2,V3,V4,V5>[] oldArr,
                                                               _5_Function<? super V1,? super V2,? super V3,? super V4,? super V5,? extends Collection<K>> mapper
                                                              ) throws Exception {
        List<K> list;
        list = new ArrayList<>();
        for (Tuple5<V1,V2,V3,V4,V5> v : oldArr)
            for (K k : mapper.apply(v._1, v._2, v._3, v._4, v._5)) {
                list.add(k);
            }
        return list;
    }

    public static <V1,V2,V3,V4,V5,K> List<K> flatMap_T5_A_A(Tuple5<V1,V2,V3,V4,V5>[] arr,
                                                 _5_Function<? super V1,? super V2,? super V3,? super V4,? super V5,? extends K[]> mapper
    ) throws Exception {
        List<K> list = new ArrayList<>();
        for (Tuple5<V1,V2,V3,V4,V5> v : arr)
            for (K k : mapper.apply(v._1, v._2, v._3, v._4, v._5)) {
                list.add(k);
            }
        return list;
    }

    public static <V1,V2,V3,V4,V5,K> List<K> flatMap_T5_A_O(Tuple5<V1,V2,V3,V4,V5>[] arr,
                                                      _5_Function<? super V1,? super V2,? super V3,? super V4,? super V5,? extends Optional<K>> mapper) throws Exception {
        List<K> list = new ArrayList<>();
        for (Tuple5<V1,V2,V3,V4,V5> v : arr) {
            Optional<K> optional = mapper.apply(v._1,v._2,v._3,v._4,v._5);
            if (!optional.isPresent()) {
                continue;
            }
            list.add(optional.get());
        }
        return list;
    }

    public static <V1,V2,V3,V4,V5,L,R> List<R> flatMap_T5_A_E(Tuple5<V1,V2,V3,V4,V5>[] arr,
                                                      _5_Function<? super V1,? super V2,? super V3,? super V4,? super V5,? extends Either<L,R>> mapper) throws Exception {
        List<R> list = new ArrayList<>();
        for (Tuple5<V1,V2,V3,V4,V5> v : arr) {
            Either<L,R> either= mapper.apply(v._1,v._2,v._3,v._4,v._5);
            if (either.isLeft()) {
                continue;
            }
            list.add(either.right());
        }
        return list;
    }


    public static <V,K> List<K> flatMap_AA_C(V[][] oldArr,
                                             _1_Function<? super V[],? extends Collection<K>> mapper) throws Exception {
        List<K> list;
        list = new ArrayList<>();
        for (V[] vs : oldArr)
            for (K k : mapper.apply(vs)) {
                list.add(k);

            }
        return list;
    }

    public static <V,K> List<K> flatMap_AA_A(V[][] oldArr,
                                         _1_Function<? super V[],? extends K[]> mapper) throws Exception {
        List<K> list = new ArrayList<>();
        for (V[] vs : oldArr)
            for (K k : mapper.apply(vs)) {
                list.add(k);
            }

        return list;
    }

    public static <V,K> List<K> flatMap_AA_O(V[][] oldArr,
                                            _1_Function<? super V[],? extends Optional<K>> mapper) throws Exception {
        List<K> list = new ArrayList<>();
        for (V[] vs : oldArr) {
            Optional<K> optional = mapper.apply(vs);
            if (!optional.isPresent()) {
                continue;
            }
            list.add(optional.get());

        }
        return list;
    }

    public static <V,L,R> List<R> flatMap_AA_E(V[][] arr,
                                            _1_Function<? super V[],? extends Either<L,R>> mapper) throws Exception {
        List<R> list = new ArrayList<>();
        for (V[] vs : arr) {
            Either<L, R> either = mapper.apply(vs);
            if (either.isLeft()) {
                continue;
            }
            list.add(either.right());

        }
        return list;
    }


    public static <V,K> List<K> flatMap_OA_C(Optional<V>[] oldArr,
                                             _1_Function<? super Optional<V>,? extends Collection<K>> mapper) throws Exception {
        List<K> list = new ArrayList<>();
        for(Optional<V> optional:oldArr){
            for(K k:mapper.apply(optional)){
                list.add(k);
            }
        }
        return list;
    }

    public static <V,K> List<K> flatMap_OA_A(Optional<V>[] arr,
                                             _1_Function<? super Optional<V>,? extends K[]> mapper) throws Exception {
        List<K> list;
        list = new ArrayList<>();
        for (Optional<V> optional : arr) {
            for (K k : mapper.apply(optional)) {
                list.add(k);
            }
        }
        return list;
    }

    public static <V,K> List<K> flatMap_OA_O(Optional<V>[] arr,
                                             _1_Function<? super Optional<V>,? extends Optional<K>> mapper) throws Exception {
        List<K> list;
        list = new ArrayList<>();
        for (Optional<V> optional : arr) {
            Optional<K> optionalOptional = mapper.apply(optional);
            if (!optionalOptional.isPresent()) {
                continue;
            }
            list.add(optionalOptional.get());
        }
        return list;
    }


    public static <V,K> List<K> flatMap_TA_C(Try<V>[] list,
                                             _1_Function<? super Try<V>,? extends Collection<K>> mapper) throws Exception {
        List<K> newList = new ArrayList<>();
        for(Try<V> vTry:list){
            for(K k:mapper.apply(vTry)){
                newList.add(k);
            }
        }
        return newList;
    }

    public static <V,K> List<K> flatMap_TA_A(Try<V>[] list,
                                             _1_Function<? super Try<V>,? extends K[]> mapper) throws Exception {
        List<K> newList = new ArrayList<>();
        for(Try<V> vTry:list){
            for(K k:mapper.apply(vTry)){
                newList.add(k);
            }
        }
        return newList;
    }

    public static <V,K> List<K> flatMap_TA_T(Try<V>[] list,
                                             _1_Function<? super Try<V>,? extends Try<K>> mapper) throws Exception {
        List<K> newList = new ArrayList<>();
        for(Try<V> vTry:list){
            Try<K> kTry = mapper.apply(vTry);
            if(kTry.success()){
                newList.add(((Success<K>)kTry).getValue());
            }
        }
        return newList;
    }

    public static <K,L,R> List<K> flatMap_EA_C(Either<L,R>[] list, _1_Function<? super Either<L,R>,? extends Collection<K>> mapper) throws Exception {
        List<K> newList = new ArrayList<>();
        for(Either<L,R> either:list){
            for(K k:mapper.apply(either)){
                newList.add(k);
            }
        }
        return newList;
    }

    public static <K,L,R> List<K> flatMap_EA_A(Either<L,R>[] list, _1_Function<? super Either<L,R>,? extends K[]> mapper) throws Exception {
        List<K> newList = new ArrayList<>();
        for(Either<L,R> either:list){
            for(K k:mapper.apply(either)){
                newList.add(k);
            }
        }
        return newList;
    }

    public static <L,R> List<R> flatMap_EA_E(Either<L,R>[] list, _1_Function<? super Either<L,R>,? extends Either<L,R>> mapper) throws Exception {
        List<R> newList = new ArrayList<>();
        for(Either<L,R> either:list){
            Either<L, R> eitherEither= mapper.apply(either);
            if (eitherEither.isLeft()) {
                continue;
            }
            newList.add(eitherEither.right());
        }
        return newList;
    }

    //filter functions
    /**
     *arr.filter(a=>{ return a!=1;});
     */
    public static <V> List<V> filter(V[] arr, _1_Predicate<V> filter) throws Exception{
        List<V> newList = new ArrayList<>();
        for(V v:arr){
            if(filter.test(v)){
                newList.add(v);
            }
        }
        return newList;
    }

    //foreach functions
    /**
     *arr.foreach(a=>{ a+1;});
     */
    public static <V> void foreach(V[] arr, _1_Consumer<V> consumer) throws Exception{
        for(V v:arr){
            consumer.accept(v);
        }
    }

    //exist functions
    /**
     * arr.exist(i=> { return i==1;});
     */
    public static <V> boolean exist(V[] arr, _1_Predicate<V> predicate) throws Exception{
        for(V v:arr){
            if(predicate.test(v)){
                return true;
            }
        }
        return false;
    }

    //find functions
    /**
     * arr.find(i=>{ return i==1};);
     */
    public static <V> Optional<V> find(V[] arr, _1_Predicate<V> predicate) throws Exception{
        for(V v:arr){
            if(predicate.test(v)){
                return Optional.of(v);
            }
        }
        return Optional.empty();
    }


    //toMap functions
    /**
     * arr.toMap();
     */
    public static <K,V> Map<K,V> toMap(Tuple2<K,V>[] arr){
        Map<K,V> map = new HashMap<>();
        for(Tuple2<K,V> tuple2:arr){
            map.put(tuple2._1,tuple2._2);
        }
        return map;
    }

    //sort functions
    /**
     * arr.sort((i,j)=> { return 1;});
     */
    public static <V> V[] sort(V[] arr, Comparator<V> comparator){
        V[] newArr = Arrays.copyOf(arr,arr.length);
        Arrays.sort(newArr,comparator);
        return newArr;
    }

    //zipWithIndex functions
    /**
     * arr.zipWithIndex();
     */
    public static <V> List<Tuple2<V,Integer>> zipWithIndex(V[] arr){
        List<Tuple2<V,Integer>> list = new ArrayList<>();
        for(int i=0;i<arr.length;i++){
            list.add(new Tuple2<>(arr[i],i));
        }
        return list;
    }

    //reduce functions
    /**
     * arr.reduce((i,j)=> {return i+j;} );
     */
    public static <V> V reduce(V[] arr, _2_Function<? super V,? super V,? extends V> reducer) throws Exception {
        V v = arr[0];
        for(int i=1;i<arr.length;i++){
            v = reducer.apply(v,arr[i]);
        }
        return v;
    }
    /**
     * arr.reduce(0,(i,j)=> { return i+j;});
     */
    public static <V> V reduce(V[] arr, V init,_2_Function<? super V,? super V,? extends V> reducer) throws Exception {
        V v = init;
        for (V value : arr) {
            v = reducer.apply(v, value);
        }
        return v;
    }

    //reduceOption functions
    /**
     * arr.reduceOption((i,j)=> {return i+j; } );
     */
    public static <V> Optional<V> reduceOption(V[] arr, _2_Function<? super V,? super V,? extends V> reducer) throws Exception {
        if(arr.length==0){
            return Optional.empty();
        }
        return Optional.of(reduce(arr,reducer));
    }


    //reverse functions
    @SuppressWarnings("unchecked")
    public static <V> V[] reverse(V[] arr){
        Object[] newArr = new Object[arr.length];
        for(int i=arr.length-1;i>-1;i--){
            newArr[arr.length-1-i] = arr[i];
        }
        return (V[])newArr;
    }

    public static <V> List<V> toList(V[] arr){
        return  Arrays.asList(arr);
    }

    public static <V> Set<V> toSet(V[] arr){
        Set<V> set = new HashSet<>();
        Collections.addAll(set,arr);
        return set;
    }

    public static <V> Set<V> toSortedSet(V[] arr, Comparator<V> comparator){
        Set<V> set = new TreeSet<>(comparator);
        Collections.addAll(set,arr);
        return set;
    }

    public static <V> Set<V> toLinkedSet(V[] arr){
        Set<V> set = new LinkedHashSet<>();
        Collections.addAll(set,arr);
        return set;
    }


    //sum
    public static int sumInt(Integer[] collection){
        int sum = 0;
        for(Integer i:collection){
            sum+=i;
        }
        return sum;
    }

    public static double sumDouble(Double[] collection){
        double sum = 0;
        for(double i:collection){
            sum+=i;
        }
        return sum;
    }

    public static float sumFloat(Float[] collection){
        float sum = 0;
        for(float i:collection){
            sum+=i;
        }
        return sum;
    }

    public static long sumLong(Long[] collection){
        long sum = 0;
        for(long i:collection){
            sum+=i;
        }
        return sum;
    }

    public static char sumChar(Character[] collection){
        char sum = 0;
        for(char i:collection){
            sum+=i;
        }
        return sum;
    }

    public static byte sumByte(Byte[] collection){
        byte sum = 0;
        for(byte i:collection){
            sum+=i;
        }
        return sum;
    }

    //max
    public static int maxInt(Integer[] collection){
        int max = Integer.MIN_VALUE;
        for(int i:collection){
            max = Integer.max(max,i);
        }
        return max;
    }

    public static double maxDouble(Double[] collection){
        double max = Double.MIN_VALUE;
        for(double i:collection){
            max = Double.max(max,i);
        }
        return max;
    }

    public static float maxFloat(Float[] collection){
        float max = Float.MIN_VALUE;
        for(float i:collection){
            max = Float.max(max,i);
        }
        return max;
    }

    public static long maxLong(Long[] collection){
        long max = Long.MIN_VALUE;
        for(long i:collection){
            max = Long.max(max,i);
        }
        return max;
    }

    public static char maxChar(Character[] collection){
        char max = Character.MIN_VALUE;
        for(char i:collection){
            if(max < i){
                i = max;
            }
        }
        return max;
    }

    public static byte maxByte(Byte[] collection){
        byte max = Byte.MIN_VALUE;
        for(byte i:collection){
            if(max < i){
                i = max;
            }
        }
        return max;
    }

    //min
    public static int minInt(Integer[] collection){
        int min = Integer.MAX_VALUE;
        for(int i:collection){
            min = Integer.min(min,i);
        }
        return min;
    }

    public static double minDouble(Double[] collection){
        double min = Double.MAX_VALUE;
        for(double i:collection){
            min = Double.min(min,i);
        }
        return min;
    }

    public static float minFloat(Float[] collection){
        float min = Float.MAX_VALUE;
        for(float i:collection){
            min = Float.min(min,i);
        }
        return min;
    }

    public static long minLong(Long[] collection){
        long min = Long.MAX_VALUE;
        for(long i:collection){
            min = Long.min(min,i);
        }
        return min;
    }

    public static char minChar(Character[] collection){
        char min = Character.MAX_VALUE;
        for(char i:collection){
            if(i < min){
                min = i;
            }
        }
        return min;
    }

    public static byte minByte(Byte[] collection){
        byte min = Byte.MAX_VALUE;
        for(byte i:collection){
            if(i < min){
                min = i;
            }
        }
        return min;
    }

    @SafeVarargs
    public static <V> V[] ofArray(V[] newVs,V... vs) {
        System.arraycopy(vs, 0, newVs, 0, vs.length);
       return newVs;
    }

}
