package com.github.eugene70;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private static List<String> readBook(String bookName) {
        try {
            final URI uri = ClassLoader.getSystemResource(bookName).toURI();
            System.out.println(uri);
            return Files.lines(Paths.get(uri))
                    .collect(Collectors.toList());
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private static List<String> cleansing(List<String> lines) {
        return lines.stream()
                .map(line -> line.replaceAll("[^a-zA-Z0-9]", " ").toLowerCase(Locale.ROOT).trim())
                .filter(line -> line.length() > 0)
                .collect(Collectors.toList());
    }


    private static Map<String, Integer> countWord(List<String> lines) {
        final Map<String, Integer> wordMap = new HashMap<>();
        lines.stream()
                .flatMap(line -> Arrays.stream(line.split("\\s+")))
                .forEach(word -> {
                    int countWord = wordMap.getOrDefault(word, 0);
                    wordMap.put(word, countWord + 1);
                });
        return wordMap;
    }

    private static List<Map.Entry<String, Integer>> sortByWordCount(Map<String, Integer> wordMap) {
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(wordMap.entrySet());
        return entries.stream()
                .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        System.out.println(
            sortByWordCount(
                    countWord(
                            cleansing(
                                readBook("book.txt"))))
        );
    }
}