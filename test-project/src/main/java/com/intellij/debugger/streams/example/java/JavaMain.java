package com.intellij.debugger.streams.example.java;

import java.util.Arrays;

public class JavaMain {
    public static void main(String[] args) {
        int result = Arrays.stream(new int[]{1, 2, 3, 4, 5})
                .map(n -> n * n)
                .filter(n -> n % 2 == 0)
                .sum();
        System.out.println(result);
    }
}
