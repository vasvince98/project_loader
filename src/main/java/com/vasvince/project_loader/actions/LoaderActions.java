package com.vasvince.project_loader.actions;

import com.vasvince.project_loader.Folder;
import com.vasvince.project_loader.api.Loader;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

public class LoaderActions {

    public void loadPath(Loader loader,  TableView<Folder> table, Label status,
                         String source) {
        loader.loadTo(table, status, source);
    }
}
