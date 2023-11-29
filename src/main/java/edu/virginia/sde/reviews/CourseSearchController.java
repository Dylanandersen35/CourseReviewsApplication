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
    private TableColumn<Course, Integer> ratingColumn;
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
        ratingColumn.setCellValueFactory(new PropertyValueFactory<Course, Integer>("rating"));
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

        Course newCourse = new Course(subjectAdd.getText().toUpperCase(), Integer.parseInt(numberAdd.getText()), titleAdd.getText(), -1);
        courses.add(newCourse);

        var service = new CoursesService();
        service.save(courses);

        courseTable.setItems(getCourses());

        addError.setText("");
    }

    @FXML
    public void handleMyReviewsButton() {
        try {
            var myReviewsPage = new  FXMLLoader(MyReviewsController.class.getResource("my-reviews.fxml"));
            var scene = new Scene(myReviewsPage.load());
            var controller = (MyReviewsController) myReviewsPage.getController();
            controller.setPrimaryStage(primaryStage);
            controller.setUsers(users);
            controller.setCourses(courses);
            controller.setActiveUser(activeUser);
            primaryStage.setTitle("My Reviews");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
