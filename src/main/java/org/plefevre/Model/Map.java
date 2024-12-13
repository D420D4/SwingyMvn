package org.plefevre.Model;

import org.plefevre.Game;
import org.plefevre.NoiseGenerator;

import java.util.Random;

import static java.lang.Math.abs;

public class Map {
    int size;
    int lvl;
    public Tile[][] tiles;

    public static final double PROBA_GEN_MONSTER = 0.03 * 0.001;

    Random random;
    NoiseGenerator noise;

    public Map(int lvl, int seed) {
        this.lvl = lvl;
        size = (lvl - 1) * 5 + 10 - 1;
        tiles = new Tile[size][size];
        generateMap(seed);
    }


    public void generateMap(int seed) {
        random = new Random(seed);
        regenerate();
    }

    public void regenerate() {
        noise = new NoiseGenerator(random.nextInt());
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {

                Tile tile = new Tile();

                double noiseValue = noise.perlin(i * 0.1, j * 0.1);
                tile.v = (int) (noiseValue * 10);
                if (noiseValue > 0.6) {
                    tile.mountain = true;
                } else if (noiseValue < 0.3) {
                    tile.water = true;
                }

                if (random.nextDouble() < 0.05) {
                    tile.type_tile = 1 + random.nextInt(4);
                } else
                    tile.type_tile = 0;


                boolean monsterAround = false;
                for (int k = -1; k < 2; k++) {
                    for (int l = -1; l < 2; l++) {
                        if (i + k < 0 || i + k >= tiles.length || j + l < 0 || j + l >= tiles[i].length)
                            continue;
                        if (tiles[i + k][j + l] != null && tiles[i + k][j + l].monster != null)
                            monsterAround = true;

                    }
                }

                if (!monsterAround && !tile.water && random.nextDouble() < PROBA_GEN_MONSTER) {
                    tile.monster = new Monster(lvl - 2 + random.nextInt(5) + (tile.mountain ? 2 : 0));  // Fonction pour générer des monstres aléatoires
                }

                tiles[i][j] = tile;
            }
        }
    }

    public void centerHero(Hero hero) {
        hero.x = size / 2;
        hero.y = size / 2;

        int maxGen = 20;
        while (tiles[hero.y][hero.x].water && maxGen-- > 0) {
            regenerate();
        }

        if (maxGen == 0) {
            System.err.println("ERREUR, CANT FOUND MAP FOR THIS SEED.");
            System.exit(1);
        }

        for (int k = -2; k < 3; k++) {
            for (int l = -2; l < 3; l++) {
                if (hero.y + k < 0 || hero.y + k >= tiles.length || hero.x + l < 0 || hero.x + l >= tiles[hero.y].length)
                    continue;
                if (tiles[hero.y + k][hero.x + l] != null)
                    tiles[hero.y + k][hero.x + l].monster = null;
            }
        }
    }

    public Tile getTile(int x, int y) {
        if (x < 0 || y < 0 || x >= size || y >= size)
            return null;
        return tiles[y][x];
    }

    public void moveMonster() {

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                tiles[i][j].movedMonster = false;
            }
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (tiles[i][j].monster != null && !tiles[i][j].movedMonster)
                    moveMonster(j, i);
            }
        }
    }

    private void moveMonster(int x, int y) {
        int nx = x;
        int ny = y;

        Hero hero = Game.game.hero;
        if (Math.sqrt(Math.pow(hero.x - x, 2) + Math.pow(hero.y - y, 2)) < 5) {
            int dx = hero.x - x;
            int dy = hero.y - y;

            if (abs(dx) > abs(dy))
                nx += dx > 0 ? 1 : -1;
            else
                ny += dy > 0 ? 1 : -1;
        } else {
            int mv = (int) (Math.random() * 4);
            if (mv == 0) nx++;
            if (mv == 1) nx--;
            if (mv == 2) ny++;
            if (mv == 3) ny--;
        }
        Map.Tile tileDepart = getTile(x, y);
        Map.Tile tile = getTile(nx, ny);
        if (tile == null || tile.water || tileDepart.mountain ^ tile.mountain || tile.monster != null)
            return;

        tile.monster = tileDepart.monster;
        tile.movedMonster = true;
        tileDepart.monster = null;

    }

    public int getSize() {
        return size;
    }

    public class Tile {
        boolean water = false;
        boolean mountain = false;
        int type_tile = 0;
        boolean movedMonster = false;
        Monster monster = null;
        int v;


        public boolean isWater() {
            return water;
        }

        public boolean isMountain() {
            return mountain;
        }

        public int getType_tile() {
            return type_tile;
        }

        public boolean isMovedMonster() {
            return movedMonster;
        }

        public Monster getMonster() {
            return monster;
        }

        public int getV() {
            return v;
        }

        public void setMonster(Monster monster) {
            this.monster = monster;
        }
    }
}
