module com.vasvince.project_loader {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.vasvince.project_loader to javafx.fxml;
    exports com.vasvince.project_loader;
}