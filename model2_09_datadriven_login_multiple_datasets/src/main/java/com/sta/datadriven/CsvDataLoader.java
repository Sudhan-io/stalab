package com.sta.datadriven;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class CsvDataLoader {

    public static Object[][] loadLoginData(String resourcePath) throws IOException {
        List<Object[]> rows = new ArrayList<>();
        InputStream is = CsvDataLoader.class.getResourceAsStream(resourcePath);
        if (is == null) {
            // Fallback to direct file read if running from working directory
            File file = new File("src/test/resources" + resourcePath);
            if (!file.exists()) {
                file = new File("model2_09_datadriven_login_multiple_datasets/src/test/resources" + resourcePath);
            }
            if (file.exists()) {
                is = new FileInputStream(file);
            } else {
                throw new FileNotFoundException("Cannot locate CSV test data resource: " + resourcePath);
            }
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                if (firstLine) {
                    firstLine = false; // skip header
                    continue;
                }
                String[] tokens = line.split(",", -1);
                if (tokens.length >= 5) {
                    String scenarioId = tokens[0].trim();
                    String username = tokens[1].trim();
                    String password = tokens[2].trim();
                    String expectedOutcome = tokens[3].trim();
                    String expectedMessage = tokens[4].trim();

                    // Convert placeholder __EMPTY__ to actual empty string
                    if ("__EMPTY__".equals(username)) username = "";
                    if ("__EMPTY__".equals(password)) password = "";

                    rows.add(new Object[]{scenarioId, username, password, expectedOutcome, expectedMessage});
                }
            }
        }
        return rows.toArray(new Object[0][]);
    }
}
