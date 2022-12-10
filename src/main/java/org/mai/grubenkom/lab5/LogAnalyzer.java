package org.mai.grubenkom.lab5;

import lombok.Data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Data
class QueryInfo {
    private long queryId;
    private Date startTime;
    private Date endTime;
    public long getExecutionTime() {
        return TimeUnit.SECONDS.convert(endTime.getTime() - startTime.getTime(), TimeUnit.MILLISECONDS);
    }
    public QueryInfo(long queryId, Date startTime) {
        this.queryId = queryId;
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        return "QueryInfo {" +
                "queryId = " + queryId + ", " +
                "duration=" + getExecutionTime() +
                '}';
    }
}

public class LogAnalyzer {
    private static final Pattern startOfQueryPattern
        = Pattern.compile("(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}) – INFO – QUERY FOR ID = (\\d*)");
    private static final Pattern endOfQueryPattern
            = Pattern.compile("(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}) – INFO – RESULT QUERY FOR ID = (\\d*)");
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void analyze(String pathToLogFile, long deviationInSeconds) {
        try (FileReader fileReader = new FileReader(pathToLogFile);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            File file = new File(pathToLogFile);
            long fileSize = file.length();
            long currentSize = 0;
            long lineNumber = 0;

            ArrayList<QueryInfo> queryInfoList = new ArrayList<>();
            String line;
            while ((line = bufferedReader.readLine()) != null) {

                lineNumber++;
                currentSize += line.length() + 6;

                Matcher startMatcher = startOfQueryPattern.matcher(line);
                Matcher endMatcher = endOfQueryPattern.matcher(line);
                if (startMatcher.find()) {
                    queryInfoList.add(new QueryInfo(Long.parseLong(startMatcher.group(2)), dateFormat.parse(startMatcher.group(1))));
                }
                else if (endMatcher.find()) {
                    long queryId = Long.parseLong(endMatcher.group(2));
                    Optional<QueryInfo> queryInfo = queryInfoList
                                                                .stream()
                                                                .filter(query -> query.getQueryId() == queryId)
                                                                .findFirst();
                    if (queryInfo.isPresent()) {
                        queryInfo.get().setEndTime(dateFormat.parse(endMatcher.group(1)));
                    } else {
                        System.err.println("Завершение запроса до начала QUERY ID: " + queryId);
                    }
                } else {
                    System.err.println("Неверная строка: " + line);
                }
                Thread.sleep(100);
                printProgress(currentSize, fileSize, lineNumber);
            }
            List<QueryInfo> sortedQueryInfoList = queryInfoList
                    .stream()
                    .sorted(Comparator.comparingLong(QueryInfo::getExecutionTime))
                    .toList();
            System.out.println("Все запросы (отсортированные по возрастанию времени выполнения):");
            sortedQueryInfoList.forEach(System.out::println);

            double medianExecutionTime;
            int size = queryInfoList.size();
            if (queryInfoList.size() % 2 == 0) {
                medianExecutionTime = (double) (sortedQueryInfoList.get(size / 2 - 1).getExecutionTime() + sortedQueryInfoList.get(size / 2).getExecutionTime()) / 2;
            }
            else {
                medianExecutionTime = sortedQueryInfoList.get(size / 2).getExecutionTime();
            }

            System.out.printf("\nАномальные запросы по времени выполнения (медиана времени выполнения %f секунд):\n", medianExecutionTime);
            sortedQueryInfoList
                    .stream()
                    .filter(query -> query.getExecutionTime() > medianExecutionTime + deviationInSeconds)
                    .forEach(System.out::println);

        } catch (IOException | ParseException | InterruptedException exception) {
            System.err.println(exception.getMessage());
        }
    }

    private static void printProgress(long index, long end, long lineCount) {

        int dotCount = (int) Math.ceil(index * 10f / end);
        StringBuilder sb = new StringBuilder("[");

        sb.append("■".repeat(Math.max(0, dotCount)));
        sb.append(" ".repeat(Math.max(0, 10 - dotCount)));

        sb.append("] ");
        if (index % 2 == 0)
            sb.append(String.format(" %d bytes / %d bytes", index, end));
        else
            sb.append(String.format(" %d bytes \\ %d bytes", index, end));

        sb.append(String.format(" | %d lines processed", lineCount));

        for (int count = 0; count < sb.length(); count++) {
            System.out.print("\b");
        }
        System.out.print(sb);
        if (index == end) {
            for (int count = 0; count < sb.length(); count++) {
                System.out.print("\b");
            }
        }
    }
}