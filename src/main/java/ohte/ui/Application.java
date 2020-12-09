package ohte.ui;

import java.io.IOException;
import java.io.File;
import java.util.NoSuchElementException;
import java.util.Optional;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Parent;

import ohte.domain.Credentials;

/**
 * Manages the startup UI flow and launches the main UI.
 */
public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) {
        startupFlow();
        showMainView(stage);
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

        do {
            WelcomeDialog dialog = new WelcomeDialog();

            result = dialog.showAndWait().orElse(null);

            if (result == null) {
                return;
            }
        } while (result.getFile() == null);

        boolean operationCompleted = false;

        if (result.getType() == InventoryAction.Type.CREATE) {
            operationCompleted = createInventoryFile(result.getFile());
        } else if (result.getType() == InventoryAction.Type.OPEN) {
            operationCompleted = openInventoryFile(result.getFile());
        }

        if (operationCompleted) {
            startupFlow();
        }
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

            System.out.println(credentials.wasCanceled());

            success = ohte.domain.Application.getSingleton()
                .openInventory(file.getAbsolutePath(), credentials);

            first = false;
        } while (!success);

        return true;
    }

    private boolean createInventoryFile(File file) {
        CredentialsDialog invDialog = new CredentialsDialog("Create superuser");

        Label message = new Label(String.format(
            "Creating new inventory file %s.\nPlease give credentials " +
            "for creating a superuser account:",
            file.getName()
        ));

        message.setWrapText(true);

        invDialog
            .getMessagePane()
            .getChildren()
            .add(message);

        Credentials credentials = invDialog.showAndWait()
            .filter(result -> !result.wasCanceled())
            .map(result -> result.getCredentials())
            .orElse(null);

        if (credentials == null) {
            return false;
        }

        ohte.domain.Application.getSingleton()
            .createInventory(file.getAbsolutePath(), credentials);

        return true;
    }

    /**
     * Creates and displays {@link MainViewController the main view}.
     */
    private void showMainView(Stage stage) {
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
