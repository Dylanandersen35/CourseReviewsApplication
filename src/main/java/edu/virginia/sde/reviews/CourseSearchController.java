package edu.virginia.sde.reviews;

import javafx.stage.Stage;

import java.util.List;

public class CourseSearchController {
    private Stage primaryStage;

    private List<User> users;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
