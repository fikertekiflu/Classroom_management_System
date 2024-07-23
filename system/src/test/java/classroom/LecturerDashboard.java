package classroom;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LecturerDashboard {

    private Connection connection;

    public LecturerDashboard(Connection connection) {
        this.connection = connection;
    }

    public void showLecturerDashboard(Stage primaryStage) {
        // Create lecturer dashboard components
        Label welcomeLabel = new Label("Welcome to Lecturer Dashboard!");
        welcomeLabel.getStyleClass().add("label");

        Button viewScheduleButton = new Button("View Schedule");
        viewScheduleButton.getStyleClass().add("button");

        Button viewAttendanceButton = new Button("View Attendance");
        viewAttendanceButton.getStyleClass().add("button");

        Button takeAttendanceButton = new Button("Take Attendance");
        takeAttendanceButton.getStyleClass().add("button");

        Button logoutButton = new Button("Logout");
        logoutButton.getStyleClass().add("button");

        VBox vbox = new VBox(10);
        vbox.getStyleClass().add("container");
        vbox.setPadding(new Insets(10));
        vbox.getChildren().addAll(welcomeLabel, viewScheduleButton, viewAttendanceButton, takeAttendanceButton, logoutButton);

        Scene scene = new Scene(vbox, 400, 200);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        primaryStage.setScene(scene);

        // Set full-screen mode
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX(primaryScreenBounds.getMinX());
        primaryStage.setY(primaryScreenBounds.getMinY());
        primaryStage.setWidth(primaryScreenBounds.getWidth());
        primaryStage.setHeight(primaryScreenBounds.getHeight());

        primaryStage.setTitle("Lecturer Dashboard");
        primaryStage.show();

        // Set action handlers for buttons
        viewScheduleButton.setOnAction(event -> showViewSchedulePage(primaryStage));
        viewAttendanceButton.setOnAction(event -> showViewAttendancePage(primaryStage));
        takeAttendanceButton.setOnAction(event -> showTakeAttendancePage(primaryStage));
        logoutButton.setOnAction(event -> showLoginPage(primaryStage));
    }

    private void showViewSchedulePage(Stage primaryStage) {
        // Create TableView to display schedule
        TableView<Schedule> scheduleTable = new TableView<>();
        scheduleTable.getStyleClass().add("table-view");

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

        // Create a back button
        Button backButton = new Button("Back");
        backButton.getStyleClass().add("button");
        backButton.setOnAction(event -> showLecturerDashboard(primaryStage));

        // Arrange components in a VBox
        VBox vbox = new VBox(10);
        vbox.getStyleClass().add("container");
        vbox.setPadding(new Insets(10));
        vbox.getChildren().addAll(new Label("Schedule"), scheduleTable, backButton);

        Scene scene = new Scene(vbox, 800, 400);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("View Schedule");
        primaryStage.show();
    }

    private void loadScheduleData(ObservableList<Schedule> scheduleList) {
        // Query database to load schedule data
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

    private void showViewAttendancePage(Stage primaryStage) {
        // Implement the UI and functionality to view attendance
        Label viewAttendanceLabel = new Label("View Attendance Page");
        viewAttendanceLabel.getStyleClass().add("label");

        // Create a back button
        Button backButton = new Button("Back");
        backButton.getStyleClass().add("button");
        backButton.setOnAction(event -> showLecturerDashboard(primaryStage));

        // Arrange components in a VBox
        VBox vbox = new VBox(10);
        vbox.getStyleClass().add("container");
        vbox.setPadding(new Insets(10));
        vbox.getChildren().addAll(viewAttendanceLabel, backButton);

        Scene scene = new Scene(vbox, 400, 200);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("View Attendance");
        primaryStage.show();
    }

    private void showTakeAttendancePage(Stage primaryStage) {
        // Implement the UI and functionality to take attendance
        Label takeAttendanceLabel = new Label("Take Attendance Page");
        takeAttendanceLabel.getStyleClass().add("label");

        // Create a back button
        Button backButton = new Button("Back");
        backButton.getStyleClass().add("button");
        backButton.setOnAction(event -> showLecturerDashboard(primaryStage));

        // Arrange components in a VBox
        VBox vbox = new VBox(10);
        vbox.getStyleClass().add("container");
        vbox.setPadding(new Insets(10));
        vbox.getChildren().addAll(takeAttendanceLabel, backButton);

        Scene scene = new Scene(vbox, 400, 200);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Take Attendance");
        primaryStage.show();
    }

    private void showLoginPage(Stage primaryStage) {
        // Create a new instance of Login and show the login window
        Login login = new Login(connection);
        login.showLoginWindow(primaryStage);
    }
}
