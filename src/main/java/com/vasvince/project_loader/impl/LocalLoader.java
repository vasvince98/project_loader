package com.vasvince.project_loader.impl;

import com.vasvince.project_loader.FileEntry;
import javafx.collections.FXCollections;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Collection;
import java.util.stream.Collectors;

public class LocalLoader extends LoaderImpl {

    public LocalLoader(Path path) {
        super(path);
    }

    @Override
    protected void validate(TableView<FileEntry> table, Label status) {
        if (!Files.exists(path) || !Files.isDirectory(path)) {
            status.setText("Invalid directory: " + path);
            table.setItems(FXCollections.observableArrayList());
        }
    }

    @Override
    protected Collection<FileEntry> getProjectList() throws IOException {
        return Files.list(path)
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
    }
}
