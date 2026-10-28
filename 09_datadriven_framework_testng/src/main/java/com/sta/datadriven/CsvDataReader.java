package com.sta.datadriven;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CsvDataReader {

    public static Object[][] readCsvData(String resourcePath) {
        List<Object[]> rows = new ArrayList<>();
        InputStream is = CsvDataReader.class.getClassLoader().getResourceAsStream(resourcePath);
        if (is == null) {
            // Try filesystem path directly if running standalone
            java.io.File file = new java.io.File(resourcePath);
            if (file.exists()) {
                try {
                    is = new java.io.FileInputStream(file);
                } catch (Exception ignored) {}
            }
        }
        if (is == null) {
            throw new RuntimeException("Could not find CSV test data resource at: " + resourcePath);
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            boolean isHeader = true;
            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                if (line.trim().isEmpty()) continue;

                // Split into exactly 5 columns: testCaseId, username, password, expectedStatus, expectedMessage
                String[] parts = line.split(",", 5);
                for (int i = 0; i < parts.length; i++) {
                    String val = parts[i].trim();
                    if (val.startsWith("\"") && val.endsWith("\"") && val.length() >= 2) {
                        val = val.substring(1, val.length() - 1);
                    }
                    parts[i] = val;
                }
                rows.add(parts);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error reading CSV test data: " + e.getMessage(), e);
        }

        Object[][] result = new Object[rows.size()][];
        for (int i = 0; i < rows.size(); i++) {
            result[i] = rows.get(i);
        }
        return result;
    }
}
