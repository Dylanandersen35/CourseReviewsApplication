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

    public Review getUserReview(int userID, int courseID) {
        DatabaseDriver databaseDriver = null;
        try {
            databaseDriver = new DatabaseDriver();
            return databaseDriver.getUserReview(userID, courseID);
        } catch (SQLException e) {
            return null;
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

    public double getAverageRating(int courseNumber) {
        DatabaseDriver databaseDriver = null;
        try {
            databaseDriver = new DatabaseDriver();
            return databaseDriver.getAverageRating(courseNumber);
        } catch (SQLException e) {
            return 0.0;
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

    public void updateReview(Review review) {
        DatabaseDriver databaseDriver = null;
        try {
            databaseDriver = new DatabaseDriver();
            databaseDriver.updateReview(review);
        } catch (SQLException e) {
            throw new RuntimeException(e);
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

    public boolean hasReviewed(int userID, int courseID) {
        DatabaseDriver databaseDriver = null;
        try {
            databaseDriver = new DatabaseDriver();
            return databaseDriver.hasReviewed(userID, courseID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
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

    public void addReview(Review review) {
        DatabaseDriver databaseDriver = null;
        try {
            databaseDriver = new DatabaseDriver();
            databaseDriver.addReview(review);
        } catch (SQLException e) {
            throw new RuntimeException(e);
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

    public void deleteReview(Review review) {
        DatabaseDriver databaseDriver = null;
        try {
            databaseDriver = new DatabaseDriver();
            databaseDriver.deleteReview(review);
        } catch (SQLException e) {
            throw new RuntimeException(e);
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

    public List<Review> retrieveReviewsByCourseID(int id) {
        DatabaseDriver databaseDriver = null;
        try {
            databaseDriver = new DatabaseDriver();
            return databaseDriver.getReviewsByCourseID(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
