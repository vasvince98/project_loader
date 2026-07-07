package com.vasvince.project_loader;

import com.vasvince.project_loader.actions.LoaderActions;
import com.vasvince.project_loader.actions.MainActions;
import com.vasvince.project_loader.enums.SelectionSource;
import com.vasvince.project_loader.exceptions.LoaderException;
import com.vasvince.project_loader.impl.CloudLoader;
import com.vasvince.project_loader.impl.LocalLoader;
import com.vasvince.project_loader.utils.LoaderUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.vasvince.project_loader.enums.LoaderEnums.*;

public class LoaderController {

    private final Logger logger = LoggerFactory.getLogger(LoaderController.class);


    @FXML private TableView<Folder> localFileTable;
    @FXML private TableColumn<Folder, String> localNameCol;
    @FXML private TableColumn<Folder, String> localSizeCol;
    @FXML private Label localStatusLabel;

    @FXML private TableView<Folder> cloudFileTable;
    @FXML private TableColumn<Folder, String> nameCol2;
    @FXML private TableColumn<Folder, String> sizeCol2;
    @FXML private Label cloudStatusLabel;

    @FXML
    private Button actionButton;

    @FXML
    private Button refreshButton;


    private LocalLoader localLoader;
    private CloudLoader cloudLoader;
    private final LoaderActions loaderActions = new LoaderActions();
    private final MainActions mainActions = new MainActions();

    private SelectionSource selectionSource = SelectionSource.NONE;

    public void initializeLoaders(LocalLoader localLoader, CloudLoader cloudLoader) {
        this.localLoader = localLoader;
        this.cloudLoader = cloudLoader;
        refreshListing();
    }

    @FXML
    public void initialize() {
        logger.info("Initializing landing page...");
        localNameCol.setCellValueFactory(c -> c.getValue().nameProperty());
        localSizeCol.setCellValueFactory(c -> c.getValue().sizeProperty());


        mainActions.initRowFactory(localFileTable, localStatusLabel);


        nameCol2.setCellValueFactory(c -> c.getValue().nameProperty());
        sizeCol2.setCellValueFactory(c -> c.getValue().sizeProperty());

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
                            actionButton.setDisable(!LoaderUtils.isProjectFolder(newValue));
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
                actionButton.setDisable(!LoaderUtils.isProjectFolder(newSel));
            } else {
                if (localFileTable.getSelectionModel().getSelectedItem() == null) {
                    selectionSource = SelectionSource.NONE;
                    actionButton.setDisable(true);
                }
            }
        });

        logger.info("Landing page initialized");
    }

    @FXML
    private void onRefreshButtonClicked() {
        refreshListing();
    }

    @FXML
    private void onActionButtonClicked() {
        switch (selectionSource) {
            case LOCAL -> mainActions.handleActionButton(localLoader, localFileTable);
            case CLOUD -> mainActions.handleActionButton(cloudLoader, cloudFileTable);
            default -> throw new LoaderException("Not a valid selectionSource");
        }
        refreshListing();
    }

    private void refreshListing() {
        loaderActions.loadPath(localLoader, localFileTable, localStatusLabel, LOCAL_SOURCE);
        loaderActions.loadPath(cloudLoader, cloudFileTable, cloudStatusLabel, CLOUD_SOURCE);
    }

}