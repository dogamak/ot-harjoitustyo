package ohte;

import ohte.ui.Application;

/**
 * Class for launching the {@link Application} JavaFX class.
 * Required for creating an executable JAR file.
 */
public class Launcher {
    /**
     * Main method for the JAR file.
     *
     * @param args Command line arguments used when launching the JAR.
     */
    public static void main(String[] args) {
        javafx.application.Application.launch(Application.class, args);
    }
}
