package classroom;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentDashboard {

    private Connection connection;

    public StudentDashboard(Connection connection) {
        this.connection = connection;
    }

    public void showStudentDashboard(Stage primaryStage) {
        Button viewAttendanceButton = new Button("View Attendance");
        Button viewScheduleButton = new Button("View Schedule");
        Button logoutButton = new Button("Logout");

        viewAttendanceButton.setOnAction(event -> {
            // Logic to show attendance (not implemented in this example)
            System.out.println("Viewing Attendance");
        });

        viewScheduleButton.setOnAction(event -> {
            // Logic to show schedule
            showViewSchedulePage(primaryStage);
        });

        logoutButton.setOnAction(event -> {
            Login login = new Login(connection);
            login.showLoginWindow(primaryStage);
        });

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        vbox.getChildren().addAll(viewAttendanceButton, viewScheduleButton, logoutButton);
        vbox.getStyleClass().add("container");

        Scene scene = new Scene(vbox, 400, 200);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Student Dashboard");
        primaryStage.show();
    }

    private void showViewSchedulePage(Stage primaryStage) {
        // Create TableView to display schedule
        TableView<Schedule> scheduleTable = new TableView<>();

        // Define columns for TableView
        TableColumn<Schedule, String> courseNameCol = new TableColumn<>("Course Name");
        courseNameCol.setCellValueFactory(cellData -> cellData.getValue().courseNameProperty());

        TableColumn<Schedule, String> lecturerCol = new TableColumn<>("Lecturer");
        lecturerCol.setCellValueFactory(cellData -> cellData.getValue().lecturerProperty());

        TableColumn<Schedule, String> dayOfWeekCol = new TableColumn<>("Day of Week");
        dayOfWeekCol.setCellValueFactory(cellData -> cellData.getValue().dayOfWeekProperty());

        TableColumn<Schedule, String> startTimeCol = new TableColumn<>("Start Time");
        startTimeCol.setCellValueFactory(cellData -> cellData.getValue().startTimeProperty());

        TableColumn<Schedule, String> endTimeCol = new TableColumn<>("End Time");
        endTimeCol.setCellValueFactory(cellData -> cellData.getValue().endTimeProperty());

        TableColumn<Schedule, String> locationCol = new TableColumn<>("Location");
        locationCol.setCellValueFactory(cellData -> cellData.getValue().locationProperty());

        // Add columns to TableView
        scheduleTable.getColumns().addAll(courseNameCol, lecturerCol, dayOfWeekCol, startTimeCol, endTimeCol, locationCol);

        // Load schedule data from database
        ObservableList<Schedule> scheduleList = FXCollections.observableArrayList();
        loadScheduleData(scheduleList);

        // Populate TableView with data
        scheduleTable.setItems(scheduleList);

        Button backButton = new Button("Back");
        backButton.setOnAction(event -> showStudentDashboard(primaryStage));

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(event -> {
            Login login = new Login(connection);
            login.showLoginWindow(primaryStage);
        });

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        vbox.getChildren().addAll(new Label("Schedule"), scheduleTable, backButton, logoutButton);
        vbox.getStyleClass().add("container");

        Scene scene = new Scene(vbox, 800, 400);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("View Schedule");
        primaryStage.show();
    }

    private void loadScheduleData(ObservableList<Schedule> scheduleList) {
        // Query database to load schedule data (example SQL query)
        String query = "SELECT * FROM schedule";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String courseName = resultSet.getString("course_name");
                String lecturer = resultSet.getString("lecturer");
                String dayOfWeek = resultSet.getString("day_of_week");
                String startTime = resultSet.getString("start_time");
                String endTime = resultSet.getString("end_time");
                String location = resultSet.getString("location");

                Schedule schedule = new Schedule(courseName, lecturer, dayOfWeek, startTime, endTime, location);
                scheduleList.add(schedule);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
