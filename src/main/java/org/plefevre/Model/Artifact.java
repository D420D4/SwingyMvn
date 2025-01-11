package org.plefevre.Model;

import org.plefevre.Effect;
import org.plefevre.Game;

import java.util.ArrayList;

public class Artifact {
    public final static int TYPE_WEAPON = 1;
    public final static int TYPE_ARMOR = 2;
    public final static int TYPE_HELM = 3;
    public final static int TYPE_POTION = 4;
    public final static int TYPE_ACCESSORY = 5;

    private String name;
    private int type;
    private int lvl;
    protected char[] ascii;
    protected byte[] ascii_color;
    private int class_destination = -1;
    private int attack;
    private int defense;

    public Artifact(String name, int type, int lvl, Hero hero) {
        this.name = name;
        this.type = type;
        setLvl(lvl);
    }

    public Artifact() {

    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public int getLvl() {
        return lvl;
    }

    @Override
    public String toString() {

        String asciiString = new String(ascii);

        StringBuilder asciiColorString = new StringBuilder();
        for (int i = 0; i < ascii_color.length; i++) {
            byte b = ascii_color[i];
            asciiColorString.append(String.format("%4d", b));
        }

        return name + ":" + type + ":" + lvl + ":" + attack + ":" + defense + ":" + class_destination + ":" + asciiColorString + ":" + asciiString;
    }

    public static Artifact fromString(String data) {
        String[] parts = data.split(":", 8);

        if (parts.length != 8) {
//            return null;
            throw new IllegalArgumentException("Invalid artifact data: " + data);
        }

        Artifact artifact = new Artifact();
        artifact.name = parts[0];
        artifact.type = Integer.parseInt(parts[1]);
        artifact.lvl = Integer.parseInt(parts[2]);
        artifact.attack = Integer.parseInt(parts[3]);
        artifact.defense = Integer.parseInt(parts[4]);
        artifact.class_destination = Integer.parseInt(parts[5]);
        String colorParts = parts[6];
        artifact.ascii = parts[7].toCharArray();

        artifact.ascii_color = new byte[colorParts.length() / 4];
        for (int i = 0; i < colorParts.length(); i += 4) {
            artifact.ascii_color[i / 4] = Byte.parseByte(
                    colorParts.substring(i, i + 4).replaceAll(" ", ""));
        }
        return artifact;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setLvl(int lvl ) {
        this.lvl = lvl;

        setAttack();
        setDefense();
    }

    public void setAscii(char[] ascii) {
        this.ascii = ascii;
    }

    /*
        public void setAscii_color(byte[] ascii_color) {
            this.ascii_color = ascii_color;
        }
    */
    public void setAscii_color(byte... b) {
        this.ascii_color = b;
    }

    public void setAscii_color(int... b) {
        this.ascii_color = new byte[b.length];
        for (int i = 0; i < b.length; i++) {
            ascii_color[i] = (byte) b[i];
        }
    }


    public void setClass_destination(int class_destination) {
        this.class_destination = class_destination;
    }

    public byte getColor() {
        if (getLvl() > 14)
            return 6;
        else if (getLvl() > 7)
            return 3;

        return 2;
    }

    public String getName(int width) {
        String name = getName();
        if (name.length() > width - 2)
            name = name.substring(0, width - 4) + "..";

        return name;
    }

    public void setAttack() {
        if (type == 1)
            attack = (int) (lvl * (2 + Math.random() * 0.5));


    }

    public void setDefense() {
        if (type == 2) defense = (int) (lvl * (2 + Math.random() * 0.5));
        if (type == 3) defense = (int) (lvl * (1 + Math.random() * 0.25));
    }

    public int getAttack(Hero hero) {
        float att = attack;


        if (!hero.getClassName().equalsIgnoreCase("mage") && class_destination == Hero.CLASS_MAGE)
            att*= 0.6f;

        if (!hero.getClassName().equalsIgnoreCase("archey") && class_destination == Hero.CLASS_ARCHER)
            att*= 0.6f;

        if (!hero.getClassName().equalsIgnoreCase("warrior") && class_destination == Hero.CLASS_WARRIOR)
            att*= 0.6f;

        return (int) att;
    }

    public int getDefense(Hero hero) {
        return defense;
    }

    public void use(Hero hero) {
        if (type == 4) {
            if (name.equalsIgnoreCase("Minor Health Potion")) hero.addPV(25);
            if (name.equalsIgnoreCase("Minor Mana Potion")) hero.addMana(25);
            if (name.equalsIgnoreCase("Minor Attack Potion")) hero.addEffect(new Effect(20, 3, 0));
            if (name.equalsIgnoreCase("Minor Defense Potion")) hero.addEffect(new Effect(20, 0, 3));
            if (name.equalsIgnoreCase("Minor Experience Potion")) hero.addXp(2000);

            if (name.equalsIgnoreCase("Health Potion")) hero.addPV(60);
            if (name.equalsIgnoreCase("Mana Potion")) hero.addMana(60);
            if (name.equalsIgnoreCase("Attack Potion")) hero.addEffect(new Effect(20, 6, 0));
            if (name.equalsIgnoreCase("Defense Potion")) hero.addEffect(new Effect(20, 0, 6));
            if (name.equalsIgnoreCase("Experience Potion")) hero.addXp(6000);

            if (name.equalsIgnoreCase("Greater Health Potion")) hero.addPV(150);
            if (name.equalsIgnoreCase("Greater Mana Potion")) hero.addMana(150);
            if (name.equalsIgnoreCase("Greater Attack Potion")) hero.addEffect(new Effect(20, 10, 0));
            if (name.equalsIgnoreCase("Greater Defense Potion")) hero.addEffect(new Effect(20, 0, 10));
            if (name.equalsIgnoreCase("Greater Experience Potion")) hero.addXp(20000);

        }
    }

    public String getNameEffect(Hero hero) {
        String name = getName();
        String effect = "";
        if (getDefense(hero) != 0) effect += getDefense(hero) + " def";
        if (getDefense(hero) != 0 && getAttack(hero) != 0)
            effect += " ";
        if (getAttack(hero) != 0) effect += getAttack(hero) + " att";

        return name + " (+" + effect + ")";
    }

    public String getStringLog(Hero hero) {
        return "Name : " + getName() + "\n"
                + "Level : " + getLvl() + "\n"
                + "Attack : " + getAttack(hero) + "\n"
                + "Defense : " + getDefense(hero);
    }

    public int getPassivRegeneration() {
        if (name.equalsIgnoreCase("Amulet of Regeneration")) return 2;
        if (name.equalsIgnoreCase("Amulet of Spirit")) return 1;
        if (name.equalsIgnoreCase("Bracelet of Energy")) return 2;
        if (name.equalsIgnoreCase("Bracelet of Regeneration")) return 8;

        return 0;
    }

    public int getPassivVitality() {
        if (name.equalsIgnoreCase("Ring of Vitality")) return 10;
        if (name.equalsIgnoreCase("Ring of the Mage")) return 5;
        if (name.equalsIgnoreCase("Amulet of Spirit")) return 2;
        if (name.equalsIgnoreCase("Amulet of the Berserker")) return 5;
        if (name.equalsIgnoreCase("Amulet of the Titan")) return 15;
        if (name.equalsIgnoreCase("Amulet of Vitality")) return 40;
        if (name.equalsIgnoreCase("Bracelet of the Berserker")) return 7;
        if (name.equalsIgnoreCase("Bracelet of the Titan")) return 20;

        return 0;
    }


    public int getPassivMana() {

        if (name.equalsIgnoreCase("Ring of the Mage")) return 30;
        if (name.equalsIgnoreCase("Bracelet of Mana")) return 35;
        if (name.equalsIgnoreCase("Bracelet of Energy")) return 10;

        return 0;
    }

    public int getPassivProtection() {
        if (name.equalsIgnoreCase("Ring of Protection")) return 1;
        if (name.equalsIgnoreCase("Ring of the Warrior")) return 2;
        if (name.equalsIgnoreCase("Ring of Resilience")) return 3;
        if (name.equalsIgnoreCase("Ring of Fury")) return -4;
        if (name.equalsIgnoreCase("Amulet of Shielding")) return 3;
        if (name.equalsIgnoreCase("Amulet of Spirit")) return 1;
        if (name.equalsIgnoreCase("Amulet of the Berserker")) return 1;
        if (name.equalsIgnoreCase("Amulet of Magic Shield")) return 8;
        if (name.equalsIgnoreCase("Amulet of Resilience")) return 10;
        if (name.equalsIgnoreCase("Bracelet of Defense")) return 3;
        if (name.equalsIgnoreCase("Bracelet of Magic Defense")) return 4;
        if (name.equalsIgnoreCase("Bracelet of Protection")) return 6;
        if (name.equalsIgnoreCase("Bracelet of the Berserker")) return 2;


        return 0;
    }

    public int getPassivPower() {
        if (name.equalsIgnoreCase("Ring of Power")) return 1;
        if (name.equalsIgnoreCase("Ring of Strength")) return 2;
        if (name.equalsIgnoreCase("Ring of Fortitude")) return 2;
        if (name.equalsIgnoreCase("Ring of Fire")) return 3;
        if (name.equalsIgnoreCase("Ring of the Warrior")) return 2;
        if (name.equalsIgnoreCase("Ring of Shadows")) return 1;
        if (name.equalsIgnoreCase("Ring of Fury")) return 6;
        if (name.equalsIgnoreCase("Ring of the Phoenix")) return 4;
        if (name.equalsIgnoreCase("Ring of Mastery")) return 2;
        if (name.equalsIgnoreCase("Amulet of Fortitude")) return 1;
        if (name.equalsIgnoreCase("Amulet of Power")) return 1;
        if (name.equalsIgnoreCase("Amulet of the Berserker")) return 5;
        if (name.equalsIgnoreCase("Amulet of the Titan")) return 1;
        if (name.equalsIgnoreCase("Amulet of Shadows")) return 0;
        if (name.equalsIgnoreCase("Amulet of Mastery")) return 3;
        if (name.equalsIgnoreCase("Bracelet of Agility")) return 1;
        if (name.equalsIgnoreCase("Bracelet of Fortitude")) return 1;
        if (name.equalsIgnoreCase("Bracelet of the Berserker")) return 5;
        if (name.equalsIgnoreCase("Bracelet of Mastery")) return 3;
        if (name.equalsIgnoreCase("Bracelet of the Titan")) return 4;
        if (name.equalsIgnoreCase("Bracelet of Attack Boost")) return 2;

        return 0;
    }

    public int getPassivPrecision() {
        if (name.equalsIgnoreCase("Ring of Precision")) return 5;
        if (name.equalsIgnoreCase("Ring of the Mage")) return 5;
        if (name.equalsIgnoreCase("Ring of Mastery")) return 14;
        if (name.equalsIgnoreCase("Amulet of Spirit")) return 6;
        if (name.equalsIgnoreCase("Amulet of Mastery")) return 12;
        if (name.equalsIgnoreCase("Bracelet of Mastery")) return 11;

        return 0;
    }

    public int getPassivEndurance() {
        if (name.equalsIgnoreCase("Ring of Endurance")) return 10;
        if (name.equalsIgnoreCase("Ring of the Mage")) return 10;
        if (name.equalsIgnoreCase("Ring of Shadows")) return 16;
        if (name.equalsIgnoreCase("Ring of the Phoenix")) return 10;
        if (name.equalsIgnoreCase("Ring of Mastery")) return 28;
        if (name.equalsIgnoreCase("Amulet of the Berserker")) return -4;
        if (name.equalsIgnoreCase("Amulet of Shadows")) return 12;
        if (name.equalsIgnoreCase("Amulet of Mastery")) return 24;
        if (name.equalsIgnoreCase("Amulet of Endurance")) return 24;
        if (name.equalsIgnoreCase("Bracelet of Endurance")) return 16;
        if (name.equalsIgnoreCase("Bracelet of the Berserker")) return -6;
        if (name.equalsIgnoreCase("Bracelet of Mastery")) return 30;

        return 0;
    }

    public int getPassivLuck() {
        if (name.equalsIgnoreCase("Ring of Luck")) return 5;
        if (name.equalsIgnoreCase("Ring of the Mage")) return 5;
        if (name.equalsIgnoreCase("Ring of Shadows")) return 8;
        if (name.equalsIgnoreCase("Ring of Mastery")) return 7;
        if (name.equalsIgnoreCase("Amulet of Spirit")) return 6;
        if (name.equalsIgnoreCase("Amulet of Shadows")) return 6;
        if (name.equalsIgnoreCase("Amulet of Mastery")) return 10;
        if (name.equalsIgnoreCase("Bracelet of Mastery")) return 5;

        return 0;
    }

    public ArrayList<PassivEffect> getPassivEffect() {
        ArrayList<PassivEffect> l = new ArrayList<>();

        if (getPassivRegeneration() != 0) l.add(new PassivEffect("Regeneration", getPassivRegeneration()));
        if (getPassivVitality() != 0) l.add(new PassivEffect("Vitality", getPassivVitality()));
        if (getPassivMana() != 0) l.add(new PassivEffect("Mana", getPassivMana()));
        if (getPassivProtection() != 0) l.add(new PassivEffect("Protection", getPassivProtection()));
        if (getPassivPower() != 0) l.add(new PassivEffect("Power", getPassivPower()));
        if (getPassivPrecision() != 0) l.add(new PassivEffect("Precision", getPassivPrecision()));
        if (getPassivEndurance() != 0) l.add(new PassivEffect("Endurance", getPassivEndurance()));
        if (getPassivLuck() != 0) l.add(new PassivEffect("Luck", getPassivLuck()));

        return l;
    }

    public class PassivEffect {
        String name;
        int value;

        public PassivEffect(String name, int value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public int getValue() {
            return value;
        }
    }

    public char[] getAscii() {
        return ascii;
    }

    public byte[] getAscii_color() {
        return ascii_color;
    }
}
