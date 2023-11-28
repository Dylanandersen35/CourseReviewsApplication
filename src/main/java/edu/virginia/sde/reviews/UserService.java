package edu.virginia.sde.reviews;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.util.*;

public class UserService {
    public void save(List<User> users) {
        DatabaseDriver databaseDriver = null;
        try {
            databaseDriver = new DatabaseDriver();
            databaseDriver.updateUsers(users);
        } catch (SQLException e) {
            throw new RuntimeException();
        } finally {
            try {
                if (databaseDriver != null) {
                    databaseDriver.disconnect();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public List<User> retrieveUsers() {
        DatabaseDriver databaseDriver = null;
        try {
            databaseDriver = new DatabaseDriver();
            return databaseDriver.getAllUsers();
        } catch (SQLException e) {
            return new ArrayList<>();
        } finally {
            try {
                if (databaseDriver != null) {
                    databaseDriver.disconnect();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
