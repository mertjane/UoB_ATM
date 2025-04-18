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
 * Displays a confirmation screen before completing a money transfer.
 * 
 * <p><strong>Created by:</strong> Bora Sozer - Week 9</p>
 * 
 * <p>This class provides a modal dialog that presents transfer details to the user
 * and requires explicit confirmation before proceeding with the transfer operation.</p>
 * 
 * <p>The implementation of this confirmation screen addresses security and usability requirements
 * for financial applications, particularly regarding:</p>
 * <ul>
 *   <li>Prevention of accidental transfers</li>
 *   <li>Transparency in financial operations</li>
 *   <li>User awareness of transaction details</li>
 *   <li>Additional security layer for financial transactions</li>
 * </ul>
 * 
 * @author Bora Sozer
 * @version 1.0.0
 */
public class TransferConfirmationScreen {
    
    /**
     * Shows the transfer confirmation screen and waits for user approval.
     * 
     * @param recipientAccNumber The account number of the recipient
     * @param amount The amount to be transferred
     * @return true if user confirms the transfer, false otherwise
     */
    public static boolean showAndWaitForConfirmation(String recipientAccNumber, double amount) {
        // Create a new stage for the confirmation dialog
        Stage confirmationStage = new Stage();
        confirmationStage.setTitle("Transfer Confirmation");
        
        // Create the confirmation text
        Text confirmationText = new Text(
            "Please confirm the following transfer details:\n\n" +
            "Recipient Account: " + recipientAccNumber + "\n" +
            "Transfer Amount: £" + amount + "\n\n" +
            "Are you sure you want to proceed with this transfer?"
        );
        confirmationText.setWrappingWidth(400);
        
        // Create buttons
        Button confirmButton = new Button("Confirm Transfer");
        Button cancelButton = new Button("Cancel");
        
        // Set up button actions
        final boolean[] result = {false}; // Array to hold result since we need a final variable
        
        confirmButton.setOnAction(e -> {
            result[0] = true;
            confirmationStage.close();
        });
        
        cancelButton.setOnAction(e -> {
            result[0] = false;
            confirmationStage.close();
        });
        
        // Layout
        HBox buttonBox = new HBox(10, confirmButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER);
        
        VBox layout = new VBox(20, confirmationText, buttonBox);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        
        // Show the dialog and wait for user response
        Scene scene = new Scene(layout);
        confirmationStage.setScene(scene);
        confirmationStage.initModality(Modality.APPLICATION_MODAL);
        confirmationStage.showAndWait();
        
        return result[0];
    }
} 