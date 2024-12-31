package com.example.filemanager.handlers;

import com.example.filemanager.HelloApplication;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javax.swing.table.TableColumn;
import javax.swing.text.TableView;
import java.net.URL;
import java.util.ResourceBundle;

public class TableHandler implements Initializable {
    @FXML
    TableView tableView;
    @FXML private TableColumn image;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
