package com.vasvince.project_loader.actions;

import com.vasvince.project_loader.Folder;
import com.vasvince.project_loader.api.Manager;
import com.vasvince.project_loader.enums.SelectionSource;
import com.vasvince.project_loader.impl.CloudLoader;
import com.vasvince.project_loader.services.ManagerService;
import com.vasvince.project_loader.utils.LoaderUtils;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;

import java.io.File;
import java.util.Arrays;

import static com.vasvince.project_loader.enums.LoaderEnums.DEACTIVATE_BUTTON_TEXT;

public class MainActions {

    private final NavigationActions navigationActions = new NavigationActions();

    public void handleActionButton(CloudLoader cloudLoader, TableView<Folder> fileTable) {
        Manager managerService = new ManagerService(cloudLoader);
        Folder selectedFolder = fileTable.getSelectionModel().getSelectedItem();
        managerService.download(selectedFolder);
    }

    public void initRowFactory(TableView<Folder> fileTable, Label statusLabel) {
        fileTable.setRowFactory(tv -> {
            TableRow<Folder> row = new TableRow<>() {
                @Override
                protected void updateItem(Folder item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null) {
                        setStyle("");
                        return;
                    }

                    if (item.isDirectory() && LoaderUtils.isProjectFolder(item)) {
                        setStyle("-fx-background-color: lightgreen;");
                    } else {
                        setStyle("");
                    }
                }
            };

            row.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY
                        && event.getClickCount() == 2
                        && !row.isEmpty()) {

                    Folder item = row.getItem();
                    if (item.isDirectory() && !LoaderUtils.isProjectFolder(item)) {
                        navigationActions.populateLocalTable(item.getPath(), statusLabel, fileTable);
                    }
                }
            });

            return row;
        });
    }
}
