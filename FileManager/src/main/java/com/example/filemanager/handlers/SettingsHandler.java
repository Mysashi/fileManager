package com.example.filemanager.handlers;

import com.example.filemanager.javafx_components.SidebarNavigation;
import com.example.filemanager.utils.DragResizeMod;
import com.example.filemanager.utils.ResizableScrollPane;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class SettingsHandler {

    private static VBox logs;
    private ScrollPane logsPane;

    public static VBox getLogs() {
        return logs;
    }
    public boolean is_ticked = false;

    public void init(BorderPane root) {
        settingsLogs();
        String settingStyles = "-fx-font-size: 15px; -fx-pref-width: 200px; -fx-pref-height: 20px;";
        VBox settingsParams = new VBox();
        settingsParams.setSpacing(5f);
        settingsParams.setPadding(new Insets(20, 0, 0, 20));
        List<Label> listOfLabels = new ArrayList<>();
        CheckBox checkBox = new CheckBox("Включить логи");
        checkBox.setOnAction(e-> {
            if (checkBox.isSelected()) {
                root.setBottom(logsPane);
            }
            else {
                root.setBottom(null);
            }

        });
        is_ticked = checkBox.isSelected();
        checkBox.setStyle(settingStyles);;
        settingsParams.getChildren().add(checkBox);
        listOfLabels.add(new Label("Включить логи"));
        listOfLabels.add(new Label("Поменять тему"));
        root.setCenter(settingsParams);
        root.setRight(new HBox());

    }


    public void settingsLogs() {
        logs = new VBox();
        logsPane = new ResizableScrollPane();
        logsPane.setHmax(200);
        DragResizeMod.makeResizable(logsPane);
        logs.getChildren().add(new Label("aaaaaaaaaaaaaaaaaa"));
        logsPane.setContent(logs);
    }
}
