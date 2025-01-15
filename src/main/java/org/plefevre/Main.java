package org.plefevre;

import org.plefevre.Controller.GameController;
import org.plefevre.Controller.MenuController;
import org.plefevre.Model.Hero;
import org.plefevre.Model.Log;
import org.plefevre.View.*;

import java.util.ArrayList;

import static org.plefevre.Model.DatabaseSetup.*;

public class Main {
    public static void main(String[] args) {

        if (args.length != 1) {
            System.err.println("Usage: java -jar swingy.jar <gui|console>");
            System.exit(1);
        }
        String mode = args[0].toLowerCase();
        boolean guiMode = false;
        if (mode.equals("gui")) {
            guiMode = true;
        } else if (!mode.equals("console")) {
            System.err.println("Usage: java -jar swingy.jar <gui|console>");
            System.exit(1);
        }

        initConnection();
        createTable();

        Input input = new Input();

        Hero chosenHero;
        chosenHero = Hero.loadHeroById(2);
/*
        if (guiMode) {
            MenuController menuController = new MenuController(new Menu_Choose_Hero_GUI());
            chosenHero = menuController.runMenuGui();
        } else {
            MenuController menuController = new MenuController(new Menu_Choose_Hero(input), input);
            chosenHero = menuController.runMenuConsole();
        }
*/
        if (chosenHero == null) {
            System.out.println("No hero selected. Exiting.");
            return;
        }


        GameController gameController = new GameController(input);
        gameController.initGame(chosenHero);
        gameController.setGuiMode(guiMode);
        gameController.run();

        closeConnection();
    }
}