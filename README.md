# UoB ATM Application

A Java-based ATM (Automated Teller Machine) application with a modern JavaFX user interface.

## Features

- **User Authentication**
  - Secure PIN-based login
  - Account creation
  - PIN change functionality
  - Secure logout

- **Account Operations**
  - Balance inquiry
  - Cash withdrawal
  - Cash deposit
  - Money transfer between accounts
  - Transaction history viewing

- **User Interface**
  - Modern JavaFX-based interface
  - Responsive design
  - Sound effects for button interactions
  - Intuitive button layout
  - Transaction receipts

## Technical Details

- **Built with**
  - Java
  - JavaFX for the user interface
  - CSS for styling

- **Architecture**
  - MVC (Model-View-Controller) pattern
  - Object-oriented design
  - Event-driven programming

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 11 or higher
- JavaFX SDK
- Maven (for dependency management)

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/mertjane/UoB_ATM.git
   ```

2. Navigate to the project directory:
   ```bash
   cd UoB_ATM
   ```

3. Build the project:
   ```bash
   mvn clean install
   ```

4. Run the application:
   ```bash
   mvn javafx:run
   ```

## Usage

1. **Login**
   - Enter your account number
   - Enter your PIN
   - Click "Ent" to login

2. **Main Operations**
   - **Deposit**: Click "Dep" and enter amount
   - **Withdraw**: Click "W/D" and enter amount
   - **Balance**: Click "Bal" to view current balance
   - **Transactions**: Click "Transactions" to view recent transactions
   - **Send Money**: Click "Send Money" to transfer funds
   - **Change PIN**: Click "Change PIN" to update your security code
   - **New Account**: Click "New Account" to create a new bank account
   - **Log Out**: Click "Log Out" to securely end your session

## Development History

- **Week 4 (v1.0.1)**: Initial implementation
- **Week 5 (v3.0.2)**: Added Change Password and Create Account features
- **Week 6 (v4.0.0)**: 
  - Implemented responsive design
  - Added new background image
  - Updated view to BasePane
- **Week 7**: 
  - Added sound effects
  - Implemented welcome prompt
- **Week 8**:
  - Added transaction history feature
  - Implemented money transfer functionality
  - Added low balance warnings

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- University of Bristol
- All contributors to the project 