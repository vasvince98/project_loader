package com.vasvince.project_loader.impl;

import com.vasvince.project_loader.FileEntry;
import com.vasvince.project_loader.api.Loader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public abstract class LoaderImpl<T> implements Loader {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    protected final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneId.systemDefault());

    protected final Path path;

    protected LoaderImpl(final Path path) {
        Objects.requireNonNull(path, "path is null");
        this.path = path;
    }

    @Override
    public void loadTo(TableView<FileEntry> table, Label status, String source) {
        validate(table, status);
        Collection<FileEntry> list;
        try {
            list = getProjectList();
        } catch (IOException e) {
            logger.info("Exception occurred during project listing: \n {}", e.getLocalizedMessage());
            list = List.of();
        }

        ObservableList<FileEntry> obs = FXCollections.observableArrayList(list);
        table.setItems(obs);
        status.setText(source);
    }

    protected abstract void validate(TableView<FileEntry> table, Label status);
    protected abstract Collection<FileEntry> getProjectList() throws IOException;
    protected Collection<FileEntry> convert(Collection<T> items) {
        return items.stream()
                .map(this::convertItem)
                .toList();
    }

    protected FileEntry convertItem(T item) {
        logger.warn("Function not implemented");
        return null;
    }

    protected static String humanReadableByteCount(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "i";
        return String.format("%.1f %sB", bytes / Math.pow(1024, exp), pre);
    }
}
