package edu.virginia.sde.reviews;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.sql.*;
import java.util.ArrayList;

import java.util.*;

public class DatabaseDriver {
    public static final String DATABASE_CONNECTION = "jdbc:sqlite:course_reviews.sqlite";
    private Connection connection;

    public DatabaseDriver() throws SQLException {
        connection = DriverManager.getConnection(DATABASE_CONNECTION);
        connection.setAutoCommit(false);
        createTables();
    }

    public void createTables() throws SQLException {
        try {
            /*
            String createUsersTable = """
                    CREATE TABLE IF NOT EXISTS Users (
                    ID INTEGER PRIMARY KEY, 
                    Username TEXT NOT NULL,
                    Password TEXT NOT NULL
                    );
                    """;

            String createCoursesTable = """
                    CREATE TABLE IF NOT EXISTS Courses (
                    ID INTEGER PRIMARY KEY,
                    Subject TEXT NOT NULL,
                    CourseNumber INTEGER NOT NULL,
                    Title TEXT NOT NULL,
                    Rating INTEGER
                    );
                    """;
             */

            var statement = connection.prepareStatement(
                    """
                    CREATE TABLE IF NOT EXISTS Users (
                    ID INTEGER PRIMARY KEY, 
                    Username TEXT NOT NULL,
                    Password TEXT NOT NULL
                    );
            """);

            statement.executeUpdate();
            connection.commit();

            var statement1 = connection.prepareStatement(
                    """
                        CREATE TABLE IF NOT EXISTS Courses (
                        ID INTEGER PRIMARY KEY,
                        Subject TEXT NOT NULL,
                        CourseNumber INTEGER NOT NULL,
                        Title TEXT NOT NULL,
                        Rating INTEGER
                        );
            """);
            statement1.executeUpdate();
            connection.commit();

            var statement2 = connection.prepareStatement(
                    """
                    CREATE TABLE IF NOT EXISTS Reviews (
                    ID INTEGER PRIMARY KEY, 
                    UserID INTEGER NOT NULL, 
                    CourseID INTEGER NOT NULL, 
                    Review TEXT,
                    Rating INTEGER NOT NULL,
                    FOREIGN KEY(UserID) REFERENCES Users(ID) ON DELETE CASCADE, 
                    FOREIGN KEY(CourseID) References Courses(ID) ON DELETE CASCADE
                    );
            """);

            statement2.executeUpdate();
            connection.commit();

            /*
            var createTables = List.of(createUsersTable, createCoursesTable);
            for (var query : createTables) {
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.executeUpdate();
                preparedStatement.close();
            }
             */

        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }

    public List<User> getAllUsers() {
        var users = new ArrayList<User>();
        try {
            var statement = connection.prepareStatement(
                    """
                        SELECT * from Users;
                    """);

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("ID"));
                user.setUsername(rs.getString("Username"));
                user.setPassword(rs.getString("Password"));
                users.add(user);
            }

            return users;

        } catch (SQLException e) {
            return users;
        }
    }

    public void updateUsers(List<User> users) throws SQLException {
        try {
            var clearStatement = connection.prepareStatement("DELETE FROM Users");
            clearStatement.executeUpdate();

            var statement = connection.prepareStatement("INSERT INTO Users(Username, Password) VALUES (?, ?)");
            for (User user : users) {
                statement.setString(1, user.getUsername());
                statement.setString(2, user.getPassword());
                statement.executeUpdate();
            }
            connection.commit();

        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }


    public List<Course> getAllCourses() {
        var courses = new ArrayList<Course>();
        try {
            var statement = connection.prepareStatement(
                    """
                        SELECT * from Courses;
                    """
            );

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Course course = new Course();
                course.setSubject(rs.getString("Subject"));
                course.setCourseNumber(rs.getInt("CourseNumber"));
                course.setTitle(rs.getString("Title"));
                course.setRating(rs.getInt("Rating"));
                courses.add(course);
            }

            return courses;

        } catch (SQLException e) {
            return courses;
        }
    }

    public void updateCourses(List<Course> courses) throws SQLException {
        try {
            var clearStatement = connection.prepareStatement("DELETE FROM Courses");
            clearStatement.executeUpdate();

            var statement = connection.prepareStatement("INSERT INTO Courses(Subject, CourseNumber, Title, Rating) VALUES (?, ?, ?, ?)");
            for (Course course : courses) {
                statement.setString(1, course.getSubject());
                statement.setInt(2, course.getCourseNumber());
                statement.setString(3, course.getTitle());
                statement.setInt(4, course.getRating());
                statement.executeUpdate();
            }
            connection.commit();

        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }

    public List<Review> getAllReviews() {
        var reviews = new ArrayList<Review>();
        try {
            var statement = connection.prepareStatement(
                    """
                        SELECT * from Reviews;
                    """
            );

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Review review = new Review();
                review.setUserID(rs.getInt("UserID"));
                review.setCourseID(rs.getInt("CourseID"));
                review.setReview(rs.getString("Review"));
                review.setRating(rs.getInt("Rating"));
                reviews.add(review);
            }

            return reviews;

        } catch (SQLException e) {
            return reviews;
        }
    }

    public void updateReviews(List<Review> reviews) throws SQLException {
        try {
            var clearStatement = connection.prepareStatement("DELETE FROM Reviews");
            clearStatement.executeUpdate();

            var statement = connection.prepareStatement("INSERT INTO Reviews(UserID, CourseID, Review, Rating) VALUES (?, ?, ?, ?)");
            for (Review review : reviews) {
                statement.setInt(1, review.getUserID());
                statement.setInt(2, review.getCourseID());
                statement.setString(3, review.getReview());
                statement.setInt(4, review.getRating());
                statement.executeUpdate();
            }
            connection.commit();

        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }

    public void disconnect() throws SQLException {
        connection.close();
    }
}
