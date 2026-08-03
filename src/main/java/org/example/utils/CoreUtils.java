package org.example.utils;

public class CoreUtils {
    /**
     * Generate KEY
     *
     * Example:
     * incorrect lodgment by call center
     *
     * =>
     * INCORRECT_LODGMENT_BY_CALL_CENTER
     */
    public static String generateSystemKey(String fieldName) {

        return fieldName
                .trim()
                .toUpperCase()
                .replaceAll("[^A-Z0-9]+", "_")
                .replaceAll("_+", "_")
                .replaceAll("^_|_$", "");
    }

}
