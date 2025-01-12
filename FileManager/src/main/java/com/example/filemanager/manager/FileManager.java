package com.example.filemanager.manager;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.apache.commons.io.FilenameUtils;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageConsumer;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

public class FileManager {
    public String[] ShowAllFiles(String path) {
        File file = new File(path);
        if (!file.exists()) {
            throw new IllegalArgumentException();
        }
        File[] files = file.listFiles();
        String[] strings = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            strings[i] = files[i].getPath();
        }

        return strings;
    }

    public HashMap<String, File> showHashMap(String path) {
        File file = new File(path);
        File[] files = file.listFiles();
        HashMap<String, File> hashMap = new HashMap<>();
        for (int i = 0; i < files.length; i++) {
            hashMap.put(files[i].getName() + FilenameUtils.getExtension(files[i].getName()), files[i]);
        }
        return hashMap;
    }

    public String[] showFilesName(String path) {
        File file = new File(path);
        if (!file.exists()) {
            throw new IllegalArgumentException();
        }
        File[] files = file.listFiles();
        String[] strings = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            strings[i] = files[i].getName();
        }

        return strings;
    }

    public static void listAllFiles() {
        try (Stream<Path> paths = Files.walk(Paths.get("C:\\"))) {
            paths
                    .filter(Files::isRegularFile)
                    .filter(Files::isReadable)
                    .forEach(System.out::println);

        }catch (AccessDeniedException e) {
            System.out.println(e.getMessage());
        }
        catch (IOException e) {
//            throw new RuntimeException(e);
        }

    }
}
