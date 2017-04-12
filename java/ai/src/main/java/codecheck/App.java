package codecheck;

import java.util.LinkedList;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.OptionalInt;
import java.util.Random;

public class App {
    private static final int SIZE_THRESHOLD = 50;
    private static final int SEARCH_MAX = 5000;

    public static void main(String[] args) {
        if(args.length < 1) {
            throw new IllegalArgumentException("At least 1 argument is required");
        }
        int currentLast = last(args[0]);
        LinkedList<String> words = new LinkedList<>();
        for(int i=1; i<args.length; i++) {
            words.addLast(args[i]);
        }
        HashMap<Integer, HashMap<Integer, Integer>> graph = new HashMap<>();
        for(String s: words) {
            int first = first(s);
            int last = last(s);
            HashMap<Integer, Integer> val =
                graph.computeIfAbsent(first, k -> new HashMap<>());
            int count = val.getOrDefault(last, 0);
            val.put(last, count + 1);
        }
        // show(graph);
        HashMap<HashMap<Integer, HashMap<Integer, Integer>>, Boolean> state =
            new HashMap<>();
        int hand = (words.size() < SIZE_THRESHOLD ?
                    win(currentLast, graph, state) : OptionalInt.empty())
            .orElseGet(() -> {
                    HashMap<Integer, Integer> val = graph.get(currentLast);
                    if(val == null) return 0;
                    Set<Integer> keys = val.keySet();
                    return (int)keys.toArray()[(new Random()).nextInt(keys.size())];
                });
        for(String s: words) {
            if(first(s) == currentLast && last(s) == hand) {
                System.out.println(s);
                return;
            }
        }
    }

    private static String cpToString(int cp) {
        return new String(new int[]{cp}, 0, 1);
    }

    private static void show(HashMap<Integer, HashMap<Integer, Integer>> graph) {
        graph.forEach((fst,m) ->
                      m.forEach((lst,cnt) ->
                                System.err.println(new String(new int[]{fst}, 0, 1) +
                                                   " -> " +
                                                   new String(new int[]{lst}, 0, 1) + " : " + cnt)));
    }

    private static OptionalInt win(int current, HashMap<Integer, HashMap<Integer, Integer>> graph,
                                   HashMap<HashMap<Integer, HashMap<Integer, Integer>>, Boolean> state) {
        // System.err.println("win: " + cpToString(current));
        HashMap<Integer, Integer> candidate = graph.get(current);
        if(candidate == null) {
            return OptionalInt.empty();
        }
        for(Map.Entry<Integer, Integer> e: candidate.entrySet()) {
            if(state.size() > SEARCH_MAX) return OptionalInt.empty();
            int last = e.getKey();
            int count = e.getValue();
            if(count == 0) {
                continue;
            }
            HashMap<Integer, HashMap<Integer, Integer>> newGraph =
                deepClone(graph);
            HashMap<Integer, Integer> newCandidate = newGraph.get(current);
            newCandidate.compute(last, (k,c) -> c - 1);
            boolean opLose = state
                .computeIfAbsent(newGraph, k ->
                                 lose(last, newGraph, state));
            if(opLose) {
                return OptionalInt.of(last);
            }
        }
        return OptionalInt.empty();
    }

    private static boolean lose(int current, HashMap<Integer, HashMap<Integer, Integer>> graph,
                                HashMap<HashMap<Integer, HashMap<Integer, Integer>>, Boolean> state) {
        // System.err.println("lose: " + cpToString(current));
        HashMap<Integer, Integer> candidate = graph.get(current);
        if(candidate == null) {
            return true;
        }
        for(Map.Entry<Integer, Integer> e: candidate.entrySet()) {
            if(state.size() > SEARCH_MAX) return false;
            int last = e.getKey();
            int count = e.getValue();
            if(count == 0) {
                continue;
            }
            HashMap<Integer, HashMap<Integer, Integer>> newGraph =
                deepClone(graph);
            HashMap<Integer, Integer> newCandidate = newGraph.get(current);
            newCandidate.compute(last, (k,c) -> c - 1);
            boolean opWin = state
                .computeIfAbsent(newGraph, k ->
                                 win(last, newGraph, state).isPresent());
            if(!opWin) {
                return false;
            }
        }
        return true;
    }

    private static HashMap<Integer, HashMap<Integer, Integer>>
        deepClone(HashMap<Integer, HashMap<Integer, Integer>> graph) {
        HashMap<Integer, HashMap<Integer, Integer>> ret = new HashMap<>();
        graph.forEach((k,v) -> ret.put(k,(HashMap<Integer, Integer>)v.clone()));
        return ret;
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
