package com.example.filemanager.handlers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class HandlerToTransform {

    private ListView<String> listView;
    private MoveHandler moveHandler;

    public HandlerToTransform(ListView<String> listView, MoveHandler moveHandler) {
        this.listView = listView;
        this.moveHandler = moveHandler;

    }

    public ObservableList<String> dragDrop(DragEvent event, ListView<String> listView) throws IOException {
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasFiles()) {
            List<File> draggedItem = db.getFiles();
            String fileName = draggedItem.getFirst().getName();
            Path needToCopyFile = db.getFiles().getFirst().toPath();
            Path needToCopyThere = new File(String.join("", moveHandler.pathToCheck)).toPath();
            Path fileNameToMove = Paths.get(needToCopyThere + fileName);
            Files.move(needToCopyFile, fileNameToMove, REPLACE_EXISTING);
            System.out.println(needToCopyFile);
            System.out.println(needToCopyThere);
            System.out.println("success");
            listView.getItems().add(fileName);

        }
        event.setDropCompleted(success);
        event.consume();
        return listView.getItems();
    }

}