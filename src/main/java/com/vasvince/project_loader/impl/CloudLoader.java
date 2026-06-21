package com.vasvince.project_loader.impl;

import com.vasvince.project_loader.FileEntry;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

public class CloudLoader extends LoaderImpl {

    public CloudLoader(Path path) {
        super(path);
    }

    @Override
    protected void validate(TableView<FileEntry> table, Label status) {
        //TODO implement connection validation
        return;
    }

    @Override
    protected Collection<FileEntry> getProjectList() throws IOException {
        return List.of();
    }
}
