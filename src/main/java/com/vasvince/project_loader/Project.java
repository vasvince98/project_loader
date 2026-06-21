package com.vasvince.project_loader;

import javafx.beans.property.SimpleStringProperty;

public class Project {

    private final SimpleStringProperty id;
    private final SimpleStringProperty name;
    private final SimpleStringProperty size;
    private final SimpleStringProperty modified;

    public Project(String id, String name, String size, String modified) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.size = new SimpleStringProperty(size);
        this.modified = new SimpleStringProperty(modified);
    }

    public SimpleStringProperty idProperty() { return id; }
    public SimpleStringProperty nameProperty() { return name; }
    public SimpleStringProperty sizeProperty() { return size; }
    public SimpleStringProperty modifiedProperty() { return modified; }

    public String getName() { return name.get(); }

    public String getId() {
        return id.get();
    }
}
