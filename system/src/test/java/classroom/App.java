package classroom;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;

public class App extends Application {

    private Connection connection;

    @Override
    public void start(Stage primaryStage) {
        try {
            // Initialize database connection
            connection = DatabaseUtil.getConnection(); // Assumes DatabaseUtil provides a valid connection

            // Create instance of Login component and show login window
            Login login = new Login(connection);
            login.showLoginWindow(primaryStage);
        } catch (SQLException e) {
            e.printStackTrace(); // Print the exception stack trace for debugging
            // Handle the exception here, e.g., show an error message to the user
            System.out.println("Failed to connect to the database. Please check your connection.");
            // Optionally, exit the application or handle the error gracefully
            Platform.exit();
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        if (connection != null && !connection.isClosed()) {
            connection.close(); // Close the database connection when the application stops
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
