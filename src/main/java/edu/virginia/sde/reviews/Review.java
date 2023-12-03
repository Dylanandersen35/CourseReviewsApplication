package edu.virginia.sde.reviews;

public class Review {
    private int userID;
    private int courseID;
    private String review;
    private int rating;
    private String timestamp;


    public Review(int userID, int courseID, String review, int rating, String timestamp) {
        this.userID = userID;
        this.courseID = courseID;
        this.review = review;
        this.rating = rating;
        this.timestamp = timestamp;
    }

    public Review(int userID, int courseID, String review, int rating) {
        this.userID = userID;
        this.courseID = courseID;
        this.review = review;
        this.rating = rating;
    }

    public Review() {

    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getTimestamp() {return timestamp; }

    public void setTimestamp(String timestamp) {this.timestamp = timestamp;}

}
