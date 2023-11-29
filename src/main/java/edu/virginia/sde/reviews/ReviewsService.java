package edu.virginia.sde.reviews;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReviewsService {
    public void save(List<Review> reviews) {
        DatabaseDriver databaseDriver = null;
        try {
            databaseDriver = new DatabaseDriver();
            databaseDriver.updateReviews(reviews);
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

    public List<Review> retrieveReviews() {
        DatabaseDriver databaseDriver = null;
        try {
            databaseDriver = new DatabaseDriver();
            return databaseDriver.getAllReviews();
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
