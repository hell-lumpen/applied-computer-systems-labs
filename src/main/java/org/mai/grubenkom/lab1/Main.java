package org.mai.grubenkom.lab1;

import org.mai.grubenkom.lab1.BracketChecker;

public class Main {
    public static void main(String[] args) {
        BracketChecker checker = new BracketChecker("src/main/resources/brackets.txt");
//        checker.printBracketSet();
        checker.checkFile("src/main/resources/file.txt");
    }
}