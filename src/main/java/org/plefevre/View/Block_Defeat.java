package org.plefevre.View;

import org.plefevre.Game;
import org.plefevre.Input;

public class Block_Defeat extends BlockRPG {
    int selected = 0;

    public Block_Defeat() {
        this(0, 0, 100, 20);
    }


    public Block_Defeat(int x, int y, int w, int h) {
        super(x, y, w, h);

        rx = x;
        ry = y;
        rw = w;
        rh = h;

        useColor = true;
        focus = true;
    }

    @Override
    public char[][] draw() {

        buffer = new char[rh][rw];
        color = new byte[rh][rw];

        drawCadre("Defeat ");
        fillColor(color, 0, 0, rw, rh, (byte) -16);

        if (focus) gestionFocus();


        drawAscii(buffer, "▗▄▄▖ ▗▄▖ ▗▖  ▗▖▗▄▄▄▖     ▗▄▖ ▗▖  ▗▖▗▄▄▄▖▗▄▄▖ \n" +
                "▐▌   ▐▌ ▐▌▐▛▚▞▜▌▐▌       ▐▌ ▐▌▐▌  ▐▌▐▌   ▐▌ ▐▌\n" +
                "▐▌▝▜▌▐▛▀▜▌▐▌  ▐▌▐▛▀▀▘    ▐▌ ▐▌▐▌  ▐▌▐▛▀▀▘▐▛▀▚▖\n" +
                "▝▚▄▞▘▐▌ ▐▌▐▌  ▐▌▐▙▄▄▖    ▝▚▄▞▘ ▝▚▞▘ ▐▙▄▄▖▐▌ ▐▌\n", (rw - 45) / 2, 3);

        setTextAt(buffer, "You are dead :/ Continue?", (rw - 25) / 2, 8);

        drawButton(buffer, color, rw / 2 - 30, 12, 12, "Restart", (byte) (selected == 0 ? -64 : -16));
        drawButton(buffer, color, rw / 2 + 18, 12, 12, "Leave", (byte) (selected == 1 ? -64 : -16));

        return buffer;
    }

    private void gestionFocus() {
        Input input = Game.game.input;
        input.setListen_tap(false);
        input.setListen_x(0);
        input.setListen_y(Game.game.getRpgInterface().getH());

        if (input.getTouch() == 3) selected++;
        if (input.getTouch() == 4) selected--;
        if (input.getTouch() == 5) {
            if (selected == 1) {
                System.exit(0);
            }else {
                Game.game.initGame();
                input.reload();
            }
        }


        if (selected < 0)
            selected = 1;
        selected %= 2;

    }
}
