package com.example.filemanager.handlers.generators;

import com.example.filemanager.handlers.HandlerToTransform;
import com.example.filemanager.handlers.MoveHandler;
import com.example.filemanager.manager.FileManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class ListViewPanel {
    //init

    public HashMap<String, File> hashMap = new HashMap<>();
    public static FileManager fm = new FileManager();
    private HBox root = new HBox();

    public ListViewPanel() {
        init();
        creatingPanel();
    }

    public void init() {
        hashMap = fm.showHashMap("C:\\");
    }
    public MoveHandler mh = new MoveHandler(hashMap);

    public ListView<String> creatingPanel() {
        ObservableList<String> fileNames = FXCollections.observableArrayList(hashMap.keySet());
        ListView<String> listView = new ListView<>();
        listView.getItems().addAll(fileNames);
        listView.setOnDragDetected(event -> {
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
        listView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> stringListView) {
                return new ListViewPanel.AttachmentListCell();
            }
        });

        listView.setOnMouseClicked(event -> {
            {
                hashMap = mh.handleMovement(root, listView, event, fm, hashMap);
                ObservableList<String> fileNamesObservable = FXCollections.observableArrayList(hashMap.keySet());
                listView.setItems(fileNamesObservable);
            }
        });
        root.getChildren().addAll(listView);
        return listView;
    }

    public HBox getRoot() {
        return root;
    }

    private static HashMap<String, Image> mapOfIcons = new HashMap<>();

    private class AttachmentListCell extends ListCell<String> {
        @Override
        public void updateItem(String item, boolean empty) {
            System.out.println(hashMap);
            System.out.println(mapOfIcons);
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
                setText(null);
            } else {
                if (item.lastIndexOf(".") == -1) {
                    Image fxImage = mapOfIcons.get("");
                    if (fxImage == null) {
                        mapOfIcons.put("", getFileIcon(item));
                    } else {
                        fxImage = mapOfIcons.get("");
                    }
                    ImageView imageView = new ImageView(fxImage);
                    setGraphic(imageView);
                    setText(item);
                } else {
                    String clearedString = item.substring(item.lastIndexOf("."));
                    Image fxImage = mapOfIcons.get(clearedString);
                    if (fxImage == null) {
                        fxImage = getFileIcon(item);
                        mapOfIcons.put(clearedString, fxImage);
                    }
                    ImageView imageView = new ImageView(fxImage);
                    setGraphic(imageView);
                    setText(item);
                }
            }
        }

        private String sizeCounter(String item) {
            File f = hashMap.get(item);
            return String.valueOf(f.length());

        }


        private static javax.swing.Icon getJSwingIconFromFileSystem(File file) {
            javax.swing.Icon icon = FileSystemView.getFileSystemView().getSystemIcon(file);
            return icon;
        }

        private Image getFileIcon(String fname) {
            System.out.println("ICON LOGGER: " + fname);
            File file = hashMap.get(fname);
            Icon fetchedIcon = getJSwingIconFromFileSystem(file);
            Image fileIcon = jswingIconToImage(fetchedIcon);
            return fileIcon;
        }

        private static Image jswingIconToImage(javax.swing.Icon jswingIcon) {
            try {
                BufferedImage bufferedImage = new BufferedImage(jswingIcon.getIconWidth(), jswingIcon.getIconHeight(),
                        BufferedImage.TYPE_INT_ARGB);
                jswingIcon.paintIcon(null, bufferedImage.getGraphics(), 0, 0);
                return SwingFXUtils.toFXImage(bufferedImage, null);
            }
            catch (NullPointerException e) {
                //TODO
            }
            return null;
        }
    }
}

