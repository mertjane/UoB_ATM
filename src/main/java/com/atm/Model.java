package com.atm;

/**
 * The Model class represents the business logic for the ATM system.
 * <p>
 * It manages the data displayed on the ATM interface (title and message boxes),
 * handles user input
 * from the Controller, and interacts with the Bank to perform account
 * operations. When any change occurs,
 * the Model instructs the View to update the display.
 * </p>
 * <p>
 * The Model operates in one of these states:
 * <ul>
 * <li>{@code ACCOUNT_NO}: Waiting for the user to enter an account number.</li>
 * <li>{@code PASSWORD}: Waiting for the user to enter a password.</li>
 * <li>{@code LOGGED_IN}: The user is logged in and can perform
 * transactions.</li>
 * <li>{@code CHANGE_PASSWORD}: User is changing their password.</li>
 * <li>{@code CONFIRM_PASSWORD}: Waiting for password confirmation during
 * change.</li>
 * <li>{@code SELECT_ACCOUNT_TYPE}: User is selecting account type for new
 * account.</li>
 * <li>{@code NEW_ACCOUNT_PASSWORD}: Setting password for new account.</li>
 * <li>{@code CONFIRM_NEW_PASSWORD}: Confirming password for new account.</li>
 * </ul>
 * </p>
 * 
 * <p>
 * <strong>Modification History:</strong><br>
 * Bora modified in Week 5:
 * - version 3.0.1: Enhanced credential handling with String support for leading
 * zeros
 * - version 3.0.2: Added Change Password functionality with validation
 * - version 3.0.3: Implemented Create New Account with account type selection
 * - version 3.0.4: Streamlined account creation workflow with AccountCreator
 * service
 * - version 3.0.5: Refactored password validation to separate utility class
 * - version 3.0.6: Improved button labels and fixed state handling during
 * password change
 * </p>
 */
public class Model {
    /** State: Waiting for account number input */
    final String ACCOUNT_NO = "account_no";
    /** State: Waiting for password input */
    final String PASSWORD = "password";
    /**
     * State: User successfully logged in and can perform various transactions like
     * withdraw, deposit, etc.
     */
    final String LOGGED_IN = "logged_in";
    /** State: User changing password (v3.0.2 - Bora Week 5) */
    final String CHANGE_PASSWORD = "change_password";
    /** State: User confirming new password (v3.0.2 - Bora Week 5) */
    final String CONFIRM_PASSWORD = "confirm_password";
    /**
     * State: User selecting account type(Student, Gold, or Platinum) for new
     * account (v3.0.4 - Bora Week 5)
     */
    final String SELECT_ACCOUNT_TYPE = "select_account_type";
    /** State: User setting password for new account (v3.0.4 - Bora Week 5) */
    final String NEW_ACCOUNT_PASSWORD = "new_account_password";
    /** State: User confirming password for new account (v3.0.4 - Bora Week 5) */
    final String CONFIRM_NEW_PASSWORD = "confirm_new_password";

    // Model state variables
    /** The current state of the ATM model. */
    String state = ACCOUNT_NO;
    /** The current number entered by the user (as an integer). */
    int number = 0;
    /** The Bank object with which the ATM interacts. */
    Bank bank = null;
    /**
     * The account number entered by the user.
     * Week 5 - Bora - Version 3.0.1: Changed type from int to String to preserve
     * leading zeros.
     */
    String accNumber = "";
    /**
     * The password entered by the user.
     * Week 5 - Bora - Version 3.0.1: Changed type from int to String to preserve
     * leading zeros.
     */
    String accPasswd = "";
    /** The title text to be displayed (e.g., "Bank ATM"). */
    String title = "Bank ATM";
    /** The first display message (typically a single-line message). */
    String display1 = null;
    /** The second display message (which may span multiple lines). */
    String display2 = null;

    // @Mertcan week 7: Flag to track if the user has checked their balance
    private boolean hasCheckedBalance = false;

    // @Mertcan week 7: Setter for the hasCheckedBalance flag
    public void setHasCheckedBalance(boolean checked) {
        this.hasCheckedBalance = checked;
    }

    // @Mertcan week 7: Getter for the hasCheckedBalance flag
    public boolean hasCheckedBalance() {
        return this.hasCheckedBalance;
    }

    // MVC references
    /** The View associated with this Model. */
    public View view;
    /** The Controller associated with this Model. */
    public Controller controller;

    /** Selected account type for new account creation */
    private String selectedAccountType;

    /**
     * Constructs a Model instance that interacts with the specified Bank.
     *
     * @param b the {@link Bank} object that the Model will use for account
     *          operations.
     */
    public Model(Bank b) {
        Debug.trace("Model::<constructor>");
        bank = b;
    }

    /**
     * Initializes or resets the ATM model.
     * <p>
     * This method sets the state to {@code ACCOUNT_NO}, resets the entered number
     * to zero,
     * and updates the display messages with a provided message and standard
     * instructions.
     * Week 5 - Bora - Version 3.0.1: Changed to use direct state assignment instead
     * of setState()
     * for better state management with empty initial display.
     * </p>
     *
     * @param message the message to be shown in the primary display.
     */
    public void initialise(String message) {
        display1 = ""; // Start empty
        display2 = message + "\n" +
                "Enter your account number\n" +
                "Followed by \"Ent\"";
        number = 0;
        state = ACCOUNT_NO; // Direct state assignment for initialization
        display();
    }

    /**
     * Changes the current state of the Model.
     * <p>
     * If the new state differs from the current state, this method logs the
     * transition.
     * </p>
     *
     * @param newState the new state to set.
     */
    public void setState(String newState) {
        if (!state.equals(newState)) {
            String oldState = state;
            state = newState;
            Debug.trace("Model::setState: changed state from " + oldState + " to " + newState);
        }
    }

    /**
     * Processes a numeric key press.
     * <p>
     * Maintains two separate representations:
     * 1. display1 (String): Preserves leading zeros for account/password display
     * 2. number (int): Used for numerical operations like deposit/withdraw
     * </p>
     * <p>
     * For credentials (account/password): The display1 String value is used when
     * Enter is pressed
     * For transactions (deposit/withdraw): The number int value is used for
     * calculations
     * </p>
     * <p>
     * Week 5 - Bora - Version 3.0.1: Redesigned to properly handle leading zeros
     * while
     * maintaining separate display and calculation values.
     * </p>
     *
     * @param label the label of the numeric key pressed
     */
    public void processNumber(String label) {
        // Only allow up to 5 digits
        if (display1.length() >= 5) {
            return;
        }

        // If display is empty, set it to the new digit (even if it's "0")
        if (display1.isEmpty()) {
            display1 = label;
        } else {
            display1 = display1 + label; // Append new digit
        }

        // Update numeric value for calculations
        try {
            number = Integer.parseInt(display1);
        } catch (NumberFormatException e) {
            number = 0;
        }

        display();
    }

    /**
     * Processes the Clear button press.
     * <p>
     * Resets the current number and clears the primary display, then updates the
     * GUI.
     * </p>
     */
    public void processClear() {
        if (state.equals(CHANGE_PASSWORD) || state.equals(CONFIRM_PASSWORD)) {
            // Cancel password change and return to logged in state
            setState(LOGGED_IN);
            display1 = "";
            display2 = "Password change cancelled\n" +
                    "You can continue with transactions";
        } else {
            // Reset the entered number.
            number = 0;
            display1 = "";
            display(); // Refresh the GUI.
        }
    }

    /**
     * Processes the Enter button press.
     * <p>
     * This method handles multiple behaviors depending on the current state:
     * <ul>
     * <li>In the {@code ACCOUNT_NO} state, it saves the entered number as the
     * account number,
     * resets the number, changes the state to {@code PASSWORD}, and prompts the
     * user for a password.</li>
     * <li>In the {@code PASSWORD} state, it saves the entered number as the
     * password and attempts to log in.
     * If the login is successful, the state changes to {@code LOGGED_IN} and a
     * success message is displayed.
     * If not, the model is re-initialized with an error message.</li>
     * <li>In the {@code LOGGED_IN} state, no action is taken.</li>
     * </ul>
     * </p>
     * 
     * Week 5 - Bora - Version 3.0.1:
     * - Added empty input handling (defaults to "0")
     * - Uses display1 String value for credentials instead of number
     */
    public void processEnter() {
        // Process the Enter action based on the current state.
        switch (state) {
            case ACCOUNT_NO:
                // Save the account number as string
                accNumber = display1.isEmpty() ? "0" : display1;
                number = 0;
                setState(PASSWORD);
                display1 = "";
                display2 = "Now enter your password\n" +
                        "Followed by \"Ent\"";
                break;

            case PASSWORD:
                // Save the password as string and prepare for password entry.
                accPasswd = display1.isEmpty() ? "0" : display1;
                number = 0;
                display1 = "";
                if (bank.login(accNumber, accPasswd)) {
                    setState(LOGGED_IN);
                    display2 = "Accepted\n" +
                            "Now enter the transaction you require";
                } else {
                    initialise("Unknown account/password");
                }
                break;

            /**
             * Week 5 - Bora - Version 3.0.2:
             * - Added change password functionality
             */
            case CHANGE_PASSWORD:
                // Store the new password and ask for confirmation
                String newPassword = display1.isEmpty() ? "0" : display1;

                if (!PasswordValidator.isValidLength(newPassword)) {
                    display1 = "";
                    display2 = PasswordValidator.getLengthErrorMessage() + "\n" +
                            "Enter your new password\n" +
                            "Followed by \"Ent\"";
                } else {
                    accPasswd = newPassword;
                    setState(CONFIRM_PASSWORD);
                    display1 = "";
                    display2 = "Confirm your new password\n" +
                            "Followed by \"Ent\"";
                }
                break;

            case CONFIRM_PASSWORD:
                // Check if the confirmation matches
                String confirmPassword = display1.isEmpty() ? "0" : display1;
                if (confirmPassword.equals(accPasswd)) {
                    // Passwords match, update the password
                    if (bank.changePassword(accNumber, accPasswd)) {
                        setState(LOGGED_IN);
                        display1 = "";
                        display2 = "Password Changed Successfully!\n" +
                                "------------------------\n" +
                                "Your new password is now active\n" +
                                "You can continue with transactions";
                    } else {
                        setState(LOGGED_IN);
                        display1 = "";
                        display2 = "Password Change Failed\n" +
                                "------------------------\n" +
                                "Please try again later or\n" +
                                "contact support for assistance";
                    }
                } else {
                    // Passwords don't match
                    setState(CHANGE_PASSWORD);
                    display1 = "";
                    display2 = "Passwords do not match\n" +
                            "Enter your new password again\n" +
                            "Followed by \"Ent\"";
                }
                break;

            /**
             * Week 5 - Bora - Version 3.0.3:
             * - Added new account creation functionality
             */
            case SELECT_ACCOUNT_TYPE:
                // Process account type selection
                String choice = display1.isEmpty() ? "0" : display1;
                switch (choice) {
                    case "1":
                        selectedAccountType = AccountCreator.STUDENT_ACCOUNT;
                        break;
                    case "2":
                        selectedAccountType = AccountCreator.GOLD_ACCOUNT;
                        break;
                    case "3":
                        selectedAccountType = AccountCreator.PLATINUM_ACCOUNT;
                        break;
                    default:
                        display1 = "";
                        display2 = "Invalid choice. Please select:\n" +
                                "1 - Student\n" +
                                "2 - Gold\n" +
                                "3 - Platinum\n" +
                                "Enter choice followed by \"Ent\"";
                        return;
                }

                setState(NEW_ACCOUNT_PASSWORD);
                display1 = "";
                display2 = "Enter password for new " + selectedAccountType + " account\n" +
                        "Followed by \"Ent\"";
                break;

            case NEW_ACCOUNT_PASSWORD:
                // Store the new account password and ask for confirmation
                String newAccPassword = display1.isEmpty() ? "0" : display1;

                if (!PasswordValidator.isValidLength(newAccPassword)) {
                    display1 = "";
                    display2 = PasswordValidator.getLengthErrorMessage() + "\n" +
                            "Enter password for new account\n" +
                            "Followed by \"Ent\"";
                } else {
                    accPasswd = newAccPassword;
                    setState(CONFIRM_NEW_PASSWORD);
                    display1 = "";
                    display2 = "Confirm your password\n" +
                            "Followed by \"Ent\"";
                }
                break;

            case CONFIRM_NEW_PASSWORD:
                // Check if the confirmation matches
                String confirmNewPassword = display1.isEmpty() ? "0" : display1;
                if (confirmNewPassword.equals(accPasswd)) {
                    // Passwords match, create the account with selected type
                    String newAccountNumber = bank.createNewAccount(selectedAccountType, accPasswd);
                    if (newAccountNumber != null) {
                        initialise("Account Created Successfully!\n" +
                                "------------------------\n" +
                                "Your Account Number: " + newAccountNumber + "\n" +
                                "------------------------\n" +
                                "Please login with your new credentials");
                    } else {
                        initialise("Account Creation Failed\n" +
                                "Please try again or contact support");
                    }
                } else {
                    // Passwords don't match
                    setState(NEW_ACCOUNT_PASSWORD);
                    display1 = "";
                    display2 = "Passwords do not match\n" +
                            "Enter password for new account again\n" +
                            "Followed by \"Ent\"";
                }
                break;

            case LOGGED_IN:
            default:
                // No action for additional Enter presses when already logged in.
        }
        display();
    }

    /**
     * Processes a withdrawal request.
     * <p>
     * If the user is logged in, attempts to withdraw the amount represented by the
     * current number.
     * The resulting message from the Bank is displayed. If the user is not logged
     * in, the model
     * is re-initialized with an error message.
     * </p>
     * <p>
     * Made by Gur Week 4 - version 1.0.1.
     * Bora Week 5 version 3.0.6: Updated to handle non-logged in states properly
     * during password change process
     * </p>
     */
    public void processWithdraw() {
        switch (state) {
            case LOGGED_IN -> {
                if (!hasCheckedBalance) { // Check if the user has seen the balance
                    display2 = "Please check your balance before withdrawing.\nPress 'Bal' to view balance.";
                    display();
                } else {
                    boolean success = bank.withdraw(number);
                    display2 = bank.getLastMessage(); // Display the message from the Bank.
                    number = 0;
                    display1 = "";
                    display(); // Refresh the GUI.
                }
            }

            case CHANGE_PASSWORD, CONFIRM_PASSWORD -> {
                // User is in password change flow, show appropriate message
                display1 = "";
                display2 = "Please complete password change\n" + "or press CLR to cancel";
                display();
            }

            default -> initialise("You are not logged in");
        }
    }

    /**
     * Processes a deposit request.
     * <p>
     * If the user is logged in, attempts to deposit the amount represented by the
     * current number.
     * The resulting status message from the Bank is then displayed. If the user is
     * not logged in,
     * the model is re-initialized with an error message.
     * </p>
     * <p>
     * Made by Gur Week 4 - version 1.0.1.
     * Bora Week 5 version 3.0.6: Updated to handle non-logged in states properly
     * during password change process
     * </p>
     */
    public void processDeposit() {
        if (state.equals(LOGGED_IN)) {
            bank.deposit(number);
            display1 = "";
            display2 = bank.getLastMessage();
            number = 0;
        } else if (state.equals(CHANGE_PASSWORD) || state.equals(CONFIRM_PASSWORD)) {
            // User is in password change flow, show appropriate message
            display1 = "";
            display2 = "Please complete password change\n" +
                    "or press CLR to cancel";
            display();
        } else {
            initialise("You are not logged in");
        }
        display(); // Refresh the GUI.
    }

    /**
     * Processes a balance inquiry.
     * <p>
     * If the user is logged in, retrieves the current account balance from the Bank
     * and updates the display.
     * If not, the model is re-initialized with an error message.
     * </p>
     * <p>
     * Made by Gur Week 4 - version 1.0.1.
     * Bora Week 5 version 3.0.6: Updated to handle non-logged in states properly
     * during password change process
     * </p>
     */
    public void processBalance() {
        if (state.equals(LOGGED_IN)) {
            number = 0;
            display2 = "Your balance is: " + bank.getBalance();
            hasCheckedBalance = true; // @Mertcan week 7: Set flag to true once balance is checked 
        } else if (state.equals(CHANGE_PASSWORD) || state.equals(CONFIRM_PASSWORD)) {
            // User is in password change flow, show appropriate message
            display1 = "";
            display2 = "Please complete password change\n" +
                    "or press CLR to cancel";
            display();
        } else {
            initialise("You are not logged in");
        }
        display(); // Refresh the GUI.
    }

    /**
     * Processes a finish request.
     * <p>
     * If the user is logged in, this method logs out the user by resetting the
     * state to
     * {@code ACCOUNT_NO}, clearing the current number, updating the display, and
     * instructing
     * the Bank to log out. If the user is not logged in, the model is
     * re-initialized with an error message.
     * </p>
     */
    public void processFinish() {
        if (state.equals(LOGGED_IN)) {
            setState(ACCOUNT_NO);
            number = 0;
            display2 = "Welcome: Enter your account number";
            bank.logout();
        } else {
            initialise("You are not logged in");
        }
        display(); // Refresh the GUI.
    }

    /**
     * Processes an unknown key press.
     * <p>
     * When an unrecognized key is pressed, this method logs the error, resets the
     * model,
     * and displays an "Invalid command" message.
     * </p>
     *
     * @param action the label of the unknown key pressed.
     */
    public void processUnknownKey(String action) {
        Debug.trace("Model::processUnknownKey: unknown button \"" + action + "\", re-initialising");
        initialise("Invalid command");
        display();
    }

    /**
     * Updates the View with the current state of the Model.
     * <p>
     * This method calls the {@code update} method on the associated {@link View} to
     * refresh
     * the displayed information.
     * </p>
     */
    public void display() {
        Debug.trace("Model::display");
        view.update();
    }

    /**
     * Initiates the password change process.
     * <p>
     * If the user is logged in, this method transitions to the change password
     * state
     * and prompts the user to enter a new password. If not logged in, it displays
     * an error message.
     * </p>
     * Week 5 - Made by Bora - Version 3.0.2: Added change password functionality
     */
    public void processChangePassword() {
        if (state.equals(LOGGED_IN)) {
            setState(CHANGE_PASSWORD);
            number = 0;
            display1 = "";
            display2 = "Enter your new password\n" +
                    "Followed by \"Ent\"";
        } else {
            initialise("You are not logged in");
        }
        display();
    }

    /**
     * Initiates the new account creation process.
     * <p>
     * This method transitions to the account type selection state and prompts
     * the user to select an account type (Student, Gold, or Platinum).
     * </p>
     * <p>
     * Bora Week 5 version 3.0.4: Updated to start with account type selection
     * </p>
     */
    public void processNewAccount() {
        setState(SELECT_ACCOUNT_TYPE);
        number = 0;
        display1 = "";
        display2 = "Select account type:\n" +
                "1 - Student\n" +
                "2 - Gold\n" +
                "3 - Platinum\n" +
                "Enter choice followed by \"Ent\"";
        display();
    }

    void processLogout() {
        hasCheckedBalance = false; // @Mertcan week 7: Reset the flag on logout
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
