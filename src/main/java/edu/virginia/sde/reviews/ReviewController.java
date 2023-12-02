package edu.virginia.sde.reviews;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

public class ReviewController {

    @FXML
    private Text courseInfo;
    @FXML
    private Text averageRating;
    @FXML
    private ListView<Review> reviewsList;
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
    private Course currentCourse;
    private User currentUser;
    private String mode;
    private List<User> users;
    private Stage primaryStage;

    public void initialize() {
        courseInfo.setText(currentCourse.getSubject() + " " + currentCourse.getCourseNumber() + ": " + currentCourse.getTitle());
        averageRating.setText(String.format("%.2f", reviewsService.getAverageRating(currentCourse.getCourseNumber())));
        refreshReviewsList();

        Review userReview = reviewsService.getUserReview(currentUser, currentCourse);
        deleteButton.setDisable(userReview == null);
        editButton.setDisable(userReview == null);
    }

    @FXML
    private void handleSubmitButtonAction() {
        try {
            int rating = Integer.parseInt(ratingField.getText());
            String comment = commentField.getText();

            if (rating < 1 || rating > 5) {
                showAlert("Invalid Rating", "Rating must be between 1 and 5.");
                return;
            }

            if ("Edit".equals(mode)) {
                Review userReview = reviewsService.getUserReview(currentUser, currentCourse);
                userReview.setRating(rating);
                userReview.setReview(comment);
                userReview.setTimestamp(System.currentTimeMillis());
                reviewsService.updateReview(userReview);
            } else {
                if (reviewsService.hasReviewed(currentUser, currentCourse)) {
                    showAlert("Review Exists", "You have already reviewed this course.");
                    return;
                }

                Review newReview = new Review(currentUser.getId(), currentCourse.getCourseNumber(), comment, rating, System.currentTimeMillis());
                reviewsService.addReview(newReview);
            }

            refreshReviewsList();

        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter a valid rating.");
        }
    }

    @FXML
    private void handleDeleteButtonAction() {
        List<Review> allReviews = reviewsService.retrieveReviews();
        Review userReview = allReviews.stream()
                .filter(review -> review.getUserID() == currentUser.getId() && review.getCourseID() == currentCourse.getCourseNumber())
                .findFirst()
                .orElse(null);
        if (userReview != null) {
            reviewsService.deleteReview(userReview);
            refreshReviewsList();
        }
    }

    @FXML
    private void handleEditButtonAction() {
        List<Review> allReviews = reviewsService.retrieveReviews();
        Review userReview = allReviews.stream()
                .filter(review -> review.getUserID() == currentUser.getId() && review.getCourseID() == currentCourse.getCourseNumber())
                .findFirst()
                .orElse(null);
        if (userReview != null) {
            ratingField.setText(String.valueOf(userReview.getRating()));
            commentField.setText(userReview.getReview());
            setMode("Edit");
        }
    }

    private void refreshReviewsList() {
        reviewsList.getItems().clear();
        List<Review> allReviews = reviewsService.retrieveReviews();
        List<Review> courseReviews = allReviews.stream()
                .filter(review -> review.getCourseID() == currentCourse.getCourseNumber())
                .collect(Collectors.toList());
        reviewsList.getItems().addAll(courseReviews);
    }

    @FXML
    private void handleBackButtonAction() {
        //still need to setup
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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