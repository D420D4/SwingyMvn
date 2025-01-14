package org.plefevre.Model;

import org.plefevre.Controller.ConstructLog;

import java.util.ArrayList;
import java.util.Arrays;

public class Log {
    private ArrayList<String> output = new ArrayList<>();
    private ArrayList<byte[]> colors = new ArrayList<>();

    public void addSimpleText(String txt) {
        addTextColor(txt, (byte) 0);
    }

    public void addTextColor(String txt, byte col) {
        byte[] arr = new byte[txt.length()];

        Arrays.fill(arr, col);

        output.add(txt);
        colors.add(arr);
    }

    public void addTextColorWord(String txt, String col) {
        byte[] arr = new byte[txt.length()];
        Arrays.fill(arr, (byte) 0);

        String[] cols = col.split(" ");
        int id = 0;
        byte curColor;

        curColor = Byte.parseByte(cols[id]);
        for (int i = 0; i < txt.length(); i++) {
            arr[i] = curColor;
            if (txt.charAt(i) == ' ' && i > 0 && txt.charAt(i - 1) != ' ') {
                id++;
                if (id < cols.length)
                    curColor = Byte.parseByte(cols[id]);
            }
        }

        output.add(txt);
        colors.add(arr);

    }

    public ArrayList<String> getOutput() {
        return output;
    }

    public ArrayList<byte[]> getColors() {
        return colors;
    }

    public void addTextColor(ConstructLog log) {
        addTextColor(log.getString(), log.getColor());
    }
    public void addTextColor(String txt, byte[] col) {
        output.add(txt);
        colors.add(col);
    }

    public void clear() {
        output.clear();
        colors.clear();
    }
}
