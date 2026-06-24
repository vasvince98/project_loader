package com.vasvince.project_loader;

import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;

import java.nio.file.Files;
import java.nio.file.Path;

public class Project {

    private final Path path;
    private final SimpleStringProperty id;
    private final SimpleStringProperty name;
    private final SimpleStringProperty size;
    private final ReadOnlyBooleanWrapper isDirectory;



    public Project(String id, String name, String size, Path path) {
        this.id = new SimpleStringProperty(id);
        this.name = new ReadOnlyStringWrapper(name);
        this.size = new SimpleStringProperty(size);
        this.path = path;
        boolean isDir = Files.isDirectory(path);
        this.isDirectory = new ReadOnlyBooleanWrapper(isDir);
    }


    public Path getPath() {
        return path;
    }

    public String getId() {
        return id.get();
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public String getSize() {
        return size.get();
    }

    public SimpleStringProperty sizeProperty() {
        return size;
    }

    public boolean isDirectory() {
        return isDirectory.get();
    }

    public ReadOnlyBooleanWrapper isDirectoryProperty() {
        return isDirectory;
    }

    @Override
    public String toString() {
        return "Project{" +
                "path=" + path +
                ", id=" + id +
                ", name=" + name +
                ", size=" + size +
                ", isDirectory=" + isDirectory +
                '}';
    }
}
