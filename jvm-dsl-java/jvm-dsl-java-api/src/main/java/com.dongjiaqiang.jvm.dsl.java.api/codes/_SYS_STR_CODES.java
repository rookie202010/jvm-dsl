package com.dongjiaqiang.jvm.dsl.java.api.codes;

import com.dongjiaqiang.jvm.dsl.java.api.extend.Either;
import com.dongjiaqiang.jvm.dsl.java.api.tuple.Tuple2;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class _SYS_STR_CODES {

    public static final String CLAZZ_NAME = "_SYS_STR_CODES";

    public interface _CharToCharFunction{
        char apply(char c) throws Exception;
    }

    public interface _CharToObjectFunction<T>{
        T apply(char c) throws Exception;
    }

    public interface _2_CharToCharFunction{
        char apply(char c1,char c2) throws Exception;
    }

    public interface _CharConsumer{
        void accept(char c) throws Exception;
    }

    public interface _CharPredicate{
        boolean test(char c) throws Exception;
    }

    //map functions
    public static  String map(String str, _CharToCharFunction mapper) throws Exception {
        char[] cs = str.toCharArray();
        char[] ncs = new char[cs.length];
        for(int i = 0;i<cs.length;i++){
            ncs[i] = mapper.apply(cs[i]);
        }
        return new String(ncs);
    }

    public static <T> List<T> map(String str, _CharToObjectFunction<T> mapper) throws Exception {
        char[] cs = str.toCharArray();
        List<T> list = new ArrayList<>();
        for (char c : cs) {
            list.add(mapper.apply(c));
        }
        return list;
    }

    //flatMap functions
    public static <T> List<T> flatMap(String str, _CharToObjectFunction<Collection<T>> mapper) throws Exception {
        char[] cs = str.toCharArray();
        List<T> list = new ArrayList<>();
        for (char c : cs) {
            list.addAll(mapper.apply(c));
        }
        return list;
    }

    public static <T> List<T> flatMapArray(String str, _CharToObjectFunction<T[]> mapper) throws Exception {
        char[] cs = str.toCharArray();
        List<T> list = new ArrayList<>();
        for (char c : cs) {
            list.addAll(Arrays.asList(mapper.apply(c)));
        }
        return list;
    }

    public static <T> List<T> flatMapOptional(String str, _CharToObjectFunction<Optional<T>> mapper) throws Exception {
        char[] cs = str.toCharArray();
        List<T> list = new ArrayList<>();
        for (char c : cs) {
            Optional<T> optional = mapper.apply(c);
            if (!optional.isPresent()) {
                continue;
            }
            list.add(optional.get());
        }
        return list;
    }

    public static <T,K> List<K> flatMapEither(String str, _CharToObjectFunction<Either<T,K>> mapper) throws Exception {
        char[] cs = str.toCharArray();
        List<K> list = new ArrayList<>();
        for (char c : cs) {
            Either<T,K> either = mapper.apply(c);
            if (either.isLeft()) {
                continue;
            }
            list.add(either.right());
        }
        return list;
    }

    //filter functions
    public static  String filter(String str,  _CharPredicate predicate) throws Exception {
        char[] cs = str.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char c : cs) {
            if (predicate.test(c)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static  String filterNot(String str,  _CharPredicate predicate) throws Exception {
        return filter(str, new _CharPredicate() {
            @Override
            public boolean test(char c) throws Exception {
                return !predicate.test(c);
            }
        });
    }

    //foreach
    public static  void foreach(String str,  _CharConsumer consumer) throws Exception {
        for(char c:str.toCharArray()){
            consumer.accept(c);
        }
    }


    //exist
    public static  boolean exist(String str, _CharPredicate predicate) throws Exception {
        char[] cs = str.toCharArray();
        for (char c : cs) {
            if (predicate.test(c)) {
                return true;
            }
        }
        return false;
    }

    //find
    public static Optional<Character> find(String str, _CharPredicate predicate) throws Exception {
        char[] cs = str.toCharArray();
        for (char c : cs) {
            if (predicate.test(c)) {
                return Optional.of(c);
            }
        }
        return Optional.empty();
    }

    //toList
    public static List<Character> toList(String str){
        List<Character> list = new ArrayList<>();
        for(char c:str.toCharArray()){
            list.add(c);
        }
        return list;
    }

    //toSet
    public static Set<Character> toSet(String str){
        Set<Character> set = new HashSet<>();
        for(char c:str.toCharArray()){
            set.add(c);
        }
        return set;
    }

    public static Set<Character> toSeqSet(String str){
        Set<Character> set = new LinkedHashSet<>();
        for(char c:str.toCharArray()){
            set.add(c);
        }
        return set;
    }

    public static Set<Character> toSortedSet(String str){
        Set<Character> set = new TreeSet<>();
        for(char c:str.toCharArray()){
            set.add(c);
        }
        return set;
    }

    public static Set<Character> toSortedSet(String str,Comparator<Character> comparator){
        Set<Character> set = new TreeSet<>(comparator);
        for(char c:str.toCharArray()){
            set.add(c);
        }
        return set;
    }

    public static String sort(String str,Comparator<Character> comparator){
        Character[] characters = ArrayUtils.toObject(str.toCharArray());
        Arrays.sort(characters,comparator);
        return new String(ArrayUtils.toPrimitive(characters));
    }

    public static String reverse(String str){
        return StringUtils.reverse(str);
    }

    public static boolean isEmpty(String str){
        return StringUtils.isEmpty(str);
    }

    public static boolean isNumeric(String str){ return StringUtils.isNumeric(str);}

    public static boolean nonEmpty(String str){
        return StringUtils.isNotEmpty(str);
    }

    public static List<Tuple2<Character,Integer>> zipWithIndex(String str){
        List<Tuple2<Character,Integer>> list = new ArrayList<>();
        char[] chars = str.toCharArray();
        for(int i=0;i<chars.length;i++){
            list.add(new Tuple2<>(chars[i],i));
        }
        return list;
    }

    public static  char reduce(String str, _2_CharToCharFunction mapper) throws Exception{
        char[] chars = str.toCharArray();
        char init = chars[0];
        for(int i=1;i<chars.length;i++){
            init = mapper.apply(init,chars[i]);
        }
        return init;
    }

    public static  Optional<Character> reduceOption(String str, _2_CharToCharFunction mapper) throws Exception{
        if(str.isEmpty()){
            return Optional.empty();
        }
        return Optional.of(reduce(str,mapper));
    }

    public static char head(String str){
        return str.charAt(0);
    }

    public static Optional<Character> headOption(String str){
        if(str.isEmpty()){
            return Optional.empty();
        }else{
            return Optional.of(head(str));
        }
    }

    public static char tail(String str){
        return str.charAt(str.length()-1);
    }

    public static Optional<Character> tailOption(String str){
        if(str.isEmpty()){
            return Optional.empty();
        }else{
            return Optional.of(tail(str));
        }
    }
}
