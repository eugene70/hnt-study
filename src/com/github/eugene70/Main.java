package com.github.eugene70;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.AbstractMap.SimpleEntry;

public class Main {

    private static Stream<String> readBook(String bookName) {
        try {
            final URI uri = ClassLoader.getSystemResource(bookName).toURI();
            return Files.lines(Paths.get(uri));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            return Stream.empty();
        }
    }

    private static String[] splitBySpace(String line) {
        return line.split("\\s+");
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        List<Map.Entry<String, Integer>> result = readBook("book.txt")
                .parallel()
                .map(Refiner::cleansing)
                .filter(s -> !s.isEmpty())
                .flatMap(line -> Arrays.stream(splitBySpace(line)))
                .collect(Collectors.groupingBy(word -> word))
                .entrySet()
                .stream()
                .map(e -> (Map.Entry<String, Integer>) new SimpleEntry(e.getKey(), e.getValue().size()))
                .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
                .collect(Collectors.toList());
        long end = System.currentTimeMillis();
        System.out.println(result);
        System.out.println((end - start) + "ms");
    }

    public static <T> T log(T param) {
        System.out.println(Thread.currentThread().getName() + ": " + param.toString());
        return param;
    }
}