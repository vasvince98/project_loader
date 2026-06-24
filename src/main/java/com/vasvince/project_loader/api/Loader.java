package com.vasvince.project_loader.api;

import com.vasvince.project_loader.Folder;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

public interface Loader {

    void loadTo(TableView<Folder> table,
                Label status, String source);
}
