package org.plefevre.View;

import org.plefevre.Artifact;
import org.plefevre.Game;
import org.plefevre.Input;

import java.util.ArrayList;

import static org.plefevre.View.Block_Inventaire.HEIGHT_CASE;
import static org.plefevre.View.Block_Inventaire.WIDTH_CASE;

public class Block_Victory extends BlockRPG {
    public ArrayList<Artifact> artifacts;
    int selected = 0;
    int xp = 0;

    public Block_Victory() {
        this(0, 0, 100, 24);
    }


    public Block_Victory(int x, int y, int w, int h) {
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
        fillColor(color, 0, 0, rw, rh, (byte) -25);

        if (focus) gestionFocus();


        drawAscii(buffer,
                "▗▖  ▗▖▗▄▄▄▖ ▗▄▄▖▗▄▄▄▖▗▄▖ ▗▄▄▖▗▖  ▗▖\n" +
                        "▐▌  ▐▌  █  ▐▌     █ ▐▌ ▐▌▐▌ ▐▌▝▚▞▘ \n" +
                        "▐▌  ▐▌  █  ▐▌     █ ▐▌ ▐▌▐▛▀▚▖ ▐▌  \n" +
                        " ▝▚▞▘ ▗▄█▄▖▝▚▄▄▖  █ ▝▚▄▞▘▐▌ ▐▌ ▐▌  ", (rw - 35) / 2, 3);

        String msg = "Congratulation, you won " + xp + " xp and";
        setTextAt(buffer, msg, (rw - msg.length()) / 2, 8);

        drawBorder(buffer, null, rw / 2 - 20, 10, WIDTH_CASE, HEIGHT_CASE);
        drawBorder(buffer, null, rw / 2 + 10, 10, WIDTH_CASE, HEIGHT_CASE);

        if (!artifacts.isEmpty()) {
            drawChar(buffer, artifacts.get(0).getAscii(), rw / 2 - 20 + 1, 11, WIDTH_CASE - 2);
            String name = artifacts.get(0).getName();
            if (name.length() > WIDTH_CASE - 2)
                name = name.substring(0, WIDTH_CASE - 4) + "..";

            setTextAt(buffer, name, rw / 2 - 20 + 1, 15);
        }
        if (artifacts.size() >= 2) {
            drawChar(buffer, artifacts.get(1).getAscii(), rw / 2 + 11, 11, WIDTH_CASE - 2);
            String name = artifacts.get(1).getName();
            if (name.length() > WIDTH_CASE - 2)
                name = name.substring(0, WIDTH_CASE - 4) + "..";

            setTextAt(buffer, name, rw / 2 + 10 + 1, 15);
        }

        drawButton(buffer, color, rw / 2 - 6, 19, 12, "Continue", (byte) -65);

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
            Game.game.getRpgInterface().setModal(null);
            input.reload();
        }
    }
}