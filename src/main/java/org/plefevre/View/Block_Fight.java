package org.plefevre.View;

import org.plefevre.ConstructLog;
import org.plefevre.Model.Hero;
import org.plefevre.Model.Log;
import org.plefevre.Model.Map;
import org.plefevre.Model.Monster;

import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Math.min;
import static org.plefevre.View.Block_Inventaire.HEIGHT_CASE;
import static org.plefevre.View.Block_Inventaire.WIDTH_CASE;

public class Block_Fight extends BlockRPG {
    private Monster monster = null;
    private int selected = 0;
    private Log log;

    public Block_Fight(int x, int y, int w, int h, Log log) {
        super(x, y, w, h);

        rx = x;
        ry = y;
        rw = w;
        rh = h;

        useColor = true;
        focus = true;
        this.log = log;
    }

    @Override
    public char[][] draw(Map map, Hero hero) {

        buffer = new char[rh][rw];
        color = new byte[rh][rw];

        drawCadre("Fight ");
        setBorderColor(0, 0, rw, rh, (byte) -16);
        setBorderColor(1, 0, rw - 2, rh, (byte) -16);

        int x1 = rw / 3;
        int x2 = 2 * rw / 3;

        buffer[0][x1] = '┬';
        buffer[0][x2] = '┬';
        buffer[rh - 1][x1] = '┴';
        buffer[rh - 1][x2] = '┴';
        for (int i = 1; i < rh - 1; i++) {
            buffer[i][x1] = '│';
            buffer[i][x2] = '│';
        }

        drawMonster(0);
        drawHero(hero, x2);
        drawButtons(x2, 30);
        drawLogs(x1);


        return buffer;
    }

    private void drawLogs(int xs) {
        int len_line = rw / 3 - 4 - 2;


        ArrayList<String> lines = new ArrayList<>();
        ArrayList<byte[]> linesCol = new ArrayList<>();

        for (int i = 0; i < log.getOutput().size(); i++) {
            for (int j = 0; j < log.getOutput().get(i).length(); j += len_line) {
                String sub = log.getOutput().get(i).substring(j, min(j + len_line, log.getOutput().get(i).length()));
                if (j > 0)
                    sub = "  " + sub;
                lines.add(sub);
                byte[] b = new byte[sub.length()];
                for (int k = j == 0 ? 0 : 2; k < b.length; k++) b[k] = log.getColors().get(i)[j + k - (j == 0 ? 0 : 2)];
                linesCol.add(b);
            }
        }
        int nbToPrint = min(lines.size(), rh - 2 - 2);

        for (int i = 0; i < nbToPrint; i++) {
            int id = lines.size() - nbToPrint + i;
            setTextAt(buffer, lines.get(id), xs + 2, 1 + i);
            setColor(color, linesCol.get(id), xs + 2, 1 + i);
        }
    }



    private void drawHero(Hero hero, int xs) {
        int sx;
        int sy;
        int y;

        String asciiArt;
        asciiArt = hero.getAsciiArt();

        sx = xs + 6;
        sy = 4;

        drawAscii(buffer, asciiArt, sx, sy);

        int len = rw / 3 - 6;
        sx = xs + 26;
        sy = 5;
        y = sy;
        for (String s : Arrays.asList("Name", "Class", "Level", "Attack", "Defense")) {
            setColor(color, (byte) -100, sx, y, 16);
            setColor(color, (byte) 2, sx + 16, y, 16);
            buffer[y][sx + 15] = ':';
            setTextAt(buffer, s, sx, y++);
        }
        setTextAt(buffer, hero.getName(), sx + 17, sy);
        setTextAt(buffer, hero.getClassName(), sx + 17, sy + 1);
        setTextAt(buffer, hero.getLvl() + "", sx + 17, sy + 2);
        setTextAt(buffer, hero.getAttack() + "", sx + 17, sy + 3);
        setTextAt(buffer, hero.getDefense() + "", sx + 17, sy + 4);

        y += 3;
        //Draw bar pv
        String pv = hero.getPv() + " / " + hero.getMaxPV() + " PV";
        setTextAt(buffer, pv, xs + (len - pv.length()) / 2, y);
        int len_bar_pv = (len * hero.getPv()) / hero.getMaxPV();
        setColor(color, (byte) -9, xs + 3, y, len_bar_pv);
        setColor(color, (byte) -63, xs + len_bar_pv + 3, y, len - len_bar_pv);
        y += 2;

        //Draw bar mana
        String mana = hero.getMana() + " / " + hero.getMaxMana() + " Mana";
        setTextAt(buffer, mana, xs + (len - mana.length()) / 2, y);
        int len_bar_mana = (len * hero.getMana()) / hero.getMaxMana();
        setColor(color, (byte) -54, xs + 3, y, len_bar_mana);
        setColor(color, (byte) -63, xs + len_bar_mana + 3, y, len - len_bar_mana);

        int rw9 = rw / 9;
        y += 2;
        for (int i = 0; i < 3; i++) {
            drawBorder(buffer, color, (int) (xs + rw9 * (i + 0.5)) - WIDTH_CASE / 2, y + 2, WIDTH_CASE, HEIGHT_CASE);
        }

        if (hero.getCurrent_weapon() != null) {
            int xx = (int) (xs + rw9 * (0 + 0.5)) - WIDTH_CASE / 2 + 1;
            int yy = y + 3;
            char[] ascii = hero.getCurrent_weapon().getAscii();
            byte[] colorii = hero.getCurrent_weapon().getAscii_color();
            drawChar(buffer, ascii, xx, yy, WIDTH_CASE - 2);
            drawColor(color, colorii, xx, yy, WIDTH_CASE - 2);

            setBorderColor(xx - 1, yy - 1, WIDTH_CASE, HEIGHT_CASE, hero.getCurrent_weapon().getColor());

            setTextAt(buffer, hero.getCurrent_weapon().getName(WIDTH_CASE), xx, yy + 4);
        }


        if (hero.getCurrent_armor() != null) {
            int xx = (int) (xs + rw9 * (1 + 0.5)) - WIDTH_CASE / 2 + 1;
            int yy = y + 3;
            char[] ascii = hero.getCurrent_armor().getAscii();
            byte[] colorii = hero.getCurrent_armor().getAscii_color();
            drawChar(buffer, ascii, xx, yy, WIDTH_CASE - 2);
            drawColor(color, colorii, xx, yy, WIDTH_CASE - 2);

            setBorderColor(xx - 1, yy - 1, WIDTH_CASE, HEIGHT_CASE, hero.getCurrent_armor().getColor());

            setTextAt(buffer, hero.getCurrent_armor().getName(WIDTH_CASE), xx, yy + 4);
        }

        if (hero.getCurrent_helm() != null) {
            int xx = (int) (xs + rw9 * (2 + 0.5)) - WIDTH_CASE / 2 + 1;
            int yy = y + 3;
            char[] ascii = hero.getCurrent_helm().getAscii();
            byte[] colorii = hero.getCurrent_helm().getAscii_color();
            drawChar(buffer, ascii, xx, yy, WIDTH_CASE - 2);
            drawColor(color, colorii, xx, yy, WIDTH_CASE - 2);

            setBorderColor(xx - 1, yy - 1, WIDTH_CASE, HEIGHT_CASE, hero.getCurrent_helm().getColor());

            setTextAt(buffer, hero.getCurrent_helm().getName(WIDTH_CASE), xx, yy + 4);
        }

    }

    private void drawButtons(int xs, int ys) {
        int rw9 = rw / 9;


        drawButton(buffer, color, (int) (xs + rw9 * 0.1), ys, (int) (rw9 * .8), "Attack", (byte) (selected == 0 ? -17 : -64));
        drawButton(buffer, color, (int) (xs + rw9 * 1.1), ys, (int) (rw9 * .8), "Block", (byte) (selected == 1 ? -17 : -64));
        drawButton(buffer, color, (int) (xs + rw9 * 2.1), ys, (int) (rw9 * .8), "Charge", (byte) (selected == 2 ? -17 : -64));
    }

    private void drawMonster(int xs) {
        String asciiArt = monster.getAsciiArt();

        int sx = xs + 6;
        int sy = 4;

        int y;

        drawAscii(buffer, asciiArt, sx, sy);

        sx = xs + 30;
        sy = 5;
        y = sy;

        for (String s : Arrays.asList("Name", "Level", "Attack", "Defense")) {
            setColor(color, (byte) -100, sx, y, 16);
            setColor(color, (byte) 2, sx + 16, y, 16);
            buffer[y][sx + 15] = ':';
            setTextAt(buffer, s, sx, y++);
        }

        setTextAt(buffer, monster.getName(), sx + 17, sy);
        setTextAt(buffer, String.valueOf(monster.getLvl()), sx + 17, sy + 1);
        setTextAt(buffer, String.valueOf(monster.getAttack()), sx + 17, sy + 2);
        setTextAt(buffer, String.valueOf(monster.getDefense()), sx + 17, sy + 3);
        int len = rw / 3 - 6;

        y += 5;
        //Draw bar pv
        String pv_m = monster.getPv() + " / " + monster.getMaxPV() + " PV";
        setTextAt(buffer, pv_m, xs + (len - pv_m.length()) / 2, y);
        int len_bar_pv_m = (len * monster.getPv()) / monster.getMaxPV();
        setColor(color, (byte) -9, xs + 3, y, len_bar_pv_m);
        setColor(color, (byte) -63, xs + len_bar_pv_m + 3, y, len - len_bar_pv_m);
        y += 2;

        //Draw bar mana
        String mana_m = monster.getMana() + " / " + monster.getMaxMana() + " Mana";
        setTextAt(buffer, mana_m, xs + (len - mana_m.length()) / 2, y);
        int len_bar_mana_m = (len * monster.getMana()) / monster.getMaxMana();
        setColor(color, (byte) -54, xs + 3, y, len_bar_mana_m);
        setColor(color, (byte) -63, xs + len_bar_mana_m + 3, y, len - len_bar_mana_m);
    }

    public void addTextColor(String txt, byte[] col) {
        log.getOutput().add(txt);
        log.getColors().add(col);
    }

    private void addTextColor(ConstructLog log) {
        addTextColor(log.getString(), log.getColor());
    }

    public void setMonster(Monster monster) {
        this.monster = monster;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }
}
