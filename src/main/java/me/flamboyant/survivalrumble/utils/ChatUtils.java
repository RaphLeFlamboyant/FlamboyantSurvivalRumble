package me.flamboyant.survivalrumble.utils;

import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ChatUtils {
    public static final List<ChatColor> chatColorValues = Collections.unmodifiableList(Arrays.asList(ChatColor.values()));
    public static final int chatColorValuesSize = chatColorValues.size();
    public static final ChatColor personalTitleColor = ChatColor.BLUE;
    public static final ChatColor questTitleColor = ChatColor.GREEN;
    public static final ChatColor generalTitleColor = ChatColor.GOLD;
    public static final ChatColor personalCorpusColor = ChatColor.GRAY;
    public static final ChatColor questCorpusColor = ChatColor.GRAY;
    public static final ChatColor generalCorpusColor = ChatColor.WHITE;
    public static final ChatColor feedbackColor = ChatColor.WHITE;
    public static final ChatColor debugColor = ChatColor.WHITE;

    public static String debugMessage(String message) {
        return ChatColor.RED + "[DEBUG] " + debugColor + message;
    }

    public static String generalAnnouncement(String title, String corpus) {
        return "\n" + ChatColor.BOLD + ChatColor.RED + "[" + generalTitleColor + title + ChatColor.RED + "]\n"
                + ChatColor.RESET + ChatColor.ITALIC + corpus + "\n";
    }

    public static String personalAnnouncement(String title, String corpus) {
        return "\n" + ChatColor.UNDERLINE + personalTitleColor + title + "\n"
                + ChatColor.RESET + ChatColor.ITALIC + personalCorpusColor + corpus + "\n";
    }

    public static String questAnnouncement(String title, String points, String corpus) {
        return "\n" + ChatColor.GOLD + "[" + questTitleColor + title + ChatColor.GOLD + "]" + ChatColor.WHITE + " - (" + points + " pts)\n"
                + ChatColor.RESET + ChatColor.ITALIC + questCorpusColor + corpus + "\n";
    }

    public static String feedback(String message) {
        return ChatColor.GREEN + "é" + ChatColor.RESET + message;
    }
}
