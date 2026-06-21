package com.vasvince.project_loader.api;

import com.vasvince.project_loader.Project;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

public interface Loader {

    void loadTo(TableView<Project> table,
                Label status, String source);
}
