package classroom;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Schedule {
    private final StringProperty courseName;
    private final StringProperty lecturer;
    private final StringProperty dayOfWeek;
    private final StringProperty startTime;
    private final StringProperty endTime;
    private final StringProperty location;

    public Schedule(String courseName, String lecturer, String dayOfWeek, String startTime, String endTime, String location) {
        this.courseName = new SimpleStringProperty(courseName);
        this.lecturer = new SimpleStringProperty(lecturer);
        this.dayOfWeek = new SimpleStringProperty(dayOfWeek);
        this.startTime = new SimpleStringProperty(startTime);
        this.endTime = new SimpleStringProperty(endTime);
        this.location = new SimpleStringProperty(location);
    }

    // Getters for properties
    public String getCourseName() {
        return courseName.get();
    }

    public StringProperty courseNameProperty() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName.set(courseName);
    }

    public String getLecturer() {
        return lecturer.get();
    }

    public StringProperty lecturerProperty() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer.set(lecturer);
    }

    public String getDayOfWeek() {
        return dayOfWeek.get();
    }

    public StringProperty dayOfWeekProperty() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek.set(dayOfWeek);
    }

    public String getStartTime() {
        return startTime.get();
    }

    public StringProperty startTimeProperty() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime.set(startTime);
    }

    public String getEndTime() {
        return endTime.get();
    }

    public StringProperty endTimeProperty() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime.set(endTime);
    }

    public String getLocation() {
        return location.get();
    }

    public StringProperty locationProperty() {
        return location;
    }

    public void setLocation(String location) {
        this.location.set(location);
    }
}
