package org.plefevre.View;

import org.plefevre.Model.Hero;
import org.plefevre.Model.Map;

import static java.lang.Math.max;

public class Block_Map extends BlockRPG {
    public Block_Map(int x, int y, int w, int h) {
        super(x, y, w, h);
        useColor = true;
    }

    @Override
    public char[][] draw(Map map, Hero hero) {
        int size = map.getSize();

        int dx = size / 2 - hero.getX();
        int dy = size / 2 - hero.getY();

        int MdX = max(0, (size * 2 - (rw - 6)) / 2);
        int MdY = max(0, (size - (rh - 4)) / 2);

        dx *= 2;

        dx = Math.min(MdX, max(-MdX, dx));
        dy = Math.min(MdY, max(-MdY, dy));


        buffer = new char[rh][rw];
        color = new byte[rh][rw];

        drawCadre("Map ");
        if (focus) setBorderColor(0, 0, rw, rh, (byte) 2);

        int centerX = rw / 2 - size + dx;
        int centerY = rh / 2 - size / 2 + dy;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Map.Tile tile = map.tiles[i][j];

                int x = j * 2 + centerX;
                int y = i + centerY;

                if (x <= 1 || x >= rw - 2 || y <= 1 || y >= rh - 1)
                    continue;

                byte col = 18;


                if (tile.isMountain()) col = 27;
                if (tile.isWater()) col = 36;

//                buffer[y][x] = (char) ('0' + tile.v);
                int typ = tile.getType_tile();

                if (hero.getX() == j && hero.getY() == i) {
                    buffer[y][x] = '[';
                    buffer[y][x + 1] = ']';
                    col += 4;
                    col *= -1;
                } else if (tile.getMonster() != null) {
                    buffer[y][x] = '>';
                    buffer[y][x + 1] = '<';
                    col += 1;
                    col *= -1;
                } else if (typ != 0) {
                    if (tile.isMountain()) {
                        if (typ == 1) {
                            buffer[y][x] = '/';
                            buffer[y][x + 1] = '\\';
                        } else if (typ == 2) {
                            buffer[y][x] = '^';
                            buffer[y][x + 1] = '^';
                        } else if (typ == 3) {
                            buffer[y][x] = '#';
                            buffer[y][x + 1] = ' ';
                        } else if (typ == 4) {
                            buffer[y][x] = ' ';
                            buffer[y][x + 1] = '#';
                        }
                    } else if (tile.isWater()) {
                        col += 6;
                        if (typ == 1) {
                            buffer[y][x] = '~';
                            buffer[y][x + 1] = ' ';
                        } else if (typ == 2) {
                            buffer[y][x] = '≈';
                            buffer[y][x + 1] = ' ';
                        } else if (typ == 3) {
                            buffer[y][x] = ' ';
                            buffer[y][x + 1] = '~';
                        } else if (typ == 4) {
                            buffer[y][x] = ' ';
                            buffer[y][x + 1] = '≈';
                        }
                    } else {
                        if (typ == 1) {
                            buffer[y][x] = '_';
                            buffer[y][x + 1] = ' ';
                        } else if (typ == 2) {
                            buffer[y][x] = '.';
                            buffer[y][x + 1] = ' ';
                        } else if (typ == 3) {
                            buffer[y][x] = ' ';
                            buffer[y][x + 1] = '_';
                        } else if (typ == 4) {
                            buffer[y][x] = ' ';
                            buffer[y][x + 1] = '.';
                        }
                    }
                } else if (i >= 1 && i < size - 1 && j >= 1 && j < size - 1) {

                    Map.Tile tl = map.tiles[i][j - 1];
                    Map.Tile tr = map.tiles[i][j + 1];
                    Map.Tile tu = map.tiles[i - 1][j];
                    Map.Tile td = map.tiles[i + 1][j];

                    if (tu.isMountain() && tr.isMountain() && !tile.isMountain() && !td.isMountain() && !tl.isMountain()) {
                        buffer[y][x + 1] = '▀';
                        col += 3;
                    } else if (tu.isMountain() && !tr.isMountain() && !tile.isMountain() && !td.isMountain() && tl.isMountain()) {
                        buffer[y][x] = '▀';
                        col += 3;
                    } else if (!tu.isMountain() && tr.isMountain() && !tile.isMountain() && td.isMountain() && !tl.isMountain()) {
                        buffer[y][x + 1] = '▄';
                        col += 3;
                    } else if (!tu.isMountain() && !tr.isMountain() && !tile.isMountain() && td.isMountain() && tl.isMountain()) {
                        buffer[y][x] = '▄';
                        col += 3;
                    } else if (!tu.isMountain() && tr.isMountain() && !tile.isMountain() && td.isMountain() && tl.isMountain()) {
                        buffer[y][x] = '▄';
                        buffer[y][x + 1] = '▄';
                        col += 3;
                    } else if (tu.isMountain() && tr.isMountain() && !tile.isMountain() && !td.isMountain() && tl.isMountain()) {
                        buffer[y][x] = '▀';
                        buffer[y][x + 1] = '▀';
                        col += 3;
                    } else if (tu.isWater() && tr.isWater() && !tile.isWater() && !td.isWater() && !tl.isWater()) {
                        buffer[y][x + 1] = '▀';
                        col += 4;
                    } else if (tu.isWater() && !tr.isWater() && !tile.isWater() && !td.isWater() && tl.isWater()) {
                        buffer[y][x] = '▀';
                        col += 4;
                    } else if (!tu.isWater() && tr.isWater() && !tile.isWater() && td.isWater() && !tl.isWater()) {
                        buffer[y][x + 1] = '▄';
                        col += 4;
                    } else if (!tu.isWater() && !tr.isWater() && !tile.isWater() && td.isWater() && tl.isWater()) {
                        buffer[y][x] = '▄';
                        col += 4;
                    } else if (!tu.isWater() && tr.isWater() && !tile.isWater() && td.isWater() && tl.isWater()) {
                        buffer[y][x] = '▄';
                        buffer[y][x + 1] = '▄';
                        col += 4;
                    } else if (tu.isWater() && tr.isWater() && !tile.isWater() && !td.isWater() && tl.isWater()) {
                        buffer[y][x] = '▀';
                        buffer[y][x + 1] = '▀';
                        col += 4;
                    }
                }

                color[y][x] = col;
                color[y][x + 1] = col;
            }
        }


        return buffer;
    }
}
