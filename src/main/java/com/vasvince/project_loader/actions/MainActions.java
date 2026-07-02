package com.vasvince.project_loader.actions;

import com.vasvince.project_loader.Folder;
import com.vasvince.project_loader.api.Manager;
import com.vasvince.project_loader.impl.CloudLoader;
import com.vasvince.project_loader.services.ManagerService;
import javafx.scene.control.TableView;

public class MainActions {

    public void handleActionButton(CloudLoader cloudLoader, TableView<Folder> fileTable) {
        Manager managerService = new ManagerService(cloudLoader);
        Folder selectedFolder = fileTable.getSelectionModel().getSelectedItem();
        managerService.download(selectedFolder);
    }
}
