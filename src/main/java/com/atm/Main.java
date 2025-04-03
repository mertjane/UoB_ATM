package com.atm;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The Main class serves as the entry point for the ATM application.
 * <p>
 * This class extends the JavaFX {@code Application} class and is responsible for
 * setting up the ATM's GUI interface, initializing the Model, View, and Controller
 * components, and linking them together. The application follows the Model-View-Controller (MVC)
 * architecture to separate concerns and improve maintainability.
 * </p>
 * <p>
 * Note: The methods in the {@code BankAccount} class (such as login, deposit, withdraw,
 * and checkBalance) are incomplete. You are required to complete these methods and add further
 * functionality/features as needed. Tutors may provide guidance but cannot assist directly with coding.
 * </p>
 */
public class Main extends Application {

    private Stage primaryStage; // Store the primary stage to reuse it

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // First, launch the WelcomePage
        WelcomePage welcomePage = new WelcomePage();
        welcomePage.setOnWelcomeClosed(() -> {
            // This runs when the WelcomePage is closed
            launchMainATM();
        });

        // Start Welcome Page (pass the primaryStage to it)
        welcomePage.start(new Stage());
    }

    /**
     * Initializes and starts the ATM application GUI.
     * <p>
     * This method sets up debugging, creates a {@code Bank} object, adds test bank accounts,
     * initializes the MVC components (Model, View, Controller), links them together, and displays the GUI.
     * </p>
     */
    private void launchMainATM() {
        // Set up debugging and print initial debugging message
        Debug.set(true);
        Debug.trace("atm starting");
        Debug.trace("Main::start");

        // Create a Bank object for this ATM
        Bank b = new Bank();

        // Add some test bank accounts
        b.addBankAccount(new StudentAccount("00000", "00000", 0));
        b.addBankAccount(new GoldAccount("11111", "11111", 0));
        b.addBankAccount(new PlatinumAccount("22222", "22222", 0));
        b.addBankAccount(new StudentAccount("00001", "00001", 100));
        b.addBankAccount(new GoldAccount("00002", "00002", 200));
        b.addBankAccount(new PlatinumAccount("00003", "00003", 300));

        // Create the Model, View, and Controller objects
        Model model = new Model(b);   // The model needs the Bank object to 'talk to' the bank
        View view = new View();
        Controller controller = new Controller();

        // Link them together so they can talk to each other
        model.view = view;
        model.controller = controller;
        controller.model = model;
        controller.view = view;
        view.model = model;
        view.controller = controller;

        // Display the main ATM interface on the primary stage
        view.start(primaryStage); // Pass the same primaryStage to the View
        model.initialise("Welcome to the ATM");
        model.display();

        // Application is now running
        Debug.trace("ATM running");

        // Ensure the Goodbye Page is shown when the user closes the application
        primaryStage.setOnCloseRequest(event -> {
            GoodbyePage goodbyePage = new GoodbyePage();
            goodbyePage.start(primaryStage); // Reuse the primaryStage for GoodbyePage
        });
    }

    /**
     * The main method is the entry point when launching the application from the command line.
     * <p>
     * When running in an IDE like BlueJ, the {@code start} method may be invoked directly.
     * </p>
     */
    public static void main(String args[]) {
        launch(args);  // Launch WelcomePage first, then ATM
    }
}

