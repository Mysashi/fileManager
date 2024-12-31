module com.example.filemanager {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires javafx.swing;

    opens com.example.filemanager to javafx.fxml;
    exports com.example.filemanager;
}