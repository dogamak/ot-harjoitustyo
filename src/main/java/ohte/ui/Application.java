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
    private File file;
    private Credentials credentials;
    private ohte.domain.Application app = ohte.domain.Application.getSingleton();
    private InventoryAction startupAction;
    private int authTryCount = 0;
    private Stage stage;

    @Override
    public void start(Stage stage) {
        handleParameters();
        this.stage = stage;
        startupFlow();
        showMainView();
    }

    private void handleParameters() {
        List<String> unnamed = getParameters().getUnnamed();
        Map<String, String> named = getParameters().getNamed();

        if (!unnamed.isEmpty()) {
            file = new File(unnamed.get(0));
        }

        System.out.println(unnamed.stream().collect(Collectors.joining(", ")));

        String username = named.get("user");
        String password = named.get("password");

        if (username != null && password != null) {
            credentials = new Credentials(username, password);
        }

        String action = named.get("action");

        System.out.println("U: " + username + " P: " + password + " A: " + action);

        if ("create".equals(action)) {
            startupAction = InventoryAction.CREATE;
        } else if ("open".equals(action)) {
            startupAction = InventoryAction.OPEN;
        }
    }

    public void setStartupAction(InventoryAction action) {
        startupAction = action;
    }

    public void setInventoryFile(File pfile) {
        file = pfile;
    }

    public void setCredentials(Credentials pcreds) {
        credentials = pcreds;
    }

    public static void main(String[] args) {
        launch();
    }

    /**
     * Start the startup UI flow, which prompts for inventory file and credentials.
     *
     * The startup flow consists of two dialogs: {@link WelcomeDialog}
     * and {@link InventoryDialog}.
     */
    private void startupFlow() {
        InventoryAction result;

        while (true) {
            if (startupAction == null) {
                startupAction = new WelcomeDialog()
                    .showAndWait()
                    .orElse(null);
            }

            if (file == null) {
                showFileChooser();
            }

            boolean canceled = false;

            if (credentials == null) {
                canceled = promptForCredentials()
                    .map(r -> {
                        credentials = r.getCredentials();
                        return r.wasCanceled();
                    })
                    .orElse(true);
            }

            if (canceled) {
                credentials = null;
            } else if (!authenticate(credentials)) {
                credentials = null;
                continue;
            }

            if (startupAction == null || file == null || credentials == null) {
                startupAction = null;
                file = null;
                credentials = null;
            } else {
                break;
            }
        }
    }

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

    private void showFileChooser() {
        FileChooser chooser = new FileChooser();

        if (startupAction == InventoryAction.CREATE) {
            file = chooser.showSaveDialog(stage);
        } else if (startupAction == InventoryAction.OPEN) {
            file = chooser.showOpenDialog(stage);
        }
    }

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

    private boolean openInventoryFile(File file) {
        boolean success;
        boolean first = true;

        do {
            Credentials credentials = createUnlockCredentialsDialog(!first)
                .showAndWait()
                .filter(r -> !r.wasCanceled())
                .map(r -> r.getCredentials())
                .orElse(null);

            if (credentials == null) {
                return false;
            }

            success = ohte.domain.Application.getSingleton()
                .openInventory(file.getAbsolutePath(), credentials);

            first = false;
        } while (!success);

        return true;
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
