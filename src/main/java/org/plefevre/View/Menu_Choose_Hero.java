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
        System.out.println(MyColor.BLUE + "Welcome to our great game, please choose or create a Hero" + MyColor.RESET);
    }

    public void displayHeroes(ArrayList<Hero> heroes) {
        System.out.println(MyColor.CYAN + "--------------------------------------------" + MyColor.RESET);
        if (heroes == null || heroes.isEmpty()) {
            System.out.println(MyColor.RED + "You have no Hero created" + MyColor.RESET);
        } else {
            System.out.println(MyColor.GREEN + "List of saved Heroes:" + MyColor.RESET);
            for (int i = 0; i < heroes.size(); i++) {
                Hero hero = heroes.get(i);
                System.out.println("  [" + MyColor.YELLOW + i + MyColor.RESET + "] : "
                        + hero.getClassName().toUpperCase(Locale.ROOT)
                        + " lvl" + hero.getLvl() + " "
                        + hero.getName());
            }
        }

        System.out.println();
        System.out.println(MyColor.CYAN + "[C]  : Create a Hero" + MyColor.RESET);
        System.out.println(MyColor.CYAN + "[E]  : Exit" + MyColor.RESET);
    }

    public String readInput() {
        input.listen();

        return input.getText();
    }

    public void displayMessage(String msg) {
        System.out.println(msg);
    }

    public String askHeroName() {
        System.out.println(MyColor.GREEN + "Creating a new Hero:" + MyColor.RESET);
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
                    System.out.println(MyColor.RED + "Invalid class choice. Please select a valid class number." + MyColor.RESET);
                }
            } catch (NumberFormatException e) {
                System.out.println(MyColor.RED + "Invalid input. Please enter a number." + MyColor.RESET);
            }
        }
    }

    public int askPoints(String attribute, int currentValue, int pointsLeft) {
        while (true) {
            System.out.println("You have " + MyColor.YELLOW + pointsLeft + MyColor.RESET + " points left to distribute.");
            System.out.println("Enter points to add points " + attribute + " (current: " + currentValue + "):");
            input.listen();
            String inputString =  input.getText();
            try {
                int points = Integer.parseInt(inputString);
                if (points >= 0 && points <= pointsLeft) {
                    return points;
                } else {
                    System.out.println(MyColor.RED + "Invalid input. You have " + pointsLeft + " points left." + MyColor.RESET);
                }
            } catch (NumberFormatException e) {
                System.out.println(MyColor.RED + "Invalid input. Please enter a valid number." + MyColor.RESET);
            }
        }
    }

    public String readLine(String s) {
        System.out.println(s);
        input.listen();
        return input.getText();
    }

}
