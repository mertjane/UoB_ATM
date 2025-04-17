package com.atm;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Displays a consent screen to obtain user agreement before collecting personal information.
 * 
 * <p><strong>Created by:</strong> Bora Sozer - Week 8</p>
 * 
 * <p>This class provides a modal dialog that presents terms and conditions to the user
 * and requires explicit consent before proceeding with operations that involve
 * personal data processing, such as account creation.</p>
 * 
 * <p>The implementation of this consent screen addresses legal and ethical requirements
 * for financial applications, particularly regarding:</p>
 * <ul>
 *   <li>GDPR compliance for informed consent</li>
 *   <li>Transparency in data collection and processing</li>
 *   <li>User privacy rights and data protection</li>
 *   <li>Ethical considerations in financial data handling</li>
 * </ul>
 * 
 * @author Bora Sozer
 * @version 1.0.0
 */
public class ConsentScreen {
    
    /**
     * Shows the terms and conditions consent screen and waits for user approval.
     * 
     * @return true if user consents, false otherwise
     */
    public static boolean showAndWaitForConsent() {
        // Create a new stage for the consent dialog
        Stage consentStage = new Stage();
        consentStage.setTitle("Terms and Conditions");
        
        // Create the consent text
        Text consentText = new Text(
            "By creating an account, you agree to the following terms:\n\n" +
            "1. Your personal information will be stored securely and used only for account management.\n" +
            "2. Transaction data will be recorded for security and audit purposes.\n" +
            "3. You are responsible for maintaining the confidentiality of your account credentials.\n" +
            "4. The bank reserves the right to monitor accounts for suspicious activity.\n\n" +
            "Do you consent to these terms and conditions?"
        );
        consentText.setWrappingWidth(400);
        
        // Create buttons
        Button agreeButton = new Button("I Agree");
        Button cancelButton = new Button("Cancel");
        
        // Set up button actions
        final boolean[] result = {false}; // Array to hold result since we need a final variable
        
        agreeButton.setOnAction(e -> {
            result[0] = true;
            consentStage.close();
        });
        
        cancelButton.setOnAction(e -> {
            result[0] = false;
            consentStage.close();
        });
        
        // Layout
        HBox buttonBox = new HBox(10, agreeButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER);
        
        VBox layout = new VBox(20, consentText, buttonBox);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        
        // Show the dialog and wait for user response
        Scene scene = new Scene(layout);
        consentStage.setScene(scene);
        consentStage.initModality(Modality.APPLICATION_MODAL);
        consentStage.showAndWait();
        
        return result[0];
    }
} 