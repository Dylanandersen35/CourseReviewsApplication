package edu.virginia.sde.reviews;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

import java.util.*;

public class LoginPageController {
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private Button signInButton;
    @FXML
    private Button signUpButton;
    @FXML
    private Label errorMessage;
    private Stage primaryStage;
    private List<User> users;
    //private User activeUser;

    public void initialize() {
        usernameField.setText("");
        passwordField.setText("");
        errorMessage.setText("");
        errorMessage.setStyle("-fx-text-fill: red");
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }


    public boolean usernameDoesNotExist() {
        var attemptedUsername = usernameField.getText();
        var attemptedPassword = passwordField.getText();

        for (User user : users) {
            if (attemptedUsername.equals(user.getUsername())) {
                return false;
            }
        }
        return true;
    }

    public boolean passwordDoesNotMatch() {
        var attemptedUsername = usernameField.getText();
        var attemptedPassword = passwordField.getText();

        for (User user : users) {
            if (attemptedUsername.equals(user.getUsername())) {
                if (!attemptedPassword.equals(user.getPassword())) {
                    return true;
                }
            }
        }
        return false;
    }

    public User getActiveUser() {
        for (User user : users) {
            String activeUsername = usernameField.getText();
            if (user.getUsername().equals(activeUsername)) {
                return user;
            }
        }
        return null;
    }

    @FXML
    public void handleSignInButton() {
        if (usernameDoesNotExist()) {
            errorMessage.setText("Error: username does not exist");
            return;
        }
        if (passwordDoesNotMatch()) {
            errorMessage.setText("Error: incorrect password");
            return;
        }

        var activeUser = getActiveUser();
        try {
            var courseSearchPage = new  FXMLLoader(CourseSearchController.class.getResource("course-search.fxml"));
            var scene = new Scene(courseSearchPage.load());
            var controller = (CourseSearchController) courseSearchPage.getController();
            controller.setPrimaryStage(primaryStage);
            controller.setUsers(users);
            controller.setActiveUser(activeUser);
            primaryStage.setTitle("Course Search");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void handleSignUpButton() {
        try {
            var signUpPage = new FXMLLoader(CreateAccountController.class.getResource("new-account.fxml"));
            var createAccountScene = new Scene(signUpPage.load());
            var controller = (CreateAccountController) signUpPage.getController();
            controller.setPrimaryStage(primaryStage);
            controller.setUsers(users);
            primaryStage.setTitle("Create Account");
            primaryStage.setScene(createAccountScene);
            primaryStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void closeApplication() {
        Platform.exit();
    }
}
