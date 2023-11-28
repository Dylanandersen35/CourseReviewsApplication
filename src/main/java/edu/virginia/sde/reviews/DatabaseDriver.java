package edu.virginia.sde.reviews;

import javax.lang.model.util.SimpleElementVisitor14;
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

    public void disconnect() throws SQLException {
        connection.close();
    }
}
