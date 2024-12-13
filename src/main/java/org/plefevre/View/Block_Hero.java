package org.plefevre.View;

import org.plefevre.Game;
import org.plefevre.Model.Hero;
import org.plefevre.Smiley;

import java.util.Arrays;

public class Block_Hero extends BlockRPG {

    public Block_Hero(int x, int y, int w, int h) {
        super(x, y, w, h);

        useColor = true;
    }

    @Override
    public char[][] draw() {
        Hero hero = Game.game.getHero();
        buffer = new char[rh][rw];
        color = new byte[rh][rw];

        if (rh < 20 || rw < 30) return buffer;

        drawCadre("Hero " + hero.getName() + " ");
        if (focus) setBorderColor(0, 0, rw, rh, (byte) 2);

        String asciiArt = hero.getAsciiArt();

        int sx = 2;
        int sy = 2;

        int x = sx;
        int y = sy;

        drawAscii(buffer, asciiArt, sx, sy);

        sx = 18;
        y = sy;

        for (String s : Arrays.asList("Name", "Class", "Attack", "Defense", "Hit Points")) {
            setColor(color, (byte) -100, sx, y, 16);
            setColor(color, (byte) 2, sx + 16, y, 16);
            buffer[y][sx + 15] = ':';
            setTextAt(buffer, s, sx, y++);
        }
        setTextAt(buffer, hero.getName(), sx + 17, sy);
        setTextAt(buffer, hero.getClassName(), sx + 17, sy + 1);

        setSmiley(buffer, Smiley.SMILEY_SWORD, sx + 17, sy + 2, hero.getAttack());
        setSmiley(buffer, Smiley.SMILEY_SHIELD, sx + 17, sy + 3, hero.getDefense());
        setSmiley(buffer, Smiley.SMILEY_HEART, sx + 17, sy + 4, hero.getHit_point());

        int effAtt = hero.getEffectAtt();
        int effDef = hero.getEffectDef();

        setSmiley(buffer, Smiley.SMILEY_SWORD, sx + 17 + hero.getAttack() + 1, sy + 2, effAtt);
        setSmiley(buffer, Smiley.SMILEY_SHIELD, sx + 17 + hero.getDefense() + 1, sy + 3, effDef);


        y += 3;
        if (y > rh) return buffer;

        int rightBorder = rw - 4;
        int len = rw - 7;
        for (int i = 3; i < rightBorder; i++) buffer[y][i] = (i < 5) ? '<' : (i > len) ? '>' : '-';

        String lvl = "==<< " + hero.getLvl() + " >>==";
        setTextAt(buffer, lvl, (rw - lvl.length()) / 2, y);

        setColor(color, (byte) 3, 3, y, len);
        setColor(color, (byte) -3, (rw - lvl.length()) / 2, y, lvl.length());


        y += 2;
        if (y > rh) return buffer;

        //Draw bar xp
        String xp = hero.getExperience() + " xp";
        setTextAt(buffer, xp, (rw - xp.length()) / 2, y);
        int len_bar_xp = (len * hero.getExperience()) / hero.xpToEndLvl();
        setColor(color, (byte) -18, 3, y, len_bar_xp);
        setColor(color, (byte) -63, len_bar_xp + 3, y, len - len_bar_xp);
        y += 2;

        //Draw bar pv
        String pv = hero.getPv() + " / " + hero.getMaxPV() + " PV";
        setTextAt(buffer, pv, (rw - pv.length()) / 2, y);
        int len_bar_pv = (len * hero.getPv()) / hero.getMaxPV();
        setColor(color, (byte) -9, 3, y, len_bar_pv);
        setColor(color, (byte) -63, len_bar_pv + 3, y, len - len_bar_pv);
        y += 2;

        //Draw bar mana
        String mana = hero.getMana() + " / " + hero.getMaxMana() + " Mana";
        setTextAt(buffer, mana, (rw - mana.length()) / 2, y);
        int len_bar_mana = (len * hero.getMana()) / hero.getMaxMana();
        setColor(color, (byte) -54, 3, y, len_bar_mana);
        setColor(color, (byte) -63, len_bar_mana + 3, y, len - len_bar_mana);

        y += 3;

        sx = 3;
        sy = y;

        for (String s : Arrays.asList("Current Weapon", "Current Armor", "Current Helm")) {
            setColor(color, (byte) -100, sx, y, 20);
            setColor(color, (byte) 2, sx + 20, y, 20);
            if (y >= 0 && y < buffer.length && (sx+19) < buffer[y].length)
                buffer[y][sx + 19] = ':';
            setTextAt(buffer, s, sx, y++);
        }

        String str_weapon = hero.getCurrent_weapon() != null ? hero.getCurrent_weapon().getNameEffect() : "None";
        String str_armor = (hero.getCurrent_armor() != null ? hero.getCurrent_armor().getNameEffect() : "None");
        String str_helm = (hero.getCurrent_helm() != null ? hero.getCurrent_helm().getNameEffect() : "None");


        setTextAt(buffer, str_weapon, sx + 21, sy);
        setTextAt(buffer, str_armor, sx + 21, sy + 1);
        setTextAt(buffer, str_helm, sx + 21, sy + 2);

        if (str_weapon.contains("(")) setColor(color, (byte) -5, sx + 20 + str_weapon.indexOf('('), sy, 10);
        if (str_armor.contains("(")) setColor(color, (byte) -5, sx + 20 + str_armor.indexOf('('), sy + 1, 10);
        if (str_helm.contains("(")) setColor(color, (byte) -5, sx + 20 + str_helm.indexOf('('), sy + 2, 10);

        return buffer;
    }

    @Override
    public boolean isFocusable() {
        return false;
    }
}
