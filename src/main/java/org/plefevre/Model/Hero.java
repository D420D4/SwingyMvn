package org.plefevre.Model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.plefevre.Controller.ValidSum;

import java.sql.*;
import java.util.ArrayList;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static org.plefevre.Model.Artifact.loadArtifactById;

@ValidSum
public class Hero {

    public final static int INVENTORY_SIZE = 10;

    final static int CLASS_WARRIOR = 0;
    final static int CLASS_MAGE = 1;
    final static int CLASS_ARCHER = 2;

    @NotNull
    @Size(min = 3, max = 15)
    @Pattern(regexp = "^[a-zA-Z0-9]+$")
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

    public static Hero loadHeroById(int heroId) {
        if (heroId <= 0) return null;
        Connection connection = DatabaseSetup.getConnection();

        String loadArtifactQuery = "SELECT * FROM Hero WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(loadArtifactQuery)) {
            preparedStatement.setInt(1, heroId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
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
                    hero.loadHeroInventory();
                    return hero;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while loading hero : " + e.getMessage());
        }
        return null;
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

        if (getCurrent_armor() != null) getCurrent_armor().saveArtifact();
        if (getCurrent_helm() != null) getCurrent_helm().saveArtifact();
        if (getCurrent_weapon() != null) getCurrent_weapon().saveArtifact();

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
            preparedStatement.setObject(9, getCurrent_weapon() == null ? null : getCurrent_weapon().getId(), Types.INTEGER);
            preparedStatement.setObject(10, getCurrent_armor() == null ? null : getCurrent_armor().getId(), Types.INTEGER);
            preparedStatement.setObject(11, getCurrent_helm() == null ? null : getCurrent_helm().getId(), Types.INTEGER);
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
            throw new RuntimeException("Error while saving hero : " + e.getMessage());
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
            throw new RuntimeException("Error while deleting Inventory : " + e.getMessage());
        }

        try (PreparedStatement saveStatement = connection.prepareStatement(saveInventoryQuery)) {
            for (Artifact artifact : inventory) {
                if (artifact == null) continue;
                artifact.saveArtifact();
                saveStatement.setInt(1, getId());
                saveStatement.setInt(2, artifact.getId());
                saveStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while saving Inventory : " + e.getMessage());
        }
    }

    public static ArrayList<Hero> getAllHeroes() {
        Connection connection = DatabaseSetup.getConnection();

        String loadHeroesQuery = "SELECT * FROM Hero";

        ArrayList<Hero> heroes = new ArrayList<>();

        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(loadHeroesQuery)) {

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
                hero.loadHeroInventory();
                heroes.add(hero);
            }
            System.out.println("Heroes loaded successfully.");
        } catch (SQLException e) {
            throw new RuntimeException("Error loading heroes : " + e.getMessage());
        }

        return heroes;
    }

    public void loadHeroInventory() {
        Connection connection = DatabaseSetup.getConnection();

        String loadInventoryQuery = "SELECT * FROM HeroInventory WHERE hero_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(loadInventoryQuery)) {
            preparedStatement.setInt(1, getId());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int artifactId = resultSet.getInt("artifact_id");
                    Artifact artifact = loadArtifactById(artifactId);
                    addToInventory(artifact);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while loading Inventory : " + e.getMessage());
        }
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

    public void use(Artifact artifact)
    {
        if(artifact == null) return;

        if(artifact.getType() == Artifact.TYPE_POTION && artifact.getLvl() <= lvl)
        {
            artifact.use(this);
            throwArtifact(artifact);
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

    public void equip(Artifact artifact) {
        for (int i = 0; i<INVENTORY_SIZE; i++){
            if (inventory[i] == artifact){
                equip(i);
                return;
            }
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

        System.out.println("╔════════════════╦═══════════════════════════════╗");
        String[] artLines = asciiArt.split("\n");
        String[] infoLines = heroInfo.split("\n");
        int maxLines = max(artLines.length, infoLines.length);

        for (int i = 0; i < maxLines; i++) {
            String artLine = i < artLines.length ? artLines[i] : "";
            String infoLine = i < infoLines.length ? infoLines[i] : "";
            System.out.printf("║ %-14s ║ %-29s ║\n", artLine, infoLine);
        }

        System.out.println("╠════════════════╩═══════════════════════════════╣");

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

    public void throwArtifact(Artifact artifact) {
        if (artifact == null) return;

        for (int i = 0; i < INVENTORY_SIZE; i++) {
            if (inventory[i] == artifact) {
                inventory[i] = null;
                return;
            }
        }
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
