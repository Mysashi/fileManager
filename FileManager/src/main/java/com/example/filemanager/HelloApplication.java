package com.example.filemanager;

import com.example.filemanager.handlers.DragHandler;
import com.example.filemanager.handlers.HandlerToTransform;
import com.example.filemanager.handlers.MoveHandler;
import com.example.filemanager.handlers.generators.ListViewPanel;
import com.example.filemanager.manager.FileManager;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.StyledEditorKit;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class HelloApplication extends Application {
    public MoveHandler mh = new MoveHandler();
    public DragHandler dh = new DragHandler();
    public static HashMap<String, File> hashMap = new HashMap<>();
    public static FileManager fm = new FileManager();
    @Override
    public void start(Stage stage) throws IOException {

        HBox root = new HBox();
        root.setLayoutX(5);
//        button.setOnAction(event -> {
//            mh.comeToPreviousTray(fm, listView);
//        });
        ListViewPanel panel1 = new ListViewPanel(root);
        ListViewPanel panel2 = new ListViewPanel(root);
        Scene scene = new Scene(root, 300, 300);
        stage.setTitle("Список строк с двойным кликом");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args)
    {
        launch();
    }
}