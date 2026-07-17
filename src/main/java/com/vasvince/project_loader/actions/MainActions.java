package com.vasvince.project_loader.actions;

import com.vasvince.project_loader.Folder;
import com.vasvince.project_loader.api.Loader;
import com.vasvince.project_loader.utils.LoaderUtils;
import javafx.scene.control.Label;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;

public class MainActions {

    public void handleActionButton(Loader loader, TableView<Folder> fileTable) {
        loader.execute(fileTable);
    }

    public void initRowFactory(TableView<Folder> fileTable, Label statusLabel, Loader loader) {
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
                        loader.populateTable(item.getPath(), statusLabel, fileTable);
                    }
                }
            });

            return row;
        });
    }
}
