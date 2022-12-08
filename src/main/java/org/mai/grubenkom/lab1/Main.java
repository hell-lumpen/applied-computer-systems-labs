package org.mai.grubenkom.lab1;

public class Main {
    public static void main(String[] args) {
        BracketChecker checker = new BracketChecker("src/main/resources/brackets.txt");
        checker.checkFile("src/main/resources/file.txt");
    }
}