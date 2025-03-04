package com.atm;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

class View {
    private double minWidth = 700;
    private double minHeight = 700;
    
    Label title;
    TextField message;
    TextArea reply;
    ScrollPane scrollPane;
    GridPane numPad, commandPad;

    public Model model;
    public Controller controller;

    public View() {
        Debug.trace("View::<constructor>");
    }

    public void start(Stage window) {
        Debug.trace("View::start");

        // Root layout with a solid background color for ATM look
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1A3C1A;"); // Dark green for ATM body
        
        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(30));
        mainLayout.setStyle("-fx-background-color: #E6ECE6; -fx-border-color: #666; -fx-border-width: 2px; -fx-border-radius: 10px;"); // Light gray ATM panel

        // Screen area
        VBox screenArea = new VBox(20);
        screenArea.setAlignment(Pos.CENTER);
        screenArea.maxWidthProperty().bind(root.widthProperty().multiply(0.6)); // 60% of window width for screen
        
        message = new TextField();
        message.setEditable(false);
        message.setId("atmScreenTop");
        message.setAlignment(Pos.CENTER);
        message.prefWidthProperty().bind(screenArea.widthProperty());
        
        reply = new TextArea();
        reply.setEditable(false);
        reply.setId("atmScreenMain");
        reply.setWrapText(true);
        reply.prefWidthProperty().bind(screenArea.widthProperty().multiply(0.95)); // 95% of screenArea width
        reply.prefHeightProperty().bind(root.heightProperty().multiply(0.4));     // 40% of window height
        reply.setMinHeight(200);
        reply.setMaxHeight(400);

        scrollPane = new ScrollPane(reply);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.prefWidthProperty().bind(screenArea.widthProperty());
        scrollPane.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;"); // Transparent scrollpane
        
        screenArea.getChildren().addAll(message, scrollPane);

        // Button area
        HBox buttonContainer = new HBox(40);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.setPadding(new Insets(20));
        
        numPad = createNumPad();
        commandPad = createCommandPad();
        
        buttonContainer.getChildren().addAll(numPad, commandPad);
        
        // Layout arrangement
        mainLayout.setCenter(screenArea);
        mainLayout.setBottom(buttonContainer);
        
        BorderPane.setAlignment(screenArea, Pos.CENTER);
        BorderPane.setAlignment(buttonContainer, Pos.CENTER);
        
        root.setCenter(mainLayout);

        Scene scene = new Scene(root, minWidth, minHeight);
        scene.getStylesheets().add("atm.css");

        window.setScene(scene);
        window.setTitle("Bank ATM");
        window.setMinWidth(minWidth);
        window.setMinHeight(minHeight);
        window.setResizable(true);
        
        // Update button sizes on window resize
        window.widthProperty().addListener((obs, old, newVal) -> updateButtonSizes());
        window.heightProperty().addListener((obs, old, newVal) -> updateButtonSizes());
        
        window.show();
    }

    private GridPane createNumPad() {
        GridPane numPad = new GridPane();
        numPad.setId("numPad");
        numPad.setAlignment(Pos.CENTER);
        numPad.setHgap(15);
        numPad.setVgap(15);

        String[][] numberLabels = {
            {"7", "8", "9"},
            {"4", "5", "6"},
            {"1", "2", "3"},
            {"CLR", "0", "Ent"}
        };

        for (int row = 0; row < numberLabels.length; row++) {
            for (int col = 0; col < numberLabels[row].length; col++) {
                Button b = new Button(numberLabels[row][col]);
                b.setOnAction(this::buttonClicked);
                numPad.add(b, col, row);
            }
        }
        return numPad;
    }

    private GridPane createCommandPad() {
        GridPane commandPad = new GridPane();
        commandPad.setId("commandPad");
        commandPad.setAlignment(Pos.CENTER);
        commandPad.setVgap(20);

        String[] commands = {"Deposit", "Balance", "Withdraw", "Finish"};
        for (int i = 0; i < commands.length; i++) {
            Button b = new Button(commands[i]);
            b.setOnAction(this::buttonClicked);
            commandPad.add(b, 0, i);
        }
        return commandPad;
    }

    private void updateButtonSizes() {
        double windowSize = Math.min(minWidth, minHeight);
        double numButtonSize = windowSize * 0.1;  // Larger buttons for better visibility
        double cmdButtonWidth = windowSize * 0.15;
        double cmdButtonHeight = windowSize * 0.08;

        for (var node : numPad.getChildren()) {
            if (node instanceof Button) {
                ((Button)node).setPrefSize(numButtonSize, numButtonSize);
            }
        }
        
        for (var node : commandPad.getChildren()) {
            if (node instanceof Button) {
                ((Button)node).setPrefSize(cmdButtonWidth, cmdButtonHeight);
            }
        }
    }

    public void buttonClicked(ActionEvent event) {
        Button b = ((Button) event.getSource());
        if (controller != null) {
            String label = b.getText();
            Debug.trace("View::buttonClicked: label = " + label);
            controller.process(label);
        }
    }

    public void update() {
        if (model != null) {
            Debug.trace("View::update");
            message.setText(model.display1);
            reply.setText(model.display2);
        }
    }
}