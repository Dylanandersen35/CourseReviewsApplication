package edu.virginia.sde.reviews;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.collections.ObservableList;

import java.io.IOException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

public class CourseSearchController {
    @FXML
    private TextField subjectSearch;
    @FXML
    private TextField numberSearch;
    @FXML
    private TextField titleSearch;
    @FXML
    private Label searchError;
    @FXML
    private TextField subjectAdd;
    @FXML
    private TextField numberAdd;
    @FXML
    private TextField titleAdd;
    @FXML
    private Label addError;
    @FXML
    private TableView<Course> courseTable;
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
    private User activeUser;

    public void initialize() {
        subjectSearch.setText("");
        numberSearch.setText("");
        titleSearch.setText("");
        searchError.setText("");
        searchError.setStyle("-fx-text-fill: red");
        subjectAdd.setText("");
        numberAdd.setText("");
        titleAdd.setText("");
        addError.setText("");
        addError.setStyle("-fx-text-fill: red");

        var courseService = new CoursesService();
        courses = courseService.retrieveCourses();

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

        courseTable.setRowFactory(tv -> {
            TableRow<Course> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (!row.isEmpty())) {
                    Course rowCourse = row.getItem();
                    handleRowClick(rowCourse);
                }
            });
            return row;
        });

        courseTable.setItems(getCourses());
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void setActiveUser(User activeUser) { this.activeUser = activeUser; }

    public boolean isValidSubject(String input) {
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (!Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }

    public boolean isValidNumber(String number) {
        for (int i = 0; i < number.length(); i++) {
            char c = number.charAt(i);
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    public boolean inputError() {
        if (subjectAdd.getText().length() < 2 || subjectAdd.getText().length() > 4) {
            addError.setText("Error: incorrect subject input");
            return true;
        }
        if (!isValidSubject(subjectAdd.getText())) {
            addError.setText("Error: incorrect subject input");
            return true;
        }

        if (numberAdd.getText().length() != 4) {
            addError.setText("Error: incorrect number input");
            return true;
        }
        if (!isValidNumber(numberAdd.getText())) {
            addError.setText("Error: incorrect number input");
            return true;
        }

        if (titleAdd.getText().length() < 1 || titleAdd.getText().length() > 50) {
            addError.setText("Error: incorrect title input");
            return true;
        }

        return false;
    }

    public boolean courseExists() {
        var subject = subjectAdd.getText().toUpperCase();
        var courseNumber = Integer.parseInt(numberAdd.getText());
        var title = titleAdd.getText().toUpperCase();
        for (Course course : courses) {
            if (courseNumber == course.getCourseNumber() && subject.equals(course.getSubject())
                    && title.equals(course.getTitle().toUpperCase())) {
                addError.setText("Error: course exists");
                return true;
            }
        }
        return false;
    }

    public ObservableList<Course> getCourses() {
        ObservableList<Course> tableCourses = FXCollections.observableArrayList();
        for (Course course : courses) {
            var courseService = new CoursesService();
            var courseID = courseService.retrieveCourseID(course);
            var reviewsService = new ReviewsService();
            var rating = reviewsService.getAverageRating(courseID);
            BigDecimal bd = BigDecimal.valueOf(rating);
            bd = bd.setScale(2, RoundingMode.HALF_UP);
            course.setRating(bd.doubleValue());
            tableCourses.add(course);
        }
        return tableCourses;
    }

    @FXML
    public void handleAddCourseButton() {
        if (inputError()) {
            return;
        }
        if (courseExists()) {
            return;
        }

        Course newCourse = new Course(subjectAdd.getText().toUpperCase(), Integer.parseInt(numberAdd.getText()), titleAdd.getText(), 0.0);
        courses.add(newCourse);

        var service = new CoursesService();
        service.save(courses);

        courseTable.setItems(getCourses());

        addError.setText("");
    }

    public boolean searchSubjectInputError() {
        if (subjectSearch.getText().equals("")) { return false; }

        if (subjectSearch.getText().length() < 2 || subjectSearch.getText().length() > 4) {
            searchError.setText("Error: incorrect subject input");
            return true;
        }
        if (!isValidSubject(subjectSearch.getText())) {
            searchError.setText("Error: incorrect subject input");
            return true;
        }
        return false;
    }

    public boolean searchNumberInputError() {
        if (numberSearch.getText().equals("")) { return false; }

        if (numberSearch.getText().length() != 4) {
            searchError.setText("Error: incorrect number input");
            return true;
        }
        if (!isValidNumber(numberSearch.getText())) {
            searchError.setText("Error: incorrect number input");
            return true;
        }
        return false;
    }

    public boolean searchTitleInputError() {
        if (titleSearch.getText().equals("")) { return false; }

        if (titleSearch.getText().length() < 1 || titleSearch.getText().length() > 50) {
            searchError.setText("Error: incorrect title input");
            return true;
        }
        return false;
    }

    @FXML
    public void handleSearchButton() {
        var subjectInput = subjectSearch.getText().toUpperCase();
        var numberInput = numberSearch.getText();
        var titleInput = titleSearch.getText().toUpperCase();
        ObservableList<Course> searchedCourses = FXCollections.observableArrayList();

        if (searchSubjectInputError()) { return; }
        if (searchNumberInputError()) { return; }
        if (searchTitleInputError()) { return; }

        for (Course course : courses) {
            if ((course.getSubject().equals(subjectInput) || subjectInput.equals("")) && (
                    numberInput.equals("") || course.getCourseNumber() == Integer.parseInt(numberInput)) && (
                    course.getTitle().toUpperCase().contains(titleInput) || titleInput.equals(""))) {
                searchedCourses.add(course);
            }
        }

        courseTable.setItems(searchedCourses);
    }

    @FXML
    public void handleMyReviewsButton() {
        try {
            var myReviewsPage = new  FXMLLoader(MyReviewsController.class.getResource("my-reviews.fxml"));
            var scene = new Scene(myReviewsPage.load());
            var controller = (MyReviewsController) myReviewsPage.getController();
            controller.setPrimaryStage(primaryStage);
            controller.coursesReviewedByUser(activeUser.getUsername());
            controller.setActiveUser(activeUser);
            controller.setUsers(users);
            controller.setCourses(courses);
            primaryStage.setTitle("My Reviews");
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
            controller.getCurrentUserID(activeUser.getUsername());
            controller.setCurrentCourse(course);
            controller.setCourseInformation(course);
            controller.setUpTable(course);
            controller.setUpButtons();
            controller.setCommentAndRatingField();
            primaryStage.setTitle("Course Reviews");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
