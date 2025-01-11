package org.plefevre.View;

import java.util.HashMap;
import java.util.Map;
public class Smiley {

    public final static char SMILEY_SWORD = 1;
    public final static char SMILEY_SHIELD = 2;
    public final static char SMILEY_HEART = 3;
    public final static char SMILEY_STAR = 4;
    public final static char SMILEY_BOW = 5;
    public final static char SMILEY_FIRE = 6;
    public final static char SMILEY_WIZARD = 7;
    public final static char SMILEY_DRAGON = 8;
    public final static char SMILEY_POTION = 9;
    public final static char SMILEY_CHAIN = 10;
    public final static char SMILEY_CRYSTAL = 11;
    public final static char SMILEY_HORSE = 12;
    public final static char SMILEY_TARGET = 13;
    public final static char SMILEY_GOLD = 14;
    public final static char SMILEY_SKULL = 15;
    public final static char SMILEY_CROWN = 16;
    public final static char SMILEY_PICKAXE = 17;
    public final static char SMILEY_TROPHY = 18;
    public final static char SMILEY_BALANCE = 19;
    public final static char SMILEY_HAMMER = 20;
    public final static char SMILEY_DAGGER = 21;
    public final static char SMILEY_HEART_BLACK = 22;
    public final static char SMILEY_CANDLE = 23;
    public final static char SMILEY_DUEL = 24;
    public final static char SMILEY_BARD = 25;
    public final static char SMILEY_VAMPIRE = 26;
    public final static char SMILEY_KNIGHT = 27;
    public final static char SMILEY_HOURGLASS = 28;
    public final static char SMILEY_MEDAL = 29;
    public final static char SMILEY_SCROLL = 30;

    final static Map<Character, String> smileyMap = new HashMap<>();

    static {
        // Initialisation de la table de correspondance
        smileyMap.put(SMILEY_SWORD, "🗡️️️️");
        smileyMap.put(SMILEY_SHIELD, "🛡️");
        smileyMap.put(SMILEY_HEART, "❤️");
        smileyMap.put(SMILEY_STAR, "⭐");
        smileyMap.put(SMILEY_BOW, "🏹");
        smileyMap.put(SMILEY_FIRE, "🔥");
        smileyMap.put(SMILEY_WIZARD, "🧙‍");
        smileyMap.put(SMILEY_DRAGON, "🐉");
        smileyMap.put(SMILEY_POTION, "⚗️");
        smileyMap.put(SMILEY_CHAIN, "⛓️");
        smileyMap.put(SMILEY_CRYSTAL, "💎");
        smileyMap.put(SMILEY_HORSE, "⛓️");
        smileyMap.put(SMILEY_TARGET, "⛓️");
        smileyMap.put(SMILEY_GOLD, "💰");
        smileyMap.put(SMILEY_SKULL, "💀");
        smileyMap.put(SMILEY_CROWN, "👑");
        smileyMap.put(SMILEY_PICKAXE, "⛏️");
        smileyMap.put(SMILEY_TROPHY, "🏆");
        smileyMap.put(SMILEY_BALANCE, "⚖️");
        smileyMap.put(SMILEY_HAMMER, "⚒️");
        smileyMap.put(SMILEY_DAGGER, "🗡️");
        smileyMap.put(SMILEY_HEART_BLACK, "🖤");
        smileyMap.put(SMILEY_CANDLE, "🕯️");
        smileyMap.put(SMILEY_DUEL, "⚔️");
        smileyMap.put(SMILEY_BARD, "🎶");
        smileyMap.put(SMILEY_VAMPIRE, "🧛‍");
        smileyMap.put(SMILEY_KNIGHT, "🦸‍");
        smileyMap.put(SMILEY_HOURGLASS, "⏳");
        smileyMap.put(SMILEY_MEDAL, "🥇");
        smileyMap.put(SMILEY_SCROLL, "📜");
    }
    final static int[] width_smile = {1,1,1,2,2,2,2,2,1,1,2,1,1,2,2,2,1,2,1,1,1,2,1,1,2,2,2,2,2,2,2};

}
