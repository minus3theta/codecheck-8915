package codecheck;

import java.util.LinkedList;
import java.io.InputStream;

public class App {
    public static final String[] AI_NAME = {"FIRST", "SECOND"};

	public static void main(String[] args) {
        if(args.length < 3) {
            throw new IllegalArgumentException("At least 3 argments are required");
        }
        final String[] AI_COMMAND = {args[0], args[1]};
        for(String s: AI_COMMAND) {
            System.out.println(s);
        }
        LinkedList<String> words = new LinkedList<String>();
        for(int i=3; i<args.length; i++) {
            words.addLast(args[i]);
        }
        String currentWord = args[2];
        int turn = 0;

        while(true) {
            words.addFirst(AI_COMMAND[turn]);
            ProcessBuilder aiPb = new ProcessBuilder(words);
            words.removeFirst();
            try {
                Process aiProcess = aiPb.start();
                aiProcess.waitFor();
                InputStream aiOutput = aiProcess.getInputStream();
            } catch(Exception e) {
                e.printStackTrace();
            }

            turn = 1 - turn;
        }
	}

    private long first(String s) {
        return s.charAt(0);
    }

    private long last(String s) {
        return s.charAt(s.length() - 1);
    }
}
