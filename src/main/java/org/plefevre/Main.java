package org.plefevre;

import org.plefevre.Controller.GameController;
import org.plefevre.Model.Hero;
import org.plefevre.View.Menu_Choose_Hero;
import org.plefevre.View.RPGInterface;
import org.plefevre.controller.MenuController;

public class Main {
    public static void main(String[] args) {
        Menu.init();

        Hero.loadHeroes();

        Menu_Choose_Hero menuView = new Menu_Choose_Hero();
        MenuController menuController = new MenuController(menuView);
        Hero chosenHero = menuController.runMenu();
        if (chosenHero == null) {
            System.out.println("No hero selected. Exiting.");
            return;
        }


        Input input = new Input();
        RPGInterface rpgInterface = new RPGInterface();
        GameController gameController = new GameController(rpgInterface, input);
        gameController.initGame(chosenHero);
        gameController.run();


    }
}