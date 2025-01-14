package org.plefevre.View;

import org.plefevre.Model.Hero;
import org.plefevre.Model.Log;
import org.plefevre.Model.Map;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Block_Log extends BlockRPG {

    private Log log;

    public Block_Log(int x, int y, int w, int h, Log log) {
        super(x, y, w, h);
        useColor = true;
        this.log = log;
    }

    @Override
    public char[][] draw(Map map, Hero hero) {

        buffer = new char[rh][rw];
        color = new byte[rh][rw];

        drawCadre("Log ");
        if (focus) setBorderColor(0, 0, rw, rh, (byte) 2);

        int nbToPrint = min(log.getOutput().size(), rh - 2 - 2);

        for (int i = 0; i < nbToPrint; i++) {
            int id = log.getOutput().size() - nbToPrint + i;
            setTextAt(buffer, log.getOutput().get(id), 2, 1 + i);
            setColor(color, log.getColors().get(id), 2, 1 + i);
        }

        return buffer;
    }

    public void setLog(Log log) {
        this.log = log;
    }
}
