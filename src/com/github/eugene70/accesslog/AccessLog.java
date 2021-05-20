package com.github.eugene70.accesslog;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AccessLog {
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
        return !line.contains("health.do") && !line.contains(".js") && !line.contains(".png");
    }

    private static <T> T log(T param) {
        System.out.println(Thread.currentThread().getName() + ": " + param.toString());
        return param;
    }

    public static void main(String[] args) {
        //System.out.println(
        readFile("ptwpta04-portal-access.log.20210518")
                .filter(AccessLog::filter)
                .map(AccessLog::log)
                .map(line -> line.split("\\s+"))
                .collect(Collectors.groupingBy(s -> s[6], Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
                .collect(Collectors.toList())
                .forEach(System.out::println);
        //);
    }
}
