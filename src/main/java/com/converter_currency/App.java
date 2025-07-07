package com.converter_currency;

import java.io.IOException;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

@Override
    @SuppressWarnings("CallToPrintStackTrace")
public void start(Stage stage) throws IOException {
    // Load loading.fxml dulu
    Parent loadingRoot = loadFXML("primary");
    Scene loadingScene = new Scene(loadingRoot, 430, 694);
    stage.setScene(loadingScene);
    stage.show();

    // Setelah 7 detik, ganti ke primary2.fxml
    PauseTransition delay = new PauseTransition(Duration.seconds(7));
    delay.setOnFinished(event -> {
        try {
            Parent mainRoot = loadFXML("primary2");
            Scene mainScene = new Scene(mainRoot, 430, 694);
            stage.setScene(mainScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    });
    delay.play();
}

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("views/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}