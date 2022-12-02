package org.mai.grubenkom.lab4;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class FolderSizeFinder {
    public static long folderSize(File directory) {
        long length = 0;
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (file.isFile())
                length += file.length();
            else
                length += folderSize(file);
        }
        return length;
    }

    public static void size(String pathToFolder) {
        try {
            File file = new File(pathToFolder);
            System.out.println(FolderSizeFinder.folderSize(file));
        } catch (NullPointerException | IllegalArgumentException exception) {
            System.err.println(exception.getMessage());
        }
    }
}
