package com.atm;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * The View class represents the graphical user interface (GUI) for the ATM application.
 * It handles the layout, user input, and display updates using JavaFX components.
 * <p>
 * This class follows the Model-View-Controller (MVC) design pattern, with references to the
 * Model and Controller objects to communicate user actions and update the display.
 * </p>
 * <p>
 * Version and Task Info: Gur Task Week 4 version 1.0.1
 * </p>
 */
class View {
    /** Initial window height */
    int H = 850;
    /** Initial window width */
    int W = 850;

    // UI components

    /** Label for the ATM title (if needed) */
    Label title;
    /** TextField used as the top display screen for messages */
    TextField message;
    /** TextArea used for the main reply or detailed messages */
    TextArea reply;
    /** ScrollPane to contain the reply TextArea */
    ScrollPane scrollPane;
    /** TilePane for the numeric keypad */
    TilePane numPad;
    /** TilePane for the command keypad (Deposit, Withdraw, etc.) */
    TilePane commandPad;
    /** ImageView for displaying the background image */
    ImageView backgroundImageView;

    // MVC references

    /** Reference to the Model (application logic) */
    public Model model;
    /** Reference to the Controller (handles user actions) */
    public Controller controller;

    /**
     * Constructs a new View instance.
     * Initializes the view and prints a debugging trace message.
     */
    public View() {
        Debug.trace("View::<constructor>");
    }

    /**
     * Sets up and displays the ATM user interface on the provided Stage.
     * <p>
     * This method configures the JavaFX components including the background image, screen displays,
     * numeric keypad, command keypad, and layout anchors. It also binds resize listeners to adjust the UI.
     * </p>
     *
     * @param window The primary stage where the UI will be displayed.
     */
    public void start(Stage window) {
        Debug.trace("View::start");

        //Gur Task Week 4 version 1.0.1
        // 1) Load the background image and center it
        Image backgroundImage = new Image("atm.jpg");
        backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setPreserveRatio(true);
        backgroundImageView.setFitWidth(W);
        backgroundImageView.setFitHeight(H);

        // 2) StackPane ensures the image stays centered
        StackPane root = new StackPane();
        root.getChildren().add(backgroundImageView);

        // 3) Create an AnchorPane to hold UI elements
        AnchorPane uiLayer = new AnchorPane();
        root.getChildren().add(uiLayer); // Add the UI layer above the background

        // 4) ATM Screen (TextField for messages)
        message = new TextField();
        message.setEditable(false);
        message.setId("atmScreenTop");
        message.setPrefWidth(300);
        message.setPrefHeight(40);

        // 5) Reply area (TextArea inside ScrollPane)
        reply = new TextArea();
        reply.setEditable(false);
        reply.setId("atmScreenMain");
        scrollPane = new ScrollPane(reply);
        scrollPane.setPrefSize(400, 250);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        // 6) Adjust screen positions relative to ATM image
        //Gur Task Week 4 version 1.0.1
        AnchorPane.setTopAnchor(message, 150.0);
        AnchorPane.setLeftAnchor(message, 280.0);
        AnchorPane.setTopAnchor(scrollPane, 200.0);
        AnchorPane.setLeftAnchor(scrollPane, 225.0);

        // 7) Create separate Keypads
        //Gur Task Week 4 version 1.0.1
        numPad = new TilePane();
        numPad.setId("numPad");
        numPad.setPrefColumns(3); // 3-column numeric keypad
        numPad.setHgap(5);
        numPad.setVgap(5);

        commandPad = new TilePane();
        commandPad.setId("commandPad");
        commandPad.setPrefColumns(1); // Adjust column count if needed
        commandPad.setHgap(5);
        commandPad.setVgap(20);

        // 8) Populate Number Pad (0-9)
        //Gur Task Week 4 version 1.0.1
        String numberLabels[][] = {
                {"7", "8", "9"},
                {"4", "5", "6"},
                {"1", "2", "3"},
                {"CLR", "0", "Ent"}
        };

        for (String[] row : numberLabels) {
            for (String label : row) {
                if (label.length() >= 1) {
                    Button b = new Button(label);
                    b.setOnAction(this::buttonClicked);
                    b.setPrefSize(60, 60); // Increase button size
                    numPad.getChildren().add(b);
                } else {
                    numPad.getChildren().add(new Text()); // Spacer
                }
            }
        }

        // 9) Populate Command Pad (Deposit, Withdraw, Balance, Finish)
        String commandLabels[][] = {
                {"Dep"},
                {"Bal"},
                {"W/D"},
                {"Fin"}
        };

        for (String[] row : commandLabels) {
            for (String label : row) {
                if (label.length() >= 1) {
                    Button b = new Button(label);
                    b.setOnAction(this::buttonClicked);
                    b.setPrefSize(50, 40); // Increase button size
                    commandPad.getChildren().add(b);
                } else {
                    commandPad.getChildren().add(new Text()); // Spacer
                }
            }
        }

        // 10) Position Keypads on the ATM
        //Gur Task Week 4 version 1.0.1
        AnchorPane.setBottomAnchor(numPad, 95.0);  // Move numeric pad to bottom left
        AnchorPane.setLeftAnchor(numPad, 190.0);

        AnchorPane.setTopAnchor(commandPad, 200.0); // Move command buttons to top middle
        AnchorPane.setLeftAnchor(commandPad, 660.0);

        // Add all UI elements to the UI layer
        uiLayer.getChildren().addAll(message, scrollPane, numPad, commandPad);

        // 11) Scene and Window
        Scene scene = new Scene(root, W, H);
        scene.getStylesheets().add("atm.css");

        // 12) Bind image size to window size (for proper scaling)
        backgroundImageView.fitWidthProperty().bind(root.widthProperty());
        backgroundImageView.fitHeightProperty().bind(root.heightProperty());

        root.widthProperty().addListener((obs, oldVal, newVal) -> adjustElements(root.getWidth(), root.getHeight()));
        root.heightProperty().addListener((obs, oldVal, newVal) -> adjustElements(root.getWidth(), root.getHeight()));

        window.setScene(scene);
        window.setTitle("Bank ATM");
        window.setResizable(false); // Ensures window cannot be resized by the user
        window.show();
    }

    /**
     * Adjusts the layout positions of UI components dynamically when the window is resized.
     * <p>
     * This method recalculates the anchors for various UI elements based on the new window dimensions.
     * </p>
     *
     * @param newWidth  The new width of the window.
     * @param newHeight The new height of the window.
     */
    private void adjustElements(double newWidth, double newHeight) {
        double widthRatio = newWidth / W;
        double heightRatio = newHeight / H;

        AnchorPane.setTopAnchor(message, 210.0 * heightRatio);
        AnchorPane.setLeftAnchor(message, 315.0 * widthRatio);
        AnchorPane.setTopAnchor(scrollPane, 265.0 * heightRatio);
        AnchorPane.setLeftAnchor(scrollPane, 315.0 * widthRatio);
        AnchorPane.setBottomAnchor(numPad, 100.0 * heightRatio);
        AnchorPane.setLeftAnchor(numPad, 200.0 * widthRatio);
        AnchorPane.setTopAnchor(commandPad, 120.0 * heightRatio);
        AnchorPane.setLeftAnchor(commandPad, 380.0 * widthRatio);
    }

    /**
     * Event handler for button click events in the ATM interface.
     * <p>
     * This method retrieves the button that was clicked, extracts its label, logs the event, and then
     * passes the label to the controller for further processing.
     * </p>
     *
     * @param event The ActionEvent triggered by a button click.
     */
    public void buttonClicked(ActionEvent event) {
        Button b = ((Button) event.getSource());
        if (controller != null) {
            String label = b.getText();
            Debug.trace("View::buttonClicked: label = " + label);
            controller.process(label);
        }
    }

    /**
     * Updates the view's display elements based on the current state of the Model.
     * <p>
     * This method retrieves the latest display text from the Model and sets the text
     * of the message TextField and reply TextArea accordingly.
     * </p>
     */
    public void update() {
        if (model != null) {
            Debug.trace("View::update");
            message.setText(model.display1);
            reply.setText(model.display2);
        }
    }
}
