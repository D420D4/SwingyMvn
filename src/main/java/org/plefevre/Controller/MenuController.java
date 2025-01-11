package org.plefevre.Controller;

import org.plefevre.Input;
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
        String heroName = menuView.readLine("Please enter a name for your hero : ");

        String[] classes = {"Warrior", "Mage", "Archey"};
        int classChoice = menuView.askClassChoice(classes);


        int attack = 0, defense = 0, hitPoints = 0;
        int pointsLeft = 5;

        while (pointsLeft > 0) {
            attack += menuView.askPoints("attack", attack, pointsLeft);
            pointsLeft -= attack;
            if (pointsLeft <= 0) break;
            defense += menuView.askPoints("defense", defense, pointsLeft);
            pointsLeft -= defense;
            if (pointsLeft <= 0) break;
            hitPoints += menuView.askPoints("hit points", hitPoints, pointsLeft);
            pointsLeft -= hitPoints;
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
