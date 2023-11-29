package edu.virginia.sde.reviews;

public class Course {
    private String subject;
    private int courseNumber;
    private String title;
    private int rating;

    public Course(String subject, int courseNumber, String title, int rating) {
        this.subject = subject;
        this.courseNumber = courseNumber;
        this.title = title;
        this.rating = rating;
    }

    public Course() {

    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(int courseNumber) {
        this.courseNumber = courseNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
