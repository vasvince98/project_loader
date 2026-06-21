package com.vasvince.project_loader;

import com.vasvince.project_loader.impl.CloudLoader;
import com.vasvince.project_loader.impl.LocalLoader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.nio.file.Path;
import java.util.List;

import static com.vasvince.project_loader.enums.LoaderEnums.LOGIC_WORK_DIR;

public class LoaderApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/vasvince/project_loader/landing_page.fxml"));
        Parent root = loader.load();
        LoaderController controller = loader.getController();

        List<String> args = getParameters().getRaw();

        String localPath = args.isEmpty() ? LOGIC_WORK_DIR : args.get(0);
        String cloudPath = args.size() >= 2 ? args.get(1) : LOGIC_WORK_DIR;

        controller.initializeLoaders(new LocalLoader(Path.of(localPath)), new CloudLoader(Path.of(cloudPath)));

        stage.setTitle("File Landing");
        stage.setScene(new Scene(root, 800, 500));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}