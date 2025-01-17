package org.plefevre.Controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.plefevre.View.Input;
import org.plefevre.Model.Hero;
import org.plefevre.View.Menu_Choose_Hero;
import org.plefevre.View.Menu_Choose_Hero_GUI;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Set;

public class MenuController {

    private Menu_Choose_Hero menuView;
    private Menu_Choose_Hero_GUI menuView_gui;
    private ArrayList<Hero> heroes;
    private Hero selectedHero;
    private Input input;

    public MenuController(Menu_Choose_Hero menuView, Input input) {
        this.menuView = menuView;
        this.heroes = Hero.getAllHeroes();
        this.input = input;

        input.setMoveCursor(false);
        input.setListen_tap(true);
    }

    public MenuController(Menu_Choose_Hero_GUI menuChooseHeroGui) {
        this.menuView_gui = menuChooseHeroGui;
        this.heroes = Hero.getAllHeroes();
    }

    public Hero runMenuGui() {
        final Object lock = new Object();

        menuView_gui.addChooseButtonListener(e -> {
            this.heroes = Hero.getAllHeroes();
            menuView_gui.updateHeroList(heroes);
            menuView_gui.setView("CHOOSE_HERO");
        });

        menuView_gui.addCreateButtonListener(e -> {
            menuView_gui.setView("CREATE_HERO");
        });

        menuView_gui.addGenerateButtonListener(this::actionPerformed);

        menuView_gui.addSelectButtonListener(e -> {
            int index = menuView_gui.getHeroIdSelected();
            selectedHero = Hero.loadHeroById(index);
            if (selectedHero != null) {
                synchronized (lock) {
                    lock.notify();
                }
            } else {
                menuView_gui.showError("Invalid choice. Please select a valid Hero.");
            }
        });

        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }
        menuView_gui.close();

        return selectedHero;
    }

    public Hero runMenuConsole() {
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
            heroName = menuView.readLine("Please enter a name for your hero (letters and numbers only and between 3 and 15 characters) : ");
            isValidName = heroName.matches("[a-zA-Z0-9]+");
            isValidName = isValidName && heroName.length() >= 3 && heroName.length() <= 15;

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

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Hero>> violations = validator.validate(newHero);

        // Affichage des erreurs
        if (!violations.isEmpty()) {
            String err = "Errors, input not valid :";
            for (ConstraintViolation<Hero> violation : violations) err += " - " + violation.getMessage();

            System.err.println(err);
            System.exit(1);
        }


        newHero.displayHero();

        newHero.saveHero();
        return newHero;
    }

    public Hero getSelectedHero() {
        return selectedHero;
    }

    private void actionPerformed(ActionEvent e) {

        String name = menuView_gui.getCreateHeroName();
        String className = menuView_gui.getCreateHeroClass();
        int attack = menuView_gui.getCreateHeroAttack();
        int defense = menuView_gui.getCreateHeroDefense();
        int hitPoints = menuView_gui.getCreateHeroHitPoints();


        Hero newHero = new Hero(name, className);
        newHero.setAttack(attack);
        newHero.setDefense(defense);
        newHero.setHit_point(hitPoints);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Hero>> violations = validator.validate(newHero);

        // Affichage des erreurs
        if (!violations.isEmpty()) {
            String err = "Errors, input not valid :";
            for (ConstraintViolation<Hero> violation : violations) err += "\n - " + violation.getMessage();

            menuView_gui.showError(err);
            return;
        }

        newHero.saveHero();


        menuView_gui.showSuccess("Hero created successfully.");


        menuView_gui.setView("MENU");
    }
}
