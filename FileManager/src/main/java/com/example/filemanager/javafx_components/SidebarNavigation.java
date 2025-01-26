package com.example.filemanager.javafx_components;
import com.example.filemanager.handlers.SettingsHandler;
import com.example.filemanager.handlers.generators.ListViewPanel;
import com.example.filemanager.manager.FileManager;
import com.example.filemanager.manager.MyFileVisitor;

import com.example.filemanager.utils.DragResizeMod;
import com.example.filemanager.utils.ResizableScrollPane;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.CheckBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class SidebarNavigation {
    public static File f;
    public static ListView<String> listView;
    public static String path;
    public static int indexDelete;

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

        panelFiles.setOnAction(e -> {
            DragResizeMod.makeResizable(contentAreaForPanels);
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
            listView.setContextMenu(new FileContextMenu().createContextMenu());
            DirectoryChooser fileChooser = new DirectoryChooser();
            f = fileChooser.showDialog(stage);
            HashMap<String, Long> hashMapOfSize = MyFileVisitor.getNthMap(walkingTreeInit(f), 20);
            ArrayList<String> arrayList = new ArrayList<>();
            hashMapOfSize.forEach((k, v) -> arrayList.add(k + "  " + MyFileVisitor.convertToHumanMeasure(v)));
            ObservableList<String> observList = FXCollections.observableArrayList(arrayList);
            listView.setItems(observList);

        });
        listView.getItems().addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(Change<? extends String> change) {
                System.out.println("was added");
                listView.refresh();
            }
        });
        listView.setOnMouseClicked(e -> {
            String item = listView.getSelectionModel().getSelectedItem();
            indexDelete = listView.getSelectionModel().getSelectedIndex();
            int index = item.indexOf(":");
            path = item.substring(index + 1, item.lastIndexOf("  ")).trim();
            if (e.getClickCount() == 2) {
                fm.openFileOrDirectory(path);
            }
        });
        sizeCounter.setOnAction(e -> {
            root.setRight(startSizing);
            root.setCenter(listView);
        });
        settingButton.setOnAction(e -> {
            SettingsHandler settingsHandler = new SettingsHandler();
            settingsHandler.init(root);
        });
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
