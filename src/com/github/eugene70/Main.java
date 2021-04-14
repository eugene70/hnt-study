package com.github.eugene70;

import java.io.*;
import java.net.URL;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello, World!");

        URL url = ClassLoader.getSystemResource("book.txt");
        System.out.println(url);
        File file = new File(url.getFile());
        String line;
        int wc = 0;
        Map<String, Integer> wordMap = new HashMap<>();
        try (
                InputStream fileInputStream = new FileInputStream(file);
                BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream))
        ) {
            while (true) {
                line = reader.readLine();
                if (line == null) {
                    break;
                }
                line = line.replaceAll("[^a-zA-Z0-9]", " ").toLowerCase(Locale.ROOT).trim();
                if (line.trim().length() > 0) {
                    //System.out.println(line);
                    //System.out.println(line);
                    String[] words = line.split("\\s+");
                    //Arrays.stream(words).forEach(System.out::println);
                    wc += words.length;
                    //System.out.println(wc);
                    for (String word : words) {
                        if (word.length() == 0) {
                            System.out.println(line);
                        }
                        int countWord = wordMap.getOrDefault(word, 0);
                        wordMap.put(word, countWord + 1);
                    }
                }
                //System.out.println(line);
            }
            System.out.println(wordMap);
            System.out.println("Word count: " + wc);
            List<Map.Entry<String, Integer>> list = new ArrayList<>(wordMap.entrySet());
            list.sort(new Comparator<Map.Entry<String, Integer>>() {
                @Override
                public int compare(Map.Entry o1, Map.Entry o2) {
                    int v1 = (int)o1.getValue();
                    int v2 = (int)o2.getValue();
                    return Integer.compare(v2, v1);
                }
            });
            System.out.println(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}