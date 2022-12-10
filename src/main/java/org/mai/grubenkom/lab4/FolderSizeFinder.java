package org.mai.grubenkom.lab4;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class FolderSizeFinder {
    public static long folderSize(File directory) {
        try {
            long size = 0;
            for (File file : Objects.requireNonNull(directory.listFiles())) {
                if (file.isFile())
                    size += file.length();
                else
                    size += folderSize(file);
            }
            return size;
        } catch (NullPointerException | SecurityException exception) {
            System.err.println(exception.getMessage());
            return 0;
        }
    }

    public static void size(String pathToFolder) {
        if (pathToFolder == null) {
            System.err.println("path is null");
            return;
        }
        try {
            File file = new File(pathToFolder);
            long size = FolderSizeFinder.folderSize(file);
            System.out.println(pathToFolder);
            System.out.printf("Size in bytes: %d\n", size);
            System.out.printf("Size in kilobytes: %d\n", size / 1024);
            System.out.printf("Size in megabytes: %d\n", size / 1024 / 1024);
        } catch (SecurityException exception) {
            System.err.println(exception.getMessage());
        }
    }
}
