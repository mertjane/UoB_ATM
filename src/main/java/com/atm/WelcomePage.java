package com.atm;
/**
 * Nadeen week 6: add a welcome page with UI changes. 
 */
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class WelcomePage extends Application {

    private Runnable onCloseAction; // Callback for when WelcomePage closes

    @Override
    public void start(Stage primaryStage) {
        // Create Welcome Label
        Label welcomeLabel = new Label("Welcome to the ATM System!");
        welcomeLabel.setId("welcomeLabel"); // Apply CSS styling

        // Create Get Started Button
        Button getStartedButton = new Button("Get Started");
        getStartedButton.setId("welcomeButton"); // Apply CSS styling

        // Action to handle button click event
        getStartedButton.setOnAction(e -> {
            System.out.println("Get Started button clicked!");
            // Close the welcome window
            primaryStage.close();

            // Notify Main that WelcomePage is done
            if (onCloseAction != null) {
                onCloseAction.run();
            }
        });

        // Layout for Welcome Page
        VBox layout = new VBox(15); // Vertical box with spacing between elements
        layout.setId("welcomeLayout"); // Apply CSS styling
        layout.getChildren().addAll(welcomeLabel, getStartedButton); // Add label and button to layout
        layout.setAlignment(javafx.geometry.Pos.CENTER); // Center the layout content

        // Scene and Styling
        Scene scene = new Scene(layout, 850, 850); // Set the size of the scene
        scene.getStylesheets().add(getClass().getResource("/atm.css").toExternalForm()); // Add CSS stylesheet

        // Configure Stage
        primaryStage.setTitle("ATM System - Welcome"); // Set the title of the window
        primaryStage.setScene(scene); // Set the scene for the stage
        primaryStage.show(); // Show the stage (window)
    }

    /**
     * Allows Main to set a callback function that runs when WelcomePage closes.
     * <p>
     * This method is used so that when the user clicks "Get Started," the WelcomePage
     * will notify the Main class to launch the ATM application.
     * </p>
     *
     * @param onCloseAction A Runnable that is executed when the WelcomePage is closed.
     */
    public void setOnWelcomeClosed(Runnable onCloseAction) {
        this.onCloseAction = onCloseAction;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
