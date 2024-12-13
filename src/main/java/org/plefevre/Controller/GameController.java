package org.plefevre.Controller;

import org.plefevre.Input;
import org.plefevre.Model.Hero;
import org.plefevre.Model.Map;
import org.plefevre.View.Block_Fight;
import org.plefevre.View.RPGInterface;

public class GameController {

    //Modele
    private Hero hero;
    private Map map;

    //View
    private RPGInterface rpgView;

    //SubController
    private Input input;


    private boolean running = true;

    public GameController(RPGInterface rpgView, Input input) {
        this.rpgView = rpgView;
        this.input = input;
    }

    public void initGame(Hero hero) {
        this.hero = hero;
        this.map  = new Map(hero.getLvl(), (int)(Math.random() * 42000));

        map.centerHero(hero);

        hero.setMana(hero.getMaxMana());
        hero.setPv(hero.getMaxPV());

        rpgView.setModal(null);
    }

    public void run() {
        while (running) {
            rpgView.update();
            rpgView.draw();

            input.listen();
            String text = input.getText().trim();

            if (text.equalsIgnoreCase("q")) {
                running = false;
                continue;
            }

            switch (input.getTouch()) {
                case 1: moveHero(0, -1); break;  // Up
                case 2: moveHero(0, 1);  break;  // Down
                case 3: moveHero(1, 0);  break;  // Right
                case 4: moveHero(-1, 0); break;  // Left
                case 5:
                    // Touche 'Enter' ou 'Confirmation'
                    // Gérer éventuellement autre chose
                    break;
                default:
                    break;
            }

            updateGamePlay();

            if (rpgView.getModal() != null) {

            }
        }

    }

    private void updateGamePlay() {
        Map.Tile[][] tiles = map.tiles;

        for (int k = -1; k < 2; k++) {
            for (int l = -1; l < 2; l++) {
                if (hero.getY() + k < 0 || hero.getY() + k >= tiles.length || hero.getX() + l < 0 || hero.getX() + l >= tiles[hero.getY() + k].length)
                    continue;
                if (tiles[hero.getY() + k][hero.getX() + l] != null && tiles[hero.getY() + k][hero.getX() + l].getMonster() != null && !tiles[hero.getY() + k][hero.getX() + l].isMountain() ^ tiles[hero.getY()][hero.getX()].isMountain()) {
                    Block_Fight blockFight = new Block_Fight(0, 0, 200, 40);
                    blockFight.monster = tiles[hero.getY() + k][hero.getX() + l].getMonster();
                    tiles[hero.getY() + k][hero.getX() + l].setMonster(null);
                    rpgView.setModal(blockFight);
                }
            }
        }
    }

    public void moveHero(int dx, int dy) {
        hero.move(dx, dy);
        hero.regenerate();
        hero.updateEffect();
        input.reload();

        map.moveMonster();
    }

    public boolean isRunning() {
        return running;
    }

    public void stop() {
        this.running = false;
    }

}
