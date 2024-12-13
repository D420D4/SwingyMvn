package org.plefevre.View;

import org.plefevre.Artifact;
import org.plefevre.Game;
import org.plefevre.Model.Hero;
import org.plefevre.Input;

import java.util.ArrayList;
import java.util.Arrays;

public class Block_Inventaire extends BlockRPG {

    int selected = 0;
    int subSelected = 0;
    boolean select = false;

    public Block_Inventaire(int x, int y, int w, int h) {
        super(x, y, w, h);
        useColor = true;
    }

    final static int HEIGHT_CASE = 7;
    final static int WIDTH_CASE = 12;

    @Override
    public char[][] draw() {
        Hero hero = Game.game.getHero();
        buffer = new char[rh][rw];
        color = new byte[rh][rw];
        if (rh < 20 || rw < 30) return buffer;

        drawCadre("Inventory ");
        if (focus) {
            setBorderColor(0, 0, rw, rh, (byte) 2);
            gestionFocus();
        }


        int nbCase_per_line = (rw - 4) / WIDTH_CASE;
        for (int i = 0; i < Hero.INVENTORY_SIZE; i++) {
            Artifact artifact = hero.getInventory(i);

            int x = (i % nbCase_per_line) * WIDTH_CASE;
            int y = (i / nbCase_per_line) * HEIGHT_CASE;

            drawBorder(buffer, color, x + 2, y + 2, WIDTH_CASE, HEIGHT_CASE);

            if (artifact != null) {

                char[] ascii = artifact.getAscii();
                byte[] colorii = artifact.getAscii_color();
                drawChar(buffer, ascii, x + 3, y + 3, WIDTH_CASE - 2);
                drawColor(color, colorii, x + 3, y + 3, WIDTH_CASE - 2);

                setBorderColor(x + 2, y + 2, WIDTH_CASE, HEIGHT_CASE, artifact.getColor());

                setTextAt(buffer, artifact.getName(WIDTH_CASE), x + 3, y + 7);
            }

            if (selected == i && focus) {
                setBorderColor(x + 2, y + 2, WIDTH_CASE, HEIGHT_CASE, (byte) ((artifact != null ? artifact.getColor() : 0) + 36));
                setBorderColor(x + 2, y + 2 + HEIGHT_CASE - 2, WIDTH_CASE, 2, (byte) ((artifact != null ? artifact.getColor() : 0) + 36));


            }

        }

        int line_y = (int) (Math.ceil(1.0f * Hero.INVENTORY_SIZE / nbCase_per_line) * HEIGHT_CASE + 3);
        buffer[line_y][0] = '├';
        buffer[line_y][rw - 1] = '┤';
        for (int i = 1; i < rw - 1; i++) {
            buffer[line_y][i] = '─';
        }


        Artifact selectArtifact = hero.getInventory(selected);
        if (selectArtifact != null) {
            drawBorder(buffer, color, 2, line_y + 2, WIDTH_CASE, HEIGHT_CASE);
            setBorderColor(2, line_y + 2, WIDTH_CASE, HEIGHT_CASE, selectArtifact.getColor());

            char[] ascii = selectArtifact.getAscii();
            byte[] colorii = selectArtifact.getAscii_color();
            drawChar(buffer, ascii, 3, line_y + 3, WIDTH_CASE - 2);
            drawColor(color, colorii, 3, line_y + 3, WIDTH_CASE - 2);
            setTextAt(buffer, selectArtifact.getName(WIDTH_CASE), 3, line_y + 7);

            int sx = 16;
            int y = line_y + 3;
            for (String s : Arrays.asList("Name", "Level", "Attack", "Defense", "Effect")) {
                setColor(color, (byte) -100, sx, y, 14);
                setColor(color, (byte) 2, sx + 14, y, 20);
                buffer[y][sx + 13] = ':';
                setTextAt(buffer, s, sx, y++);
            }
            y = line_y + 3;
            setTextAt(buffer, selectArtifact.getName(), sx + 15, y++);
            setTextAt(buffer, selectArtifact.getLvl() + "", sx + 15, y++);
            setTextAt(buffer, selectArtifact.getAttack() + "", sx + 15, y++);
            setTextAt(buffer, selectArtifact.getDefense() + "", sx + 15, y++);

            ArrayList<Artifact.PassivEffect> passivEffects = selectArtifact.getPassivEffect();

            for (Artifact.PassivEffect passivEffect : passivEffects) {
                String val = "";
                byte col = -1;
                if (passivEffect.getValue() >= 0) {
                    val = "+" + passivEffect.getValue();
                    col = -2;
                } else val = passivEffect.getValue() + "";
                setColor(color, col, sx + 14, y, 20);
                setTextAt(buffer, val + " " + passivEffect.getName(), sx + 15, y++);
            }
        }


        if (select) {
            if (selectArtifact != null) {
                int y = line_y + HEIGHT_CASE + 3;
                boolean isEquipable = selectArtifact.getType() == Artifact.TYPE_WEAPON || selectArtifact.getType() == Artifact.TYPE_ARMOR || selectArtifact.getType() == Artifact.TYPE_HELM;
                boolean isUsable = selectArtifact.getType() == Artifact.TYPE_POTION;

                if (isEquipable)
                    drawButton(buffer, color, 2, y, 10, "Equip", (byte) (subSelected == 0 ? -65 : 0));
                if (isUsable)
                    drawButton(buffer, color, 2, y, 10, "Use", (byte) (subSelected == 0 ? -65 : 0));

                drawButton(buffer, color, 16, y, 10, "Throw", (byte) (subSelected == ((isEquipable || isUsable) ? 1 : 0) ? -65 : 0));

                drawButton(buffer, color, 30, y, 5, "X", (byte) ((subSelected == 2 || (!(isEquipable || isUsable) && subSelected == 1)) ? -65 : 0));
            } else {
                int y = line_y - 1;
                drawButton(buffer, color, 2, y += 3, 20, "Un-equip Weapon", (byte) (subSelected == 0 ? -65 : 0));
                drawButton(buffer, color, 2, y += 3, 20, "Un-equip Armor", (byte) (subSelected == 1 ? -65 : 0));
                drawButton(buffer, color, 2, y += 3, 20, "Un-equip Helm", (byte) (subSelected == 2 ? -65 : 0));

                drawButton(buffer, color, 2, y += 3, 5, "X", (byte) (subSelected == 3 ? -65 : 0));

            }
        }

        return buffer;
    }

    private void gestionFocus() {
        Input input = Game.game.input;
        input.setListen_tap(false);
        input.setListen_x(0);
        input.setListen_y(Game.game.getRpgInterface().getH());

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
            Artifact selectArtifact = Game.game.getHero().getInventory(selected);
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
                            Game.game.getHero().throwE(selected);
                            select = false;
                        }
                        if (subSelected == 0) {
                            if (selectArtifact.getLvl() > Game.game.getHero().getLvl()) {
                                Game.game.getLog().addTextColor("Level too hight", (byte) 1);

                            } else {
                                if (selectArtifact.getType() == Artifact.TYPE_WEAPON || selectArtifact.getType() == Artifact.TYPE_ARMOR || selectArtifact.getType() == Artifact.TYPE_HELM)
                                    Game.game.getHero().equip(selected);
                                else if (selectArtifact.getType() == Artifact.TYPE_POTION) Game.game.getHero().use(selected);
                                select = false;
                            }
                        }
                    }
                } else {
                    if (subSelected < 0)
                        subSelected = 1;
                    subSelected %= 2;

                    if (input.getTouch() == 5) {
                        if (subSelected == 0) Game.game.getHero().throwE(selected);
                        select = false;
                    }

                }

            } else {
                if (input.getTouch() == 1) subSelected--;
                if (input.getTouch() == 2) subSelected++;

                if (input.getTouch() == 5) {
                    Game.game.getLog().addSimpleText("Select : " + subSelected);

                    if (subSelected == 0) Game.game.getHero().unequip(Artifact.TYPE_WEAPON, selected);
                    else if (subSelected == 1) Game.game.getHero().unequip(Artifact.TYPE_ARMOR, selected);
                    else if (subSelected == 2) Game.game.getHero().unequip(Artifact.TYPE_HELM, selected);

                    select = false;
                }

                if (subSelected < 0)
                    subSelected = 3;
                subSelected %= 4;
            }

        }
        if (input.getTouch() == 5)
            input.reload();


    }

}
