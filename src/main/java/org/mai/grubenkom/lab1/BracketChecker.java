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
    private Character expectedCharacter;
}

enum ErrorStatus {
    INCORRECT_OPEN_BRACKET,
    EXTRA_BRACKET,
    EXTRA_OPEN_BRACKET,
    OK
}


@Data
@AllArgsConstructor
class NewError {
    private Bracket incorrectBracket;
    private ErrorStatus errorStatus;
    NewError() {
        this.incorrectBracket = null;
        this.errorStatus = ErrorStatus.OK;
    }
}


@Data
@AllArgsConstructor
class Bracket {
    private char bracket;
    private long positionOfBracket;
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
        if (stack.isEmpty())
            return new Error(-1, ' ');
        else
            return new Error(index, getClosingBracket(stack.peek()));
    }

    public NewError checkBracketSequenceNew (String line) {
        Deque<Bracket> stack = new ArrayDeque<>();
        long index = 0;
        for (char c : line.toCharArray()) {
            if (bracketsMap.containsValue(c)) {
                if (!stack.isEmpty()){
                    if (bracketsMap.get(c) != null && bracketsMap.get(c) == stack.peek().getBracket()) {
                        stack.pop();
                        index++;
                        continue;
                    }
                }
                stack.addFirst(new Bracket(c, index));
            }
            else if (bracketsMap.containsKey(c)) {
                if (!stack.isEmpty()) {
                    Bracket poppedBracket = stack.pop();
                    if (poppedBracket.getBracket() != bracketsMap.get(c)) {
                        return new NewError(poppedBracket, ErrorStatus.INCORRECT_OPEN_BRACKET);
                    }
                }
                else {
                    return new NewError(new Bracket(c, index), ErrorStatus.EXTRA_BRACKET);
                }
            }

            index++;
        }
        if (stack.isEmpty()) {
            return new NewError();
//                todo: последовательность ок
        }
        else {
            return new NewError(stack.pop(), ErrorStatus.INCORRECT_OPEN_BRACKET);
//                todo: прокинуть ошибки что в начале лишние скобки
        }
    }

    private void printErrorNew (NewError error) {
        if (error.getErrorStatus() != ErrorStatus.OK) {
            Bracket incorrectBracket = error.getIncorrectBracket();
            long indexOfError = incorrectBracket.getPositionOfBracket();
            while (indexOfError-- != 0) {
                System.out.print(" ");
            }
            if (error.getErrorStatus() == ErrorStatus.EXTRA_BRACKET)
                System.out.println("^ - ErrorStatus.EXTRA_BRACKET");
            if (error.getErrorStatus() == ErrorStatus.EXTRA_OPEN_BRACKET)
                System.out.println("^ - ErrorStatus.EXTRA_OPEN_BRACKET");
            if (error.getErrorStatus() == ErrorStatus.INCORRECT_OPEN_BRACKET)
                System.out.println("^ - ErrorStatus.INCORRECT_OPEN_BRACKET");
        } else
            System.out.println("Correct bracket sequence");
    }

    private void printError (Error error) {
        int indexOfError = error.getIndexOfError();
        while (indexOfError-- != 0) {
            System.out.print(" ");
        }
        if (error.getExpectedCharacter() != ' ')
            System.out.printf("^ - expected '%c'",error.getExpectedCharacter());
        else
            System.out.print("^ - incorrect symbol");
    }

    public void checkFile(String pathToCheckedFile) {
        try (FileReader fileReader = new FileReader(pathToCheckedFile);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String line = bufferedReader.readLine();
            while (line != null) {
                System.out.println("------------------------------");
                Error error = checkBracketSequence(line);
                System.out.println(line);
                if (error.getIndexOfError() != -1) {
                    printError(error);
                    System.out.println();
                }
                else {
                    System.out.println("Correct bracket sequence");
                }

                NewError newError = checkBracketSequenceNew(line);
                printErrorNew(newError);

                line = bufferedReader.readLine();
            }

        } catch (IOException exception) {
            System.err.println(exception.getMessage());
        }
    }
}