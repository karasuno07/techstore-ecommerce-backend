package com.techstore.ecommerce.util;

import org.springframework.util.StringUtils;

import java.text.Normalizer;
import java.util.UUID;
import java.util.regex.Pattern;

public final class CustomStringUtil {

    public static String generateFileName(String originalFileName) {
        return UUID.randomUUID() + getExtension(originalFileName);
    }

    public static String getExtension(String originalFileName) {
        return StringUtils.getFilenameExtension(originalFileName);
    }

    public static String generateSlug(String text) {
        String s = removeAccent(text).replace("[^a-zA-Z0-9\\s]", "");
        return s.toLowerCase().replaceAll("\\s", "-");
    }

    private static String removeAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("");
    }
}
