package com.vasvince.project_loader.actions;

import com.vasvince.project_loader.Folder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

import static com.vasvince.project_loader.impl.LoaderImpl.humanReadableByteCount;

public class NavigationActions {

    public void populateLocalTable(Path path, Label localStatusLabel, TableView<Folder> localFileTable) {
        try {
            Path real = path.toRealPath();
            localStatusLabel.setText(real.toString());

            ObservableList<Folder> items = FXCollections.observableArrayList();

            // Add parent entry if parent exists
            Path parent = real.getParent();
            if (parent != null) {
                // create a synthetic FileItem representing ".."
                Folder up = new Folder("back", "..", "size", parent);
                items.add(up);
            }

            try (Stream<Path> stream = Files.list(real)) {
                stream.sorted(Comparator.comparing((Path p) -> !Files.isDirectory(p))
                                .thenComparing(p -> p.getFileName().toString(), String.CASE_INSENSITIVE_ORDER))
                        .forEach(p -> items.add(new Folder("id", p.getFileName().toString(), getFileSize(p), p)));
            }

            localFileTable.setItems(items);

        } catch (IOException e) {
            //TODO: use this alert method for own exception handling
            Alert a = new Alert(Alert.AlertType.ERROR, "Cannot open folder: " + e.getMessage(), ButtonType.OK);
            a.showAndWait();
        }
    }


}
