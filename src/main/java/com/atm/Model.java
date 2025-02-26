package com.atm;

/**
 * The Model class encapsulates the core functionality and state of the ATM application.
 * <p>
 * It manages the data displayed on the ATM interface (title and message boxes), handles user input
 * from the Controller, and interacts with the Bank to perform account operations. When any change occurs,
 * the Model instructs the View to update the display.
 * </p>
 * <p>
 * The Model operates in one of three states:
 * <ul>
 *   <li>{@code ACCOUNT_NO}: Waiting for the user to enter an account number.</li>
 *   <li>{@code PASSWORD}: Waiting for the user to enter a password.</li>
 *   <li>{@code LOGGED_IN}: The user is logged in and can perform transactions.</li>
 * </ul>
 * </p>
 */
public class Model
{
    /** Constant for the "account_no" state. */
    final String ACCOUNT_NO = "account_no";
    /** Constant for the "password" state. */
    final String PASSWORD = "password";
    /** Constant for the "logged_in" state. */
    final String LOGGED_IN = "logged_in";

    // Model state variables
    /** The current state of the ATM model. */
    String state = ACCOUNT_NO;
    /** The current number entered by the user (as an integer). */
    int number = 0;
    /** The Bank object with which the ATM interacts. */
    Bank bank = null;
    /** The account number entered by the user. */
    int accNumber = -1;
    /** The password entered by the user. */
    int accPasswd = -1;
    /** The title text to be displayed (e.g., "Bank ATM"). */
    String title = "Bank ATM";
    /** The first display message (typically a single-line message). */
    String display1 = null;
    /** The second display message (which may span multiple lines). */
    String display2 = null;

    // MVC references
    /** The View associated with this Model. */
    public View view;
    /** The Controller associated with this Model. */
    public Controller controller;

    /**
     * Constructs a Model instance that interacts with the specified Bank.
     *
     * @param b the {@link Bank} object that the Model will use for account operations.
     */
    public Model(Bank b)
    {
        Debug.trace("Model::<constructor>");
        bank = b;
    }

    /**
     * Initializes or resets the ATM model.
     * <p>
     * This method sets the state to {@code ACCOUNT_NO}, resets the entered number to zero,
     * and updates the display messages with a provided message and standard instructions.
     * </p>
     *
     * @param message the message to be shown in the primary display.
     */
    public void initialise(String message) {
        setState(ACCOUNT_NO);
        number = 0;
        display1 = message;
        display2 = "Enter your account number\n" +
                "Followed by \"Ent\"";
    }

    /**
     * Changes the current state of the Model.
     * <p>
     * If the new state differs from the current state, this method logs the transition.
     * </p>
     *
     * @param newState the new state to set.
     */
    public void setState(String newState)
    {
        if (!state.equals(newState))
        {
            String oldState = state;
            state = newState;
            Debug.trace("Model::setState: changed state from " + oldState + " to " + newState);
        }
    }

    /**
     * Processes a numeric key press.
     * <p>
     * Converts the first character of the key label into an integer, updates the current number,
     * and then refreshes the display.
     * </p>
     *
     * @param label the label of the numeric key pressed.
     */
    public void processNumber(String label)
    {
        // Convert the first character of the label to an integer and build the current number.
        char c = label.charAt(0);
        number = number * 10 + c - '0';
        // Update the primary display with the new number.
        display1 = "" + number;
        display();  // Refresh the GUI.
    }

    /**
     * Processes the Clear button press.
     * <p>
     * Resets the current number and clears the primary display, then updates the GUI.
     * </p>
     */
    public void processClear()
    {
        // Reset the entered number.
        number = 0;
        display1 = "";
        display();  // Refresh the GUI.
    }

    /**
     * Processes the Enter button press.
     * <p>
     * This method handles multiple behaviors depending on the current state:
     * <ul>
     *   <li>In the {@code ACCOUNT_NO} state, it saves the entered number as the account number,
     *       resets the number, changes the state to {@code PASSWORD}, and prompts the user for a password.</li>
     *   <li>In the {@code PASSWORD} state, it saves the entered number as the password and attempts to log in.
     *       If the login is successful, the state changes to {@code LOGGED_IN} and a success message is displayed.
     *       If not, the model is re-initialized with an error message.</li>
     *   <li>In the {@code LOGGED_IN} state, no action is taken.</li>
     * </ul>
     * </p>
     */
    public void processEnter()
    {
        // Process the Enter action based on the current state.
        switch (state)
        {
            case ACCOUNT_NO:
                // Save the account number and prepare for password entry.
                accNumber = number;
                number = 0;
                setState(PASSWORD);
                display1 = "";
                display2 = "Now enter your password\n" +
                        "Followed by \"Ent\"";
                break;
            case PASSWORD:
                // Save the password and attempt to log in.
                accPasswd = number;
                number = 0;
                display1 = "";
                if (bank.login(accNumber, accPasswd))
                {
                    setState(LOGGED_IN);
                    display2 = "Accepted\n" +
                            "Now enter the transaction you require";
                }
                else
                {
                    initialise("Unknown account/password");
                }
                break;
            case LOGGED_IN:
            default:
                // No action for additional Enter presses when already logged in.
        }
        display();  // Refresh the GUI.
    }

    /**
     * Processes a withdrawal request.
     * <p>
     * If the user is logged in, attempts to withdraw the amount represented by the current number.
     * The resulting message from the Bank is displayed. If the user is not logged in, the model
     * is re-initialized with an error message.
     * </p>
     * <p>
     * Made by Gur Week 4 - version 1.0.1.
     * </p>
     */
    public void processWithdraw()
    {
        if (state.equals(LOGGED_IN))
        {
            bank.withdraw(number);
            display2 = bank.getLastMessage(); // Display the message from the Bank.
            number = 0;
            display1 = "";
        }
        else
        {
            initialise("You are not logged in");
        }
        display();  // Refresh the GUI.
    }

    /**
     * Processes a deposit request.
     * <p>
     * If the user is logged in, attempts to deposit the amount represented by the current number.
     * The resulting status message from the Bank is then displayed. If the user is not logged in,
     * the model is re-initialized with an error message.
     * </p>
     * <p>
     * Made by Gur Week 4 - version 1.0.1.
     * </p>
     */
    public void processDeposit()
    {
        if (state.equals(LOGGED_IN))
        {
            bank.deposit(number);
            display1 = "";
            display2 = bank.getLastMessage();
            number = 0;
        }
        else
        {
            initialise("You are not logged in");
        }
        display();  // Refresh the GUI.
    }

    /**
     * Processes a balance inquiry.
     * <p>
     * If the user is logged in, retrieves the current account balance from the Bank and updates the display.
     * If not, the model is re-initialized with an error message.
     * </p>
     * <p>
     * Made by Gur Week 4 - version 1.0.1.
     * </p>
     */
    public void processBalance()
    {
        if (state.equals(LOGGED_IN))
        {
            number = 0;
            display2 = "Your balance is: " + bank.getBalance();
        }
        else
        {
            initialise("You are not logged in");
        }
        display();  // Refresh the GUI.
    }

    /**
     * Processes a finish request.
     * <p>
     * If the user is logged in, this method logs out the user by resetting the state to
     * {@code ACCOUNT_NO}, clearing the current number, updating the display, and instructing
     * the Bank to log out. If the user is not logged in, the model is re-initialized with an error message.
     * </p>
     */
    public void processFinish()
    {
        if (state.equals(LOGGED_IN))
        {
            setState(ACCOUNT_NO);
            number = 0;
            display2 = "Welcome: Enter your account number";
            bank.logout();
        }
        else
        {
            initialise("You are not logged in");
        }
        display();  // Refresh the GUI.
    }

    /**
     * Processes an unknown key press.
     * <p>
     * When an unrecognized key is pressed, this method logs the error, resets the model,
     * and displays an "Invalid command" message.
     * </p>
     *
     * @param action the label of the unknown key pressed.
     */
    public void processUnknownKey(String action)
    {
        Debug.trace("Model::processUnknownKey: unknown button \"" + action + "\", re-initialising");
        initialise("Invalid command");
        display();
    }

    /**
     * Updates the View with the current state of the Model.
     * <p>
     * This method calls the {@code update} method on the associated {@link View} to refresh
     * the displayed information.
     * </p>
     */
    public void display()
    {
        Debug.trace("Model::display");
        view.update();
    }
}
