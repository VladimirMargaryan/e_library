package com.app.e_library.util;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

public class ObjectUtil {

    public static <T> Predicate<T> distinctByField(Function<? super T, ?> keyExtractor) {
        Set<Object> keySet = ConcurrentHashMap.newKeySet();
        return t -> keySet.add(keyExtractor.apply(t));
    }

}
