package com.example.filemanager;

import com.example.filemanager.javafx_components.FileLevelSlider;
import com.example.filemanager.javafx_components.SidebarNavigation;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    SidebarNavigation sidebarNav = new SidebarNavigation();
    @Override
    public void start(Stage stage){
        FileLevelSlider fileLevelSlider = new FileLevelSlider();
        Slider slider = fileLevelSlider.init(SidebarNavigation.getFile());
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 300, 300);
        VBox sidebar = sidebarNav.createSidebar(stage,root);
        sidebar.getChildren().add(slider);
        root.setLayoutX(5);
        root.setLeft(sidebar);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args)
    {
        launch();
    }
}