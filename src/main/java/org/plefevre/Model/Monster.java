package org.plefevre.Model;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Monster {
    String name;
    int lvl;
    int mana;
    int pv;
    int initial_pv;
    int initial_mana;
    int attack;
    int defense;

    public Monster(int lvl) {
        this.lvl = lvl;
        name = MonsterInfo.getRandomName(lvl);

        initial_pv = calcInitial_pv();
        initial_mana = calcInitial_mana();
        attack = calcAttack();
        defense = calcDefense();

        pv = initial_pv;
        mana = initial_mana;
    }

    private int calcDefense() {
        return (int) (2 + lvl * (2 + Math.random() * 3));
    }

    private int calcAttack() {
        return (int) (2 + lvl * (2 + Math.random() * 3));
    }

    public int calcInitial_pv() {
        return (int) (20 + lvl * (20 + Math.random() * 7));
    }

    public int calcInitial_mana() {
        return (int) (30 + lvl * (10 + Math.random() * 4));
    }


    public int xpGet() {
        return (int) (lvl * 400 * (0.9 + Math.random() * 0.1));
    }

    public String getAsciiArt() {
        if (name.equals("Rat")) return
                "    .  ,\n" +
                        "   (\\;/)\n" +
                        "  oo   \\//,        _\n" +
                        ",/_;~      \\,     / '\n" +
                        "\"'    (  (   \\    !\n" +
                        "      //  \\   |__.'\n" +
                        "    '~  '~----''";
        if (name.equals("Goblin")) return
                "  ,      ,\n" +
                        " /(.-\"\"-.)\\\n" +
                        " |  o  o  |\n" +
                        " \\  ( )   /\n" +
                        "  '-/  \\-' \n" +
                        "    \\__/  \n" +
                        "   /    \\\n";
        if (name.equals("Kobold")) return
                "          {\\ /}     \n" +
                        "       /\\ | \" | /\\\n" +
                        "      / \\\\(o o)/ \\\\\n" +
                        "      |  :/ ^ \\:  |\n" +
                        "      |  ((   ))  |\n" +
                        "      |'  \\\" \"/  '|\n" +
                        "           )I(\n" +
                        "          \"\"`\"\"";
        if (name.equals("Giant Spider")) return
                "\n" +
                        "\n" +
                        "     /^\\ ___ /^\\\n" +
                        "    //^\\(o o)/^\\\\\n" +
                        "   /'<^~``~''~^>`\\";
        if (name.equals("Orc")) return "   --\\\\|//--\n" +
                "  { --   -- }\n" +
                "|\\{{o | | o}}/|\n" +
                "\\ \\-  / \\  -/ /\n" +
                " \\/|   =   |\\/\n" +
                "  |_ V---V _|\n" +
                "    \\_)_(_/";
        if (name.equals("Bandit")) return "⠀⠀⠀⠀⢀⡀⠀⠀⠀⠀\n" +
                "⠀⣠⣶⣿⣿⣿⣿⣷⣄⠀\n" +
                "⢰⣿⣿⣿⣿⣿⣿⣿⣿⣆\n" +
                "⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿\n" +
                "⣠⣴⣶⣦⣤⣤⣴⣶⣦⣄\n" +
                "⢿⣧⣤⣼⣿⣿⣧⣤⣼⡿\n" +
                "⠀⠀⠉⠁⠀⠀⠈⠉⠀⠀";
        if (name.equals("Troll")) return "\n\n\n( o . o)";
        if (name.equals("Hobgoblin")) return
                "  /\\    /\\\n" +
                        " /(.-\"\"-.)\\\n" +
                        " |  o  o  |\n" +
                        " \\   ( )   /\n" +
                        "  '-/  \\-' \n" +
                        "    \\__/  \n" +
                        "   /    \\\n";
        ;
        if (name.equals("Zombie")) return "\n\n\n\n(o_o)";
        if (name.equals("Ghoul")) return "";
        if (name.equals("Warg")) return "";
        if (name.equals("Vampire")) return "";
        if (name.equals("Minotaur")) return "";
        if (name.equals("Basilisk")) return "";
        if (name.equals("Werewolf")) return "";
        if (name.equals("Ogre")) return "";
        if (name.equals("Manticore")) return "";
        if (name.equals("Skeleton King")) return "";
        if (name.equals("Lich")) return "";
        if (name.equals("Wyvern")) return "";
        if (name.equals("Wraith")) return "";
        if (name.equals("Beholder")) return "";
        if (name.equals("Stone Golem")) return "";
        if (name.equals("Dragon")) return "     _(,  {\\V/}  ,)_\n" +
                "       /\\ | \" | /\\\n" +
                "      //\\\\(o o)//\\\\\n" +
                "      ||::/ ^ \\::||\n" +
                "      |||((   ))|||\n" +
                "      |'  \\\" \"/  '|\n" +
                "           )I(\n" +
                "          \"\"`\"\"";
        if (name.equals("Ancient Red Dragon")) return
                "(______ <\\-/> ______)\n" +
                        " /_.-=-.\\| \" |/.-=-._\\\n" +
                        "  /_    \\(o_o)/    _\\\n" +
                        "   /_  /\\/ ^ \\/\\  _\\\n" +
                        "     \\/ | / \\ | \\/\n" +
                        "       /((( )))\\\n" +
                        "     __\\ \\___/ /__\n" +
                        "   (((---'   '---)))";


        return "";
    }

    public int getMaxPV() {
        return initial_pv;
    }

    public int getMaxMana() {
        return initial_mana;
    }

    public void setMana(int m) {
        mana = min(m, initial_mana);
    }

    public ArrayList<Artifact> getArtifact() {
        ArrayList<Artifact> artifacts = new ArrayList<>();

        if (Math.random() > 0.4f) artifacts.add(getOneArtifact());
        if (Math.random() > 0.75f) artifacts.add(getOneArtifact());
        return artifacts;
    }

    public Artifact getOneArtifact() {
        return ArtifactLibrary.artifactLibrary.getArtifactByLevel(lvl, 2);
    }

    public String getName() {
        return name;
    }

    public int getLvl() {
        return lvl;
    }

    public int getMana() {
        return mana;
    }

    public int getPv() {
        return pv;
    }

    public int getInitial_pv() {
        return initial_pv;
    }

    public int getInitial_mana() {
        return initial_mana;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public void removePv(int degat) {
        pv = max(0, pv-degat);
    }

    public void removeMana(int manaConsom) {
        mana = max(0, mana-manaConsom);
    }
}


class MonsterInfo {
    String name;
    int baseLevel;

    static List<MonsterInfo> monsterList = new ArrayList<>();

    public MonsterInfo(String name, int baseLevel) {
        this.name = name;
        this.baseLevel = baseLevel;
    }

    public static String getRandomName(int lvl) {

        if (monsterList.isEmpty()) {
            addMonsters();
        }

        List<String> possibleMonsters = new ArrayList<>();

        for (MonsterInfo monster : monsterList) {
            if (monster.baseLevel >= lvl - 2 && monster.baseLevel <= lvl + 2) {
                possibleMonsters.add(monster.name);
            }
        }

        if (possibleMonsters.isEmpty()) {
            return "Unknown Monster";
        }

        Random random = new Random();
        return possibleMonsters.get(random.nextInt(possibleMonsters.size()));
    }

    public static void addMonsters() {
        monsterList.clear();
        monsterList.add(new MonsterInfo("Rat", 1));
        monsterList.add(new MonsterInfo("Goblin", 2));
        monsterList.add(new MonsterInfo("Kobold", 3));
        monsterList.add(new MonsterInfo("Giant Spider", 4));
        monsterList.add(new MonsterInfo("Orc", 5));
        monsterList.add(new MonsterInfo("Bandit", 6));
        monsterList.add(new MonsterInfo("Troll", 6));
        monsterList.add(new MonsterInfo("Hobgoblin", 7));
        monsterList.add(new MonsterInfo("Zombie", 8));
        monsterList.add(new MonsterInfo("Ghoul", 9));
        monsterList.add(new MonsterInfo("Warg", 10));
        monsterList.add(new MonsterInfo("Vampire", 11));
        monsterList.add(new MonsterInfo("Minotaur", 11));
        monsterList.add(new MonsterInfo("Basilisk", 12));
        monsterList.add(new MonsterInfo("Werewolf", 13));
        monsterList.add(new MonsterInfo("Ogre", 14));
        monsterList.add(new MonsterInfo("Manticore", 15));
        monsterList.add(new MonsterInfo("Skeleton King", 15));
        monsterList.add(new MonsterInfo("Lich", 16));
        monsterList.add(new MonsterInfo("Wyvern", 17));
        monsterList.add(new MonsterInfo("Wraith", 18));
        monsterList.add(new MonsterInfo("Beholder", 18));
        monsterList.add(new MonsterInfo("Stone Golem", 19));
        monsterList.add(new MonsterInfo("Dragon", 20));
        monsterList.add(new MonsterInfo("Ancient Red Dragon", 21));

    }

    public String getName() {
        return name;
    }

    public int getBaseLevel() {
        return baseLevel;
    }
}
