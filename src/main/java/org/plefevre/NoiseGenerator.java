package org.plefevre;

import java.util.Random;

public class NoiseGenerator {
    private final int[] permutation;

    public NoiseGenerator(int seed) {
        Random random = new Random(seed);
        permutation = new int[512];
        int[] p = new int[256];

        // Générer une permutation initiale
        for (int i = 0; i < 256; i++) {
            p[i] = i;
        }

        // Mélanger la permutation avec le seed
        for (int i = 0; i < 256; i++) {
            int swap = random.nextInt(256);
            int temp = p[i];
            p[i] = p[swap];
            p[swap] = temp;
        }

        // Dupliquer la permutation pour éviter des boucles dans le calcul du bruit
        for (int i = 0; i < 256; i++) {
            permutation[i] = p[i];
            permutation[256 + i] = p[i];
        }
    }

    // Fonction pour générer du bruit à partir de coordonnées x, y
    public double perlin(double x, double y) {
        // Trouver la position de la grille
        int xi = (int) Math.floor(x) & 255;
        int yi = (int) Math.floor(y) & 255;

        // Calculer les points flottants internes dans la cellule de la grille
        double xf = x - Math.floor(x);
        double yf = y - Math.floor(y);

        // Calculer des vecteurs de lissage
        double u = fade(xf);
        double v = fade(yf);

        // Extraire les vecteurs gradients pour chaque coin de la cellule
        int aa = permutation[permutation[xi] + yi];
        int ab = permutation[permutation[xi] + yi + 1];
        int ba = permutation[permutation[xi + 1] + yi];
        int bb = permutation[permutation[xi + 1] + yi + 1];

        // Interpoler les gradients
        double x1, x2, y1;
        x1 = lerp(grad(aa, xf, yf), grad(ba, xf - 1, yf), u);
        x2 = lerp(grad(ab, xf, yf - 1), grad(bb, xf - 1, yf - 1), u);
        y1 = lerp(x1, x2, v);

        // Retourner la valeur du bruit dans l'intervalle [-1, 1]
        return (y1 + 1) / 2;
    }

    // Fonction d'interpolation linéaire
    private double lerp(double a, double b, double t) {
        return a + t * (b - a);
    }

    // Fonction pour lisser les transitions entre les valeurs (courbe de fade de Perlin)
    private double fade(double t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    // Fonction gradient pour calculer l'influence de chaque vecteur de gradient
    private double grad(int hash, double x, double y) {
        int h = hash & 7;
        double u = h < 4 ? x : y;
        double v = h < 4 ? y : x;
        return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
    }
}

