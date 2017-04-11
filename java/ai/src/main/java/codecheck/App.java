package codecheck;

import java.util.LinkedList;
import java.util.HashMap;

public class App {
    public static void main(String[] args) {
        if(args.length < 1) {
            throw new IllegalArgumentException("At least 1 argument is required");
        }
        int currentLast = last(args[0]);
        LinkedList<String> words = new LinkedList<String>();
        for(int i=1; i<args.length; i++) {
            words.addLast(args[i]);
        }
        HashMap<Integer, HashMap<Integer, Integer>> graph =
            new HashMap<Integer, HashMap<Integer, Integer>>();
        for(String s: words) {
            int first = first(s);
            int last = last(s);
            HashMap<Integer, Integer> val = graph.get(first);
            if(val == null) {
                val = new HashMap<Integer, Integer>();
                graph.put(first, val);
            }
            Integer count = val.get(last);
            if(count == null) {
                count = 0;
            }
            val.put(last, count + 1);
        }
        for(String s: words) {
            if(first(s) == currentLast) {
                System.out.println(s);
                return;
            }
        }
    }

    private static int first(String s) {
        return s.codePointAt(0);
    }

    private static int last(String s) {
        int l = s.length();
        if(l < 2) {
            return s.codePointAt(l - 1);
        }
        if(Character.isSurrogatePair(s.charAt(l - 2), s.charAt(l - 1))) {
            return s.codePointAt(l - 2);
        }
        return s.codePointAt(l - 1);
    }
}
