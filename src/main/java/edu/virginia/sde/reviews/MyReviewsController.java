package edu.virginia.sde.reviews;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class MyReviewsController {
    @FXML
    private TableView<Course> reviewsTable;
    @FXML
    private TableColumn<Course, String> subjectColumn;
    @FXML
    private TableColumn<Course, Integer> numberColumn;
    @FXML
    private TableColumn<Course, String> titleColumn;
    @FXML
    private TableColumn<Course, Double> ratingColumn;
    private Stage primaryStage;
    private List<User> users;
    private List<Course> courses;
    private List<Review> reviews;
    private User activeUser;
    private int activeUserID;
    private UserService userService;

    public void initialize() {
        var reviewsService = new ReviewsService();
        reviews = reviewsService.retrieveReviews();

        subjectColumn.setCellValueFactory(new PropertyValueFactory<Course, String>("subject"));
        numberColumn.setCellValueFactory(new PropertyValueFactory<Course, Integer>("courseNumber"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<Course, String>("title"));
        ratingColumn.setCellValueFactory(new PropertyValueFactory<Course, Double>("rating"));
        ratingColumn.setCellFactory(tc -> new TableCell<Course, Double>() {
            @Override
            protected void updateItem(Double rating, boolean empty) {
                super.updateItem(rating, empty);
                setText(empty || rating == 0.0 ? "" : rating.toString());
            }
        });

        reviewsTable.setRowFactory(tv -> {
            TableRow<Course> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (!row.isEmpty())) {
                    Course rowCourse = row.getItem();
                    handleRowClick(rowCourse);
                }
            });
            return row;
        });
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


    public void coursesReviewedByUser(String username) {
        ObservableList<Course> tableCourses = FXCollections.observableArrayList();
        userService = new UserService();
        activeUserID = userService.retrieveUserID(username);

        for (Review review : reviews) {
            if (review.getUserID() == activeUserID) {
                var courseID = review.getCourseID();
                var courseService = new CoursesService();
                var currCourse = courseService.retrieveCourseByID(courseID);
                var reviewedCourse = new Course(currCourse.getSubject(), currCourse.getCourseNumber(), currCourse.getTitle(), review.getRating());
                tableCourses.add(reviewedCourse);
            }
        }
        reviewsTable.setItems(tableCourses);
    }

    @FXML
    public void handleCourseSearchButton() {
        try {
            var courseSearchPage = new FXMLLoader(CourseSearchController.class.getResource("course-search.fxml"));
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
    public void handleLogOutButton() {
        try {
            var logoutPage = new FXMLLoader(CourseReviewsApplication.class.getResource("login-page.fxml"));
            var scene = new Scene(logoutPage.load());
            var controller = (LoginPageController) logoutPage.getController();
            controller.setPrimaryStage(primaryStage);
            controller.setUsers(users);
            primaryStage.setTitle("Course Reviews");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleRowClick(Course course) {
        try {
            var courseReview = new FXMLLoader(ReviewController.class.getResource("course-reviews.fxml"));
            var scene = new Scene(courseReview.load());
            var controller = (ReviewController) courseReview.getController();
            controller.setPrimaryStage(primaryStage);
            controller.setActiveUser(activeUser);
            controller.setUsers(users);
            controller.setCurrentCourse(course);
            controller.setCourseInformation(course);
            controller.setUpTable(course);
            controller.setUpButtons();
            primaryStage.setTitle("Course Reviews");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
