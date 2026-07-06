package com.vasvince.project_loader.impl;

import com.vasvince.project_loader.Folder;
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
    public void loadTo(TableView<Folder> table, Label status, String source) {
        validate(table, status);
        Collection<Folder> list;
        try {
            list = getFolderList();
        } catch (IOException e) {
            logger.info("Exception occurred during project listing: \n {}", e.getLocalizedMessage());
            list = List.of();
        }

        ObservableList<Folder> obs = FXCollections.observableArrayList(list);
        table.setItems(obs);
        status.setText(source);
    }

    protected abstract void validate(TableView<Folder> table, Label status);
    protected abstract Collection<Folder> getFolderList() throws IOException;
    protected Collection<Folder> convert(Collection<T> items) {
        return items.stream()
                .map(this::convertItem)
                .toList();
    }

    protected Folder convertItem(T item) {
        logger.warn("Function not implemented");
        return null;
    }

    public static String humanReadableByteCount(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "i";
        return String.format("%.1f %sB", bytes / Math.pow(1024, exp), pre);
    }
}
