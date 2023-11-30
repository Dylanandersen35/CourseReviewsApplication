package edu.virginia.sde.reviews;

public class Course {
    private String subject;
    private int courseNumber;
    private String title;
    private Integer rating;

    public Course(String subject, int courseNumber, String title, Integer rating) {
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

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
