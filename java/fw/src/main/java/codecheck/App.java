package codecheck;

import java.util.LinkedList;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public class App {
    public static final String[] AI_NAME = {"FIRST", "SECOND"};
    public static final int TIMELIMIT = 2;

	public static void main(String[] args) {
        if(args.length < 3) {
            throw new IllegalArgumentException("At least 3 argments are required");
        }
        final String[] AI_COMMAND = {args[0], args[1]};
        LinkedList<String> words = new LinkedList<String>();
        for(int i=3; i<args.length; i++) {
            words.addLast(args[i]);
        }
        String currentWord = args[2];
        int turn = 0;

        while(true) {
            words.addFirst(currentWord);
            words.addFirst(AI_COMMAND[turn]);
            String hand = runAi(words);
            words.removeFirst();
            words.removeFirst();

            boolean exist = words.remove(hand);
            boolean ok = exist && last(currentWord) == first(hand);
            System.out.println(AI_NAME[turn] + " (" +
                               (ok ? "OK" : "NG") + "): " + hand);
            turn = 1 - turn;
            currentWord = hand;
            if(!ok) break;
        }
        System.out.println("WIN - " + AI_NAME[turn]);
	}

    private static String runAi(LinkedList<String> command) {
        ProcessBuilder pb = new ProcessBuilder(command);
        try {
            Process p = pb.start();
            BufferedReader reader =
                new BufferedReader(new InputStreamReader(p.getInputStream()));
            boolean end = p.waitFor(TIMELIMIT, TimeUnit.SECONDS);
            if(end) {
                String ret = reader.readLine();
                return ret != null ? ret : "";
            }
            p.destroy();
            return "";
        } catch(Exception e) {
            e.printStackTrace();
        }
        return "";
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
