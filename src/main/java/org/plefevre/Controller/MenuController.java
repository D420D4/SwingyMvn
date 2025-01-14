package org.plefevre.Controller;

import org.plefevre.View.Input;
import org.plefevre.Model.Hero;
import org.plefevre.View.Menu_Choose_Hero;

import java.util.ArrayList;

public class MenuController {

    private Menu_Choose_Hero menuView;
    private ArrayList<Hero> heroes;
    private Hero selectedHero;
    private Input input;

    public MenuController(Menu_Choose_Hero menuView, Input input) {
        this.menuView = menuView;
        this.heroes = Hero.getHeroesSaved();
        this.input = input;

        input.setMoveCursor(false);
        input.setListen_tap(true);
    }


    public Hero runMenu() {
        boolean running = true;
        while (running) {
            menuView.displayWelcome();
            menuView.displayHeroes(heroes);
            String input = menuView.readInput();

            if (input.equalsIgnoreCase("E")) {

                menuView.displayMessage("Bye!");
                return null;
            } else if (input.equalsIgnoreCase("C")) {
                Hero newHero = createHero();
                return newHero;
            } else {
                try {
                    int index = Integer.parseInt(input);
                    if (index >= 0 && index < heroes.size()) {
                        this.selectedHero = heroes.get(index);

                        menuView.displayMessage("You have selected " + selectedHero.getName());
                        return selectedHero;
                    } else {
                        menuView.displayMessage("Invalid choice. Please select a valid hero number.");
                    }
                } catch (NumberFormatException e) {
                    menuView.displayMessage("Invalid input. Please enter a number or 'C' to create a new hero.");
                }
            }
        }
        return null;
    }


    private Hero createHero() {
        menuView.displayMessage("Creating a new Hero : ");

        String heroName;
        boolean isValidName;

        do {
            heroName = menuView.readLine("Please enter a name for your hero (letters and numbers only): ");
            isValidName = heroName.matches("[a-zA-Z0-9]+");

            if (!isValidName) {
                menuView.displayMessage("Invalid name. Please use only letters and numbers.");
            }
        } while (!isValidName);



        String[] classes = {"Warrior", "Mage", "Archey"};
        int classChoice = menuView.askClassChoice(classes);


        int attack = 0, defense = 0, hitPoints = 0;
        int pointsLeft = 5;

        while (pointsLeft > 0) {
            int attackAdd = menuView.askPoints("attack", attack, pointsLeft);
            attack += attackAdd;
            pointsLeft -= attackAdd;
            if (pointsLeft <= 0) break;
            int defenseAdd = menuView.askPoints("defense", defense, pointsLeft);
            defense += defenseAdd;
            pointsLeft -= defenseAdd;
            if (pointsLeft <= 0) break;
            int hitPointsAdd = menuView.askPoints("hit points", hitPoints, pointsLeft);
            hitPoints += hitPointsAdd;
            pointsLeft -= hitPointsAdd;
        }

        Hero newHero = new Hero(heroName, classes[classChoice]);
        newHero.setAttack(attack);
        newHero.setDefense(defense);
        newHero.setHit_point(hitPoints);

        newHero.displayHero();

        Hero.addHero(newHero);
        Hero.saveHeroes();
        return newHero;
    }

    public Hero getSelectedHero() {
        return selectedHero;
    }
}
