package com.github.eugene70;


import java.util.Locale;

public class Refiner {
    public static String cleansing(String line) {
        return line.replaceAll("[^a-zA-Z0-9]", " ").toLowerCase(Locale.ROOT).trim();
    }
}