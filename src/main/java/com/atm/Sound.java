package com.atm;

import javax.sound.sampled.*;
import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;

/**
 * The {@code Sound} class provides various sound playback methods
 * for the ATM application. It includes methods to generate a simple beep,
 * an acceptance confirmation sound, and to play MP3 files for welcome and
 * thank you prompts.
 * <p>
 * This code was made by Gur.
 * </p>
 *
 * @author Gur
 */
public class Sound {

    /**
     * Produces an electronic beep (similar to an ATM button press) by generating
     * a sine wave and playing it through the system's audio output.
     */
    public static void beep() {
        new Thread(() -> {
            try {
                float sampleRate = 44100;   // Samples per second
                int durationMs = 150;       // Duration in milliseconds
                int numSamples = (int) (sampleRate * durationMs / 1000);
                double frequency = 1000;    // Frequency in Hz
                byte volume = 100;          // Volume (0-127)

                // Define the audio format: 8-bit, mono, signed.
                AudioFormat format = new AudioFormat(sampleRate, 8, 1, true, false);
                SourceDataLine line = AudioSystem.getSourceDataLine(format);
                line.open(format);
                line.start();

                // Generate a constant sine wave tone.
                byte[] buffer = new byte[numSamples];
                for (int i = 0; i < numSamples; i++) {
                    double time = i / sampleRate;
                    double angle = 2.0 * Math.PI * frequency * time;
                    buffer[i] = (byte) (Math.sin(angle) * volume);
                }

                line.write(buffer, 0, buffer.length);
                line.drain();
                line.stop();
                line.close();
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Produces a rising tone that is suitable for an acceptance confirmation sound.
     * The tone starts at a lower frequency and increases to a higher frequency.
     */
    public static void acceptedSound() {
        new Thread(() -> {
            try {
                float sampleRate = 44100;   // Samples per second
                int durationMs = 300;       // Duration in milliseconds
                int numSamples = (int) (sampleRate * durationMs / 1000);
                double startFrequency = 600;  // Starting frequency in Hz
                double endFrequency = 1000;   // Ending frequency in Hz
                byte volume = 100;          // Volume (0-127)

                // Define the audio format: 8-bit, mono, signed.
                AudioFormat format = new AudioFormat(sampleRate, 8, 1, true, false);
                SourceDataLine line = AudioSystem.getSourceDataLine(format);
                line.open(format);
                line.start();

                byte[] buffer = new byte[numSamples];
                double durationSec = durationMs / 1000.0;

                // Generate a rising tone by changing the frequency over time.
                for (int i = 0; i < numSamples; i++) {
                    double t = i / sampleRate;
                    // The phase is the integral of frequency over time.
                    double phase = 2 * Math.PI * (startFrequency * t
                            + 0.5 * (endFrequency - startFrequency) / durationSec * t * t);
                    buffer[i] = (byte) (Math.sin(phase) * volume);
                }

                line.write(buffer, 0, buffer.length);
                line.drain();
                line.stop();
                line.close();
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Plays the welcome prompt MP3 file in a separate thread using JavaFX.
     * The method initializes the JavaFX runtime if necessary, constructs a file URI,
     * and plays the media.
     */
    public static void playWelcomePrompt() {
        new Thread(() -> {
            // Initialize the JavaFX runtime using Platform.startup.
            try {
                Platform.startup(() -> {
                });
            } catch (IllegalStateException e) {
                // JavaFX runtime is already started; no action needed.
            }

            Platform.runLater(() -> {
                try {
                    // Construct a file URI for the MP3 file.
                    File file = new File("welcome_prompt.mp3"); // file sound made with https://elevenlabs.io/
                    System.out.println("Absolute path: " + file.getAbsolutePath());
                    String uriString = file.toURI().toString();

                    // Create Media and MediaPlayer instances to play the file.
                    Media media = new Media(uriString);
                    MediaPlayer mediaPlayer = new MediaPlayer(media);

                    // Play the media.
                    mediaPlayer.play();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }).start();
    }

    /**
     * Plays the thank you prompt MP3 file in a separate thread using JavaFX.
     * The method initializes the JavaFX runtime if necessary, constructs a file URI,
     * and plays the media with logging for readiness and errors.
     */
    public static void playThankYouPrompt() {
        new Thread(() -> {
            // Initialize the JavaFX runtime using Platform.startup.
            try {
                Platform.startup(() -> {
                });
            } catch (IllegalStateException e) {
                // JavaFX runtime is already started; no action needed.
            }

            Platform.runLater(() -> {
                try {
                    // Construct a file URI for the MP3 file.
                    File file = new File("thankyou.mp3"); // file sound made with https://elevenlabs.io/
                    System.out.println("Attempting to play thankyou prompt from: " + file.getAbsolutePath());
                    Media media = new Media(file.toURI().toString());
                    MediaPlayer mediaPlayer = new MediaPlayer(media);

                    // Log when the media is ready and start playback.
                    mediaPlayer.setOnReady(() -> {
                        System.out.println("Thank you prompt is ready. Starting playback.");
                        mediaPlayer.play();
                    });

                    // Log any errors that occur during media loading or playback.
                    mediaPlayer.setOnError(() -> {
                        System.err.println("Error playing thankyou prompt: " + mediaPlayer.getError());
                    });

                    // As a fallback, attempt to play immediately (if already ready).
                    mediaPlayer.play();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }).start();
    }

    /**
     * Main method to test the sound playback methods.
     * <p>
     * It triggers the ATM beep, accepted sound, welcome prompt, and thank you prompt.
     * </p>
     *
     * @param args the command-line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("Triggering ATM beep...");
        beep();
        System.out.println("Triggering accepted sound...");
        acceptedSound();
        System.out.println("Playing welcome prompt sound...");
        playWelcomePrompt();
        System.out.println("Playing thank you prompt sound...");
        playThankYouPrompt();
        System.out.println("Sounds triggered. Program continues executing.");
    }
}
