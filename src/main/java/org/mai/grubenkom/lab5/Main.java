package org.mai.grubenkom.lab5;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        LogAnalyzer.analyze("src/main/resources/log.txt", 5);
//        final Pattern startOfQueryPattern
//                = Pattern.compile("(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}) – INFO – QUERY FOR ID = (\\d)");
//        final Pattern endOfQueryPattern
//                = Pattern.compile("(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}) – INFO – RESULT QUERY FOR ID = (\\d)");
//
//        Matcher startMatcher = startOfQueryPattern.matcher("2019-09-01 00:05:03 – IFO – QUERY FOR ID = 1");
//        Matcher endMatcher = endOfQueryPattern.matcher("2019-09-01 00:05:04 – INF – RESULT QUERY FOR ID = 1");
//        if (startMatcher.find()) {
//            System.out.println(startMatcher.group(1));
//            System.out.println(startMatcher.group(2));
//        }
//        if (endMatcher.find()) {
//            System.out.println(endMatcher.group(1));
//            System.out.println(endMatcher.group(2));
//        } else {
//            System.out.println("Неверная строка");
//        }
    }
}
