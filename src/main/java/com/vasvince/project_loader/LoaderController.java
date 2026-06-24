package com.vasvince.project_loader;

import com.vasvince.project_loader.api.Loader;
import com.vasvince.project_loader.api.Manager;
import com.vasvince.project_loader.enums.SelectionSource;
import com.vasvince.project_loader.impl.CloudLoader;
import com.vasvince.project_loader.impl.LocalLoader;
import com.vasvince.project_loader.services.ManagerService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

import static com.vasvince.project_loader.enums.LoaderEnums.*;
import static com.vasvince.project_loader.impl.LoaderImpl.humanReadableByteCount;

public class LoaderController {

    private final Logger logger = LoggerFactory.getLogger(LoaderController.class);

    private LocalLoader localLoader;
    private CloudLoader cloudLoader;


    @FXML private TableView<Project> localFileTable;
    @FXML private TableColumn<Project, String> localNameCol;
    @FXML private TableColumn<Project, String> localSizeCol;
    @FXML private Label localStatusLabel;

    @FXML private TableView<Project> cloudFileTable;
    @FXML private TableColumn<Project, String> nameCol2;
    @FXML private TableColumn<Project, String> sizeCol2;
    @FXML private Label cloudStatusLabel;

    @FXML
    private Button actionButton;
    @FXML
    private Button refreshButton;

    private Path currentLocalPath;

    private SelectionSource selectionSource = SelectionSource.NONE;

    public void initializeLoaders(LocalLoader localLoader, CloudLoader cloudLoader) {
        this.localLoader = localLoader;
        this.cloudLoader = cloudLoader;

        loadPath(localLoader);
        loadPath(cloudLoader);
    }

    @FXML
    public void initialize() {
        logger.info("Initializing landing page...");
        localNameCol.setCellValueFactory(c -> c.getValue().nameProperty());
        localSizeCol.setCellValueFactory(c -> c.getValue().sizeProperty());

        // double-click row to open directory
        localFileTable.setRowFactory(tv -> {
            TableRow<Project> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 && !row.isEmpty()) {
                    Project item = row.getItem();
                    if (item.isDirectory()) {
                        populateLocalTable(item.getPath());
                    }
                }
            });
            return row;
        });

        nameCol2.setCellValueFactory(c -> c.getValue().nameProperty());
        sizeCol2.setCellValueFactory(c -> c.getValue().sizeProperty());

        actionButton.setText(DISABLED_BUTTON_TEXT);
        actionButton.setDisable(true);

        localFileTable.getSelectionModel()
                .selectedItemProperty()
                .addListener((
                    (observable, oldValue, newValue) -> {
                        if (newValue != null) {
                            cloudFileTable.getSelectionModel().clearSelection();
                            selectionSource = SelectionSource.LOCAL;
                            actionButton.setText(DEACTIVATE_BUTTON_TEXT);
                            actionButton.setDisable(false);
                        } else {
                            if (cloudFileTable.getSelectionModel().getSelectedItem() == null) {
                                selectionSource = SelectionSource.NONE;
                                actionButton.setDisable(true);
                            }
                        }
                    }
                    )
                );

        cloudFileTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                localFileTable.getSelectionModel().clearSelection();
                selectionSource = SelectionSource.CLOUD;
                actionButton.setText(ACTIVATE_BUTTON_TEXT);
                actionButton.setDisable(false);
            } else {
                if (localFileTable.getSelectionModel().getSelectedItem() == null) {
                    selectionSource = SelectionSource.NONE;
                    actionButton.setDisable(true);
                }
            }
        });

        logger.info("Landing page initialized");
    }

    @FXML
    private void onRefreshButtonClicked() {
        refreshListing();
    }

    @FXML
    private void onActionButtonClicked() {
        Manager managerService = new ManagerService(cloudLoader);
        Project selectedProject;
        if (selectionSource == SelectionSource.LOCAL) {
            selectedProject = localFileTable.getSelectionModel().getSelectedItem();
            managerService.archive(selectedProject);
        } else if (selectionSource == SelectionSource.CLOUD) {
            selectedProject = cloudFileTable.getSelectionModel().getSelectedItem();
            managerService.download(selectedProject);
        }
        refreshListing();
    }

    private void refreshListing() {
        loadPath(localLoader);
        loadPath(cloudLoader);
    }

    private void populateLocalTable(Path path) {
        try {
            Path real = path.toRealPath();
            currentLocalPath = real;
            localStatusLabel.setText(real.toString());

            ObservableList<Project> items = FXCollections.observableArrayList();

            // Add parent entry if parent exists
            Path parent = real.getParent();
            System.out.println("Parent: " + parent.toString());
            if (parent != null) {
                // create a synthetic FileItem representing ".."
                Project up = new Project("back", "..", "size", parent);
                items.add(up);
            }

            try (Stream<Path> stream = Files.list(real)) {
                stream.sorted(Comparator.comparing((Path p) -> !Files.isDirectory(p))
                                .thenComparing(p -> p.getFileName().toString(), String.CASE_INSENSITIVE_ORDER))
                        .forEach(p -> items.add(new Project("id", p.getFileName().toString(), getFileSize(p), p)));
            }

            localFileTable.setItems(items);

        } catch (IOException e) {
            // on error, show alert and do not change current path
            Alert a = new Alert(Alert.AlertType.ERROR, "Cannot open folder: " + e.getMessage(), ButtonType.OK);
            a.showAndWait();
        }
    }

    private void loadPath(Loader loader) {
        if (loader instanceof LocalLoader) {
            logger.info("Loading local projects...");
            localLoader.loadTo(localFileTable, localStatusLabel, LOCAL_SOURCE);
            logger.info("Local projects are loaded");
        } else if (loader instanceof CloudLoader) {
            logger.info("Loading cloud projects...");
            cloudLoader.loadTo(cloudFileTable, cloudStatusLabel, CLOUD_SOURCE);
            logger.info("Cloud projects are loaded");
        } else {
            throw new IllegalStateException("Loader should be an instance of LoaderImpl class");
        }
    }

    private String getFileSize(Path path) {
        try {
            return humanReadableByteCount(Files.size(path));
        } catch (IOException e) {
            return "0";
        }

    }
}