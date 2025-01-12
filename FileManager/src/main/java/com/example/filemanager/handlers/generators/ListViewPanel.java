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
    public MoveHandler mh = new MoveHandler();
    public static HashMap<String, File> hashMap = new HashMap<>();
    public static FileManager fm = new FileManager();

    public ListViewPanel(HBox root) {
        init();
        creatingPanel(root);
    }

    public void init() {
        hashMap = fm.showHashMap("C:\\");
    }

    public ListView<String> creatingPanel(HBox root) {
        ObservableList<String> fileNames = FXCollections.observableArrayList(hashMap.keySet());
        ListView<String> listView = new ListView<>();
        listView.getItems().addAll(fileNames);
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
        listView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> stringListView) {
                return new ListViewPanel.AttachmentListCell();
            }
        });
        root.getChildren().addAll(listView);
        return listView;
    }

    private  static HashMap<String, Image> mapOfIcons = new HashMap<>();

    private static class AttachmentListCell extends ListCell<String> {
        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            System.out.println(item);
            if (empty) {
                setGraphic(null);
                setText(null);
            } else {

                if (item.lastIndexOf(".") == -1) {
                    Image fxImage = mapOfIcons.get("");
                    if (fxImage == null) {
                        mapOfIcons.put("", getFileIcon(item));
                    }
                    else {
                        fxImage = mapOfIcons.get("");
                    }
                    ImageView imageView = new ImageView(fxImage);
//                    String newSize = String.valueOf(size(hashMap.get(item).toPath()));
                    setGraphic(imageView);
                    setText(item);
                }
                else {
                    String clearedString = item.substring(item.lastIndexOf("."), item.length());
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
    }

    public static long size(Path path) {

        final AtomicLong size = new AtomicLong(0);

        try {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {

                    size.addAndGet(attrs.size());
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) {

                    System.out.println("skipped: " + file + " (" + exc + ")");
                    // Skip folders that can't be traversed
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) {

                    if (exc != null)
                        System.out.println("had trouble traversing: " + dir + " (" + exc + ")");
                    // Ignore errors traversing a folder
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new AssertionError("walkFileTree will not throw IOException if the FileVisitor does not");
        }

        return size.get();
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
