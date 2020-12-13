package ohte.ui;

import java.io.IOException;
import java.io.File;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.stage.FileChooser;

import ohte.domain.Credentials;

/**
 * Manages the startup UI flow and launches the main UI.
 */
public class Application extends javafx.application.Application {
    /**
     * Inventory file to be created or opened.
     */
    private File file;

    /**
     * Credentials for unlocking an inventory file or creating a superuser.
     */
    private Credentials credentials;

    /**
     * Reference to the global {@link ohte.domain.Application} instance.
     */
    private ohte.domain.Application app = ohte.domain.Application.getSingleton();

    /**
     * The startup action chosen by the user.
     * Either {@code OPEN} or {@code CREATE}.
     */
    private InventoryAction startupAction;

    /**
     * Counter of authentication attempts.
     * Used to display a varying message to the user.
     */
    private int authTryCount = 0;

    /**
     * Current JavaFX stage.
     */
    private Stage stage;

    /**
     * Entry point of the UI code.
     * Called by JavaFX after application initialization.
     */
    @Override
    public void start(Stage stage) {
        handleParameters();
        this.stage = stage;
        startupFlow();
        showMainView();
    }

    /**
     * Checks the provided command-line parameters and
     * populates the member fields accordingly.
     */
    private void handleParameters() {
        List<String> unnamed = getParameters().getUnnamed();
        Map<String, String> named = getParameters().getNamed();

        if (!unnamed.isEmpty()) {
            file = new File(unnamed.get(0));
        }

        String username = named.get("user");
        String password = named.get("password");

        if (username != null && password != null) {
            credentials = new Credentials(username, password);
        }

        String action = named.get("action");

        if ("create".equals(action)) {
            startupAction = InventoryAction.CREATE;
        } else if ("open".equals(action)) {
            startupAction = InventoryAction.OPEN;
        }
    }

    /**
     * Trunk for launching the application.
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Launch the startup UI flow. Skips initial prompts for
     * values that were provided via command-line arguments.
     *
     * The startup flow consists of the following steps:
     * <ol>
     *     <li>Prompting for the desired action (open or create) using {@link WelcomeDialog}</li>
     *     <li>Showing the appropriate file chooser dialog.</li>
     *     <li>Prompt for account credentials using {@link CredentialsDialog}.</li>
     * </ol>
     */
    private void startupFlow() {
        InventoryAction result;

        while (true) {
            // Prompt for the desired action if not known.
            if (startupAction == null) {
                startupAction = new WelcomeDialog()
                    .showAndWait()
                    .orElse(null);
            }

            // Prompt for the inventory file if not known.
            if (file == null) {
                showFileChooser();
            }

            boolean canceled = false;

            // Prompt for credentials if not known.
            if (credentials == null) {
                canceled = promptForCredentials()
                    .map(r -> {
                        credentials = r.getCredentials();
                        return r.wasCanceled();
                    })
                    .orElse(true);
            }

            if (canceled) {
                // If user selects Cancel from the credentials dialog,
                // clear the value for credentials, causing a re-prompt of all values.
                credentials = null;
            } else if (!authenticate(credentials)) {
                // If the user enters the wrong credentials, clear the credentials,
                // but do not re-prompt for anything else.
                credentials = null;
                continue;
            }

            // If any of the following values are not defined, clear them all an re-prompt.
            // If all of them are known, return.
            if (startupAction == null || file == null || credentials == null) {
                startupAction = null;
                file = null;
                credentials = null;
            } else {
                break;
            }
        }
    }

    /**
     * Try to authenticate or create an account using the provided credentials.
     *
     * @return True if authentication or account creation was successfull.
     */
    private boolean authenticate(Credentials creds) {
        boolean success = false;

        if (startupAction == InventoryAction.CREATE) {
            app.createInventory(file.getAbsolutePath(), creds);
            success = true;
        } else if (startupAction == InventoryAction.OPEN) {
            success = app.openInventory(file.getAbsolutePath(), creds);
        }

        return success;
    }

    /**
     * Show file chooser with differing properties based on the selected inventory action.
     */
    private void showFileChooser() {
        FileChooser chooser = new FileChooser();

        if (startupAction == InventoryAction.CREATE) {
            file = chooser.showSaveDialog(stage);
        } else if (startupAction == InventoryAction.OPEN) {
            file = chooser.showOpenDialog(stage);
        }
    }

    /**
     * Prompts for account credentials.
     */
    private Optional<CredentialsDialog.Result> promptForCredentials() {
        CredentialsDialog dialog;

        boolean isRetry = authTryCount > 0;
        authTryCount++;

        if (startupAction == InventoryAction.OPEN) {
            dialog = createUnlockCredentialsDialog(isRetry);
        } else if (startupAction == InventoryAction.CREATE) {
            dialog = createAccountCreationDialog();
        } else {
            return Optional.empty();
        }

        return dialog.showAndWait();
    }

    /**
     * Constructs the {@link CredentialsDialog} with message appropriate
     * for the action of opening an existing inventory file.
     */
    private CredentialsDialog createUnlockCredentialsDialog(boolean retry) {
        CredentialsDialog dialog = new CredentialsDialog("Unlock");

        String message = "Provide valid credentials to unlock the inventory file:";

        if (retry) {
            message = "Authentication failed!\n" + message;
        }

        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);

        dialog.getMessagePane().getChildren().add(messageLabel);

        return dialog;
    }

    /**
     * Constructs the {@link CredentialsDialog} with message appropriate
     * for the action of creating a new inventory file and superuser.
     */
    private CredentialsDialog createAccountCreationDialog() {
        CredentialsDialog dialog = new CredentialsDialog("Create superuser");

        Label message = new Label(String.format(
            "Creating new inventory file %s.\nPlease give credentials " +
            "for creating a superuser account:",
            file.getName()
        ));

        message.setWrapText(true);

        dialog
            .getMessagePane()
            .getChildren()
            .add(message);

        return dialog;
    }

    /**
     * Creates and displays {@link MainViewController the main view}.
     */
    private void showMainView() {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader()
                .getResource("ui/MainView.fxml"));

        try {
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        stage.setTitle("IT Asset Manager");
        stage.show();
    }
}
