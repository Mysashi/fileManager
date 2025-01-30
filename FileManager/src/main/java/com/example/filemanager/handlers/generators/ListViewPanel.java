package com.example.filemanager.handlers.generators;

import com.example.filemanager.handlers.HandlerToTransform;
import com.example.filemanager.handlers.MoveHandler;
import com.example.filemanager.handlers.SettingsHandler;
import com.example.filemanager.javafx_components.FileContextMenu;
import com.example.filemanager.manager.FileManager;
import com.example.filemanager.utils.DragResizeMod;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class ListViewPanel {
    //init

    public HashMap<String, File> hashMap = new HashMap<>();
    public static FileManager fm = new FileManager();
    private HBox root = new HBox();

    public ListViewPanel() {
        init();
        creatingPanel();
    }
    private static HashMap<String, Image> mapOfIcons = new HashMap<>();
    private final ObjectProperty<ListCell<String>> dragSource = new SimpleObjectProperty<>();
    public void init() {
        hashMap = fm.showHashMap("C:\\");
    }
    public MoveHandler mh = new MoveHandler(hashMap);

    public ListView<String> creatingPanel() {
        BorderPane borderPane = new BorderPane();
        Button backButton = new Button("Back");
        ObservableList<String> fileNames = FXCollections.observableArrayList(hashMap.keySet());
        ListView<String> listView = new ListView<>();
        listView.getItems().addAll(fileNames);

        listView.setCellFactory(lv -> {
            ListCell<String> cell = new ListCell<String>() {
                @Override
                public void updateItem(String item, boolean empty) {
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
                    } catch (NullPointerException e) {
                        //TODO
                    }
                    return null;
                }
            };

           cell.setOnDragDetected(event -> {
               Dragboard db = cell.startDragAndDrop(javafx.scene.input.TransferMode.ANY);
               String selected = listView.getSelectionModel().getSelectedItem();
               ClipboardContent content = new ClipboardContent();
               List<File> fileList = new ArrayList<>();
               fileList.add(new File(String.join("", mh.pathToCheck) + selected));
               content.putFiles(fileList);
               db.setContent(content);
               event.consume();
            });

            cell.setOnDragDropped(event -> {
//                System.out.println("EVENTSALAM");
//                try {
//                    listView.setItems(new HandlerToTransform(listView, mh)
//                            .dragDrop(event, listView, dragSource));
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
                Dragboard db = event.getDragboard();
                List<File> draggedItem = db.getFiles();
                String fileName = draggedItem.getFirst().getName();
                Path needToCopyFile = db.getFiles().getFirst().toPath();
                Path needToCopyThere = new File(String.join("", mh.pathToCheck) + "\\").toPath();
                Path fileNameToMove = Paths.get(needToCopyThere + "\\" + fileName);
                SettingsHandler.writeInLogs(String.format("FILE %s WAS MOVED FROM %s TO %s", fileName, needToCopyFile.toFile().getPath(), needToCopyThere.toFile().getPath()));
                ListCell<String> listCell = (ListCell<String>)event.getGestureTarget();
                if (listCell.getItem() != null) {
                    System.out.println(listCell.getItem());
                    File file = hashMap.get(listCell.getItem());
                    Path fileNextLocation = needToCopyThere.resolve(file.toPath().resolve(fileName));
                    try {
                            Files.createDirectory(fileNextLocation);

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println(needToCopyThere);
                    System.out.println(fileNextLocation);
                    FileManager.move(needToCopyFile.toFile(), fileNextLocation.toFile());

                }
                else {
                    try {
                        Files.move(needToCopyFile, fileNameToMove, REPLACE_EXISTING);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    listView.getItems().add(db.getFiles().get(0).getName());
                }


                event.setDropCompleted(true);
                event.consume();

//                if (db.hasString() && dragSource.get() != null) {
//                    // in this example you could just do
//                    // listView.getItems().add(db.getString());
//                    // but more generally:
//                    listView.getItems().add(db.getString());
//                } else {
//                    event.setDropCompleted(false);
//                }

            });

            cell.setOnDragDone(e -> {
                lv.getItems().remove(lv.getItems().get(lv.getItems().indexOf(cell.getItem())));
            });


            cell.setOnMouseDragged(event -> {
                System.out.println("DRAGGING");
            });
            cell.setOnDragOver(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasFiles()) {
                    event.acceptTransferModes(javafx.scene.input.TransferMode.MOVE);
                }
                event.consume();
            });


            return cell;
        });


        listView.setOnMouseClicked(event -> {
            {
                if (event.getClickCount() == 2) {
                    hashMap = mh.handleMovement(root, listView, event, fm, hashMap);
                    ObservableList<String> fileNamesObservable = FXCollections.observableArrayList(hashMap.keySet());
                    listView.setItems(fileNamesObservable);
                }
            }
        });
        backButton.setOnAction(e -> {
            mh.comeToPreviousTray(fm, listView);
        });
        borderPane.setCenter(listView);
        borderPane.setTop(backButton);
        root.getChildren().addAll(borderPane);
        return listView;
    }

    public HBox getRoot() {
        return root;
    }

}

