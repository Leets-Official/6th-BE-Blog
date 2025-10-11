package com.leets.backend.blog.util;

public class StringUtil {
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean IsNullOrWhiteSpace(String str) {
        return str == null || str.isBlank();
    }
}
