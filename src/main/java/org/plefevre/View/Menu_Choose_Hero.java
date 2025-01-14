package org.plefevre.View;

import org.plefevre.Model.Hero;

import java.util.ArrayList;
import java.util.Locale;


public class Menu_Choose_Hero {
    private Input input;

    public Menu_Choose_Hero(Input input) {
        this.input = input;
    }


    public void displayWelcome() {
        System.out.println(Color.BLUE + "Welcome to our great game, please choose or create a Hero" + Color.RESET);
    }

    public void displayHeroes(ArrayList<Hero> heroes) {
        System.out.println(Color.CYAN + "--------------------------------------------" + Color.RESET);
        if (heroes == null || heroes.isEmpty()) {
            System.out.println(Color.RED + "You have no Hero created" + Color.RESET);
        } else {
            System.out.println(Color.GREEN + "List of saved Heroes:" + Color.RESET);
            for (int i = 0; i < heroes.size(); i++) {
                Hero hero = heroes.get(i);
                System.out.println("  [" + Color.YELLOW + i + Color.RESET + "] : "
                        + hero.getClassName().toUpperCase(Locale.ROOT)
                        + " lvl" + hero.getLvl() + " "
                        + hero.getName());
            }
        }

        // Options
        System.out.println();
        System.out.println(Color.CYAN + "[C]  : Create a Hero" + Color.RESET);
        System.out.println(Color.CYAN + "[E]  : Exit" + Color.RESET);
    }

    public String readInput() {
        input.listen();

        return input.getText();
    }

    public void displayMessage(String msg) {
        System.out.println(msg);
    }

    public String askHeroName() {
        System.out.println(Color.GREEN + "Creating a new Hero:" + Color.RESET);
        System.out.println("Please enter a name for your hero:");
        input.listen();
        return input.getText();
    }

    public int askClassChoice(String[] classes) {
        System.out.println("Choose a class for your hero:");
        for (int i = 0; i < classes.length; i++) {
            System.out.println("  [" + i + "] " + classes[i]);
        }
        while (true) {
            input.listen();
            String inputString =  input.getText();
            try {
                int classChoice = Integer.parseInt(inputString);
                if (classChoice >= 0 && classChoice < classes.length) {
                    return classChoice;
                } else {
                    System.out.println(Color.RED + "Invalid class choice. Please select a valid class number." + Color.RESET);
                }
            } catch (NumberFormatException e) {
                System.out.println(Color.RED + "Invalid input. Please enter a number." + Color.RESET);
            }
        }
    }

    public int askPoints(String attribute, int currentValue, int pointsLeft) {
        while (true) {
            System.out.println("You have " + Color.YELLOW + pointsLeft + Color.RESET + " points left to distribute.");
            System.out.println("Enter points to add points " + attribute + " (current: " + currentValue + "):");
            input.listen();
            String inputString =  input.getText();
            try {
                int points = Integer.parseInt(inputString);
                if (points >= 0 && points <= pointsLeft) {
                    return points;
                } else {
                    System.out.println(Color.RED + "Invalid input. You have " + pointsLeft + " points left." + Color.RESET);
                }
            } catch (NumberFormatException e) {
                System.out.println(Color.RED + "Invalid input. Please enter a valid number." + Color.RESET);
            }
        }
    }

    public String readLine(String s) {
        System.out.println(s);
        input.listen();
        return input.getText();
    }

}
