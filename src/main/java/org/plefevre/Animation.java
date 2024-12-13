package org.plefevre;

public class Animation {

    public static void circleAnimation(char[][] buffer, byte[][] color, int a) {
        int w2 = buffer[0].length / 2;
        int h2 = buffer.length / 2;

        for (int i = 0; i < buffer.length; i++) {
            for (int j = 0; j < buffer[i].length; j++) {
                int dis_cen = (int) Math.sqrt(Math.pow((i - h2)*2, 2) + Math.pow(j - w2, 2));
                if (dis_cen > a) {
                    buffer[i][j] = ' ';
                    color[i][j] = 0;
                }
            }
        }
    }
}
