package com.viking.chattagprefix;

import com.viking.chattagprefix.config.ModConfig;
import com.viking.chattagprefix.handler.ChatHandler;
import com.viking.chattagprefix.manager.PrefixManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ChatTagPrefix.MOD_ID)
public class ChatTagPrefix {
    public static final String MOD_ID = "chattagprefix";
    public static final Logger LOGGER = LogManager.getLogger();

    public ChatTagPrefix() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // 注册配置
        ModLoadingContext.get().registerConfig(Type.COMMON, ModConfig.SPEC);

        // 注册设置事件
        modEventBus.addListener(this::commonSetup);

        // 注册游戏事件
        MinecraftForge.EVENT_BUS.register(new ChatHandler());

        LOGGER.info("ChatTagPrefix mod initialized");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // 加载 TAB 配置和前缀数据
        event.enqueueWork(() -> {
            PrefixManager.loadConfig();
            LOGGER.info("ChatTagPrefix config loaded");
        });
    }
}
