package com.viking.chattagprefix.handler;

import com.viking.chattagprefix.ChatTagPrefix;
import com.viking.chattagprefix.config.ModConfig;
import com.viking.chattagprefix.manager.PrefixManager;
import com.viking.chattagprefix.util.ColorParser;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ChatHandler {

    @SubscribeEvent
    public void onServerChat(ServerChatEvent event) {
        if (!ModConfig.isEnabled()) {
            return;
        }

        ServerPlayer player = event.getPlayer();
        String playerName = player.getName().getString();

        // 获取玩家前缀
        String prefix = PrefixManager.getPlayerPrefix(playerName);

        if (prefix == null || prefix.isEmpty()) {
            return;
        }

        // 解析颜色代码
        Component prefixComponent = ColorParser.parse(prefix);

        // 构建新消息: [Prefix] PlayerName: Message
        MutableComponent newMessage = Component.empty()
                .append(prefixComponent)
                .append(Component.literal(playerName + ": "))
                .append(event.getMessage());

        // 设置新消息
        event.setMessage(newMessage);

        ChatTagPrefix.LOGGER.debug("Applied prefix '{}' for player '{}'", prefix, playerName);
    }
}
