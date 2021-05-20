package com.github.eugene70.accesslog;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SizeAnalysis {
    private final Map<String, Integer> minMap = new HashMap<>();
    private final Map<String, Integer> maxMap = new HashMap<>();
    private final Map<String, Integer> countMap = new HashMap<>();

    private Map.Entry<String, Integer> compute(String key, int value) {
        if (minMap.getOrDefault(key, Integer.MAX_VALUE) > value) {
            minMap.put(key, value);
        }
        if (maxMap.getOrDefault(key, 0) < value) {
            maxMap.put(key, value);
        }
        countMap.put(key, countMap.getOrDefault(key, 0) + 1);
        return new AbstractMap.SimpleEntry<>(key, value);
    }

    private static Stream<String> readFile(String fileName) {
        try {
            final URI uri = ClassLoader.getSystemResource(fileName).toURI();
            return Files.lines(Paths.get(uri));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            return Stream.empty();
        }
    }

    private static boolean filter(String line) {
        return line.contains("v1.00");
    }

    private static <T> T log(T param) {
        System.out.println(Thread.currentThread().getName() + ": " + param.toString());
        return param;
    }

    public static void main(String[] args) {
        SizeAnalysis sa = new SizeAnalysis();

        readFile("ptwpta04-portal-access.log.20210518")
                .filter(SizeAnalysis::filter)
                .map(line -> line.split("\\s+"))
                .filter(s -> !s[9].equals("-"))
                .map(s -> sa.compute(s[6], Integer.parseInt(s[9])))
                .collect(Collectors.groupingBy(e -> e.getKey(), Collectors.averagingInt(e -> e.getValue())))
                .entrySet()
                .stream()
                .sorted(Comparator.comparing(a -> sa.countMap.get(a.getKey())))
                .forEach(e -> {
                    String key = e.getKey();
                    System.out.println(key + ": min=" + sa.minMap.get(key) +
                            ", max=" + sa.maxMap.get(key) +
                            ", count=" + sa.countMap.get(key) +
                            ", avg=" + e.getValue());
                });
    }
}
