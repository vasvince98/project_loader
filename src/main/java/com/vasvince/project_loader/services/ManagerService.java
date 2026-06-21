package com.vasvince.project_loader.services;

import com.vasvince.project_loader.Project;
import com.vasvince.project_loader.api.Manager;
import com.vasvince.project_loader.impl.CloudLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

import static com.vasvince.project_loader.enums.LoaderEnums.LOGIC_WORK_DIR;

public class ManagerService implements Manager {

    private static final Logger logger = LoggerFactory.getLogger(ManagerService.class);

    private final CloudLoader cloudLoader;

    public ManagerService(final CloudLoader cloudLoader) {
        Objects.requireNonNull(cloudLoader, "driveService is null");
        this.cloudLoader = cloudLoader;
    }

    @Override
    public void archive(Project project) {

    }

    @Override
    public void download(Project project) {
        logger.info("Downloading project: {}...", project.getName());
        try {
            cloudLoader.getDriveService().downloadProject(project, Path.of(LOGIC_WORK_DIR));
            logger.info("Successfully downloaded project: {}", project.getName());
        } catch (IOException e) {
            logger.error("Something went wrong during downloading project: {}", project.getName());
        }
    }
}
