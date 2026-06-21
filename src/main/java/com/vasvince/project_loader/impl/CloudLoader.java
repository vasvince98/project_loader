package com.vasvince.project_loader.impl;

import com.google.api.services.drive.model.File;
import com.vasvince.project_loader.Project;
import com.vasvince.project_loader.services.DriveService;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Collection;
import java.util.List;

public class CloudLoader extends LoaderImpl<File> {

    private final DriveService driveService = new DriveService();

    public CloudLoader(Path path) {
        super(path);
    }

    @Override
    protected void validate(TableView<Project> table, Label status) {
        //TODO implement connection validation
    }

    @Override
    protected Collection<Project> getProjectList() throws IOException {
        List<File> files;
        files = driveService.getFilesFromDrive();
        return convert(files);
    }

    @Override
    protected Project convertItem(File item) {
        return new Project(
                item.getId(),
                item.getName(),
                humanReadableByteCount(item.getSize() == null ? 0 : item.getSize()),
                item.getModifiedTime() == null ? "EMPTY" : fmt.format(Instant.ofEpochMilli(item.getModifiedTime().getValue()))
        );
    }

    public DriveService getDriveService() {
        return driveService;
    }
}
