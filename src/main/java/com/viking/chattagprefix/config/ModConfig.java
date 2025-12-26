package com.viking.chattagprefix.config;

import net.minecraftforge.common.ForgeConfigSpec;
import java.util.List;

public class ModConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.BooleanValue ENABLED;
    public static final ForgeConfigSpec.ConfigValue<String> TAB_CONFIG_PATH;
    public static final ForgeConfigSpec.ConfigValue<String> DEFAULT_GROUP;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> PLAYER_GROUPS;

    static {
        BUILDER.push("general");

        ENABLED = BUILDER
                .comment("Enable or disable chat prefix feature")
                .define("enabled", true);

        TAB_CONFIG_PATH = BUILDER
                .comment("Path to TAB plugin's groups.yml config file")
                .define("tabConfigPath", "config/TAB/groups.yml");

        DEFAULT_GROUP = BUILDER
                .comment("Default group for players not in the player_groups list")
                .define("defaultGroup", "_DEFAULT_");

        BUILDER.pop();

        BUILDER.push("playerGroups");

        PLAYER_GROUPS = BUILDER
                .comment("Player to group mappings in format: 'PlayerName=GroupName'",
                        "Example: 'Steve=Owner', 'Alex=VIP'")
                .defineList("mappings",
                        List.of("ExamplePlayer=Player"),
                        obj -> obj instanceof String && ((String) obj).contains("="));

        BUILDER.pop();

        SPEC = BUILDER.build();
    }

    public static boolean isEnabled() {
        return ENABLED.get();
    }

    public static String getTabConfigPath() {
        return TAB_CONFIG_PATH.get();
    }

    public static String getDefaultGroup() {
        return DEFAULT_GROUP.get();
    }

    public static List<? extends String> getPlayerGroups() {
        return PLAYER_GROUPS.get();
    }
}
