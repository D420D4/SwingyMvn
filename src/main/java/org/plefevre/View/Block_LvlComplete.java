package org.plefevre.View;

import org.plefevre.Game;
import org.plefevre.Input;

public class Block_LvlComplete extends BlockRPG {
    int selected = 0;

    public Block_LvlComplete() {
        this(0, 0, 100, 20);
    }


    public Block_LvlComplete(int x, int y, int w, int h) {
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

        drawCadre("Level complete ");
        fillColor(color, 0, 0, rw, rh, (byte) -25);

        if (focus) gestionFocus();

        drawAscii(buffer, "▗▖   ▗▄▄▄▖▗▖  ▗▖▗▄▄▄▖▗▖        ▗▄▄▖ ▗▄▖ ▗▖  ▗▖▗▄▄▖ ▗▖   ▗▄▄▄▖▗▄▄▄▖▗▄▄▄▖\n" +
                "▐▌   ▐▌   ▐▌  ▐▌▐▌   ▐▌       ▐▌   ▐▌ ▐▌▐▛▚▞▜▌▐▌ ▐▌▐▌   ▐▌     █  ▐▌   \n" +
                "▐▌   ▐▛▀▀▘▐▌  ▐▌▐▛▀▀▘▐▌       ▐▌   ▐▌ ▐▌▐▌  ▐▌▐▛▀▘ ▐▌   ▐▛▀▀▘  █  ▐▛▀▀▘\n" +
                "▐▙▄▄▖▐▙▄▄▖ ▝▚▞▘ ▐▙▄▄▖▐▙▄▄▖    ▝▚▄▄▖▝▚▄▞▘▐▌  ▐▌▐▌   ▐▙▄▄▖▐▙▄▄▖  █  ▐▙▄▄▖", (rw - 73) / 2, 3);

        setTextAt(buffer, "Congratulation! Next level?", (rw - 27) / 2, 8);

        drawButton(buffer, color, rw / 2 - 30, 12, 12, "Continue", (byte) (selected == 0 ? -65 : -25));
        drawButton(buffer, color, rw / 2 + 18, 12, 12, "Leave", (byte) (selected == 1 ? -65 : -25));

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
            } else {
                Game.game.initGame();
                input.reload();
            }

        }


        if (selected < 0)
            selected = 1;
        selected %= 2;

    }
}