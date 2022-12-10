package org.mai.grubenkom.lab3;

import lombok.Getter;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
class SearchTask implements Callable<List<Integer>> {
    private List<String> lineList;
    private int startIndex;
    private int endIndex;

    public SearchTask(List<String> lineList, int startIndex, int endIndex, String token) {
        this.lineList = lineList;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.token = token;
        currentCountOfLines = new AtomicInteger(1);
        currentCountOfTokenContain = new AtomicInteger(0);
        lineNumbersWithTokenList = new ArrayList<>();
    }

    private String token;
    private List<Integer> lineNumbersWithTokenList;

    private static AtomicInteger currentCountOfLines;
    private static AtomicInteger currentCountOfTokenContain;

    public List<Integer> call() {
        for (int i = startIndex; i < endIndex; i++) {
            if (lineList.get(i).contains(token)) {
                lineNumbersWithTokenList.add(i);
                currentCountOfTokenContain.getAndIncrement();
            }
            currentCountOfLines.getAndIncrement();
        }
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        printProgress(currentCountOfLines.get(), lineList.size(), currentCountOfTokenContain.get());
        return lineNumbersWithTokenList;
    }
    private synchronized static void printProgress(int index, int end, int lineCount) {

        int dotCount = (int) Math.ceil(index * 10f / end);
        StringBuilder sb = new StringBuilder("[");

        sb.append("■".repeat(Math.max(0, dotCount)));
        sb.append(" ".repeat(Math.max(0, 10 - dotCount)));

        sb.append("] ");
        if (index % 2 == 0)
            sb.append(String.format(" %d / %d lines", index, end));
        else
            sb.append(String.format(" %d \\ %d lines", index, end));

        sb.append(String.format(" | %d keywords found", lineCount));

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

public class TokenSearcher {
    private List<String> lineList;
    private List<Callable<List<Integer>>> taskList;
    private ExecutorService executorService;

    private String pathToFile;
    private String keyword;

    private int numberOfLineBefore;
    private int numberOfLineAfter;

    public void search(String pathToFile, String keyword, int numberOfLineBefore, int numberOfLineAfter) {
        this.lineList = getLineList(pathToFile);
        executorService = Executors.newFixedThreadPool(8);
        taskList = new ArrayList<>();

        int startIndex = 0;
        int endIndex = 0;
        int listSize = lineList.size();
        int blockSize = 1000;

        while (endIndex < listSize - 1) {
            if (endIndex + blockSize >= listSize) endIndex = listSize - 1;
            else endIndex += blockSize;

            taskList.add(new SearchTask(lineList, startIndex, endIndex, keyword));

            startIndex += blockSize;
        }


        try {
            List<Future<List<Integer>>> results = executorService.invokeAll(taskList);
            List<Integer> resultList = new ArrayList<>();
            for (Future<List<Integer>> listFuture: results) {
                resultList.addAll(listFuture.get());
            }

            System.out.println();
            resultList.forEach(index -> System.out.println(lineList.get(index)));

        } catch (ExecutionException | InterruptedException e) {
            System.err.println(e.getCause().getMessage());
        }
        executorService.shutdown();
    }


    private List<String> getLineList(String pathToFile) {
        List<String> list = new ArrayList<>();
        try (FileReader fileReader = new FileReader(pathToFile);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                list.add(line);
            }
        }
        catch (IOException exception) {
            System.err.println(exception.getMessage());
        }

        return list;
    }
}
