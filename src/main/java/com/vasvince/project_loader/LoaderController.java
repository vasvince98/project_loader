package com.vasvince.project_loader;

import com.vasvince.project_loader.api.Loader;
import com.vasvince.project_loader.impl.CloudLoader;
import com.vasvince.project_loader.impl.LocalLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.beans.property.SimpleStringProperty;

import static com.vasvince.project_loader.enums.LoaderEnums.CLOUD_SOURCE;
import static com.vasvince.project_loader.enums.LoaderEnums.LOCAL_SOURCE;

public class LoaderController {

    @FXML private TableView<FileEntry> localFileTable;
    @FXML private TableColumn<FileEntry, String> nameCol1;
    @FXML private TableColumn<FileEntry, String> sizeCol1;
    @FXML private TableColumn<FileEntry, String> modifiedCol1;
    @FXML private Label statusLabel1;

    @FXML private TableView<FileEntry> cloudFileTable;
    @FXML private TableColumn<FileEntry, String> nameCol2;
    @FXML private TableColumn<FileEntry, String> sizeCol2;
    @FXML private TableColumn<FileEntry, String> modifiedCol2;
    @FXML private Label statusLabel2;



    @FXML
    public void initialize() {
        nameCol1.setCellValueFactory(c -> c.getValue().nameProperty());
        sizeCol1.setCellValueFactory(c -> c.getValue().sizeProperty());
        modifiedCol1.setCellValueFactory(c -> c.getValue().modifiedProperty());

        nameCol2.setCellValueFactory(c -> c.getValue().nameProperty());
        sizeCol2.setCellValueFactory(c -> c.getValue().sizeProperty());
        modifiedCol2.setCellValueFactory(c -> c.getValue().modifiedProperty());
    }

    public void loadPath(Loader loader) {
        if (loader instanceof LocalLoader localLoader) {
            localLoader.loadTo(localFileTable, statusLabel1, LOCAL_SOURCE);
        } else if (loader instanceof CloudLoader cloudLoader) {
            cloudLoader.loadTo(cloudFileTable, statusLabel1, CLOUD_SOURCE);
        } else {
            throw new IllegalStateException();
        }
    }
}