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

        initConnection();
        createTable();

        Hero.loadHeroes();

        Input input = new Input();

        Menu_Choose_Hero menuView = new Menu_Choose_Hero(input);
        MenuController menuController = new MenuController(menuView, input);
        Hero chosenHero = menuController.runMenu();
        if (chosenHero == null) {
            System.out.println("No hero selected. Exiting.");
            return;
        }


        RPGInterface rpgInterface = new RPGInterface();
        Log log = new Log();

        ArrayList<BlockRPG> blockRPGS = new ArrayList<>();

        blockRPGS.add(new Block_Hero(4, 0, 1, 2));
        blockRPGS.add(new Block_Inventaire(4, 2, 1, 3));
        blockRPGS.add(new Block_Map(0, 0, 4, 4));
        blockRPGS.add(new Block_Log(0, 4, 4, 1, log));

        BlockRPG.sort(blockRPGS);
        rpgInterface.setBlockRPGS(blockRPGS);


        GameController gameController = new GameController(rpgInterface, input, log);
        gameController.initGame(chosenHero);
        gameController.run();

        closeConnection();
    }
}