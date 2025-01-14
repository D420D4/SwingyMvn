package org.plefevre.Controller;

public class Tools {
    public static String arrToString(int[] arr) {
        if (arr == null || arr.length == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < arr.length; i++) {
            sb.append(arr[i]);
            if (i < arr.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public static char[][] deepCopyCharArray(char[][] original) {
        if (original == null) return null;

        char[][] copy = new char[original.length][];

        for (int i = 0; i < original.length; i++) copy[i] = original[i].clone();

        return copy;
    }
    public static byte[][] deepCopyByteArray(byte[][] original) {
        if (original == null) return null;

        byte[][] copy = new byte[original.length][];

        for (int i = 0; i < original.length; i++) copy[i] = original[i].clone();

        return copy;
    }

    public static byte[] sub(byte[] by, int s, int f) {
        byte[] b = new byte[f-s];

        for (int i = 0; i < b.length; i++) {
            b[i] = by[s+i];
        }
        return b;
    }
}

