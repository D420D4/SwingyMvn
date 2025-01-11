package org.plefevre.View;

import org.plefevre.Model.Hero;
import org.plefevre.Model.Map;

public class Block_LvlUp extends BlockRPG {
    private int selected = 0;


    public Block_LvlUp() {
        this(0, 0, 100, 20);
    }


    public Block_LvlUp(int x, int y, int w, int h) {
        super(x, y, w, h);

        rx = x;
        ry = y;
        rw = w;
        rh = h;

        useColor = true;
        focus = true;
    }

    @Override
    public char[][] draw(Map map, Hero hero) {
        buffer = new char[rh][rw];
        color = new byte[rh][rw];

        drawCadre("Level Up ");
        fillColor(color, 0, 0, rw, rh, (byte) -25);

        drawAscii(buffer, "▗▖   ▗▄▄▄▖▗▖  ▗▖▗▄▄▄▖▗▖       ▗▖ ▗▖▗▄▄▖ \n" +
                "▐▌   ▐▌   ▐▌  ▐▌▐▌   ▐▌       ▐▌ ▐▌▐▌ ▐▌\n" +
                "▐▌   ▐▛▀▀▘▐▌  ▐▌▐▛▀▀▘▐▌       ▐▌ ▐▌▐▛▀▘ \n" +
                "▐▙▄▄▖▐▙▄▄▖ ▝▚▞▘ ▐▙▄▄▖▐▙▄▄▖    ▝▚▄▞▘▐▌  ", (rw - 40) / 2, 3);

        setTextAt(buffer, "Congratulation! Choose how to distribute your point! (" + hero.getPoint_to_distribute()+")", (rw - 48) / 2, 8);

        drawButton(buffer, color, rw / 2 - 30, 12, 12, "Attack", (byte) (selected == 0 ? -65 : -25));
        drawButton(buffer, color, rw / 2 - 6, 12, 12, "Defense", (byte) (selected == 1 ? -65 : -25));
        drawButton(buffer, color, rw / 2 + 18, 12, 12, "Hit Point", (byte) (selected == 2 ? -65 : -25));

        return buffer;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }


}