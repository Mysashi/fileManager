package com.example.filemanager.manager;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

public class MyFileVisitor extends SimpleFileVisitor {
    private Path sourcePath = null;
    private Path targetPath;
    public MyFileVisitor(Path targetPath) {
        this.targetPath = targetPath;
    }

    ;
    public ArrayList<Path> getDirectories() {
        return directories;
    }

    public ArrayList<Path> getFiles() {
        return files;
    }



    public void addFiles(Path filesP) {
        files.add(filesP);
    }

    private ArrayList<Path> directories = new ArrayList<>();
    private ArrayList<Path> files = new ArrayList<>();

    @Override
    public FileVisitResult visitFile(Object file, BasicFileAttributes attrs) throws IOException {
        Path filePath = (Path) file;
        System.out.println(filePath);
//        Files.copy((Path) file,
//                targetPath.resolve(sourcePath.relativize((Path) file)));
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult preVisitDirectory(Object dir, BasicFileAttributes attrs) throws IOException {
//        if (sourcePath == null) {
//            sourcePath = (Path) dir;
//        } else {
////            Files.createDirectories(targetPath.resolve(sourcePath
////                    .relativize((Path) dir)));
//        }
        return FileVisitResult.CONTINUE;
    }
}