package org.mai.grubenkom;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class BracketChecker {
    private final HashMap<Character, Character> bracketsMap;
    private final String pathToCheckedFile;
    private Deque<Character> stack;
    BracketChecker(String pathToConfigFile, String pathToCheckedFile) {
        bracketsMap = new HashMap<>();
        this.configure(pathToConfigFile);
        this.pathToCheckedFile = pathToCheckedFile;
    }
    private void configure(String pathToConfigFile) {
        try (FileReader fileReader = new FileReader(pathToConfigFile);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String line = bufferedReader.readLine();
            while (line != null) {
                bracketsMap.put(line.charAt(0), line.charAt(1));
                line = bufferedReader.readLine();
            }
        }
        catch (IOException exception) {
            System.err.println(exception.getMessage());
        }
    }
    public void printBracketSet() {
        bracketsMap.forEach((key, value) -> System.out.println(key + " " + value));
    }

    private int checkBracketSequence (String line) {
        stack = new ArrayDeque<>();
        try {
            for (char c: line.toCharArray()) {
                if (bracketsMap.containsKey(c)) {
                    stack.addLast(c);
                } else if(bracketsMap.containsValue(c)){
                    // TODO: 02.12.2022 доделать
                }
            }
        } catch (NullPointerException exception) {
            System.err.println(exception.getMessage());
        }
        return 2;
    }

    private void printError (int indexOfError) {
        while (indexOfError-- != 0) {
            System.err.print(" ");
        }
        System.err.print("^");
        System.err.println("\nIncorrect bracket sequence");
    }

    public void checkFile() {
        try (FileReader fileReader = new FileReader(pathToCheckedFile);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String line = bufferedReader.readLine();
            int index = -1;
            while (line != null) {
                index = checkBracketSequence(line);
                System.out.println(line);
                if (index != -1) {
                    printError(index);
                }
                line = bufferedReader.readLine();
            }

        } catch (IOException exception) {
            System.err.println(exception.getMessage());
        }
    }
}