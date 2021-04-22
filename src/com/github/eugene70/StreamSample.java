package com.github.eugene70;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Stream;

public class StreamSample {
    public static void main(String[] args) {

        Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .flatMap(n -> {
                    List<Integer> list = new ArrayList<>(n);
                    for (int i = 0; i < n; i++) {
                        list.add(n);
                    }
                    return list.stream();
                })
                .parallel()
                .forEach(n -> System.out.println(n + " " + Thread.currentThread().getName()));
        System.out.println(ForkJoinPool.commonPool().getPoolSize());
    }
}
