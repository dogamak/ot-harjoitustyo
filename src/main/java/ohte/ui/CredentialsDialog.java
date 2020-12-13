package ohte.ui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import ohte.domain.Credentials;

/**
 * Dialog for prompting for a username and password pair.
 */
public class CredentialsDialog extends Dialog<CredentialsDialog.Result> {
    /**
     * Button type for the "Create" button.
     */
    private ButtonType primaryButton = new ButtonType("Create");

    /**
     * Button type for the "Cancel" button.
     */
    private ButtonType cancelButton = new ButtonType("Cancel");

    /**
     * Reference to the FXML controller instance.
     */
    private Controller controller;

    /**
     * Class for the result returned by this dialog.
     */
    public static class Result {
        /**
         * Whether the user selected "Cancel" on the dialog.
         */
        private boolean canceled = false;

        /**
         * The credentials provided by the user.
         */
        private Credentials credentials;

        Result(boolean canceled, Credentials credentials) {
            this.canceled = canceled;
            this.credentials = credentials;
        }

        /**
         * Whether the user selected "Cancel" on the dialog.
         *
         * @return {@code True} if user canceled the dialog
         */
        public boolean wasCanceled() {
            return canceled;
        }

        /**
         * The credentials provided by the user.
         *
         * @return Credentials-object containing the credentials entered by the user
         */
        public Credentials getCredentials() {
            return credentials;
        }
    }

    /**
     * FXML controller for the dialog.
     */
    public static class Controller {
        /**
         * Reference to the message pane. Populated by FXML.
         *
         * This reference is exposed to the user of the dialog
         * for displaying custom messages.
         */
        @FXML
        private Pane messagePane;

        /**
         * Reference to the username field.
         * Populated by FXML.
         */
        @FXML
        private TextField usernameField;

        /**
         * Reference to the password field.
         * Populated by FXML.
         */
        @FXML
        private PasswordField passwordField;

        /**
         * Returns a reference to the message pane.
         */
        Pane getMessagePane() {
            return messagePane;
        }

        /**
         * Returns the credentials entered by the user.
         */
        Credentials getCredentials() {
            return new Credentials(usernameField.getText(), passwordField.getText());
        }
    }

    /**
     * Creates a new dialog with a custom label for the primary button.
     *
     * @param primaryButtonLabel A custom label for the primary button.
     */
    public CredentialsDialog(String primaryButtonLabel) {
        primaryButton = new ButtonType(primaryButtonLabel);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader()
                    .getResource("ui/AccountCreationDialog.fxml"));

            Parent root = loader.load();
            controller = loader.getController();

            getDialogPane().setContent(root);

            setResultConverter(button -> {
                boolean canceled = button == cancelButton;
                return new Result(canceled, controller.getCredentials());
            });
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        getDialogPane()
            .getButtonTypes()
            .addAll(primaryButton, cancelButton);
    }

    /**
     * Get a reference to the dialogs message pane.
     * Custom messages can be displayed by populating this pane.
     *
     * @return Reference to the message pane
     */
    public Pane getMessagePane() {
        return controller.getMessagePane();
    }
}
