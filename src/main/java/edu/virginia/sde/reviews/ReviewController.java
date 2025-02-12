package edu.virginia.sde.reviews;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;
import java.util.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class ReviewController {

    @FXML
    private Text courseInfo;
    @FXML
    private Text averageRating;
    @FXML
    private TableView<Review> reviewsTable;
    @FXML
    private TableColumn<Review, String> timestampColumn;
    @FXML
    private TableColumn<Review, String> reviewColumn;
    @FXML
    private TableColumn<Review, Integer> ratingColumn;
    @FXML
    private TextField ratingField;
    @FXML
    private TextArea commentField;
    @FXML
    private Button submitButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button editButton;
    @FXML
    private Button backButton;

    private ReviewsService reviewsService;
    private CoursesService coursesService;
    private Course currentCourse;
    private User currentUser;
    private String mode;
    private List<User> users;
    private Stage primaryStage;
    private int currentCourseID;
    private int activeUserID;

    public void initialize() {
        timestampColumn.setCellValueFactory(new PropertyValueFactory<Review, String>("Timestamp"));
        reviewColumn.setCellValueFactory(new PropertyValueFactory<Review, String>("Review"));
        ratingColumn.setCellValueFactory(new PropertyValueFactory<Review, Integer>("Rating"));

        reviewColumn.setCellFactory(column -> {
            var cell = new TableCell<Review, String>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(reviewColumn.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell;
        });

        reviewsService = new ReviewsService();
        coursesService = new CoursesService();
    }

    @FXML
    private void handleSubmitButtonAction() {
        System.out.println(mode);
        try {
            int rating = Integer.parseInt(ratingField.getText());
            String comment = commentField.getText();

            if (rating < 1 || rating > 5) {
                showAlert("Invalid Rating", "Rating must be between 1 and 5.");
                return;
            }

            if ("Edit".equals(mode)) {
                Review userReview = reviewsService.getUserReview(activeUserID, currentCourseID);
                userReview.setRating(rating);
                userReview.setReview(comment);
                userReview.setTimestamp((new Timestamp(System.currentTimeMillis())).toString());
                reviewsService.updateReview(userReview);
                setUpTable(currentCourse);
                setMode("submit");
                commentField.setStyle("-fx-border-color: gray");
                ratingField.setStyle("-fx-border-color: gray");
                commentField.setDisable(true);
                ratingField.setDisable(true);
            } else {
                if (reviewsService.hasReviewed(activeUserID, currentCourseID)) {
                    showAlert("Review Exists", "You have already reviewed this course. Use edit button to change review");
                    return;
                }

                Review newReview = new Review(activeUserID, coursesService.retrieveCourseID(currentCourse), comment, rating, (new Timestamp(System.currentTimeMillis())).toString());
                reviewsService.addReview(newReview);
                setUpButtons();
            }

            setUpTable(currentCourse);
            var updatedRating = reviewsService.getAverageRating(currentCourseID);
            coursesService.updateCourseRating(updatedRating, currentCourseID);
            averageRating.setText(String.format("%.2f", updatedRating));

        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter a valid rating. Make sure there are no blank spaces");
        }
    }

    @FXML
    private void handleDeleteButtonAction() {
        List<Review> allReviews = reviewsService.retrieveReviews();
        Review userReview = allReviews.stream()
                .filter(review -> review.getUserID() == activeUserID && review.getCourseID() == currentCourseID)
                .findFirst()
                .orElse(null);
        if (userReview != null) {
            reviewsService.deleteReview(userReview);
            setUpTable(currentCourse);
            var updatedRating = reviewsService.getAverageRating(currentCourseID);
            coursesService.updateCourseRating(updatedRating, currentCourseID);
            averageRating.setText(String.format("%.2f", updatedRating));
            setUpButtons();
            ratingField.setText("");
            commentField.setText("");
            setMode("Delete");
        }

    }

    @FXML
    private void handleEditButtonAction() {
        List<Review> allReviews = reviewsService.retrieveReviews();
        Review userReview = allReviews.stream()
                .filter(review -> review.getUserID() == activeUserID && review.getCourseID() == currentCourseID)
                .findFirst()
                .orElse(null);
        if (userReview != null) {
            setMode("Edit");
            commentField.setDisable(false);
            ratingField.setDisable(false);
            commentField.setStyle("-fx-border-color: red");
            ratingField.setStyle("-fx-border-color: red");
        }
    }

    @FXML
    private void handleBackButtonAction() {
        try {
            var courseSearchPage = new FXMLLoader(CourseSearchController.class.getResource("course-search.fxml"));
            var scene = new Scene(courseSearchPage.load());
            var controller = (CourseSearchController) courseSearchPage.getController();
            controller.setPrimaryStage(primaryStage);
            controller.setUsers(users);
            controller.setActiveUser(currentUser);
            primaryStage.setTitle("Course Search");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setCourseInformation(Course course) {
        courseInfo.setText(course.getSubject() + " " + course.getCourseNumber() + ": " + course.getTitle());
        var courseService = new CoursesService();
        var courseID = courseService.retrieveCourseID(course);
        var reviewsService = new ReviewsService();
        var rating = reviewsService.getAverageRating(courseID);
        averageRating.setText(rating == 0.0 ? "No ratings" : String.format("%.2f", rating));
    }

    public void setUpTable(Course course) {
        ObservableList<Review> tableCourses = FXCollections.observableArrayList();
        var courseService = new CoursesService();
        currentCourseID = courseService.retrieveCourseID(course);

        var reviewService = new ReviewsService();
        var tableReviews = reviewService.retrieveReviewsByCourseID(currentCourseID);
        for (Review review : tableReviews) {
            tableCourses.add(review);
        }


        reviewsTable.setItems(tableCourses);
    }

    public void setCourseAverageRating(Course course) {
        var courseService = new CoursesService();
        var courseID = courseService.retrieveCourseID(course);
        var reviewsService = new ReviewsService();
        var rating = reviewsService.getAverageRating(courseID);
        averageRating.setText(String.format("%.2f", rating));
    }

    public void setUpButtons() {
        var reviewsService = new ReviewsService();
        Review userReview = reviewsService.getUserReview(activeUserID, currentCourseID);
        deleteButton.setDisable(userReview == null);
        editButton.setDisable(userReview == null);
        ratingField.setDisable(userReview != null);
        commentField.setDisable(userReview != null);
    }

    public void getCurrentUserID(String username) {
        var userService = new UserService();
        activeUserID = userService.retrieveUserID(username);
    }

    public void setCommentAndRatingField() {
        var reviewsService = new ReviewsService();
        Review userReview = reviewsService.getUserReview(activeUserID, currentCourseID);
        if (userReview != null) {
            commentField.setText(userReview.getReview());
            ratingField.setText("" + userReview.getRating());
        }
    }



    public void setCurrentCourse(Course currentCourse) {
        this.currentCourse = currentCourse;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void setActiveUser(User activeUser) {
        this.currentUser = activeUser;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}