package com.owncloud.self.api.utils;

import java.util.List;
import java.util.stream.StreamSupport;

public class ListCasting {

    public static <T> List<T> iterableToList(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false)
                .toList(); // Java 16+
    }
}
