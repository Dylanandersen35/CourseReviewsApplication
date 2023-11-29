package edu.virginia.sde.reviews;

import javafx.stage.Stage;

import java.util.List;

public class MyReviewsController {
    private Stage primaryStage;
    private List<User> users;
    private List<Course> courses;
    private User activeUser;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void setActiveUser(User activeUser) { this.activeUser = activeUser; }

    public void setCourses(List<Course> courses) {
            this.courses = courses;
    }

}
