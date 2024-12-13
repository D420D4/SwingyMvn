package org.plefevre;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class TerminalSize {

    public static int[] getTerminalSize() {
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "stty size </dev/tty"});
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String[] size = reader.readLine().split(" ");
            int rows = Integer.parseInt(size[0]) - 1;//TODO remove -1
            int columns = Integer.parseInt(size[1]);
            return new int[]{rows, columns};
        } catch (Exception e) {
            e.printStackTrace();
            return new int[]{0, 0};  // Valeur par défaut si la taille n'est pas récupérable
        }
    }
}