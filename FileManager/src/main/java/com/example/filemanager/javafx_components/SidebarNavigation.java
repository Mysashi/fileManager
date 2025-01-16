package com.example.filemanager.javafx_components;
import com.example.filemanager.handlers.generators.ListViewPanel;
import com.example.filemanager.manager.FileManager;
import com.example.filemanager.manager.MyFileVisitor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.geometry.Pos;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class SidebarNavigation {
    public static File f;
    public static ListView<String> listView;

    public static File getFile() {
        return f;
    }

    public VBox createSidebar(Stage stage, BorderPane root) {
        VBox sidebar = new VBox(10);
        FileManager fm = new FileManager();
        HBox contentAreaForPanels = new HBox();
        listView = new ListView<String>();
        sidebar.setStyle("-fx-background-color: #e0e0e0;");
        sidebar.setMinWidth(200);

        Button panelFiles = createSidebarButton("Панели файлов");
        Button sizeCounter = createSidebarButton("Подсчет занятой дисковой памяти");
        Button settingButton = createSidebarButton("Настройки");


        Button creationButton = new Button("CREATE PANEL");
        Button startSizing = new Button("START COUNTING SIZE");

        panelFiles.setOnAction(e ->  {
            root.setRight(creationButton);
            root.setCenter(contentAreaForPanels);
        });

        sidebar.getChildren().addAll(panelFiles, sizeCounter, settingButton);

        creationButton.setOnAction(e -> {
            HBox panel = new ListViewPanel().getRoot();
            contentAreaForPanels.getChildren().add(panel);
        });

        startSizing.setOnAction(e -> {
            listView.getItems().clear();
            DirectoryChooser fileChooser = new DirectoryChooser();
            f = fileChooser.showDialog(stage);
            HashMap<String, Long> hashMapOfSize = MyFileVisitor.getNthMap(walkingTreeInit(f), 20);
            ArrayList<String> arrayList = new ArrayList<>();
            hashMapOfSize.forEach((k,v) -> arrayList.add(k + "  " + MyFileVisitor.convertToHumanMeasure(v)));
            ObservableList<String> observList = FXCollections.observableArrayList(arrayList);
            listView.setItems(observList);

        });
        listView.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2) {
                    String item = listView.getSelectionModel().getSelectedItem();
                    int index = item.indexOf(":");
                    String path = item.substring(index + 1, item.lastIndexOf("  ")).trim();
                    fm.openFileOrDirectory(path);
                }
        });
        sizeCounter.setOnAction(e ->  {
            root.setRight(startSizing);
            root.setCenter(listView);
        });
        settingButton.setOnAction(e -> System.out.println("Settings Clicked!"));
        return sidebar;
    }

    private Button createSidebarButton(String text) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setAlignment(Pos.CENTER_LEFT);
        return button;
    }

    public HashMap<String, Long> walkingTreeInit(File p) {
        HashMap<String, Long> hash = MyFileVisitor.counterSize(p, 1);
        return hash;
    }
}
