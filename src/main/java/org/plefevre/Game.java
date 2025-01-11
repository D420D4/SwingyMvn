package org.plefevre;

import org.plefevre.Model.Hero;
import org.plefevre.Model.Map;
import org.plefevre.View.*;

import java.util.ArrayList;

public class Game {

    //TODO modal choose battle
    //TODO GUI
    //TODO BDD
    //TODO Annotation based user input validation.

/*
    public static Game game = new Game();
    public Input input = new Input();
    Map map;
    Hero hero = null;

    int heroId = 0;

    int focus = 0;

    RPGInterface rpgInterface;
    Block_Log log;

    private boolean redraw = false;

    public Game() {
        rpgInterface = new RPGInterface();

        System.out.println("Test " + Color.BOLD + Color.BLUE + "Test " + Color.BOLD_OFF + "Test " + Color.RESET);

        ArrayList<BlockRPG> blockRPGS = new ArrayList<>();

        log = new Block_Log(0, 4, 4, 1);

        blockRPGS.add(new Block_Hero(4, 0, 1, 2));
        blockRPGS.add(new Block_Inventaire(4, 2, 1, 3));
        blockRPGS.add(new Block_Map(0, 0, 4, 4));
        blockRPGS.add(log);

        BlockRPG.sort(blockRPGS);
        rpgInterface.blockRPGS = blockRPGS;

        rpgInterface.blockRPGS.get(focus).focus = true;
    }

    public void initGame() {
        rpgInterface.modal = null;

        Hero.loadHeroes();
        ArrayList<Hero> heroes = Hero.getHeroesSaved();
        hero = heroes.get(heroId);

        map = new Map(hero.lvl, (int) (Math.random() * 42000));

        hero.pv = hero.getMaxPV();
        hero.mana = hero.getMaxMana();
        map.centerHero(hero);
    }

    @Override
    public boolean interaction() {

        redraw = false;
        updateGamePlay();

        rpgInterface.update();

        if (!rpgInterface.loaded) {
            input.listen();
            return true;
        }

        rpgInterface.draw();
        if (redraw)
            return true;

        input.listen();

        if (input.text.equalsIgnoreCase("q")) System.exit(0);

        if (rpgInterface.modal != null) {
            rpgInterface.blockRPGS.get(focus).focus = false;
        } else {
            if (input.is_tab) {
                rpgInterface.blockRPGS.get(focus).focus = false;
                focus++;
                focus %= rpgInterface.blockRPGS.size();

                while (!rpgInterface.blockRPGS.get(focus).isFocusable()) {
                    focus++;
                    focus %= rpgInterface.blockRPGS.size();
                }
            }
            rpgInterface.blockRPGS.get(focus).focus = true;
        }

        return true;
    }

    private void updateGamePlay() {
        //Chercher si ennemy autour;

        Map.Tile[][] tiles = map.tiles;

        for (int k = -1; k < 2; k++) {
            for (int l = -1; l < 2; l++) {
                if (hero.y + k < 0 || hero.y + k >= tiles.length || hero.x + l < 0 || hero.x + l >= tiles[hero.y + k].length)
                    continue;
                if (tiles[hero.y + k][hero.x + l] != null && tiles[hero.y + k][hero.x + l].monster != null && !tiles[hero.y + k][hero.x + l].mountain ^ tiles[hero.y][hero.x].mountain) {
                    Block_Fight blockFight = new Block_Fight(0, 0, 200, 40);
                    blockFight.monster = tiles[hero.y + k][hero.x + l].monster;
                    tiles[hero.y + k][hero.x + l].monster = null;
                    rpgInterface.modal = blockFight;
                }
            }
        }
    }

    public void setHero(int heroId) {
        this.heroId = heroId;
    }


    public void moveHero(int dx, int dy) {
        hero.move(dx, dy);
        hero.regenerate();
        hero.updateEffect();
        input.reload();

        map.moveMonster(hero);
    }

    public void setRedraw(boolean redraw) {
        this.redraw = redraw;
    }

    public RPGInterface getRpgInterface() {
        return rpgInterface;
    }

    public Hero getHero() {
        return hero;
    }

    public Block_Log getLog() {
        return log;
    }

    public Map getMap() {
        return map;
    }*/
}
