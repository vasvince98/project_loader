package com.vasvince.project_loader;

import javafx.beans.property.SimpleStringProperty;

public class FileEntry {

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
