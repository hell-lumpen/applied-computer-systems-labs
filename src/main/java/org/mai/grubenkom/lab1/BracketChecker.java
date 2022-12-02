package org.mai.grubenkom.lab1;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Data
@AllArgsConstructor
class Error {
    private int indexOfError;
    private char expectedCharacter;
}

public class BracketChecker {
    private final HashMap<Character, Character> bracketsMap;
    BracketChecker(String pathToConfigFile) {
        bracketsMap = new HashMap<>();
        this.configure(pathToConfigFile);
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
        bracketsMap.forEach((key, value) -> System.out.println(value + " " + key));
    }

    private Character getClosingBracket(Character character) {
        for (Map.Entry<Character, Character> entry : bracketsMap.entrySet()) {
            if (Objects.equals(character, entry.getValue())) {
                return entry.getKey();
            }
        }
        return ' ';
    }

    private Error checkBracketSequence (String line) {
        Deque<Character> stack = new ArrayDeque<>();
        int index = 0;
        try {
            for (char c: line.toCharArray()) {
                if (bracketsMap.containsValue(c)) {

                    if (!stack.isEmpty()){
                        if (bracketsMap.get(c) == stack.peek()) {
                            stack.pop();
                            index++;
                            continue;
                        }
                    }
                    stack.addFirst(c);
                } else if(bracketsMap.containsKey(c)){
                    if (!stack.isEmpty()) {
                        Character pop = stack.pop();
                        if (pop != bracketsMap.get(c)) {
                            return new Error(index, getClosingBracket(pop));
                        }
                    } else {
                        return new Error(index, ' ');
                    }
                }
                index++;
            }
        } catch (NullPointerException exception) {
            System.err.println(exception.getMessage());
        }
        if (stack.isEmpty())
            return new Error(-1, ' ');
        else
            return new Error(index, getClosingBracket(stack.peek()));
    }

    private void printError (Error error) {
        int indexOfError = error.getIndexOfError();
        while (indexOfError-- != 0) {
            System.out.print(" ");
        }
        if (error.getExpectedCharacter() != ' ')
            System.out.printf("^ - expected '%c'",error.getExpectedCharacter());
        else
            System.out.println("^ - incorrect symbol");
        System.out.println("\nIncorrect bracket sequence");
    }

    public void checkFile(String pathToCheckedFile) {
        try (FileReader fileReader = new FileReader(pathToCheckedFile);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String line = bufferedReader.readLine();
            int index;
            while (line != null) {
                Error error = checkBracketSequence(line);
                System.out.println(line);
                if (error.getIndexOfError() != -1) {
                    printError(error);
                }
                else {
                    System.out.println("Correct bracket sequence");
                }
                line = bufferedReader.readLine();
            }

        } catch (IOException exception) {
            System.err.println(exception.getMessage());
        }
    }
}