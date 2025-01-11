package org.plefevre.Model;

import org.plefevre.View.Smiley;

import java.util.ArrayList;
import java.util.List;

public class ArtifactLibrary {
    private List<Artifact> artifacts = new ArrayList<>();

    public static ArtifactLibrary artifactLibrary = new ArtifactLibrary();

    public ArtifactLibrary() {
        populateLibrary();
    }

    private void populateLibrary() {
        // Armes de Guerrier
        artifacts.add(createWoodenSword());
        artifacts.add(createIronSword());
        artifacts.add(createSteelAxe());
        artifacts.add(createBronzeHammer());
        artifacts.add(createBattleAxe());
        artifacts.add(createKnightsSword());
        artifacts.add(createDragonSlayer());

        // Armes de Mage
        artifacts.add(createApprenticesStaff());
        artifacts.add(createOakWand());
        artifacts.add(createCrystalRod());
        artifacts.add(createSorcerersScepter());
        artifacts.add(createElementalStaff());
        artifacts.add(createEnchantedOrb());
        artifacts.add(createArchmagesStaff());

        // Armes d'Archer
        artifacts.add(createShortBow());
        artifacts.add(createHuntingBow());
        artifacts.add(createRecurveBow());
        artifacts.add(createLongBow());
        artifacts.add(createCrossbow());
        artifacts.add(createElvenBow());
        artifacts.add(createPhoenixBow());

        // Armures
        artifacts.add(createLeatherArmor());
        artifacts.add(createChainmail());
        artifacts.add(createSteelArmor());
        artifacts.add(createMageRobe());
        artifacts.add(createBattleArmor());
        artifacts.add(createDragonScaleArmor());
        artifacts.add(createElvenTunic());
        artifacts.add(createPhoenixArmor());
        artifacts.add(createClothTunic());
        artifacts.add(createGuardianArmor());
        artifacts.add(createShadowArmor());

        // Casques
        artifacts.add(createLeatherCap());
        artifacts.add(createClothHood());
        artifacts.add(createBronzeHelm());
        artifacts.add(createIronHelm());
        artifacts.add(createSteelHelm());
        artifacts.add(createWarriorHelm());
        artifacts.add(createMageHood());
        artifacts.add(createBattleHelm());
        artifacts.add(createElvenCrown());
        artifacts.add(createShadowHelm());
        artifacts.add(createDragonHelm());
        artifacts.add(createPhoenixHelm());
        artifacts.add(createShadowHood());

        // Potions
        artifacts.add(createHealthPotionLow());
        artifacts.add(createManaPotionLow());
        artifacts.add(createAttackPotionLow());
        artifacts.add(createDefensePotionLow());
        artifacts.add(createExperiencePotionLow());

        artifacts.add(createHealthPotionMid());
        artifacts.add(createManaPotionMid());
        artifacts.add(createAttackPotionMid());
        artifacts.add(createDefensePotionMid());
        artifacts.add(createExperiencePotionMid());

        artifacts.add(createHealthPotionHigh());
        artifacts.add(createManaPotionHigh());
        artifacts.add(createAttackPotionHigh());
        artifacts.add(createDefensePotionHigh());
        artifacts.add(createExperiencePotionHigh());

        //Rings
        artifacts.add(createRingOfStrength());
        artifacts.add(createRingOfProtection());
        artifacts.add(createRingOfVitality());
        artifacts.add(createRingOfPower());
        artifacts.add(createRingOfPrecision());
        artifacts.add(createRingOfEndurance());
        artifacts.add(createRingOfFortitude());
        artifacts.add(createRingOfLuck());
        artifacts.add(createRingOfFire());
        artifacts.add(createRingOfTheWarrior());
        artifacts.add(createRingOfTheMage());
        artifacts.add(createRingOfShadows());
        artifacts.add(createRingOfResilience());
        artifacts.add(createRingOfFury());
        artifacts.add(createRingOfThePhoenix());
        artifacts.add(createRingOfMastery());

        // Amulets
        artifacts.add(createAmuletOfPower());
        artifacts.add(createAmuletOfRegeneration());
        artifacts.add(createAmuletOfShielding());
        artifacts.add(createAmuletOfFortitude());
        artifacts.add(createAmuletOfSpirit());
        artifacts.add(createAmuletOfTheBerserker());
        artifacts.add(createAmuletOfTheTitan());
        artifacts.add(createAmuletOfVitality());
        artifacts.add(createAmuletOfMagicShield());
        artifacts.add(createAmuletOfResilience());
        artifacts.add(createAmuletOfShadows());
        artifacts.add(createAmuletOfMastery());
        artifacts.add(createAmuletOfEndurance());

        // Bracelets
        artifacts.add(createBraceletOfAgility());
        artifacts.add(createBraceletOfStrength());
        artifacts.add(createBraceletOfEndurance());
        artifacts.add(createBraceletOfFortitude());
        artifacts.add(createBraceletOfDefense());
        artifacts.add(createBraceletOfMagicDefense());
        artifacts.add(createBraceletOfEnergy());
        artifacts.add(createBraceletOfMana());
        artifacts.add(createBraceletOfProtection());
        artifacts.add(createBraceletOfTheBerserker());
        artifacts.add(createBraceletOfRegeneration());
        artifacts.add(createBraceletOfMastery());
        artifacts.add(createBraceletOfTheTitan());
        artifacts.add(createBraceletOfAttackBoost());
    }


    public List<Artifact> getAllArtifacts() {
        return artifacts;
    }

    public Artifact getArtifactByName(String name) {
        for (Artifact artifact : artifacts) {
            if (artifact.getName().equalsIgnoreCase(name)) {
                return artifact;
            }
        }
        return null;
    }

    public Artifact getArtifactByLevel(int lvl, int round) {

        ArrayList<Artifact> list = new ArrayList<>();
        for (Artifact artifact : artifacts) {
            if (artifact.getLvl() >= lvl - round && artifact.getLvl() <= lvl + round)
                list.add(artifact);
        }

        if (list.isEmpty())
            return null;

        return list.get((int) (list.size() * Math.random()));
    }


    private Artifact createWoodenSword() {
        Artifact woodenSword = new Artifact();
        woodenSword.setName("Wooden Sword");
        woodenSword.setType(Artifact.TYPE_WEAPON);
        woodenSword.setLvl(1);
        woodenSword.setAscii(("          " + "   ─╞══>  " + "          ").toCharArray());
        woodenSword.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 3, 3, 3, 3, 3, 3, 3, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        woodenSword.setClass_destination(Hero.CLASS_WARRIOR);
        return woodenSword;
    }

    private Artifact createIronSword() {
        Artifact ironSword = new Artifact();
        ironSword.setName("Iron Sword");
        ironSword.setType(Artifact.TYPE_WEAPON);
        ironSword.setLvl(3);
        ironSword.setAscii(("          " + "  O─╞═══>   " + "          ").toCharArray());
        ironSword.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 3, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        ironSword.setClass_destination(Hero.CLASS_WARRIOR);
        return ironSword;
    }

    private Artifact createSteelAxe() {
        Artifact steelAxe = new Artifact();
        steelAxe.setName("Steel Axe");
        steelAxe.setType(Artifact.TYPE_WEAPON);
        steelAxe.setLvl(5);
        steelAxe.setAscii(("   ┐      " + " O─╞════> " + "   ┘      ").toCharArray());
        steelAxe.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 3, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        steelAxe.setClass_destination(Hero.CLASS_WARRIOR);
        return steelAxe;
    }

    private Artifact createBronzeHammer() {
        Artifact bronzeHammer = new Artifact();
        bronzeHammer.setName("Bronze Hammer");
        bronzeHammer.setType(Artifact.TYPE_WEAPON);
        bronzeHammer.setLvl(8);
        bronzeHammer.setAscii(("     ▄▄▄  " + " O═══▒▒▒  " + "     ▀▀▀  ").toCharArray());
        bronzeHammer.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 3, 3, 3, 3, 3, 3, 3, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        bronzeHammer.setClass_destination(Hero.CLASS_WARRIOR);
        return bronzeHammer;
    }

    private Artifact createBattleAxe() {
        Artifact battleAxe = new Artifact();
        battleAxe.setName("Battle Axe");
        battleAxe.setType(Artifact.TYPE_WEAPON);
        battleAxe.setLvl(12);
        battleAxe.setAscii(("     ▄▄▄  " + " O════X>  " + "     ▀▀▀  ").toCharArray());
        battleAxe.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 3, 3, 3, 3, 1, 3, 3, 3,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        battleAxe.setClass_destination(Hero.CLASS_WARRIOR);
        return battleAxe;
    }

    private Artifact createKnightsSword() {
        Artifact knightsSword = new Artifact();
        knightsSword.setName("Knight's Sword");
        knightsSword.setType(Artifact.TYPE_WEAPON);
        knightsSword.setLvl(14);
        knightsSword.setAscii(("  ╖       " + "O─╢══════>" + "  ╜       ").toCharArray());
        knightsSword.setAscii_color(
                0, 0, 1, 0, 0, 0, 0, 0, 0, 0,
                0, 3, 1, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 1, 0, 0, 0, 0, 0, 0, 0);
        knightsSword.setClass_destination(Hero.CLASS_WARRIOR);
        return knightsSword;
    }

    private Artifact createDragonSlayer() {
        Artifact dragonSlayer = new Artifact();
        dragonSlayer.setName("Dragon Slayer");
        dragonSlayer.setType(Artifact.TYPE_WEAPON);
        dragonSlayer.setLvl(20);
        dragonSlayer.setAscii(("  ╥─>     " + "──╢" + Smiley.SMILEY_FIRE + "════> " + "  ╨─>     ").toCharArray());
        dragonSlayer.setAscii_color(
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
        dragonSlayer.setClass_destination(Hero.CLASS_WARRIOR);
        return dragonSlayer;
    }


    private Artifact createApprenticesStaff() {
        Artifact staff = new Artifact();
        staff.setName("Apprentice's Staff");
        staff.setType(Artifact.TYPE_WEAPON);
        staff.setLvl(1);
        staff.setAscii(("    o     " + "    |     " + "          ").toCharArray());
        staff.setAscii_color(
                0, 0, 0, 0, 3, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 3, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        staff.setClass_destination(Hero.CLASS_MAGE);
        return staff;
    }

    private Artifact createOakWand() {
        Artifact wand = new Artifact();
        wand.setName("Oak Wand");
        wand.setType(Artifact.TYPE_WEAPON);
        wand.setLvl(3);
        wand.setAscii(("    o     " + "    |     " + "    |     ").toCharArray());
        wand.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 3, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 3, 0, 0, 0, 0, 0);
        wand.setClass_destination(Hero.CLASS_MAGE);
        return wand;
    }

    private Artifact createCrystalRod() {
        Artifact rod = new Artifact();
        rod.setName("Crystal Rod");
        rod.setType(Artifact.TYPE_WEAPON);
        rod.setLvl(5);
        rod.setAscii(("   " + Smiley.SMILEY_CRYSTAL + "      " + "    |     " + "    |     ").toCharArray());
        rod.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 3, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 3, 0, 0, 0, 0, 0);
        rod.setClass_destination(Hero.CLASS_MAGE);
        return rod;
    }

    private Artifact createSorcerersScepter() {
        Artifact scepter = new Artifact();
        scepter.setName("Sorcerer's Scepter");
        scepter.setType(Artifact.TYPE_WEAPON);
        scepter.setLvl(8);
        scepter.setAscii(("    ■     " + "    ║     " + "    ║     ").toCharArray());
        scepter.setAscii_color(
                0, 0, 0, 0, 5, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 3, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 3, 0, 0, 0, 0, 0);
        scepter.setClass_destination(Hero.CLASS_MAGE);
        return scepter;
    }

    private Artifact createElementalStaff() {
        Artifact staff = new Artifact();
        staff.setName("Elemental Staff");
        staff.setType(Artifact.TYPE_WEAPON);
        staff.setLvl(12);
        staff.setAscii(("    ░     " + "    ║     " + "    ║     ").toCharArray());
        staff.setAscii_color(
                0, 0, 0, 0, 2, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 3, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 3, 0, 0, 0, 0, 0);
        staff.setClass_destination(Hero.CLASS_MAGE);
        return staff;
    }

    private Artifact createEnchantedOrb() {
        Artifact orb = new Artifact();
        orb.setName("Enchanted Orb");
        orb.setType(Artifact.TYPE_WEAPON);
        orb.setLvl(14);
        orb.setAscii(("    O     " + "    ║     " + "    ║     ").toCharArray());
        orb.setAscii_color(
                0, 0, 0, 0, 4, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 3, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 3, 0, 0, 0, 0, 0);
        orb.setClass_destination(Hero.CLASS_MAGE);
        return orb;
    }

    private Artifact createArchmagesStaff() {
        Artifact staff = new Artifact();
        staff.setName("Archmage's Staff");
        staff.setType(Artifact.TYPE_WEAPON);
        staff.setLvl(21);
        staff.setAscii(("    ▓     " + "    │     " + "    │     ").toCharArray());
        staff.setAscii_color(
                0, 0, 0, 0, 1, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 3, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 3, 0, 0, 0, 0, 0);
        staff.setClass_destination(Hero.CLASS_MAGE);
        return staff;
    }


    private Artifact createShortBow() {
        Artifact shortBow = new Artifact();
        shortBow.setName("Short Bow");
        shortBow.setType(Artifact.TYPE_WEAPON);
        shortBow.setLvl(1);
        shortBow.setAscii(("          " + "    ⌠)    " + "          ").toCharArray());
        shortBow.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 3, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        shortBow.setClass_destination(Hero.CLASS_ARCHER);
        return shortBow;
    }

    private Artifact createHuntingBow() {
        Artifact huntingBow = new Artifact();
        huntingBow.setName("Hunting Bow");
        huntingBow.setType(Artifact.TYPE_WEAPON);
        huntingBow.setLvl(3);
        huntingBow.setAscii(("          " + "    |\\    " + "    |/    ").toCharArray());
        huntingBow.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 3, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 3, 0, 0, 0, 0);
        huntingBow.setClass_destination(Hero.CLASS_ARCHER);
        return huntingBow;
    }

    private Artifact createRecurveBow() {
        Artifact recurveBow = new Artifact();
        recurveBow.setName("Recurve Bow");
        recurveBow.setType(Artifact.TYPE_WEAPON);
        recurveBow.setLvl(5);
        recurveBow.setAscii(("          " + "    ⌠\\    " + "    ⌡/    ").toCharArray());
        recurveBow.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 3, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 3, 0, 0, 0, 0);
        recurveBow.setClass_destination(Hero.CLASS_ARCHER);
        return recurveBow;
    }

    private Artifact createLongBow() {
        Artifact longBow = new Artifact();
        longBow.setName("Long Bow");
        longBow.setType(Artifact.TYPE_WEAPON);
        longBow.setLvl(8);
        longBow.setAscii(("    |\\     " + "   | |    " + "   |/     ").toCharArray());
        longBow.setAscii_color(
                0, 0, 0, 0, 0, 3, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 3, 3, 0, 0, 0,
                0, 0, 0, 0, 0, 3, 0, 0, 0, 0);
        longBow.setClass_destination(Hero.CLASS_ARCHER);
        return longBow;
    }

    private Artifact createCrossbow() {
        Artifact crossbow = new Artifact();
        crossbow.setName("Crossbow");
        crossbow.setType(Artifact.TYPE_WEAPON);
        crossbow.setLvl(12);
        crossbow.setAscii(("     |\\    " + "  ══╪═╪   " + "    |/    ").toCharArray());
        crossbow.setAscii_color(
                0, 0, 0, 0, 0, 3, 3, 0, 0, 0,
                0, 0, 3, 3, 3, 3, 3, 3, 0, 0,
                0, 0, 0, 0, 0, 3, 3, 0, 0, 0);
        crossbow.setClass_destination(Hero.CLASS_ARCHER);
        return crossbow;
    }

    private Artifact createElvenBow() {
        Artifact elvenBow = new Artifact();
        elvenBow.setName("Elven Bow");
        elvenBow.setType(Artifact.TYPE_WEAPON);
        elvenBow.setLvl(14);
        elvenBow.setAscii(("    ⌠\\     " + "   | |    " + "   ⌡/     ").toCharArray());
        elvenBow.setAscii_color(
                0, 0, 0, 0, 0, 3, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 3, 0, 0, 0,
                0, 0, 0, 0, 0, 3, 0, 0, 0, 0);
        elvenBow.setClass_destination(Hero.CLASS_ARCHER);
        return elvenBow;
    }

    private Artifact createPhoenixBow() {
        Artifact phoenixBow = new Artifact();
        phoenixBow.setName("Phoenix Bow");
        phoenixBow.setType(Artifact.TYPE_WEAPON);
        phoenixBow.setLvl(21);
        phoenixBow.setAscii(("   " + Smiley.SMILEY_FIRE + " \\     " + "  " + Smiley.SMILEY_FIRE + "  |    " + "  " + Smiley.SMILEY_FIRE + " /     ").toCharArray());
        phoenixBow.setAscii_color(
                0, 0, 0, 0, 0, 3, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 3, 0, 0, 0,
                0, 0, 0, 0, 0, 3, 0, 0, 0, 0);
        phoenixBow.setClass_destination(Hero.CLASS_ARCHER);
        return phoenixBow;
    }


    private Artifact createLeatherArmor() {
        Artifact leatherArmor = new Artifact();
        leatherArmor.setName("Leather Armor");
        leatherArmor.setType(Artifact.TYPE_ARMOR);
        leatherArmor.setLvl(2);
        leatherArmor.setAscii(("  ▄▄  ▄▄   " + "  ▐▓▓▌    " + "  ▐▓▓▌    ").toCharArray());
        leatherArmor.setAscii_color(
                3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
                3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
                3, 3, 3, 3, 3, 3, 3, 3, 3, 3);
        return leatherArmor;
    }

    private Artifact createChainmail() {
        Artifact chainmail = new Artifact();
        chainmail.setName("Chainmail");
        chainmail.setType(Artifact.TYPE_ARMOR);
        chainmail.setLvl(5);
        chainmail.setAscii(("  ▄▄  ▄▄   " + "  ▐░░▌    " + "  ▐░░▌    ").toCharArray());
        chainmail.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 7, 7, 0, 0, 0, 0,
                0, 0, 0, 0, 7, 7, 0, 0, 0, 0);
        return chainmail;
    }

    private Artifact createSteelArmor() {
        Artifact steelArmor = new Artifact();
        steelArmor.setName("Steel Armor");
        steelArmor.setType(Artifact.TYPE_ARMOR);
        steelArmor.setLvl(8);
        steelArmor.setAscii(("  ▄▄  ▄▄   " + "  ▐██▌    " + "  ▐██▌    ").toCharArray());
        steelArmor.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        return steelArmor;
    }

    private Artifact createMageRobe() {
        Artifact mageRobe = new Artifact();
        mageRobe.setName("Mage's Robe");
        mageRobe.setType(Artifact.TYPE_ARMOR);
        mageRobe.setLvl(6);
        mageRobe.setAscii(("   ▄  ▄    " + " ▐████▌   " + " ██████   ").toCharArray());
        mageRobe.setAscii_color(
                5, 5, 5, 5, 5, 5, 5, 5, 5, 5,
                5, 5, 5, 5, 5, 5, 5, 5, 5, 5,
                5, 5, 5, 5, 5, 5, 5, 5, 5, 5);
        return mageRobe;
    }

    private Artifact createBattleArmor() {
        Artifact battleArmor = new Artifact();
        battleArmor.setName("Battle Armor");
        battleArmor.setType(Artifact.TYPE_ARMOR);
        battleArmor.setLvl(10);
        battleArmor.setAscii(("  ▄▄  ▄▄   " + "  ▐██▌    " + "  ▄██▄    ").toCharArray());
        battleArmor.setAscii_color(
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                0, 0, 0, 1, 0, 0, 1, 0, 0, 0,
                0, 0, 0, 1, 0, 0, 1, 0, 0, 0);
        return battleArmor;
    }

    private Artifact createDragonScaleArmor() {
        Artifact dragonScaleArmor = new Artifact();
        dragonScaleArmor.setName("Dragon Scale Armor");
        dragonScaleArmor.setType(Artifact.TYPE_ARMOR);
        dragonScaleArmor.setLvl(18);
        dragonScaleArmor.setAscii(("  ▀█▄▄█▀   " + "  ▐" + Smiley.SMILEY_FIRE + " ▌    " + "   ▀▀     ").toCharArray());
        dragonScaleArmor.setAscii_color(
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
        return dragonScaleArmor;
    }

    private Artifact createElvenTunic() {
        Artifact elvenTunic = new Artifact();
        elvenTunic.setName("Elven Tunic");
        elvenTunic.setType(Artifact.TYPE_ARMOR);
        elvenTunic.setLvl(12);
        elvenTunic.setAscii(("   ╕  ╒    " + "  │░░│    " + "  │░░│    ").toCharArray());
        elvenTunic.setAscii_color(
                2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                2, 2, 2, 2, 2, 2, 2, 2, 2, 2);
        return elvenTunic;
    }

    private Artifact createPhoenixArmor() {
        Artifact phoenixArmor = new Artifact();
        phoenixArmor.setName("Phoenix Armor");
        phoenixArmor.setType(Artifact.TYPE_ARMOR);
        phoenixArmor.setLvl(20);
        phoenixArmor.setAscii(("  ▀█▄▄█▀   " + "  ▐" + Smiley.SMILEY_FIRE + " ▌    " + "  ▐▀▀▌    ").toCharArray());
        phoenixArmor.setAscii_color(
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
        return phoenixArmor;
    }


    private Artifact createClothTunic() {
        Artifact clothTunic = new Artifact();
        clothTunic.setName("Cloth Tunic");
        clothTunic.setType(Artifact.TYPE_ARMOR);
        clothTunic.setLvl(1);
        clothTunic.setAscii(("   \\  /   " + "   |  |   " + "   |..|   ").toCharArray());
        clothTunic.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        return clothTunic;
    }

    private Artifact createGuardianArmor() {
        Artifact guardianArmor = new Artifact();
        guardianArmor.setName("Guardian Armor");
        guardianArmor.setType(Artifact.TYPE_ARMOR);
        guardianArmor.setLvl(14);
        guardianArmor.setAscii(("   \\  /   " + "   |XX|   " + "   |XX|   ").toCharArray());
        guardianArmor.setAscii_color(
                0, 0, 0, 1, 0, 0, 1, 0, 0, 0,
                0, 0, 0, 1, 0, 0, 1, 0, 0, 0,
                0, 0, 0, 1, 0, 0, 1, 0, 0, 0);
        return guardianArmor;
    }

    private Artifact createShadowArmor() {
        Artifact shadowArmor = new Artifact();
        shadowArmor.setName("Shadow Armor");
        shadowArmor.setType(Artifact.TYPE_ARMOR);
        shadowArmor.setLvl(16);
        shadowArmor.setAscii(("  \\\\  //  " + "   |¯¯|   " + "   └──┘   ").toCharArray());
        shadowArmor.setAscii_color(
                0, 0, 1, 1, 0, 0, 1, 1, 0, 0,
                0, 0, 0, 1, 0, 0, 1, 0, 0, 0,
                0, 0, 0, 1, 0, 0, 1, 0, 0, 0);
        return shadowArmor;
    }

    private Artifact createLeatherCap() {
        Artifact leatherCap = new Artifact();
        leatherCap.setName("Leather Cap");
        leatherCap.setType(Artifact.TYPE_HELM);
        leatherCap.setLvl(1);
        leatherCap.setAscii(("   ▄  ▄    " + " ▐░░░░▌   " + " █░░░░█   ").toCharArray());
        leatherCap.setAscii_color(
                3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
                3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
                3, 3, 3, 3, 3, 3, 3, 3, 3, 3);
        return leatherCap;
    }

    private Artifact createClothHood() {
        Artifact clothHood = new Artifact();
        clothHood.setName("Cloth Hood");
        clothHood.setType(Artifact.TYPE_HELM);
        clothHood.setLvl(2);
        clothHood.setAscii(("   \\  /   " + "   |xx|   " + "   |xx|   ").toCharArray());
        clothHood.setAscii_color(
                0, 0, 0, 2, 0, 0, 2, 0, 0, 0,
                0, 0, 0, 2, 3, 3, 2, 0, 0, 0,
                0, 0, 0, 2, 3, 3, 2, 0, 0, 0);
        return clothHood;
    }

    private Artifact createBronzeHelm() {
        Artifact bronzeHelm = new Artifact();
        bronzeHelm.setName("Bronze Helm");
        bronzeHelm.setType(Artifact.TYPE_HELM);
        bronzeHelm.setLvl(3);
        bronzeHelm.setAscii(("  ▄▄▄▄▄   " + "  █▄▀▄█   " + "  ▀▀ ▀▀   ").toCharArray());
        bronzeHelm.setAscii_color(
                3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
                3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
                3, 3, 3, 3, 3, 3, 3, 3, 3, 3);
        return bronzeHelm;
    }

    private Artifact createIronHelm() {
        Artifact ironHelm = new Artifact();
        ironHelm.setName("Iron Helm");
        ironHelm.setType(Artifact.TYPE_HELM);
        ironHelm.setLvl(5);
        ironHelm.setAscii(("  ▄▄▒▄▄   " + "  █▄▀▄█   " + "  ▀▀ ▀▀   ").toCharArray());
        ironHelm.setAscii_color(
                0, 0, 0, 0, 6, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        return ironHelm;
    }


    private Artifact createSteelHelm() {
        Artifact steelHelm = new Artifact();
        steelHelm.setName("Steel Helm");
        steelHelm.setType(Artifact.TYPE_HELM);
        steelHelm.setLvl(8);
        steelHelm.setAscii(("  ▄▄█▄▄   " + "  █▄▀▄█   " + "  ▀▀ ▀▀   ").toCharArray());
        steelHelm.setAscii_color(
                0, 0, 0, 0, 1, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        return steelHelm;
    }

    private Artifact createWarriorHelm() {
        Artifact warriorHelm = new Artifact();
        warriorHelm.setName("Warrior's Helm");
        warriorHelm.setType(Artifact.TYPE_HELM);
        warriorHelm.setLvl(9);
        warriorHelm.setAscii(("  ▄▄▄▄▄   " + "  █▄▀▄█   " + "  █▀▀▀█   ").toCharArray());
        warriorHelm.setAscii_color(
                0, 0, 1, 0, 1, 0, 1, 0, 0, 0,
                0, 0, 1, 0, 1, 0, 1, 0, 0, 0,
                0, 0, 1, 0, 1, 0, 1, 0, 0, 0);
        return warriorHelm;
    }

    private Artifact createMageHood() {
        Artifact mageHood = new Artifact();
        mageHood.setName("Mage's Hood");
        mageHood.setType(Artifact.TYPE_HELM);
        mageHood.setLvl(7);
        mageHood.setAscii(("    /\\    " + "   /  \\   " + "  /____\\  ").toCharArray());
        mageHood.setAscii_color(
                5, 5, 5, 5, 5, 5, 5, 5, 5, 5,
                5, 5, 5, 5, 5, 5, 5, 5, 5, 5,
                5, 5, 5, 5, 5, 5, 5, 5, 5, 5);
        return mageHood;
    }

    private Artifact createBattleHelm() {
        Artifact battleHelm = new Artifact();
        battleHelm.setName("Battle Helm");
        battleHelm.setType(Artifact.TYPE_HELM);
        battleHelm.setLvl(10);
        battleHelm.setAscii(("  ▄▄▄▄▄   " + "  █▄▀▄█   " + "  ▀|||▀   ").toCharArray());
        battleHelm.setAscii_color(
                0, 0, 1, 1, 1, 1, 1, 0, 0, 0,
                0, 0, 1, 0, 1, 0, 1, 0, 0, 0,
                0, 0, 0, 1, 1, 1, 0, 0, 0, 0);
        return battleHelm;
    }


    private Artifact createElvenCrown() {
        Artifact elvenCrown = new Artifact();
        elvenCrown.setName("Elven Crown");
        elvenCrown.setType(Artifact.TYPE_HELM);
        elvenCrown.setLvl(12);
        elvenCrown.setAscii((" o o o o  " + " █▄█▄█▄█  " + "          ").toCharArray());
        elvenCrown.setAscii_color(
                6, 6, 6, 6, 6, 6, 6, 6, 6, 6,
                2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                2, 2, 2, 2, 2, 2, 2, 2, 2, 2);
        return elvenCrown;
    }

    private Artifact createShadowHelm() {
        Artifact shadowHelm = new Artifact();
        shadowHelm.setName("Shadow Helm");
        shadowHelm.setType(Artifact.TYPE_HELM);
        shadowHelm.setLvl(16);
        shadowHelm.setAscii(("   ____   " + "  /____\\  " + "          ").toCharArray());
        shadowHelm.setAscii_color(
                3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
                3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
                3, 3, 3, 3, 3, 3, 3, 3, 3, 3);
        return shadowHelm;
    }

    private Artifact createDragonHelm() {
        Artifact dragonHelm = new Artifact();
        dragonHelm.setName("Dragon Helm");
        dragonHelm.setType(Artifact.TYPE_HELM);
        dragonHelm.setLvl(18);
        dragonHelm.setAscii(("  " + Smiley.SMILEY_FIRE + "▄▄" + Smiley.SMILEY_FIRE + "    " + "  █▀█▀█   " + "  ██ ██   ").toCharArray());
        dragonHelm.setAscii_color(
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
        return dragonHelm;
    }

    private Artifact createPhoenixHelm() {
        Artifact phoenixHelm = new Artifact();
        phoenixHelm.setName("Phoenix Helm");
        phoenixHelm.setType(Artifact.TYPE_HELM);
        phoenixHelm.setLvl(20);
        phoenixHelm.setAscii(("  " + Smiley.SMILEY_FIRE + "▄▄" + Smiley.SMILEY_FIRE + "    " + "  █▀█▀█   " + "  █▀ ▀█   ").toCharArray());
        phoenixHelm.setAscii_color(
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
        return phoenixHelm;
    }

    private Artifact createShadowHood() {
        Artifact shadowHood = new Artifact();
        shadowHood.setName("Shadow Hood");
        shadowHood.setType(Artifact.TYPE_HELM);
        shadowHood.setLvl(14);
        shadowHood.setAscii(("   ____   " + "  /    \\  " + " /______\\ ").toCharArray());
        shadowHood.setAscii_color(
                5, 5, 5, 5, 5, 5, 5, 5, 5, 5,
                5, 5, 5, 5, 5, 5, 5, 5, 5, 5,
                5, 5, 5, 5, 5, 5, 5, 5, 5, 5);
        return shadowHood;
    }

    private Artifact createHealthPotionLow() {
        Artifact potion = new Artifact();
        potion.setName("Minor Health Potion");
        potion.setType(Artifact.TYPE_POTION);
        potion.setLvl(1);
        potion.setAscii(("    ││    " + "    ▐▌    " + "    ▐▌    ").toCharArray());
        potion.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 1, 1, 0, 0, 0, 0,
                0, 0, 0, 0, 1, 1, 0, 0, 0, 0);
        return potion;
    }

    private Artifact createManaPotionLow() {
        Artifact potion = new Artifact();
        potion.setName("Minor Mana Potion");
        potion.setType(Artifact.TYPE_POTION);
        potion.setLvl(1);
        potion.setAscii(("    ││    " + "    ▐▌    " + "    ▐▌    ").toCharArray());
        potion.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 4, 4, 0, 0, 0, 0,
                0, 0, 0, 0, 4, 4, 0, 0, 0, 0);
        return potion;
    }

    private Artifact createAttackPotionLow() {
        Artifact potion = new Artifact();
        potion.setName("Minor Attack Potion");
        potion.setType(Artifact.TYPE_POTION);
        potion.setLvl(2);
        potion.setAscii(("    ││    " + "    ▐▌    " + "    ▐▌    ").toCharArray());
        potion.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 5, 5, 0, 0, 0, 0,
                0, 0, 0, 0, 5, 5, 0, 0, 0, 0);
        return potion;
    }

    private Artifact createDefensePotionLow() {
        Artifact potion = new Artifact();
        potion.setName("Minor Defense Potion");
        potion.setType(Artifact.TYPE_POTION);
        potion.setLvl(3);
        potion.setAscii(("    ││    " + "    ▐▌    " + "    ▐▌    ").toCharArray());
        potion.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 3, 3, 0, 0, 0, 0,
                0, 0, 0, 0, 3, 3, 0, 0, 0, 0);
        return potion;
    }

    private Artifact createExperiencePotionLow() {
        Artifact potion = new Artifact();
        potion.setName("Minor Experience Potion");
        potion.setType(Artifact.TYPE_POTION);
        potion.setLvl(4);
        potion.setAscii(("    ││    " + "    ▐▌    " + "    ▐▌    ").toCharArray());
        potion.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 2, 2, 0, 0, 0, 0,
                0, 0, 0, 0, 2, 2, 0, 0, 0, 0);
        return potion;
    }

    // Potions de niveau intermédiaire
    private Artifact createHealthPotionMid() {
        Artifact potion = new Artifact();
        potion.setName("Health Potion");
        potion.setType(Artifact.TYPE_POTION);
        potion.setLvl(8);
        potion.setAscii(("    ||    " + "   /██\\    " + "  \\██/    ").toCharArray());
        potion.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 1, 1, 0, 0, 0, 0,
                0, 0, 0, 0, 1, 1, 0, 0, 0, 0);
        return potion;
    }

    private Artifact createManaPotionMid() {
        Artifact potion = new Artifact();
        potion.setName("Mana Potion");
        potion.setType(Artifact.TYPE_POTION);
        potion.setLvl(8);
        potion.setAscii(("    ||    " + "   /██\\    " + "  \\██/    ").toCharArray());
        potion.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 4, 4, 0, 0, 0, 0,
                0, 0, 0, 0, 4, 4, 0, 0, 0, 0);
        return potion;
    }

    private Artifact createAttackPotionMid() {
        Artifact potion = new Artifact();
        potion.setName("Attack Potion");
        potion.setType(Artifact.TYPE_POTION);
        potion.setLvl(9);
        potion.setAscii(("    ||    " + "   /██\\    " + "  \\██/    ").toCharArray());
        potion.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 5, 5, 0, 0, 0, 0,
                0, 0, 0, 0, 5, 5, 0, 0, 0, 0);
        return potion;
    }

    private Artifact createDefensePotionMid() {
        Artifact potion = new Artifact();
        potion.setName("Defense Potion");
        potion.setType(Artifact.TYPE_POTION);
        potion.setLvl(10);
        potion.setAscii(("    ||    " + "   /██\\    " + "  \\██/    ").toCharArray());
        potion.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 3, 3, 0, 0, 0, 0,
                0, 0, 0, 0, 3, 3, 0, 0, 0, 0);
        return potion;
    }

    private Artifact createExperiencePotionMid() {
        Artifact potion = new Artifact();
        potion.setName("Experience Potion");
        potion.setType(Artifact.TYPE_POTION);
        potion.setLvl(10);
        potion.setAscii(("    ||    " + "   /██\\    " + "  \\██/    ").toCharArray());
        potion.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 2, 2, 0, 0, 0, 0,
                0, 0, 0, 0, 2, 2, 0, 0, 0, 0);
        return potion;
    }

    // Potions de haut niveau
    private Artifact createHealthPotionHigh() {
        Artifact potion = new Artifact();
        potion.setName("Greater Health Potion");
        potion.setType(Artifact.TYPE_POTION);
        potion.setLvl(16);
        potion.setAscii(("    ||    " + "   /██\\    " + " /████\\   ").toCharArray());
        potion.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 1, 1, 0, 0, 0, 0,
                0, 0, 0, 1, 1, 1, 1, 0, 0, 0);
        return potion;
    }

    private Artifact createManaPotionHigh() {
        Artifact potion = new Artifact();
        potion.setName("Greater Mana Potion");
        potion.setType(Artifact.TYPE_POTION);
        potion.setLvl(16);
        potion.setAscii(("    ||    " + "   /██\\    " + " /████\\   ").toCharArray());
        potion.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 4, 4, 0, 0, 0, 0,
                0, 0, 0, 4, 4, 4, 4, 0, 0, 0);
        return potion;
    }

    private Artifact createAttackPotionHigh() {
        Artifact potion = new Artifact();
        potion.setName("Greater Attack Potion");
        potion.setType(Artifact.TYPE_POTION);
        potion.setLvl(17);
        potion.setAscii(("    ||    " + "   /██\\    " + " /████\\   ").toCharArray());
        potion.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 5, 5, 0, 0, 0, 0,
                0, 0, 0, 5, 5, 5, 5, 0, 0, 0);
        return potion;
    }

    private Artifact createDefensePotionHigh() {
        Artifact potion = new Artifact();
        potion.setName("Greater Defense Potion");
        potion.setType(Artifact.TYPE_POTION);
        potion.setLvl(18);
        potion.setAscii(("    ||    " + "   /██\\    " + " /████\\   ").toCharArray());
        potion.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 3, 3, 0, 0, 0, 0,
                0, 0, 0, 3, 3, 3, 3, 0, 0, 0);
        return potion;
    }

    private Artifact createExperiencePotionHigh() {
        Artifact potion = new Artifact();
        potion.setName("Greater Experience Potion");
        potion.setType(Artifact.TYPE_POTION);
        potion.setLvl(15);
        potion.setAscii(("    ||    " + "   /██\\    " + " /████\\   ").toCharArray());
        potion.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 2, 2, 0, 0, 0, 0,
                0, 0, 0, 2, 2, 2, 2, 0, 0, 0);
        return potion;
    }

    private Artifact createRingOfStrength() {
        Artifact ring = new Artifact();
        ring.setName("Ring of Strength");
        ring.setType(Artifact.TYPE_ACCESSORY);
        ring.setLvl(3);
        ring.setAscii(("          " + "  =<O>=      " + "          ").toCharArray());
        ring.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 1, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        return ring;
    }

    private Artifact createRingOfProtection() {
        Artifact ring = new Artifact();
        ring.setName("Ring of Protection");
        ring.setType(Artifact.TYPE_ACCESSORY);
        ring.setLvl(5);
        ring.setAscii(("          " + "  =<O>=      " + "          ").toCharArray());
        ring.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 2, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        return ring;
    }

    private Artifact createRingOfVitality() {
        Artifact ring = new Artifact();
        ring.setName("Ring of Vitality");
        ring.setType(Artifact.TYPE_ACCESSORY);
        ring.setLvl(10);
        ring.setAscii(("          " + "  =<●>=      " + "          ").toCharArray());
        ring.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 1, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        return ring;
    }

    private Artifact createRingOfPower() {
        Artifact ring = new Artifact();
        ring.setName("Ring of Power");
        ring.setType(Artifact.TYPE_ACCESSORY);
        ring.setLvl(6);
        ring.setAscii(("          " + "  =<O>=      " + "          ").toCharArray());
        ring.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 3, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        return ring;
    }

    private Artifact createRingOfPrecision() {
        Artifact ring = new Artifact();
        ring.setName("Ring of Precision");
        ring.setType(Artifact.TYPE_ACCESSORY);
        ring.setLvl(8);
        ring.setAscii(("          " + "  =<●>=      " + "          ").toCharArray());
        ring.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 5, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        return ring;
    }

    private Artifact createRingOfEndurance() {
        Artifact ring = new Artifact();
        ring.setName("Ring of Endurance");
        ring.setType(Artifact.TYPE_ACCESSORY);
        ring.setLvl(9);
        ring.setAscii(("          " + "  =<●>=      " + "          ").toCharArray());
        ring.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 2, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        return ring;
    }

    private Artifact createRingOfFortitude() {
        Artifact ring = new Artifact();
        ring.setName("Ring of Fortitude");
        ring.setType(Artifact.TYPE_ACCESSORY);
        ring.setLvl(11);
        ring.setAscii(("          " + "  =<●>=      " + "          ").toCharArray());
        ring.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 3, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        return ring;
    }

    private Artifact createRingOfLuck() {
        Artifact ring = new Artifact();
        ring.setName("Ring of Luck");
        ring.setType(Artifact.TYPE_ACCESSORY);
        ring.setLvl(4);
        ring.setAscii(("          " + "  =<♣>=      " + "          ").toCharArray());
        ring.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 2, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        return ring;
    }

    private Artifact createRingOfFire() {
        Artifact ring = new Artifact();
        ring.setName("Ring of Fire");
        ring.setType(Artifact.TYPE_ACCESSORY);
        ring.setLvl(13);
        ring.setAscii(("          " + "  =<●>=      " + "          ").toCharArray());
        ring.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 1, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        return ring;
    }

    private Artifact createRingOfTheWarrior() {
        Artifact ring = new Artifact();
        ring.setName("Ring of the Warrior");
        ring.setType(Artifact.TYPE_ACCESSORY);
        ring.setLvl(14);
        ring.setAscii(("          " + "  =<●>=      " + "          ").toCharArray());
        ring.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 5, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        return ring;
    }

    private Artifact createRingOfTheMage() {
        Artifact ring = new Artifact();
        ring.setName("Ring of the Mage");
        ring.setType(Artifact.TYPE_ACCESSORY);
        ring.setLvl(14);
        ring.setAscii(("          " + "  =<●>=      " + "          ").toCharArray());
        ring.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 5, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        return ring;
    }

    private Artifact createRingOfShadows() {
        Artifact ring = new Artifact();
        ring.setName("Ring of Shadows");
        ring.setType(Artifact.TYPE_ACCESSORY);
        ring.setLvl(15);
        ring.setAscii(("          " + "  =<●>=      " + "          ").toCharArray());
        ring.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 3, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        return ring;
    }

    private Artifact createRingOfResilience() {
        Artifact ring = new Artifact();
        ring.setName("Ring of Resilience");
        ring.setType(Artifact.TYPE_ACCESSORY);
        ring.setLvl(16);
        ring.setAscii(("          " + "  =[●]=      " + "          ").toCharArray());
        ring.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 3, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        return ring;
    }

    private Artifact createRingOfFury() {
        Artifact ring = new Artifact();
        ring.setName("Ring of Fury");
        ring.setType(Artifact.TYPE_ACCESSORY);
        ring.setLvl(17);
        ring.setAscii(("          " + "  =[●]=      " + "          ").toCharArray());
        ring.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 1, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        return ring;
    }

    private Artifact createRingOfThePhoenix() {
        Artifact ring = new Artifact();
        ring.setName("Ring of the Phoenix");
        ring.setType(Artifact.TYPE_ACCESSORY);
        ring.setLvl(18);
        ring.setAscii(("          " + "  =[●]=      " + "          ").toCharArray());
        ring.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 1, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        return ring;
    }

    private Artifact createRingOfMastery() {
        Artifact ring = new Artifact();
        ring.setName("Ring of Mastery");
        ring.setType(Artifact.TYPE_ACCESSORY);
        ring.setLvl(20);
        ring.setAscii(("          " + "  =[●]=      " + "          ").toCharArray());
        ring.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 4, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        return ring;
    }

    //Amulet
    private Artifact createAmuletOfPower() {
        Artifact amulet = new Artifact();
        amulet.setName("Amulet of Power");
        amulet.setType(Artifact.TYPE_ACCESSORY);
        amulet.setLvl(5);
        amulet.setAscii((" ┌──────┐ " + " │ oOOo │ " + " └──────┘ ").toCharArray());
        amulet.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 1, 1, 1, 1, 1, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        return amulet;
    }

    private Artifact createAmuletOfRegeneration() {
        Artifact amulet = new Artifact();
        amulet.setName("Amulet of Regeneration");
        amulet.setType(Artifact.TYPE_ACCESSORY);
        amulet.setLvl(9);
        amulet.setAscii((" ┌──────┐ " + " │ +♥♥+ │ " + " └──────┘ ").toCharArray());
        amulet.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 2, 1, 1, 2, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        return amulet;
    }

    private Artifact createAmuletOfShielding() {
        Artifact amulet = new Artifact();
        amulet.setName("Amulet of Shielding");
        amulet.setType(Artifact.TYPE_ACCESSORY);
        amulet.setLvl(12);
        amulet.setAscii((" ┌──────┐ " + " │ ████ │ " + " └──────┘ ").toCharArray());
        amulet.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 6, 6, 6, 6, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        return amulet;
    }

    private Artifact createAmuletOfFortitude() {
        Artifact amulet = new Artifact();
        amulet.setName("Amulet of Fortitude");
        amulet.setType(Artifact.TYPE_ACCESSORY);
        amulet.setLvl(6);
        amulet.setAscii((" ┌──────┐ " + " │  ██  │ " + " └──────┘ ").toCharArray());
        amulet.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 1, 1, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        return amulet;
    }


    private Artifact createAmuletOfSpirit() {
        Artifact amulet = new Artifact();
        amulet.setName("Amulet of Spirit");
        amulet.setType(Artifact.TYPE_ACCESSORY);
        amulet.setLvl(15);
        amulet.setAscii((" ┌──────┐ " + " │ <==> │ " + " └──────┘ ").toCharArray());
        amulet.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 3, 2, 2, 3, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        return amulet;
    }

    private Artifact createAmuletOfTheBerserker() {
        Artifact amulet = new Artifact();
        amulet.setName("Amulet of the Berserker");
        amulet.setType(Artifact.TYPE_ACCESSORY);
        amulet.setLvl(16);
        amulet.setAscii((" ┌──────┐ " + " │ <██> │ " + " └──────┘ ").toCharArray());
        amulet.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 5, 1, 1, 5, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        return amulet;
    }

    private Artifact createAmuletOfTheTitan() {
        Artifact amulet = new Artifact();
        amulet.setName("Amulet of the Titan");
        amulet.setType(Artifact.TYPE_ACCESSORY);
        amulet.setLvl(17);
        amulet.setAscii((" ┌──────┐ " + " │ >██< │ " + " └──────┘ ").toCharArray());
        amulet.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 1, 5, 5, 1, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        return amulet;
    }

    private Artifact createAmuletOfVitality() {
        Artifact amulet = new Artifact();
        amulet.setName("Amulet of Vitality");
        amulet.setType(Artifact.TYPE_ACCESSORY);
        amulet.setLvl(18);
        amulet.setAscii((" ┌──────┐ " + " │ ♡♥♥♡ │ " + " └──────┘ ").toCharArray());
        amulet.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 1, 1, 1, 1, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        return amulet;
    }

    private Artifact createAmuletOfMagicShield() {
        Artifact amulet = new Artifact();
        amulet.setName("Amulet of Magic Shield");
        amulet.setType(Artifact.TYPE_ACCESSORY);
        amulet.setLvl(20);
        amulet.setAscii((" ┌──────┐ " + " │ [][] │ " + " └──────┘ ").toCharArray());
        amulet.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 4, 4, 4, 4, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        return amulet;
    }

    private Artifact createAmuletOfResilience() {
        Artifact amulet = new Artifact();
        amulet.setName("Amulet of Resilience");
        amulet.setType(Artifact.TYPE_ACCESSORY);
        amulet.setLvl(19);
        amulet.setAscii((" ┌──────┐ " + " │ [██] │ " + " └──────┘ ").toCharArray());
        amulet.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 3, 2, 2, 3, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        return amulet;
    }

    private Artifact createAmuletOfShadows() {
        Artifact amulet = new Artifact();
        amulet.setName("Amulet of Shadows");
        amulet.setType(Artifact.TYPE_ACCESSORY);
        amulet.setLvl(14);
        amulet.setAscii((" ┌──────┐ " + " │ ░░░░ │ " + " └──────┘ ").toCharArray());
        amulet.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 5, 5, 5, 5, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        return amulet;
    }


    private Artifact createAmuletOfMastery() {
        Artifact amulet = new Artifact();
        amulet.setName("Amulet of Mastery");
        amulet.setType(Artifact.TYPE_ACCESSORY);
        amulet.setLvl(20);
        amulet.setAscii((" ┌──────┐ " + " │ /\\/\\ │ " + " └──────┘ ").toCharArray());
        amulet.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 6, 6, 6, 6, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        return amulet;
    }

    private Artifact createAmuletOfEndurance() {
        Artifact amulet = new Artifact();
        amulet.setName("Amulet of Endurance");
        amulet.setType(Artifact.TYPE_ACCESSORY);
        amulet.setLvl(8);
        amulet.setAscii((" ┌──────┐ " + " │ =<>= │ " + " └──────┘ ").toCharArray());
        amulet.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 4, 4, 4, 4, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        return amulet;
    }


    //Bracelet
    private Artifact createBraceletOfAgility() {
        Artifact bracelet = new Artifact();
        bracelet.setName("Bracelet of Agility");
        bracelet.setType(Artifact.TYPE_ACCESSORY);
        bracelet.setLvl(4);
        bracelet.setAscii(("   /--\\   " + "==< <> >==" + "   \\--/   ").toCharArray());
        bracelet.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 4, 4, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        return bracelet;
    }

    private Artifact createBraceletOfStrength() {
        Artifact bracelet = new Artifact();
        bracelet.setName("Bracelet of Strength");
        bracelet.setType(Artifact.TYPE_ACCESSORY);
        bracelet.setLvl(6);
        bracelet.setAscii(("   /--\\   " + "==< ██ >==" + "   \\--/   ").toCharArray());
        bracelet.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 1, 1, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        return bracelet;
    }

    private Artifact createBraceletOfEndurance() {
        Artifact bracelet = new Artifact();
        bracelet.setName("Bracelet of Endurance");
        bracelet.setType(Artifact.TYPE_ACCESSORY);
        bracelet.setLvl(8);
        bracelet.setAscii(("   /--\\   " + "==< ▀▄ >==" + "   \\--/   ").toCharArray());
        bracelet.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 6, 6, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        return bracelet;
    }

    private Artifact createBraceletOfFortitude() {
        Artifact bracelet = new Artifact();
        bracelet.setName("Bracelet of Fortitude");
        bracelet.setType(Artifact.TYPE_ACCESSORY);
        bracelet.setLvl(5);
        bracelet.setAscii(("   /--\\   " + "==< ▐▌ >==" + "   \\--/   ").toCharArray());
        bracelet.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 2, 2, 2, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        return bracelet;
    }

    private Artifact createBraceletOfDefense() {
        Artifact bracelet = new Artifact();
        bracelet.setName("Bracelet of Defense");
        bracelet.setType(Artifact.TYPE_ACCESSORY);
        bracelet.setLvl(9);
        bracelet.setAscii(("   /--\\   " + "==< ][ >==" + "   \\--/   ").toCharArray());
        bracelet.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 4, 4, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        return bracelet;
    }

    private Artifact createBraceletOfMagicDefense() {
        Artifact bracelet = new Artifact();
        bracelet.setName("Bracelet of Magic Defense");
        bracelet.setType(Artifact.TYPE_ACCESSORY);
        bracelet.setLvl(10);
        bracelet.setAscii(("   /--\\   " + "==< /\\ >==" + "   \\--/   ").toCharArray());
        bracelet.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 4, 4, 4, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        return bracelet;
    }


    private Artifact createBraceletOfEnergy() {
        Artifact bracelet = new Artifact();
        bracelet.setName("Bracelet of Energy");
        bracelet.setType(Artifact.TYPE_ACCESSORY);
        bracelet.setLvl(10);
        bracelet.setAscii(("   /--\\   " + "==< () >==" + "   \\--/   ").toCharArray());
        bracelet.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 1, 1, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        return bracelet;
    }

    private Artifact createBraceletOfMana() {
        Artifact bracelet = new Artifact();
        bracelet.setName("Bracelet of Mana");
        bracelet.setType(Artifact.TYPE_ACCESSORY);
        bracelet.setLvl(11);
        bracelet.setAscii(("   /--\\   " + "==< || >==" + "   \\--/   ").toCharArray());
        bracelet.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 4, 4, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        return bracelet;
    }

    private Artifact createBraceletOfProtection() {
        Artifact bracelet = new Artifact();
        bracelet.setName("Bracelet of Protection");
        bracelet.setType(Artifact.TYPE_ACCESSORY);
        bracelet.setLvl(16);
        bracelet.setAscii(("   /--\\   " + "==< XX >==" + "   \\--/   ").toCharArray());
        bracelet.setAscii_color(
                7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
                7, 7, 7, 7, 4, 4, 7, 7, 7, 7,
                7, 7, 7, 7, 7, 7, 7, 7, 7, 7);
        return bracelet;
    }

    private Artifact createBraceletOfTheBerserker() {
        Artifact bracelet = new Artifact();
        bracelet.setName("Bracelet of the Berserker");
        bracelet.setType(Artifact.TYPE_ACCESSORY);
        bracelet.setLvl(17);
        bracelet.setAscii(("   /--\\   " + "==< ▌▐ >==" + "   \\--/   ").toCharArray());
        bracelet.setAscii_color(
                5, 5, 5, 5, 5, 5, 5, 5, 5, 5,
                5, 5, 5, 5, 1, 1, 5, 5, 5, 5,
                5, 5, 5, 5, 5, 5, 5, 5, 5, 5);
        return bracelet;
    }

    private Artifact createBraceletOfRegeneration() {
        Artifact bracelet = new Artifact();
        bracelet.setName("Bracelet of Regeneration");
        bracelet.setType(Artifact.TYPE_ACCESSORY);
        bracelet.setLvl(18);
        bracelet.setAscii(("   /--\\   " + "==< ♥♥ >==" + "   \\--/   ").toCharArray());
        bracelet.setAscii_color(
                6, 6, 6, 6, 6, 6, 6, 6, 6, 6,
                6, 6, 6, 6, 1, 1, 6, 6, 6, 6,
                6, 6, 6, 6, 6, 6, 6, 6, 6, 6);
        return bracelet;
    }

    private Artifact createBraceletOfMastery() {
        Artifact bracelet = new Artifact();
        bracelet.setName("Bracelet of Mastery");
        bracelet.setType(Artifact.TYPE_ACCESSORY);
        bracelet.setLvl(19);
        bracelet.setAscii(("   /--\\   " + "==< ♦♦ >==" + "   \\--/   ").toCharArray());
        bracelet.setAscii_color(
                2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                2, 2, 2, 2, 5, 5, 2, 2, 2, 2,
                2, 2, 2, 2, 2, 2, 2, 2, 2, 2);
        return bracelet;
    }

    private Artifact createBraceletOfTheTitan() {
        Artifact bracelet = new Artifact();
        bracelet.setName("Bracelet of the Titan");
        bracelet.setType(Artifact.TYPE_ACCESSORY);
        bracelet.setLvl(20);
        bracelet.setAscii(("   /--\\   " + "==<▐██▌>==" + "   \\--/   ").toCharArray());
        bracelet.setAscii_color(
                3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
                3, 3, 3, 1, 1, 1, 1, 3, 3, 3,
                3, 3, 3, 3, 3, 3, 3, 3, 3, 3);
        return bracelet;
    }

    private Artifact createBraceletOfAttackBoost() {
        Artifact bracelet = new Artifact();
        bracelet.setName("Bracelet of Attack Boost");
        bracelet.setType(Artifact.TYPE_ACCESSORY);
        bracelet.setLvl(11);
        bracelet.setAscii(("   /--\\   " + "==< -> >==" + "   \\--/   ").toCharArray());
        bracelet.setAscii_color(
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 3, 3, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        return bracelet;
    }



}
