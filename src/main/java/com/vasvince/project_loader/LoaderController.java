package com.vasvince.project_loader;

import com.vasvince.project_loader.api.Loader;
import com.vasvince.project_loader.enums.SelectionSource;
import com.vasvince.project_loader.impl.CloudLoader;
import com.vasvince.project_loader.impl.LocalLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import static com.vasvince.project_loader.enums.LoaderEnums.*;

public class LoaderController {

    private LocalLoader localLoader;
    private CloudLoader cloudLoader;


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
    private Button actionButton;
    @FXML
    private Button refreshButton;

    private SelectionSource selectionSource = SelectionSource.NONE;



    @FXML
    public void initialize() {
        nameCol1.setCellValueFactory(c -> c.getValue().nameProperty());
        sizeCol1.setCellValueFactory(c -> c.getValue().sizeProperty());
        modifiedCol1.setCellValueFactory(c -> c.getValue().modifiedProperty());

        nameCol2.setCellValueFactory(c -> c.getValue().nameProperty());
        sizeCol2.setCellValueFactory(c -> c.getValue().sizeProperty());
        modifiedCol2.setCellValueFactory(c -> c.getValue().modifiedProperty());

        actionButton.setText(DISABLED_BUTTON_TEXT);
        actionButton.setDisable(true);

        localFileTable.getSelectionModel()
                .selectedItemProperty()
                .addListener((
                    (observable, oldValue, newValue) -> {
                        if (newValue != null) {
                            cloudFileTable.getSelectionModel().clearSelection();
                            selectionSource = SelectionSource.LOCAL;
                            actionButton.setText(DEACTIVATE_BUTTON_TEXT);
                            actionButton.setDisable(false);
                        } else {
                            if (cloudFileTable.getSelectionModel().getSelectedItem() == null) {
                                selectionSource = SelectionSource.NONE;
                                actionButton.setDisable(true);
                            }
                        }
                    }
                    )
                );

        cloudFileTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                localFileTable.getSelectionModel().clearSelection();
                selectionSource = SelectionSource.CLOUD;
                actionButton.setText(ACTIVATE_BUTTON_TEXT);
                actionButton.setDisable(false);
            } else {
                if (localFileTable.getSelectionModel().getSelectedItem() == null) {
                    selectionSource = SelectionSource.NONE;
                    actionButton.setDisable(true);
                }
            }
        });
    }

    @FXML
    private void onRefreshButtonClicked() {
        loadPath(localLoader);
        loadPath(cloudLoader);
    }

    @FXML
    private void onActionButtonClicked() {
        FileEntry selected = null;
        if (selectionSource == SelectionSource.LOCAL) {
            selected = localFileTable.getSelectionModel().getSelectedItem();
        } else if (selectionSource == SelectionSource.CLOUD) {
            selected = cloudFileTable.getSelectionModel().getSelectedItem();
        }

        if (selected != null) {
            System.out.println(selected);
        } else {
            System.out.println("No selection");
        }
    }

    private void loadPath(Loader loader) {
        if (loader instanceof LocalLoader localLoader) {
            localLoader.loadTo(localFileTable, statusLabel1, LOCAL_SOURCE);
        } else if (loader instanceof CloudLoader cloudLoader) {
            cloudLoader.loadTo(cloudFileTable, statusLabel1, CLOUD_SOURCE);
        } else {
            throw new IllegalStateException();
        }
    }

    public void initializeLoaders(LocalLoader localLoader, CloudLoader cloudLoader) {
        this.localLoader = localLoader;
        this.cloudLoader = cloudLoader;

        loadPath(localLoader);
        loadPath(cloudLoader);
    }
}