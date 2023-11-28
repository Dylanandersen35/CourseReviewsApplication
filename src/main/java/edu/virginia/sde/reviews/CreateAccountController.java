package edu.virginia.sde.reviews;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

import java.util.*;

public class CreateAccountController {
    @FXML
    private TextField newUsername;
    @FXML
    private PasswordField newPassword;
    @FXML
    private Button createAccountButton;
    @FXML
    private Label errorMessage;
    private Stage primaryStage;
    private List<User> users;

    public void initialize() {
        newUsername.setText("");
        newPassword.setText("");
        errorMessage.setText("");
        errorMessage.setStyle("-fx-text-fill: red");
    }
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setUsers(List<User> users) { this.users = users; }

    public boolean usernameExists() {
        var username = newUsername.getText();
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    @FXML
    public void handleCreateAccountButton() {
        if (usernameExists()) {
            errorMessage.setText("Error: username already exists");
            return;
        }
        if (newPassword.getText().length() < 8) {
            errorMessage.setText("Error: password must be more than 8 characters");
            return;
        }

        // add new user to database with username and password
        User newUser = new User(newUsername.getText(), newPassword.getText());
        users.add(newUser);

        var service = new UserService();
        service.save(users);

        try {
            var newUsers = service.retrieveUsers();
            var loginPage = new FXMLLoader(CourseReviewsApplication.class.getResource("login-page.fxml"));
            var scene = new Scene(loginPage.load());
            var controller = (LoginPageController) loginPage.getController();
            controller.setPrimaryStage(primaryStage);
            controller.setUsers(newUsers);
            primaryStage.setTitle("Course Reviews");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
