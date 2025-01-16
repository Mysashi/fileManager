package com.example.filemanager.manager;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class MyFileVisitor {
    private static HashMap<String, Long> listViewMap = new HashMap<>();

    public static HashMap<String, Long> counterSize(File dir, int n) {
        listViewMap.clear();
        try {
            File[] listFiles = dir.listFiles();
            for (int i = 0; i < listFiles.length; i++) {
                if (listFiles[i].isDirectory()) {
                    Path p = listFiles[i].toPath();
                    try {
                        Files.walkFileTree(p, EnumSet.noneOf(FileVisitOption.class), n, new PrintFiles());
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        } catch (NullPointerException e) {

        }
        return listViewMap;
    }

    public static String convertToHumanMeasure(long bytes) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;

        if (bytes < kb) {
            return bytes + " B";
        } else if (bytes < mb) {
            return String.format("%.2f KB", (double) bytes / kb);
        } else if (bytes < gb) {
            return String.format("%.2f MB", (double) bytes / mb);
        } else {
            return String.format("%.2f GB", (double) bytes / gb);
        }
    }

    private static class PrintFiles extends SimpleFileVisitor<Path> {

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {
            if (attr.isDirectory()) {
                try {
                    System.out.format("Directory: %s, size: %d bytes\n", file, getDirSize(file));
                    listViewMap.put("DIRECTORY: " + file.toFile().getPath(), getDirSize(file));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (attr.isRegularFile()) {
                System.out.format("Regular file: %s, size %d bytes\n", file, attr.size());
                try {
                    listViewMap.put("FILE: " + file.toFile().getPath(), getDirSize(file));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) {
            System.err.println(exc);
            return FileVisitResult.CONTINUE;
        }

        /**
         * Walks through directory path and sums up all files' sizes.
         *
         * @param dirPath Path to directory.
         * @return Total size of all files included in dirPath.
         * @throws IOException
         */
        private long getDirSize(Path dirPath) throws IOException {
            final AtomicLong size = new AtomicLong(0L);

            Files.walkFileTree(dirPath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    size.addAndGet(attrs.size());
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    //just skip
                    return FileVisitResult.CONTINUE;
                }
            });

            return size.get();
        }
    }

    public static HashMap<String, Long> getNthMap(HashMap<String, Long> hash, int n) {
        LinkedHashMap<String, Long> h = hash.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(n)
                .collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new));
        return h;
    }
}
