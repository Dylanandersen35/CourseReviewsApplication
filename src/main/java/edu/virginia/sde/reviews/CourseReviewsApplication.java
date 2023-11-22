package edu.virginia.sde.reviews;

import javafx.application.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.desktop.AppForegroundListener;
import java.io.IOException;

public class CourseReviewsApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loginPage = new FXMLLoader(
                CourseReviewsApplication.class.getResource("login-page.fxml"));
        Scene scene = new Scene(loginPage.load());
        stage.setTitle("Course Reviews");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
