package org.plefevre.View;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.plefevre.Color;
import org.plefevre.Game;
import org.plefevre.Menu;
import org.plefevre.Model.Hero;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;


public class Menu_Choose_Hero extends Menu {

    @Override
    public boolean interaction() {
        ArrayList<Hero> heroes = Hero.getHeroesSaved();

        try (Terminal terminal = TerminalBuilder.terminal()) {
            LineReader reader = LineReaderBuilder.builder().terminal(terminal).build();


            System.out.println(Color.BLUE + "Welcome to our great game, please choose or create a Hero" + Color.RESET);

            while (true) {
                System.out.println(Color.CYAN + "--------------------------------------------" + Color.RESET);


                if (heroes == null || heroes.size() == 0)
                    System.out.println(Color.RED + "You have no Hero created" + Color.RESET);
                else {

                    System.out.println(Color.GREEN + "List of saved Heroes:" + Color.RESET);
                    for (int i = 0; i < heroes.size(); i++) {
                        Hero hero = heroes.get(i);
                        System.out.println("  [" + Color.YELLOW + i + Color.RESET + "] : " + hero.className.toUpperCase(Locale.ROOT) + " lvl" + hero.lvl + " " + hero.name);
                    }
//                Game.game.setHero(0);
//                Game.game.initGame();
//                Menu.menu = Game.game;
//                if (true) return true;
                }

                //TODO Remove


                System.out.println();
                System.out.println(Color.CYAN + "[C]  : Create a Hero" + Color.RESET);
                System.out.println(Color.CYAN + "[E]  : Exit" + Color.RESET);

                String input = reader.readLine().trim();

                if (input.equalsIgnoreCase("E")) {

                    System.out.println(Color.RED + "Bye!" + Color.RESET);
                    return false;
                } else if (input.equalsIgnoreCase("C")) {

                    createHero(reader);

                } else {
                    try {
                        int chosenHero = Integer.parseInt(input);
                        if (chosenHero >= 0 && chosenHero < (heroes != null ? heroes.size() : 0)) {
                            Hero selectedHero = heroes.get(chosenHero);

                            Game.game.setHero(chosenHero);
                            Game.game.initGame();

                            Menu.menu = Game.game;
                            System.out.println(Color.YELLOW + "You have selected " + selectedHero.name + " the " + selectedHero.className + Color.RESET);
                            return true;
                        } else {
                            System.out.println(Color.RED + "Invalid choice. Please select a valid hero number." + Color.RESET);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println(Color.RED + "Invalid input. Please enter a number or 'C' to create a new hero." + Color.RESET);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean createHero(LineReader scanner) {
        System.out.println(Color.GREEN + "Creating a new Hero:" + Color.RESET);


        System.out.println("Please enter a name for your hero:");
        String heroName = scanner.readLine().trim();


        String[] classes = {"Warrior", "Mage", "Archey"};
        System.out.println("Choose a class for your hero:");
        for (int i = 0; i < classes.length; i++) {
            System.out.println("  [" + i + "] " + classes[i]);
        }

        String className = "";
        while (true) {
            try {
                int classChoice = Integer.parseInt(scanner.readLine().trim());
                if (classChoice >= 0 && classChoice < classes.length) {
                    className = classes[classChoice];
                    break;
                } else {
                    System.out.println(Color.RED + "Invalid class choice. Please select a valid class number." + Color.RESET);
                }
            } catch (NumberFormatException e) {
                System.out.println(Color.RED + "Invalid input. Please enter a number." + Color.RESET);
            }
        }

        int attack = 0, defense = 0, hitPoints = 0;
        int pointsLeft = 5;

        while (pointsLeft > 0) {
            System.out.println("You have " + Color.YELLOW + pointsLeft + Color.RESET + " points left to distribute between attack, defense and hit points.");
            System.out.println("Enter points for attack (current: " + attack + "):");
            int previous = attack;
            attack += getValidPoints(scanner, pointsLeft);
            pointsLeft -= attack - previous;

            if (pointsLeft > 0) {
                System.out.println("Enter points for defense (current: " + defense + "):");
                previous = defense;
                defense += getValidPoints(scanner, pointsLeft);
                pointsLeft -= defense - previous;
            }

            if (pointsLeft > 0) {
                System.out.println("Enter points for hit points (current: " + hitPoints + "):");
                previous = hitPoints;
                hitPoints += getValidPoints(scanner, pointsLeft);
                pointsLeft -= hitPoints - previous;
            }
        }

        Hero newHero = new Hero(heroName, className);
        newHero.attack = attack;
        newHero.defense = defense;
        newHero.hit_point = hitPoints;
        newHero.displayHero();

        Hero.addHero(newHero);
        ArrayList<Hero> heroes = Hero.getHeroesSaved();

        Game.game.setHero(heroes.size() - 1);
        Game.game.initGame();
        Menu.menu = Game.game;

        return true;
    }


    private int getValidPoints(LineReader scanner, int maxPoints) {
        while (true) {
            try {
                int points = Integer.parseInt(scanner.readLine().trim());
                if (points >= 0 && points <= maxPoints) {
                    return points;
                } else {
                    System.out.println(Color.RED + "Invalid input. You have " + maxPoints + " points left." + Color.RESET);
                }
            } catch (NumberFormatException e) {
                System.out.println(Color.RED + "Invalid input. Please enter a valid number." + Color.RESET);
            }
        }
    }
}
