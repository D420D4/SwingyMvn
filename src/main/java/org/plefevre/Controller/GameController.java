package org.plefevre.Controller;

import org.plefevre.Model.*;
import org.plefevre.View.Input;
import org.plefevre.View.*;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import static java.lang.Math.*;
import static java.lang.Math.max;

public class GameController {
    private final HeroController heroController;
    private final FocusController focusController;
    private final Input input;

    private Hero hero;
    private Map map;
    private final Log log;
    private final Log fightLog;

    private final RPGInterface rpgView;
    private RPGInterface_GUI rpgViewGUI;

    private Monster fightMonster = null;


    private boolean guiMode = false;

    public GameController(Input input) {
        this.input = input;

        log = new Log();
        fightLog = new Log();
        rpgView = new RPGInterface(log);
        heroController = new HeroController();
        focusController = new FocusController(input, rpgView);
    }

    public void initGui() {
        rpgViewGUI.setArrowKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        moveHero(-1, 0);
                        break;
                    case KeyEvent.VK_RIGHT:
                        moveHero(1, 0);
                        break;
                    case KeyEvent.VK_UP:
                        moveHero(0, -1);
                        break;
                    case KeyEvent.VK_DOWN:
                        moveHero(0, 1);
                        break;
                    case KeyEvent.VK_C:
                        setGuiMode(false);
                        break;
                }
            }
        });

        rpgViewGUI.setOnThrow(e -> {
            Artifact artifact = rpgViewGUI.getSelectedArtifact();
            if (artifact != null) {
                hero.throwArtifact(artifact);
                rpgViewGUI.refreshInventory();
            }
        });

        rpgViewGUI.setOnUse(e -> {
            Artifact artifact = rpgViewGUI.getSelectedArtifact();
            if (artifact == null) return;
            if (artifact.getLvl() > hero.getLvl()) {
                rpgViewGUI.showError("You need to be at least level " + artifact.getLvl() + " to use this artifact");
                return;
            }

            hero.use(artifact);
            rpgViewGUI.refreshInventory();
        });

        rpgViewGUI.setOnEquip(e -> {
                    Artifact artifact = rpgViewGUI.getSelectedArtifact();
                    if (artifact == null) return;

                    if (artifact.getLvl() > hero.getLvl()) {
                        rpgViewGUI.showError("You need to be at least level " + artifact.getLvl() + " to equip this artifact");
                        return;
                    }

                    hero.equip(artifact);
                    rpgViewGUI.refreshInventory();
                    rpgViewGUI.refreshHero();

                }
        );

        rpgViewGUI.setOnUnequipArmorListener(e -> {
            if (hero.getNbFreeInventory() == 0) {
                rpgViewGUI.showError("You need to free an inventory slot to unequip your armor");
                return;
            }
            hero.unequip(Artifact.TYPE_ARMOR, -1);
            rpgViewGUI.refreshInventory();
            rpgViewGUI.refreshHero();
        });

        rpgViewGUI.setOnUnequipHelmListener(e -> {
            if (hero.getNbFreeInventory() == 0) {
                rpgViewGUI.showError("You need to free an inventory slot to unequip your helm");
                return;
            }
            hero.unequip(Artifact.TYPE_HELM, -1);
            rpgViewGUI.refreshInventory();
            rpgViewGUI.refreshHero();
        });

        rpgViewGUI.setOnUnequipWeaponListener(e -> {
            if (hero.getNbFreeInventory() == 0) {
                rpgViewGUI.showError("You need to free an inventory slot to unequip your weapon");
                return;
            }
            hero.unequip(Artifact.TYPE_WEAPON, -1);

            rpgViewGUI.refreshHero();
            rpgViewGUI.refreshInventory();
        });

        rpgViewGUI.setOnAttack(e -> {
            one_move_fight(0);
            updateFightGui();
        });

        rpgViewGUI.setOnDefense(e -> {
            one_move_fight(1);
            updateFightGui();
        });

        rpgViewGUI.setOnCharge(e -> {
            one_move_fight(2);
            updateFightGui();
        });
    }

    public void updateFightGui() {
        rpgViewGUI.updateMonsterInfo(fightMonster);
        rpgViewGUI.updateHeroInfo(hero);
        rpgViewGUI.updateLog(fightLog);
    }

    public void initGame(Hero hero) {
        this.hero = hero;
        this.map = new Map(hero.getLvl(), (int) (Math.random() * 42000));

        input.setMoveCursor(true);

        heroController.setCurrentHero(hero);
        focusController.setHero(hero);
        focusController.setLog(log);
        focusController.setGameController(this);

        map.centerHero(hero);
        heroController.setInitValue();

        rpgView.setModal(null);
    }

    public void initGame() {
        int nbPtDistribute = hero.getPoint_to_distribute();
        hero = Hero.loadHeroById(hero.getId());

        if (hero == null)
            throw new RuntimeException("Herror while loading hero");

        hero.setPoint_to_distribute(nbPtDistribute);

        focusController.setHero(hero);
        heroController.setCurrentHero(hero);
        heroController.setInitValue();

        this.map = new Map(hero.getLvl(), (int) (Math.random() * 42000));
        map.centerHero(hero);
        rpgView.setModal(null);
        if (guiMode)
            setGuiMode(true);
    }

    public void run() {

        while (true) {
            if (!guiMode) {
                rpgView.update();
                rpgView.setFocus(focusController.getFocusId());

                if (!rpgView.isLoaded()) {
                    input.setListen_tap(false);
                    input.listen();
                    continue;
                }

                rpgView.draw(map, hero);
                focusController.focus();
                input.reload();
            } else {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ignored) {

                }
            }


            if ((rpgView.getModal() == null || guiMode) && hero.getPoint_to_distribute() != 0) {
                rpgView.setModal(new Block_LvlUp());

                if (guiMode) {
                    int choice = rpgViewGUI.showLvlUpDialog();

                    if (choice == 0) hero.setAttack(hero.getAttackPoint() + 1);
                    if (choice == 1) hero.setDefense(hero.getDefensePoint() + 1);
                    if (choice == 2) hero.setHit_point(hero.getHit_point() + 1);

                    hero.setPoint_to_distribute(hero.getPoint_to_distribute() - 1);
                    rpgViewGUI.refreshHero();
                }
            }

        }
    }

    public void moveMonsters() {

        for (int i = 0; i < map.getSize(); i++) {
            for (int j = 0; j < map.getSize(); j++) {
                map.getTile(j, i).setMovedMonster(false);
            }
        }

        for (int i = 0; i < map.getSize(); i++) {
            for (int j = 0; j < map.getSize(); j++) {
                if (map.getTile(j, i).getMonster() != null && !map.getTile(j, i).isMovedMonster())
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
        if (heroController.moveHero(dx, dy, map)) {
            finishLevel();
            return;
        } else
            moveMonsters();

        if (guiMode)
            rpgViewGUI.refreshMap();

        updateGamePlay();
    }

    public void finishLevel() {
        heroController.addXP(map);
        input.reload();
        rpgView.setModal(new Block_LvlComplete());
        hero.saveHero();
        if (guiMode) {
            int choice = rpgViewGUI.showLvlCompleteDialog();
            if (choice == JOptionPane.YES_OPTION) {
                rpgViewGUI.closeFightDialog();
                initGame();
            } else {
                System.exit(0);
            }
        }
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
                        fightMonster = monster;
                        fightLog.clear();

                        if (guiMode) {
                            rpgViewGUI.showLvlChooseFightDialog(monster);
                            tiles[hero.getY() + k][hero.getX() + l].setMonster(null);
                            return;
                        } else {

                            Block_Fight blockFight = new Block_Fight(0, 0, 200, 40, fightLog);

                            Block_LvlChooseFight blockLvlChooseFight = new Block_LvlChooseFight(monster);
                            blockLvlChooseFight.setBlock_fight(blockFight);

                            blockFight.setMonster(monster);
                            tiles[hero.getY() + k][hero.getX() + l].setMonster(null);
                            rpgView.setModal(blockLvlChooseFight);
                        }
                    }
                }
            }
        }
    }

    public void setGuiMode(boolean guiMode) {
        this.guiMode = guiMode;
        if (guiMode) {
            if (rpgViewGUI != null)
                rpgViewGUI.close();

            rpgViewGUI = new RPGInterface_GUI(hero, map);
            initGui();
        }else{
            if(rpgViewGUI != null)
                rpgViewGUI.close();
            rpgView.setModal(null);
        }
    }

    void one_move_fight(int action) {
        int moveMonster = (int) (Math.random() * 3);

        ConstructLog log = new ConstructLog();

        if (fightMonster.getMana() < 10) moveMonster = 2;

        else if (fightMonster.getMana() >= fightMonster.getMaxMana() * 0.9 && moveMonster == 2)
            moveMonster = (int) (Math.random() * 2);


        if (moveMonster == 2) {
            fightMonster.setMana(fightMonster.getMana() + 5 + fightMonster.getLvl() * 2);

            log.clean();
            log.add(fightMonster.getName(), (byte) -1);
            log.add(" charges its mana.", (byte) 0);
            fightLog.addTextColor(log);
        }

        if (action == 2) {
            hero.setMana(hero.getMana() + 10 + hero.getLvl() * 3);

            log.clean();
            log.add(hero.getName(), (byte) -2);
            log.add(" charges its mana.", (byte) 0);
            fightLog.addTextColor(log);
        }


        if (action == 0) {
            int degat = hero.getAttackFight();
            int defense = (int) (fightMonster.getDefense() * 0.2);
            if (moveMonster == 1)
                defense = (int) (fightMonster.getDefense() + (4 + (double) fightMonster.getLvl() / 2) * (Math.random() - 0.5));
            int mana_consom = (int) (degat * 0.8);

            mana_consom = max(1, min(mana_consom, hero.getMaxMana() / 4));

            log.clean();
            log.add(hero.getName(), (byte) -2);
            log.add(" attacks and deals ", (byte) 0);
            log.add(degat + "", (byte) -1);
            log.add(" but ", (byte) 0);
            log.add(fightMonster.getName(), (byte) -1);
            log.add(" block ", (byte) 0);
            log.add(defense + "", (byte) -2);
            log.add(" and get finally ", (byte) 0);
            log.add(max(0, degat - defense) + "", (byte) -1);
            log.add(" damage ", (byte) 0);
            fightLog.addTextColor(log);

            if (hero.removeMana(mana_consom)) {
                degat = max(0, degat - defense);
                fightMonster.removePv(degat);
            } else {
                log.clean();
                log.add(hero.getName(), (byte) -2);
                log.add(" has not enought mana.", (byte) 0);
                fightLog.addTextColor(log);
            }
        }

        if (moveMonster == 0 && fightMonster.getPv() > 0) {
            int degat = (int) (fightMonster.getAttack() + (4 + (double) fightMonster.getLvl() / 2) * (Math.random() - 0.5));
            int defense = (int) (hero.getDefense() * 0.2);
            if (action == 1) {
                defense = hero.getDefenseFight();
            }

            int mana_consom = (int) (degat * 0.75);


            if (fightMonster.getMana() >= mana_consom) {
                log.clean();
                log.add(fightMonster.getName(), (byte) -1);
                log.add(" attacks and deals ", (byte) 0);
                log.add(degat + "", (byte) -1);
                log.add(" but ", (byte) 0);
                log.add(hero.getName(), (byte) -2);
                log.add(" block ", (byte) 0);
                log.add(defense + "", (byte) -2);
                log.add(" and get finally ", (byte) 0);
                log.add(max(0, degat - defense) + "", (byte) -1);
                log.add(" damage ", (byte) 0);
                fightLog.addTextColor(log);

                fightMonster.removeMana(mana_consom);
                degat = max(0, degat - defense);
                hero.removePv(degat);

            } else {
                log.clean();
                log.add(fightMonster.getName(), (byte) -1);
                log.add(" has not enought mana.", (byte) 0);
                fightLog.addTextColor(log);
            }
        }


        if (hero.getPv() <= 0) {
            input.reload();
            if (guiMode) {
                int choice = rpgViewGUI.showDefeatDialog();
                if (choice == JOptionPane.YES_OPTION) {
                    rpgViewGUI.closeFightDialog();
                    initGame();
                } else {
                    System.exit(0);
                }
            } else
                rpgView.setModal(new Block_Defeat());
        }
        if (fightMonster.getPv() <= 0) {
            ArrayList<Artifact> artifacts = fightMonster.getArtifact();
            int xp = fightMonster.xpGet();

            hero.addXp(xp);
            for (Artifact artifact : artifacts) {
                hero.addToInventory(artifact);
            }

            input.reload();
            Block_Victory modal = new Block_Victory();
            modal.setXp(xp);
            modal.artifacts = artifacts;
            if (guiMode) {
                rpgViewGUI.showVictoryDialog(artifacts, xp);
                rpgViewGUI.closeFightDialog();
            } else
                rpgView.setModal(modal);
        }
    }

}
