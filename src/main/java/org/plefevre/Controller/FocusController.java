package org.plefevre.Controller;

import org.plefevre.Model.*;
import org.plefevre.View.*;

import java.util.ArrayList;

public class FocusController {

    private GameController gameController;

    private final Input input;
    private Hero hero;
    private Log log;
    private final RPGInterface rpgView;

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

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public void setLog(Log log) {
        this.log = log;
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
            else if (text.startsWith("help") || text.startsWith("h")) showHelp(text);
            else if (text.equals("guimode")) gameController.setGuiMode(true);
            else if (text.startsWith("show")) command_show(text);
            else if (text.startsWith("equip")) command_equip(text);
            else if (text.startsWith("unequip")) command_unequip(text);
            else if (text.startsWith("throw")) command_throw(text);
            else if (text.startsWith("use")) command_use(text);
            else if (text.startsWith("dimension")) log.addSimpleText(rpgView.getH() + " * " + rpgView.getW());
            else {
                redraw = false;
                log.addTextColorWord("Command not found, try 'help'", "1 1 1 1 -1");
            }

            if (redraw) input.setText("");
        }
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


    public void showHelp(String text) {
        int page = 0;
        String[] tt = text.split(" ");

        if(tt.length == 2) {
            text = tt[1];
            try {
                if (!text.isEmpty())
                    page = Integer.parseInt(text);

            } catch (NumberFormatException e) {
                log.addTextColor("Not a number [" + text + "]", (byte) 1);
            }
        }

        if(tt.length > 2) {
            log.addTextColorWord("Usage: help [page]", "1 -1 -1");
            return;
        }

        if (page == 0) {
            log.addTextColorWord("Available Commands:", "-3");
            log.addTextColorWord("- up/u: Move hero up", "-3 -3 2");
            log.addTextColorWord("- down/d: Move hero down", "-3 -3 2");
            log.addTextColorWord("- left/l: Move hero left", "-3 -3 2");
            log.addTextColorWord("- right/r: Move hero right", "-3 -3 2");
            log.addTextColorWord("- help/h [page]: Display this help message", "-3 -3 3 2");
        }

        if (page == 1) {
            log.addTextColorWord("- equip [id_inventory]: Equip an item from the inventory", "-3 -3 3 2");
            log.addTextColorWord("- unequip [weapon|armor|helm]: Unequip a specific type of item", "-3 -3 3 2");
            log.addTextColorWord("- use [id_inventory]: Use a consumable item (potion)", "-3 -3 3 2");
            log.addTextColorWord("- throw [id_inventory]: Discard an item from the inventory", "-3 -3 3 2");
            log.addTextColorWord("- show [id_inventory]: Show details of an item in the inventory", "-3 -3 3 2");
            log.addTextColorWord("- dimension: Show current game dimensions", "-3 -3 2");
        }

        if (page == 2) {
            log.addTextColorWord("- guimode: Enable GUI mode", "-3 -3 2");
            log.addTextColorWord("- exit/quit: Exit the game", "-3 -3 2");
        }
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

                if (res != -1) gameController.one_move_fight(selected);

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
}
