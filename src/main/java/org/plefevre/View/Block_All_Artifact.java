package org.plefevre.View;

import org.plefevre.Model.Artifact;
import org.plefevre.Model.ArtifactLibrary;
import org.plefevre.Model.Hero;
import org.plefevre.Model.Map;

import java.util.List;

import static org.plefevre.View.Block_Inventaire.HEIGHT_CASE;
import static org.plefevre.View.Block_Inventaire.WIDTH_CASE;

public class Block_All_Artifact extends BlockRPG {
    public Block_All_Artifact(int x, int y, int w, int h) {
        super(x, y, w, h);

        useColor = true;
    }


    public char[][] draw(Map map, Hero hero) {
        buffer = new char[rh][rw];
        color = new byte[rh][rw];

        List<Artifact> artifacts = ArtifactLibrary.artifactLibrary.getAllArtifacts();

        drawCadre("All artifacts (" + artifacts.size() + ")");
/*
        for (int i = 0; i < 30; i++) {
            buffer[i+1][4] = (char) (i + '0');
            buffer[i+1][8] = (char) (Smiley.width_smile[i+1] + '0');
            buffer[i+1][10] = (char) (i+1);
            buffer[i+1][20] = '|';
        }

        if(true)
            return buffer;
*/
        int nbCase_per_line = (rw - 4) / WIDTH_CASE;
        for (int i = 0; i < artifacts.size() ; i++) {
            Artifact artifact = artifacts.get(i);
            int x = (i % nbCase_per_line) * WIDTH_CASE;
            int y = (i / nbCase_per_line) * HEIGHT_CASE;

            char[] ascii = artifact.getAscii();
            byte[] colorii = artifact.getAscii_color();

            drawChar(buffer, ascii, x + 3, y + 3, WIDTH_CASE - 2);
            drawColor(color, colorii, x + 3, y + 3, WIDTH_CASE - 2);

            drawBorder(buffer, color,x + 2, y + 2, WIDTH_CASE, HEIGHT_CASE);

            int color = 2;
            if(artifact.getLvl()>14)
                color = 6;
            else if(artifact.getLvl()>7)
                color = 3;

            setBorderColor(x + 2, y + 2, WIDTH_CASE, HEIGHT_CASE, (byte) color);

            String name = artifact.getName();
            if(name.length()> WIDTH_CASE -2)
                name = name.substring(0, WIDTH_CASE -4)+"..";

            setTextAt(buffer, name,x+3,y+7);
        }

        return buffer;
    }


}
