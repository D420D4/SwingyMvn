package org.plefevre.View;

import org.plefevre.Model.Hero;
import org.plefevre.Model.Map;

public class Block_LvlChooseFight extends BlockRPG {
    private int selected = 0;
    private Block_Fight block_fight;

    public Block_LvlChooseFight() {
        this(0, 0, 100, 20);
    }


    public Block_LvlChooseFight(int x, int y, int w, int h) {
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

        drawCadre("Fight ?");
        fillColor(color, 0, 0, rw, rh, (byte) -16);

        drawAscii(buffer,
                "▗▄▄▄▖▗▄▄▄▖ ▗▄▄▖▗▖ ▗▖▗▄▄▄▖   ▗▄▄▄▖ \n" +
                   "▐▌     █  ▐▌   ▐▌ ▐▌  █    ▝▘   █ \n" +
                   "▐▛▀▀▘  █  ▐▌▝▜▌▐▛▀▜▌  █       ▄▀  \n" +
                   "▐▌   ▗▄█▄▖▝▚▄▞▘▐▌ ▐▌  █       ▄ ", (rw - 34) / 2, 3);

        setTextAt(buffer, "You meet a monster. Do you want fight or run?", (rw - 45) / 2, 8);

        drawButton(buffer, color, rw / 2 - 30, 12, 12, "Fight", (byte) (selected == 0 ? -64 : -16));
        drawButton(buffer, color, rw / 2 + 18, 12, 12, "Try to run", (byte) (selected == 1 ? -64 : -16));


        return buffer;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public Block_Fight getBlock_fight() {
        return block_fight;
    }

    public void setBlock_fight(Block_Fight block_fight) {
        this.block_fight = block_fight;
    }
}