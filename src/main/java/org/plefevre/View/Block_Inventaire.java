package org.plefevre.View;

import org.plefevre.Model.Artifact;
import org.plefevre.Model.Hero;
import org.plefevre.Model.Map;

import java.util.ArrayList;
import java.util.Arrays;

public class Block_Inventaire extends BlockRPG {

    private int subSelected = 0;
    private boolean select = false;

    public Block_Inventaire(int x, int y, int w, int h) {
        super(x, y, w, h);
        useColor = true;
    }

    final static int HEIGHT_CASE = 7;
    final static int WIDTH_CASE = 12;

    @Override
    public char[][] draw(Map map, Hero hero) {
        buffer = new char[rh][rw];
        color = new byte[rh][rw];
        if (rh < 20 || rw < 30) return buffer;

        drawCadre("Inventory ");
        if (focus) setBorderColor(0, 0, rw, rh, (byte) 2);


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
            setTextAt(buffer, selectArtifact.getAttack(hero) + "", sx + 15, y++);
            setTextAt(buffer, selectArtifact.getDefense(hero) + "", sx + 15, y++);

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

    public int getSubSelected() {
        return subSelected;
    }

    public void setSubSelected(int subSelected) {
        this.subSelected = subSelected;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }
}
