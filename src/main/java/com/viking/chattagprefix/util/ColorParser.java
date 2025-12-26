package com.viking.chattagprefix.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

import java.util.HashMap;
import java.util.Map;

public class ColorParser {
    private static final Map<Character, ChatFormatting> COLOR_CODES = new HashMap<>();

    static {
        COLOR_CODES.put('0', ChatFormatting.BLACK);
        COLOR_CODES.put('1', ChatFormatting.DARK_BLUE);
        COLOR_CODES.put('2', ChatFormatting.DARK_GREEN);
        COLOR_CODES.put('3', ChatFormatting.DARK_AQUA);
        COLOR_CODES.put('4', ChatFormatting.DARK_RED);
        COLOR_CODES.put('5', ChatFormatting.DARK_PURPLE);
        COLOR_CODES.put('6', ChatFormatting.GOLD);
        COLOR_CODES.put('7', ChatFormatting.GRAY);
        COLOR_CODES.put('8', ChatFormatting.DARK_GRAY);
        COLOR_CODES.put('9', ChatFormatting.BLUE);
        COLOR_CODES.put('a', ChatFormatting.GREEN);
        COLOR_CODES.put('b', ChatFormatting.AQUA);
        COLOR_CODES.put('c', ChatFormatting.RED);
        COLOR_CODES.put('d', ChatFormatting.LIGHT_PURPLE);
        COLOR_CODES.put('e', ChatFormatting.YELLOW);
        COLOR_CODES.put('f', ChatFormatting.WHITE);
        COLOR_CODES.put('k', ChatFormatting.OBFUSCATED);
        COLOR_CODES.put('l', ChatFormatting.BOLD);
        COLOR_CODES.put('m', ChatFormatting.STRIKETHROUGH);
        COLOR_CODES.put('n', ChatFormatting.UNDERLINE);
        COLOR_CODES.put('o', ChatFormatting.ITALIC);
        COLOR_CODES.put('r', ChatFormatting.RESET);
    }

    public static Component parse(String text) {
        if (text == null || text.isEmpty()) {
            return Component.empty();
        }

        MutableComponent result = Component.empty();
        StringBuilder currentText = new StringBuilder();
        Style currentStyle = Style.EMPTY;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            if ((c == '&' || c == '\u00A7') && i + 1 < text.length()) {
                char code = Character.toLowerCase(text.charAt(i + 1));

                if (COLOR_CODES.containsKey(code)) {
                    if (currentText.length() > 0) {
                        result = result.append(Component.literal(currentText.toString()).withStyle(currentStyle));
                        currentText = new StringBuilder();
                    }

                    ChatFormatting format = COLOR_CODES.get(code);

                    if (format == ChatFormatting.RESET) {
                        currentStyle = Style.EMPTY;
                    } else if (format.isColor()) {
                        currentStyle = Style.EMPTY.withColor(format);
                    } else {
                        currentStyle = applyFormat(currentStyle, format);
                    }

                    i++;
                    continue;
                }
            }

            currentText.append(c);
        }

        if (currentText.length() > 0) {
            result = result.append(Component.literal(currentText.toString()).withStyle(currentStyle));
        }

        return result;
    }

    private static Style applyFormat(Style style, ChatFormatting format) {
        return switch (format) {
            case BOLD -> style.withBold(true);
            case ITALIC -> style.withItalic(true);
            case UNDERLINE -> style.withUnderlined(true);
            case STRIKETHROUGH -> style.withStrikethrough(true);
            case OBFUSCATED -> style.withObfuscated(true);
            default -> style;
        };
    }
}
