package ohte.ui;

import java.io.File;
import java.io.IOException;

import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import ohte.domain.Credentials;
import ohte.ui.InventoryAction;
import ohte.ui.InventoryAction.Type;

/**
 * The dialog displayed immediately after choosing the startup action from {@link WelcomeDialog}.
 * Prompts the user for username and password, and displays information about the inventory file.
 */
public class InventoryDialog extends Dialog<Credentials> {
  private static ButtonType CREATE_BUTTON = new ButtonType("Create");
  private static ButtonType OPEN_BUTTON = new ButtonType("Open");
  private static ButtonType CANCEL_BUTTON = new ButtonType("Cancel");

  /**
   * Controller tied to the FXML document.
   */
  public static class Controller {
    /** Label component for displaying the inventory files path. */
    @FXML
    private Label filepathLabel;

    /** The username input field. */
    @FXML
    private TextField usernameField;

    /** The password input field. */
    @FXML
    private PasswordField passwordField;

    /**
     * Path of the inventory file.
     *
     * Stored temporarily here, because the text for {@link #filepathLabel} can
     * be set only after construction, when {@link #initialize} is called.
     */
    String filepath;

    public Controller(String path) {
      filepath = path;
    }

    /**
     * Populates the dialog with information.
     * 
     * Called after the UI has been properly initialied and the FXML-linked properties
     * of this instance  have been populated.
     */
    @FXML
    private void initialize() {
      filepathLabel.setText(filepath);
    }

    Credentials getCredentials() {
      return new Credentials(usernameField.getText(), passwordField.getText());
    }
  }

  public InventoryDialog(InventoryAction.Type type, File file) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getClassLoader()
          .getResource("ui/InventoryDialog.fxml"));

      // Because we want to pass data to the controller via it's constructor,
      // we need to use setController and leave the "fx:controller"-property
      // empty in the XFML-file.
      Controller controller = new Controller(file.getAbsolutePath());
      loader.setController(controller);

      getDialogPane().setContent(loader.load());

      setResultConverter(button -> convertResult(controller, type, button));
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }


    ButtonType primaryButtonType = null;

    if (type == Type.CREATE)
      primaryButtonType = CREATE_BUTTON;
    else if (type == Type.OPEN)
      primaryButtonType = OPEN_BUTTON;

    getDialogPane().getButtonTypes()
      .addAll(primaryButtonType, CANCEL_BUTTON);
  }

  private static Credentials convertResult(
    Controller controller,
    InventoryAction.Type actionType,
    ButtonType buttonType
  ) {
    return controller.getCredentials();
  }
}
