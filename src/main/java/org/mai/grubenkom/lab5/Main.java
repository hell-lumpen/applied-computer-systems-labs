package org.mai.grubenkom.lab5;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("path to log file:");
        String pathToFile = scanner.nextLine();
        System.out.println("deviation:");
        long deviation = scanner.nextLong();
        LogAnalyzer.analyze(pathToFile, deviation);
    }
}
