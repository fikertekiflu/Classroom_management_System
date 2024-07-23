package classroom;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Login {

    private Connection connection;

    public Login(Connection connection) {
        this.connection = connection;
    }

    public void showLoginWindow(Stage primaryStage) {
        // Left section
        ImageView classroomImage = new ImageView(new Image("file:/mnt/data/2810604.jpg")); // Path to the spaceship image
        classroomImage.setFitWidth(200);
        classroomImage.setFitHeight(200);

        Label welcomeLabel = new Label("Welcome to Classroom Management System Page");
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        VBox classroomSection = new VBox(20, classroomImage, welcomeLabel);
        classroomSection.getStyleClass().add("left-section");
        classroomSection.setAlignment(Pos.CENTER);
        classroomSection.setPadding(new Insets(20));

        Label loginLabel = new Label("Login into your Account");
        loginLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        usernameField.getStyleClass().add("text-field");
        usernameField.setPromptText("Enter your username");

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        passwordField.getStyleClass().add("password-field");
        passwordField.setPromptText("Enter your password");

        Button loginButton = new Button("Login");
        loginButton.getStyleClass().add("login-button");

        VBox loginSection = new VBox(10, loginLabel, usernameLabel, usernameField, passwordLabel, passwordField, loginButton);
        loginSection.getStyleClass().add("right-section");
        loginSection.setAlignment(Pos.CENTER);
        loginSection.setPadding(new Insets(20));

        // Main container
        HBox mainContainer = new HBox(classroomSection, loginSection);
        mainContainer.setPadding(new Insets(20));
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setSpacing(20);
        HBox.setHgrow(classroomSection, Priority.ALWAYS);
        HBox.setHgrow(loginSection, Priority.ALWAYS);

        Scene scene = new Scene(mainContainer, 800, 600);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        // Set full-screen mode
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX(primaryScreenBounds.getMinX());
        primaryStage.setY(primaryScreenBounds.getMinY());
        primaryStage.setWidth(primaryScreenBounds.getWidth());
        primaryStage.setHeight(primaryScreenBounds.getHeight());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Classroom Management System");
        primaryStage.show();

        // Login button action
        loginButton.setOnAction(event -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            // Authenticate and check role
            User user = authenticate(username, password);
            if (user != null) {
                switch (user.getRole()) {
                    case "admin":
                        AdminDashboard adminDashboard = new AdminDashboard(connection);
                        adminDashboard.showAdminDashboard(primaryStage);
                        break;
                    case "lecturer":
                        LecturerDashboard lecturerDashboard = new LecturerDashboard(connection);
                        lecturerDashboard.showLecturerDashboard(primaryStage);
                        break;
                    case "student":
                        StudentDashboard studentDashboard = new StudentDashboard(connection);
                        studentDashboard.showStudentDashboard(primaryStage);
                        break;
                    default:
                        showUserDashboard(primaryStage);
                }
            } else {
                System.out.println("Invalid username or password. Please try again.");
            }
        });
    }

    private User authenticate(String username, String password) {
        // Query to check if username and password match in the database
        String query = "SELECT id, password, role FROM users WHERE username = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String hashedPasswordFromDB = resultSet.getString("password");
                // Replace with actual password hashing verification logic (e.g., bcrypt)
                // For demonstration, comparing plaintext password
                if (password.equals(hashedPasswordFromDB)) {
                    int id = resultSet.getInt("id");
                    String role = resultSet.getString("role");
                    return new User(id, role);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if authentication fails
    }

    private void showUserDashboard(Stage primaryStage) {
        // Method to show user dashboard or main application window for regular users
        Label welcomeLabel = new Label("Welcome to User Dashboard!");
        VBox vbox = new VBox(10);
        vbox.getChildren().add(welcomeLabel);
        Scene scene = new Scene(vbox, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("User Dashboard");
        primaryStage.show();
    }

    // Inner class to store user information
    private class User {
        private int id;
        private String role;

        public User(int id, String role) {
            this.id = id;
            this.role = role;
        }

        public int getId() {
            return id;
        }

        public String getRole() {
            return role;
        }
    }
}
