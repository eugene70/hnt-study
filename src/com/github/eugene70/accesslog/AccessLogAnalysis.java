package com.github.eugene70.accesslog;

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

public class AccessLogAnalysis {
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
        return line.contains("selectExchangRateCal");
    }

    private static <T> T log(T param) {
        System.out.println(Thread.currentThread().getName() + ": " + param.toString());
        return param;
    }

    public static void main(String[] args) {
        List<String> files = Arrays.asList(
                "ptwpta01-portal-access.log.20210518",
                "ptwpta02-portal-access.log.20210518",
                "ptwpta03-portal-access.log.20210518",
                "ptwpta04-portal-access.log.20210518"
        );
        //System.out.println(
        for (String fileName : files) {
            List<Integer> sortedList =
                    readFile(fileName)
                            .filter(AccessLogAnalysis::filter)
                            .map(line -> line.split("\\s+")[4])
                            .map(Integer::valueOf)
                            .sorted(Integer::compare)
                            .collect(Collectors.toList());
            System.out.println(sortedList.get(sortedList.size() / 2));
        }
        //);
    }
}
