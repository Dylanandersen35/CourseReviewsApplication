package edu.virginia.sde.reviews;

import javafx.application.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.desktop.AppForegroundListener;
import java.io.IOException;

import java.sql.SQLException;

public class CourseReviewsApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        var userService = new UserService();
        var users = userService.retrieveUsers();

        var loginPage = new FXMLLoader(CourseReviewsApplication.class.getResource("login-page.fxml"));
        var scene = new Scene(loginPage.load());
        var controller = (LoginPageController) loginPage.getController();
        controller.setPrimaryStage(primaryStage);
        controller.setUsers(users);
        primaryStage.setTitle("Course Reviews");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) throws SQLException{
        launch(args);
    }
}
