package org.plefevre.Controller;

import org.plefevre.View.Input;
import org.plefevre.Model.*;
import org.plefevre.View.*;

import java.util.ArrayList;

import static java.lang.Math.max;
import static java.lang.Math.min;

import org.plefevre.Model.Map;

public class FocusController {

    private HeroController heroController;
    private GameController gameController;

    private Input input;
    private Hero hero;
    private Map map;
    private Log log;
    private Log fightLog;
    private RPGInterface rpgView;

    private Monster monster = null;

    private int selected = 0;
    private int subSelected = 0;
    private boolean select = false;

    private int idFocus = FOCUS_MAP;

    public static final int FOCUS_MAP = 0;
    public static final int FOCUS_INVENTORY = 1;
    public static final int FOCUS_LOG = 2;


    public FocusController(Input input, RPGInterface rpgView) {
        this.input = input;
        this.rpgView = rpgView;
    }

    public void setHeroController(HeroController heroController) {
        this.heroController = heroController;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public void setLog(Log log) {
        this.log = log;
    }

    public void setFightLog(Log fightLog) {
        this.fightLog = fightLog;
    }

    public void gestionFocusInventaire() {
        input.setListen_x(0);
        input.setListen_y(rpgView.getH());
        input.setListen_tap(false);
        input.listen();

        if (!select) {
            if (input.getTouch() == 3) selected++;
            if (input.getTouch() == 4) selected--;

            if (input.getTouch() == 1 && selected >= 4) selected -= 4;
            if (input.getTouch() == 2 && selected <= 5) selected += 4;


            if (input.getTouch() == 5) {
                select = true;
                subSelected = 0;
            }

            if (selected < 0)
                selected = Hero.INVENTORY_SIZE - 1;
            selected %= Hero.INVENTORY_SIZE;
        } else {
            Artifact selectArtifact = hero.getInventory(selected);
            if (selectArtifact != null) {
                if (input.getTouch() == 3) subSelected++;
                if (input.getTouch() == 4) subSelected--;

                boolean isEquipable = selectArtifact.getType() == Artifact.TYPE_WEAPON || selectArtifact.getType() == Artifact.TYPE_ARMOR || selectArtifact.getType() == Artifact.TYPE_HELM;
                boolean isUsable = selectArtifact.getType() == Artifact.TYPE_POTION;

                if (isEquipable || isUsable) {
                    if (subSelected < 0)
                        subSelected = 2;
                    subSelected %= 3;

                    if (input.getTouch() == 5) {
                        if (subSelected == 2) {
                            select = false;
                        }
                        if (subSelected == 1) {
                            hero.throwE(selected);
                            select = false;
                        }
                        if (subSelected == 0) {
                            if (selectArtifact.getLvl() > hero.getLvl()) {
                                log.addTextColor("Level too hight", (byte) 1);

                            } else {
                                if (selectArtifact.getType() == Artifact.TYPE_WEAPON || selectArtifact.getType() == Artifact.TYPE_ARMOR || selectArtifact.getType() == Artifact.TYPE_HELM)
                                    hero.equip(selected);
                                else if (selectArtifact.getType() == Artifact.TYPE_POTION) hero.use(selected);
                                select = false;
                            }
                        }
                    }
                } else {
                    if (subSelected < 0)
                        subSelected = 1;
                    subSelected %= 2;

                    if (input.getTouch() == 5) {
                        if (subSelected == 0) hero.throwE(selected);
                        select = false;
                    }

                }

            } else {
                if (input.getTouch() == 1) subSelected--;
                if (input.getTouch() == 2) subSelected++;

                if (input.getTouch() == 5) {
                    if (subSelected == 0) hero.unequip(Artifact.TYPE_WEAPON, selected);
                    else if (subSelected == 1) hero.unequip(Artifact.TYPE_ARMOR, selected);
                    else if (subSelected == 2) hero.unequip(Artifact.TYPE_HELM, selected);

                    select = false;
                }

                if (subSelected < 0)
                    subSelected = 3;
                subSelected %= 4;
            }

        }
        if (input.getTouch() == 5)
            input.reload();

        BlockRPG blockRPG = null;
        for (int i = 0; i < rpgView.getBlockRPGS().size(); i++) {
            if (rpgView.getBlockRPGS().get(i) instanceof Block_Inventaire) blockRPG = rpgView.getBlockRPGS().get(i);
        }

        if (blockRPG != null) {
            ((Block_Inventaire) blockRPG).setSelected(selected);
            ((Block_Inventaire) blockRPG).setSubSelected(subSelected);
            ((Block_Inventaire) blockRPG).setSelect(select);

        }

    }

    private int gestionFocusModal(int nb) {

        input.setListen_x(0);
        input.setListen_y(rpgView.getH());
        input.setListen_tap(false);
        input.listen();

        if (input.getTouch() == 3) selected++;
        if (input.getTouch() == 4) selected--;
        if (input.getTouch() == 5) {
            if (selected == 1) {
                return 1;
            } else {
                return 0;
            }
        }

        if (selected < 0)
            selected = nb - 1;
        selected %= nb;

        if (rpgView.getModal() instanceof Block_LvlComplete blockLvlComplete) blockLvlComplete.setSelected(selected);
        else if (rpgView.getModal() instanceof Block_Fight blockFight) blockFight.setSelected(selected);
        else if (rpgView.getModal() instanceof Block_Victory blockVictory) blockVictory.setSelected(selected);
        else if (rpgView.getModal() instanceof Block_Defeat blockDefeat) blockDefeat.setSelected(selected);
        else if (rpgView.getModal() instanceof Block_LvlUp blockLvlUp) blockLvlUp.setSelected(selected);
        else if (rpgView.getModal() instanceof Block_LvlChooseFight blockLvlChooseFight)
            blockLvlChooseFight.setSelected(selected);

        return -1;
    }

    private void one_move_fight() {
        int moveMonster = (int) (Math.random() * 3);

        ConstructLog log = new ConstructLog();

        if (monster.getMana() < 10) moveMonster = 2;

        else if (monster.getMana() >= monster.getMaxMana() * 0.9 && moveMonster == 2)
            moveMonster = (int) (Math.random() * 2);


        if (moveMonster == 2) {
            monster.setMana(monster.getMana() + 5 + monster.getLvl() * 2);

            log.clean();
            log.add(monster.getName(), (byte) -1);
            log.add(" charges its mana.", (byte) 0);
            fightLog.addTextColor(log);
        }

        if (selected == 2) {
            hero.setMana(hero.getMana() + 10 + hero.getLvl() * 3);

            log.clean();
            log.add(hero.getName(), (byte) -2);
            log.add(" charges its mana.", (byte) 0);
            fightLog.addTextColor(log);
        }


        if (selected == 0) {
            int degat = hero.getAttackFight();
            int defense = (int) (monster.getDefense() * 0.2);
            if (moveMonster == 1)
                defense = (int) (monster.getDefense() + (4 + monster.getLvl() / 2) * (Math.random() - 0.5));
            int mana_consom = (int) (degat * 0.8);

            mana_consom = max(1, min(mana_consom, hero.getMaxMana() / 4));

            log.clean();
            log.add(hero.getName(), (byte) -2);
            log.add(" attacks and deals ", (byte) 0);
            log.add(degat + "", (byte) -1);
            log.add(" but ", (byte) 0);
            log.add(monster.getName(), (byte) -1);
            log.add(" block ", (byte) 0);
            log.add(defense + "", (byte) -2);
            log.add(" and get finally ", (byte) 0);
            log.add(max(0, degat - defense) + "", (byte) -1);
            log.add(" damage ", (byte) 0);
            fightLog.addTextColor(log);

            if (hero.removeMana(mana_consom)) {
                degat = max(0, degat - defense);
                monster.removePv(degat);
            } else {
                log.clean();
                log.add(hero.getName(), (byte) -2);
                log.add(" has not enought mana.", (byte) 0);
                fightLog.addTextColor(log);
            }
        }

        if (moveMonster == 0 && monster.getPv() > 0) {
            int degat = (int) (monster.getAttack() + (4 + monster.getLvl() / 2) * (Math.random() - 0.5));
            int defense = (int) ((int) hero.getDefense() * 0.2);
            if (selected == 1) {
                defense = hero.getDefenseFight();
            }

            int mana_consom = (int) (degat * 0.75);


            if (monster.getMana() >= mana_consom) {
                log.clean();
                log.add(monster.getName(), (byte) -1);
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

                monster.removeMana(mana_consom);
                degat = max(0, degat - defense);
                hero.removePv(degat);

            } else {
                log.clean();
                log.add(monster.getName(), (byte) -1);
                log.add(" has not enought mana.", (byte) 0);
                fightLog.addTextColor(log);
            }
        }


        if (hero.getPv() <= 0) {
            input.reload();
            setModal(new Block_Defeat());
        }
        if (monster.getPv() <= 0) {
            ArrayList<Artifact> artifacts = monster.getArtifact();
            int xp = monster.xpGet();

            hero.addXp(xp);
            for (Artifact artifact : artifacts) {
                hero.addToInventory(artifact);
            }

            input.reload();
            Block_Victory modal = new Block_Victory();
            modal.setXp(xp);
            modal.artifacts = artifacts;
            setModal(modal);
        }
    }

    private void setModal(BlockRPG modal) {
        rpgView.setModal(modal);
        selected = 0;
        subSelected = 0;
        select = false;
    }


    private void gestionFocusMap() {

        input.setListen_tap(false);
        input.listen();

        if (input.getTouch() == 1) gameController.moveHero(0, -1);
        if (input.getTouch() == 2) gameController.moveHero(0, 1);
        if (input.getTouch() == 3) gameController.moveHero(1, 0);
        if (input.getTouch() == 4) gameController.moveHero(-1, 0);

    }

    public void gestionFocusLog() {
        int y = 2;

        y += log.getOutput().size();


        BlockRPG blockRPG = null;

        ArrayList<BlockRPG> blockRPGS = rpgView.getBlockRPGS();

        for (int i = 0; i < blockRPGS.size(); i++) {
            if (blockRPGS.get(i) instanceof Block_Log)
                blockRPG = blockRPGS.get(i);
        }

        if (blockRPG == null)
            return;

        int rh = blockRPG.getRh();
        int rx = blockRPG.getRx();
        int ry = blockRPG.getRy();


        if (y > rh - 1)
            y = rh - 1;

        input.setListen_x(rx + 2);
        input.setListen_y(ry + y);
        input.setMoveCursor(true);
        input.setListen_tap(true);

        input.listen();
        if (input.isEnter() && !input.getText().isEmpty()) logInterprete(input.getText());
    }

    public void logInterprete(String text) {

        if (text.isEmpty())
            return;

        log.addTextColor(text, (byte) 3);
        text = text.toLowerCase();

        if (text.equals("exit") || text.equals("quit")) System.exit(0);
        else {
            boolean redraw = true;
            if (text.equals("down") || text.equals("d")) gameController.moveHero(0, 1);
            else if (text.equals("up") || text.equals("u")) gameController.moveHero(0, -1);
            else if (text.equals("right") || text.equals("r")) gameController.moveHero(1, 0);
            else if (text.equals("left") || text.equals("l")) gameController.moveHero(-1, 0);
            else if (text.equals("help") || text.equals("h")) showHelp();
            else if (text.startsWith("show")) command_show(text);
            else if (text.startsWith("equip")) command_equip(text);
            else if (text.startsWith("unequip")) command_unequip(text);
            else if (text.startsWith("throw")) command_throw(text);
            else if (text.startsWith("use")) command_use(text);
            else if (text.startsWith("log")) command_log();
            else if (text.startsWith("dimension")) log.addSimpleText(rpgView.getH() + " * " + rpgView.getW());
            else {
                redraw = false;
                log.addTextColorWord("Command not found, try 'help'", "1 1 1 1 -1");
            }

            if (redraw) {
                input.setText("");
                gameController.setRedraw();
            }
        }
    }

    private void command_log() {
        //Map.Tile tile = map.getTile(hero.getX(), hero.getY());

        log.addSimpleText(hero.getPoint_to_distribute()+" point to distribute");

    }


    public void command_equip(String text) {

        String[] tt = text.split(" ");
        if (tt.length != 2)
            log.addTextColorWord("Usage: equip id_inventory", "1 -1 -1");
        else {
            try {
                int id = Integer.parseInt(tt[1]);

                if (id < 0 || id >= Hero.INVENTORY_SIZE)
                    log.addTextColor("Outside index", (byte) 1);
                else {
                    Artifact artifact = hero.getInventory(id);
                    if (artifact == null || (artifact.getType() != Artifact.TYPE_WEAPON && artifact.getType() != Artifact.TYPE_ARMOR & artifact.getType() != Artifact.TYPE_HELM))
                        log.addTextColor("No armor/weapon at this index", (byte) 1);
                    else if (artifact.getLvl() > hero.getLvl())
                        log.addTextColor("Level too hight", (byte) 1);
                    else hero.equip(id);
                }

            } catch (NumberFormatException e) {
                log.addTextColor("Not a number", (byte) 1);
            }
        }
    }

    public void command_use(String text) {


        String[] tt = text.split(" ");
        if (tt.length != 2)
            log.addTextColorWord("Usage: use id_inventory", "1 -1 -1");
        else {
            try {
                int id = Integer.parseInt(tt[1]);

                if (id < 0 || id >= Hero.INVENTORY_SIZE)
                    log.addTextColor("Outside index", (byte) 1);
                else {
                    Artifact artifact = hero.getInventory(id);
                    if (artifact == null || (artifact.getType() != Artifact.TYPE_POTION))
                        log.addTextColor("No potion at this index", (byte) 1);
                    else if (artifact.getLvl() > hero.getLvl())
                        log.addTextColor("Level too hight", (byte) 1);
                    else hero.use(id);
                }

            } catch (NumberFormatException e) {
                log.addTextColor("Not a number", (byte) 1);
            }
        }
    }

    public void command_throw(String text) {

        String[] tt = text.split(" ");
        if (tt.length != 2)
            log.addTextColorWord("Usage: throw id_inventory", "1 -1 -1");
        else {
            try {
                int id = Integer.parseInt(tt[1]);

                if (id < 0 || id >= Hero.INVENTORY_SIZE)
                    log.addTextColor("Outside index", (byte) 1);
                else {
                    Artifact artifact = hero.getInventory(id);
                    if (artifact == null)
                        log.addTextColor("No item at this index", (byte) 1);
                    else hero.throwE(id);
                }

            } catch (NumberFormatException e) {
                log.addTextColor("Not a number", (byte) 1);
            }
        }
    }

    public void command_unequip(String text) {

        String[] tt = text.split(" ");
        if (tt.length != 2)
            log.addTextColorWord("Usage: unequip weapon|armor|helm", "1 -1 -1");
        else {
            String type = tt[1].toLowerCase();

            int type_int = 0;
            if (type.equals("weapon")) type_int = Artifact.TYPE_WEAPON;
            if (type.equals("armor")) type_int = Artifact.TYPE_ARMOR;
            if (type.equals("helm")) type_int = Artifact.TYPE_HELM;

            if (type_int == 0)
                log.addTextColor("Type not recognized", (byte) 1);
            else if (hero.getNbFreeInventory() == 0)
                log.addTextColor("No space left", (byte) 1);
            else {
                hero.unequip(type_int, -1);
            }


        }
    }

    public void command_show(String text) {

        String[] tt = text.split(" ");
        if (tt.length != 2)
            log.addTextColorWord("Usage: show id_inventory", "1 -1 -1");
        else {
            try {
                int id = Integer.parseInt(tt[1]);

                if (id < 0 || id >= Hero.INVENTORY_SIZE)
                    log.addTextColor("Outside index", (byte) 1);
                else {
                    Artifact artifact = hero.getInventory(id);
                    if (artifact == null)
                        log.addTextColor("No artifact at this index", (byte) 1);
                    else {
                        String s = artifact.getStringLog(hero);
                        String[] ss = s.split("\n");
                        for (int i = 0; i < ss.length; i++) {
                            log.addTextColor(ss[i], (byte) 6);
                        }
                    }
                }

            } catch (NumberFormatException e) {
                log.addTextColor("Not a number", (byte) 1);
            }
        }
    }


    public void showHelp() {

    }

    public void focus() {

        if (rpgView.getModal() != null) {

            if (rpgView.getModal() instanceof Block_LvlComplete) {
                int res = gestionFocusModal(2);


                if (res == 1)
                    System.exit(0);
                else if (res == 0)
                    gameController.initGame();
                return;

            } else if (rpgView.getModal() instanceof Block_Fight) {
                int res = gestionFocusModal(3);

                if (res != -1) one_move_fight();

                return;

            } else if (rpgView.getModal() instanceof Block_Victory) {
                int res = gestionFocusModal(2);

                if (res != -1) setModal(null);
                return;

            } else if (rpgView.getModal() instanceof Block_Defeat) {
                int res = gestionFocusModal(2);

                if (res != -1) {
                    setModal(null);
                    gameController.initGame();
                }
                return;
            } else if (rpgView.getModal() instanceof Block_LvlUp) {
                int res = gestionFocusModal(3);

                if (res != -1) {

                    if (selected == 0)
                        hero.setAttack(hero.getAttackPoint() + 1);
                    if (selected == 1)
                        hero.setDefense(hero.getDefensePoint() + 1);
                    if (selected == 2)
                        hero.setHit_point(hero.getHit_point() + 1);
                    hero.setPoint_to_distribute(hero.getPoint_to_distribute() - 1);

                    if (hero.getPoint_to_distribute() == 0)
                        setModal(null);

                }
                return;
            } else if (rpgView.getModal() instanceof Block_LvlChooseFight) {
                int res = gestionFocusModal(2);

                if (res != -1) {

                    if (selected == 1 && Math.random() > 0.5) {
                        setModal(null);
                        return;
                    }

                    //Fight
                    setModal(((Block_LvlChooseFight) rpgView.getModal()).getBlock_fight());
                    return;
                }
            } else {
                System.out.println("Modal not recognized");
            }

            return;
        }


        if (idFocus == FOCUS_INVENTORY) gestionFocusInventaire();
        if (idFocus == FOCUS_MAP) gestionFocusMap();
        if (idFocus == FOCUS_LOG) gestionFocusLog();

        if (input.isIs_tab()) {
            idFocus++;
            idFocus %= 3;
            for (int i = 0; i < rpgView.getBlockRPGS().size(); i++) {
                rpgView.getBlockRPGS().get(i).setSelected(selected);
            }
        }

    }

    public int getFocusId() {
        return idFocus;
    }

    public void setMonster(Monster monster) {
        this.monster = monster;
    }
}
