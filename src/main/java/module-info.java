module com.atm {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql; // declaring the dependency on java.sql module
    requires javafx.media; // Added for AudioClip
    requires java.dotenv; // Environment Variables
    requires java.desktop; // Added by Gur - for sound options

    opens com.atm to javafx.fxml;
    exports com.atm;
}