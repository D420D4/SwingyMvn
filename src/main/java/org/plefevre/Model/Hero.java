package org.plefevre.Model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
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


    public Hero(int id, String name, String className, int lvl, int experience, int attack, int defense, int hit_point, int current_weapon_id, int current_armor_id, int current_helm_id) {
        this.name = name;
        this.className = className;
        this.id = id;
        this.lvl = lvl;
        this.experience = experience;
        this.attack = attack;
        this.defense = defense;
        this.hit_point = hit_point;
        this.current_weapon = loadArtifactById(current_weapon_id);
        this.current_armor = loadArtifactById(current_armor_id);
        this.current_helm = loadArtifactById(current_helm_id);
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

    public void saveHero() {

        Connection connection = DatabaseSetup.getConnection();


        String saveHeroQuery = """
                INSERT INTO Hero (id, name, class_name, lvl, experience, attack, defense, hit_point, current_weapon, current_armor, current_helm)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE
                    name = VALUES(name),
                    class_name = VALUES(class_name),
                    lvl = VALUES(lvl),
                    experience = VALUES(experience),
                    attack = VALUES(attack),
                    defense = VALUES(defense),
                    hit_point = VALUES(hit_point),
                    current_weapon = VALUES(current_weapon),
                    current_armor = VALUES(current_armor),
                    current_helm = VALUES(current_helm);""";

        try (PreparedStatement preparedStatement = connection.prepareStatement(saveHeroQuery, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setObject(1, getId() == 0 ? null : getId(), Types.INTEGER);
            preparedStatement.setString(2, getName());
            preparedStatement.setString(3, getClassName());
            preparedStatement.setInt(4, getLvl());
            preparedStatement.setInt(5, getExperience());
            preparedStatement.setInt(6, getAttackPoint());
            preparedStatement.setInt(7, getDefensePoint());
            preparedStatement.setInt(8, getHit_point());
            preparedStatement.setObject(9, getCurrent_weapon().getId(), Types.INTEGER);
            preparedStatement.setObject(10, getCurrent_armor().getId(), Types.INTEGER);
            preparedStatement.setObject(11, getCurrent_helm().getId(), Types.INTEGER);
            preparedStatement.executeUpdate();

            if (getId() == 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        setId(generatedKeys.getInt(1));
                    }
                }
            }

            saveInventory();

        } catch (SQLException e) {

        }
    }

    public void saveInventory() {
        Connection connection = DatabaseSetup.getConnection();

        String deleteInventoryQuery = "DELETE FROM HeroInventory WHERE hero_id = ?";
        String saveInventoryQuery = "INSERT INTO HeroInventory (hero_id, artifact_id) VALUES (?, ?)";

        try (PreparedStatement deleteStatement = connection.prepareStatement(deleteInventoryQuery)) {
            deleteStatement.setInt(1, getId());
            deleteStatement.executeUpdate();
        } catch (SQLException e) {

        }

        try (PreparedStatement saveStatement = connection.prepareStatement(saveInventoryQuery)) {
            for (Artifact artifact : inventory) {
                if(artifact == null) continue;
                saveStatement.setInt(1, getId());
                saveStatement.setInt(2, artifact.getId());
                artifact.saveArtifact();
                saveStatement.executeUpdate();
            }
        } catch (SQLException e) {

        }
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
        Connection connection = DatabaseSetup.getConnection();

        String loadHeroesQuery = "SELECT * FROM Hero";

        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(loadHeroesQuery)) {
            heroes.clear();
            while (resultSet.next()) {
                Hero hero = new Hero(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("class_name"),
                        resultSet.getInt("lvl"),
                        resultSet.getInt("experience"),
                        resultSet.getInt("attack"),
                        resultSet.getInt("defense"),
                        resultSet.getInt("hit_point"),
                        resultSet.getInt("current_weapon"),
                        resultSet.getInt("current_armor"),
                        resultSet.getInt("current_helm")
                );
                loadHeroInventory(hero);
                heroes.add(hero);
            }
            System.out.println("Heroes loaded successfully.");
        } catch (SQLException e) {
            System.err.println("Error while loading heroes: " + e.getMessage());
        }
    }

    public static void loadHeroInventory(Hero hero) {
        Connection connection = DatabaseSetup.getConnection();

        String loadInventoryQuery = "SELECT * FROM HeroInventory WHERE hero_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(loadInventoryQuery)) {
            preparedStatement.setInt(1, hero.getId());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int artifactId = resultSet.getInt("artifact_id");
                    Artifact artifact = loadArtifactById(artifactId);
                    hero.addToInventory(artifact);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error while loading hero inventory: " + e.getMessage());
        }
    }

    public static Artifact loadArtifactById(int artifactId) {
        Connection connection = DatabaseSetup.getConnection();

        String loadArtifactQuery = "SELECT * FROM Artifact WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(loadArtifactQuery)) {
            preparedStatement.setInt(1, artifactId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Artifact(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getInt("type"),
                            resultSet.getInt("lvl"),
                            resultSet.getInt("class_destination"),
                            resultSet.getInt("attack"),
                            resultSet.getInt("defense"),
                            resultSet.getString("ascii"),
                            resultSet.getString("ascii_color")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error while loading artifact: " + e.getMessage());
        }
        return null;
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
