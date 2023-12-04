package edu.virginia.sde.reviews;

import javax.xml.transform.Result;
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
                    Ts TEXT,
                    FOREIGN KEY(UserID) REFERENCES Users(ID) ON DELETE CASCADE, 
                    FOREIGN KEY(CourseID) References Courses(ID) ON DELETE CASCADE
                    );
            """);

            statement2.executeUpdate();
            connection.commit();

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

    public int getUserID(String username) {
        try {
            var statement = connection.prepareStatement(
                    """
                        SELECT * from Users;
                    """);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                if (username.equals(rs.getString("Username"))) {
                    return rs.getInt("ID");
                }
            }
            return 0;
        } catch (SQLException e) {
            return 0;
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
                double rating = rs.getDouble("Rating");
                course.setRating(rs.wasNull() ? null : rating);
                courses.add(course);
            }

            return courses;

        } catch (SQLException e) {
            return courses;
        }
    }

    public Course getCourseByID(int id) {
        try {
            var statement = connection.prepareStatement(
                    """
                        SELECT * from Courses;
                    """
            );

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                if (rs.getInt("ID") == id) {
                    return new Course(rs.getString("Subject"), rs.getInt("CourseNumber"), rs.getString("Title"), rs.getDouble("Rating"));
                }
            }
            return new Course();
        } catch (SQLException e) {
            return new Course();
        }
    }

    public void updateCourses(List<Course> courses) throws SQLException {
        try {
            var clearStatement = connection.prepareStatement("DELETE FROM Courses");
            clearStatement.executeUpdate();

            var statement = connection.prepareStatement("INSERT INTO Courses(Subject, CourseNumber, Title, Rating) VALUES (?, ?, ?, ?)");
            for (Course course : courses) {
                statement.setString(1, course.getSubject().toUpperCase());
                statement.setInt(2, course.getCourseNumber());
                statement.setString(3, course.getTitle().toUpperCase());
                statement.setDouble(4, course.getRating());
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
                review.setTimestamp(rs.getString("Ts"));
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

    public Review getUserReview(int userId, int courseId) throws SQLException {
        String query = "SELECT * FROM Reviews WHERE UserID = ? AND CourseID = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, userId);
        statement.setInt(2, courseId);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            int userID = resultSet.getInt("UserID");
            int courseID = resultSet.getInt("CourseID");
            String review = resultSet.getString("Review");
            int rating = resultSet.getInt("Rating");
            return new Review(userID, courseID, review, rating);
        } else {
            return null;
        }
    }

    public int getCourseID(Course course) throws SQLException {
        String query = "SELECT * FROM Courses WHERE Subject = ? AND CourseNumber = ? AND Title = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, course.getSubject().toUpperCase());
        statement.setInt(2, course.getCourseNumber());
        statement.setString(3, course.getTitle().toUpperCase());
        ResultSet rs = statement.executeQuery();
        if (rs.next()) {
            return rs.getInt("ID");
        }
        return 0;
    }


    public double getAverageRating(int courseId) throws SQLException {
        String query = "SELECT AVG(Rating) FROM Reviews WHERE CourseID = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, courseId);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getDouble(1);
        } else {
            return 0.0;
        }
    }

    public void updateCourseRating(double updatedRating, int id) throws SQLException{
        String query = "UPDATE Courses SET Rating = ? WHERE ID = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setDouble(1, updatedRating);
        statement.setInt(2, id);
        statement.executeUpdate();
        connection.commit();
    }

    public void updateReview(Review review) throws SQLException {
        String query = "UPDATE Reviews SET Review = ?, Rating = ?, Ts = ? WHERE UserID = ? AND CourseID = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, review.getReview());
        statement.setInt(2, review.getRating());
        statement.setString(3, review.getTimestamp());
        statement.setInt(4, review.getUserID());
        statement.setInt(5, review.getCourseID());
        statement.executeUpdate();
        connection.commit();
    }

    public boolean hasReviewed(int userID, int courseID) throws SQLException {
        String query = "SELECT COUNT(*) FROM Reviews WHERE UserID = ? AND CourseID = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, userID);
        statement.setInt(2, courseID);
        ResultSet resultSet = statement.executeQuery();
        return resultSet.next() && resultSet.getInt(1) > 0;
    }

    public void addReview(Review review) throws SQLException {
        String query = "INSERT INTO Reviews (UserID, CourseID, Review, Rating, Ts) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, review.getUserID());
        statement.setInt(2, review.getCourseID());
        statement.setString(3, review.getReview());
        statement.setInt(4, review.getRating());
        statement.setString(5, review.getTimestamp());
        statement.executeUpdate();
        connection.commit();
    }

    public void deleteReview(Review review) throws SQLException {
        String query = "DELETE FROM Reviews WHERE UserID = ? AND CourseID = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, review.getUserID());
        statement.setInt(2, review.getCourseID());
        statement.executeUpdate();
        connection.commit();
    }

    public List<Review> getReviewsByCourseID(int id) throws SQLException {
        var reviews = new ArrayList<Review>();
        String query = "SELECT * FROM Reviews WHERE CourseID = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, id);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            Review review = new Review();
            review.setCourseID(rs.getInt("CourseID"));
            review.setRating(rs.getInt("Rating"));
            review.setTimestamp(rs.getString("Ts"));
            review.setUserID(rs.getInt("UserID"));
            review.setReview(rs.getString("Review"));
            reviews.add(review);
        }
        return reviews;
    }

    public void disconnect() throws SQLException {
        connection.close();
    }
}
