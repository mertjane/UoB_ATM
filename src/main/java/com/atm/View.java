package com.atm;

import java.util.List;

import com.atm.utils.TransactionReader;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
import javafx.stage.Stage; // Import for sound

/**
 * The {@code View} class is responsible for constructing and managing the ATM
 * user interface.
 * It creates and lays out all UI components, handles user interactions, and
 * updates the display
 * based on data from the {@link Model} and instructions from the
 * {@link Controller}.
 *
 * Version and Task Info:
 * 1. Gur Task Week 4 version 1.0.1
 * 2. Bora modified in Week 5 (version 3.0.2):
 * - Added Change Password button to the interface
 * - Added Create Account button to the interface
 * 3. Gur Modified in week 6 (version 4.0.0):
 * - responsive design
 * - new background image
 * - change view to BasePane
 * 4. Gur week 7:
 * - Implementing sound effects when pressing buttons.
 * - Welcome prompt.
 */

class View {

    private Bank bank;
    public Model model;
    public Controller controller;

    // The design width used when placing UI components.
    private static final double DESIGN_WIDTH = 850;
    private static final double DESIGN_HEIGHT = 850;

    // UI components

    TextField message;
    TextArea reply;
    ScrollPane scrollPane;
    TilePane numPad;
    TilePane commandPad;
    ImageView backgroundImageView;
    TilePane extraPad;
    Button logOutButton;

    /* public View() {
        Debug.trace("View::<constructor>");
    } */

    public View(Bank bank) {
        if (bank == null) {
            throw new IllegalArgumentException("Bank cannot be null");
        }
        this.bank = bank;
        Debug.trace("View::<constructor>");
    }

    /**
     * Initializes and displays the ATM user interface on the given stage.
     *
     * <p>
     * This method sets up the main layout, loads images, creates all UI controls
     * (such as the
     * numeric keypad, command keypad, and extra controls), and configures event
     * handling for user
     * interactions. It also establishes scaling behavior to maintain the aspect
     * ratio when the window is resized.
     * </p>
     *
     * @param window the primary stage on which the UI will be set.
     */
    public void start(Stage window) {
        Debug.trace("View::start");

        // 1) Create a fixed-size Pane at our original (design) dimensions
        Pane basePane = new Pane();
        basePane.setPrefSize(DESIGN_WIDTH, DESIGN_HEIGHT);

        // 2) Add the "ATM console" background image at (0,0), sized to the design
        // dimensions
        Image backgroundImage = new Image("atm.jpg"); // the "ATM machine" area
        backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setLayoutX(0);
        backgroundImageView.setLayoutY(0);
        backgroundImageView.setFitWidth(DESIGN_WIDTH);
        backgroundImageView.setFitHeight(DESIGN_HEIGHT);
        backgroundImageView.setPreserveRatio(true);
        basePane.getChildren().add(backgroundImageView);

        // 3) Create and place the message TextField
        message = new TextField();
        message.setEditable(false);
        message.setId("atmScreenTop");
        message.setPrefSize(300, 40);
        message.setLayoutX(280);
        message.setLayoutY(150);
        basePane.getChildren().add(message);

        // 4) Create and place the scrollable reply area
        reply = new TextArea();
        reply.setEditable(false);
        reply.setId("atmScreenMain");
        scrollPane = new ScrollPane(reply);
        scrollPane.setPrefSize(400, 250);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setLayoutX(225);
        scrollPane.setLayoutY(200);
        basePane.getChildren().add(scrollPane);

        // 5) Create the numeric keypad
        numPad = new TilePane();
        numPad.setId("numPad");
        numPad.setPrefColumns(3);
        numPad.setHgap(5);
        numPad.setVgap(5);

        String[][] numberLabels = {
                { "7", "8", "9" },
                { "4", "5", "6" },
                { "1", "2", "3" },
                { "CLR", "0", "Ent" }
        };

        for (String[] row : numberLabels) {
            for (String label : row) {
                if (!label.isEmpty()) {
                    Button b = new Button(label);
                    b.setOnAction(this::buttonClicked);
                    b.setPrefSize(60, 60);
                    numPad.getChildren().add(b);
                } else {
                    numPad.getChildren().add(new Text()); // Spacer
                }
            }
        }
        numPad.setLayoutX(190);
        numPad.setLayoutY(500);
        basePane.getChildren().add(numPad);

        // 6) Create the command keypad (Deposit, Bal, W/D, Finish)
        commandPad = new TilePane();
        commandPad.setId("commandPad");
        commandPad.setPrefColumns(1);
        commandPad.setHgap(5);
        commandPad.setVgap(20);

        String[][] commandLabels = { { "Dep" }, { "Bal" }, { "W/D" }, { "Fin" } };
        for (String[] row : commandLabels) {
            for (String label : row) {
                if (!label.isEmpty()) {
                    Button b = new Button(label);
                    b.setOnAction(this::buttonClicked);
                    b.setPrefSize(50, 40);
                    commandPad.getChildren().add(b);
                } else {
                    commandPad.getChildren().add(new Text()); // Spacer
                }
            }
        }
        commandPad.setLayoutX(660);
        commandPad.setLayoutY(200);
        basePane.getChildren().add(commandPad);

        // 7) Extra pad for "Change PIN", "New Account" and "Log Out"
        extraPad = new TilePane();
        extraPad.setId("extraPad");
        extraPad.setPrefColumns(1);
        extraPad.setHgap(5);
        extraPad.setVgap(20);

        Button chpButton = new Button("Change PIN");
        chpButton.setOnAction(this::buttonClicked);
        chpButton.setPrefSize(80, 40);
        chpButton.setPadding(new Insets(2));
        extraPad.getChildren().add(chpButton);

        Button newAccButton = new Button("New Account");
        newAccButton.setOnAction(this::buttonClicked);
        newAccButton.setPrefSize(80, 40);
        newAccButton.setPadding(new Insets(2));
        extraPad.getChildren().add(newAccButton);

        // Transactions/Receipts Button
        Button transButton = new Button("Transactions");
        transButton.setOnAction(this::transButtonClicked);
        transButton.setPrefSize(80, 40);
        transButton.setPadding(new Insets(2));
        extraPad.getChildren().add(transButton);

        // Create the "Log Out" button
        logOutButton = new Button("Log Out");
        logOutButton.setOnAction(this::logOutButtonClicked); // Set action handler
        logOutButton.setPrefSize(80, 40);
        logOutButton.setPadding(new Insets(2));
        extraPad.getChildren().add(logOutButton);

        extraPad.setLayoutX(110);
        extraPad.setLayoutY(200);
        basePane.getChildren().add(extraPad);

        // 8) Wrap basePane inside a Group so we can scale the entire ATM layout
        Group atmGroup = new Group(basePane);

        // 9) Build a StackPane root. Add a second "full-window" background behind the
        // atmGroup
        StackPane root = new StackPane();
        ImageView fullWindowBackground = new ImageView(new Image("background.jpg"));
        fullWindowBackground.setPreserveRatio(false);
        fullWindowBackground.fitWidthProperty().bind(root.widthProperty());
        fullWindowBackground.fitHeightProperty().bind(root.heightProperty());
        root.getChildren().addAll(fullWindowBackground, atmGroup);

        // 10) Create the Scene
        Scene scene = new Scene(root, DESIGN_WIDTH, DESIGN_HEIGHT);
        scene.getStylesheets().add("atm.css");

        // 11) Scale the atmGroup whenever the window is resized (uniform scale)
        root.widthProperty().addListener((obs, oldV, newV) -> scaleAll(atmGroup, scene.getWidth(), scene.getHeight()));
        root.heightProperty().addListener((obs, oldV, newV) -> scaleAll(atmGroup, scene.getWidth(), scene.getHeight()));

        // 12) Show the Stage
        window.setScene(scene);
        window.setTitle("Bank ATM");
        window.setResizable(true);
        window.show();

        // Scale once at startup
        scaleAll(atmGroup, scene.getWidth(), scene.getHeight());
    }

    /**
     * Scales the entire layout to maintain a fixed aspect ratio.
     */
    private void scaleAll(Group group, double currentWidth, double currentHeight) {
        double scaleX = currentWidth / DESIGN_WIDTH;
        double scaleY = currentHeight / DESIGN_HEIGHT;
        double scale = Math.min(scaleX, scaleY);
        group.setScaleX(scale);
        group.setScaleY(scale);
    }

    /**
     * Handles button click events from all ATM interface buttons.
     */
    public void buttonClicked(ActionEvent event) {
        Button b = (Button) event.getSource();
        Sound.beep(); // make a beep sound when button is clicked - Gur version 05.04.2025
        // Play the sound when any button is clicked
        // buttonClickSound.play();

        if (controller != null) {
            String label = b.getText();
            Debug.trace("View::buttonClicked: label = " + label);
            controller.process(label);
        }
    }

    /**
     * Handling the transaction button click
     * @Mertcan week8 v3.2.0
     * @param event
     */

    public void transButtonClicked(ActionEvent event) {
        Sound.beep();
        Debug.trace("View::transButtonClicked");

        // Get the current logged-in account (BankAccount)
        BankAccount account = bank.getCurrentAccount();

        // Ensure account is not null before proceeding
        if (account != null) {
            // Get the last transaction for this account
            List<String> transactions = TransactionReader.getTransactions(account.getAccNumber(), 1);

            if (!transactions.isEmpty()) {
                String[] fields = transactions.get(0).split(",");
                if (fields.length >= 5) {
                    String timestamp = fields[0];
                    String accNumber = fields[1];
                    String type = fields[2];
                    double amount = Double.parseDouble(fields[3]);
                    double balance = Double.parseDouble(fields[4]);

                    String receipt = String.format("""
                            ====== Transaction Receipt ======
                            Date: %s
                            Account: ****%s
                            --------------------------------
                            %s:     £%.2f
                            Balance:        £%.2f
                            Thank you for using our ATM.
                            =================================
                            """,
                            timestamp,
                            accNumber.substring(accNumber.length() - 4),
                            type,
                            amount,
                            balance);

                    reply.setText(receipt);
                    message.setText("Transaction Summary");
                } else {
                    reply.setText("Transaction format invalid.");
                    message.setText("Error");
                }
            } else {
                reply.setText("No recent transactions found.");
                message.setText("No Transactions");
            }
        } else {
            reply.setText("No account is logged in.");
            message.setText("Error");
        }
    }

    /**
     * Nadeen EL Samad week 6: create a logout button for users with a goodbye
     * message.
     * Handles the "Log Out" button click event.
     * This method will be called when the Log Out button is clicked.
     */
    public void logOutButtonClicked(ActionEvent event) {
        Sound.beep(); // make a beep sound when button is clicked - Gur version 05.04.2025
        Debug.trace("View::logOutButtonClicked");

        // Create an instance of GoodbyePage and pass the current window (Stage)
        GoodbyePage goodbyePage = new GoodbyePage();
        goodbyePage.start((Stage) ((Node) event.getSource()).getScene().getWindow());
    }

    /**
     * Nadeen EL Samad week 6.
     * Updates the ATM display by refreshing the message and reply fields.
     *
     * <p>
     * This method is typically called after the model is updated, ensuring that the
     * UI reflects the current state
     * of the application. It sets the text of the message field and reply area
     * based on the data in the {@code Model}.
     * </p>
     */
    public void update() {
        if (model != null) {
            Debug.trace("View::update");
            message.setText(model.display1);
            reply.setText(model.display2);
        }
    }

    /**
     * Displays an error message to the user.
     * 
     * @param message The error message to display
     */
    public void showErrorMessage(String message) {
        this.message.setText(message);
        this.reply.setText("Error: " + message);
        update();
    }

    /**
     * Shows a confirmation dialog to the user.
     * 
     * @param message The confirmation message to display
     * @return true if user confirms, false otherwise
     */
    public boolean showConfirmationDialog(String message) {
        // TODO: Implement actual dialog
        // For now, always return true
        return true;
    }
}
