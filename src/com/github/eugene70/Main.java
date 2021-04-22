package com.github.eugene70;

import java.io.*;
import java.net.URL;
import java.util.*;

public class Main {

    private static List<String> readBook(String bookName) {
        final URL url = ClassLoader.getSystemResource(bookName);
        System.out.println(url);
        final File file = new File(url.getFile());
        final List<String> lines = new ArrayList<>();
        try (
                final InputStream fileInputStream = new FileInputStream(file);
                final BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.replaceAll("[^a-zA-Z0-9]", " ").toLowerCase(Locale.ROOT).trim();
                if (line.trim().length() > 0) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    private static Map<String, Integer> countWord(List<String> lines) {
        final Map<String, Integer> wordMap = new HashMap<>();
        for (String line : lines) {
            String[] words = line.split("\\s+");
            for (String word : words) {
                int countWord = wordMap.getOrDefault(word, 0);
                wordMap.put(word, countWord + 1);
            }
        }
        return wordMap;
    }

    private static List<Map.Entry<String, Integer>> sortByWordCount(Map<String, Integer> wordMap) {
        List<Map.Entry<String, Integer>> sortedList = new ArrayList<>(wordMap.entrySet());
        sortedList.sort(new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry o1, Map.Entry o2) {
                int v1 = (int)o1.getValue();
                int v2 = (int)o2.getValue();
                return Integer.compare(v2, v1);
            }
        });
        return sortedList;
    }

    public static void main(String[] args) {
        System.out.println(
            sortByWordCount(
                    countWord(
                            readBook("book.txt")))
        );
    }
}