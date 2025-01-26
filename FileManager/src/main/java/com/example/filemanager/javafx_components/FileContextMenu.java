package com.example.filemanager.javafx_components;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


public class FileContextMenu {
    public ContextMenu createContextMenu() {
        ContextMenu cm = new ContextMenu();
        MenuItem delete = new MenuItem("Delete");
        cm.getItems().add(delete);
        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    Files.deleteIfExists(new File(SidebarNavigation.path).toPath());
                    SidebarNavigation.listView.getItems().remove(SidebarNavigation.indexDelete);
                    SidebarNavigation.listView.refresh();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return cm;
    }
}
