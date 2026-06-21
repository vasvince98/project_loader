package com.vasvince.project_loader.services;

import com.vasvince.project_loader.api.Manager;

import java.util.Objects;

public class ManagerService implements Manager {

    private final DriveService driveService;

    public ManagerService(final DriveService driveService) {
        Objects.requireNonNull(driveService, "driveService is null");
        this.driveService = driveService;
    }

    @Override
    public void archive() {

    }

    @Override
    public void downloadById(String id) {

    }
}
