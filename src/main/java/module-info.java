module com.vasvince.project_loader {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires org.slf4j;
    requires com.google.api.client;
    requires com.google.api.client.json.gson;
    requires com.google.api.services.drive;
    requires com.google.api.client.auth;
    requires google.api.client;
    requires com.google.api.client.extensions.jetty.auth;
    requires com.google.api.client.extensions.java6.auth;
    requires jdk.httpserver;
    requires jdk.jshell;

    opens com.vasvince.project_loader to javafx.fxml;
    exports com.vasvince.project_loader;
    exports com.vasvince.project_loader.services;
    exports com.vasvince.project_loader.impl;
    opens com.vasvince.project_loader.services to javafx.fxml;
}