package org.example.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;

public class ConfigurationLoader {

    public static DocumentConfiguration load() throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        InputStream inputStream =
                ConfigurationLoader.class
                        .getClassLoader()
                        .getResourceAsStream("documentConfiguration.json");

        if (inputStream == null) {
            throw new RuntimeException("documentConfiguration.json not found");
        }

        return mapper.readValue(
                inputStream,
                DocumentConfiguration.class
        );
    }

    public static EnhancementConfiguration enhancementDocumentLoaderConfig() throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        InputStream inputStream =
                ConfigurationLoader.class
                        .getClassLoader()
                        .getResourceAsStream("documentEnhancement.json");

        if (inputStream == null) {
            throw new RuntimeException("documentConfiguration.json not found");
        }

        return mapper.readValue(
                inputStream,
                EnhancementConfiguration.class
        );
    }
    public static PickListConfiguration pickListConfigurationLoader() throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        InputStream is =
                ConfigurationLoader.class
                        .getClassLoader()
                        .getResourceAsStream("picklist.json");

        return mapper.readValue(is, PickListConfiguration.class);
    }
}