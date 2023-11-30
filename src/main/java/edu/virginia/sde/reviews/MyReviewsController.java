package edu.virginia.sde.reviews;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;

public class MyReviewsController {
    @FXML
    private TableView<Review> reviewsTable;
    @FXML
    private TableColumn<Review, String> subjectColumn;
    @FXML
    private TableColumn<Review, Integer> numberColumn;
    @FXML
    private TableColumn<Review, String> titleColumn;
    @FXML
    private TableColumn<Review, Integer> ratingColumn;
    private Stage primaryStage;
    private List<User> users;
    private List<Course> courses;
    private List<Review> reviews;
    private User activeUser;

    public void initialize() {
        var reviewsService = new ReviewsService();
        reviews = reviewsService.retrieveReviews();

        //subjectColumn.setCellValueFactory(new PropertyValueFactory<Review, String>("subject"));
        //numberColumn.setCellValueFactory(new PropertyValueFactory<Review, Integer>("courseNumber"));
        //titleColumn.setCellValueFactory(new PropertyValueFactory<Review, String>("title"));
        //ratingColumn.setCellValueFactory(new PropertyValueFactory<Review, Integer>("rating"));
        //reviewsTable.setItems(getReviews());
    }

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


    /*
    public ObservableList<Review> getReviews() {
        ObservableList<Review> tableReviews = FXCollections.observableArrayList();
        for (Review review : reviews) {
            tableReviews.add(review);
        }
        return tableReviews;
    }

     */

}
