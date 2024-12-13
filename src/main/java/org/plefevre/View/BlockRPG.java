package org.plefevre.View;

import java.util.ArrayList;

public class BlockRPG {
    int x;
    int y;
    int w;
    int h;

    int rw;
    int rh;
    int rx;
    int ry;

    boolean useColor = false;
    char[][] buffer;
    byte[][] color;

    public boolean focus = false;

    public BlockRPG(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public void update(int w, int h, int x, int y) {
        rw = w;
        rh = h;
        rx = x;
        ry = y;
    }

    public char[][] draw() {
        buffer = new char[rh][rw];
        color = new byte[rh][rw];
        String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";//░█▒ *.";
        char c = str.charAt((int) (Math.random() * str.length()));
        c = str.charAt(x * 3 + y);
        drawCadre("Test [" + x + ";" + y + "]");

        for (int i = 1; i < rh - 1; i++) {
            for (int j = 1; j < rw - 1; j++) {
                buffer[i][j] = c;
            }
        }

        return buffer;
    }

    protected void drawCadre(String name) {
        drawBorder(buffer, color, 0, 0, rw, rh);
        setTextAt(buffer, name, 3, 0);
    }

    public byte[][] color() {
        if (useColor)
            return color;
        return null;
    }

    public static void sort(ArrayList<BlockRPG> blocks) {
        blocks.sort((b1, b2) -> {
            if (b1.y != b2.y) return Integer.compare(b1.y, b2.y);
            return Integer.compare(b1.x, b2.x);
        });
    }

    @Override
    public String toString() {
        return "\tx: " + x + "\ty: " + y + "\tw: " + w + "\th: " + h;
    }

    public static void setTextAt(char[][] buf, String s, int x, int y) {
        if (y < 0 || y >= buf.length)
            return;
        for (int i = 0; i < s.length(); i++)
            if (x + i >= 0 && x + i < buf[y].length)
                buf[y][x + i] = s.charAt(i);
    }

    public static void setColor(byte[][] buf, byte color, int x, int y, int len) {
        if (x < 0 || y < 0 || y >= buf.length || x + len >= buf[y].length)
            return;
        for (int i = 0; i < len; i++) buf[y][x + i] = color;
    }

    public static void setColor(byte[][] buf, byte[] color, int x, int y) {
        if (x < 0 || y < 0 || y >= buf.length || x + color.length >= buf[y].length)
            return;
        for (int i = 0; i < color.length; i++) buf[y][x + i] = color[i];
    }


    public static void setSmiley(char[][] buf, char smil, int x, int y, int len) {
        for (int i = 0; i < len; i++)
            if (x >= 0 && y >= 0 && y < buf.length && (x + i) < buf[y].length)
                buf[y][x + i] = smil;
    }

    public static void drawButton(char[][] buf, byte[][] col, int x, int y, int w, String s, byte color) {
        drawBorder(buf, col, x, y, w, 3);
        fillColor(col, x, y, w, 3, color);
        setTextAt(buf, s, x + (w - s.length()) / 2, y + 1);
    }

    public static void drawChar(char[][] buf, char[] draw, int x, int y, int len_line) {
        int nb_line = (int) Math.ceil(1.0f * draw.length / len_line);
        for (int i = 0; i < nb_line; i++) {
            for (int j = 0; j < len_line; j++) {
                int ind = i * len_line + j;
                if (ind >= draw.length)
                    return;
                buf[y + i][x + j] = draw[ind];
            }
        }
    }

    public static void drawColor(byte[][] buf, byte[] draw, int x, int y, int len_line) {
        int nb_line = (int) Math.ceil(1.0f * draw.length / len_line);
        for (int i = 0; i < nb_line; i++) {
            for (int j = 0; j < len_line; j++) {
                int ind = i * len_line + j;
                if (ind >= draw.length)
                    return;
                buf[y + i][x + j] = draw[ind];
            }
        }
    }

    public void setBorderColor(int x, int y, int w, int h, byte c) {
        if (x < 0 || y < 0 || y + h > rh || x + w > rw)
            return;
        for (int i = x + 1; i < x + w; i++) {
            color[y][i] = c;
            color[y + h - 1][i] = c;
        }
        for (int i = y; i < y + h; i++) {
            color[i][x] = c;
            color[i][x + w - 1] = c;
        }
    }

    public static void fillColor(byte[][] color, int x, int y, int w, int h, byte c) {
        if (x < 0 || y < 0 || y + h > color.length || x + w > color[0].length)
            return;
        for (int i = x; i < x + w; i++) {
            for (int j = y; j < y + h; j++) {
                color[j][i] = c;
            }
        }
    }

    public static void drawAscii(char[][] buffer, String s, int sx, int y) {
        int x = sx;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '\n') {
                x = sx;
                y++;
            } else {
                if (y >= 0 && y < buffer.length && x >= 0 && x < buffer[0].length)
                    buffer[y][x] = c;
                x++;
            }
        }

    }


    public static void drawBorder(char[][] buffer, byte[][] color, int x, int y, int w, int h) {
        if (x < 0 || y < 0 || y + h > buffer.length || x + w > buffer[0].length)
            return;

        buffer[y][x] = '┌';
        buffer[y][x + w - 1] = '┐';
        buffer[y + h - 1][x + w - 1] = '┘';
        buffer[y + h - 1][x] = '└';

        for (int i = x + 1; i < x + w - 1; i++) {
            buffer[y][i] = '─';
            buffer[y + h - 1][i] = '─';
        }
        for (int i = y + 1; i < y + h - 1; i++) {
            buffer[i][x] = '│';
            buffer[i][x + w - 1] = '│';
        }

        if (color != null) {
            for (int i = x; i < x + w; i++) {
                color[y][i] = 0;
                color[y + h - 1][i] = 0;
            }
            for (int i = y; i < y + h; i++) {
                color[i][x] = 0;
                color[i][x + w - 1] = 0;
            }
        }
    }

    public boolean isFocusable() {
        return true;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

    public int getRw() {
        return rw;
    }

    public int getRh() {
        return rh;
    }

    public int getRx() {
        return rx;
    }

    public int getRy() {
        return ry;
    }
}
