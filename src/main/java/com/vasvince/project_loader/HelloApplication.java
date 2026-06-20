package com.vasvince.project_loader;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/vasvince/project_loader/landing_page.fxml"));
        Parent root = loader.load();
        HelloController controller = loader.getController();

        List<String> args = getParameters().getRaw();
        String logicWorkDir = "/Users/vasvince/Music/Logic_test";
        String path1 = args.size() >= 1 ? args.get(0) : logicWorkDir;
        String path2 = args.size() >= 2 ? args.get(1) : logicWorkDir;
        controller.loadPaths(path1, path2);

        stage.setTitle("File Landing");
        stage.setScene(new Scene(root, 800, 500));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}