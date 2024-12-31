package com.example.filemanager;

import com.example.filemanager.handlers.DragHandler;
import com.example.filemanager.handlers.HandlerToTransform;
import com.example.filemanager.handlers.MoveHandler;
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

import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.StyledEditorKit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class HelloApplication extends Application {
    public MoveHandler mh = new MoveHandler();
    public DragHandler dh = new DragHandler();
    public static HashMap<String, File> hashMap = new HashMap<>();
    public static FileManager fm = new FileManager();
    @Override
    public void start(Stage stage) throws IOException {
        // Создаем JFrame// Вертикальная раскладка

        // Создаем список строк
        FileManager fm = new FileManager();
        String[] strings = fm.ShowAllFiles("C:\\");
        hashMap = fm.showHashMap("C:\\");
        ObservableList<String> fileNames = FXCollections.observableArrayList(hashMap.keySet());
        ListView<String> listView = new ListView<>();
//        listView.getItems().addAll(strings);
        listView.getItems().addAll(fileNames);

        HBox root = new HBox();
        AnchorPane pane = new AnchorPane();
        root.setLayoutX(5);
        listView.setOnDragDetected(event ->{
            Dragboard db = listView.startDragAndDrop(javafx.scene.input.TransferMode.ANY);
            String selected = listView.getSelectionModel().getSelectedItem();
            ClipboardContent content = new ClipboardContent();
            List<File> fileList = new ArrayList<>();
            fileList.add(new File(String.join("", mh.pathToCheck) + selected));

            listView.getItems().remove(listView.getItems().get(listView.getItems().indexOf(selected)));
            content.putFiles(fileList);
            db.setContent(content);
            event.consume();
        });
        listView.setOnMouseDragged(event -> {
            System.out.println("DRAGGING");
        });
        listView.setOnDragOver(event -> {
            if (event.getDragboard().hasFiles()) {
                event.acceptTransferModes(javafx.scene.input.TransferMode.MOVE);
            }
            event.consume();
        });
        listView.setOnDragDropped(event -> {
            try {
                listView.setItems(new HandlerToTransform(listView, mh)
                        .dragDrop(event, listView));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            event.setDropCompleted(true);
            event.consume();
        });
        listView.setOnMouseClicked(event -> {{
            mh.handleMovement(root, listView, event, fm);
            }
        });
//        button.setOnAction(event -> {
//            mh.comeToPreviousTray(fm, listView);
//        });

//        TextFlow textFlow = new TextFlow(text1,text2);
//        root.getChildren().add(textFlow);
//        root.getChildren().add(button);
        root.getChildren().addAll(listView);
//        pane.getChildren().add(new ListView<String>());
        listView.setLayoutX(20);
        Scene scene = new Scene(root, 300, 300);
        listView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> stringListView) {
                return new AttachmentListCell();
            }
        });

        stage.setTitle("Список строк с двойным кликом");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args)
    {
        launch();
    }

    private static class AttachmentListCell extends ListCell<String> {
        @Override
        public void updateItem(String item, boolean empty) {
            System.out.println(item);
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
                setText(null);
            } else {
                Image fxImage = getFileIcon(item);
                ImageView imageView = new ImageView(fxImage);
                setGraphic(imageView);
                setText(item);
            }
        }
    }
    private static javax.swing.Icon getJSwingIconFromFileSystem(File file) {
        javax.swing.Icon icon = FileSystemView.getFileSystemView().getSystemIcon(file);

        // }

        // OS X {
        //final javax.swing.JFileChooser fc = new javax.swing.JFileChooser();
        //javax.swing.Icon icon = fc.getUI().getFileView(fc).getIcon(file);
        // }

        return icon;
    }

    private static Image getFileIcon(String fname) {
        File file = hashMap.get(fname);
        Icon fetchedIcon = getJSwingIconFromFileSystem(file);
        Image fileIcon =  jswingIconToImage(fetchedIcon);
        return fileIcon;
    }

    private static Image jswingIconToImage(javax.swing.Icon jswingIcon) {
        BufferedImage bufferedImage = new BufferedImage(jswingIcon.getIconWidth(), jswingIcon.getIconHeight(),
                BufferedImage.TYPE_INT_ARGB);
        jswingIcon.paintIcon(null, bufferedImage.getGraphics(), 0, 0);
        return SwingFXUtils.toFXImage(bufferedImage, null);
    }

}