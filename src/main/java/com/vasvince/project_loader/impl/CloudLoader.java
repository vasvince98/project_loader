package com.vasvince.project_loader.impl;

import com.google.api.services.drive.model.File;
import com.vasvince.project_loader.Folder;
import com.vasvince.project_loader.services.DriveService;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

import static com.vasvince.project_loader.enums.LoaderEnums.LOGIC_WORK_DIR;

public class CloudLoader extends LoaderImpl<File> {

    private static final Logger logger = LoggerFactory.getLogger(CloudLoader.class);

    private final DriveService driveService = new DriveService();

    public CloudLoader(Path path) {
        super(path);
    }

    @Override
    protected void validate(TableView<Folder> table, Label status) {
        //TODO implement connection validation
    }

    @Override
    protected Collection<Folder> getFolderList() throws IOException {
        List<File> files;
        files = driveService.getFilesFromDrive();
        return convert(files);
    }

    @Override
    protected Folder convertItem(File item) {
        return new Folder(
                item.getId(),
                item.getName(),
                humanReadableByteCount(item.getSize() == null ? 0 : item.getSize()),
                Path.of("CLOUD")
        );
    }

    public DriveService getDriveService() {
        return driveService;
    }

    @Override
    public void execute(TableView<Folder> fileTable) {
        Folder selectedFolder = fileTable.getSelectionModel().getSelectedItem();
        logger.info("Downloading project: {}...", selectedFolder.getName());
        try {
            driveService.downloadProject(selectedFolder, Path.of(LOGIC_WORK_DIR));
            logger.info("Successfully downloaded project: {}", selectedFolder.getName());
        } catch (IOException e) {
            logger.error("Something went wrong during downloading project: {}", selectedFolder.getName());
        }
    }
}
