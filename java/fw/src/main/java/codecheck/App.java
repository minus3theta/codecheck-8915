package codecheck;

import java.util.HashMap;

class MultiSet<S> {
    private HashMap<S, Integer> map;
    public MultiSet() {
        map = new HashMap<S, Integer>();
    }
    public void add(S elem) {
        int num = map.containsKey(elem) ? map.get(elem) : 0;
        map.put(elem, num + 1);
    }
    public void remove(S elem) {
        if(!map.containsKey(elem)) return;
        int num = map.get(elem);
        if(num == 1) {
            map.remove(elem);
        } else {
            map.put(elem, num - 1);
        }
    }
    public int getCount(S elem) {
        int num = map.containsKey(elem) ? map.get(elem) : 0;
        return num;
    }
}

public class App {
    public static final String[] AI_NAME = {"FIRST", "SECOND"};

	public static void main(String[] args) {
        if(args.length < 3) {
            throw new IllegalArgumentException("At least 3 argments are required");
        }
        String[] aiCommand = {args[0], args[1]};
        for(String s: aiCommand) {
            System.out.println(s);
        }
        MultiSet<String> words = new MultiSet<String>();
        for(int i=3; i<args.length; i++) {
            words.add(args[i]);
        }
        String currentWord = args[2];
        int turn = 0;
	}
}
