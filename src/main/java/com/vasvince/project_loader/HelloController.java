package com.vasvince.project_loader;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.beans.property.SimpleStringProperty;

import java.io.IOException;
import java.nio.file.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class HelloController {

    @FXML private TextField pathField1;
    @FXML private TextField pathField2;

    @FXML private TableView<FileEntry> filesTable1;
    @FXML private TableColumn<FileEntry, String> nameCol1;
    @FXML private TableColumn<FileEntry, String> sizeCol1;
    @FXML private TableColumn<FileEntry, String> modifiedCol1;
    @FXML private Label statusLabel1;

    @FXML private TableView<FileEntry> filesTable2;
    @FXML private TableColumn<FileEntry, String> nameCol2;
    @FXML private TableColumn<FileEntry, String> sizeCol2;
    @FXML private TableColumn<FileEntry, String> modifiedCol2;
    @FXML private Label statusLabel2;

    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneId.systemDefault());

    @FXML
    public void initialize() {
        nameCol1.setCellValueFactory(c -> c.getValue().nameProperty());
        sizeCol1.setCellValueFactory(c -> c.getValue().sizeProperty());
        modifiedCol1.setCellValueFactory(c -> c.getValue().modifiedProperty());

        nameCol2.setCellValueFactory(c -> c.getValue().nameProperty());
        sizeCol2.setCellValueFactory(c -> c.getValue().sizeProperty());
        modifiedCol2.setCellValueFactory(c -> c.getValue().modifiedProperty());
    }

    public void loadPaths(String path1, String path2) {
        pathField1.setText(path1);
        pathField2.setText(path2);
        loadTo(filesTable1, statusLabel1, path1);
        loadTo(filesTable2, statusLabel2, path2);
    }

    private void loadTo(TableView<FileEntry> table, Label status, String pathStr) {
        Path p = Paths.get(pathStr);
        if (!Files.exists(p) || !Files.isDirectory(p)) {
            status.setText("Invalid directory: " + pathStr);
            table.setItems(FXCollections.observableArrayList());
            return;
        }

        try {
            List<FileEntry> list = Files.list(p)
                    .map(pth -> {
                        try {
                            String name = pth.getFileName().toString();
                            boolean dir = Files.isDirectory(pth);
                            long size = dir ? 0L : Files.size(pth);
                            String sizeStr = dir ? "<DIR>" : humanReadableByteCount(size);
                            String mod = fmt.format(Instant.ofEpochMilli(Files.getLastModifiedTime(pth).toMillis()));
                            return new FileEntry(name, sizeStr, mod);
                        } catch (IOException e) {
                            return new FileEntry(pth.getFileName().toString(), "?", "?");
                        }
                    })
                    .sorted((a, b) -> a.getName().compareToIgnoreCase(b.getName()))
                    .collect(Collectors.toList());

            ObservableList<FileEntry> obs = FXCollections.observableArrayList(list);
            table.setItems(obs);
            status.setText("Listing: " + pathStr);
        } catch (IOException e) {
            status.setText("Error reading directory: " + e.getMessage());
            table.setItems(FXCollections.observableArrayList());
        }
    }

    private static String humanReadableByteCount(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "i";
        return String.format("%.1f %sB", bytes / Math.pow(1024, exp), pre);
    }

    public static class FileEntry {
        private final SimpleStringProperty name;
        private final SimpleStringProperty size;
        private final SimpleStringProperty modified;

        public FileEntry(String name, String size, String modified) {
            this.name = new SimpleStringProperty(name);
            this.size = new SimpleStringProperty(size);
            this.modified = new SimpleStringProperty(modified);
        }

        public SimpleStringProperty nameProperty() { return name; }
        public SimpleStringProperty sizeProperty() { return size; }
        public SimpleStringProperty modifiedProperty() { return modified; }

        public String getName() { return name.get(); }
    }
}