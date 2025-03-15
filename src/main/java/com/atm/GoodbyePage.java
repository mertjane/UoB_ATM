package com.atm;
/**
 * Nadeen EL Samad week 6: add a goodbye page with UI chnages. 
 */
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GoodbyePage {

    public void start(Stage window) {
        // Creating a goodbye message
        Label goodbyeLabel = new Label("Thank you for using the ATM. Goodbye!");
        goodbyeLabel.setId("goodbyeLabel"); // Apply CSS styling

        // Creating a button to close the application
        Button closeButton = new Button("Close");
        closeButton.setId("goodbyeButton"); // Apply CSS styling
        closeButton.setOnAction(e -> {
            // Close the application when the button is clicked
            window.close();
        });

        // Layout to arrange the UI elements properly
        VBox layout = new VBox(20); // Spacing of 20px between elements
        layout.setId("goodbyeLayout"); // Apply CSS styling
        layout.getChildren().addAll(goodbyeLabel, closeButton);
        layout.setAlignment(Pos.CENTER); // Center elements vertically and horizontally

        // Scene and Stage setup
        Scene scene = new Scene(layout, 850, 850); // Set the desired width and height
        scene.getStylesheets().add(getClass().getResource("/atm.css").toExternalForm()); // Add CSS stylesheet

        window.setTitle("ATM System - Goodbye");
        window.setScene(scene);
        window.show();
    }
}
