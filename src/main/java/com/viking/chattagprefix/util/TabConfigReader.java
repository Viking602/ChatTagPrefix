package com.viking.chattagprefix.util;

import com.viking.chattagprefix.ChatTagPrefix;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class TabConfigReader {
    private static final Map<String, String> groupPrefixes = new HashMap<>();

    public static void loadFromFile(String configPath) {
        groupPrefixes.clear();

        Path path = Paths.get(configPath);
        if (!Files.exists(path)) {
            ChatTagPrefix.LOGGER.warn("TAB config file not found: {}", configPath);
            return;
        }

        try (InputStream input = new FileInputStream(path.toFile())) {
            Yaml yaml = new Yaml();
            Map<String, Object> config = yaml.load(input);

            if (config != null) {
                parseGroups(config);
            }

            ChatTagPrefix.LOGGER.info("Loaded {} group prefixes from TAB config", groupPrefixes.size());
        } catch (IOException e) {
            ChatTagPrefix.LOGGER.error("Failed to read TAB config: {}", e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private static void parseGroups(Map<String, Object> config) {
        for (Map.Entry<String, Object> entry : config.entrySet()) {
            String groupName = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof Map) {
                Map<String, Object> groupData = (Map<String, Object>) value;
                Object tagPrefix = groupData.get("tagprefix");

                if (tagPrefix != null) {
                    String prefix = tagPrefix.toString();
                    groupPrefixes.put(groupName, prefix);
                    ChatTagPrefix.LOGGER.debug("Loaded prefix for group '{}': {}", groupName, prefix);
                }
            }
        }
    }

    public static String getGroupPrefix(String groupName) {
        return groupPrefixes.getOrDefault(groupName,
                groupPrefixes.getOrDefault("_DEFAULT_", ""));
    }

    public static Map<String, String> getAllPrefixes() {
        return new HashMap<>(groupPrefixes);
    }

    public static void reload(String configPath) {
        loadFromFile(configPath);
    }
}
