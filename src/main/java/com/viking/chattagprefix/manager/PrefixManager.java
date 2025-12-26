package com.viking.chattagprefix.manager;

import com.viking.chattagprefix.ChatTagPrefix;
import com.viking.chattagprefix.config.ModConfig;
import com.viking.chattagprefix.util.TabConfigReader;

import java.util.HashMap;
import java.util.Map;

public class PrefixManager {
    private static final Map<String, String> playerGroupMap = new HashMap<>();

    public static void loadConfig() {
        // 加载 TAB 的 groups.yml
        String tabConfigPath = ModConfig.getTabConfigPath();
        TabConfigReader.loadFromFile(tabConfigPath);

        // 加载玩家-权限组映射
        loadPlayerGroups();

        ChatTagPrefix.LOGGER.info("PrefixManager initialized");
    }

    private static void loadPlayerGroups() {
        playerGroupMap.clear();

        for (String mapping : ModConfig.getPlayerGroups()) {
            String[] parts = mapping.split("=", 2);
            if (parts.length == 2) {
                String playerName = parts[0].trim();
                String groupName = parts[1].trim();
                playerGroupMap.put(playerName.toLowerCase(), groupName);
                ChatTagPrefix.LOGGER.debug("Mapped player '{}' to group '{}'", playerName, groupName);
            }
        }
    }

    public static String getPlayerGroup(String playerName) {
        return playerGroupMap.getOrDefault(
                playerName.toLowerCase(),
                ModConfig.getDefaultGroup()
        );
    }

    public static String getGroupPrefix(String groupName) {
        return TabConfigReader.getGroupPrefix(groupName);
    }

    public static String getPlayerPrefix(String playerName) {
        String group = getPlayerGroup(playerName);
        return getGroupPrefix(group);
    }

    public static void reload() {
        loadConfig();
    }
}
