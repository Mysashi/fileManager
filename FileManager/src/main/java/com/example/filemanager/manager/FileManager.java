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
            hashMap.put(files[i].getName(), files[i]);
        }
        return hashMap;
    }

    public void openFileOrDirectory(String path) {
        File file = new File(path);
        Desktop desktop = Desktop.getDesktop();
        if(!Desktop.isDesktopSupported())
        {
            System.out.println("Desktop Support Not Present in the system.");
        }
        if (file.exists()) {
            try {
                desktop.open(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            System.out.println(path + "NOT EXISTS");
        }


    }

    public static void move(File sourceFile, File destFile) {
        if (sourceFile.isDirectory()) {
            File[] files = sourceFile.listFiles();
            assert files != null;
            for (File file : files) move(file, new File(destFile, file.getName()));
            if (!sourceFile.delete()) throw new RuntimeException();
        } else {
            if (!destFile.getParentFile().exists())
                if (!destFile.getParentFile().mkdirs()) throw new RuntimeException();
            if (!sourceFile.renameTo(destFile)) throw new RuntimeException();
        }
    }


}
