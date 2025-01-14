package org.plefevre.Controller;

import java.util.ArrayList;
import java.util.Arrays;

public class ConstructLog {
    ArrayList<String> text = new ArrayList<>();
    ArrayList<byte[]> color = new ArrayList<>();

    public void clean() {
        text.clear();
        color.clear();
    }


    public void add(String txt, byte c) {
        byte[] b = new byte[txt.length()];
        Arrays.fill(b, c);
        text.add(txt);
        color.add(b);
    }


    public String getString() {
        StringBuilder s = new StringBuilder();
        for (String string : text) s.append(string);
        return s.toString();
    }

    public byte[] getColor() {
        int l = 0;
        for (int i = 0; i < color.size(); i++) {
            l += color.get(i).length;
        }

        byte[] b = new byte[l];

        int ind = 0;
        for (int i = 0; i < color.size(); i++) {
            for (int j = 0; j < color.get(i).length; j++) {
                b[ind + j] = color.get(i)[j];
            }
            ind += color.get(i).length;
        }

        return b;
    }
}
