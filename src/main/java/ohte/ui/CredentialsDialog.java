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

public class CredentialsDialog extends Dialog<CredentialsDialog.Result> {
  private ButtonType primaryButton = new ButtonType("Create");
  private ButtonType cancelButton = new ButtonType("Cancel");

  private Controller controller;

  public static class Result {
    private boolean canceled = false;
    private Credentials credentials;

    Result(boolean canceled, Credentials credentials) {
      this.canceled = canceled;
      this.credentials = credentials;
    }

    public boolean wasCanceled() {
      return canceled;
    }

    public Credentials getCredentials() {
      return credentials;
    }
  }

  public static class Controller {
    @FXML
    private Pane messagePane;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    Pane getMessagePane() {
      return messagePane;
    }

    Credentials getCredentials() {
      return new Credentials(usernameField.getText(), passwordField.getText());
    }
  }

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

  public Pane getMessagePane() {
    return controller.getMessagePane();
  }
}
