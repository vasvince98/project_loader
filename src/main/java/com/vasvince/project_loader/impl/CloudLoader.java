package com.vasvince.project_loader.impl;

import com.google.api.services.drive.model.File;
import com.vasvince.project_loader.FileEntry;
import com.vasvince.project_loader.services.DriveService;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.util.Collection;
import java.util.List;

public class CloudLoader extends LoaderImpl {

    private final DriveService driveService = new DriveService();

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
        List<File> files;
        try {
            files = driveService.getFilesFromDrive();
        } catch (GeneralSecurityException e) {
            throw new IOException(e);
        }
        return convertFileToFileEntry(files);
    }
}
