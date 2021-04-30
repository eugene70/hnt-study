package com.github.eugene70;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.AbstractMap.*;

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
        System.out.println(
        readBook("book.txt")
                //.parallel()
                .map(x -> {System.out.println(Thread.currentThread().getName() + ": " + x); return x;})
                .map(Refiner::cleansing)
                .map(x -> {System.out.println(Thread.currentThread().getName() + ": " + x); return x;})
                .filter(s -> !s.isEmpty())
                .map(x -> {System.out.println(Thread.currentThread().getName() + ": " + x); return x;})
                .flatMap(line -> Arrays.stream(splitBySpace(line)))
                .map(x -> {System.out.println(Thread.currentThread().getName() + ": " + x); return x;})
                .collect(Collectors.groupingBy(word -> word))
                .entrySet()
                .stream()
                .map(e -> (Map.Entry<String, Integer>) new SimpleEntry(e.getKey(), e.getValue().size()))
                .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
                .collect(Collectors.toList())
        );
    }
}