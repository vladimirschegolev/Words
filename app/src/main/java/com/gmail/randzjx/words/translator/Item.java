package com.gmail.randzjx.words.translator;

import java.util.ArrayList;

public class Item {
    Item[] array;
    String string;

    public Item(String input) {
        String[] split = splitter(input);
        if (split.length == 1) {
            if (split[0].equals("null")) {
                string = "n";
            } else if (split[0].startsWith("\"") && split[0].endsWith("\"")) {
                string = split[0].substring(1, split[0].length() - 1);
            } else if (split[0].startsWith("[") && split[0].endsWith("]")) {
                array = new Item[]{new Item(split[0])};
            } else {
                string = split[0];
            }
        } else {
            array = new Item[split.length];
            for (int i = 0; i < split.length; i++) {
                array[i] = new Item(split[i]);
            }
        }

    }

    private String[] splitter(String in) {
        if (in.startsWith("[") && in.endsWith("]")) {
            in = in.substring(1, in.length() - 1);
            ArrayList<Integer> commas = new ArrayList<>();
            boolean quotes = false;
            for (int i = 0, depth = 0; i < in.length(); i++) {
                switch (in.charAt(i)) {
                    case '"':
                        quotes = !quotes;
                        break;
                    case '[':
                        depth++;
                        break;
                    case ']':
                        depth--;
                        break;
                    case ',':
                        if (depth == 0 && !quotes) {
                            commas.add(i);
                        }
                }
            }
            if (commas.size() > 0) {
                String[] out = new String[commas.size() + 1];
                int prev = 0;
                for (int i = 0; i < commas.size(); i++) {
                    out[i] = in.substring(prev, commas.get(i));
                    prev = commas.get(i) + 1;
                }
                out[out.length - 1] = in.substring(prev);
                return out;
            }
        }
        return new String[]{in};
    }


    public String[] get(int[] in, int depth) {
        if (depth < in.length && array[in[depth]] != null) {
            return array[in[depth]].get(in, ++depth);
        }
        if (array != null) {
            String[] out = new String[array.length];
            for (int i = 0; i < array.length; i++) {
                out[i] = array[i].toString();
            }
            return out;
        }
        return new String[]{string};
    }

    public boolean isEmpty() {
        return array == null;
    }

    @Override
    public String toString() {
        if (isEmpty()) return string;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            sb.append(array[i]).append(" ");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public int size() {
        if (array == null) {
            return 1;
        } else {
            return array.length;
        }
    }
}
