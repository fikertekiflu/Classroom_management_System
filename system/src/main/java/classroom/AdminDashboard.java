package classroom;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDashboard {

    private Connection connection;

    public AdminDashboard(Connection connection) {
        this.connection = connection;
    }

    public void showAdminDashboard(Stage primaryStage) {
        // Create admin dashboard components
        Label welcomeLabel = new Label("Welcome to Admin Dashboard!");

        Button addUserButton = new Button("Add User");
        Button updateUserInfoButton = new Button("Update User Info");
        Button showScheduleButton = new Button("Show Schedule");
        Button updateScheduleButton = new Button("Update Schedule");
        Button logoutButton = new Button("Logout");

        // Apply styles to buttons
        addUserButton.getStyleClass().add("dashboard-button");
        updateUserInfoButton.getStyleClass().add("dashboard-button");
        showScheduleButton.getStyleClass().add("dashboard-button");
        updateScheduleButton.getStyleClass().add("dashboard-button");
        logoutButton.getStyleClass().add("logout-button");

        // Create a GridPane to hold the buttons
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(20);
        gridPane.setVgap(20);
        gridPane.setPadding(new Insets(20));

        // Add buttons to the GridPane
        gridPane.add(addUserButton, 0, 0);
        gridPane.add(updateUserInfoButton, 1, 0);
        gridPane.add(showScheduleButton, 0, 1);
        gridPane.add(updateScheduleButton, 1, 1);

        // Create an HBox for the logout button
        HBox logoutBox = new HBox(logoutButton);
        logoutBox.setAlignment(Pos.CENTER);
        logoutBox.setPadding(new Insets(20, 0, 0, 0));

        // Create a BorderPane for the layout
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(20));
        borderPane.setTop(welcomeLabel);
        BorderPane.setAlignment(welcomeLabel, Pos.CENTER);
        borderPane.setCenter(gridPane);
        borderPane.setBottom(logoutBox);

        Scene scene = new Scene(borderPane, 500, 400);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Admin Dashboard");
        primaryStage.show();

        // Set action handlers for buttons
        addUserButton.setOnAction(event -> showAddUserPage(primaryStage));
        updateUserInfoButton.setOnAction(event -> showUpdateUserInfoPage(primaryStage));
        showScheduleButton.setOnAction(event -> showSchedulePage(primaryStage));
        updateScheduleButton.setOnAction(event -> showUpdateSchedulePage(primaryStage));
        logoutButton.setOnAction(event -> logout(primaryStage));
    }

    private void showAddUserPage(Stage primaryStage) {
        Label addUserLabel = new Label("Add User Page");

        // Create form fields
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();

        Label roleLabel = new Label("Role:");
        TextField roleField = new TextField();

        Button addButton = new Button("Add User");
        Button backButton = new Button("Back");

        // Add button action
        addButton.setOnAction(event -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String role = roleField.getText();

            if (username.isEmpty() || password.isEmpty() || role.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("All fields are required!");
                alert.showAndWait();
            } else {
                addUserToDatabase(username, password, role);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("User added successfully!");
                alert.showAndWait();
                usernameField.clear();
                passwordField.clear();
                roleField.clear();
            }
        });

        backButton.setOnAction(event -> showAdminDashboard(primaryStage));

        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(addUserLabel, usernameLabel, usernameField, passwordLabel, passwordField, roleLabel, roleField, addButton, backButton);
        vbox.setPadding(new Insets(10));
        vbox.getStyleClass().add("container");

        Scene scene = new Scene(vbox, 400, 300);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Add User");
        primaryStage.show();
    }

    private void addUserToDatabase(String username, String password, String role) {
        String query = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, role);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showUpdateUserInfoPage(Stage primaryStage) {
        Label updateUserInfoLabel = new Label("Update User Info Page");

        // Create form fields
        Label idLabel = new Label("User ID:");
        TextField idField = new TextField();

        Label usernameLabel = new Label("New Username:");
        TextField usernameField = new TextField();

        Label passwordLabel = new Label("New Password:");
        PasswordField passwordField = new PasswordField();

        Label roleLabel = new Label("New Role:");
        TextField roleField = new TextField();

        Button updateButton = new Button("Update User Info");
        Button backButton = new Button("Back");

        // Update button action
        updateButton.setOnAction(event -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String username = usernameField.getText().isEmpty() ? null : usernameField.getText();
                String password = passwordField.getText().isEmpty() ? null : passwordField.getText();
                String role = roleField.getText().isEmpty() ? null : roleField.getText();

                if (username == null && password == null && role == null) {
                    displayAlert(Alert.AlertType.ERROR, "Input Error", "At least one field (username, password, role) must be filled.");
                    return;
                }

                updateUserInDatabase(id, username, password, role);
                displayAlert(Alert.AlertType.INFORMATION, "Success", "User information updated successfully!");
            } catch (NumberFormatException e) {
                displayAlert(Alert.AlertType.ERROR, "Input Error", "Please enter a valid User ID.");
            } catch (SQLException e) {
                e.printStackTrace();
                displayAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update user information. Please try again.");
            }
        });

        backButton.setOnAction(event -> showAdminDashboard(primaryStage));

        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(updateUserInfoLabel, idLabel, idField, usernameLabel, usernameField, passwordLabel, passwordField, roleLabel, roleField, updateButton, backButton);
        vbox.setPadding(new Insets(10));
        vbox.getStyleClass().add("container");

        Scene scene = new Scene(vbox, 400, 400);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Update User Info");
        primaryStage.show();
    }

    private void updateUserInDatabase(int id, String username, String password, String role) throws SQLException {
        String query = "SELECT * FROM users WHERE id=?";
        try (PreparedStatement selectStmt = connection.prepareStatement(query)) {
            selectStmt.setInt(1, id);
            ResultSet resultSet = selectStmt.executeQuery();
            if (resultSet.next()) {
                if (username == null) username = resultSet.getString("username");
                if (password == null) password = resultSet.getString("password");
                if (role == null) role = resultSet.getString("role");
            } else {
                throw new SQLException("User with ID " + id + " not found.");
            }
        }

        String updateQuery = "UPDATE users SET username=?, password=?, role=? WHERE id=?";
        try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
            updateStmt.setString(1, username);
            updateStmt.setString(2, password);
            updateStmt.setString(3, role);
            updateStmt.setInt(4, id);
            updateStmt.executeUpdate();
        }
    }

    private void displayAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSchedulePage(Stage primaryStage) {
        Label scheduleLabel = new Label("Schedule");

        TableView<Schedule> scheduleTable = new TableView<>();

        TableColumn<Schedule, String> courseNameColumn = new TableColumn<>("Course Name");
        courseNameColumn.setCellValueFactory(cellData -> cellData.getValue().courseNameProperty());

        TableColumn<Schedule, String> lecturerColumn = new TableColumn<>("Lecturer");
        lecturerColumn.setCellValueFactory(cellData -> cellData.getValue().lecturerProperty());

        TableColumn<Schedule, String> dayOfWeekColumn = new TableColumn<>("Day of Week");
        dayOfWeekColumn.setCellValueFactory(cellData -> cellData.getValue().dayOfWeekProperty());

        TableColumn<Schedule, String> startTimeColumn = new TableColumn<>("Start Time");
        startTimeColumn.setCellValueFactory(cellData -> cellData.getValue().startTimeProperty());

        TableColumn<Schedule, String> endTimeColumn = new TableColumn<>("End Time");
        endTimeColumn.setCellValueFactory(cellData -> cellData.getValue().endTimeProperty());

        TableColumn<Schedule, String> locationColumn = new TableColumn<>("Location");
        locationColumn.setCellValueFactory(cellData -> cellData.getValue().locationProperty());

        scheduleTable.getColumns().addAll(courseNameColumn, lecturerColumn, dayOfWeekColumn, startTimeColumn, endTimeColumn, locationColumn);

        loadScheduleFromDatabase(scheduleTable);

        Button backButton = new Button("Back");
        backButton.setOnAction(event -> showAdminDashboard(primaryStage));

        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(scheduleLabel, scheduleTable, backButton);
        vbox.setPadding(new Insets(10));
        vbox.getStyleClass().add("container");

        Scene scene = new Scene(vbox, 800, 600);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Show Schedule");
        primaryStage.show();
    }

    private void loadScheduleFromDatabase(TableView<Schedule> scheduleTable) {
        scheduleTable.getItems().clear();
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
                scheduleTable.getItems().add(new Schedule(courseName, lecturer, dayOfWeek, startTime, endTime, location));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showUpdateSchedulePage(Stage primaryStage) {
        Label updateScheduleLabel = new Label("Update Schedule Page");

        // Create form fields
        Label idLabel = new Label("Schedule ID:");
        TextField idField = new TextField();

        Label courseNameLabel = new Label("New Course Name:");
        TextField courseNameField = new TextField();

        Label lecturerLabel = new Label("New Lecturer:");
        TextField lecturerField = new TextField();

        Label dayOfWeekLabel = new Label("New Day of Week:");
        TextField dayOfWeekField = new TextField();

        Label startTimeLabel = new Label("New Start Time:");
        TextField startTimeField = new TextField();

        Label endTimeLabel = new Label("New End Time:");
        TextField endTimeField = new TextField();

        Label locationLabel = new Label("New Location:");
        TextField locationField = new TextField();

        Button updateButton = new Button("Update Schedule");
        Button backButton = new Button("Back");

        // Update button action
        updateButton.setOnAction(event -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String courseName = courseNameField.getText().isEmpty() ? null : courseNameField.getText();
                String lecturer = lecturerField.getText().isEmpty() ? null : lecturerField.getText();
                String dayOfWeek = dayOfWeekField.getText().isEmpty() ? null : dayOfWeekField.getText();
                String startTime = startTimeField.getText().isEmpty() ? null : startTimeField.getText();
                String endTime = endTimeField.getText().isEmpty() ? null : endTimeField.getText();
                String location = locationField.getText().isEmpty() ? null : locationField.getText();

                if (courseName == null && lecturer == null && dayOfWeek == null && startTime == null && endTime == null && location == null) {
                    displayAlert(Alert.AlertType.ERROR, "Input Error", "At least one field (course name, lecturer, day of week, start time, end time, location) must be filled.");
                    return;
                }

                updateScheduleInDatabase(id, courseName, lecturer, dayOfWeek, startTime, endTime, location);
                displayAlert(Alert.AlertType.INFORMATION, "Success", "Schedule updated successfully!");
            } catch (NumberFormatException e) {
                displayAlert(Alert.AlertType.ERROR, "Input Error", "Please enter a valid Schedule ID.");
            } catch (SQLException e) {
                e.printStackTrace();
                displayAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update schedule. Please try again.");
            }
        });

        backButton.setOnAction(event -> showAdminDashboard(primaryStage));

        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(updateScheduleLabel, idLabel, idField, courseNameLabel, courseNameField, lecturerLabel, lecturerField, dayOfWeekLabel, dayOfWeekField, startTimeLabel, startTimeField, endTimeLabel, endTimeField, locationLabel, locationField, updateButton, backButton);
        vbox.setPadding(new Insets(10));
        vbox.getStyleClass().add("container");

        Scene scene = new Scene(vbox, 400, 500);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Update Schedule");
        primaryStage.show();
    }

    private void updateScheduleInDatabase(int id, String courseName, String lecturer, String dayOfWeek, String startTime, String endTime, String location) throws SQLException {
        String query = "SELECT * FROM schedule WHERE id=?";
        try (PreparedStatement selectStmt = connection.prepareStatement(query)) {
            selectStmt.setInt(1, id);
            ResultSet resultSet = selectStmt.executeQuery();
            if (resultSet.next()) {
                if (courseName == null) courseName = resultSet.getString("courseName");
                if (lecturer == null) lecturer = resultSet.getString("lecturer");
                if (dayOfWeek == null) dayOfWeek = resultSet.getString("dayOfWeek");
                if (startTime == null) startTime = resultSet.getString("startTime");
                if (endTime == null) endTime = resultSet.getString("endTime");
                if (location == null) location = resultSet.getString("location");
            } else {
                throw new SQLException("Schedule with ID " + id + " not found.");
            }
        }

        String updateQuery = "UPDATE schedule SET courseName=?, lecturer=?, dayOfWeek=?, startTime=?, endTime=?, location=? WHERE id=?";
        try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
            updateStmt.setString(1, courseName);
            updateStmt.setString(2, lecturer);
            updateStmt.setString(3, dayOfWeek);
            updateStmt.setString(4, startTime);
            updateStmt.setString(5, endTime);
            updateStmt.setString(6, location);
            updateStmt.setInt(7, id);
            updateStmt.executeUpdate();
        }
    }

    private void logout(Stage primaryStage) {
        // Navigate back to the login page
        Login loginPage = new Login(connection);
        loginPage.showLoginWindow(primaryStage);
    }
}
