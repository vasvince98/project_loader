package com.vasvince.project_loader.api;

import com.vasvince.project_loader.Folder;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

import java.nio.file.Path;

public interface NavigationActions {

    void populateTable(Path path, Label statusLabel, TableView<Folder> fileTable);
}
