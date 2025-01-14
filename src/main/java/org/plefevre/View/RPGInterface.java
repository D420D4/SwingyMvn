package org.plefevre.View;

import org.plefevre.Model.Hero;
import org.plefevre.Model.Map;
import org.plefevre.Controller.Tools;

import java.util.ArrayList;

import static java.lang.Math.max;

import static org.plefevre.Controller.FocusController.*;
import static org.plefevre.View.TerminalSize.getTerminalSize;
import static org.plefevre.View.Smiley.smileyMap;

public class RPGInterface {
    ArrayList<BlockRPG> blockRPGS;

    BlockRPG modal = null;

    int animationOpen = 0;
    int final_animationOpen = 0;

    int h = 0;
    int w = 0;
    int height_bloc;
    int[] width_bloc;
    boolean loaded = false;

    final static boolean DISPLAY_TERMINAL = true;

    public void update() {
        int[] size = getTerminalSize();
        h = size[0];
        w = size[1];

        if (h < 65 || w < 272) {
            System.out.println("Screen too small. Press any touch when resized. (current : " + h + ";" + w + ", needed : 65;272)");
            return;
        }
        loaded = true;


        final_animationOpen = w / 2;

//        for (int i = 0; i < blockRPGS.size(); i++) System.out.println(blockRPGS.get(i));
        //La hauteur max en bloc
        height_bloc = 0;
        for (BlockRPG bc : blockRPGS) height_bloc = max(height_bloc, bc.getY() + bc.getH());
        //la largeur max en bloc par y;
        width_bloc = new int[height_bloc];
        for (BlockRPG bc : blockRPGS) {
            for (int j = 0; j < bc.getH(); j++)
                width_bloc[bc.getY() + j] = max(width_bloc[bc.getY() + j], bc.getX() + bc.getW());
        }

        for (BlockRPG bc : blockRPGS) {
            bc.update(w * bc.getW() / width_bloc[bc.getY()], h * bc.getH() / height_bloc, bc.getX() * w / width_bloc[bc.getY()], bc.getY() * h / height_bloc);
        }

//        System.out.println("Height : " + h + "  Width : " + w);
//        System.out.println("Width_bloc: " + Tools.arrToString(width_bloc));
//        System.out.println("height_bloc: " + height_bloc);

    }

    public void draw(Map map, Hero hero) {
        char[][] buffer = new char[h][w];
        byte[][] color = new byte[h][w];

        if (!loaded)
            return;

        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) color[i][j] = 0;
        }

        for (BlockRPG bc : blockRPGS) {
            int x = bc.getX() * w / width_bloc[bc.getY()];
            int y = bc.getY() * h / height_bloc;

            char[][] buf = bc.draw(map, hero);
            byte[][] col = bc.color();

            for (int i = 0; i < buf.length; i++) System.arraycopy(buf[i], 0, buffer[y + i], x, buf[i].length);
            if (col != null)
                for (int i = 0; i < col.length; i++) System.arraycopy(col[i], 0, color[y + i], x, col[i].length);
        }

        if (modal != null) {
            int x = (w - modal.getRw()) / 2;
            int y = (h - modal.getRh()) / 2;

            char[][] buf = modal.draw(map, hero);
            if (modal == null)
                return;
            byte[][] col = modal.color();

            for (int i = 0; i < buf.length; i++) System.arraycopy(buf[i], 0, buffer[y + i], x, buf[i].length);
            if (col != null)
                for (int i = 0; i < col.length; i++) System.arraycopy(col[i], 0, color[y + i], x, col[i].length);
        }

        char[][] buffer_cpy = Tools.deepCopyCharArray(buffer);
        byte[][] color_cpy = Tools.deepCopyByteArray(color);

        while (DISPLAY_TERMINAL && animationOpen < final_animationOpen) {
            animationOpen += 3;

            buffer = Tools.deepCopyCharArray(buffer_cpy);
            color = Tools.deepCopyByteArray(color_cpy);

            Animation.circleAnimation(buffer, color, animationOpen);
            System.out.println(animationOpen + "");
            displayOnScreen(buffer, color);

            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
            }
        }


        if (DISPLAY_TERMINAL) displayOnScreen(buffer_cpy, color_cpy);
    }

    private void displayOnScreen(char[][] buffer, byte[][] color) {

        System.out.print("\033[H\033[2J");
        System.out.flush();

        System.out.print(Color.RESET);

        StringBuilder outputBuffer = new StringBuilder();
        byte lastC = 100;

        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {

                byte bcolor = color[i][j];
                if (bcolor != lastC) {

                    outputBuffer.append(Color.RESET);
                    if (bcolor < 0)
                        outputBuffer.append(Color.BOLD);

                    lastC = bcolor;

                    if (bcolor < 0)
                        bcolor *= -1;
                    if (bcolor == 100)
                        bcolor = 0;

                    int ind_front = bcolor % 9;
                    int ind_bg = bcolor / 9;

                    if (ind_front > 0) outputBuffer.append(Color.frontColor[ind_front]);
                    if (ind_bg > 0) outputBuffer.append(Color.backgroundColor[ind_bg]);
                }

                char c = buffer[i][j];
                if (smileyMap.containsKey(c)) {
                    outputBuffer.append(smileyMap.get(c));
                    j += Smiley.width_smile[c - 1] - 1; // Ajuster j si nécessaire
                } else if (c != '\0') outputBuffer.append(c);
                else outputBuffer.append(' ');
            }
            outputBuffer.append(System.lineSeparator());
        }

        System.out.print(outputBuffer);
    }

    public int getH() {
        return h;
    }

    public int getW() {
        return w;
    }

    public void setModal(BlockRPG modal) {
        this.modal = modal;
    }

    public BlockRPG getModal() {
        return modal;
    }

    public ArrayList<BlockRPG> getBlockRPGS() {
        return blockRPGS;
    }

    public void setBlockRPGS(ArrayList<BlockRPG> blockRPGS) {
        this.blockRPGS = blockRPGS;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setFocus(int idFocus) {
        for (int i = 0; i < blockRPGS.size(); i++) blockRPGS.get(i).setFocus(false);

        for (int i = 0; i < blockRPGS.size(); i++) {
            if (blockRPGS.get(i) instanceof Block_Map && idFocus == FOCUS_MAP) blockRPGS.get(i).setFocus(true);
            if (blockRPGS.get(i) instanceof Block_Log && idFocus == FOCUS_LOG) blockRPGS.get(i).setFocus(true);
            if (blockRPGS.get(i) instanceof Block_Inventaire && idFocus == FOCUS_INVENTORY) blockRPGS.get(i).setFocus(true);
        }
    }
}


//  ▄▄▄▄▄  ▄▄▄▄▄  ▄▄▄▄▄  ▄▄▄▄▄   ▄▄▄▄▄
//  █▄█▄█  █▄█▄█  █▄█▄█  █▄█▄█   █▄▀▄█
//  ▀|||▀   ▀▀▀   ▀███▀  █▀▀▀█   ▀▀ ▀▀

//  o o o o
//  ▄ ▄ ▄ ▄
//  ▀▀▀▀▀▀▀

//    ||     ||     ││      ||      ||     ||        ||
//   /  \   /  \    ▐▌     /██\    /██\   /██\      /██\
//   \__/  /____\   ▐▌     |██|    \██/  /████\     |██|


//      /--\
//  ===<    >===
//      \--/

//      ┌──────┐
//      │      │
//      └──────┘

//♠♤♥♡♧♦♢
// =<O>=

//█
//▄
//▌
//▐▌
//▐
//▀
//░
//▒
//▓