package org.plefevre.Model;

import org.plefevre.Effect;
import org.plefevre.Game;
import org.plefevre.View.Block_LvlComplete;

import java.io.*;
import java.util.ArrayList;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Hero {

    static final String FILE_SAVE = "heroes.txt";
    static ArrayList<Hero> heroes = new ArrayList<>();

    public final static int INVENTORY_SIZE = 10;

    final static int CLASS_WARRIOR = 0;
    final static int CLASS_MAGE = 1;
    final static int CLASS_ARCHER = 2;

    private String name;
    private String className;
    private int id;
    private int lvl = 1;
    private int experience = 0;
    private int attack = 1;
    private int defense = 1;
    private int hit_point = 1;
    private int point_to_distribute = 0;

    private int pv = 100;
    private int mana = 100;
    private int x;
    private int y;

    Artifact[] inventory = new Artifact[INVENTORY_SIZE];

    ArrayList<Effect> effects = new ArrayList<Effect>();

    Artifact current_weapon = null;
    Artifact current_armor = null;
    Artifact current_helm = null;

    public Hero(String name, String className) {
        setName(name);
        setClassName(className);
    }



    public int getAttack() {
        int att = 2 + (lvl + attack - 1) * 3;
        if (current_weapon != null) att += current_weapon.getAttack(this);

        for (Effect effect : effects) att += effect.getAttack() * 3;

        return att;
    }

    public int getDefense() {
        int def = 2 + (lvl + defense - 1) * 3;
        if (current_armor != null) def += current_armor.getDefense(this);
        if (current_helm != null) def += current_helm.getDefense(this);

        for (Effect effect : effects) def += effect.getDefense() * 3;

        return def;
    }

    public static void saveHeroes() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_SAVE))) {
            for (Hero hero : heroes) {
                writer.write(hero.name + "," + hero.className + "," + hero.lvl + "," +
                        hero.experience + "," + hero.attack + "," + hero.defense + "," + hero.hit_point);

                // Save current_weapon, current_armor, and current_helm
                writer.write(",");
                writer.write(hero.current_weapon != null ? hero.current_weapon.toString() : "null");
                writer.write(",");
                writer.write(hero.current_armor != null ? hero.current_armor.toString() : "null");
                writer.write(",");
                writer.write(hero.current_helm != null ? hero.current_helm.toString() : "null");

                // Save inventory (all artifacts)
                writer.write(",");
                for (Artifact artifact : hero.inventory) {
                    writer.write(artifact != null ? artifact.toString() : "null");
                    writer.write(";");
                }
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error while saving heroes : " + e.getMessage());
            System.exit(1);
        }
    }

    public static void loadHeroes() {
        File file = new File(FILE_SAVE);

        if (!file.exists()) {
            return;
        }

        heroes.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_SAVE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                String name = data[0];
                String className = data[1];
                int level = Integer.parseInt(data[2]);
                int experience = Integer.parseInt(data[3]);
                int attack = Integer.parseInt(data[4]);
                int defense = Integer.parseInt(data[5]);
                int hit_point = Integer.parseInt(data[6]);

                Hero hero = new Hero(name, className);

                hero.lvl = level;
                hero.experience = experience;
                hero.attack = attack;
                hero.defense = defense;
                hero.hit_point = hit_point;

                hero.current_weapon = !data[7].equals("null") ? Artifact.fromString(data[7]) : null;
                hero.current_armor = !data[8].equals("null") ? Artifact.fromString(data[8]) : null;
                hero.current_helm = !data[9].equals("null") ? Artifact.fromString(data[9]) : null;

                // Load inventory
                String[] inventoryItems = data[10].split(";");
                for (int i = 0; i < inventoryItems.length; i++) {
                    hero.inventory[i] = !inventoryItems[i].equals("null") ? Artifact.fromString(inventoryItems[i]) : null;
                }
                hero.setId(heroes.size());
                heroes.add(hero);
            }
        } catch (IOException e) {
            System.out.println("Error while loading heroes : " + e.getMessage());
        }
    }


    public static ArrayList<Hero> getHeroesSaved() {
        return heroes;
    }

    public static void addHero(Hero newHero) {
        newHero.setId(heroes.size());
        heroes.add(newHero);
        saveHeroes();
    }

    public void setName(String name) {
        this.name = name.replaceAll(",", "");
    }

    public void setClassName(String className) {
        this.className = className.replaceAll(",", "");
    }

    public boolean addToInventory(Artifact artifact) {
        for (int i = 0; i < INVENTORY_SIZE; i++) {
            if (inventory[i] == null) {
                inventory[i] = artifact;
                return true;
            }
        }
        return false;
    }

    public int getNbFreeInventory() {
        int nb = 0;

        for (int i = 0; i < INVENTORY_SIZE; i++) if (inventory[i] == null) nb++;

        return nb;
    }

    public void unequip(int type, int id) {
        if (id == -1) {
            for (int i = 0; i < INVENTORY_SIZE; i++) {
                if (inventory[i] == null) {
                    id = i;
                    break;
                }
            }
        }

        if (id < 0 || id >= Hero.INVENTORY_SIZE) return;
        if (inventory[id] != null) return;


        if (type == Artifact.TYPE_WEAPON) {
            inventory[id] = current_weapon;
            current_weapon = null;
        }

        if (type == Artifact.TYPE_ARMOR) {
            inventory[id] = current_armor;
            current_armor = null;
        }

        if (type == Artifact.TYPE_HELM) {
            inventory[id] = current_helm;
            current_helm = null;
        }
    }

    public void use(int id_inv) {
        if (id_inv < 0 || id_inv >= Hero.INVENTORY_SIZE) return;

        Artifact artifact = inventory[id_inv];
        if (artifact != null && artifact.getType() == Artifact.TYPE_POTION && artifact.getLvl() <= lvl) {
            artifact.use(this);
            inventory[id_inv] = null;
        }
    }

    public void equip(int id_inv) {
        if (id_inv < 0 || id_inv >= Hero.INVENTORY_SIZE) return;

        Artifact artifact = inventory[id_inv];
        if (artifact == null)
            return;


        if (artifact.getType() == Artifact.TYPE_WEAPON) {
            inventory[id_inv] = current_weapon;
            current_weapon = artifact;
        }
        if (artifact.getType() == Artifact.TYPE_ARMOR) {
            inventory[id_inv] = current_armor;
            current_armor = artifact;
        }
        if (artifact.getType() == Artifact.TYPE_HELM) {
            inventory[id_inv] = current_helm;
            current_helm = artifact;
        }
    }

    public int xpToEndLvl() {
        return (int) (lvl * 1000 + Math.pow(lvl - 1, 2) * 450);
    }

    public int getMaxPV() {
        int v = 85 + 10 * hit_point + lvl * 5;
        for (Artifact artifact : inventory) if (artifact != null) v += artifact.getPassivVitality();

        return v;
    }

    public int getMaxMana() {
        int v = 90 + lvl * 10;
        for (Artifact artifact : inventory) if (artifact != null) v += artifact.getPassivMana();


        return v;
    }

    public void addXp(int xp) {
        experience += xp;
        while (experience > xpToEndLvl()) {
            experience -= xpToEndLvl();
            lvl++;
            point_to_distribute++;
        }
    }


    public String getAsciiArt() {
        return "     )\n" +
                "    (=)\n" +
                "   (\\_/)\n" +
                "   |\\#/|\n" +
                "   |/_\\ +)=>>\n" +
                "    T^T\n" +
                "    | |";
    }

    public void displayHero() {
        String asciiArt = getAsciiArt();

        String heroInfo = getHeroInfoFormated();

        String artifacts = String.format(
                "║%-15s : %-29s ║\n" +
                        "║%-15s : %-29s ║\n" +
                        "║%-15s : %-29s ║",
                "Current Weapon", (current_weapon != null ? current_weapon.getName() : "None"),
                "Current Armor", (current_armor != null ? current_armor.getName() : "None"),
                "Current Helm", (current_helm != null ? current_helm.getName() : "None")
        );

        // Affiche l'art ASCII à gauche et les informations du héros à droite
        System.out.println("╔════════════════╦═══════════════════════════════╗");
        String[] artLines = asciiArt.split("\n");
        String[] infoLines = heroInfo.split("\n");
        int maxLines = max(artLines.length, infoLines.length);

        // Imprime chaque ligne de l'art ASCII avec l'info correspondante
        for (int i = 0; i < maxLines; i++) {
            String artLine = i < artLines.length ? artLines[i] : "";
            String infoLine = i < infoLines.length ? infoLines[i] : "";
            System.out.printf("║ %-14s ║ %-29s ║\n", artLine, infoLine);
        }

        System.out.println("╠════════════════╩═══════════════════════════════╣");

        // Affiche les artefacts en dessous
        System.out.println(artifacts);

        System.out.println("╚════════════════════════════════════════════════╝");
    }

    public String getHeroInfoFormated() {
        return String.format(
                "%-15s : %s\n" +
                        "%-15s : %s\n" +
                        "%-15s : %d\n" +
                        "%-15s : %d\n" +
                        "%-15s : %d\n" +
                        "%-15s : %d\n" +
                        "%-15s : %d\n",
                "Name", name,
                "Class", className,
                "Level", lvl,
                "Experience", experience,
                "Attack", attack,
                "Defense", defense,
                "Hit Points", hit_point
        );
    }

    public void setMana(int m) {
        mana = min(m, getMaxMana());
    }

    public void addPV(int p) {
        pv = min(pv + p, getMaxPV());

    }

    public void addMana(int m) {
        mana = min(mana + m, getMaxMana());
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void addEffect(Effect effect) {
        effects.add(effect);
    }

    public void regenerate() {

        int v = lvl / 4 + 1;
        for (Artifact artifact : inventory) if (artifact != null) v += artifact.getPassivRegeneration();

        addPV(v);
        addMana(v);

    }

    public int getDefenseFight() {
        float precision = 0;
        for (Artifact artifact : inventory) if (artifact != null) precision += artifact.getPassivPrecision() / 32f;
        precision = min(1, precision);
        for (Artifact artifact : inventory) if (artifact != null) precision += artifact.getPassivLuck() / 10f;

        int v = (int) (defense + (4 + lvl / 2) * ((1 - Math.random() * (1 - precision)) - 0.5));

        for (Artifact artifact : inventory) if (artifact != null) v += artifact.getPassivProtection();

        return v;
    }


    public int getAttackFight() {
        float precision = 0;
        for (Artifact artifact : inventory) if (artifact != null) precision += artifact.getPassivPrecision() / 32f;
        precision = min(1, precision);
        for (Artifact artifact : inventory) if (artifact != null) precision += artifact.getPassivLuck() / 10f;

        int v = (int) (getAttack() + (4 + lvl / 2) * ((1 - Math.random() * (1 - precision)) - 0.5));

        for (Artifact artifact : inventory) if (artifact != null) v += artifact.getPassivPower();

        return v;
    }

    public boolean removeMana(int m) {
        float f = 100;
        for (Artifact artifact : inventory) if (artifact != null) f -= artifact.getPassivEndurance();
        f = max(20, f);
        m = (int) (m * f / 100);

        if (mana < m)
            return false;

        mana -= m;
        return true;
    }

    public int getEffectAtt() {
        int v = 0;
        for (Effect effect : effects) v += effect.getAttack();
        return v;
    }

    public int getEffectDef() {
        int v = 0;
        for (Effect effect : effects) v += effect.getDefense();
        return v;
    }


    public void throwE(int id) {
        if (id < 0 || id >= Hero.INVENTORY_SIZE)
            return;

        inventory[id] = null;
    }

    public Artifact getInventory(int id) {
        if (id < 0 || id >= Hero.INVENTORY_SIZE)
            return null;

        return inventory[id];
    }

    public int getLvl() {
        return lvl;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public String getName() {
        return name;
    }

    public String getClassName() {
        return className;
    }

    public int getExperience() {
        return experience;
    }

    public int getAttackPoint() {
        return attack;
    }

    public int getDefensePoint() {
        return defense;
    }

    public int getHit_point() {
        return hit_point;
    }

    public int getPv() {
        return pv;
    }

    public int getMana() {
        return mana;
    }

    public Artifact getCurrent_weapon() {
        return current_weapon;
    }

    public Artifact getCurrent_armor() {
        return current_armor;
    }

    public Artifact getCurrent_helm() {
        return current_helm;
    }

    public void removePv(int degat) {
        pv = max(0, pv - degat);
    }

    public void setPv(int pv) {
        this.pv = pv;
    }

    public ArrayList<Effect> getEffects() {
        return effects;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public void setHit_point(int hit_point) {
        this.hit_point = hit_point;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPoint_to_distribute() {
        return point_to_distribute;
    }

    public void setPoint_to_distribute(int point_to_distribute) {
        this.point_to_distribute = point_to_distribute;
    }
}
