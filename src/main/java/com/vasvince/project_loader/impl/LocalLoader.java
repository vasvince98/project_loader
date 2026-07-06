package com.vasvince.project_loader.impl;

import com.vasvince.project_loader.Folder;
import javafx.collections.FXCollections;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Collection;
import java.util.stream.Stream;

import static com.vasvince.project_loader.enums.LoaderEnums.UNINITIALIZED_ID;

public class LocalLoader extends LoaderImpl<Folder> {

    public LocalLoader(Path path) {
        super(path);
    }

    @Override
    protected void validate(TableView<Folder> table, Label status) {
        if (!Files.exists(path) || !Files.isDirectory(path)) {
            status.setText("Invalid directory: " + path);
            table.setItems(FXCollections.observableArrayList());
        }
    }

    @Override
    protected Collection<Folder> getFolderList() throws IOException {
        try (Stream<Path> stream = Files.list(path)) {
            return stream
                    .filter(Files::isDirectory)
                    .map(pth -> {
                        try {
                            long size;
                            String name = pth.getFileName().toString();
                            try (Stream<Path> walk = Files.walk(pth)) {
                                size = walk.filter(Files::isRegularFile)
                                        .mapToLong(sub -> {
                                            try { return Files.size(sub); }
                                            catch (IOException e) { return 0L; }
                                        })
                                        .sum();
                            }

                            String sizeStr = humanReadableByteCount(size);
                            return new Folder(UNINITIALIZED_ID, name, sizeStr, pth);
                        } catch (IOException e) {
                            return new Folder(UNINITIALIZED_ID, pth.getFileName().toString(), "?", pth);
                        }
                    })
                    .sorted((a, b) -> a.getName().compareToIgnoreCase(b.getName()))
                    .toList();
        }
    }

    @Override
    public void execute(TableView<Folder> fileTable) {
        //TODO: implement upload
    }
}
