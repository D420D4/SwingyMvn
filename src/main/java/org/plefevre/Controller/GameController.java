package org.plefevre.Controller;

import org.plefevre.Game;
import org.plefevre.Input;
import org.plefevre.Model.Hero;
import org.plefevre.Model.Log;
import org.plefevre.Model.Map;
import org.plefevre.Model.Monster;
import org.plefevre.View.*;

import java.util.ArrayList;

import static java.lang.Math.abs;

public class GameController {
    private final HeroController heroController;
    private final FocusController focusController;
    //Modele
    private Hero hero;
    private Map map;
    private Log log;
    private Log fightLog;
    //View
    private RPGInterface rpgView;

    //SubController
    private Input input;

    private boolean redraw = false;


    private boolean running = true;

    public GameController(RPGInterface rpgView, Input input, Log log) {
        this.rpgView = rpgView;
        this.input = input;
        this.log = log;

        fightLog = new Log();
        heroController = new HeroController();
        focusController = new FocusController(input, rpgView);
    }

    public void initGame(Hero hero) {
        this.hero = hero;
        this.map = new Map(hero.getLvl(), (int) (Math.random() * 42000));

        input.setMoveCursor(true);

        heroController.setCurrentHero(hero);
        focusController.setMap(map);
        focusController.setHero(hero);
        focusController.setLog(log);
        focusController.setFightLog(fightLog);
        focusController.setHeroController(heroController);
        focusController.setGameController(this);

        map.centerHero(hero);

        rpgView.setModal(null);
    }

    public void initGame() {
        Hero.loadHeroes();
        ArrayList<Hero> heroes = Hero.getHeroesSaved();
        hero = heroes.get(hero.getId());
        heroController.setInitValue();

        this.map = new Map(hero.getLvl(), (int) (Math.random() * 42000));
        map.centerHero(hero);
        rpgView.setModal(null);
    }

    public void run() {
        while (running) {
//            log.addSimpleText("Input " + input);
            rpgView.setFocus(focusController.getFocusId());

            rpgView.update();

            if (!rpgView.isLoaded()) {
                input.setListen_tap(false);
                input.listen();
                continue;
            }

            rpgView.draw(map, hero);

            focusController.focus();

            input.reload();

            updateGamePlay();

            if (rpgView.getModal() == null && hero.getPoint_to_distribute() !=0) {
                rpgView.setModal(new Block_LvlUp());
            }
        }
    }

    public void moveMonsters() {

        for (int i = 0; i < map.getSize(); i++) {
            for (int j = 0; j < map.getSize(); j++) {
                map.getTile(j,i).setMovedMonster(false);
            }
        }

        for (int i = 0; i < map.getSize(); i++) {
            for (int j = 0; j < map.getSize(); j++) {
                if (map.getTile(j,i).getMonster() != null && !map.getTile(j,i).isMovedMonster())
                    moveMonster(j, i, hero);
            }
        }
    }

    private void moveMonster(int x, int y, Hero hero) {
        int nx = x;
        int ny = y;


        if (Math.sqrt(Math.pow(hero.getX() - x, 2) + Math.pow(hero.getY() - y, 2)) < 5) {
            int dx = hero.getX() - x;
            int dy = hero.getY() - y;

            if (abs(dx) > abs(dy))
                nx += dx > 0 ? 1 : -1;
            else
                ny += dy > 0 ? 1 : -1;
        } else {
            int mv = (int) (Math.random() * 4);
            if (mv == 0) nx++;
            if (mv == 1) nx--;
            if (mv == 2) ny++;
            if (mv == 3) ny--;
        }
        Map.Tile tileDepart = map.getTile(x, y);
        Map.Tile tile = map.getTile(nx, ny);
        if (tile == null || tile.isWater() || tileDepart.isMountain() ^ tile.isMountain() || tile.getMonster() != null)
            return;

        tile.setMonster(tileDepart.getMonster());
        tile.setMovedMonster(true);
        tileDepart.setMonster(null);

    }

    public void moveHero(int dx, int dy) {
        if (heroController.moveHero(dx, dy, map))
            finishLevel();
        else
            moveMonsters();
    }

    public void finishLevel() {
        heroController.addXP(map);
        input.reload();
        rpgView.setModal(new Block_LvlComplete());
        Hero.saveHeroes();
    }

    private void updateGamePlay() {
        Map.Tile[][] tiles = map.tiles;

        for (int k = -1; k < 2; k++) {
            for (int l = -1; l < 2; l++) {
                if (hero.getY() + k < 0 || hero.getY() + k >= tiles.length || hero.getX() + l < 0 || hero.getX() + l >= tiles[hero.getY() + k].length)
                    continue;
                if (tiles[hero.getY() + k][hero.getX() + l] != null) {
                    Monster monster = tiles[hero.getY() + k][hero.getX() + l].getMonster();
                    if (monster != null && !tiles[hero.getY() + k][hero.getX() + l].isMountain() ^ tiles[hero.getY()][hero.getX()].isMountain()) {
                        fightLog.clear();
                        Block_Fight blockFight = new Block_Fight(0, 0, 200, 40, fightLog);

                        Block_LvlChooseFight blockLvlChooseFight = new Block_LvlChooseFight();
                        blockLvlChooseFight.setBlock_fight(blockFight);

                        blockFight.setMonster(monster);
                        focusController.setMonster(monster);
                        tiles[hero.getY() + k][hero.getX() + l].setMonster(null);
                        rpgView.setModal(blockLvlChooseFight);
                    }
                }
            }
        }
    }


    public boolean isRunning() {
        return running;
    }

    public void stop() {
        this.running = false;
    }

    public void setRedraw() {
        this.redraw = true;
    }
}
