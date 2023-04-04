package com.dongjiaqiang.jvm.dsl.java.api.codes;

import com.dongjiaqiang.jvm.dsl.java.api.lambda.consumer._2_Consumer;
import com.dongjiaqiang.jvm.dsl.java.api.lambda.function._1_Function;
import com.dongjiaqiang.jvm.dsl.java.api.lambda.function._2_Function;
import com.dongjiaqiang.jvm.dsl.java.api.lambda.predicate._1_Predicate;
import com.dongjiaqiang.jvm.dsl.java.api.lambda.predicate._2_Predicate;
import com.dongjiaqiang.jvm.dsl.java.api.tuple.Tuple2;

import java.util.*;

public class _SYS_MAP_CODES {

    public static final String CLAZZ_NAME = "_SYS_MAP_CODES";

    /**
     * a.map((k,v)=> { return k+v;});
     */
    public static <K,V,T> List<T> map(Map<K,V> map,
                                      _2_Function<? super K,? super V,? extends T> mapper) throws Exception {
        List<T> list = new ArrayList<>();
        for(Map.Entry<K,V> entry:map.entrySet()){
            list.add(mapper.apply(entry.getKey(),entry.getValue()));
        }
        return list;
    }
    /**
     * a.map(kv=> { return kv._1+kv._2;});
     */
    public static <K,V,T> List<T> map(Map<K,V> map,
                                      _1_Function<Tuple2<? super K,? super V>,? extends T> mapper) throws Exception {
        List<T> list = new ArrayList<>();
        for(Map.Entry<K,V> entry:map.entrySet()){
            list.add(mapper.apply(new Tuple2<>(entry.getKey(),entry.getValue())));
        }
        return list;
    }

    public static <K,V> Map<K,V> filter(Map<K,V> map,Map<K,V> newMap,
                                     _1_Predicate<Tuple2<? super K,? super V>> mapper) throws Exception {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (mapper.test(new Tuple2<>(entry.getKey(), entry.getValue()))) {
                newMap.put(entry.getKey(), entry.getValue());
            }
        }
        return newMap;
    }


    public static <K,V> Map<K,V> filter(Map<K,V> map,Map<K,V> newMap,
                                     _2_Predicate<? super K,? super V> mapper) throws Exception {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (mapper.test(entry.getKey(), entry.getValue())) {
                newMap.put(entry.getKey(), entry.getValue());
            }
        }
        return newMap;
    }
    /**
     * a.values();
     */
    public static <K,V> List<V> values(Map<K,V> map){
        return new ArrayList<>(map.values());
    }

    /**
     * a.mapValue(v=> { return v+1;});
     */
    public static <K,V,NV> Map<K,NV> mapValue(Map<K,V> map,
                                              Map<K,NV> newMap,
                                      _1_Function<? super V,? extends NV> mapper) throws Exception {
        for(Map.Entry<K,V> entry:map.entrySet()){
            newMap.put(entry.getKey(),mapper.apply(entry.getValue()));
        }
        return newMap;
    }
    /**
     * a.head();
     */
    public static <K,V> Tuple2<K,V>  head(Map<K,V> map){
        Map.Entry<K,V> entry = map.entrySet().iterator().next();
        return new Tuple2<>(entry.getKey(),entry.getValue());
    }

    /**
     * a.headOption();
     */
    public static <K,V> Optional<Tuple2<K,V>>  headOption(Map<K,V> map){
        for (Map.Entry<K, V> entry : map.entrySet()) {
            return Optional.of(new Tuple2<>(entry.getKey(), entry.getValue()));
        }
        return Optional.empty();
    }
    /**
     * a.last();
     */
    public static <K,V> Tuple2<K,V> last(Map<K,V> map){
        Iterator<Map.Entry<K,V>> entry = map.entrySet().iterator();
        Map.Entry<K,V> head = entry.next();
        while (entry.hasNext()){
            head = entry.next();
        }
        return new Tuple2<>(head.getKey(),head.getValue());
    }

    /**
     * a.lastOption();
     */
    public static <K,V> Optional<Tuple2<K,V>> lastOption(Map<K,V> map){
        Iterator<Map.Entry<K,V>> entry = map.entrySet().iterator();
        Map.Entry<K,V> head = null;
        while (entry.hasNext()){
            head = entry.next();
        }
        return head==null?Optional.empty():Optional.of(new Tuple2<>(head.getKey(),head.getValue()));
    }

    /**
     *a.foreach((k,v)=>{ ... })
     */
    public static <K,V> void foreach(Map<K,V> map, _2_Consumer<? super K,? super V> consumer) throws Exception{
        for(Map.Entry<K,V> entry:map.entrySet()){
            consumer.accept(entry.getKey(),entry.getValue());
        }
    }

    //mkString
    public static <K,V> String mkString(Map<K,V> map,String separator){
        return mkString(map,"",separator,"");
    }
    public static <K,V> String mkString(Map<K,V> map,String start,String separator,String end){
            StringBuilder sb = new StringBuilder();
            sb.append(start);

            if(map.isEmpty()){
                sb.append(end);
                return sb.toString();
            }
            Iterator<Map.Entry<K,V>> iterator = map.entrySet().iterator();
            do{
                Map.Entry<K,V> entry = iterator.next();
                sb.append(entry.getKey());
                sb.append(separator);
                sb.append(entry.getValue());
                sb.append(separator);
            }while (iterator.hasNext());
            sb.append(end);
            return sb.toString();
    }

    @SafeVarargs
    public static <K, V> Map<K, V> ofMap(Tuple2<K, V>... ts) {
        Map<K, V> map = new HashMap<>();
        for (Tuple2<K, V> t : ts) {
            map.put(t._1, t._2);
        }
        return map;
    }

    @SafeVarargs
    public static <K, V> Map<K, V> ofSeqMap(Tuple2<K, V>... ts) {
        Map<K, V> map = new LinkedHashMap<>();
        for (Tuple2<K, V> t : ts) {
            map.put(t._1, t._2);
        }
        return map;
    }

    public static <K, V> Map<K, V> toSeqMap(Map<K,V> oldMap) {
        return new LinkedHashMap<>(oldMap);
    }

    @SafeVarargs
    public static <K extends Comparable<K>, V> Map<K, V> ofSortMap(Tuple2<K, V>... ts) {
        Map<K,V> map = new TreeMap<>();
        for (Tuple2<K, V> t : ts) {
            map.put(t._1, t._2);
        }
        return map;
    }

    @SafeVarargs
    public static <K , V> Map<K, V> ofSortMap(Comparator<K> comparator, Tuple2<K, V>... ts) {
        Map<K,V> map = new TreeMap<>(comparator);
        for (Tuple2<K, V> t : ts) {
            map.put(t._1, t._2);
        }
        return map;
    }


    public static <K,V> List<Tuple2<K,V>> toList(Map<K,V> map){
        List<Tuple2<K,V>> list = new ArrayList<>();
        for(Map.Entry<K,V> entry:map.entrySet()){
            list.add(new Tuple2<>(entry.getKey(),entry.getValue()));
        }
        return list;
    }
}
