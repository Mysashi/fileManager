package com.example.filemanager.handlers;

import com.example.filemanager.HelloApplication;
import com.example.filemanager.HelloController;
import com.example.filemanager.manager.FileManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;

import java.io.File;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;

public class MoveHandler {

    public Deque<String> pathToCheck = new ArrayDeque<>();
    public static HashMap<String, File> function1 = new HashMap<>();

    {
        pathToCheck.addFirst("C:\\");
    }

    public void handleMovement(HBox root, ListView<String> list, MouseEvent e, FileManager fm) {
        if (e.getClickCount() == 2) {
            String selectedItem = list.getSelectionModel().getSelectedItem();
            pathToCheck.addLast(selectedItem + "\\");
            try {
                HelloApplication.hashMap = fm.showHashMap(String.join("", pathToCheck));
                ObservableList<String> fileNames = FXCollections.observableArrayList(HelloApplication.hashMap.keySet());
                list.setItems(fileNames);
            } catch (NullPointerException ee) {
                pathToCheck.removeLast();
            }
        }
    }

    public void comeToPreviousTray(FileManager fm, ListView<String> list) {
        if (pathToCheck.size() != 1) {
            pathToCheck.removeLast();
        }
        System.out.println(pathToCheck.toString());
        HashMap<String, File> function = fm.showHashMap(String.join("", pathToCheck));
        ObservableList<String> fileNames = FXCollections.observableArrayList(function.keySet());
        list.setItems(fileNames);
    }


}
